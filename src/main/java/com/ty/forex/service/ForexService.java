package com.ty.forex.service;

import com.ty.forex.dto.ErrorCode;
import com.ty.forex.dto.ErrorResponse;
import com.ty.forex.repository.ForexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ForexService {

    @Autowired
    private ForexRepository forexRepository;

    public ResponseEntity<?> getUsdTwdHistoryResponse(String currency, String startDateStr, String endDateStr) {
        Optional<ErrorResponse> error = validateDateRange(startDateStr, endDateStr);
        if (error.isPresent()) {
            return ResponseEntity.badRequest().body(error.get());
        }
        List<Map<String, String>> records = getRates(currency, startDateStr, endDateStr);
        return ResponseEntity.ok(Map.of(
            "error", new ErrorResponse(ErrorCode.SUCCESS),
            "currency", records
        ));
    }

    public Optional<ErrorResponse> validateDateRange(String startDateStr, String endDateStr) {
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

    public List<Map<String, String>> getRates(String currency, String startDateStr, String endDateStr) {
        LocalDate start = LocalDate.parse(startDateStr.replace("/", "-"));
        LocalDate end = LocalDate.parse(endDateStr.replace("/", "-"));
        return forexRepository.findByCurrencyAndDateBetween(currency, start, end);
    }
}
