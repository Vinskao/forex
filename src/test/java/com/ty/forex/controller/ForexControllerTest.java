package com.ty.forex.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ty.forex.dto.ErrorCode;
import com.ty.forex.dto.ForexRequest;
import com.ty.forex.repository.ForexRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ForexController.class)
@ImportAutoConfiguration(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class ForexControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ForexRepository forexRepository;

    @BeforeEach
    void setup() {
        Mockito.reset(forexRepository);
    }

    @Test
    void testGetForexHistory_Success() throws Exception {
        List<Map<String, String>> mockResult = List.of(
                Map.of("date", "20240103", "usd", "31.01"),
                Map.of("date", "20240104", "usd", "31.016")
        );
        Mockito.when(forexRepository.findUsdTwdRates(
                eq(LocalDate.parse("2024-01-01")), eq(LocalDate.parse("2024-01-04"))
        )).thenReturn(mockResult);

        ForexRequest req = new ForexRequest();
        req.setStartDate("2024/01/01");
        req.setEndDate("2024/01/04");
        req.setCurrency("usd");

        mockMvc.perform(post("/api/forex/history")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error.code").value(ErrorCode.SUCCESS.getCode()))
                .andExpect(jsonPath("$.currency").isArray())
                .andExpect(jsonPath("$.currency[0].date").value("20240103"));
    }

    @Test
    void testGetForexHistory_InvalidDateRange() throws Exception {
        ForexRequest req = new ForexRequest();
        req.setStartDate("2020/01/01");
        req.setEndDate("2024/01/01");
        req.setCurrency("usd");

        mockMvc.perform(post("/api/forex/history")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_DATE_RANGE.getCode()));
    }

    @Test
    void testGetForexHistory_UnsupportedCurrency() throws Exception {
        ForexRequest req = new ForexRequest();
        req.setStartDate("2024/01/01");
        req.setEndDate("2024/01/01");
        req.setCurrency("eur");

        mockMvc.perform(post("/api/forex/history")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.UNSUPPORTED_CURRENCY.getCode()));
    }

    @Test
    void testGetForexHistory_InvalidDateFormat() throws Exception {
        ForexRequest req = new ForexRequest();
        req.setStartDate("2024-01-01");
        req.setEndDate("2024-01-01");
        req.setCurrency("usd");

        mockMvc.perform(post("/api/forex/history")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_DATE_FORMAT.getCode()));
    }
} 