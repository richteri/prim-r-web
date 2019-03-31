package com.example.primrweb.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.primrweb.AbstractTest;
import com.example.primrweb.domain.PrimeNumber;
import com.example.primrweb.service.PrimeNumberService;
import com.example.primrweb.service.QueueService;
import java.util.Optional;
import lombok.val;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

public class PrimeNumberControllerTest extends AbstractTest {

    private static final long SAMPLE_NUMBER = 42L;
    private static final PrimeNumber SAMPLE_PRIME_NUMBER = new PrimeNumber()
            .setId(SAMPLE_NUMBER)
            .setNumber(SAMPLE_NUMBER)
            .setPrime(SAMPLE_NUMBER);

    @Mock
    private PrimeNumberService primeNumberService;

    @Mock
    private QueueService queueService;

    @InjectMocks
    private PrimeNumberController controller;

    @Test
    public void findByNumberShouldDelegateToServiceAndReturn200IfFound() {
        when(primeNumberService.findByNumber(SAMPLE_NUMBER)).thenReturn(Optional.of(SAMPLE_PRIME_NUMBER));

        val actual = controller.findByNumber(SAMPLE_NUMBER);

        collector.checkThat(actual.getStatusCode(), is(HttpStatus.OK));
        collector.checkThat(actual.getBody(), is(SAMPLE_PRIME_NUMBER));
    }

    @Test
    public void findByNumberShouldDelegateToQueueAndReturn202IfNotFound() {
        when(primeNumberService.findByNumber(SAMPLE_NUMBER)).thenReturn(Optional.empty());

        val actual = controller.findByNumber(SAMPLE_NUMBER);

        collector.checkThat(actual.getStatusCode(), is(HttpStatus.ACCEPTED));
        collector.checkThat(actual.getBody(), nullValue());
        verify(queueService).addToQueue(SAMPLE_NUMBER);
    }
}
