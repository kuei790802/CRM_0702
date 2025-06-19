//package com.example.demo.service;
//
//import com.example.demo.dto.request.AddItemRequestDto;
//import com.example.demo.dto.response.CartDetailDto;
//import com.example.demo.dto.response.CartViewDto;
//import com.example.demo.entity.*;
//import com.example.demo.repository.CartDetailRepository;
//import com.example.demo.repository.CartRepository;
//import com.example.demo.repository.ProductRepository;
//import com.example.demo.repository.CCsutomerRepository;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.EntityNotFoundException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//public class CartService {
//    @Autowired
//    private CartRepository cartRepository;
//    @Autowired
//    private CartDetailRepository cartDetailRepository;
//    @Autowired
//    private ProductRepository productRepository;
//    @Autowired
//    private CCsutomerRepository CCsutomerRepository;
//    @Autowired
//    private EntityManager entityManager;
//
//    /**
//     * 檢查此 userid 有無購物車，若有則呈現出對應的 CartViewDto；若無則呈現空的 CartViewDto
//     */
//    @Transactional
//    public CartViewDto getCartByCCustomerId(Long CCustomerId) {
//        return cartRepository.findByCCustomer_CustomerId(CCustomerId)
//                .map(this::mapToCartViewDto) // 如果找到購物車，則轉換成 DTO
//                .orElse(getEmptyCart());   // 如果找不到，則回傳一個空購物車 DTO
//    }
//
//    /**
//     * 新增商品到購物車
//     */
//    @Transactional
//    public CartViewDto addItemToCart(Long userId, AddItemRequestDto requestDto) {
//        Cart cart = findOrCreateCartByUser(userId);
//
//        Product product = productRepository.findById(requestDto.getProductid())
//                .orElseThrow(() -> new EntityNotFoundException("找不到商品 ID: " + requestDto.getProductid()));
//
//        // === 以下是庫存檢查邏輯 ===
//        // 1. 從 Product 物件中取得所有相關的 Inventory 紀錄列表
////        List<Inventory> inventories = product.getInventories();
////
////        // 2. 使用 Stream API 將所有庫存地點的 unitsinstock 加總
////        int totalStock = inventories.stream()
////                .mapToInt(Inventory::getUnitsinstock) // 將每個 Inventory 物件轉換成它的庫存數量(int)
////                .sum(); // 將所有的 int 數字加總
////
////        int reservedStock = inventories.stream()
////                .mapToInt(Inventory::getUnitsinreserved)
////                .sum();
//
////        int usableStock = totalStock - reservedStock;
//
//        // 3. 用計算出來的總庫存進行比較
////        if (usableStock < requestDto.getQuantity()) {
////            throw new IllegalStateException("商品庫存不足，目前可下訂庫存為: " + usableStock);
////        }
//
//        // 如果您在 Product Entity 中新增了 getTotalStock() 方法，這裡也可以簡化為：
//        // if (product.getTotalStock() < requestDto.getQuantity()) {
//        //     throw new IllegalStateException("商品庫存不足，目前總庫存為: " + product.getTotalStock());
//        // }
//
//        // 查找購物車是否已存在此商品
////        Optional<CartDetail> existingDetailOpt = cart.getCartdetails().stream()
////                .filter(d -> d.getProduct().getProductid().equals(requestDto.getProductid()))
////                .findFirst();
//
//        if (existingDetailOpt.isPresent()) {
//            // 如果存在，則更新數量
//            CartDetail existingDetail = existingDetailOpt.get();
//            int newQuantity = existingDetail.getQuantity() + requestDto.getQuantity();
//            if (newQuantity > usableStock) {
//                throw new IllegalStateException("商品庫存不足，累加後超過可下訂庫存: " + usableStock);
//            }
//            existingDetail.setQuantity(newQuantity);
//            cartRepository.saveAndFlush(cart);
//        } else {
//            // 如果不存在，則新增一個 CartDetail
//            CartDetail newDetail = new CartDetail();
//            newDetail.setProduct(product);
//            newDetail.setQuantity(requestDto.getQuantity());
//            newDetail.setAddat(LocalDateTime.now());
//            newDetail.setCart(cart); // 明確設定它屬於哪個 cart
//            cartDetailRepository.saveAndFlush(newDetail);
//        }
//
//        entityManager.refresh(cart);
//
//
//        return mapToCartViewDto(cart);
//    }
//
//    /**
//     * 更新購物車項目數量
//     */
//    @Transactional
//    public CartViewDto updateItemQuantity(Long userId, Long cartDetailId, int newQuantity) {
//        Cart cart = findCartByUser(userId);
//        CartDetail detail = findCartDetailInCart(cart, cartDetailId);
//
//        if (newQuantity <= 0) {
//            // 如果數量小於等於0，則移除該項目
//            removeItemFromCartInternal(cart, detail);
//        } else {
//
//            // 檢查庫存
//            // 1. 從購物車項目(detail)中取得對應的商品(Product)物件
//            Product product = detail.getProduct();
//
//            // 2. 加總該商品在所有庫存地點的庫存量
//            int totalStock = product.getInventories().stream()
//                    .mapToInt(Inventory::getUnitsinstock)
//                    .sum();
//
//            // 3. 比較總庫存與使用者想更新的數量
//            if (totalStock < newQuantity) {
//                throw new IllegalStateException("商品庫存不足，目前總庫存為: " + totalStock);
//            }
//
//            // 4. 如果庫存充足，才更新數量
//            detail.setQuantity(newQuantity);
//        }
//
//        Cart updatedCart = cartRepository.save(cart);
//        return mapToCartViewDto(updatedCart);
//    }
//
//    /**
//     * 從購物車移除單一項目
//     */
//    @Transactional
//    public CartViewDto removeItemFromCart(Long userId, Long cartDetailId) {
//        Cart cart = findCartByUser(userId);
//        CartDetail detail = findCartDetailInCart(cart, cartDetailId);
//
//        // 現在這個操作會觸發 orphanRemoval，直接刪除資料庫紀錄
//        cart.getCartdetails().remove(detail);
//
//        Cart updatedCart = cartRepository.save(cart);
//        return mapToCartViewDto(updatedCart);
//    }
//
//    /**
//     * 清空購物車
//     */
//    @Transactional
//    public void clearCart(Long userId) {
//        Cart cart = findCartByUser(userId);
//        cart.getCartdetails().clear(); // 因為 orphanRemoval=true，這會刪除所有關聯的 CartDetail
//        cartRepository.save(cart);
//    }
//
//    //----------以下為拉出來撰寫的方法
//    private CartViewDto mapToCartViewDto(Cart cart) {
//        List<CartDetailDto> detailDtosDtos = cart.getCartdetails().stream()
//                .map(detail -> {
//                    List<ProductImg> imgs = detail.getProduct().getProductimgs();
//                    String ImgUrl;
//                    if (imgs != null && !imgs.isEmpty()) {
//                        ImgUrl = imgs.get(0).getImgurl();
//                    } else {
//                        ImgUrl = "/images/default_product.jpg";
//                    }
//
//                    return CartDetailDto.builder()
//                            .cartdetailid(detail.getCartdetailid())
//                            .productid(detail.getProduct().getProductid())
//                            .productname(detail.getProduct().getProductname())
//                            .productimgurl(ImgUrl)
//                            .unitprice(detail.getProduct().getUnitprice())
//                            .quantity(detail.getQuantity())
//                            .totalprice(detail.getProduct().getUnitprice() * detail.getQuantity())
//                            .build();
//                })
//                .collect(Collectors.toList());
//
//        double grandTotal = detailDtosDtos.stream().mapToDouble(CartDetailDto::getTotalprice).sum();
//        int totalItems = detailDtosDtos.stream().mapToInt(CartDetailDto::getQuantity).sum();
//
//        return CartViewDto.builder()
//                .cartid(cart.getCartid())
//                .cartdetails(detailDtosDtos)
//                .totalprice(grandTotal)
//                .quantity(totalItems)
//                .build();
//    }
//
//    private CartViewDto getEmptyCart() {
//        return CartViewDto.builder()
//                .cartdetails(Collections.emptyList())
//                .totalprice(0.0)
//                .quantity(0)
//                .build();
//    }
//
//    private Cart findOrCreateCartByUser(Long userId) {
//        return cartRepository.findByCCustomer_CustomerId(userId)
//                .orElseGet(() -> {
//                    CCustomer CCustomer = CCsutomerRepository.findById(userId)
//                            .orElseThrow(() -> new EntityNotFoundException("找不到使用者 ID: " + userId));
//                    Cart newCart = new Cart();
//                    newCart.setCCustomer(CCustomer);
//                    newCart.setCreateat(LocalDateTime.now());
//                    newCart.setUpdateat(LocalDateTime.now());
//                    return cartRepository.save(newCart);
//                });
//    }
//
//    private Cart findCartByUser(Long userId) {
//        return cartRepository.findByCCustomer_CustomerId((long)userId)
//                .orElseThrow(() -> new EntityNotFoundException("使用者 " + userId + " 尚無購物車"));
//    }
//
//    private CartDetail findCartDetailInCart(Cart cart, Long cartDetailId) {
//        return cart.getCartdetails().stream()
//                .filter(d -> d.getCartdetailid().equals(cartDetailId))
//                .findFirst()
//                .orElseThrow(() -> new EntityNotFoundException("在購物車中找不到項目 ID: " + cartDetailId));
//    }
//
//    private void removeItemFromCartInternal(Cart cart, CartDetail detail) {
//        cart.removeCartDetail(detail); // 使用輔助方法移除
//    }
//
//}
//
