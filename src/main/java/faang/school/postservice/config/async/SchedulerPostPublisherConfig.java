package faang.school.postservice.config.async;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class SchedulerPostPublisherConfig {

    @Value("${async.publishScheduledPosts.corePoolSize}")
    private int corePoolSize;

    @Value("${async.publishScheduledPosts.maxPoolSize}")
    private int maxPoolSize;

    @Value("${async.publishScheduledPosts.queueCapacity}")
    private int queueCapacity;

    @Bean(name = "scheduledPostPublish")
    public ThreadPoolTaskExecutor scheduledPostPublish() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("PremiumUserDeletionAsync-");
        executor.initialize();
        return executor;
    }
}
