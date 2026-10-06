package com.andressierra.sns.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "adapters.sns")
public record SnsProperties(
        String region,
        String topicArn) {
}
