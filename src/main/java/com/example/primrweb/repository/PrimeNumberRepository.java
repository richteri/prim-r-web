package com.example.primrweb.repository;

import com.example.primrweb.domain.PrimeNumber;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrimeNumberRepository extends JpaRepository<PrimeNumber, Long> {

    Optional<PrimeNumber> findByNumber(Long number);

}
