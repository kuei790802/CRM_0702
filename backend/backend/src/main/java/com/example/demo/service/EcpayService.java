package com.example.demo.service;

import com.example.demo.config.EcpayProperties;
import com.example.demo.dto.ecpay.AioCheckoutDto;
import com.example.demo.dto.ecpay.EcpayRequestDto;
import com.example.demo.dto.ecpay.LogisticsAllInOneDto;
import com.example.demo.entity.CCustomerAddress;
import com.example.demo.entity.Order;
import com.example.demo.enums.PaymentMethod;
import com.example.demo.repository.OrderRepository;
import com.example.demo.util.EcpayAesUtil;
import com.example.demo.util.EcpayCheckMacValueUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class EcpayService {

    @Autowired
    private EcpayProperties ecpayProperties;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private OrderRepository orderRepository;

    /**
     * 金流 AIO 結帳
     * 【修改】方法簽名改變，直接接收 Order 物件
     */
    public AioCheckoutDto createAioOrder(Order order) { // 原本是 (Integer totalAmount, String itemName, String merchantTradeNo)
        AioCheckoutDto dto = new AioCheckoutDto();
        EcpayProperties.Aio aioConfig = ecpayProperties.getAio();

        // === 【修改重點】從 Order 的 orderDetails 動態產生商品名稱 ===
        String itemName = order.getOrderDetails().stream()
                .map(detail -> detail.getProduct().getProductname()) // 取得每個品項的名稱
                .collect(Collectors.joining("#")); // 用 # 符號組合起來

        dto.setMerchantID(aioConfig.getMerchantId());
        dto.setMerchantTradeNo(order.getMerchantTradeNo()); // 從 order 物件取得
        dto.setMerchantTradeDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
        dto.setTotalAmount(order.getTotalAmount().intValue()); // 從 order 物件取得
        dto.setTradeDesc("訂單編號: " + order.getMerchantTradeNo()); // 交易描述也可以更具體
        dto.setItemName(itemName); // 設定我們剛剛組合好的商品名稱字串
        dto.setReturnURL(aioConfig.getReturnUrl());

        // 將 DTO 轉換為 Map 並產生 CheckMacValue
        Map<String, String> params = dto.toMap();
        String checkMacValue = EcpayCheckMacValueUtil.generate(
                params,
                aioConfig.getHashKey(),
                aioConfig.getHashIv()
        );
        dto.setCheckMacValue(checkMacValue);

        return dto;
    }

    /**
     * 【新增】準備物流選擇頁面所需的所有參數
     * 這個方法會在一個交易內完成，避免 LazyInitializationException
     * @param orderId 訂單ID
     * @param model Spring UI Model
     */
    @Transactional(readOnly = true) // 標示為唯讀交易，效能更好
    public void prepareLogisticsRedirect(Long orderId, Model model) {
        // 1. 根據 orderId 找到訂單
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("找不到訂單 ID: " + orderId));

        // 2. 呼叫現有的方法產生需要的參數
        Map<String, String> params = this.createLogisticsRedirectParams(order);

        // 3. 將參數和目標 URL 加入 Model
        model.addAttribute("logisticsUrl", ecpayProperties.getLogistics().getUrl());
        model.addAttribute("params", params);
    }

    // createLogisticsRedirectParams 方法簽名不變，但現在由 prepareLogisticsRedirect 呼叫
    /**
     * 【修改】產生用於導向到「全方位物流 RWD 頁面」的請求參數 Map
     * @param order 您的訂單實體
     * @return 包含所有參數的 Map，可直接用於渲染 Thymeleaf 表單
     */
    public Map<String, String> createLogisticsRedirectParams(Order order) {

        EcpayProperties.Logistics logisticsConfig = ecpayProperties.getLogistics();
        Map<String, String> params = new TreeMap<>();

        params.put("MerchantID", logisticsConfig.getMerchantId());
        params.put("MerchantTradeNo", order.getMerchantTradeNo());
        params.put("MerchantTradeDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
        params.put("LogisticsType", "CVS"); // CVS 表示超商取貨
        params.put("LogisticsSubType", "UNIMART_C2C"); // 這裡以統一超商 C2C 為例，可改為 FAMI, HILIFE
        params.put("GoodsAmount", String.valueOf(order.getTotalAmount().longValue()));

        // ====================== 【新增的關鍵邏輯】 ======================
        if (order.getPaymentMethod() == PaymentMethod.CASH_ON_DELIVERY) {
            params.put("IsCollection", "Y"); // Y: 代收貨款
        } else {
            params.put("IsCollection", "N"); // N: 純取貨
        }
        // ===============================================================

        params.put("GoodsName", "網站商品一批");
        params.put("SenderName", logisticsConfig.getSenderName());
        params.put("ServerReplyURL", logisticsConfig.getServerReplyUrl());
        params.put("ClientReplyURL", logisticsConfig.getClientReplyUrl());

        // 計算 CheckMacValue
        String checkMacValue = EcpayCheckMacValueUtil.generate(params, logisticsConfig.getHashKey(), logisticsConfig.getHashIv());
        params.put("CheckMacValue", checkMacValue);

        return params;
    }

    /**
     * 【新增】步驟 B：呼叫 API 建立物流訂單
     */
    public String createLogisticsOrder(Order order, Map<String, String> storeReplyData) {
        String createApiUrl = "https://logistics-stage.ecpay.com.tw/Express/Create";

        EcpayProperties.Logistics logisticsConfig = ecpayProperties.getLogistics();
        Map<String, String> params = new TreeMap<>();
        params.put("MerchantID", logisticsConfig.getMerchantId());
        params.put("MerchantTradeNo", order.getMerchantTradeNo());
        params.put("MerchantTradeDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
        params.put("LogisticsType", "CVS");
        params.put("LogisticsSubType", storeReplyData.get("LogisticsSubType"));
        params.put("GoodsAmount", String.valueOf(order.getTotalAmount().longValue()));
        params.put("GoodsName", "網站商品一批");

        // 來自門市選擇回調的關鍵資訊
        params.put("CVSStoreID", storeReplyData.get("CVSStoreID"));
        params.put("CVSRecipientName", order.getCCustomerAddress().getName()); // 假設收件人名稱在訂單地址中
        params.put("CVSRecipientCellPhone", order.getCCustomerAddress().getPhone()); // 假設收件人電話在訂單地址中

        // 寄件人資訊
        params.put("SenderName", logisticsConfig.getSenderName());
        params.put("SenderCellPhone", logisticsConfig.getSenderCellphone());

        // 回調網址
        params.put("ServerReplyURL", logisticsConfig.getServerReplyUrl().replace("/store-reply", "/callback")); // 注意，這裡用的是接收「狀態」的 callback

        String checkMacValue = EcpayCheckMacValueUtil.generate(params, logisticsConfig.getHashKey(), logisticsConfig.getHashIv());
        params.put("CheckMacValue", checkMacValue);

        // 發送 Server-to-Server 請求
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // ====================== 【以下為修正的程式碼】 ======================
// 1. 先建立一個空的 LinkedMultiValueMap
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

// 2. 使用 .setAll() 方法將您原本的 params Map 轉換並填入
        formData.setAll(params);

// 3. 使用新的 formData 物件來建立 HttpEntity
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);
// ===============================================================

        ResponseEntity<String> response = restTemplate.postForEntity(createApiUrl, request, String.class);
        return response.getBody(); // 回傳綠界的結果，例如 "1|OK"
    }

    /**
     * 【新增】專門用於建立「宅配」物流訂單的方法
     * @param order 您的訂單實體
     * @return 綠界回傳的結果
     */
    public String createHomeDeliveryOrder(Order order) {
        String createApiUrl = "https://logistics-stage.ecpay.com.tw/Express/Create";

        EcpayProperties.Logistics logisticsConfig = ecpayProperties.getLogistics();
        Map<String, String> params = new TreeMap<>();
        params.put("MerchantID", logisticsConfig.getMerchantId());
        params.put("MerchantTradeNo", order.getMerchantTradeNo());
        params.put("MerchantTradeDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
        params.put("LogisticsType", "Home"); // <<--【關鍵差異】類型為 Home
        params.put("LogisticsSubType", "TCAT"); // <<--【關鍵差異】子類型為 TCAT (黑貓)
        params.put("GoodsAmount", String.valueOf(order.getTotalAmount().longValue()));
        params.put("GoodsName", "網站商品一批");

        // 收件人資訊，直接從訂單關聯的地址物件中取得
        CCustomerAddress address = order.getCCustomerAddress();
        params.put("ReceiverName", address.getName());
        params.put("ReceiverCellPhone", address.getPhone());
        params.put("ReceiverZipCode", address.getZipcode());
        params.put("ReceiverAddress", address.getFullAddress());

        // 寄件人資訊
        params.put("SenderName", logisticsConfig.getSenderName());
        params.put("SenderCellPhone", logisticsConfig.getSenderCellphone());

        // 回調網址，用來接收後續的物流狀態更新
        params.put("ServerReplyURL", logisticsConfig.getServerReplyUrl().replace("/store-reply", "/callback"));

        // 計算 CheckMacValue
        String checkMacValue = EcpayCheckMacValueUtil.generate(params, logisticsConfig.getHashKey(), logisticsConfig.getHashIv());
        params.put("CheckMacValue", checkMacValue);

        // 發送 Server-to-Server 請求
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.setAll(params);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(createApiUrl, request, String.class);
        return response.getBody();
    }
}