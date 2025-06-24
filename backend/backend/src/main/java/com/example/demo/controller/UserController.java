package com.example.demo.controller;

import com.example.demo.dto.request.UpdateCCustomerProfileRequest;
import com.example.demo.dto.request.UpdateUserProfileRequest;
import com.example.demo.dto.request.UserRegisterRequest;
import com.example.demo.dto.response.CCustomerProfileResponse;
import com.example.demo.dto.response.UserProfileResponse;
import com.example.demo.entity.Authority;
import com.example.demo.entity.User;
import com.example.demo.security.CheckJwt;
import com.example.demo.service.CCustomerService;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @CheckJwt
    @PostMapping("/register")
    public ResponseEntity<User> register(HttpServletRequest request,
                                         @RequestBody UserRegisterRequest req) throws AccessDeniedException {
//        List<Authority> authorities = req.getAuthorityCodes().stream()
//                .map(code -> authorityRepo.findByCode(code)
//                        .orElseThrow(() -> new IllegalArgumentException("找不到權限代碼: " + code)))
//                .collect(Collectors.toList());

        String operatorAccount = (String) request.getAttribute("account");

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

    @GetMapping("/profile")
    @CheckJwt
    public ResponseEntity<UserProfileResponse> getProfile(HttpServletRequest request) {
        String account = (String) request.getAttribute("account");
        return ResponseEntity.ok(userService.getUserProfileByAccount(account));
    }


    @CheckJwt
    @PutMapping("/profile/update")
    public ResponseEntity<UserProfileResponse> updateProfile(
            HttpServletRequest request,
            @RequestBody UpdateUserProfileRequest updateRequest) {

        String account = (String) request.getAttribute("account");

        UserProfileResponse updatedProfile = userService.updateOwnProfile(account, updateRequest);

        return ResponseEntity.ok(updatedProfile);
    }

}
