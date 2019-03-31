package com.example.primrweb;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("application")
public class ApplicationProperties {

    private String jobQueueKey;
    private String processingQueueKey;

    // TODO PRIMRWEB-1 Use configurable cache name using CacheResolver
    private String cacheName;

    private Gke gke;

    @Data
    public static class Gke {
        private String nodeName;
        private String podName;
        private String podNamespace;
        private String podIp;
        private String podServiceAccount;
    }
}
