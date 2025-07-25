package com.ty.forex.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "forex_rates")
public class ExchangeRate {
    @Id
    private String id;
    private String currency;
    private LocalDate date;
    private BigDecimal rate;

    public ExchangeRate() {}

    public ExchangeRate(String id, String currency, LocalDate date, BigDecimal rate) {
        this.id = id;
        this.currency = currency;
        this.date = date;
        this.rate = rate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}