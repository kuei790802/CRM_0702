package com.example.demo.controller;

import com.example.demo.dto.request.UserRegisterRequest;
import com.example.demo.entity.Authority;
import com.example.demo.entity.User;
import com.example.demo.service.CCustomerService;
import com.example.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }



    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestHeader("X-Account") String operatorAccount,
                                         @RequestBody UserRegisterRequest req) throws AccessDeniedException {
//        List<Authority> authorities = req.getAuthorityCodes().stream()
//                .map(code -> authorityRepo.findByCode(code)
//                        .orElseThrow(() -> new IllegalArgumentException("找不到權限代碼: " + code)))
//                .collect(Collectors.toList());

        User createdUser = userService.register(
                operatorAccount,
                req.getAccount(),
                req.getUserName(),
                req.getPassword(),
                req.getEmail(),
                req.getAccessEndDate(),
                req.getAuthorityCodes()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
}
