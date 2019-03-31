package com.example.primrweb.service;

import static com.example.primrweb.config.CacheConfig.CACHE_NAME;

import com.example.primrweb.domain.PrimeNumber;
import com.example.primrweb.repository.PrimeNumberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrimeNumberServiceImpl implements PrimeNumberService {

    private final PrimeNumberRepository repository;

    @Cacheable(value = CACHE_NAME, unless = "#result == null")
    @Override
    public Optional<PrimeNumber> findByNumber(Long number) {
        log.info("Searching number #{} in database", number);
        return repository.findByNumber(number);
    }

}
