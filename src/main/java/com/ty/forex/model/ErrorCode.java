package com.ty.forex.model;

public enum ErrorCode {
    SUCCESS("0000", "成功"),
    INVALID_DATE_RANGE("E001", "日期區間不符"),
    UNSUPPORTED_CURRENCY("E002", "不支援的幣別"),
    INVALID_DATE_FORMAT("E001", "日期格式錯誤");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
} 