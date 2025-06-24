package com.example.demo.dto.response.dashboard;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NameValueDto {

    private String name;
    private Number value;

    public NameValueDto() {}

    public NameValueDto(String name, Long value) {
        this.name = name;
        this.value = value;
    }

}
