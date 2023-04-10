package com.example.csv3.exception;

public class CsvException extends BaseException {

    public CsvException(ErrorCode errorCode) {
        super(errorCode);
    }

    public CsvException(ErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }

    public CsvException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }

    public CsvException(ErrorCode errorCode, String customMessage, Throwable e) {
        super(errorCode, customMessage, e);
    }
}
