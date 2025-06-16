package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "platforms")
@Getter
@Setter
public class Platform {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long platformid;
    private String platformname;

    @ManyToOne
    @JoinColumn(name = "employeeid")
    private Employee employee;

    private LocalDateTime createat;
    private LocalDateTime updateat;
}
