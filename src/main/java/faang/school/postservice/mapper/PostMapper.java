package faang.school.postservice.mapper;

import faang.school.postservice.dto.post.PostDto;
import faang.school.postservice.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {

//    @Mapping(target = "ad", source = "ad.id")
    PostDto toDto(Post entity);

//    @Mapping(source = "ad", target = "ad.id")
    Post toEntity(PostDto dto);

//    @Mapping(target = "ad", source = "ad.id")
    List<PostDto> toDtoList(List<Post> entities);
}
