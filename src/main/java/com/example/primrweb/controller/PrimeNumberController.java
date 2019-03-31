package com.example.primrweb.controller;

import static com.example.primrweb.controller.PrimeNumberController.ENDPOINT;

import com.example.primrweb.domain.PrimeNumber;
import com.example.primrweb.service.PrimeNumberService;
import com.example.primrweb.service.QueueService;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ENDPOINT)
@RequiredArgsConstructor
@Validated
@Slf4j
public class PrimeNumberController {

    static final String ENDPOINT = "/prime-numbers";
    static final String NUMBER_PARAM = "number";

    private final PrimeNumberService primeNumberService;
    private final QueueService queueService;

    @GetMapping
    public ResponseEntity<PrimeNumber> findByNumber(@RequestParam(NUMBER_PARAM) @Min(1) Long number) {
        log.info("Searching number #{} in cache", number);
        val primeNumber = primeNumberService.findByNumber(number);
        if (primeNumber.isPresent()) {
            return ResponseEntity
                    .ok(primeNumber.get());
        } else {
            queueService.addToQueue(number);
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .build();
        }
    }

}
