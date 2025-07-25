package com.example.demo.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="units")
@Getter
@Setter
public class Unit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unit_id")
    private Long unitId;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    

    @Column(name = "updated_by", nullable = false)
    private Long updatedBy;
}
