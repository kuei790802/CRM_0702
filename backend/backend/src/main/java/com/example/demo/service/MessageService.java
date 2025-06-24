package com.example.demo.service;

import com.example.demo.dto.request.MessageCreateRequest;
import com.example.demo.dto.request.MessageReplyCreateRequest;
import com.example.demo.dto.response.MessageResponse;
import com.example.demo.entity.CCustomer;
import com.example.demo.entity.Message;
import com.example.demo.entity.MessageReply;
import com.example.demo.enums.SenderType;
import com.example.demo.repository.CCustomerRepo;
import com.example.demo.repository.MessageReplyRepo;
import com.example.demo.repository.MessageRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final MessageRepo messageRepo;
    private final MessageReplyRepo messageReplyRepo;
    private final CCustomerRepo cCustomerRepo;

    public MessageService(MessageRepo messageRepo, MessageReplyRepo messageReplyRepo, CCustomerRepo cCustomerRepo) {
        this.messageRepo = messageRepo;
        this.messageReplyRepo = messageReplyRepo;
        this.cCustomerRepo = cCustomerRepo;
    }


//    - 查詢留言列表
//    - 標記已解決、關閉主題等

    // 建立開始提問/留言主題
    @Transactional
    public MessageResponse createMessage(Long customerId, MessageCreateRequest req) {
        // 拉是哪個客戶的留言
        CCustomer customer = cCustomerRepo.findById(customerId).orElseThrow(
                () -> new RuntimeException("查無此客戶"));

        Message message = Message.builder()
                .cCustomer(customer)
                .questionTitle(req.getQuestionTitle())
                .build();

        Message savedMessage = messageRepo.save(message);

        // 建立第一筆留言
        MessageReply firstReply = MessageReply.builder()
                .message(savedMessage)
                .cCustomer(customer)
                .senderType(SenderType.CUSTOMER)
                .content(req.getContent())
                .build();
        messageReplyRepo.save(firstReply);

        return new MessageResponse(
                savedMessage.getMessageId(),
                savedMessage.getQuestionTitle(),
                savedMessage.getIsResolved(),
                savedMessage.getCreatedAt(),
                savedMessage.getCCustomer().getCustomerId(),
                firstReply.getContent(),
                firstReply.getSentAt()
        );
    }

    // 查詢該顧客所有留言
    public List<MessageResponse> getMessagesByCustomer(Long customerId) {
        List<Message> messages = messageRepo.findBycCustomer_CustomerId(customerId);
        return messages.stream().map(this::toResponse).collect(Collectors.toList());
    }

    // 依留言id查詢留言
    public Optional<Message> getMessageById(Long messageId) {
        return messageRepo.findById(messageId);
    }

    private MessageResponse toResponse(Message message) {
        MessageResponse resp = new MessageResponse();
        resp.setMessageId(message.getMessageId());
        resp.setCustomerId(message.getCCustomer().getCustomerId());
        resp.setQuestionTitle(message.getQuestionTitle());
        resp.setCreatedAt(message.getCreatedAt());
        resp.setIsResolved(message.getIsResolved());

        // 可以選擇只取最後一筆回覆當 summary（如果要）
        List<MessageReply> replies = message.getReplies();
        if (replies != null && !replies.isEmpty()) {
            MessageReply lastReply = replies.get(replies.size() - 1);
            resp.setLastReplyContent(lastReply.getContent());
            resp.setLastReplyTime(lastReply.getSentAt());
        }

        return resp;
    }
}
