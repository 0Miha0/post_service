package faang.school.postservice.event_drive.redis.event;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentEvent {

    @NotNull
    private Long commentId;

    @NotNull
    private Long actorId;

    @NotNull
    private LocalDateTime receivedAt;
}

