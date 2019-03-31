package com.example.primrweb.service;

import com.example.primrweb.domain.PrimeNumber;
import java.util.Optional;

public interface PrimeNumberService {

    Optional<PrimeNumber> findByNumber(Long number);
}
