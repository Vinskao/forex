package com.ty.forex.model;

public enum CurrencyPair {
    USD_NTD("USD/NTD");

    private final String value;

    CurrencyPair(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
} 