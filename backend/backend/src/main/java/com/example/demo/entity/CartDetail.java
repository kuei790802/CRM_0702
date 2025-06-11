package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "cartdetails")
@Getter
@Setter
public class CartDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartdetailid;
    private Integer quantity;
    private LocalDateTime addat;

    //---------------
    @ManyToOne
    @JoinColumn(name = "cartid")
    private Cart cart;

    //-----------------
    @ManyToOne
    @JoinColumn(name = "productid")
    private Product product;
}
