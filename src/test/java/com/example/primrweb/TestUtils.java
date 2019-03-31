package com.example.primrweb;

import lombok.experimental.UtilityClass;

@UtilityClass
class TestUtils {

    static final String SPRING_PROFILE_NAME = "test";
    static final String SPRING_REDIS_PORT_KEY = "${spring.redis.port}";

}
