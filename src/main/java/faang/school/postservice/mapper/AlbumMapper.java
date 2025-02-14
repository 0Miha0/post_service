package faang.school.postservice.mapper;

import faang.school.postservice.dto.album.AlbumDto;
import faang.school.postservice.model.Album;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AlbumMapper {

    AlbumDto toDto(Album album);

    Album toEntity(AlbumDto dto);

    List<AlbumDto> toDtoList(List<Album> album);
}
