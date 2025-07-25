package com.ty.forex.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.swing.text.Document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ty.forex.dto.ErrorResponse;
import com.ty.forex.dto.ForexRequest;
import com.ty.forex.dto.ForexResponse;
import com.ty.forex.repository.ForexRepository;
import com.ty.forex.service.ForexService;

@RestController
@RequestMapping("/api/forex")
public class ForexController {
    @Autowired
    private ForexRepository forexRepository;

    @Autowired
    private ForexService forexService;

    @GetMapping("/rates")
    public ResponseEntity<Map<String, Double>> getExchangeRates() {
        return ResponseEntity.ok(forexService.getExchangeRates());
    }

    @PostMapping("/convert")
    public ResponseEntity<ForexResponse> convertCurrency(@RequestBody ForexRequest request) {
        ForexResponse response = forexService.convertCurrency(
            request.getFromCurrency(),
            request.getToCurrency(),
            request.getAmount()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/usd-twd-history")
    public List<Document> getUsdTwdHistory(
        @RequestParam String startDate,
        @RequestParam String endDate
    ) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return forexRepository.findUsdTwdRates(start, end);
    }

    @PostMapping("/history")
    public ResponseEntity<?> getForexHistory(@RequestBody ForexRequest request) {
        LocalDate start;
        LocalDate end;

        try {
            start = LocalDate.parse(request.getStartDate().replace("/", "-"));
            end = LocalDate.parse(request.getEndDate().replace("/", "-"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("E001", "日期格式錯誤"));
        }

        LocalDate today = LocalDate.now();
        LocalDate oneYearAgo = today.minusYears(1);
        LocalDate yesterday = today.minusDays(1);

        if (start.isBefore(oneYearAgo) || end.isAfter(yesterday) || start.isAfter(end)) {
            return ResponseEntity.badRequest().body(new ErrorResponse("E001", "日期區間不符"));
        }

        if (!"usd".equalsIgnoreCase(request.getCurrency())) {
            return ResponseEntity.badRequest().body(new ErrorResponse("E002", "不支援的幣別"));
        }

        List<Document> records = forexRepository.findUsdTwdRates(start, end);

        return ResponseEntity.ok(Map.of(
            "error", new ErrorResponse("0000", "成功"),
            "currency", records
        ));
    }
}