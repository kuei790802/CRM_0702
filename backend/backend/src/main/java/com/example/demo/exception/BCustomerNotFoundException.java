package com.example.demo.exception;

// 找不到客戶資源的自定義異常
public class BCustomerNotFoundException extends RuntimeException {

    public BCustomerNotFoundException(String message) {
        super(message);
    }

    public BCustomerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}