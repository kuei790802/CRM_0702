package com.example.demo.entity;

import com.example.demo.enums.CustomerIndustry;
import com.example.demo.enums.CustomerLevel;
import com.example.demo.enums.CustomerType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "customers")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long customerId;

    @Column(nullable = false, length = 100)
    private String customerName;

    @Enumerated(EnumType.STRING)
    @Column(length = 100)
    private CustomerIndustry industry;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private CustomerType customerType;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private CustomerLevel customerLevel;

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

    // ----- 一對多關聯：客戶擁有的聯絡人集合 -----
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Contact> contacts = new HashSet<>();

    // ----- 關聯管理輔助方法 -----
    /**
     * 添加一個聯絡人到此客戶。
     * 同時維護聯絡人的客戶關聯（雙向），確保數據一致性。
     * @param contact 要添加的聯絡人實體
     */
    public void addContact(Contact contact) {
        contacts.add(contact);
        contact.setCustomer(this); // 設定聯絡人所屬的客戶
    }

    /**
     * 從此客戶移除一個聯絡人。
     * 同時清除聯絡人的客戶關聯，確保數據一致性。
     * @param contact 要移除的聯絡人實體
     */
    public void removeContact(Contact contact) {
        contacts.remove(contact);
        contact.setCustomer(null); // 清除聯絡人所屬的客戶
    }
}
