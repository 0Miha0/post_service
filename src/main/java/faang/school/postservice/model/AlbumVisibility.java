package faang.school.postservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "album_visibility")
@IdClass(AlbumVisibilityId.class)
public class AlbumVisibility {

    @Id
    @ManyToOne
    @JoinColumn(name = "album_id", nullable = false)
    private Album album;

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;
}
