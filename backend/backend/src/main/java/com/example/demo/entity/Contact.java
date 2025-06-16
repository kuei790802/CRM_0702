package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "contacts")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"customer"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long contactId;

    @Column(nullable = false, length = 100)
    private String contactName;

    @Column(length = 100)
    private String contactTitle;

    @Column(length = 150)
    private String email;

    @Column(length = 30)
    private String contactPhone;

    @Column(columnDefinition = "TEXT")
    private String contactNotes;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // ----- 多對一關聯：聯絡人所屬的客戶 -----
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

}
