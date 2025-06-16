package com.example.demo.dto.ecpay;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RqHeader {
    @JsonProperty("Timestamp")
    private long timestamp;
}