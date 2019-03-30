package com.example.primrweb.service;

import com.example.primrweb.ApplicationProperties;
import java.util.stream.LongStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class QueueServiceImpl implements QueueService {

    private final RedisTemplate<String, Long> redisTemplate;
    private final ApplicationProperties properties;

    @Override
    public void addToQueue(Long number) {
        if (isInQueue(properties.getJobQueueKey(), number) || isInQueue(properties.getProcessingQueueKey(), number)) {
            log.info("Ignoring number #{} as it is already a member of the queue", number);
        } else {
            log.info("Adding number #{} to the search queue", number);
            redisTemplate.opsForList().leftPush(properties.getJobQueueKey(), number);
        }
    }

    // TODO Non-atomic operation, might result in duplicated tasks
    private boolean isInQueue(String key, Long number) {
        boolean inQueue = false;
        val size = redisTemplate.opsForList().size(key);
        if (size != null) {
            inQueue = LongStream
                    .range(0, size.intValue())
                    .anyMatch(i -> number.equals(redisTemplate.opsForList().index(key, i)));
        }
        return inQueue;
    }
}
