package com.example.csv3.exception;

public class BaseException extends Exception {

    private ErrorCode errorCode;
    private String customMessage;

    public BaseException() {
    }

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BaseException(ErrorCode errorCode, String customMessage) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.customMessage = customMessage;
    }

    public BaseException(ErrorCode errorCode, Throwable e) {
        super(errorCode.getMessage(), e);
        this.errorCode = errorCode;
    }

    public BaseException(ErrorCode errorCode, String customMessage, Throwable e) {
        super(errorCode.getMessage(), e);
        this.errorCode = errorCode;
        this.customMessage = customMessage;
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public String getCustomMessage() {
        return this.customMessage;
    }

    public void setCustomMessage(String customMessage) {
        this.customMessage = customMessage;
    }
}
