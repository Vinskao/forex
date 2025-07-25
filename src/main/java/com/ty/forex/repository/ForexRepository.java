package com.ty.forex.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.swing.text.Document;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ty.forex.model.ExchangeRate;

public interface ForexRepository extends MongoRepository<ExchangeRate, String>, ForexRepositoryCustom {
    List<Map<String, String>> findUsdTwdRates(LocalDate start, LocalDate end);
}
