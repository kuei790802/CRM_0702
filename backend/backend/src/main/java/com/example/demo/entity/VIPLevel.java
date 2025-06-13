package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "viplevel")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VIPLevel {
    @Id
    private String level; // "BRONZE"、"SILVER"…
    private double upgradeThreshold;
    private double downgradeThreshold;
    private String bonusDescription;
}

