package net.lanet.vollmed.infra.others;

import org.springframework.cloud.context.restart.RestartEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestartConfiguration {
    @Bean
    public RestartEndpoint restartEndpoint() {
        return new RestartEndpoint();
    }
}
