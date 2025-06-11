package com.example.demo.exception;

public class ForgetAccountOrPasswordException extends RuntimeException{
    public ForgetAccountOrPasswordException(String account, String password) {
        //之後要修改，並了解RUNTIMEEXCEPTION運作方式，怎麼設定會有我在SERVICE想要的效果?
        super("帳號密碼錯誤");
    }
}
