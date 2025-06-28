package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"bCustomers", "opportunities"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long tagId;

    @Column(nullable = false, unique = true, length = 100)
    private String tagName;

    @Column(length = 100)
    private String color;

    // ----- 多對多關聯：標籤可以關聯多個客戶 -----
    @Builder.Default
    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private Set<BCustomer> bCustomers = new HashSet<>();

    // ----- 多對多關聯：標籤可以關聯多個商機 -----
    @Builder.Default
    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private Set<Opportunity> opportunities = new HashSet<>();

    // 添加客戶關聯
    public void addBCustomer(BCustomer bCustomer) {
        this.bCustomers.add(bCustomer);
        bCustomer.getTags().add(this);
    }

    // 移除客戶關聯
    public void removeBCustomer(BCustomer bCustomer) {
        this.bCustomers.remove(bCustomer);
        bCustomer.getTags().remove(this);
    }

    // 添加商機關聯
    public void addOpportunity(Opportunity opportunity) {
        this.opportunities.add(opportunity);
        opportunity.getTags().add(this);
    }

    // 移除商機關聯
    public void removeOpportunity(Opportunity opportunity) {
        this.opportunities.remove(opportunity);
        opportunity.getTags().remove(this);
    }
}
