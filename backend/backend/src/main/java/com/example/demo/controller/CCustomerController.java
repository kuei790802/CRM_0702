package com.example.demo.controller;


import com.example.demo.dto.request.CCustomerRegisterRequest;
import com.example.demo.dto.response.CCustomerProfileResponse;
import com.example.demo.entity.CCustomer;
import com.example.demo.security.CheckJwt;
import com.example.demo.security.JwtTool;
import com.example.demo.service.CCustomerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

///register, /update-profile, /delete-account
/// 帳戶基本操作
@RestController
@RequestMapping("/customer")
public class CCustomerController {

    private final CCustomerService cCustomerService;

    public CCustomerController(CCustomerService cCustomerService) {
        this.cCustomerService = cCustomerService;
    }


    @PostMapping("/register")
    public ResponseEntity<CCustomer> register(@RequestBody CCustomerRegisterRequest req){
        CCustomer cCustomer =  cCustomerService.register(
                req.getAccount(),
                req.getCustomerName(),
                req.getPassword(),
                req.getEmail(),
                req.getAddress(),
                req.getBirthday()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(cCustomer);
    }

//    @CheckJwt // 自訂註解 + 切面處理
//    @GetMapping("/profile")
//    public CCustomerProfileResponse getProfile(@RequestAttribute("account") String account) {
//
//        return cCustomerService.getProfile(account);
//    }

    @CheckJwt
    @GetMapping("/profile")
    public CCustomerProfileResponse getProfile(HttpServletRequest request) {
        String account = (String) request.getAttribute("account");
        System.out.println("profile endpoint account = " + account);
        return cCustomerService.getProfile(account);
    }
}
