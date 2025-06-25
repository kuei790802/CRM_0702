package com.example.demo.controller;

import com.example.demo.config.EcpayProperties;
import com.example.demo.entity.Order;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.EcpayService;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/api/logistics")
public class LogisticsController {

    @Autowired
    private EcpayService ecpayService;
    @Autowired
    private OrderService orderService;
    // 移除 OrderRepository 的注入，這個職責應該在 Service 層

    /**
     * 【修改】此端點用來產生一個表單，並自動將使用者導向綠界物流選擇頁面
     * 現在由 Service 直接處理所有邏輯並返回參數
     */
    @GetMapping("/select/{orderId}")
    public String selectLogistics(@PathVariable Long orderId, Model model) {
        // 直接呼叫 Service 層，傳入 ID 和 model
        ecpayService.prepareLogisticsRedirect(orderId, model);

        // 回傳模板名稱，Thymeleaf 會渲染此模板
        return "ecpay-logistics-redirect";
    }

    /**
     * 【新增】接收使用者在綠界地圖選擇門市後的回調
     */
    @PostMapping("/store-reply")
    @ResponseBody
    public String handleStoreSelectionReply(@RequestParam Map<String, String> replyData) {
        System.out.println("收到門市選擇結果: " + replyData);
        try {
            orderService.processStoreSelection(replyData);
            return "1|OK";
        } catch (Exception e) {
            System.err.println("處理門市選擇結果失敗: " + e.getMessage());
            return "0|Error";
        }
    }

    /**
     * 【新增】接收綠界物流狀態更新的回調
     * @param callbackData 綠界以 POST form-data 形式發送的資料
     * @return 回應給綠界的字串 "1|OK"
     */
    @PostMapping("/callback")
    @ResponseBody
    public String handleLogisticsCallback(@RequestParam Map<String, String> callbackData) {
        System.out.println("收到綠界物流回調: " + callbackData);

        try {
            orderService.processLogisticsCallback(callbackData);
            return "1|OK";
        } catch (Exception e) {
            System.err.println("處理綠界物流回調失敗: " + e.getMessage());
            return "1|OK";
        }
    }
}