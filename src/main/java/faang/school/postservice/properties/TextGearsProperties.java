package faang.school.postservice.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "text-gears")
public class TextGearsProperties {

    private String baseUrl;
    private String xRapidKey;
    private String xRapidHost;
    private String contentType;
}
