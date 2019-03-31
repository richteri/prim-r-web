package com.example.primrweb.service;

import java.util.List;

public interface QueueService {

    void addToQueue(Long number);

    List<Long> getJobQueueSnapshot();

    List<Long> getProcessingQueueSnapshot();
}
