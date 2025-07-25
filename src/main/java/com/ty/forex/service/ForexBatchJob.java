package com.ty.forex.service;

import com.ty.forex.repository.ForexRepository;
import com.ty.forex.model.ExchangeRate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ForexBatchJob {

    ForexRepository forexRepository;
    RestTemplate restTemplate = new RestTemplate();
    @Value("${taifex.forex-api-url}")
    String apiUrl;

    public ForexBatchJob(ForexRepository forexRepository) {
        this.forexRepository = forexRepository;
    }

    // 第1功能
    @Scheduled(cron = "0 0 18 * * *")
    public void fetchAndSaveUsdTwdRate() {
        try {
            String response = restTemplate.getForObject(apiUrl, String.class);

            org.json.JSONArray arr = new org.json.JSONArray(response);
            for (int i = 0; i < arr.length(); i++) {
                org.json.JSONObject obj = arr.getJSONObject(i);
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
}