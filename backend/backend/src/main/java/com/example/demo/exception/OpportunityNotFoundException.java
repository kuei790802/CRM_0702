package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 當找不到指定的商機時拋出的例外。
 * 自動將 HTTP 狀態碼設定為 404 (Not Found)。
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class OpportunityNotFoundException extends RuntimeException {
    public OpportunityNotFoundException(String message) {
        super(message);
    }
}