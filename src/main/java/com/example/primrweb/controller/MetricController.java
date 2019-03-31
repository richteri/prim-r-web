package com.example.primrweb.controller;

import static com.example.primrweb.controller.MetricController.ENDPOINT;

import com.example.primrweb.ApplicationProperties;
import com.example.primrweb.service.QueueService;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ENDPOINT)
@RequiredArgsConstructor
public class MetricController {

    static final String ENDPOINT = "/metrics";
    static final String FLUSH_ALL_ENDPOINT = "/flush-all";

    static final String STATUS_KEY = "status";
    static final String STATUS_UP = "UP";
    static final String GKE_KEY = "gke";

    private final QueueService queueService;
    private final ApplicationProperties properties;
    private final RedisConnectionFactory redisConnectionFactory;

    @GetMapping
    public Map<String, Object> get() {
        val map = new HashMap<String, Object>();
        map.put(STATUS_KEY, STATUS_UP);
        map.put(GKE_KEY, properties.getGke());
        map.put(properties.getJobQueueKey(), queueService.getJobQueueSnapshot());
        map.put(properties.getProcessingQueueKey(), queueService.getProcessingQueueSnapshot());
        map.put(properties.getCacheName(), getCacheKeys());
        return map;
    }

    @GetMapping(FLUSH_ALL_ENDPOINT)
    public void flushAll() {
        RedisConnection redisConnection = null;
        try {
            redisConnection = redisConnectionFactory.getConnection();
            redisConnection.flushAll();
        } finally {
            if (redisConnection != null) {
                redisConnection.close();
            }
        }
    }

    private Set<String> getCacheKeys() {
        RedisConnection connection = null;
        try {
            connection = redisConnectionFactory.getConnection();
            val redisKeys = connection.keys((properties.getCacheName() + "*").getBytes());
            return redisKeys == null ? Collections.emptySet() : redisKeys.stream()
                    .map(data -> new String(data, 0, data.length))
                    .collect(Collectors.toSet());
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

}
