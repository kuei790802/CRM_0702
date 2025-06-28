package com.example.demo.entity;

import com.example.demo.enums.BCustomerIndustry;
import com.example.demo.enums.BCustomerLevel;
import com.example.demo.enums.BCustomerType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "b_customers")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@Builder
@SuperBuilder //TODO(joshkuei): Add for passing the test.
@DiscriminatorValue("B2B")
@ToString(exclude = {"contacts", "tags"})
@EqualsAndHashCode(callSuper = true,
        onlyExplicitlyIncluded = true)
public class BCustomer extends CustomerBase {

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

    @Column(length = 150)
    private String customerEmail;

    // ----- 關聯欄位維持不變 -----
    @Builder.Default
    @OneToMany(mappedBy = "bCustomer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Contact> contacts = new HashSet<>();

    // ----- 多對多關聯：一個客戶可以有多個標籤 -----
    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "b_customer_tags",
            joinColumns = @JoinColumn(name = "b_customer_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    // ----- 關聯管理輔助方法 -----
    /**
     * 添加一個聯絡人到此客戶。
     * 同時維護聯絡人的客戶關聯（雙向），確保數據一致性。
     * @param contact 要添加的聯絡人實體
     */
    public void addContact(Contact contact) {
        contacts.add(contact);
        contact.setBCustomer(this); // 設定聯絡人所屬的客戶
    }

    /**
     * 從此客戶移除一個聯絡人。
     * 同時清除聯絡人的客戶關聯，確保數據一致性。
     * @param contact 要移除的聯絡人實體
     */
    public void removeContact(Contact contact) {
        contacts.remove(contact);
        contact.setBCustomer(null); // 清除聯絡人所屬的客戶
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
