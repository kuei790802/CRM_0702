package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Table(name = "authority")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authorityId;

    // 權限項目代碼：如 "user_create"
    @Column(unique = true, nullable = false)
    private String code;

    // 對應頁面或模組名稱，如 "使用者管理"
    private String module;

    // 功能說明，如 "建立使用者帳號"
    private String description;

    // 顯示在權限勾選畫面或權限管理列表，讓非技術人員看得懂
    private String displayName;

    // 例如："USER_MANAGEMENT", "CUSTOMER_SERVICE"
    @Column(nullable = false)
    private String moduleGroup;

    @ManyToMany(mappedBy = "authorities")
    private List<User> users;
}
