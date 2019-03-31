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
import lombok.val;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class MetricControllerTest extends AbstractTest {

    private static final ApplicationProperties.Gke SAMPLE_GKE = new ApplicationProperties.Gke()
            .setPodIp("1.2.3.4")
            .setPodName("prim-r-web-pod");
    private static final String SAMPLE_JOB_QUEUE_KEY = "jobQueue";
    private static final String SAMPLE_PROCESSING_QUEUE_KEY = "processingQueue";
    private static final List<Long> SAMPLE_JOB_QUEUE = Collections.unmodifiableList(Arrays.asList(1L, 2L, 3L));
    private static final List<Long> SAMPLE_PROCESSING_QUEUE = Collections.unmodifiableList(Arrays.asList(4L, 5L, 6L));

    @Mock
    private QueueService queueService;

    @Mock
    private ApplicationProperties properties;

    @InjectMocks
    private MetricController controller;

    @Before
    public void setUp() {
        when(properties.getGke()).thenReturn(SAMPLE_GKE);
        when(properties.getJobQueueKey()).thenReturn(SAMPLE_JOB_QUEUE_KEY);
        when(properties.getProcessingQueueKey()).thenReturn(SAMPLE_PROCESSING_QUEUE_KEY);
    }

    @Test
    public void getShouldBuildMetricsMap() {
        // Arrange
        when(queueService.getJobQueueSnapshot()).thenReturn(SAMPLE_JOB_QUEUE);
        when(queueService.getProcessingQueueSnapshot()).thenReturn(SAMPLE_PROCESSING_QUEUE);

        // Act
        val actual = controller.get();

        // Assert
        collector.checkThat(actual.get(STATUS_KEY), is(STATUS_UP));
        collector.checkThat(actual.get(GKE_KEY), is(SAMPLE_GKE));
        collector.checkThat(actual.get(SAMPLE_JOB_QUEUE_KEY), is(equalTo(SAMPLE_JOB_QUEUE)));
        collector.checkThat(actual.get(SAMPLE_PROCESSING_QUEUE_KEY), is(equalTo(SAMPLE_PROCESSING_QUEUE)));
    }
}
