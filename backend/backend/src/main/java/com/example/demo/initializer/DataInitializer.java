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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        logger.info("--- 應用程式啟動，開始生成一年份的假資料 (僅限 dev profile) ---");

        // 步驟 1: 建立基礎資料 (標籤、客戶、聯絡人)
        List<Long> tagIds = createTags(5);
        List<Long> customerIds = createCustomers(20);
        if (customerIds.isEmpty()) {
            logger.error("未能創建任何客戶。終止資料生成。");
            return;
        }
        List<Long> contactIds = createContacts(30, customerIds);
        if (contactIds.isEmpty()) {
            logger.warn("未能創建任何聯絡人，但仍將繼續生成。");
        }

        // 步驟 2: 遍歷過去 12 個月，生成時間序列資料
        LocalDate today = LocalDate.now();
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");

        for (int i = 11; i >= 0; i--) {
            LocalDate currentMonth = today.minusMonths(i).withDayOfMonth(1);
            logger.info("--- 開始為 {} 月份生成資料 ---", currentMonth.format(monthFormatter));

            // 步驟 3: 每月生成 5 至 10 筆商機
            int opportunitiesToCreate = random.nextInt(6) + 5;
            List<Long> monthlyOpportunityIds = createMonthlyOpportunities(
                    opportunitiesToCreate, customerIds, contactIds, tagIds, currentMonth
            );

            if (monthlyOpportunityIds.isEmpty()) {
                logger.warn("月份 {} 未能成功創建任何商機，跳過此月份的後續步驟。", currentMonth.format(monthFormatter));
                continue;
            }

            // 步驟 4: 為本月新建立的商機評分
            rateOpportunities(monthlyOpportunityIds);

            // 步驟 5: 為每個新商機建立 1 到 3 個相關活動
            int activitiesToCreate = monthlyOpportunityIds.size() * (random.nextInt(3) + 1);
            createMonthlyActivities(activitiesToCreate, monthlyOpportunityIds, contactIds, currentMonth);
        }

        logger.info("--- 所有年度假資料生成結束 ---");
    }

    /**
     * 建立標籤
     */
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

    /**
     * 建立客戶
     */
    private List<Long> createCustomers(int count) {
        logger.info("--- 開始生成客戶 ({} 筆)...", count);
        List<BCustomerRequest> fakeRequests = bCustomerFaker.generateFakeBCustomerRequests(count);
        List<Long> generatedIds = new ArrayList<>();
        for (BCustomerRequest request : fakeRequests) {
            try {
                BCustomerDto created = bCustomerService.create(request);
                generatedIds.add(created.getCustomerId());
            } catch (Exception e) {
                logger.error("創建客戶失敗: {}", e.getMessage());
            }
        }
        logger.info("客戶生成完畢。");
        return generatedIds;
    }

    /**
     * 建立聯絡人
     */
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

    /**
     * 為指定月份建立特定數量的商機。
     * @param month 商機應歸屬的月份。
     */
    private List<Long> createMonthlyOpportunities(int count, List<Long> customerIds, List<Long> contactIds, List<Long> tagIds, LocalDate month) {
        logger.info("生成 {} 筆商機...", count);
        List<OpportunityRequest> fakeRequests = opportunityFaker.generateFakeOpportunityRequests(count, customerIds, contactIds);

        for (OpportunityRequest request : fakeRequests) {
            int dayOfMonth = random.nextInt(month.lengthOfMonth()) + 1;
            LocalDateTime creationDateTime = month.withDayOfMonth(dayOfMonth).atTime(random.nextInt(24), random.nextInt(60));

            request.setCreateDate(creationDateTime);

            request.setCloseDate(creationDateTime.toLocalDate().plusDays(random.nextInt(90) + 30));
        }


        List<Long> generatedIds = new ArrayList<>();
        for (OpportunityRequest request : fakeRequests) {
            try {
                OpportunityDto created = opportunityService.create(request);
                generatedIds.add(created.getOpportunityId());
            } catch (Exception e) {
                logger.error("創建商機失敗: {}", e.getMessage(), e);
            }
        }
        logger.info("本月商機生成完畢，共 {} 筆。", generatedIds.size());
        return generatedIds;
    }

    /**
     * 為指定的商機進行評分。
     */
    private void rateOpportunities(List<Long> opportunityIds) {
        logger.info("--- 開始為商機評分 ---");
        if (opportunityIds == null || opportunityIds.isEmpty()) {
            logger.warn("沒有商機可供評分，跳過此步驟。");
            return;
        }

        Long testUserId = 1L; // 假設一個用於測試的用戶 ID
        for (Long oppId : opportunityIds) {
            int numRatings = random.nextInt(3) + 1; // 每筆商機有 1 到 3 個評分
            for (int i = 0; i < numRatings; i++) {
                int ratingScore = random.nextInt(3) + 1; // 評分分數為 1, 2, 或 3
                try {
                    opportunityService.rateOpportunity(oppId, testUserId, ratingScore);
                } catch (Exception e) {
                    logger.error("評分商機 ID {} 失敗: {}", oppId, e.getMessage());
                }
            }
        }
        logger.info("商機評分結束。");
    }

    /**
     * 為指定月份建立與商機相關的活動。
     */
    private void createMonthlyActivities(int count, List<Long> opportunityIds, List<Long> contactIds, LocalDate month) {
        logger.info("生成 {} 筆活動...", count);
        if (opportunityIds == null || opportunityIds.isEmpty()) {
            logger.warn("沒有商機可供創建活動，跳過。");
            return;
        }

        for (int i = 0; i < count; i++) {
            try {
                ActivityRequest request = activityFaker.generateFakeActivityRequest(opportunityIds, contactIds);

                int dayOfMonth = random.nextInt(month.lengthOfMonth()) + 1;
                LocalDateTime activityStartTime = month.withDayOfMonth(dayOfMonth).atTime(random.nextInt(8) + 9, random.nextInt(60));

                request.setStartTime(activityStartTime);
                request.setEndTime(activityStartTime.plusHours(1));

                activityService.create(request);
            } catch (Exception e) {
                logger.error("創建活動失敗: {}", e.getMessage(), e);
            }
        }
        logger.info("本月活動生成完畢。");
    }
}