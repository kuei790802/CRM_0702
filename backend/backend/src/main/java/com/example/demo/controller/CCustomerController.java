package com.example.demo.controller;


import com.example.demo.dto.request.CCustomerRegisterRequest;
import com.example.demo.dto.request.UpdateCCustomerProfileRequest;
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
import java.util.HashMap;
import java.util.Map;

///register, /update-profile, /delete-account
/// 帳戶基本操作
@RestController
@RequestMapping("/api/customer")
public class CCustomerController {
    private final CCustomerService cCustomerService;
    public CCustomerController(CCustomerService cCustomerService) {
        this.cCustomerService = cCustomerService;
    }

    @GetMapping("/emailcheck")
    public ResponseEntity<Map<String, Object>> emailcheck(@RequestParam String email) {
        boolean exists = cCustomerService.checkEmailExist(email);
        Map<String, Object> result = new HashMap<>();
        result.put("exists", exists);
        result.put("message", exists ? "true" : "false");
        return ResponseEntity.ok(result);
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
    public ResponseEntity<CCustomerProfileResponse> getProfile(HttpServletRequest request) {
        String account = (String) request.getAttribute("account");
        System.out.println("profile endpoint account = " + account);
        return ResponseEntity.ok(cCustomerService.getProfile(account));
    }

    @CheckJwt
    @PutMapping("/profile/update")
    public ResponseEntity<CCustomerProfileResponse> updateProfile(
            HttpServletRequest request,
            @RequestBody UpdateCCustomerProfileRequest updateRequest) {

        String account = (String) request.getAttribute("account");

        CCustomerProfileResponse updatedProfile = cCustomerService.updateProfile(account, updateRequest);

        return ResponseEntity.ok(updatedProfile);
    }
}
