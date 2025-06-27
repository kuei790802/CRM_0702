package com.example.demo.util;

import com.example.demo.dto.request.BCustomerRequest;
import com.example.demo.enums.BCustomerIndustry;
import com.example.demo.enums.BCustomerLevel;
import com.example.demo.enums.BCustomerType;
//import com.github.javafaker.Faker;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class BCustomerFaker {

//    private final Faker faker = new Faker();
    private final Random random = new Random();

    /**
     * 產生一個隨機的 BCustomerRequest 物件。
     * @return 包含假客戶資料的 BCustomerRequest
     */
//    public BCustomerRequest generateFakeBCustomerRequest() {
//        List<BCustomerIndustry> industries = Arrays.asList(BCustomerIndustry.values());
//        List<BCustomerType> customerTypes = Arrays.asList(BCustomerType.values());
//        List<BCustomerLevel> customerLevels = Arrays.asList(BCustomerLevel.values());
//
//        return BCustomerRequest.builder()
//                .customerName(faker.company().name() + (faker.bool().bool() ? " 有限公司" : " 個人戶")) // 更豐富的客戶名稱
//                .industry(industries.get(random.nextInt(industries.size()))) // 隨機行業
//                .BCustomerType(customerTypes.get(random.nextInt(customerTypes.size()))) // 隨機客戶類型
//                .BCustomerLevel(customerLevels.get(random.nextInt(customerLevels.size()))) // 隨機客戶級別
//                .customerAddress(faker.address().fullAddress()) // 完整地址
//                .customerTel(faker.phoneNumber().phoneNumber()) // 電話號碼
//                .customerEmail(faker.internet().emailAddress()) // 電子郵件
//                .build();
//    }

    /**
     * 產生指定數量的假客戶請求。
     * @param count 要產生的請求數量
     * @return 包含假客戶請求的列表
     */
//    public List<BCustomerRequest> generateFakeBCustomerRequests(int count) {
//        return IntStream.range(0, count)
//                .mapToObj(i -> generateFakeBCustomerRequest())
//                .collect(Collectors.toList());
//    }
}
