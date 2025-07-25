package com.ty.forex.dto;
import java.util.Objects;

public class ForexRequest {
    private String startDate;
    private String endDate;
    private String currency;
    private String fromCurrency;
    private String toCurrency;
    private double amount;

    public ForexRequest() {
    }

    public ForexRequest(String startDate, String endDate, String currency, String fromCurrency, String toCurrency, double amount) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.currency = currency;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.amount = amount;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getFromCurrency() {
        return this.fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public String getToCurrency() {
        return this.toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public ForexRequest startDate(String startDate) {
        setStartDate(startDate);
        return this;
    }

    public ForexRequest endDate(String endDate) {
        setEndDate(endDate);
        return this;
    }

    public ForexRequest currency(String currency) {
        setCurrency(currency);
        return this;
    }

    public ForexRequest fromCurrency(String fromCurrency) {
        setFromCurrency(fromCurrency);
        return this;
    }

    public ForexRequest toCurrency(String toCurrency) {
        setToCurrency(toCurrency);
        return this;
    }

    public ForexRequest amount(double amount) {
        setAmount(amount);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ForexRequest)) {
            return false;
        }
        ForexRequest forexRequest = (ForexRequest) o;
        return Objects.equals(startDate, forexRequest.startDate) && Objects.equals(endDate, forexRequest.endDate) && Objects.equals(currency, forexRequest.currency) && Objects.equals(fromCurrency, forexRequest.fromCurrency) && Objects.equals(toCurrency, forexRequest.toCurrency) && amount == forexRequest.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate, currency, fromCurrency, toCurrency, amount);
    }

    @Override
    public String toString() {
        return "{" +
            " startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", fromCurrency='" + getFromCurrency() + "'" +
            ", toCurrency='" + getToCurrency() + "'" +
            ", amount='" + getAmount() + "'" +
            "}";
    }

}
