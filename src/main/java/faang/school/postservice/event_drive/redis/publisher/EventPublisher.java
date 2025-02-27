package faang.school.postservice.event_drive.redis.publisher;

public interface EventPublisher<T> {

    void publish(T event);
}
