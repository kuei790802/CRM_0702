package com.example.demo.dto.request;

import lombok.Data;
import java.util.Date;

@Data
public class CCustomerUpdateRequest {
    private String email;
    private String address;
    private Date birthday;
}
