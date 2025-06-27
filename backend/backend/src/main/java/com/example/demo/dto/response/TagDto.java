package com.example.demo.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TagDto {
    private Long tagId;
    private String tagName;
    private String color;
}
