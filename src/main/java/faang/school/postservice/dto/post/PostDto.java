package faang.school.postservice.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    private Long id;
    @NotBlank(message = "Content is not empty")
    private String content;
    private Long authorId;
    private Long projectId;
    private List<Long> likeIds;
    private List<Long> commentIds;
    private List<Long> albumIds;
    private Long adId;
    private List<Long> resourceIds;
    private boolean published;
    private LocalDateTime publishedAt;
    private LocalDateTime scheduledAt;
    private boolean deleted;
    private Long numLikes;
}