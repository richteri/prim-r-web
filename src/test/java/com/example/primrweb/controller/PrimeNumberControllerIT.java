package com.example.primrweb.controller;

import com.example.primrweb.ApplicationProperties;
import com.example.primrweb.config.CacheConfig;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class PrimeNumberControllerIT extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ApplicationProperties properties;

    @Autowired
    private RedisTemplate<String, Long> redisTemplate;

    @Autowired
    private CacheManager cacheManager;

    @Before
    public void setUp() {
        redisTemplate.opsForList().trim(properties.getJobQueueKey(), 0, 0);
        redisTemplate.opsForList().trim(properties.getProcessingQueueKey(), 0, 0);
        cacheManager.getCache(CacheConfig.CACHE_NAME).clear();
    }

    @Test
    public void findByNumberShouldReturn200IfPresentInDb() {

    }

    @Test
    public void findByNumberShouldReturn200IfPresentInCache() {

    }

    @Test
    public void findByNumberShouldAddToQueueAndReturn202IfNotFound() {

    }

}
