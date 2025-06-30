package com.example.demo.controller.erp;

import com.example.demo.dto.erp.ProductResponseDTO;
import com.example.demo.dto.erp.ProductSimpleDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import com.example.demo.dto.erp.ProductCreateDTO;
import com.example.demo.dto.erp.ProductUpdateDTO;
//import com.example.demo.entity.Product;
import com.example.demo.service.erp.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "產品管理API(Product Management)", description = "包含產品建立、多種查詢、簡易產品清單、更新、刪除")
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "新增產品", description = "新增單筆產品或多筆產品")
    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(
            @Valid @RequestBody ProductCreateDTO productDTO) {
        Long currentUserId = 1L;

        ProductResponseDTO createdProduct = productService.createProductFromDTO(productDTO, currentUserId);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @Operation(summary = "查詢產品", description = "根據條件，查詢產品列表")
    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> searchProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Boolean isSalable,
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "productId") Pageable pageable) {
        
        
        
        Page<ProductResponseDTO> productPage = productService.searchProducts(
                categoryId, isSalable, keyword, pageable);
        
        return ResponseEntity.ok(productPage);
    }

    @Operation(summary = "查詢單筆產品", description = "根據產品ID，查詢單筆產品")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id){
        ProductResponseDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "獲取所有產品的簡易列表", description = "只返回所有產品的ID和名稱，用於下拉選單等場景")
    @GetMapping("/simple-list")
    public ResponseEntity<List<ProductSimpleDTO>> getAllProductsSimple() {
        List<ProductSimpleDTO> products = productService.getAllProductsSimple();
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "更新產品", description = "根據產品ID，更新產品")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Long id, 
            @Valid @RequestBody ProductUpdateDTO productDTO) {
        
        Long currentUserId = 1L;
        
        ProductResponseDTO updatedProduct = productService.updateProductFromDTO(id, productDTO, currentUserId);
        
        return ResponseEntity.ok(updatedProduct);
    }

    @Operation(summary = "刪除產品", description = "根據產品ID，刪除產品")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        Long currentUserId = 1L;
        
        productService.deleteProduct(id, currentUserId);
        
        
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "復原產品", description = "根據產品ID，復原產品")
    @PostMapping("/{id}/restore")
    public ResponseEntity<ProductResponseDTO> restoreProduct(@PathVariable Long id) {
        Long currentUserId = 1L;

        ProductResponseDTO restoredProduct = productService.restoreProduct(id, currentUserId);
        
        
        return ResponseEntity.ok(restoredProduct);
    }
}
