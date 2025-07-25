package com.ty.forex.service;

import com.ty.forex.repository.ForexRepository;
import com.ty.forex.model.ExchangeRate;
import com.ty.forex.model.CurrencyPair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ForexBatchJob {
    private static final Logger logger = LoggerFactory.getLogger(ForexBatchJob.class);
    ForexRepository forexRepository;
    RestTemplate restTemplate = new RestTemplate();
    @Value("${taifex.forex-api-url}")
    String apiUrl;

    public ForexBatchJob(ForexRepository forexRepository) {
        this.forexRepository = forexRepository;
    }

    // First function
    @Scheduled(cron = "${forex.batch.cron}")
    public void fetchAndSaveUsdNtdRate() {
        logger.info("Batch job started");
        try {
            String response = restTemplate.getForObject(apiUrl, String.class);
            logger.info("API response: {}", response);

            org.json.JSONArray arr = new org.json.JSONArray(response);
            for (int i = 0; i < arr.length(); i++) {
                org.json.JSONObject obj = arr.getJSONObject(i);
                if (obj.has(CurrencyPair.USD_NTD.getValue())) {
                    String dateStr = obj.getString("Date");
                    String rateStr = obj.getString(CurrencyPair.USD_NTD.getValue());
                    logger.info("Found USD/NTD: date={}, rate={}", dateStr, rateStr);

                    LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyyMMdd"));
                    BigDecimal rate = new BigDecimal(rateStr);
                    ExchangeRate exchangeRate = new ExchangeRate();
                    exchangeRate.setCurrency(CurrencyPair.USD_NTD.getValue());
                    exchangeRate.setDate(date);
                    exchangeRate.setRate(rate);

                    forexRepository.save(exchangeRate);
                    logger.info("Saved exchange rate: {}", exchangeRate);
                }
            }
        } catch (Exception e) {
            logger.error("Error in batch job", e);
        }
    }
}