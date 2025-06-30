package com.example.demo.controller.erp;

import com.example.demo.dto.erp.ProductHomepageDto;
import com.example.demo.dto.erp.ProductResponseDTO;
import com.example.demo.entity.Product;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.erp.ProductCreateDTO;
import com.example.demo.dto.erp.ProductUpdateDTO;
//import com.example.demo.entity.Product;
import com.example.demo.service.erp.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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


    /**
     * 【新增】提供給首頁獲取所有商品的 API 端點
     * @return 返回包含所有商品的 JSON 陣列
     */
    @GetMapping("/homepage")
    public ResponseEntity<List<ProductHomepageDto>> getAllProductsForHomepage() {
        List<ProductHomepageDto> products = productService.getAllProductsForHomepage();
        return ResponseEntity.ok(products);
    }

    /**
     * 【全新增加】提供給前台，根據分類 ID 獲取商品列表的專用 API。
     * 使用 @PathVariable 將 URL 路徑中的一部分作為參數。
     * @param categoryId 商品分類的 ID，來自 URL 路徑，例如 /api/products/category/1 中的 1
     * @return 返回該分類下的商品列表
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductHomepageDto>> getProductsByCategoryId(@PathVariable Long categoryId) {
        List<ProductHomepageDto> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }

    /**
     * 【全新增加】建立新商品的 API 端點 (包含圖片上傳)
     * 使用 @RequestPart 來同時接收 JSON 和檔案
     */
    @PostMapping("/add")
    public ResponseEntity<?> createProduct(
            @RequestPart("productData") ProductCreateDTO createDTO,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {

        try {
            Product newProduct = productService.createProductWithImages(createDTO, files);
            // 這裡可以回傳一個更詳細的 DTO，但為了簡單起見，我們先回傳 ID
            return ResponseEntity.ok(Map.of(
                    "message", "商品建立成功",
                    "productId", newProduct.getProductId()
            ));
        } catch (IOException e) {
            return ResponseEntity.status(500).body("圖片儲存失敗: " + e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

}
