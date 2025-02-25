package faang.school.postservice.mapper;

import faang.school.postservice.dto.ad.AdDto;
import faang.school.postservice.model.ad.Ad;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdMapper {

    @Mapping(target = "post.id", source = "post")
    Ad toEntity(AdDto dto);

    @Mapping(target = "post", source = "post.id")
    AdDto toDto(Ad entity);
}
