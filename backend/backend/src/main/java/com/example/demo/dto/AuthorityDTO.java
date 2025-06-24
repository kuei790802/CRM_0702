package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityDTO {
    private String code;
    private String module;
    private String description;
    private String displayName;
    private String moduleGroup;
}
