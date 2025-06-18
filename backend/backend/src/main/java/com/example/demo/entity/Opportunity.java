package com.example.demo.entity;


import com.example.demo.enums.OpportunityStage;
import com.example.demo.enums.OpportunityStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "opportunities")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"bCustomer", "contact"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Opportunity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long opportunityId;

    @Column(nullable = false, length = 255)
    private String opportunityName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(precision = 14, scale = 2)
    private BigDecimal expectedValue;

    private LocalDate closeDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private OpportunityStage stage;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private OpportunityStatus status;

    // ----- 多對一關聯：商機所屬的客戶 -----
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private BCustomer bCustomer;

    // ----- 多對一關聯：商機所屬的聯絡人 -----
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id")
    private Contact contact;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;


}
