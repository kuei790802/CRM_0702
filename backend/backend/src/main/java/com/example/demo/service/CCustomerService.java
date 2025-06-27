package com.example.demo.service;

import com.example.demo.dto.request.UpdateCCustomerProfileRequest;
import com.example.demo.dto.response.CCustomerProfileResponse;
import com.example.demo.entity.CCustomer;
import com.example.demo.exception.AccountAlreadyExistsException;
import com.example.demo.exception.EmailAlreadyExistsException;
import com.example.demo.exception.ForgetAccountOrPasswordException;
import com.example.demo.exception.UsernameNotFoundException;
import com.example.demo.repository.CCustomerRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CCustomerService {
    private final CCustomerRepo cCustomerRepo;
    private final BCryptPasswordEncoder encoder;

    // 建構自注入customerRepo、encoder
    public CCustomerService(CCustomerRepo cCustomerRepo) {
        this.cCustomerRepo = cCustomerRepo;
        this.encoder = new BCryptPasswordEncoder(); // 或改為在外部注入
    }

    // 檢視帳號是否已存在
    public Boolean checkAccountExist(String account){
        return cCustomerRepo.findByAccount(account).isPresent();
    }

    // 檢視email是否已被註冊
    public Boolean checkEmailExist(String email){
        return cCustomerRepo.existsByEmail(email);
    }

    // 密碼強度規範
    private void validatePasswordStrength(String password) {
        if (password.length() < 12) {
            throw new IllegalArgumentException("密碼長度需至少12碼");
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("密碼需包含至少一個大寫英文字母");
        }

        if (!password.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("密碼需包含至少一個小寫英文字母");
        }

        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("密碼需包含至少一個數字");
        }

        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            throw new IllegalArgumentException("密碼需包含至少一個特殊符號");
        }
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

        if(checkEmailExist(email)){
            throw new EmailAlreadyExistsException(email);
        }

        validatePasswordStrength(password);

        CCustomer newCCustomer = CCustomer.builder()
                .account(account)
                .name(customerName)
                .password(encoder.encode(password))
                .email(email)
                .address(address)
                .birthday(birthday)
                .isActive(true)
                .isDeleted(false)
                .build();

        return cCustomerRepo.save(newCCustomer);
    }



    // 登入驗證 (JWT + OUATH2)+ 拋給別人我已經登入的資訊供後續開發
    public CCustomer login(String account, String password){
        // 測帳號 //暫時，之後要改成能有重設帳密功能，跳轉介面?發送EMAIL?顯示錯誤訊息?
        CCustomer loginCCustomer = cCustomerRepo.findByAccount(account)
                .orElseThrow(() -> new ForgetAccountOrPasswordException(account, password));

        // 測密碼 //暫時，之後要改成能有重設帳密功能，跳轉介面?發送EMAIL?顯示錯誤訊息?
        String dbPassword = loginCCustomer.getPassword();
        if(!encoder.matches(password, dbPassword)){
            throw new ForgetAccountOrPasswordException(account, password);
        }

        return loginCCustomer;
    }

    // 檢視顧客資料: 顯示用戶個人基本資訊（帳號、姓名、電話、地址等）資料查詢、權限驗證（只能看自己的資料）
    public CCustomerProfileResponse getProfile(String account) {
        CCustomer customer = cCustomerRepo.findByAccount(account)
                .orElseThrow(()-> new UsernameNotFoundException("找不到使用者: " + account));

        return new CCustomerProfileResponse(
                customer.getAccount(),
                customer.getName(),
                customer.getEmail(),
                customer.getAddress(),
                customer.getBirthday(),
                customer.getCoupons(),
                customer.getSpending(),
                customer.getVipLevel()
        );
    }


    // 更改基本資料: 讓用戶更新個人資訊（含忘記密碼、密碼更改）資料驗證、密碼加密更新、資料一致性

    public CCustomerProfileResponse updateProfile(String account, UpdateCCustomerProfileRequest request) {
        CCustomer customer = cCustomerRepo.findByAccount(account)
                .orElseThrow(() -> new RuntimeException("帳號不存在"));

        // 更新基本資料，並防止為空值
        if (request.getCustomerName() != null) {
            customer.setName(request.getCustomerName());
        }
        if (request.getEmail() != null) {
            customer.setEmail(request.getEmail());
        }
        if (request.getAddress() != null) {
            customer.setAddress(request.getAddress());
        }

        // 如果 newPassword 不是空的，執行密碼修改流程
        if (request.getNewPassword() != null && !request.getNewPassword().isEmpty()) {
            // 驗證舊密碼正確
            if (!encoder.matches(request.getOldPassword(), customer.getPassword())) {
                throw new IllegalArgumentException("舊密碼錯誤");
            }

            // 驗證新密碼強度
            validatePasswordStrength(request.getNewPassword());

            // 加密新密碼
            customer.setPassword(encoder.encode(request.getNewPassword()));
        }

        CCustomer savedCustomer = cCustomerRepo.save(customer);

        return new CCustomerProfileResponse(
                savedCustomer.getAccount(),
                savedCustomer.getName(),
                savedCustomer.getEmail(),
                savedCustomer.getAddress(),
                savedCustomer.getBirthday(),
                savedCustomer.getCoupons(),
                savedCustomer.getSpending(),
                savedCustomer.getVipLevel()
        );
    }

    // ID找帳號
    public Long getCustomerIdByAccount(String account) {
        return cCustomerRepo.findByAccount(account)
                .map(CCustomer::getCustomerId)
                .orElseThrow(() -> new UsernameNotFoundException("找不到顧客: " + account));
    }

    // 帳號是否啟用中
    public boolean isCustomerActive(String account) {
        return cCustomerRepo.findByAccount(account)
                .map(c -> c.isActive() && !c.isDeleted())
                .orElse(false); // 找不到代表不合法，當作不允許操作
    }

    // 帳號刪除(真刪除)
    public void deleteAccountPermanently(String account) {
        CCustomer customer = cCustomerRepo.findByAccount(account)
                .orElseThrow(() -> new UsernameNotFoundException("找不到帳號: " + account));

        cCustomerRepo.delete(customer);
    }




//    todo: 6/11繼續





    // 留言給課服: 前台用戶對商品、客服、社區的留言與回覆，留言CRUD，留言屬性包含userId, productId等，防止惡意留言

    // (購物邏輯是別人負責的，所以這部分可以晚點做嗎?)拉回別人開發的購物下單資訊 -> 我要做的事更新累積消費金額、VIP等級升降，如下
    // VIP互動: 針對VIP用戶提供額外功能（專屬優惠、積分），VIP等級判斷、積分管理、VIP專屬訊息提醒

    // 開api給系統管理者: 註冊時紀錄註冊時間、修改實紀錄修改時間、下單時紀錄下單時間






}
