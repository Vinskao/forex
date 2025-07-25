package com.ty.forex.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

import com.ty.forex.dto.ForexResponse;

@Service
public class ForexService {

    public Map<String, Double> getExchangeRates() {
        Map<String, Double> rates = new HashMap<>();
        rates.put("USD", 1.0);
        rates.put("EUR", 0.85);
        rates.put("JPY", 110.0);
        rates.put("TWD", 30.0);
        return rates;
    }

    public ForexResponse convertCurrency(String fromCurrency, String toCurrency, double amount) {
        Map<String, Double> rates = getExchangeRates();

        double fromRate = rates.getOrDefault(fromCurrency.toUpperCase(), -1.0);
        double toRate = rates.getOrDefault(toCurrency.toUpperCase(), -1.0);

        if (fromRate <= 0 || toRate <= 0) {
            throw new IllegalArgumentException("Unsupported currency");
        }

        double convertedAmount = (amount / fromRate) * toRate;

        return new ForexResponse(
            fromCurrency.toUpperCase(),
            toCurrency.toUpperCase(),
            amount,
            convertedAmount,
            toRate / fromRate
        );
    }
}