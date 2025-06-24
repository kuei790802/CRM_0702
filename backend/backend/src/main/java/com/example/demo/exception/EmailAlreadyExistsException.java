package com.example.demo.exception;


public class EmailAlreadyExistsException extends RuntimeException{
  public EmailAlreadyExistsException(String email) {
    super("信箱已被註冊：" + email);
  }
}
