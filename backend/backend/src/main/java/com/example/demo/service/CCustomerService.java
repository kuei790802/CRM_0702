package com.example.demo.service;

import com.example.demo.entity.CCustomer;
import com.example.demo.exception.AccountAlreadyExistsException;
import com.example.demo.exception.ForgetAccountOrPasswordException;
import com.example.demo.repository.CCustomerRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CCustomerService {
    private final CCustomerRepo CCustomerRepo;
    private final BCryptPasswordEncoder encoder;

    // 建構自注入customerRepo、encoder
    public CCustomerService(CCustomerRepo CCustomerRepo) {
        this.CCustomerRepo = CCustomerRepo;
        this.encoder = new BCryptPasswordEncoder(); // 或改為在外部注入
    }

    // 檢視帳號是否已存在
    public Boolean checkAccountExist(String account){
        return CCustomerRepo.findByAccount(account).isPresent();
    }

    // 註冊 + 加密
    public CCustomer register(String account
                            , String customerName
                            , String password
                            , String email
                            , LocalDate birthday){
        if(checkAccountExist(account)){
            throw new AccountAlreadyExistsException(account);
        }

        CCustomer newCCustomer = CCustomer.builder()
                .account(account)
                .customerName(customerName)
                .password(encoder.encode(password))
                .email(email)
                .birthday(birthday)
                .isActive(true)
                .isDeleted(false)
                .build();

        return CCustomerRepo.save(newCCustomer);
    }



    // 登入驗證 (JWT + OUATH2)+ 拋給別人我已經登入的資訊供後續開發
    public CCustomer login(String account, String password){
        // 測帳號 //暫時，之後要改成能有重設帳密功能，跳轉介面?發送EMAIL?顯示錯誤訊息?
        CCustomer loginCCustomer = CCustomerRepo.findByAccount(account)
                .orElseThrow(() -> new ForgetAccountOrPasswordException(account, password));

        // 測密碼 //暫時，之後要改成能有重設帳密功能，跳轉介面?發送EMAIL?顯示錯誤訊息?
        String dbPassword = loginCCustomer.getPassword();
        if(!encoder.matches(password, dbPassword)){
            throw new ForgetAccountOrPasswordException(account, password);
        }

        return loginCCustomer;
    }




//    todo: 6/11繼續
    // 刪除帳號



    // 拉回別人開發的購物下單資訊 -> 累積消費金額、VIP等級升降

    // 更改基本資料

    // 註冊時紀錄註冊時間、修改實紀錄修改時間、下單時紀錄下單時間

    // 忘記密碼

    // 留言給課服



}
