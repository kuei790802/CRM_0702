package com.example.demo.controller;

import com.example.demo.security.CheckJwt;
import com.example.demo.security.JwtTool;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CCustomerAuthController {
    @GetMapping("/test")
    @CheckJwt
    public String testEndpoint() {
        return "Token 驗證成功，你進來了！";
    }

    @GetMapping("/get-token")
    public String getToken() {
        return JwtTool.createToken("test-user");
    }

    private void teToken(String s) {
    }

}
