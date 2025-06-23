package com.example.demo.service;

import com.example.demo.dto.request.MessageCreateRequest;
import com.example.demo.dto.response.MessageResponse;
import com.example.demo.entity.Message;
import com.example.demo.repository.MessageRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final MessageRepo messageRepo;

    public MessageService(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    // 新增留言
//    public MessageResponse createMessage(Long customerId, MessageCreateRequest request) {
//        Message message = Message.builder()
//                .customerId(customerId)
//                .questionId(request.getQuestionId())
//                .content(request.getContent())
//                .createdAt(LocalDateTime.now())
//                .build();
//
//        Message saved = messageRepo.save(message);
//
//        return toResponse(saved);
//    }

    // 查詢該顧客所有留言
//    public List<MessageResponse> getMessagesByCustomer(Long customerId) {
//        List<Message> messages = messageRepo.findByCustomerId(customerId);
//        return messages.stream().map(this::toResponse).collect(Collectors.toList());
//    }

//    private MessageResponse toResponse(Message message) {
//        MessageResponse resp = new MessageResponse();
//        resp.setId(message.getId());
//        resp.setCustomerId(message.getCustomerId());
//        resp.setQuestionId(message.getQuestionId());
//        resp.setContent(message.getContent());
//        resp.setCreatedAt(message.getCreatedAt());
//        resp.setReply(message.getReply());
//        return resp;
//    }
}
