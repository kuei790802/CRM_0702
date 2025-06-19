package com.example.demo.service;

import com.example.demo.config.EcpayProperties;
import com.example.demo.dto.ecpay.AioCheckoutDto;
import com.example.demo.dto.ecpay.EcpayRequestDto;
import com.example.demo.dto.ecpay.LogisticsAllInOneDto;
import com.example.demo.util.EcpayAesUtil;
import com.example.demo.util.EcpayCheckMacValueUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class EcpayService {

    @Autowired
    private EcpayProperties ecpayProperties;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 金流 AIO 結帳
     */
    public AioCheckoutDto createAioOrder(Integer totalAmount, String itemName, String merchantTradeNo) {
        AioCheckoutDto dto = new AioCheckoutDto();
        EcpayProperties.Aio aioConfig = ecpayProperties.getAio();

        dto.setMerchantID(aioConfig.getMerchantId());
        dto.setMerchantTradeNo(merchantTradeNo);
        dto.setMerchantTradeDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
        dto.setTotalAmount(totalAmount);
        dto.setTradeDesc("交易描述範例"); // 可自行修改
        dto.setItemName(itemName);
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
     * 產生用於導向到「全方位物流整合 RWD 頁面」的請求 DTO
     */
    public EcpayRequestDto<String> redirectToLogisticsSelection(int goodsAmount, String goodsName) {

        EcpayProperties.Logistics logisticsConfig = ecpayProperties.getLogistics();

        // 1. 準備 Data 內容
        LogisticsAllInOneDto dataDto = new LogisticsAllInOneDto();
        dataDto.setGoodsAmount(goodsAmount);
        dataDto.setGoodsName(goodsName);
        dataDto.setSenderName(logisticsConfig.getSenderName());
        dataDto.setSenderZipCode(logisticsConfig.getSenderZipCode());
        dataDto.setSenderAddress(logisticsConfig.getSenderAddress());
        dataDto.setServerReplyURL("https://your-domain.com/logistics/status-callback");
        dataDto.setClientReplyURL("https://your-domain.com/checkout/confirm-order"); // 使用者選完物流後，跳回的訂單確認頁

        try {
            // 2. 將 Data 內容序列化為 JSON 字串
            String dataJson = objectMapper.writeValueAsString(dataDto);

            // 3. 將 JSON 字串進行 AES 加密
            String encryptedData = EcpayAesUtil.encrypt(dataJson, logisticsConfig.getHashKey(), logisticsConfig.getHashIv());

            // 4. 準備最外層的請求 DTO
            EcpayRequestDto<String> requestDto = new EcpayRequestDto<>();
            requestDto.setMerchantID(logisticsConfig.getMerchantId());
            requestDto.setData(encryptedData);

            EcpayRequestDto.RqHeader header = new EcpayRequestDto.RqHeader();
            header.setTimestamp(System.currentTimeMillis() / 1000);
            header.setRevision("1.0.0"); // 根據文件
            requestDto.setRqHeader(header);

            return requestDto;

        } catch (Exception e) {
            throw new RuntimeException("Failed to create logistics selection request", e);
        }
    }
}