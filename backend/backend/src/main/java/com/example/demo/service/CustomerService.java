package com.example.demo.service;

import com.example.demo.entity.Customer;
import com.example.demo.exception.AccountAlreadyExistsException;
import com.example.demo.repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class CustomerService {
    // 要有CustomerService首先要有Customer，所以要擁有
    @Autowired
    private CustomerRepo customerRepo;


    // 註冊 + 加密 +
    public Customer register(String account
                            , String customerName
                            , String password
                            , String email
                            , LocalDate birthday){
        if(checkAccountExist(account)){
            throw new AccountAlreadyExistsException(account);
        }

        Customer newCustomer = Customer.builder()
                .account(account)
                .customerName(customerName)
                .password(password) // todo: 6/11加密
                .email(email)
                .birthday(birthday)
                .isActive(true)
                .isDeleted(false)
                .build();

        return customerRepo.save(newCustomer);
    }

    public Boolean checkAccountExist(String account){
        return customerRepo.findByAccount(account).isPresent();
    }

//    todo: 6/11繼續
    // 刪除帳號

    // 登入驗證 (這裡是用 JWT + OUATH2嗎?)+ 拋給別人我已經登入的資訊供後續開發

    // 拉回別人開發的購物下單資訊 -> 累積消費金額、VIP等級升降

    // 更改基本資料

    // 註冊時紀錄註冊時間、修改實紀錄修改時間、下單時紀錄下單時間

    // 忘記密碼

    // 留言給課服



}
