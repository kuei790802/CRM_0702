package com.example.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest extends RegisterRequest {
    private String userName;
    private LocalDate accessEndDate; // 用來設定 accessEndTime 的日期
    private List<String> authorityCodes;
}
