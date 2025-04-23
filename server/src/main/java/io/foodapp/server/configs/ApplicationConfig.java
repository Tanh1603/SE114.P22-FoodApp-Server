package io.foodapp.server.configs;

import io.foodapp.server.services.AuditorAwareService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class ApplicationConfig {
    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareService();
    }
}
