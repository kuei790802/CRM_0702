package com.example.demo.entity;

import com.example.demo.enums.BCustomerIndustry;
import com.example.demo.enums.BCustomerLevel;
import com.example.demo.enums.BCustomerType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder; // ✨ 修改 #1: 引入 SuperBuilder
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "b_customers")
// ✨ 修改 #2: 繼承 CustomerBase
// @EntityListeners(AuditingEntityListener.class) // 從 CustomerBase 繼承，不需重複
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder // ✨ 修改 #3: 將 @Builder 改為 @SuperBuilder
@ToString(exclude = {"contacts", "tags"})
// ✨ 修改 #4: 加上 DiscriminatorValue
@DiscriminatorValue("B2B")
// @EqualsAndHashCode(onlyExplicitlyIncluded = true) // CustomerBase已有，若要覆寫請小心
public class BCustomer extends CustomerBase { // ✨ 修改 #2: 繼承 CustomerBase

    // ✨ 修改 #5: 刪除所有與 CustomerBase 重複的欄位
    // private Long customerId;
    // private String customerName;
    // private String customerAddress;
    // private String customerTel;
    // private String customerEmail;
    // private LocalDateTime createdAt;
    // private LocalDateTime updatedAt;

    // 以下是 BCustomer 特有的欄位，予以保留
    @Enumerated(EnumType.STRING)
    @Column(length = 100)
    private BCustomerIndustry industry;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private BCustomerType bCustomerType; // 欄位名稱建議改為 bCustomerType

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private BCustomerLevel bCustomerLevel; // 欄位名稱建議改為 bCustomerLevel

    // 假設 B2B 客戶有統一編號
    @Column(name = "tin_number", length = 20)
    private String tinNumber;


    // ----- 關聯欄位維持不變 -----
    @OneToMany(mappedBy = "bCustomer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Contact> contacts = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "b_customer_tags",
            joinColumns = @JoinColumn(name = "b_customer_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    // ----- 關聯管理輔助方法維持不變 -----
    public void addContact(Contact contact) {
        contacts.add(contact);
        contact.setBCustomer(this);
    }

    public void removeContact(Contact contact) {
        contacts.remove(contact);
        contact.setBCustomer(null);
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
        tag.getBCustomers().add(this);
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getBCustomers().remove(this);
    }
}