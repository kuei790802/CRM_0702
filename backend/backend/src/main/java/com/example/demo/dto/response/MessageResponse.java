package com.example.demo.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageResponse {
    private Long id;
    private Long customerId;
    private Long questionId;
    private String content;
    private LocalDateTime createdAt;
    private String reply;
}
