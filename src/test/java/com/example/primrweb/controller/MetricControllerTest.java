package com.example.primrweb.controller;

import static com.example.primrweb.controller.MetricController.GKE_KEY;
import static com.example.primrweb.controller.MetricController.STATUS_KEY;
import static com.example.primrweb.controller.MetricController.STATUS_UP;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import com.example.primrweb.AbstractTest;
import com.example.primrweb.ApplicationProperties;
import com.example.primrweb.service.QueueService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import lombok.val;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;

public class MetricControllerTest extends AbstractTest {

    private static final ApplicationProperties.Gke SAMPLE_GKE = new ApplicationProperties.Gke()
            .setPodIp("1.2.3.4")
            .setPodName("prim-r-web-pod");
    private static final String SAMPLE_JOB_QUEUE_KEY = "jobQueue";
    private static final String SAMPLE_PROCESSING_QUEUE_KEY = "processingQueue";
    private static final List<Long> SAMPLE_JOB_QUEUE = Collections.unmodifiableList(Arrays.asList(1L, 2L, 3L));
    private static final List<Long> SAMPLE_PROCESSING_QUEUE = Collections.unmodifiableList(Arrays.asList(4L, 5L, 6L));
    private static final String SAMPLE_CACHE_NAME = "sampleCacheName";
    private static final String SAMPLE_CACHE_KEY = "sampleCacheKey";
    private static final Set<byte[]> SAMPLE_CACHE_KEYS = Collections.singleton(SAMPLE_CACHE_KEY.getBytes());

    @Mock
    private QueueService queueService;

    @Mock
    private ApplicationProperties properties;

    @Mock
    private RedisConnectionFactory redisConnectionFactory;

    @Mock
    private RedisConnection connection;

    @InjectMocks
    private MetricController controller;

    @Before
    public void setUp() {
        when(properties.getGke()).thenReturn(SAMPLE_GKE);
        when(properties.getJobQueueKey()).thenReturn(SAMPLE_JOB_QUEUE_KEY);
        when(properties.getProcessingQueueKey()).thenReturn(SAMPLE_PROCESSING_QUEUE_KEY);
        when(properties.getCacheName()).thenReturn(SAMPLE_CACHE_NAME);
        when(redisConnectionFactory.getConnection()).thenReturn(connection);
    }

    @Test
    public void getShouldBuildMetricsMap() {
        // Arrange
        when(queueService.getJobQueueSnapshot()).thenReturn(SAMPLE_JOB_QUEUE);
        when(queueService.getProcessingQueueSnapshot()).thenReturn(SAMPLE_PROCESSING_QUEUE);
        when(connection.keys((SAMPLE_CACHE_NAME + "*").getBytes())).thenReturn(SAMPLE_CACHE_KEYS);

        // Act
        val actual = controller.get();

        // Assert
        collector.checkThat(actual.get(STATUS_KEY), is(STATUS_UP));
        collector.checkThat(actual.get(GKE_KEY), is(SAMPLE_GKE));
        collector.checkThat(actual.get(SAMPLE_JOB_QUEUE_KEY), is(equalTo(SAMPLE_JOB_QUEUE)));
        collector.checkThat(actual.get(SAMPLE_PROCESSING_QUEUE_KEY), is(equalTo(SAMPLE_PROCESSING_QUEUE)));
        collector.checkThat(actual.get(SAMPLE_CACHE_NAME), is(Collections.singleton(SAMPLE_CACHE_KEY)));
    }


    @Test
    public void getShouldReturnEmptySetIfCacheIsNull() {
        // Arrange
        when(connection.keys((SAMPLE_CACHE_NAME + "*").getBytes())).thenReturn(null);

        // Act
        val actual = controller.get();

        // Assert
        collector.checkThat(actual.get(SAMPLE_CACHE_NAME), is(Collections.emptySet()));
    }

}
