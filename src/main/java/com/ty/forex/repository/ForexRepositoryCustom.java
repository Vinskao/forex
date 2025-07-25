package com.ty.forex.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ForexRepositoryCustom {
    List<Map<String, String>> findUsdTwdRates(LocalDate start, LocalDate end);
} 