package faang.school.postservice.config.webclient;

import faang.school.postservice.properties.TextGearsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient textGearsClient(TextGearsProperties properties) {
        return WebClient.builder()
                .baseUrl(properties.getBaseUrl())
                .defaultHeader("x-rapidapi-key", properties.getXRapidKey())
                .defaultHeader("x-rapidapi-host", properties.getXRapidHost())
                .defaultHeader("Content-Type", properties.getContentType())
                .build();
    }
}
