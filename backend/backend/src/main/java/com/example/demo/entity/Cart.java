package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "carts")
@Getter
@Setter
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartid;
    private LocalDateTime createat;
    private LocalDateTime updateat;

    //------------
    @OneToOne
    @JoinColumn(name = "userid")
    private User user;

    //------------
    @OneToMany(mappedBy = "cart")
    private List<CartDetail> cartdetails;

}
