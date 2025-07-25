package com.ty.forex.service;

import com.ty.forex.dto.ErrorCode;
import com.ty.forex.dto.ErrorResponse;
import com.ty.forex.repository.ForexRepository;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class ForexServiceTest {

    private final ForexRepository forexRepository = Mockito.mock(ForexRepository.class);
    private final ForexService forexService = new ForexService();

    {
        forexService.getClass().getDeclaredFields();
        try {
            var field = forexService.getClass().getDeclaredField("forexRepository");
            field.setAccessible(true);
            field.set(forexService, forexRepository);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testValidateDateRange_Success() {
        String start = LocalDate.now().minusDays(10).toString();
        String end = LocalDate.now().minusDays(1).toString();
        Optional<ErrorResponse> error = forexService.validateDateRange(start, end);
        assertTrue(error.isEmpty());
    }

    @Test
    void testValidateDateRange_InvalidFormat() {
        Optional<ErrorResponse> error = forexService.validateDateRange("2024.01.01", "2024/01/05");
        assertTrue(error.isPresent());
        assertEquals(ErrorCode.INVALID_DATE_FORMAT.getCode(), error.get().getCode());
    }

    @Test
    void testGetRates() {
        Mockito.when(forexRepository.findByCurrencyAndDateBetween(any(), any(), any()))
                .thenReturn(List.of(Map.of("usd", "31.0", "date", "2024-01-01")));

        List<Map<String, String>> result = forexService.getRates("usd", "2024/01/01", "2024/01/01");

        assertEquals(1, result.size());
        assertEquals("31.0", result.get(0).get("usd"));
    }
}
