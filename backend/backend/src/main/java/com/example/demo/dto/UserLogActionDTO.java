package com.example.demo.dto;

import com.example.demo.enums.ActionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLogActionDTO {
    private String userAccount;
    private ActionType actionType;
    private String description;
    private String targetTable;
    private String targetId;
    private String ipAddress;
    private String userAgent;
    private LocalDateTime createdAt;
}
