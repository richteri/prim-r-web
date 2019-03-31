package com.example.primrweb.config;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TestConstants {

    public static final String SPRING_PROFILE_NAME = "test";
    public static final String SPRING_REDIS_PORT_KEY = "${spring.redis.port}";

}
