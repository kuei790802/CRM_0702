package com.example.demo.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;
import java.util.TreeMap;

public class EcpayCheckMacValueUtil {

    /**
     * 產生 ECPay CheckMacValue
     * @param params 參數 Map
     * @param hashKey ECPay HashKey
     * @param hashIv ECPay HashIV
     * @return CheckMacValue
     */
    public static String generate(Map<String, String> params, String hashKey, String hashIv) {
        // 使用 TreeMap 來確保參數按字母順序排序
        Map<String, String> sortedParams = new TreeMap<>(params);

        StringBuilder sb = new StringBuilder();
        sb.append("HashKey=").append(hashKey);
        for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
            sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
        }
        sb.append("&HashIV=").append(hashIv);

        String urlEncoded = ecpayUrlEncode(sb.toString()).toLowerCase();
        return hash(urlEncoded, "SHA-256");
    }

    /**
     * ECPay 特定的 URL 編碼，對應 PHP 的 UrlService::ecpayUrlEncode
     * @param source 來源字串
     * @return 編碼後的字串
     */
    private static String ecpayUrlEncode(String source) {
        try {
            String encoded = URLEncoder.encode(source, StandardCharsets.UTF_8.toString());
            // .net URLEncode 的特殊字元轉換
            return encoded
                    .replace("%2d", "-")
                    .replace("%5f", "_")
                    .replace("%2e", ".")
                    .replace("%21", "!")
                    .replace("%2a", "*")
                    .replace("%28", "(")
                    .replace("%29", ")");
        } catch (Exception e) {
            throw new RuntimeException("URL encoding failed", e);
        }
    }

    /**
     * 進行 SHA-256 雜湊
     * @param data 要雜湊的資料
     * @param algorithm 演算法 (SHA-256)
     * @return 雜湊後的十六進位字串
     */
    private static String hash(String data, String algorithm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] hashedBytes = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * hashedBytes.length);
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString().toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException("Hashing failed", e);
        }
    }
}