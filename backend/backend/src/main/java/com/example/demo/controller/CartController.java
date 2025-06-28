package com.example.demo.controller;

import com.example.demo.dto.request.AddItemRequestDto;
import com.example.demo.dto.response.CartViewDto;
import com.example.demo.dto.request.UpdateQuantityRequestDto;
import com.example.demo.security.CheckJwt;
import com.example.demo.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    // 修改前：@GetMapping("/{userid}")
    // 修改後：
    @Operation(summary = "獲取當前客戶的購物車 (需要 JWT)")
    @GetMapping("/check")
    @CheckJwt // ★★★【修改重點 1】★★★：加上這個註解來啟用 JWT 驗證
    public ResponseEntity<CartViewDto> checkCart(HttpServletRequest request) {
            // 從 request 中獲取 customerId（由 JwtAspect 設置）
            Long customerId = (Long) request.getAttribute("customerId");
            CartViewDto cart = cartService.getCartByCCustomerId(customerId);
            return ResponseEntity.ok(cart);
    }
//    public ResponseEntity<CartViewDto> getCart(@RequestAttribute("customerId") Long customerId) { // ★★★【修改重點 2】★★★：從 @PathVariable 改為 @RequestAttribute
//        CartViewDto cartView = cartService.getCartByCCustomerId(customerId);
//        return ResponseEntity.ok(cartView);
//    }

    /**
     * 新增商品到購物車
     */
    // 修改前：@PostMapping("/items/{userid}")
    // 修改後：
    @Operation(summary = "新增商品到購物車 (需要 JWT)")
    @PostMapping("/items")
    @CheckJwt
    public ResponseEntity<CartViewDto> addItemToCart(HttpServletRequest request,
                                                     @RequestBody AddItemRequestDto requestDto) {
        Long customerId = (Long) request.getAttribute("customerId");
        CartViewDto updatedCart = cartService.addItemToCart(customerId, requestDto);
        return ResponseEntity.ok(updatedCart);
    }

    /**
     * 更新購物車項目數量
     */
    // 修改前：@PutMapping("/items/{cartDetailId}/{userid}")
    // 修改後：
    @Operation(summary = "更新購物車項目數量 (需要 JWT)")
    @PutMapping("/items/{cartDetailId}")
    @CheckJwt
    public ResponseEntity<CartViewDto> updateItemQuantity(HttpServletRequest request,
                                                          @PathVariable Long cartDetailId,
                                                          @RequestBody UpdateQuantityRequestDto requestDto) {
        Long customerId = (Long) request.getAttribute("customerId");
        CartViewDto updatedCart = cartService.updateItemQuantity(customerId, cartDetailId, requestDto.getQuantity());
        return ResponseEntity.ok(updatedCart);
    }

    /**
     * 移除購物車中的單一項目
     */
    // 修改前：@DeleteMapping("/items/{cartDetailId}/{userid}")
    // 修改後：
    @Operation(summary = "移除購物車中的單一項目 (需要 JWT)")
    @DeleteMapping("/items/{cartDetailId}")
    @CheckJwt
    public ResponseEntity<CartViewDto> removeItemFromCart(HttpServletRequest request,
                                                          @PathVariable Long cartDetailId) {
        Long customerId = (Long) request.getAttribute("customerId");
        CartViewDto updatedCart = cartService.removeItemFromCart(customerId, cartDetailId);
        return ResponseEntity.ok(updatedCart);
    }

    /**
     * 清空購物車
     */
    // 修改前：@DeleteMapping("delete/{userid}")
    // 修改後：
    @Operation(summary = "清空購物車 (需要 JWT)")
    @DeleteMapping("/delete")
    @CheckJwt
    public ResponseEntity<Void> clearCart(HttpServletRequest request) {
        Long customerId = (Long) request.getAttribute("customerId");
        cartService.clearCart(customerId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}