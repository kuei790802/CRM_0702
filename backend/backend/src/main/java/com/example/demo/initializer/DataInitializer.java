//package com.example.demo.initializer;
//
//import com.example.demo.dto.request.BCustomerRequest;
//import com.example.demo.dto.request.ContactRequest;
//import com.example.demo.dto.request.OpportunityRequest;
//import com.example.demo.dto.request.TagRequest;
//import com.example.demo.dto.response.BCustomerDto;
//import com.example.demo.dto.response.ContactDto;
//import com.example.demo.dto.response.OpportunityDto;
//import com.example.demo.dto.response.TagDto;
//import com.example.demo.service.BCustomerService;
//import com.example.demo.service.ContactService;
//import com.example.demo.service.OpportunityService;
//import com.example.demo.service.TagService;
//import com.example.demo.util.BCustomerFaker;
//import com.example.demo.util.ContactFaker;
//import com.example.demo.util.OpportunityFaker;
//import com.example.demo.util.TagFaker;
//import jakarta.persistence.EntityNotFoundException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Profile;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Random;
//import java.util.stream.Collectors;
//
//@Component
//@Profile("dev")
//public class DataInitializer implements CommandLineRunner {
//
//    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
//    private final Random random = new Random();
//
//    @Autowired
//    private BCustomerService bCustomerService;
//    @Autowired
//    private ContactService contactService;
//    @Autowired
//    private OpportunityService opportunityService;
//    @Autowired
//    private TagService tagService;
//
//    @Autowired
//    private BCustomerFaker bCustomerFaker;
//    @Autowired
//    private ContactFaker contactFaker;
//    @Autowired
//    private OpportunityFaker opportunityFaker;
//    @Autowired
//    private TagFaker tagFaker;
//
//    @Override
//    public void run(String... args) throws Exception {
//        logger.info("--- 應用程式啟動，開始生成假資料 (僅限 dev profile) ---");
//
//        // 0. 生成並保存假標籤資料
//        int numTags = 5; // 生成 5 個假標籤
//        List<TagRequest> fakeTagRequests = tagFaker.generateFakeTagRequests(numTags);
//        List<Long> tagIds = new ArrayList<>(); // 用於後續關聯客戶和商機
//        logger.info("正在生成並保存 {} 筆假標籤數據...", numTags);
//        for (TagRequest request : fakeTagRequests) {
//            try {
//                TagDto createdTag = tagService.create(request);
//                tagIds.add(createdTag.getTagId());
//                logger.info("成功創建標籤: {} (ID: {})", createdTag.getTagName(), createdTag.getTagId());
//            } catch (IllegalArgumentException e) {
//                logger.warn("創建標籤失敗 (名稱 {} 已存在)", request.getTagName());
//                tagService.findByTagName(request.getTagName()).ifPresent(tag -> tagIds.add(tag.getTagId()));
//            } catch (Exception e) {
//                logger.error("創建標籤失敗: {}", e.getMessage(), e);
//            }
//        }
//        if (tagIds.isEmpty()) {
//            logger.error("未能創建任何標籤。客戶和商機將無法關聯標籤。");
//        }
//        logger.info("標籤數據生成完畢。已創建標籤ID: {}", tagIds);
//
//
//        // 1. 生成並保存假客戶資料
//        int numCustomers = 5;
//        List<BCustomerRequest> fakeCustomerRequests = bCustomerFaker.generateFakeBCustomerRequests(numCustomers);
//        List<Long> customerIds = new ArrayList<>();
//        logger.info("正在生成並保存 {} 筆假客戶數據...", numCustomers);
//        for (BCustomerRequest request : fakeCustomerRequests) {
//            List<Long> associatedTagIdsForCustomer = new ArrayList<>();
//            if (!tagIds.isEmpty()) {
//                int tagsToAssociate = random.nextInt(4); // 關聯 0 到 3 個標籤
//                // 打亂標籤列表以確保隨機性
//                Collections.shuffle(tagIds);
//                for (int i = 0; i < tagsToAssociate && i < tagIds.size(); i++) {
//                    associatedTagIdsForCustomer.add(tagIds.get(i));
//                }
//            }
//
//            // *** 已修正 ***
//            // 將生成的標籤ID列表設定到 request 物件中
//            // 前提: BCustomerRequest DTO 中有名為 setTagIds 的方法
//            request.setTagIds(associatedTagIdsForCustomer.stream().distinct().collect(Collectors.toList())); // 使用 distinct 避免重複
//
//            try {
//                BCustomerDto createdCustomer = bCustomerService.create(request);
//                customerIds.add(createdCustomer.getCustomerId());
//                logger.info("成功創建客戶: {} (ID: {})，關聯標籤ID: {}", createdCustomer.getCustomerName(), createdCustomer.getCustomerId(), request.getTagIds());
//            } catch (Exception e) {
//                logger.error("創建客戶失敗: {}", e.getMessage(), e);
//            }
//        }
//        if (customerIds.isEmpty()) {
//            logger.error("未能創建任何客戶。無法繼續生成聯絡人和商機。");
//            return;
//        }
//        logger.info("客戶數據生成完畢。已創建客戶ID: {}", customerIds);
//
//
//        // 2. 生成並保存假聯絡人資料 (關聯到已創建的客戶)
//        int numContacts = 15;
//        List<ContactRequest> fakeContactRequests = contactFaker.generateFakeContactRequests(numContacts, customerIds);
//        List<Long> contactIds = new ArrayList<>();
//        logger.info("正在生成並保存 {} 筆假聯絡人數據...", numContacts);
//        for (ContactRequest request : fakeContactRequests) {
//            try {
//                ContactDto createdContact = contactService.create(request);
//                contactIds.add(createdContact.getContactId());
//                logger.info("成功創建聯絡人: {} (ID: {})", createdContact.getContactName(), createdContact.getContactId());
//            } catch (EntityNotFoundException e) {
//                logger.warn("創建聯絡人失敗 (客戶ID {} 不存在): {}", request.getCustomerId(), e.getMessage());
//            } catch (Exception e) {
//                logger.error("創建聯絡人時發生錯誤: {}", e.getMessage(), e);
//            }
//        }
//        logger.info("聯絡人數據生成完畢。已創建聯絡人ID: {}", contactIds);
//
//        // 3. 生成並保存假商機資料 (關聯到已創建的客戶和聯絡人)
//        int numOpportunities = 20;
//        List<Long> opportunityIds = new ArrayList<>();
//        if (customerIds.isEmpty()) {
//            logger.warn("沒有客戶ID，跳過商機數據生成。");
//            return;
//        }
//
//        List<OpportunityRequest> fakeOpportunityRequests = opportunityFaker.generateFakeOpportunityRequests(numOpportunities, customerIds, contactIds);
//        logger.info("正在生成並保存 {} 筆假商機數據...", numOpportunities);
//        for (OpportunityRequest request : fakeOpportunityRequests) {
//            List<Long> associatedTagIdsForOpportunity = new ArrayList<>();
//            if (!tagIds.isEmpty()) {
//                int tagsToAssociate = random.nextInt(4); // 關聯 0 到 3 個標籤
//                Collections.shuffle(tagIds);
//                for (int i = 0; i < tagsToAssociate && i < tagIds.size(); i++) {
//                    associatedTagIdsForOpportunity.add(tagIds.get(i));
//                }
//            }
//
//            // *** 已修正 ***
//            // 將生成的標籤ID列表設定到 request 物件中
//            // 前提: OpportunityRequest DTO 中有名為 setTagIds 的方法
//            request.setTagIds(associatedTagIdsForOpportunity.stream().distinct().collect(Collectors.toList())); // 使用 distinct 避免重複
//
//            try {
//                OpportunityDto createdOpportunity = opportunityService.create(request);
//                opportunityIds.add(createdOpportunity.getOpportunityId());
//                logger.info("成功創建商機: {} (ID: {})，關聯標籤ID: {}", createdOpportunity.getOpportunityName(), createdOpportunity.getOpportunityId(), request.getTagIds());
//            } catch (EntityNotFoundException e) {
//                logger.warn("創建商機失敗 (關聯實體不存在): {}", e.getMessage());
//            } catch (Exception e) {
//                logger.error("創建商機時發生錯誤: {}", e.getMessage(), e);
//            }
//        }
//        logger.info("商機數據生成完畢。已創建商機ID: {}", opportunityIds);
//
//        logger.info("--- 開始為生成的商機隨機評分 ---");
//        Long testUserId = 1L; // 假設用戶 ID 為 1
//        if (opportunityIds.isEmpty()) {
//            logger.warn("沒有商機ID，跳過評分生成。");
//        } else {
//            for (Long oppId : opportunityIds) {
//                int numRatings = random.nextInt(5) + 1; // 每個商機評分 1 到 5 次
//                for (int i = 0; i < numRatings; i++) {
//                    int ratingScore = random.nextInt(3) + 1; // 評分 1 到 3 分
//                    try {
//                        opportunityService.rateOpportunity(oppId, testUserId, ratingScore);
//                        logger.info("商機 ID {} 收到評分: {}", oppId, ratingScore);
//                    } catch (EntityNotFoundException e) {
//                        logger.warn("評分商機失敗 (商機ID {} 不存在): {}", oppId, e.getMessage());
//                    } catch (IllegalArgumentException e) {
//                        logger.warn("評分商機失敗 (無效評分分數): {}", e.getMessage());
//                    } catch (Exception e) {
//                        logger.error("評分商機時發生錯誤 (ID: {}): {}", oppId, e.getMessage(), e);
//                    }
//                }
//            }
//        }
//        logger.info("--- 假資料生成和評分結束 ---");
//    }
//}
