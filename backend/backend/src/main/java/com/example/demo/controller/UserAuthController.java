package com.example.demo.controller;

import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.response.CCustomerLoginResponse;
import com.example.demo.dto.response.UserLoginResponse;
import com.example.demo.entity.CCustomer;
import com.example.demo.entity.User;
import com.example.demo.security.CheckJwt;
import com.example.demo.security.JwtTool;
import com.example.demo.service.CCustomerService;
import com.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

///login, /test, /get-token
/// 登入、JWT、驗證類
@RestController
@RequestMapping("/user/auth")
public class UserAuthController {
    private final UserService userService;
    public UserAuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody LoginRequest req){
        User user = userService.login(
                req.getAccount(),
                req.getPassword()
        );

        String token =  JwtTool.createToken(
                user.getCustomerName(),
                user.getAccount(),
                user.getCustomerId(),
                user.getVipLevel()
        );

        CCustomerLoginResponse res = CCustomerLoginResponse.builder()
                .token(token)
                .account(user.getAccount())
                .customerName(user.getCustomerName())
                .email(user.getEmail())
                .birthday(user.getBirthday())
                .createdAt(user.getCreatedAt())
                .spending(user.getSpending())
                .build();
        return ResponseEntity.ok(res);
    }

    @GetMapping("/test")
    @CheckJwt
    public String testEndpoint() {
        return "Token 驗證成功，你進來了！";
    }

    // /forget-password
}
