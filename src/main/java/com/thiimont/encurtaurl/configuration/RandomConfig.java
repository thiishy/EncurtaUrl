package com.thiimont.encurtaurl.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.SecureRandom;

@Configuration
public class RandomConfig {
    @Bean
    public SecureRandom secureRandom() {
        return new SecureRandom();
    }
}
