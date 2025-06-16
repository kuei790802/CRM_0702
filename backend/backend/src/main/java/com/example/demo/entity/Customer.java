package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Entity
@Table(name = "customers")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long customerId;

    @Column(nullable = false, length = 100)
    private String customerName;

    @Enumerated(EnumType.STRING)
    @Column(length = 100)
    private String industry;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private String customerType;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private String customerLevel;

    @Column(length = 255)
    private String customerAddress;

    @Column(length = 30)
    private String customerTel;

    @Column(length = 150)
    private String customerEmail;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;


}
