package com.example.demo.controller;

import com.example.demo.dto.AddItemRequestDto;
import com.example.demo.dto.CartViewDto;
import com.example.demo.dto.UpdateQuantityRequestDto;
import com.example.demo.repository.CartRepository;
import com.example.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    // 點擊購物車後，透過 userid 呈現使用者的購物車資料
    @GetMapping("/{userid}")
    public ResponseEntity<CartViewDto> getCart(@PathVariable Long userid) {
        CartViewDto cartView = cartService.getCartByUserId(userid);
        return ResponseEntity.ok(cartView);
    }

    /**
     * 新增商品到購物車
     */
    @PostMapping("/items/{userid}")
    public ResponseEntity<CartViewDto> addItemToCart(@PathVariable Integer userid,
                                                     @RequestBody AddItemRequestDto requestDto) {
        CartViewDto updatedCart = cartService.addItemToCart(userid, requestDto);
        return ResponseEntity.ok(updatedCart);
    }

    /**
     * 更新購物車項目數量
     */
    @PutMapping("/items/{cartDetailId}/{userid}")
    public ResponseEntity<CartViewDto> updateItemQuantity(@PathVariable Integer userid,
                                                          @PathVariable Integer cartDetailId,
                                                          @RequestBody UpdateQuantityRequestDto requestDto) {
        CartViewDto updatedCart = cartService.updateItemQuantity(userid, cartDetailId, requestDto.getQuantity());
        return ResponseEntity.ok(updatedCart);
    }

    /**
     * 移除購物車中的單一項目
     */
    @DeleteMapping("/items/{cartDetailId}/{userid}")
    public ResponseEntity<CartViewDto> removeItemFromCart(@PathVariable Integer userid,
                                                          @PathVariable Integer cartDetailId) {
        CartViewDto updatedCart = cartService.removeItemFromCart(userid, cartDetailId);
        return ResponseEntity.ok(updatedCart);
    }

    /**
     * 清空購物車
     */
    @DeleteMapping("delete/{userid}")
    public ResponseEntity<Void> clearCart(@PathVariable Integer userid) {
        cartService.clearCart(userid);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
