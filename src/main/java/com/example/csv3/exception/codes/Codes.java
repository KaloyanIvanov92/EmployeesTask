package com.example.csv3.exception.codes;

import com.example.csv3.exception.ErrorCode;

public enum Codes implements ErrorCode {

    GENERAL_ERROR(1000, "General error"),
    WRONG_FILE_TYPE(1001, "Wrong file type. Must be .csv");

    private final int code;
    private final String message;

    Codes(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
