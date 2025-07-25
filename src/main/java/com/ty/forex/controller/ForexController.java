package com.ty.forex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ty.forex.service.ForexService;

@RestController
@RequestMapping("/api/forex")
public class ForexController {

    @Autowired
    private ForexService forexservice;

    // 第2功能
    @GetMapping("/usd-ntd-history")
    public ResponseEntity<?> getUsdNtdHistory(
        @RequestParam String currency,
        @RequestParam String startDate,
        @RequestParam String endDate
    ) {
        return forexservice.getUsdNtdHistoryResponse(currency, startDate, endDate);
    }
}