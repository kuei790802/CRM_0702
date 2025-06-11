package com.example.demo.dto;

import java.time.LocalDateTime;

// 返回統一的錯誤響應結構
public class ErrorResponse {
    private final LocalDateTime timestamp; // 錯誤發生的時間
    private final int status; // HTTP 狀態碼
    private final String error; // HTTP 狀態描述（例如 "Not Found", "Bad Request"）
    private final String message; // 具體的錯誤訊息
    private final String path; // 請求的路徑

    public ErrorResponse(int status, String error, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    // --- Getters ---
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

}

