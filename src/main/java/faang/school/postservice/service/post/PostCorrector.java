package faang.school.postservice.service.post;

import faang.school.postservice.properties.TextGearsProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class PostCorrector {

    private final WebClient webClient;
    private final TextGearsProperties textGearsProperties;

    @Autowired
    public PostCorrector(@Qualifier("textGearsClient") WebClient webClient, TextGearsProperties textGearsProperties) {
        this.webClient = webClient;
        this.textGearsProperties = textGearsProperties;
    }

    public Mono<String> correctPost(String content) {
        log.info("Correcting post content: {}", content);
        return webClient.post()
                .uri(textGearsProperties.getBaseUrl())
                .bodyValue("text=" + content)
                .retrieve()
                .bodyToMono(String.class);
    }
}
