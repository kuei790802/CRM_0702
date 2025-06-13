package com.example.demo.exception;

public class AccountAlreadyExistsException extends RuntimeException{
    public AccountAlreadyExistsException(String account) {
        super("帳號已存在：" + account);
    }
}
