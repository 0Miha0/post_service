package faang.school.postservice.event_drive.redis.event;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeEvent {

    @NotNull
    private Long postId;

    @NotNull
    private Long receiverId;

    @NotNull
    private LocalDateTime receiverAt;
}
