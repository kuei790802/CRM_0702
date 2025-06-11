package com.example.demo.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "contacts")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contactid")
    private Long id;

    @Column(name = "customerid")
    private Long customerid;

    @Column(name = "contactname")
    private String name;

    @Column(name = "contacttitle")
    private String title;

    @Column(name = "contactphone")
    private String phone;

    @Column(name = "contactemail")
    private String email;

    @Column(name = "contactaddress")
    private String address;

    // ----- Join -----
    @ManyToOne
    @JoinColumn(name = "customerid", insertable = false, updatable = false)
    private Customer customer;

    // Getter and Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerid() {
        return customerid;
    }

    public void setCustomerid(Long customerid) {
        this.customerid = customerid;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
