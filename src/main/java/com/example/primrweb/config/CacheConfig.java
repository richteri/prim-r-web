package com.example.primrweb.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    // TODO PRIMRWEB-1 Use configurable cache name using CacheResolver
    public static final String CACHE_NAME = "primeNumberCache";
}
