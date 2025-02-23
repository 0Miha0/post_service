package faang.school.postservice.service.minio;

import faang.school.postservice.exception.custom.MinioException;
import faang.school.postservice.properties.MinioProperties;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    public void uploadFile(String fileName, byte[] data, String contentType) {
        log.info("Uploading file {} to bucket {} with object name {}", contentType, minioProperties.getBucketName(), fileName);
        try (InputStream inputStream = new ByteArrayInputStream(data)) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioProperties.getBucketName())
                            .object(fileName)
                            .stream(inputStream, data.length, -1)
                            .contentType(contentType)
                            .build()
            );
        } catch (Exception e) {
            log.warn("Error uploading file to MinIO: {}", e.getMessage(), e);
            throw new MinioException("Error uploading file to MinIO: " + e.getMessage());
        }
    }

    public InputStream downloadFile(String objectKey) {
        log.info("Downloading file with key {}", objectKey);
        try {
            InputStream inputStream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(minioProperties.getBucketName())
                            .object(objectKey)
                            .build()
            );
            log.info("File {} downloaded successfully", objectKey);
            return inputStream;
        } catch (Exception e) {
            log.warn("Error downloading file {}", objectKey, e);
            throw new MinioException(String.format("Error downloading file from Minio: %s", objectKey), e);
        }
    }

    public void deleteFile(String objectKey) {
        log.info("Deleting file with key {}", objectKey);
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(minioProperties.getBucketName())
                            .object(objectKey)
                            .build()
            );
            log.info("File {} deleted successfully", objectKey);
        } catch (Exception e) {
            log.warn("Error deleting file {}", objectKey, e);
            throw new MinioException(String.format("Error deleting file from Minio: %s", objectKey), e);
        }
    }
}
