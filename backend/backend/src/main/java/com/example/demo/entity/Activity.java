package com.example.demo.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

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
    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

//    public void setCreatedAt(LocalDateTime createdAt) {
//        this.createdAt = createdAt;
//    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

//    public void setUpdatedAt(LocalDateTime updatedAt) {
//        this.updatedAt = updatedAt;
//    }
}
