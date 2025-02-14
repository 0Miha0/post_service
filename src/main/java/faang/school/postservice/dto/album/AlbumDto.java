package faang.school.postservice.dto.album;

import faang.school.postservice.model.enums.VisibilityAlbums;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class AlbumDto {

    @NotNull
    @Size(min = 1, max = 256)
    private String title;

    @NotNull
    @Size(min = 1, max = 4096)
    private String description;

    @NotNull
    private VisibilityAlbums visibilityAlbums;
}
