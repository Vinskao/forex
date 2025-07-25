package com.ty.forex.repository;

import java.time.LocalDate;
import java.util.List;

import javax.swing.text.Document;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ty.forex.model.ExchangeRate;

public interface ForexRepository extends MongoRepository<ExchangeRate, String> {
    List<ExchangeRate> findByCurrencyAndDateBetween(String currency, String startDate, String endDate);

    List<Document> findUsdTwdRates(LocalDate start, LocalDate end);
}
