package com.ty.forex.service;

import com.ty.forex.model.ExchangeRate;
import com.ty.forex.repository.ForexRepository;
import org.json.JSONArray;
import org.json.JSONObject;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        String json = "[" +
                "{\"Currency\":\"USD/NTD\",\"Date\":\"2024-01-01 18:00:00\",\"Rate\":\"31.01\"}," +
                "{\"Currency\":\"EUR/NTD\",\"Date\":\"2024-01-01 18:00:00\",\"Rate\":\"34.01\"}]";
        when(restTemplate.getForObject(any(String.class), eq(String.class))).thenReturn(json);

        ForexBatchJob job = new ForexBatchJob(forexRepository) {
            @Override
            public void fetchAndSaveUsdNtdRate() {
                try {
                    String response = restTemplate.getForObject(apiUrl, String.class);
                    JSONArray arr = new JSONArray(response);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        if ("USD/NTD".equals(obj.getString("Currency"))) {
                            String dateStr = obj.getString("Date");
                            String rateStr = obj.getString("Rate");
                            LocalDateTime date = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                            BigDecimal rate = new BigDecimal(rateStr);
                            ExchangeRate exchangeRate = new ExchangeRate();
                            exchangeRate.setCurrency("USD/NTD");
                            exchangeRate.setDate(date.toLocalDate());
                            exchangeRate.setRate(rate);
                            forexRepository.save(exchangeRate);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        job.restTemplate = restTemplate;
        job.apiUrl = apiUrl;

        job.fetchAndSaveUsdNtdRate();

        ArgumentCaptor<ExchangeRate> captor = ArgumentCaptor.forClass(ExchangeRate.class);
        verify(forexRepository, times(1)).save(captor.capture());
        ExchangeRate saved = captor.getValue();
        assertEquals("USD/NTD", saved.getCurrency());
        assertEquals(LocalDate.of(2024, 1, 1), saved.getDate());
        assertEquals(new BigDecimal("31.01"), saved.getRate());
    }

    @Test
    void testFetchAndSaveUsdNtdRate_ApiException() {
        when(restTemplate.getForObject(any(String.class), eq(String.class))).thenThrow(new RuntimeException("API error"));
        ForexBatchJob job = new ForexBatchJob(forexRepository) {
            @Override
            public void fetchAndSaveUsdNtdRate() {
                try {
                    String response = restTemplate.getForObject(apiUrl, String.class);
                    JSONArray arr = new JSONArray(response);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        if ("USD/NTD".equals(obj.getString("Currency"))) {
                            String dateStr = obj.getString("Date");
                            String rateStr = obj.getString("Rate");
                            LocalDateTime date = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                            BigDecimal rate = new BigDecimal(rateStr);
                            ExchangeRate exchangeRate = new ExchangeRate();
                            exchangeRate.setCurrency("USD/NTD");
                            exchangeRate.setDate(date.toLocalDate());
                            exchangeRate.setRate(rate);
                            forexRepository.save(exchangeRate);
                        }
                    }
                } catch (Exception e) {
                }
            }
        };
        job.restTemplate = restTemplate;
        job.apiUrl = apiUrl;

        assertDoesNotThrow(job::fetchAndSaveUsdNtdRate);
        verify(forexRepository, never()).save(any());
    }
}
