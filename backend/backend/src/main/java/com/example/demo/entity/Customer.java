package com.example.demo.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "customers")
@EntityListeners(AuditingEntityListener.class)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @Column(nullable = false, length = 100)
    private String customerName;

    @Column(length = 100)
    private String industry;

    @Column(length = 50)
    private String customerType;

    // ----- 多對一 -----
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id")
    private Source source;

    @Column(length = 50)
    private String customerLevel;

    @Column(length = 255)
    private String customerAddress;

    @Column(length = 30)
    private String customerTel;

    @Column(length = 150)
    private String customerEmail;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // ----- 多對多 -----
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "tagmaps",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    public Customer() {}
    public Customer(String customerName) {
        this.customerName = customerName;
    }


    // Getter and Setter
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getCustomerLevel() {
        return customerLevel;
    }

    public void setCustomerLevel(String customerLevel) {
        this.customerLevel = customerLevel;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerTel() {
        return customerTel;
    }

    public void setCustomerTel(String customerTel) {
        this.customerTel = customerTel;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    // ----- 關聯管理輔助方法 -----
    public void addTag(Tag tag) {
        this.tags.add(tag);
        tag.getCustomers().add(this);
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getCustomers().remove(this);
    }

    // --- equals, hashCode, toString ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // 使用 instanceof 檢查，可以處理代理物件 (Proxy) 的情況
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        // 確保 id 不為 null，才用 id 比較
        return customerId != null && Objects.equals(customerId, customer.getCustomerId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // --- 覆寫 toString() 方法 (方便除錯) ---
    // 覆寫 toString() 可以方便地在日誌或除錯時查看實體的屬性值。
    // 注意：避免在 toString() 中包含懶載入的關聯集合（如 `tags`），因為這可能導致 N+1 問題
    // 或在未正確初始化集合時拋出 LazyInitializationException。
    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                ", industry='" + industry + '\'' +
                ", customerType='" + customerType + '\'' +
                '}';
    }

}
