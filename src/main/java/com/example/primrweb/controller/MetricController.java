package com.example.primrweb.controller;

import static com.example.primrweb.controller.MetricController.ENDPOINT;

import com.example.primrweb.ApplicationProperties;
import com.example.primrweb.service.QueueService;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ENDPOINT)
@RequiredArgsConstructor
public class MetricController {

    static final String ENDPOINT = "/metrics";

    static final String STATUS_KEY = "status";
    static final String STATUS_UP = "UP";
    static final String GKE_KEY = "gke";

    private final QueueService queueService;
    private final ApplicationProperties properties;

    @GetMapping
    public Map<String, Object> get() {
        val map = new HashMap<String, Object>();
        map.put(STATUS_KEY, STATUS_UP);
        map.put(GKE_KEY, properties.getGke());
        map.put(properties.getJobQueueKey(), queueService.getJobQueueSnapshot());
        map.put(properties.getProcessingQueueKey(), queueService.getProcessingQueueSnapshot());
        return map;
    }

}
