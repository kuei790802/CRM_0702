package com.example.demo.entity;

import jakarta.persistence.*;

import java.util.Objects;


@Entity
@Table(name = "contacts")
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

    public Contact() {}

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
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return id != null && Objects.equals(id, contact.id);
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hash(id) : super.hashCode();
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", customerid=" + customerId +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
