package faang.school.postservice.mapper;

import faang.school.postservice.dto.post.PostDto;
import faang.school.postservice.event_drive.kafka.event.KafkaPostDto;
import faang.school.postservice.event_drive.kafka.event.KafkaPostViewDto;
import faang.school.postservice.model.Album;
import faang.school.postservice.model.Comment;
import faang.school.postservice.model.Like;
import faang.school.postservice.model.Post;
import faang.school.postservice.model.Resource;
import faang.school.postservice.model.redis.CachePost;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PostMapper {

    PostDto toDto(Post entity);

    Post toEntity(PostDto dto);

    List<PostDto> toDtoList(List<Post> entities);

    KafkaPostDto toKafkaPostDto(Post entity);

    @Mapping(source = "id", target = "postId")
    KafkaPostViewDto toKafkaPostViewDto(PostDto dto);

    @Mapping(source = "likes", target = "likeIds", qualifiedByName = "mapLikes")
    @Mapping(source = "comments", target = "commentIds", qualifiedByName = "mapComments")
    @Mapping(source = "ad.id", target = "adId")
    @Mapping(source = "resources", target = "resourceIds", qualifiedByName = "mapResources")
    @Mapping(target = "numLikes", expression = "java(mapLikesToNumLikes(entity.getLikes()))")
    CachePost toCachePost(Post entity);

    @Named("mapLikes")
    default List<Long> mapLikesToLikeIds(List<Like> likes) {
        if (likes == null) {
            likes = new ArrayList<>();
        }
        return likes.stream()
                .map(Like::getId)
                .toList();
    }

    @Named("mapComments")
    default List<Long> mapCommentsToCommentIds(List<Comment> comments) {
        if (comments == null) {
            comments = new ArrayList<>();
        }
        return comments.stream()
                .map(Comment::getId)
                .toList();
    }

    @Named("mapAlbums")
    default List<Long> mapAlbumsToAlbumsIds(List<Album> albums) {
        if (albums == null) {
            albums = new ArrayList<>();
        }
        return albums.stream()
                .map(Album::getId)
                .toList();
    }

    @Named("mapResources")
    default List<Long> mapResourcesToResourceIds(List<Resource> resources) {
        if (resources == null) {
            resources = new ArrayList<>();
        }
        return resources.stream()
                .map(Resource::getId)
                .toList();
    }

    default Long mapLikesToNumLikes(List<Like> likes) {
        if (likes == null) return 0L;
        return ((long) likes.size());
    }
}
