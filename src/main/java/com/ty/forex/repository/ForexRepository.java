package com.ty.forex.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ty.forex.model.ExchangeRate;

public interface ForexRepository extends MongoRepository<ExchangeRate, String> {
    List<Map<String, String>> findByCurrencyAndDateBetween(String currency, LocalDate start, LocalDate end);
}
