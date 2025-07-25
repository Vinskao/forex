package com.ty.forex.service;

import com.ty.forex.model.ExchangeRate;
import com.ty.forex.repository.ForexRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ForexBatchJobTest {
    @Mock
    private ForexRepository forexRepository;
    @InjectMocks
    private ForexBatchJob forexBatchJob;
    @Mock
    private RestTemplate restTemplate;

    @Value("${taifex.forex-api-url}")
    private String apiUrl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFetchAndSaveUsdNtdRate_Success() {
        // Arrange
        String json = "[{\"Date\":\"20240101\",\"USD/NTD\":\"31.01\"},{\"Date\":\"20240101\",\"EUR/NTD\":\"34.01\"}]";
        when(restTemplate.getForObject(any(String.class), eq(String.class))).thenReturn(json);
        forexBatchJob.restTemplate = restTemplate;
        forexBatchJob.apiUrl = apiUrl;

        // Act
        forexBatchJob.fetchAndSaveUsdNtdRate();

        // Assert
        ArgumentCaptor<ExchangeRate> captor = ArgumentCaptor.forClass(ExchangeRate.class);
        verify(forexRepository, times(1)).save(captor.capture());
        ExchangeRate saved = captor.getValue();
        assertEquals("USD/NTD", saved.getCurrency());
        assertEquals(LocalDate.of(2024, 1, 1), saved.getDate());
        assertEquals(new BigDecimal("31.01"), saved.getRate());
    }

    @Test
    void testFetchAndSaveUsdNtdRate_ApiException() {
        // Arrange
        when(restTemplate.getForObject(any(String.class), eq(String.class))).thenThrow(new RuntimeException("API error"));
        forexBatchJob.restTemplate = restTemplate;
        forexBatchJob.apiUrl = apiUrl;

        // Act & Assert
        assertDoesNotThrow(() -> forexBatchJob.fetchAndSaveUsdNtdRate());
        verify(forexRepository, never()).save(any());
    }
}
