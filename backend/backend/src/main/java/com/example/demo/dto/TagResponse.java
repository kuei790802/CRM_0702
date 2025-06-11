package com.example.demo.dto;

public class TagResponse {
    private String message;
    private boolean success;

    public TagResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    // 建議覆寫 toString() 以方便除錯
    @Override
    public String toString() {
        return "TagResponse{" +
                "message='" + message + '\'' +
                ", success=" + success +
                '}';
    }
}
