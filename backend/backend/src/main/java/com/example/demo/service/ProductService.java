package com.example.demo.service;

import com.example.demo.dto.ProductResponseDTO;
import com.example.demo.entity.ProductCategory;
import com.example.demo.entity.Unit;
import com.example.demo.exception.DataConflictException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ProductCategoryRepository;
import com.example.demo.repository.UnitRepository;
import jakarta.validation.Valid;
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

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductCategoryRepository categoryRepository;
    private final UnitRepository unitRepository;


    
    public ProductResponseDTO createProductFromDTO(ProductCreateDTO dto, Long userId) {


        productRepository.findByProductCode(dto.getProductCode()).ifPresent(product->{
            throw new DataConflictException("商品代號'"+dto.getProductCode()+"'已存在");
        });

        ProductCategory category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(()-> new ResourceNotFoundException("找不到商品分類ID為："+dto.getCategoryId()+"的分類"));

        Unit unit = unitRepository.findById(dto.getUnitId())
                .orElseThrow(() -> new ResourceNotFoundException("找不到單位ID為：" + dto.getUnitId()+"的單位"));

        Product product = new Product();
        product.setProductCode(dto.getProductCode());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setBasePrice(dto.getBasePrice());
        product.setTaxType(dto.getTaxType());
        product.setIsActive(true);
        product.setCreatedBy(userId);
        product.setUpdatedBy(userId);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        product.setCategory(category);
        product.setUnit(unit);
        product.setCostMethod(dto.getCostMethod());
        product.setSafetyStockQuantity(0);

        Product savedProduct = productRepository.save(product);
        return ProductResponseDTO.fromEntity(savedProduct);
    }

    public Page<ProductResponseDTO> searchProducts(Long categoryId, Boolean isSalable,
                                                   String keyword, Pageable pageable) {
        
        Specification<Product> spec = ProductSpecification.findByCriteria(categoryId, isSalable, keyword);

        Page<Product> productPage = productRepository.findAll(spec, pageable);
        return productPage.map(ProductResponseDTO::fromEntity);
    }

    public ProductResponseDTO getProductById(Long productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("找不到商品ID"+productId+"的商品"));
        return ProductResponseDTO.fromEntity(product);
    }
    
    public ProductResponseDTO updateProductFromDTO(Long productId, ProductUpdateDTO updatedInfo, Long userId) {
        
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("找不到商品ID為 " + productId + " 的商品"));

        ProductCategory category = categoryRepository.findById(updatedInfo.getCategoryId())
                        .orElseThrow(()->new ResourceNotFoundException("找不到商品分類ID為"+updatedInfo.getCategoryId()+"的分類"));
        Unit unit = unitRepository.findById(updatedInfo.getUnitId())
                        .orElseThrow(()->new ResourceNotFoundException("找不到單位iD為"+updatedInfo.getUnitId()+"的單位"));

        existingProduct.setName(updatedInfo.getName());
        existingProduct.setDescription(updatedInfo.getDescription());
        existingProduct.setCategory(category);
        existingProduct.setUnit(unit);
        existingProduct.setBasePrice(updatedInfo.getBasePrice());
        existingProduct.setTaxType(updatedInfo.getTaxType());
        existingProduct.setIsActive(updatedInfo.getIsActive());
        existingProduct.setUpdatedBy(userId);

        Product updatedProduct = productRepository.save(existingProduct);
        return ProductResponseDTO.fromEntity(updatedProduct);
    }


    public void deleteProduct(Long productId, Long userId) {
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("找不到商品ID 為 " + productId + " 的產品"));

        product.setIsActive(false);
        product.setUpdatedBy(userId);

        
        productRepository.save(product);
    }

    public ProductResponseDTO restoreProduct(Long productId, Long userId) {
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("找不到商品ID 為 " + productId + " 的產品"));

        
        product.setIsActive(true);
        product.setUpdatedBy(userId);

        Product restoredProduct = productRepository.save(product);
        return ProductResponseDTO.fromEntity(restoredProduct);
}

}
