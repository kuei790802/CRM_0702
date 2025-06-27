package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employees")
@Getter
@Setter
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeid;
    private String name;
    private String email;
    private String phone;
    private String title;
    private LocalDate hiredate;
    private LocalDate leavedate;
    private LocalDateTime createat;
    private LocalDateTime updateat;

    @OneToMany(mappedBy = "employee")
    private List<Platform> platform = new ArrayList<>();
}
