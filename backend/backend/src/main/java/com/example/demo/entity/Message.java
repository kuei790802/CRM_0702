package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    // 客戶留言內容
    private String content;

    // 客戶留言時間
    private LocalDateTime createdAt;

    // 後台回覆內容（可空）
    private String reply;

    // 回覆時間（可空）
    private LocalDateTime replyAt;

    // 客戶發的留言
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CCustomer cCustomer;

    // 哪個後台人員回覆
    @ManyToOne
    @JoinColumn(name = "replied_by_user_id")
    private User repliedBy;
}
