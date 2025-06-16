package com.example.demo.handler;

import com.example.demo.dto.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Collectors;


/**
 * @ControllerAdvice: 將這個類別標記為一個全域的例外處理器，
 * Spring 會自動將所有 Controller 拋出的例外導向到這裡。
 * 統一處理應用程式中的各種常見異常，並返回標準化的 ErrorResponse 格式。
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 處理所有由 Service 層拋出的 EntityNotFoundException。
     * 當 findById().orElseThrow(...) 找不到實體時觸發。
     * @return HTTP 404 Not Found
     */
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // 設置響應狀態碼為 404 Not Found
    public ResponseEntity<ErrorResponse> handleJpaEntityNotFoundException(
            EntityNotFoundException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(), // 返回具體訊息，例如 "找不到客戶，ID: 123"
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * 處理由 @Valid 註解觸發的參數驗證失敗例外。
     * 當 Controller 方法的參數驗證失敗時觸發。
     * @return HTTP 400 Bad Request
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 設置響應狀態碼為 400 Bad Request
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> String.format("'%s': %s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.joining("; "));

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "輸入參數驗證失敗: " + errorMessage,
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * 處理所有未被其他特定處理器捕獲的通用異常。
     * 這是最後一道防線，確保任何未預期的錯誤都能以標準格式回傳。
     * 在生產環境中，應避免暴露內部錯誤細節。
     * @return HTTP 500 Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 設置響應狀態碼為 500 Internal Server Error
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {

        System.err.println("未捕獲的系統異常: " + ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "系統發生未預期的錯誤，請聯繫管理員。",
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
