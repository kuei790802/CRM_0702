package com.example.demo.controller;

import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.response.CCustomerLoginResponse;
import com.example.demo.entity.CCustomer;
import com.example.demo.entity.VIPLevel;
import com.example.demo.security.CheckJwt;
import com.example.demo.security.JwtTool;
import com.example.demo.security.JwtUserPayload;
import com.example.demo.service.CCustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


///login, /test, /get-token
/// 登入、JWT、驗證類
@RestController
@RequestMapping("/customer/auth")
public class CCustomerAuthController {

    private final CCustomerService cCustomerService;
    public CCustomerAuthController(CCustomerService cCustomerService) {
        this.cCustomerService = cCustomerService;
    }

    @PostMapping("/login")
    public ResponseEntity<CCustomerLoginResponse> login(@RequestBody LoginRequest req){
        CCustomer cCustomer = cCustomerService.login(
                req.getAccount(),
                req.getPassword()
        );

        String token =  JwtTool.createToken(JwtUserPayload.fromCustomer(cCustomer));

        CCustomerLoginResponse res = CCustomerLoginResponse.builder()
                .token(token)
                .account(cCustomer.getAccount())
                .customerName(cCustomer.getCustomerName())
                .email(cCustomer.getEmail())
                .birthday(cCustomer.getBirthday())
                .createdAt(cCustomer.getCreatedAt())
                .spending(cCustomer.getSpending())
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
