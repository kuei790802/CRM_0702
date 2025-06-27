package com.example.demo.util;

import com.example.demo.dto.request.ContactRequest;
//import com.github.javafaker.Faker;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class ContactFaker {

//    private final Faker faker = new Faker();
    private final Random random = new Random();

    /**
     * 產生一個隨機的 ContactRequest 物件。
     * @param customerIds 可選的客戶ID列表，用於關聯聯絡人。
     * @return 包含假聯絡人資料的 ContactRequest
     */
//    public ContactRequest generateFakeContactRequest(List<Long> customerIds) {
//        Long customerId = null;
//        if (customerIds != null && !customerIds.isEmpty()) {
//            customerId = customerIds.get(random.nextInt(customerIds.size()));
//        }
//
//        return ContactRequest.builder()
//                .contactName(faker.name().fullName())
//                .title(faker.company().profession())
//                .phone(faker.phoneNumber().phoneNumber())
//                .email(faker.internet().emailAddress())
//                .notes(faker.lorem().sentence()) // 隨機生成一些筆記
//                .customerId(customerId) // 關聯到傳入的客戶ID
//                .build();
//    }

    /**
     * 產生指定數量的假聯絡人請求。
     * @param count 要產生的請求數量
     * @param customerIds 可選的客戶ID列表，用於關聯聯絡人。
     * @return 包含假聯絡人請求的列表
     */
//    public List<ContactRequest> generateFakeContactRequests(int count, List<Long> customerIds) {
//        return IntStream.range(0, count)
//                .mapToObj(i -> generateFakeContactRequest(customerIds))
//                .collect(Collectors.toList());
//    }
}
