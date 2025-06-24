package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "customers")
@DiscriminatorValue("B2B")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
//@EntityListeners(AuditingEntityListener.class)
public class BCustomer extends CustomerBase{




    @Column(name = "industry")
    private String industry;

    @Column(name = "customer_type_detail")
    private String type;

    @Column(name = "sourceid")
    private Long sourceId;

    @Column(name = "customerlevel")
    private String level;

    @Column(name = "TIN_number")
    private String tinNumber;



//    @CreatedDate
//    @Column(name = "customercreated", updatable = false)
//    private LocalDateTime createdAt;
//
//    @LastModifiedDate
//    @Column(name = "customerupdated")
//    private LocalDateTime updatedAt;

    // ----- 多對多 -----
   @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "tagmaps",
            joinColumns = @JoinColumn(name = "customerid"),
            inverseJoinColumns = @JoinColumn(name = "tagid")
    )
    private Set<Tag> tags = new HashSet<>();

//    public BCustomer() {}
//    public BCustomer(String name) {
//        setName(name);
    }



    // Getter and Setter
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getIndustry() {
//        return industry;
//    }
//
//    public void setIndustry(String industry) {
//        this.industry = industry;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    public Long getSourceId() {
//        return sourceId;
//    }
//
//    public void setSourceId(Long sourceid) {
//        this.sourceId = sourceId;
//    }
//
//    public String getLevel() {
//        return level;
//    }
//
//    public void setLevel(String level) {
//        this.level = level;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }
//
//    public String getTel() {
//        return tel;
//    }
//
//    public void setTel(String tel) {
//        this.tel = tel;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public LocalDateTime getCreatedAt() {
//        return createdAt;
//    }
//
////    public void setCreatedAt(LocalDateTime createdAt) {
////        this.createdAt = createdAt;
////    }
//
//    public LocalDateTime getUpdatedAt() {
//        return updatedAt;
//    }
//
////    public void setUpdatedAt(LocalDateTime updatedAt) {
////        this.updatedAt = updatedAt;
////    }
//
//    // -----
//    public void addTag(Tag tag) {
//        this.tags.add(tag);
//    }
//
//    public void removeTag(Tag tag) {
//        this.tags.remove(tag);
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        // 檢查物件是否為 null 或類別是否不一致
//        if (o == null || getClass() != o.getClass()) return false;
//        BCustomer BCustomer = (BCustomer) o;
//        // 如果 ID 已生成，則使用 ID 進行比較；如果 ID 尚未生成（即為新實體），則使用物件引用比較 (super.equals(o))
//        // 這種做法可以避免在 ID 未生成時，兩個不同的新實體被誤判為相同。
//        return id != null && Objects.equals(id, BCustomer.id);
//    }
//
//    @Override
//    public int hashCode() {
//        // 如果 ID 已生成，則使用 ID 的 hashCode；如果 ID 尚未生成，則返回 Objects.hash(super.hashCode()) 或一個常數。
//        // 與 equals() 方法保持一致性是關鍵。
//        return id != null ? Objects.hash(id) : super.hashCode();
//    }
//
//    // --- 覆寫 toString() 方法 (方便除錯) ---
//    // 覆寫 toString() 可以方便地在日誌或除錯時查看實體的屬性值。
//    // 注意：避免在 toString() 中包含懶載入的關聯集合（如 `tags`），因為這可能導致 N+1 問題
//    // 或在未正確初始化集合時拋出 LazyInitializationException。
//    @Override
//    public String toString() {
//        return "Customer{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", industry='" + industry + '\'' +
//                ", type='" + type + '\'' +
//                ", sourceid=" + sourceId +
//                ", level='" + level + '\'' +
//                ", address='" + address + '\'' +
//                ", tel='" + tel + '\'' +
//                ", email='" + email + '\'' +
//                ", createdAt=" + createdAt +
//                ", updatedAt=" + updatedAt +
//                '}';
//    }


