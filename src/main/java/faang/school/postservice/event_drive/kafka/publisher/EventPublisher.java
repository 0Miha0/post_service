package faang.school.postservice.event_drive.kafka.publisher;

public interface EventPublisher<T>{

    void publish(T event);
}