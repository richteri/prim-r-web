package com.example.primrweb.controller;

import static com.example.primrweb.config.CacheConfig.CACHE_NAME;
import static com.example.primrweb.controller.PrimeNumberController.ENDPOINT;
import static com.example.primrweb.controller.PrimeNumberController.NUMBER_PARAM;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.primrweb.AbstractMockMvcIT;
import com.example.primrweb.ApplicationProperties;
import com.example.primrweb.domain.PrimeNumber;
import com.example.primrweb.repository.PrimeNumberRepository;
import com.example.primrweb.service.PrimeNumberService;
import lombok.val;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.web.servlet.MockMvc;

public class PrimeNumberControllerIT extends AbstractMockMvcIT {

    private static final long SAMPLE_NUMBER = 2L;
    private static final String SAMPLE_NUMBER_PARAM = "2";
    private static final long SAMPLE_PRIME = 3L;
    private static final PrimeNumber SAMPLE_PRIME_NUMBER = new PrimeNumber()
            .setNumber(SAMPLE_NUMBER)
            .setPrime(SAMPLE_PRIME);

    private static final String PRIME_PATH = "$.prime";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ApplicationProperties properties;

    @Autowired
    private RedisTemplate<String, Long> redisTemplate;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private PrimeNumberRepository repository;

    @Autowired
    private PrimeNumberService service;

    @Before
    public void setUp() {
        redisTemplate.opsForList().trim(properties.getJobQueueKey(), 0, 0);
        redisTemplate.opsForList().trim(properties.getProcessingQueueKey(), 0, 0);
        cacheManager.getCache(CACHE_NAME).clear();
    }

    @Test
    public void findByNumberShouldReturn200IfPresentInDb() throws Exception {
        // Arrange
        repository.save(new PrimeNumber()
                .setNumber(SAMPLE_NUMBER)
                .setPrime(SAMPLE_PRIME));

        // Act & Assert
        mockMvc.perform(get(ENDPOINT).param(NUMBER_PARAM, SAMPLE_NUMBER_PARAM))
                .andExpect(status().isOk())
                .andExpect(jsonPath(PRIME_PATH).value(SAMPLE_PRIME));
    }

    @Test
    public void findByNumberShouldReturn200IfPresentInCache() throws Exception {
        // Arrange
        cacheManager.getCache(CACHE_NAME).put(SAMPLE_NUMBER, SAMPLE_PRIME_NUMBER);

        // Act & Assert
        mockMvc.perform(get(ENDPOINT).param(NUMBER_PARAM, SAMPLE_NUMBER_PARAM))
                .andExpect(status().isOk())
                .andExpect(jsonPath(PRIME_PATH).value(SAMPLE_PRIME));

    }

    @Test
    public void findByNumberShouldAddToQueueAndReturn202IfNotFound() throws Exception {
        // Arrange
        mockMvc.perform(get(ENDPOINT).param(NUMBER_PARAM, SAMPLE_NUMBER_PARAM))
                .andExpect(status().isAccepted());

        // Act & Assert
        val number = redisTemplate.opsForList().index(properties.getJobQueueKey(), 0);
        collector.checkThat(number, is(SAMPLE_NUMBER));
    }

}
