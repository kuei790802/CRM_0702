package com.example.demo.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder // 使用後可於 service 建構物件時更方便
public class CartDetailDto {
    private Long cartdetailid;
    private Long productid;
    private String productname;
    private String productimgurl;
    private Double unitprice;
    private Integer quantity;
    private Double totalprice;
}
