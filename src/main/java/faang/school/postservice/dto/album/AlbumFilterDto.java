package faang.school.postservice.dto.album;

import faang.school.postservice.dto.filter.FilterDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlbumFilterDto implements FilterDto {

    private String title;

    private Long authorId;
}
