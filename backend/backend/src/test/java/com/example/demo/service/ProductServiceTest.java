package com.example.demo.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void testCreateProductSuccess(){
         // 1. 準備 (Arrange)
        // 模擬一個要被儲存的 Product 物件
        Product productToSave = new Product();
        productToSave.setProductCode("TEST001");
        productToSave.setName("測試商品");
        productToSave.setUnitId(1L);
        productToSave.setBasePrice(new BigDecimal("100.00"));
        productToSave.setTaxType("TAXABLE");
        productToSave.setCreatedBy(1L);
        productToSave.setUpdatedBy(1L);

        // 模擬儲存成功後，資料庫回傳的 Product 物件 (多了 ID 和時間)
        Product savedProduct = new Product();
        savedProduct.setProductId(1L); // 模擬資料庫產生的 ID
        savedProduct.setProductCode("TEST001");
        savedProduct.setName("測試商品");

        // 設定 Mockito：當 productRepository.save() 方法被呼叫時，就回傳我們上面準備好的 savedProduct
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // 2. 執行 (Act)
        // 實際呼叫我們「即將要開發」的 createProduct 方法
        Product result = productService.createProduct(productToSave);

        // 3. 斷言 (Assert)
        // 驗證回傳的結果是否如我們預期
        assertNotNull(result); // 結果不應為空
        assertEquals(1L, result.getProductId()); // 驗證 ID 是否為 1
        assertEquals("TEST001", result.getProductCode()); // 驗證品號是否正確
    }

    @Test
    void testSearchProducts_shouldReturnPagedResult() {
        // 1. 準備 (Arrange)
        // 模擬查詢條件
        Long categoryId = 1L;
        Boolean isSalable = true;
        String keyword = "測試";
        Pageable pageable = PageRequest.of(0, 10); // 查詢第 0 頁，每頁 10 筆

        // 模擬資料庫中符合條件的產品
        Product product = new Product();
        product.setProductId(1L);
        product.setName("測試商品");
        List<Product> productList = Collections.singletonList(product);

        // 模擬一個分頁結果物件
        Page<Product> expectedPage = new PageImpl<>(productList, pageable, 1);

        // 設定 Mockito：當 repository 被呼叫時 (使用任何 Specification 和 Pageable)，就回傳我們預設的 Page 物件
        when(productRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(expectedPage);

        // 2. 執行 (Act)
        // 呼叫我們即將開發的 searchProducts 方法
        Page<Product> actualPage = productService.searchProducts(categoryId, isSalable, keyword, pageable);

        // 3. 斷言 (Assert)
        // 驗證回傳的分頁結果是否符合預期
        assertNotNull(actualPage);
        assertEquals(1, actualPage.getTotalElements()); // 總筆數為 1
        assertEquals(1, actualPage.getTotalPages()); // 總頁數為 1
        assertEquals("測試商品", actualPage.getContent().get(0).getName()); // 內容正確
    }

    @Test
    void testUpdateProduct_Success() {
        // 1. 準備 (Arrange)
        Long productId = 1L;
        Product existingProduct = new Product(); // 模擬資料庫中已存在的產品
        existingProduct.setProductId(productId);
        existingProduct.setName("舊品名");

        Product updatedInfo = new Product(); // 模擬要更新的資訊
        updatedInfo.setName("新品名");
        updatedInfo.setBasePrice(new BigDecimal("150.00"));
        
        // 設定 Mockito：當 findById 被呼叫時，回傳我們模擬的已存在的產品
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        // 設定 Mockito：當 save 被呼叫時，直接回傳傳入的物件
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 2. 執行 (Act)
        Product result = productService.updateProduct(productId, updatedInfo, 1L); // 假設由 user 1 更新

        // 3. 斷言 (Assert)
        assertNotNull(result);
        assertEquals("新品名", result.getName()); // 驗證品名是否已更新
        assertEquals(0, new BigDecimal("150.00").compareTo(result.getBasePrice())); // 驗證價格是否已更新
    }

    // 測試更新不存在的產品
    @Test
    void testUpdateProduct_NotFound_shouldThrowException() {
        // 1. 準備 (Arrange)
        Long nonExistentProductId = 99L;
        Product updatedInfo = new Product();
        updatedInfo.setName("新品名");
        
        // 設定 Mockito：當 findById 被呼叫時，回傳一個空的 Optional，表示找不到
        when(productRepository.findById(nonExistentProductId)).thenReturn(Optional.empty());

        // 2. 執行與斷言 (Act & Assert)
        // 驗證當呼叫 updateProduct 時，會拋出 RuntimeException (或更精確的自訂例外)
        assertThrows(RuntimeException.class, () -> {
            productService.updateProduct(nonExistentProductId, updatedInfo, 1L);
        });
    }

    @Test
    void testDeleteProduct_shouldSetIsActiveToFalse() {
        // 1. 準備 (Arrange)
        Long productId = 1L;
        Product existingProduct = new Product();
        existingProduct.setProductId(productId);
        existingProduct.setIsActive(true); // 產品原本是啟用的

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        // 2. 執行 (Act)
        productService.deleteProduct(productId, 1L); // 由 user 1 刪除

        // 3. 斷言 (Assert)
        // 我們要驗證 'save' 方法被呼叫時，傳入的 product 物件其 is_active 是 false
        
        // ArgumentCaptor 可以捕獲方法被呼叫時傳入的參數
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        
        // 驗證 save 方法確實被以某個 Product 物件呼叫了
        verify(productRepository).save(productCaptor.capture());
        
        // 取得被傳入的 Product 物件
        Product savedProduct = productCaptor.getValue();
        
        // 斷言它的 is_active 狀態是 false
        assertFalse(savedProduct.getIsActive());
    }

}
