package com.example.demo.util;

import com.example.demo.dto.request.TagRequest;
import com.github.javafaker.Faker;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class TagFaker {

    private final Faker faker = new Faker();
    private final Random random = new Random();

    private static final String[] PREDEFINED_TAG_NAMES = {
            "VIP客戶", "潛在客戶", "新客戶", "戰略合作", "一般客戶"
    };

    private static final String[] PREDEFINED_COLORS = {
            "#D4EDDA", // success (淺綠)
            "#D1ECF1", // processing (淺藍)
            "#F8D7DA", // error (淺紅)
            "#FFF3CD", // warning (淺黃)
            "#E9ECEF", // default (淺灰)
    };

    /**
     * 生成指定數量的假標籤請求。
     * 標籤名稱嚴格從預定義列表中隨機選取，保證不帶數字且唯一。
     * 顏色從預定義顏色列表中隨機選取，確保長度為 7。
     *
     * @param count 要生成的標籤數量
     * @return 假標籤請求列表
     * @throws IllegalArgumentException 如果請求生成的標籤數量超過預定義名稱的數量
     */
    public List<TagRequest> generateFakeTagRequests(int count) {
        List<TagRequest> requests = new ArrayList<>();

        // 檢查請求的數量是否超過預定義的名稱數量
        if (count > PREDEFINED_TAG_NAMES.length) {
            throw new IllegalArgumentException("請求生成的標籤數量 (" + count +
                    ") 超過預定義的標籤名稱數量 (" +
                    PREDEFINED_TAG_NAMES.length + ")，請調整數量或擴展預定義列表。");
        }

        // 複製預定義的名稱列表並打亂順序，以便隨機選擇且不重複。
        List<String> availableTagNames = new ArrayList<>(List.of(PREDEFINED_TAG_NAMES));
        Collections.shuffle(availableTagNames, random); // 打亂整個池子

        // 複製預定義的顏色列表並打亂順序，以便隨機選擇顏色。
        List<String> shuffledColors = new ArrayList<>(List.of(PREDEFINED_COLORS));
        // 如果請求的標籤數量超過預定義顏色數量，循環添加顏色使其足夠，然後再次打亂。
        while (shuffledColors.size() < count) {
            shuffledColors.addAll(List.of(PREDEFINED_COLORS));
        }
        Collections.shuffle(shuffledColors, random); // 顏色列表進行隨機打亂


        for (int i = 0; i < count; i++) {
            // 從打亂後的名稱列表中依序取用標籤名稱，保證唯一性。
            String tagName = availableTagNames.get(i);

            // 從打亂後的顏色列表中取用顏色，或重複使用 (現在是隨機的)。
            String color = shuffledColors.get(i);


            requests.add(TagRequest.builder()
                    .tagName(tagName)
                    .color(color)
                    .build());
        }
        return requests;
    }
}
