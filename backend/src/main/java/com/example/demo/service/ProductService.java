package com.example.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ProductCreateDTO;
import com.example.demo.dto.ProductUpdateDTO;
import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.specification.ProductSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    
    public Product createProduct(Product productToSave) {
        
        return productRepository.save(productToSave);
    }

    
    public Product createProductFromDTO(ProductCreateDTO dto, Long userId) {
        Product product = new Product();
        product.setProductCode(dto.getProductCode());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setCategoryId(dto.getCategoryId());
        product.setUnitId(dto.getUnitId());
        product.setBasePrice(dto.getBasePrice());
        product.setTaxType(dto.getTaxType());
        product.setCostMethod(dto.getCostMethod());
        
        
        product.setSafetyStockQuantity(0);
        
        product.setIsActive(true);

        
        product.setCreatedBy(userId);
        product.setUpdatedBy(userId);

        return productRepository.save(product);
    }

    public Page<Product> searchProducts(Long categoryId, Boolean isSalable, String keyword, Pageable pageable) {
        
        Specification<Product> spec = ProductSpecification.findByCriteria(categoryId, isSalable, keyword);

        
        return productRepository.findAll(spec, pageable);
    }

    
    public Product updateProduct(Long productId, Product updatedInfo, Long userId) {
        
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("找不到 ID 為 " + productId + " 的產品"));

        
        existingProduct.setName(updatedInfo.getName());
        existingProduct.setBasePrice(updatedInfo.getBasePrice());
        
        
        
        existingProduct.setUpdatedBy(userId);

        
        return productRepository.save(existingProduct);
    }
    
    
    public Product updateProductFromDTO(Long productId, ProductUpdateDTO dto, Long userId) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("找不到 ID 為 " + productId + " 的產品"));

        existingProduct.setName(dto.getName());
        existingProduct.setDescription(dto.getDescription());
        existingProduct.setCategoryId(dto.getCategoryId());
        existingProduct.setUnitId(dto.getUnitId());
        existingProduct.setBasePrice(dto.getBasePrice());
        existingProduct.setTaxType(dto.getTaxType());
        existingProduct.setIsActive(dto.getIsActive());
        existingProduct.setUpdatedBy(userId);

        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long productId, Long userId) {
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("找不到商品ID 為 " + productId + " 的產品"));
        
        
        product.setIsActive(false);
        product.setUpdatedBy(userId);

        
        productRepository.save(product);
    }

    public Product restoreProduct(Long productId, Long userId) {
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("找不到商品ID 為 " + productId + " 的產品"));

        
        product.setIsActive(true);
        product.setUpdatedBy(userId);

        
        return productRepository.save(product);
}

}
