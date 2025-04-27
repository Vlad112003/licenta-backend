package com.elearning.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    ///
    /// RestTemplate Bean
    /// params: None
    /// return: RestTemplate
    /// description: This method creates a RestTemplate bean for making HTTP requests.
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
