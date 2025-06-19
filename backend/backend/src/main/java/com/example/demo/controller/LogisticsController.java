package com.example.demo.controller;

import com.example.demo.config.EcpayProperties;
import com.example.demo.dto.ecpay.EcpayRequestDto;
import com.example.demo.dto.ecpay.EcpayLogisticsResponseDto; // 引入新的 DTO
import com.example.demo.service.EcpayService;
import com.example.demo.util.EcpayAesUtil; // 引入 AES 工具
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class LogisticsController {

    @Autowired
    private EcpayService ecpayService;

    @Autowired
    private EcpayProperties ecpayProperties;

    @GetMapping("/logistics/select")
    public ResponseEntity<String> selectLogistics() {
        // 1. 準備請求資料
        EcpayRequestDto<String> requestDto = ecpayService.redirectToLogisticsSelection(1200, "精美Java書籍一本");
        String logisticsRwdUrl = ecpayProperties.getLogistics().getUrl();

        // 2. 發送請求到綠界
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders ecpayHeaders = new HttpHeaders();
        ecpayHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EcpayRequestDto<String>> request = new HttpEntity<>(requestDto, ecpayHeaders);

        // 3. 接收綠界回傳的 JSON，並讓 RestTemplate 將其自動轉換為 EcpayLogisticsResponseDto 物件
        ResponseEntity<EcpayLogisticsResponseDto> responseFromEcpay = restTemplate.postForEntity(
                logisticsRwdUrl,
                request,
                EcpayLogisticsResponseDto.class
        );

        // 4. 從回應中取得加密的 Data
        EcpayLogisticsResponseDto responseBody = responseFromEcpay.getBody();
        if (responseBody == null || responseBody.getTransCode() != 1) {
            // 如果交易失敗或沒有內容，回傳錯誤訊息
            String errorMessage = (responseBody != null) ? responseBody.getTransMsg() : "ECPay response is empty.";
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String encryptedData = responseBody.getData();

        // 5. 使用 AES 工具解密 Data，得到最終的 HTML
        String decryptedHtml = EcpayAesUtil.decrypt(
                encryptedData,
                ecpayProperties.getLogistics().getHashKey(),
                ecpayProperties.getLogistics().getHashIv()
        );

        // 6. 準備回傳給瀏覽器的 Headers，設定為 HTML
        HttpHeaders browserHeaders = new HttpHeaders();
        browserHeaders.setContentType(MediaType.TEXT_HTML);

        // 7. 將解密後的 HTML 回傳給瀏覽器
        return new ResponseEntity<>(decryptedHtml, browserHeaders, HttpStatus.OK);
    }
}