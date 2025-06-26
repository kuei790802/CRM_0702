package com.example.demo.controller.erp;

import com.example.demo.dto.erp.ProductResponseDTO;
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

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(
            @Valid @RequestBody ProductCreateDTO productDTO) {
        Long currentUserId = 1L;

        ProductResponseDTO createdProduct = productService.createProductFromDTO(productDTO, currentUserId);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

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

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id){
        ProductResponseDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Long id, 
            @Valid @RequestBody ProductUpdateDTO productDTO) {
        
        Long currentUserId = 1L;
        
        ProductResponseDTO updatedProduct = productService.updateProductFromDTO(id, productDTO, currentUserId);
        
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        Long currentUserId = 1L;
        
        productService.deleteProduct(id, currentUserId);
        
        
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<ProductResponseDTO> restoreProduct(@PathVariable Long id) {
        Long currentUserId = 1L;

        ProductResponseDTO restoredProduct = productService.restoreProduct(id, currentUserId);
        
        
        return ResponseEntity.ok(restoredProduct);
    }
}
