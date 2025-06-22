package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;


@Entity
@Table(name = "contacts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "BCustomer")
@EqualsAndHashCode(of = "id")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contactid")
    private Long id;

    @Column(name = "customerid", nullable = false)
    private Long customerId;

    @Column(name = "contactname", nullable = false)
    private String name;

    @Column(name = "contacttitle")
    private String title;

    @Column(name = "contactphone")
    private String phone;

    @Column(name = "contactemail")
    private String email;

    @Column(name = "contactnotes")
    private String notes;



    // ----- 多對一 -----
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerid", insertable = false, updatable = false)
    private BCustomer BCustomer;

    public BCustomer getCustomer() {
        return BCustomer;
    }

    public void setCustomer(BCustomer BCustomer) {
        this.BCustomer = BCustomer;
        if (BCustomer != null) {
            // Fix: Use getCustomerId() instead of getId()
            this.customerId = BCustomer.getCustomerId();
        } else {
            this.customerId = null;
        }
    }

    // Custom setter for customerId to handle relationship consistency
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
        // Fix: Use getCustomerId() instead of getId()
        if (this.BCustomer != null && !Objects.equals(this.BCustomer.getCustomerId(), customerId)) {
            this.BCustomer = null;
        }
    }
}
