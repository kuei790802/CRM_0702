package com.example.demo.service;

import com.example.demo.config.EcpayConfig;
import com.example.demo.dto.ecpay.LogisticsData;
import com.example.demo.entity.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Formatter;

@Service
public class EcpayService {

    @Autowired
    private EcpayConfig ecpayConfig;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper; // Spring Boot 自動配置了 Jackson 的 ObjectMapper

    public String createLogisticsOrder(Order order) {
        try {
            // 1. 準備 Data 物件
            LogisticsData data = new LogisticsData();
            data.setMerchantID(ecpayConfig.getMerchantId());
            // 商店交易編號，必須唯一，我們直接用系統的訂單ID
            data.setMerchantTradeNo("b2c" + System.currentTimeMillis()); // 測試用，每次都產生新的
            data.setMerchantTradeDate(order.getCreateat().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
            data.setLogisticsType("CVS"); // 超商
            data.setLogisticsSubType("FAMI"); // 全家 (可根據需求修改為 UNIMART)
            double totalPrice = order.getOrderDetails().stream()
                    .mapToDouble(d -> d.getUnitprice() * d.getQuantity()).sum();
            data.setGoodsAmount((int) totalPrice);
            data.setCollectionAmount(0); // 這裡假設不代收貨款
            data.setIsCollection("N");
            data.setGoodsName("冰棒商品一批"); // 商品名稱
            // 寄件人資訊 (應從設定檔或資料庫讀取)
            data.setSenderName("冰棒天堂");
            data.setSenderPhone("0227881234");
            // 收件人資訊
            data.setReceiverName(order.getCCustomerAddress().getName());
            data.setReceiverPhone(order.getCCustomerAddress().getPhone());
            data.setReceiverCellPhone(order.getCCustomerAddress().getPhone());
            // 後端接收綠界通知的網址 (非常重要，但我們先用一個假網址)
            data.setServerReplyURL("https://www.yourdomain.com/api/logistics/callback");

            // 2. 將 Data 物件轉為 JSON 字串
            String jsonData = objectMapper.writeValueAsString(data);

            // 3. AES 加密
            String encryptedData = aesEncrypt(jsonData, ecpayConfig.getHashKey(), ecpayConfig.getHashIv());

            // 4. URL 編碼
            String urlEncodedData = URLEncoder.encode(encryptedData, StandardCharsets.UTF_8.toString());

            // 5. 準備要 POST 的表單資料
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("MerchantID", ecpayConfig.getMerchantId());
            map.add("RqHeader", "{\"Timestamp\":" + (System.currentTimeMillis() / 1000) + "}");
            map.add("Data", urlEncodedData);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            // 6. 發送請求並接收回應
            String response = restTemplate.postForObject(ecpayConfig.getApiUrl(), request, String.class);

            System.out.println("ECPay Response: " + response);
            return response; // 回傳綠界原始的回應字串

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("建立綠界物流訂單失敗", e);
        }
    }

    // AES 加密 (與 PHP 範例的邏輯對應)
    private String aesEncrypt(String data, String key, String iv) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(encryptedBytes).toLowerCase();
    }

    // 將 byte[] 轉換為 16 進位字串
    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        Formatter formatter = new Formatter(sb);
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        return sb.toString();
    }
}