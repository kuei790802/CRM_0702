package com.example.demo.dto.ecpay;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LogisticsData {
    // --- 必填 ---
    @JsonProperty("MerchantID")
    private String merchantID; // 商店代號

    @JsonProperty("MerchantTradeNo")
    private String merchantTradeNo; // 商店交易編號 (你的訂單ID)

    @JsonProperty("MerchantTradeDate")
    private String merchantTradeDate; // 商店交易時間 yyyy/MM/dd HH:mm:ss

    @JsonProperty("LogisticsType")
    private String logisticsType; // 物流類型 (CVS: 超商)

    @JsonProperty("LogisticsSubType")
    private String logisticsSubType; // 物流子類型 (FAMI: 全家, UNIMART: 7-11)

    @JsonProperty("GoodsAmount")
    private int goodsAmount; // 商品金額

    @JsonProperty("CollectionAmount")
    private int collectionAmount; // 代收金額 (不代收請填0)

    @JsonProperty("IsCollection")
    private String isCollection; // 是否代收 (Y/N)

    @JsonProperty("GoodsName")
    private String goodsName; // 商品名稱

    @JsonProperty("SenderName")
    private String senderName; // 寄件人姓名

    @JsonProperty("SenderPhone")
    private String senderPhone; // 寄件人電話

    @JsonProperty("ReceiverName")
    private String receiverName; // 收件人姓名

    @JsonProperty("ReceiverPhone")
    private String receiverPhone; // 收件人電話

    @JsonProperty("ReceiverCellPhone")
    private String receiverCellPhone; // 收件人手機

    @JsonProperty("ServerReplyURL")
    private String serverReplyURL; // 物流狀態通知網址

    // --- 選填 ---
    @JsonProperty("ReceiverEmail")
    private String receiverEmail;

    @JsonProperty("Remark")
    private String remark;

    @JsonProperty("PlatformID")
    private String platformID = ""; // 平台ID
}