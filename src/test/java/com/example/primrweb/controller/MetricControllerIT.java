package com.example.primrweb.controller;

import static com.example.primrweb.controller.MetricController.ENDPOINT;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.primrweb.AbstractMockMvcIT;
import com.example.primrweb.domain.PrimeNumber;
import com.example.primrweb.repository.PrimeNumberRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MetricControllerIT extends AbstractMockMvcIT {

    private static final long SAMPLE_NUMBER = 42L;
    private static final String SAMPLE_STRING = String.valueOf(SAMPLE_NUMBER);

    @Autowired
    private PrimeNumberRepository repository;

    @Test
    public void getShouldReturnMetricsWithEmptyQueuesAndCache() throws Exception {
        mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jobQueue").isEmpty())
                .andExpect(jsonPath("$.processingQueue").isEmpty())
                .andExpect(jsonPath("$.primeNumberCache").isEmpty());
    }

    @Test
    public void getShouldReturnQueuedSearch() throws Exception {
        // Arrange
        mockMvc.perform(get(PrimeNumberController.ENDPOINT)
                .param(PrimeNumberController.NUMBER_PARAM, SAMPLE_STRING))
                .andExpect(status().isAccepted());

        // Act & Assert
        mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jobQueue", hasSize(1)))
                .andExpect(jsonPath("$.jobQueue[0]").value(SAMPLE_NUMBER))
                .andExpect(jsonPath("$.primeNumberCache").isEmpty());
    }

    @Test
    public void getShouldReturnCachedNumber() throws Exception {
        // Arrange
        repository.save(new PrimeNumber()
                .setNumber(SAMPLE_NUMBER)
                .setPrime(SAMPLE_NUMBER));

        mockMvc.perform(get(PrimeNumberController.ENDPOINT)
                .param(PrimeNumberController.NUMBER_PARAM, SAMPLE_STRING))
                .andExpect(status().isOk());

        // Act & Assert
        mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jobQueue").isEmpty())
                .andExpect(jsonPath("$.primeNumberCache", hasSize(1)))
                .andExpect(jsonPath("$.primeNumberCache[0]")
                        .value(properties.getCacheName() + "::" + SAMPLE_STRING));
    }
}
