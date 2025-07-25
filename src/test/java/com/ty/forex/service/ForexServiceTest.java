package com.ty.forex.service;

import com.ty.forex.dto.ErrorResponse;
import com.ty.forex.model.ErrorCode;
import com.ty.forex.model.ExchangeRate;
import com.ty.forex.repository.ForexRepository;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class ForexServiceTest {

    private final ForexRepository forexRepository = Mockito.mock(ForexRepository.class);
    private final ForexService forexService = new ForexService(forexRepository);

    @Test
    void testValidateDateRange_Success() throws Exception {
        // Arrange
        String start = LocalDate.now().minusDays(10).toString();
        String end = LocalDate.now().minusDays(1).toString();
        var method = forexService.getClass().getDeclaredMethod("validateDateRange", String.class, String.class);
        method.setAccessible(true);
        // Act
        @SuppressWarnings("unchecked")
        Optional<ErrorResponse> error = (Optional<ErrorResponse>) method.invoke(forexService, start, end);
        // Assert
        assertTrue(error.isEmpty());
    }

    @Test
    void testValidateDateRange_InvalidFormat() throws Exception {
        // Arrange
        var method = forexService.getClass().getDeclaredMethod("validateDateRange", String.class, String.class);
        method.setAccessible(true);
        // Act
        @SuppressWarnings("unchecked")
        Optional<ErrorResponse> error = (Optional<ErrorResponse>) method.invoke(forexService, "2024.01.01", "2024/01/05");
        // Assert
        assertTrue(error.isPresent());
        assertEquals(ErrorCode.INVALID_DATE_FORMAT.getCode(), error.get().getCode());
    }

    @Test
    void testGetRates() throws Exception {
        // Arrange
        ExchangeRate rate = new ExchangeRate();
        rate.setCurrency("usd");
        rate.setDate(LocalDate.of(2024, 1, 1));
        rate.setRate(new java.math.BigDecimal("31.0"));
        Mockito.when(forexRepository.findByCurrencyAndDateBetween(any(), any(), any()))
                .thenReturn(List.of(rate));
        var method = forexService.getClass().getDeclaredMethod("getRates", String.class, String.class, String.class);
        method.setAccessible(true);
        // Act
        @SuppressWarnings("unchecked")
        List<ExchangeRate> result = (List<ExchangeRate>) method.invoke(forexService, "usd", "2024/01/01", "2024/01/01");
        // Assert
        assertEquals(1, result.size());
        assertEquals("usd", result.get(0).getCurrency());
        assertEquals(LocalDate.of(2024, 1, 1), result.get(0).getDate());
        assertEquals(new java.math.BigDecimal("31.0"), result.get(0).getRate());
    }
}
