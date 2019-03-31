package com.example.primrweb.service;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.primrweb.AbstractTest;
import com.example.primrweb.ApplicationProperties;
import java.util.Collections;
import lombok.val;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

public class QueueServiceImplTest extends AbstractTest {

    private static final String SAMPLE_JOB_QUEUE_KEY = "jobQueue";
    private static final String SAMPLE_PROCESSING_QUEUE_KEY = "processingQueue";
    private static final long SAMPLE_NUMBER_IN_QUEUE = 42L;
    private static final long SAMPLE_NUMBER_NOT_IN_QUEUE = 43L;

    @Mock
    private RedisTemplate<String, Long> redisTemplate;

    @Mock
    private ApplicationProperties properties;

    @Mock
    private ListOperations<String, Long> opsForList;

    @InjectMocks
    private QueueServiceImpl service;

    @Before
    public void setUp() {
        when(properties.getJobQueueKey()).thenReturn(SAMPLE_JOB_QUEUE_KEY);
        when(properties.getProcessingQueueKey()).thenReturn(SAMPLE_PROCESSING_QUEUE_KEY);
        when(redisTemplate.opsForList()).thenReturn(opsForList);
    }

    @Test
    public void addToQueueShouldNotPushNumberIfInJob() {
        // Arrange
        when(opsForList.size(SAMPLE_JOB_QUEUE_KEY)).thenReturn(1L);
        when(opsForList.index(SAMPLE_JOB_QUEUE_KEY, 0)).thenReturn(SAMPLE_NUMBER_IN_QUEUE);

        // Act
        service.addToQueue(SAMPLE_NUMBER_IN_QUEUE);

        // Assert
        verify(opsForList, never()).leftPush(anyString(), anyLong());
    }

    @Test
    public void addToQueueShouldNotPushNumberIfInProcessing() {
        // Arrange
        when(opsForList.size(SAMPLE_PROCESSING_QUEUE_KEY)).thenReturn(1L);
        when(opsForList.index(SAMPLE_PROCESSING_QUEUE_KEY, 0)).thenReturn(SAMPLE_NUMBER_IN_QUEUE);

        // Act
        service.addToQueue(SAMPLE_NUMBER_IN_QUEUE);

        // Assert
        verify(opsForList, never()).leftPush(anyString(), anyLong());
    }

    @Test
    public void addToQueueShouldPushNumberIfJobSizeIsNull() {
        // Arrange
        when(opsForList.size(SAMPLE_JOB_QUEUE_KEY)).thenReturn(null);

        // Act
        service.addToQueue(SAMPLE_NUMBER_IN_QUEUE);

        // Assert
        verify(opsForList).leftPush(SAMPLE_JOB_QUEUE_KEY, SAMPLE_NUMBER_IN_QUEUE);
    }

    @Test
    public void addToQueueShouldPushNumberIfNotInQueue() {
        // Arrange
        when(opsForList.size(SAMPLE_JOB_QUEUE_KEY)).thenReturn(1L);
        when(opsForList.index(SAMPLE_JOB_QUEUE_KEY, 0)).thenReturn(SAMPLE_NUMBER_IN_QUEUE);

        // Act
        service.addToQueue(SAMPLE_NUMBER_NOT_IN_QUEUE);

        // Assert
        verify(opsForList).leftPush(SAMPLE_JOB_QUEUE_KEY, SAMPLE_NUMBER_NOT_IN_QUEUE);
    }

    @Test
    public void getJobQueueSnapshotShouldReturnSnapshot() {
        // Arrange
        when(opsForList.size(SAMPLE_JOB_QUEUE_KEY)).thenReturn(1L);
        when(opsForList.index(SAMPLE_JOB_QUEUE_KEY, 0)).thenReturn(SAMPLE_NUMBER_IN_QUEUE);

        // Act
        val actual = service.getJobQueueSnapshot();

        // Assert
        collector.checkThat(actual, is(equalTo(Collections.singletonList(SAMPLE_NUMBER_IN_QUEUE))));
    }

    @Test
    public void getJobQueueSnapshotShouldReturnEmptyListIfQueueSizeIsNull() {
        // Arrange
        when(opsForList.size(SAMPLE_JOB_QUEUE_KEY)).thenReturn(null);

        // Act
        val actual = service.getJobQueueSnapshot();

        // Assert
        collector.checkThat(actual.isEmpty(), is(true));
    }

    @Test
    public void getProcessingQueueSnapshotShouldReturnSnapshot() {
        // Arrange
        when(opsForList.size(SAMPLE_PROCESSING_QUEUE_KEY)).thenReturn(1L);
        when(opsForList.index(SAMPLE_PROCESSING_QUEUE_KEY, 0)).thenReturn(SAMPLE_NUMBER_IN_QUEUE);

        // Act
        val actual = service.getProcessingQueueSnapshot();

        // Assert
        collector.checkThat(actual, is(equalTo(Collections.singletonList(SAMPLE_NUMBER_IN_QUEUE))));
    }
}
