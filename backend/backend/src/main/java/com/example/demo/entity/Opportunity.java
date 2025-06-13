package com.example.demo.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "opportunities")
@EntityListeners(AuditingEntityListener.class)
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

    public Opportunity() {}

    // Getter and Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
        if (this.BCustomer != null && !Objects.equals(this.BCustomer.getId(), customerId)) {
            this.BCustomer = null;
        }
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(LocalDate closeDate) {
        this.closeDate = closeDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getSalesPersonName() {
        return salesPersonName;
    }

    public void setSalesPersonName(String salesPersonName) {
        this.salesPersonName = salesPersonName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

//    public void setCreatedAt(LocalDateTime createdAt) {
//        this.createdAt = createdAt;
//    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

//    public void setUpdatedAt(LocalDateTime updatedAt) {
//        this.updatedAt = updatedAt;
//    }

    public BCustomer getCustomer() {
        return BCustomer;
    }

    public void setCustomer(BCustomer BCustomer) {
        this.BCustomer = BCustomer;
        if (BCustomer != null) {
            this.customerId = BCustomer.getId();
        } else {
            this.customerId = null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Opportunity that = (Opportunity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hash(id) : super.hashCode();
    }

    @Override
    public String toString() {
        return "Opportunity{" +
                "id=" + id +
                ", customerid=" + customerId +
                ", stage='" + stage + '\'' +
                ", status='" + status + '\'' +
                ", closeDate=" + closeDate +
                ", amount=" + amount +
                ", salesPersonName='" + salesPersonName + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
