package com.service;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.entity.Inventory;
import com.example.demo.entity.InventoryMovement;
import com.example.demo.entity.Product;
import com.example.demo.entity.PurchaseOrder;
import com.example.demo.entity.PurchaseOrderDetail;
import com.example.demo.entity.Users;
import com.example.demo.entity.Warehouse;
import com.example.demo.enums.PurchaseOrderStatus;
import com.example.demo.repository.InventoryMovementRepository;
import com.example.demo.repository.InventoryRepository;
import com.example.demo.repository.PurchaseOrderRepository;
import com.example.demo.service.InventoryService;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private InventoryMovementRepository inventoryMovementRepository;

    @Mock
    private PurchaseOrderRepository purchaseOrderRepository;

    @InjectMocks
    private InventoryService inventoryService;
    
    private Warehouse sampleWarehouse;
    private PurchaseOrder samplePurchaseOrder;
    private Product sampleProduct;
    private Users sampleUser;


    @BeforeEach
    void setUp() {
        
        sampleUser = new Users();
        sampleUser.setUserId(1L);

        sampleProduct = new Product();
        sampleProduct.setProductId(1L);

        sampleWarehouse = new Warehouse();
        sampleWarehouse.setWarehouseId(1L);

        
        samplePurchaseOrder = new PurchaseOrder();
        samplePurchaseOrder.setPurchaseOrderId(10L);
        samplePurchaseOrder.setStatus(PurchaseOrderStatus.CONFIRMED); // 狀態為「已確認」，可以入庫

      
        PurchaseOrderDetail detail1 = new PurchaseOrderDetail();
        detail1.setItemId(100L);
        detail1.setProductId(sampleProduct.getProductId());
        detail1.setWarehouseId(sampleWarehouse.getWarehouseId());
        detail1.setQuantity(new BigDecimal("20"));
        samplePurchaseOrder.addDetail(detail1);
    }

    @Test
    void testReceivePurchaseOrder(){
         // 1. 準備 (Arrange)
        // 模擬一個現有的庫存紀錄，庫存為 30
        Inventory existingInventory = new Inventory();
        existingInventory.setProduct(sampleProduct);
        existingInventory.setWarehouse(sampleWarehouse);
        existingInventory.setCurrentStock(new BigDecimal("30"));

        // 設定 Mockito 的行為
        // 當試圖找 ID 為 101 的訂單時，回傳我們準備好的 sampleOrder
        when(purchaseOrderRepository.findById(101L)).thenReturn(Optional.of(samplePurchaseOrder));
        // 當試圖用產品ID和倉庫ID找庫存時，回傳我們準備好的 existingInventory
        when(inventoryRepository.findByProductIdAndWarehouseId(1L, 1L)).thenReturn(Optional.of(existingInventory));

        // 2. 執行 (Act)
        // 呼叫我們「即將要開發」的 receivePurchaseOrder 方法
        inventoryService.receivePurchaseOrder(101L, 1L);

        // 3. 斷言 (Assert) - 驗證系統是否做了我們預期的事

        // 驗證 1: 進貨單的狀態是否被更新為「已到貨」
        assertEquals(PurchaseOrderStatus.RECEIVED, samplePurchaseOrder.getStatus());
        
        // 驗證 2: 庫存數量是否正確增加 (原本 30 + 入庫 20 = 50)
        assertEquals(0, new BigDecimal("50").compareTo(existingInventory.getCurrentStock()));

        // 驗證 3: 是否有儲存庫存異動紀錄
        // ArgumentCaptor 用來捕捉傳遞給 mock 物件方法的參數
        ArgumentCaptor<InventoryMovement> movementCaptor = ArgumentCaptor.forClass(InventoryMovement.class);
        // 驗證 inventoryMovementRepository.save() 方法被呼叫了「一次」，並捕捉傳入的參數
        verify(inventoryMovementRepository, times(1)).save(movementCaptor.capture());
        
        // 取得被儲存的 InventoryMovement 物件
        InventoryMovement savedMovement = movementCaptor.getValue();
        // 驗證異動紀錄的內容是否正確
        assertEquals("IN_PURCHASE", savedMovement.getMovementType());
        assertEquals(0, new BigDecimal("20").compareTo(savedMovement.getQuantityChange())); // 數量變化是 +20
        assertEquals(101L, savedMovement.getDocumentId()); // 關聯單據 ID 正確
        assertEquals(0, new BigDecimal("50").compareTo(savedMovement.getCurrentStockAfterMovement())); // 異動後庫存是 50
    }
}
