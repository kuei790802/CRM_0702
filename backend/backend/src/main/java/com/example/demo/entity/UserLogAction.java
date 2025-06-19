package com.example.demo.entity;

import com.example.demo.enums.ActionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "userlogaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLogAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActionType actionType;

    private String description;
    private String targetTable;
    private String targetId;
    private String ipAddress;
    private String userAgent;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
