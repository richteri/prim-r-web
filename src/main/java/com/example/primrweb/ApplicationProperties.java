package com.example.primrweb;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("application")
public class ApplicationProperties {

    private String jobQueueKey;
    private String processingQueueKey;

    // TODO Use configurable cache name using CacheResolver
    private String cacheName;
}
