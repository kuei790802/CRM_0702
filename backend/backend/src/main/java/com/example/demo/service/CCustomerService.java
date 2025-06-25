package com.example.demo.service;

import com.example.demo.dto.response.CCustomerProfileResponse;
import com.example.demo.entity.CCustomer;
import com.example.demo.entity.Order; // <<--【新增】導入 Order
import com.example.demo.enums.OrderStatus; // <<--【新增】導入 OrderStatus
import com.example.demo.exception.AccountAlreadyExistsException;
import com.example.demo.exception.ForgetAccountOrPasswordException;
import com.example.demo.repository.CCustomerRepo;
import com.example.demo.repository.OrderRepository; // <<--【新增】導入 OrderRepository
import jakarta.persistence.EntityNotFoundException; // <<--【新增】導入 EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired; // <<--【新增】導入 Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // <<--【新增】導入 Transactional

import java.time.LocalDate;
import java.util.List; // <<--【新增】導入 List

@Service
public class CCustomerService {
    private final CCustomerRepo cCustomerRepo; // <<--【修改】將名稱改為 cCustomerRepo 較符合慣例
    private final BCryptPasswordEncoder encoder;


    // <<--【新增】注入 OrderRepository -->>
    @Autowired
    private OrderRepository orderRepository;

    // <<--【修改】建構子參數名稱對應修改 -->>
    public CCustomerService(CCustomerRepo cCustomerRepo) {
        this.cCustomerRepo = cCustomerRepo;
        this.encoder = new BCryptPasswordEncoder();
    }

    // 檢視帳號是否已存在
    public Boolean checkAccountExist(String account){
        return cCustomerRepo.findByAccount(account).isPresent();
    }

    // 註冊 + 加密
    public CCustomer register(String account
            , String customerName
            , String password
            , String email
            , String address
            , LocalDate birthday){
        if(checkAccountExist(account)){
            throw new AccountAlreadyExistsException(account);
        }

        CCustomer newCCustomer = CCustomer.builder()
                .account(account)
                .name(customerName) //TODO(joshkuei): Change from customerName to name (adopt to CustomerBase)
                .password(encoder.encode(password))
                .email(email)
                .address(address)
                .birthday(birthday)
                .spending(0L) // <<--【建議】註冊時預設消費為 0
                .isActive(true)
                .isDeleted(false)
                .build();

        return cCustomerRepo.save(newCCustomer);
    }



    // 登入驗證 (JWT + OUATH2)+ 拋給別人我已經登入的資訊供後續開發
    public CCustomer login(String account, String password){
        CCustomer loginCCustomer = cCustomerRepo.findByAccount(account)
                .orElseThrow(() -> new ForgetAccountOrPasswordException(account, password));

        String dbPassword = loginCCustomer.getPassword();
        if(!encoder.matches(password, dbPassword)){
            throw new ForgetAccountOrPasswordException(account, password);
        }

        return loginCCustomer;
    }

    // 檢視顧客資料
    public CCustomerProfileResponse getProfile(String account) {
        CCustomer customer = cCustomerRepo.findByAccount(account)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new CCustomerProfileResponse(
                customer.getAccount(),
                customer.getName(), //TODO(joshkuei): Change from customerName to name (adopt to CustomerBase)
                customer.getEmail(),
                customer.getAddress(),
                customer.getBirthday()
        );
    }

    // =================================================================
    // ====================== 【以下為新增的程式碼】 ======================
    // =================================================================

    /**
     * 更新客戶的總消費金額。
     * 此方法會查詢所有已完成的訂單並加總其金額。
     * @param customerId 要更新的客戶 ID
     */
    @Transactional
    public void updateCustomerSpending(Long customerId) {
        // 1. 根據 customerId 查找其所有歷史訂單
        List<Order> orders = orderRepository.findByCCustomer_CustomerIdOrderByOrderdateDesc(customerId);

        // 2. 過濾出狀態為 COMPLETE 的訂單，並加總其 totalAmount
        double totalSpending = orders.stream()
                .filter(order -> order.getOrderStatus() == OrderStatus.COMPLETE) // 只計算已完成的訂單
                .mapToDouble(order -> order.getTotalAmount() != null ? order.getTotalAmount() : 0.0)
                .sum();

        // 3. 找到對應的客戶實體
        CCustomer customer = cCustomerRepo.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("更新消費金額時找不到客戶 ID: " + customerId));

        // 4. 更新客戶的 spending 欄位並儲存
        customer.setSpending((long) totalSpending); // 將 double 轉為 Long
        cCustomerRepo.save(customer);
    }
}