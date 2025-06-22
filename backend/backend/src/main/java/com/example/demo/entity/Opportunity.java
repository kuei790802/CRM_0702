package com.example.demo.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "opportunities")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude ="BCustomer")
@EqualsAndHashCode(of = "id")
public class Opportunity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "opportunityid")
    private Long id;

    @Column(name = "customerid", nullable = false)
    private Long customerId;

    @Column(name = "opportunitystage")
    private String stage;

    @Column(name = "opportunitystatus")
    private String status;

    @Column(name = "opportunitydescription", columnDefinition = "TEXT") // 商機描述
    private String description;

    @Column(name = "opportunityclosedate")
    private LocalDate closeDate;

    @Column(name = "opportunityamount", precision = 14, scale = 2)
    private BigDecimal amount;

    @Column(name = "salespersonname")
    private String salesPersonName;

    @CreatedDate
    @Column(name = "opportunitycreated", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "opportunityupdated")
    private LocalDateTime updatedAt;

    // ----- 多對一 ------
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerid", insertable = false, updatable = false)
    private BCustomer BCustomer;


    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
        if (this.BCustomer != null && !Objects.equals(this.BCustomer.getCustomerId(), customerId)) {
            this.BCustomer = null;
        }
    }

    public BCustomer getCustomer(){
        return BCustomer;
    }

    public void setCustomer(BCustomer BCustomer) {
        this.BCustomer = BCustomer;
        if (BCustomer != null) {
            this.customerId = BCustomer.getCustomerId();
        } else {
            this.customerId = null;
        }
    }




}
