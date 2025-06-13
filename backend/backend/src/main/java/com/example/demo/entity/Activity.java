package com.example.demo.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "activities")
@EntityListeners(AuditingEntityListener.class)
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity")
    private Long id;

    @Column(name = "relatedtype")
    private String relatedType;

    @Column(name = "relatedid")
    private Long relatedId;

    @Column(name = "activitytype", nullable = false)
    private String type;

    @Column(name = "activitydate", nullable = false)
    private LocalDateTime activityDate;

    @Column(name = "activitydescription", columnDefinition = "TEXT")
    private String activityDescription;

    @Column(name = "activitynotes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "salespersonname")
    private String salesPersonName;

    @Column(name = "activitystatus", nullable = false)
    private String status;

    @CreatedDate
    @Column(name = "activitycreated", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "activityupdated")
    private LocalDateTime updatedAt;

    // ----- 多對ㄧ 活動與客戶 -----
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "relatedid", referencedColumnName = "customerid", insertable = false, updatable = false)
    private BCustomer BCustomer;

    // ----- 多對一 活動與商機 -----
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "relatedid", referencedColumnName = "opportunityid", insertable = false, updatable = false)
    private Opportunity opportunity;

    public Activity() {}


    // Getter and Setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRelatedType() {
        return relatedType;
    }

    public void setRelatedType(String relatedType) {
        this.relatedType = relatedType;
    }

    public Long getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(Long relatedId) {
        this.relatedId = relatedId;
        this.BCustomer = null;
        this.opportunity = null;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(LocalDateTime activityDate) {
        this.activityDate = activityDate;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getSalesPersonName() {
        return salesPersonName;
    }

    public void setSalesPersonName(String salesPersonName) {
        this.salesPersonName = salesPersonName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public BCustomer getBCustomer() {
        return BCustomer;
    }

    public void setBCustomer(BCustomer BCustomer) {
        this.BCustomer = BCustomer;
        if (BCustomer != null) {
            this.relatedId = BCustomer.getId();
            this.relatedType = "Customer"; // 自動設定 relatedType
        } else {
            this.relatedId = null;
            this.relatedType = null;
        }
    }

    public Opportunity getOpportunity() {
        return opportunity;
    }

    public void setOpportunity(Opportunity opportunity) {
        this.opportunity = opportunity;
        if (opportunity != null) {
            this.relatedId = opportunity.getId();
            this.relatedType = "Opportunity";
        } else {
            this.relatedId = null;
            this.relatedType = null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return id != null && Objects.equals(id, activity.id);
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hash(id) : super.hashCode();
    }


    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", relatedType='" + relatedType + '\'' +
                ", relatedId=" + relatedId +
                ", type='" + type + '\'' +
                ", activityDate=" + activityDate +
                ", notes='" + notes + '\'' +
                ", activityDescription='" + activityDescription + '\'' +
                ", salesPersonName='" + salesPersonName + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
