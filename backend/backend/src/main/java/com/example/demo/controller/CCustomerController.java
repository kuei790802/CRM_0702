package com.example.demo.controller;


import com.example.demo.dto.request.CCustomerRegisterRequest;
import com.example.demo.entity.CCustomer;
import com.example.demo.service.CCustomerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public CCustomer register(@RequestBody CCustomerRegisterRequest req){
        return cCustomerService.register(
                req.getAccount(),
                req.getCustomerName(),
                req.getPassword(),
                req.getEmail(),
                req.getBirthday()
        );
    }


}
