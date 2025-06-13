package com.example.demo.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ProductCreateRequestDTO;
import com.example.demo.dto.ProductUpdateRequestDTO;
import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductCreateRequestDTO productDTO) {
        Long currentUserId = 1L;

        Product createdProduct = productService.createProductFromDTO(productDTO, currentUserId);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @GetMapping // 當收到 HTTP GET 請求時，會執行此方法
    public ResponseEntity<Page<Product>> searchProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Boolean isSalable,
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "product_id") Pageable pageable) {
        
        
        
        Page<Product> productPage = productService.searchProducts(categoryId, isSalable, keyword, pageable);
        
        return ResponseEntity.ok(productPage);
    }

    @PutMapping("/{id}") // 處理 PUT /api/products/{id} 的請求
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id, 
            @Valid @RequestBody ProductUpdateRequestDTO productDTO) {
        
        Long currentUserId = 1L;
        
        Product updatedProduct = productService.updateProductFromDTO(id, productDTO, currentUserId);
        
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}") // 處理 DELETE /api/products/{id} 的請求
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        Long currentUserId = 1L;
        
        productService.deleteProduct(id, currentUserId);
        
        
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/restore") // 處理 POST /api/products/{id}/restore 的請求
    public ResponseEntity<Product> restoreProduct(@PathVariable Long id) {
        Long currentUserId = 1L;

        Product restoredProduct = productService.restoreProduct(id, currentUserId);
        
        
        return ResponseEntity.ok(restoredProduct);
    }
}
