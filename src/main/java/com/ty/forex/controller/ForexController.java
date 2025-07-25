package com.ty.forex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ty.forex.service.ForexService;
import com.ty.forex.dto.ForexRequest;

@RestController
@RequestMapping("/api/forex")
public class ForexController {

    @Autowired
    private ForexService forexservice;

    // Second function
    @PostMapping("/usd-ntd-history")
    public ResponseEntity<?> getUsdNtdHistory(@RequestBody ForexRequest request) {
        return forexservice.getUsdNtdHistoryResponse(request.getCurrency(), request.getStartDate(), request.getEndDate());
    }
}