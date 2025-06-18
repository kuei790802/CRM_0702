package com.example.demo.dto.response;

import com.example.demo.enums.OpportunityStage;
import com.example.demo.enums.OpportunityStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用於向客戶端傳送商機資訊的回應資料。
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpportunityDto {

    private Long opportunityId;

    private String opportunityName;

    private String description;

    private BigDecimal expectedValue;

    private LocalDate closeDate;

    private OpportunityStage stage;

    private OpportunityStatus status;

    private Long customerId;
    private String customerName;

    private Long contactId;
    private String contactName;


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
