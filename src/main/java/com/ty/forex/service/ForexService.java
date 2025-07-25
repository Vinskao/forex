package com.ty.forex.service;

import com.ty.forex.dto.ErrorResponse;
import com.ty.forex.model.ErrorCode;
import com.ty.forex.model.ExchangeRate;
import com.ty.forex.repository.ForexRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;

@Service
public class ForexService {

    private final ForexRepository forexRepository;

    public ForexService(ForexRepository forexRepository) {
        this.forexRepository = forexRepository;
    }

    public ResponseEntity<?> getUsdNtdHistoryResponse(String currency, String startDateStr, String endDateStr) {
        Optional<ErrorResponse> error = validateDateRange(startDateStr, endDateStr);
        if (error.isPresent()) {
            return ResponseEntity.badRequest().body(error.get());
        }
        List<ExchangeRate> records = getRates(currency, startDateStr, endDateStr);
        List<Map<String, Object>> result = new java.util.ArrayList<>();
        for (ExchangeRate rate : records) {
            Map<String, Object> item = new HashMap<>();
            item.put("date", rate.getDate().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd")));
            item.put(currency.toLowerCase(), rate.getRate().toString());
            result.add(item);
        }
        return ResponseEntity.ok(Map.of(
            "error", new ErrorResponse(ErrorCode.SUCCESS),
            "currency", result
        ));
    }

    private Optional<ErrorResponse> validateDateRange(String startDateStr, String endDateStr) {
        try {
            LocalDate start = LocalDate.parse(startDateStr.replace("/", "-"));
            LocalDate end = LocalDate.parse(endDateStr.replace("/", "-"));

            LocalDate today = LocalDate.now();
            LocalDate oneYearAgo = today.minusYears(1);
            LocalDate yesterday = today.minusDays(1);

            if (start.isBefore(oneYearAgo) || end.isAfter(yesterday) || start.isAfter(end)) {
                return Optional.of(new ErrorResponse(ErrorCode.INVALID_DATE_RANGE));
            }

            return Optional.empty();
        } catch (Exception e) {
            return Optional.of(new ErrorResponse(ErrorCode.INVALID_DATE_FORMAT));
        }
    }

    private List<ExchangeRate> getRates(String currency, String startDateStr, String endDateStr) {
        LocalDate start = LocalDate.parse(startDateStr.replace("/", "-"));
        LocalDate end = LocalDate.parse(endDateStr.replace("/", "-"));
        return forexRepository.findByCurrencyAndDateBetween(currency, start, end);
    }
}
