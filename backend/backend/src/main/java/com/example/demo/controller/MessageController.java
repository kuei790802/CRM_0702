package com.example.demo.controller;

import com.example.demo.dto.request.MessageCreateRequest;
import com.example.demo.dto.response.MessageResponse;
import com.example.demo.security.CheckJwt;
import com.example.demo.service.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer/message")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService){
        this.messageService = messageService;
    }

    @CheckJwt
    @PostMapping("/create")
    public ResponseEntity<MessageResponse> createMessage(
            HttpServletRequest request,
            @RequestBody MessageCreateRequest messageCreateRequest){

        String account = (String) request.getAttribute("account");
        // TODO: 用 account 查 customerId，假設 service 有方法轉換
        Long customerId = getCustomerIdByAccount(account);

        MessageResponse created = messageService.createMessage(customerId, messageCreateRequest);

        return ResponseEntity.ok(created);
    }

    @CheckJwt
    @GetMapping("/list")
    public ResponseEntity<List<MessageResponse>> getMessages(HttpServletRequest request){
        String account = (String) request.getAttribute("account");
        Long customerId = getCustomerIdByAccount(account);

        List<MessageResponse> messages = messageService.getMessagesByCustomer(customerId);
        return ResponseEntity.ok(messages);
    }

    // TODO: 實作 getCustomerIdByAccount 或改用 service 注入 CCustomerService 查詢
    private Long getCustomerIdByAccount(String account){
        // 範例：請自行實作
        return 1L; // placeholder
    }
}
