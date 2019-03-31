package com.example.primrweb;

import static com.example.primrweb.TestUtils.SPRING_PROFILE_NAME;
import static org.springframework.http.HttpHeaders.ORIGIN;
import static org.springframework.http.MediaType.ALL;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.example.primrweb.domain.PrimeNumber;
import lombok.val;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ErrorCollector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcBuilderCustomizer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles(SPRING_PROFILE_NAME)
@Import(AbstractMockMvcIT.TestConfig.class)
public abstract class AbstractMockMvcIT extends AbstractTransactionalJUnit4SpringContextTests {

    @Rule
    public final ErrorCollector collector = new ErrorCollector();

    @Autowired
    protected RedisTemplate<String, Long> redisTemplate;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ApplicationProperties properties;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Before
    public void setUp() {
        redisTemplate.opsForList().trim(properties.getJobQueueKey(), 0, 0);
        redisTemplate.opsForList().trim(properties.getProcessingQueueKey(), 0, 0);

        val redisConnection = redisConnectionFactory.getConnection();
        redisConnection.flushAll();
        redisConnection.close();
    }

    static class TestConfig {

        @Bean
        public MockMvcBuilderCustomizer defaultMockMvcBuilderCustomizer() {
            return builder -> {
                builder.defaultRequest(get("")
                        .contentType(APPLICATION_JSON)
                        .header(ORIGIN, "test")
                        .accept(APPLICATION_JSON, TEXT_PLAIN, ALL));
                builder.alwaysDo(print());
            };
        }

    }
}
