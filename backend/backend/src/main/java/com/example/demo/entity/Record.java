package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "records")
@Getter
@Setter
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recordid")
    private Long id;

    @Column(name = "customerid")
    private Long customerId;

    @Column(name = "opportunityid")
    private Long opportunityId;

    @Column(name = "salesdate")
    private Date date;

    @Column(name = "totalamount")
    private Double amount;

    @Column(name = "salespersonname")
    private String salesPersonName;

    // Getter and Setter



}
