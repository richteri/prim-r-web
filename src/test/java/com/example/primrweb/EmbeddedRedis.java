package com.example.primrweb;

import static com.example.primrweb.TestUtils.SPRING_REDIS_PORT_KEY;
import static com.example.primrweb.TestUtils.SPRING_PROFILE_NAME;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import redis.embedded.RedisServer;

@Profile(SPRING_PROFILE_NAME)
@Component
public class EmbeddedRedis {

    @Value(SPRING_REDIS_PORT_KEY)
    private int redisPort;

    private RedisServer redisServer;

    @PostConstruct
    public void startRedis() {
        redisServer = RedisServer.builder()
                .port(redisPort)
                // maxheap 1gb must be added on windows 10
                .build();
        redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {
        redisServer.stop();
    }
}
