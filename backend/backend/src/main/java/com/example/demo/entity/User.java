package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @OneToOne(mappedBy = "user")
    private Cart cart;

}
