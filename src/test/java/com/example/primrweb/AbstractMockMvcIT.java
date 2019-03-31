package com.example.primrweb;

import static com.example.primrweb.TestUtils.SPRING_PROFILE_NAME;
import static org.springframework.http.HttpHeaders.ORIGIN;
import static org.springframework.http.MediaType.ALL;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.Rule;
import org.junit.rules.ErrorCollector;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcBuilderCustomizer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles(SPRING_PROFILE_NAME)
@Import(AbstractMockMvcIT.TestConfig.class)
public abstract class AbstractMockMvcIT extends AbstractTransactionalJUnit4SpringContextTests {

    @Rule
    public final ErrorCollector collector = new ErrorCollector();

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
