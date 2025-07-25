package com.ty.forex.service;

import com.ty.forex.repository.ForexRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ForexBatchJobTest {
    @Mock
    private ForexRepository forexRepository;
    @InjectMocks
    private ForexBatchJob forexBatchJob;

    @Value("${taifex.forex-api-url}")
    private String apiUrl;

    public ForexBatchJobTest() {
        MockitoAnnotations.openMocks(this);
    }
}
