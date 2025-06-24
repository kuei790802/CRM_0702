package com.example.demo.controller;

import com.example.demo.dto.request.MessageCreateRequest;
import com.example.demo.dto.request.MessageReplyCreateRequest;
import com.example.demo.dto.response.MessageReplyResponse;
import com.example.demo.dto.response.MessageResponse;
import com.example.demo.entity.MessageReply;
import com.example.demo.enums.AuthorityCode;
import com.example.demo.enums.SenderType;
import com.example.demo.security.CheckAuthority;
import com.example.demo.security.CheckJwt;
import com.example.demo.service.CCustomerService;
import com.example.demo.service.MessageReplyService;
import com.example.demo.service.MessageService;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customer/message")
public class MessageController {

    private final MessageService messageService;
    private final MessageReplyService messageReplyService;
    private final UserService userService;
    private final CCustomerService cCustomerService;

    public MessageController(MessageService messageService, MessageReplyService messageReplyService, UserService userService, CCustomerService cCustomerService){
        this.messageService = messageService;
        this.messageReplyService = messageReplyService;
        this.userService = userService;
        this.cCustomerService = cCustomerService;
    }

    // 建立留言主題
    @CheckJwt
    @PostMapping("/create")
    public ResponseEntity<MessageResponse> createMessage(
            HttpServletRequest request,
            @RequestBody MessageCreateRequest messageCreateRequest){

        String account = (String) request.getAttribute("account");
        Long customerId = cCustomerService.getCustomerIdByAccount(account);

        MessageResponse created = messageService.createMessage(customerId, messageCreateRequest);

        return ResponseEntity.ok(created);
    }

    // 客戶留言回覆
    @CheckJwt
    @PostMapping("/{messageId}/reply/customer")
    public ResponseEntity<MessageReplyResponse> replyAsCustomer(
            HttpServletRequest request,
            @PathVariable Long messageId,
            @RequestBody MessageReplyCreateRequest requestDto
    ) {
        String account = (String) request.getAttribute("account");
        Long customerId = cCustomerService.getCustomerIdByAccount(account);
        MessageReplyResponse reply = messageReplyService.replyByCustomer(customerId, messageId, requestDto.getContent());
        return ResponseEntity.ok(reply);
    }

    // 管理員留言回覆
    @CheckJwt
    @PostMapping("/{messageId}/reply/admin")
    @CheckAuthority(AuthorityCode.CUSTOMER_SUPPORT) // 自定義權限驗證註解
    public ResponseEntity<MessageReplyResponse> replyAsAdmin(
            HttpServletRequest request,
            @PathVariable Long messageId,
            @RequestBody MessageReplyCreateRequest requestDto
    ) {
        String account = (String) request.getAttribute("account");
        Long userId = userService.getUserIdByAccount(account);
        MessageReplyResponse reply = messageReplyService.replyByUser(userId, messageId, requestDto.getContent());
        return ResponseEntity.ok(reply);
    }


    // 調閱客戶、員工所有留言
    @CheckJwt
    @GetMapping("/list")
    public ResponseEntity<List<MessageResponse>> getMessages(HttpServletRequest request){
        String account = (String) request.getAttribute("account");
        Long customerId = cCustomerService.getCustomerIdByAccount(account);

        List<MessageResponse> messages = messageService.getMessagesByCustomer(customerId);
        return ResponseEntity.ok(messages);
    }

    // 查詢留言對話內容
    @CheckJwt
    @GetMapping("/{messageId}/replies")
    public ResponseEntity<List<MessageReplyResponse>> getReplies(
            @PathVariable Long messageId
    ) {
        List<MessageReplyResponse> replies = messageReplyService.getRepliesForMessage(messageId);
        return ResponseEntity.ok(replies);
    }

}
