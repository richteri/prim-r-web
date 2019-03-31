package com.example.primrweb.service;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.primrweb.AbstractTest;
import com.example.primrweb.domain.PrimeNumber;
import com.example.primrweb.repository.PrimeNumberRepository;
import java.util.Optional;
import lombok.val;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class PrimeNumberServiceImplTest extends AbstractTest {

    private static final long SAMPLE_NUMBER = 42L;
    private static final PrimeNumber SAMPLE_PRIME_NUMBER = new PrimeNumber()
            .setId(SAMPLE_NUMBER)
            .setNumber(SAMPLE_NUMBER)
            .setPrime(SAMPLE_NUMBER);

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private static final Optional<PrimeNumber> SAMPLE_OPTIONAL = Optional.of(SAMPLE_PRIME_NUMBER);

    @Mock
    private PrimeNumberRepository repository;

    @InjectMocks
    private PrimeNumberServiceImpl service;

    @Test
    public void findByNumberShouldDelegateToRepository() {
        // Arrange
        when(repository.findByNumber(SAMPLE_NUMBER)).thenReturn(SAMPLE_OPTIONAL);

        // Act
        val actual = service.findByNumber(SAMPLE_NUMBER);

        // Assert
        collector.checkThat(actual, is(SAMPLE_OPTIONAL));
        verify(repository).findByNumber(SAMPLE_NUMBER);
    }

    @Test
    public void saveShouldDelegateToRepository() {
        // Arrange
        when(repository.save(SAMPLE_PRIME_NUMBER)).thenReturn(SAMPLE_PRIME_NUMBER);

        // Act
        val actual = service.save(SAMPLE_PRIME_NUMBER);

        // Assert
        collector.checkThat(actual, is(SAMPLE_PRIME_NUMBER));
        verify(repository).save(SAMPLE_PRIME_NUMBER);
    }
}
