package com.example.primrweb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import redis.embedded.RedisServer;

@Profile("test")
@Component
public class EmbeddedRedis {

    private int redisPort;

    private RedisServer redisServer;

    @PostConstruct
    public void startRedis() throws IOException {
        Path myTempDir = Files.createTempDirectory("redis");
        redisServer = RedisServer.builder()
                .port(16379)
                .setting("maxheap 1gb")
                .setting("heapdir " + myTempDir.toAbsolutePath().toString())
                .build();
        redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {
        redisServer.stop();
    }
}
