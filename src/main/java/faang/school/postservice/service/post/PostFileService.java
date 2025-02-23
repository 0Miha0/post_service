package faang.school.postservice.service.post;

import faang.school.postservice.dto.resource.ResourceDto;
import faang.school.postservice.exception.custom.FileUploadException;
import faang.school.postservice.model.Post;
import faang.school.postservice.model.Resource;
import faang.school.postservice.service.ResourcesService;
import faang.school.postservice.service.file_streaming.FileStreamingService;
import faang.school.postservice.service.minio.MinioService;
import faang.school.postservice.validation.service_validator.PostValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostFileService {

    private final PostValidator postValidator;
    private final MinioService minioService;
    private final ResourcesService resourcesService;
    private final PostService postService;
    private final FileStreamingService fileStreamingService;

    public void addFileToPost(Long postId, MultipartFile[] files, Long userId) {
        log.info("Adding files to post {}", postId);
        Post post = postService.findById(postId);
        postValidator.isUserTheAuthor(userId, post.getAuthorId());

        String folder = postId + "/";
        for (MultipartFile file : files) {
            try {
                postValidator.nonvalizesLimiter(file.getSize());

                String key = String.format("%s/%d%s", folder, System.currentTimeMillis(), file.getOriginalFilename());

                byte[] optimizedImage = optimizeImage(file);

                resourcesService.createResource(
                        ResourceDto.builder()
                                .key(key)
                                .size((long) optimizedImage.length)
                                .name(file.getOriginalFilename())
                                .type(file.getContentType())
                                .post(postId)
                                .build()
                );

                minioService.uploadFile(key, optimizedImage, file.getContentType());
                log.info("File {} added to post {}", file.getOriginalFilename(), postId);
            } catch (IOException e) {
                log.warn("Failed to upload file {} to post {}: {}", file.getOriginalFilename(), postId, e.getMessage());
                throw new FileUploadException("Failed to upload file", e);
            }
        }
        log.info("All files added to post {}", postId);
    }

    @Transactional
    public void removeFileFromPost(Long postId, Long resourceId) {
        log.info("Attempting to remove file from post {} by resource id {}", postId, resourceId);
        Post post = postService.findById(postId);
        Resource deletedResource = resourcesService.findById(resourceId);

        postValidator.validateResourceExists(deletedResource, post);

        post.getResources().removeIf(resource -> resource.getId().equals(resourceId));
        resourcesService.deleteById(resourceId);
        minioService.deleteFile(deletedResource.getKey());

        log.info("File successfully removed from post {}", postId);
    }

    public StreamingResponseBody downloadFileFromPost(Long postId, Long resourceId) {
        log.info("Attempting to download file from post {} by resource id {}", postId, resourceId);
        Resource resource = resourcesService.findById(resourceId);
        postValidator.validateResourceExists(resource, postService.findById(postId));
        InputStream inputStream = minioService.downloadFile(resource.getKey());
        log.info("File successfully downloaded from post {}", postId);
        return fileStreamingService.getStreamingResponseBody(inputStream);
    }

    public StreamingResponseBody downloadFilesFromPost(Long postId) {
        log.info("Attempting to download all files from post {}", postId);
        Map<String, InputStream> files = new HashMap<>();
        resourcesService.findAllByPostId(postId).forEach(resource -> {
            files.put(resource.getKey(), minioService.downloadFile(resource.getKey()));
        });
        log.info("Files successfully downloaded from post {}", postId);
        return fileStreamingService.getStreamingResponseBodyInZip(files);
    }

    private byte[] optimizeImage(MultipartFile file) throws IOException {
        log.info("Optimizing image {}", file.getOriginalFilename());
        BufferedImage image = ImageIO.read(file.getInputStream());

        if (image == null) {
            log.warn("Failed to read image data");
            throw new IOException("Failed to read image data");
        }

        int width = image.getWidth();
        int height = image.getHeight();

        if (width > 1080 || height > 1080) {
            int targetWidth = 1080;
            int targetHeight;

            if (width > height) {
                targetHeight = (int) (height * (1080.0 / width));
                if (targetHeight > 566) {
                    targetHeight = 566;
                }
            } else {
                targetHeight = 1080;
            }

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                Thumbnails.of(image)
                        .size(targetWidth, targetHeight)
                        .outputFormat("png")
                        .toOutputStream(outputStream);
                return outputStream.toByteArray();
            }
        }
        log.info("Image {} is already optimized", file.getOriginalFilename());
        return file.getBytes();
    }
}
