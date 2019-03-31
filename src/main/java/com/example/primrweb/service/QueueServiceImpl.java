package com.example.primrweb.service;

import com.example.primrweb.ApplicationProperties;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
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

    @Override
    public List<Long> getJobQueueSnapshot() {
        return getQueueSnapshot(properties.getJobQueueKey());
    }

    @Override
    public List<Long> getProcessingQueueSnapshot() {
        return getQueueSnapshot(properties.getProcessingQueueKey());
    }

    private boolean isInQueue(String key, Long number) {
        return getQueueSnapshot(key).contains(number);
    }

    // TODO PRIMRWEB-2 Non-atomic operation, might result in duplicated jobs
    private List<Long> getQueueSnapshot(String key) {
        val size = redisTemplate.opsForList().size(key);
        if (size != null) {
            return LongStream
                    .range(0, size.intValue())
                    .boxed()
                    .map(i -> redisTemplate.opsForList().index(key, i))
                    .filter(Objects::nonNull)
                    .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
        }

        return Collections.emptyList();
    }
}
