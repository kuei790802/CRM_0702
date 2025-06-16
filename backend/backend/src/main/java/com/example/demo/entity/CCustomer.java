package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class CCustomer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid")
    private Long userid;
    private String name;
    private String email;
    private String phone;
    private String password;
    private LocalDate registrationdate;
    private LocalDate lastlogindate;
    private Boolean isactive;
    private LocalDateTime createat;
    private LocalDateTime updateat;

    //----------------
    @OneToOne(mappedBy = "CCustomer")
    private Cart cart;

    @OneToMany(mappedBy = "CCustomer")
    private List<CCustomerAddress> CCustomerAddress = new ArrayList<>();

}
