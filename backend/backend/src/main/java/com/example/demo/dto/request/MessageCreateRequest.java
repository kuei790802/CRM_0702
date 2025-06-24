package com.example.demo.dto.request;

import lombok.Data;

@Data
public class MessageCreateRequest {
    private Long questionId;
    private String content;
}

