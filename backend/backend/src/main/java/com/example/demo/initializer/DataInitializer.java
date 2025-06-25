package com.example.demo.initializer;

import com.example.demo.dto.request.*;
import com.example.demo.dto.response.BCustomerDto;
import com.example.demo.dto.response.ContactDto;
import com.example.demo.dto.response.OpportunityDto;
import com.example.demo.dto.response.TagDto;
import com.example.demo.service.*;
import com.example.demo.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@Profile("dev")
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private final Random random = new Random();

    @Autowired
    private BCustomerService bCustomerService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private OpportunityService opportunityService;

    @Autowired
    private TagService tagService;

    @Autowired
    private ActivityService activityService;

    // -----

    @Autowired
    private BCustomerFaker bCustomerFaker;

    @Autowired
    private ContactFaker contactFaker;

    @Autowired
    private OpportunityFaker opportunityFaker;

    @Autowired
    private TagFaker tagFaker;

    @Autowired
    private ActivityFaker activityFaker;


    @Override
    public void run(String... args) throws Exception {
        logger.info("--- 應用程式啟動，開始生成假資料 (僅限 dev profile) ---");

        List<Long> tagIds = createTags(5);
        List<Long> customerIds = createCustomers(10, tagIds);

        if (customerIds.isEmpty()) {
            logger.error("未能創建任何客戶。終止資料生成。");
            return;
        }

        List<Long> contactIds = createContacts(15, customerIds);
        List<Long> opportunityIds = createOpportunities(20, customerIds, contactIds, tagIds);

        rateOpportunities(opportunityIds);
        createActivities(15, opportunityIds, contactIds);

        logger.info("--- 所有假資料生成結束 ---");
    }

    private List<Long> createTags(int count) {
        logger.info("--- 開始生成標籤 ({} 筆)...", count);
        List<TagRequest> fakeRequests = tagFaker.generateFakeTagRequests(count);
        List<Long> generatedIds = new ArrayList<>();
        for (TagRequest request : fakeRequests) {
            try {
                TagDto created = tagService.create(request);
                generatedIds.add(created.getTagId());
            } catch (Exception e) {
                logger.error("創建標籤失敗: {}", e.getMessage());
            }
        }
        logger.info("標籤生成完畢。");
        return generatedIds;
    }

    private List<Long> createCustomers(int count, List<Long> tagIds) {
        logger.info("--- 開始生成客戶 ({} 筆)...", count);
        List<BCustomerRequest> fakeRequests = bCustomerFaker.generateFakeBCustomerRequests(count);
        List<Long> generatedIds = new ArrayList<>();
        for (BCustomerRequest request : fakeRequests) {
            try {
                // 可以在這裡加入關聯標籤的邏輯
                BCustomerDto created = bCustomerService.create(request);
                generatedIds.add(created.getCustomerId());
            } catch (Exception e) {
                logger.error("創建客戶失敗: {}", e.getMessage());
            }
        }
        logger.info("客戶生成完畢。");
        return generatedIds;
    }

    private List<Long> createContacts(int count, List<Long> customerIds) {
        logger.info("--- 開始生成聯絡人 ({} 筆)...", count);
        List<ContactRequest> fakeRequests = contactFaker.generateFakeContactRequests(count, customerIds);
        List<Long> generatedIds = new ArrayList<>();
        for (ContactRequest request : fakeRequests) {
            try {
                ContactDto created = contactService.create(request);
                generatedIds.add(created.getContactId());
            } catch (Exception e) {
                logger.error("創建聯絡人失敗: {}", e.getMessage());
            }
        }
        logger.info("聯絡人生成完畢。");
        return generatedIds;
    }

    private List<Long> createOpportunities(int count, List<Long> customerIds, List<Long> contactIds, List<Long> tagIds) {
        logger.info("--- 開始生成商機 ({} 筆)...", count);
        List<OpportunityRequest> fakeRequests = opportunityFaker.generateFakeOpportunityRequests(count, customerIds, contactIds);
        List<Long> generatedIds = new ArrayList<>();
        for (OpportunityRequest request : fakeRequests) {
            try {
                // 可以在這裡加入關聯標籤的邏輯
                OpportunityDto created = opportunityService.create(request);
                generatedIds.add(created.getOpportunityId());
            } catch (Exception e) {
                logger.error("創建商機失敗: {}", e.getMessage());
            }
        }
        logger.info("商機生成完畢。");
        return generatedIds;
    }

    private void rateOpportunities(List<Long> opportunityIds) {
        logger.info("--- 開始為商機評分 ---");
        if (opportunityIds == null || opportunityIds.isEmpty()) {
            logger.warn("沒有商機可供評分，跳過此步驟。");
            return;
        }

        Long testUserId = 1L;
        Random random = new Random();
        for (Long oppId : opportunityIds) {
            int numRatings = random.nextInt(3) + 1; // 1 to 3 ratings
            for (int i = 0; i < numRatings; i++) {
                int ratingScore = random.nextInt(3) + 1;
                try {
                    opportunityService.rateOpportunity(oppId, testUserId, ratingScore);
                } catch (Exception e) {
                    logger.error("評分商機 ID {} 失敗: {}", oppId, e.getMessage());
                }
            }
        }
        logger.info("商機評分結束。");
    }

    private void createActivities(int count, List<Long> opportunityIds, List<Long> contactIds) {
        logger.info("--- 開始生成活動 ({} 筆)...", count);
        for (int i = 0; i < count; i++) {
            try {
                ActivityRequest request = activityFaker.generateFakeActivityRequest(opportunityIds, contactIds);
                activityService.create(request);
            } catch (Exception e) {
                logger.error("創建活動失敗: {}", e.getMessage());
            }
        }
        logger.info("活動生成完畢。");
    }
}
