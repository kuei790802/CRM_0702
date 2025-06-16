package com.example.demo.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum CustomerIndustry {
    TECHNOLOGY("科技業"),
    FINANCE("金融業"),
    RETAIL("零售業"),
    MANUFACTURING("製造業"),
    HEALTHCARE("醫療保健業"),
    EDUCATION("教育業"),
    REAL_ESTATE("房地產業"),
    CONSULTING("顧問業"),
    ENTERTAINMENT("娛樂業"),
    GOVERNMENT("政府機關"),
    OTHER("其他"); // 用於無法明確分類的行業

    private final String displayName;

    private static final Map<String, CustomerIndustry> NAMES_TO_INDUSTRIES =
            Arrays.stream(values()).collect(Collectors.toMap(CustomerIndustry::getDisplayName, Function.identity()));

    CustomerIndustry(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static CustomerIndustry fromDisplayName(String input) {
        CustomerIndustry customerIndustry = NAMES_TO_INDUSTRIES.get(input);
        if (customerIndustry != null) {
            return customerIndustry;
        }
        return Arrays.stream(values())
                .filter(e -> e.name().equalsIgnoreCase(input))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "無效的行業類型: '" + input + "'. 有效類型為: " + getAllDisplayNames() + " 或其英文名稱。"
                ));
    }

    public static CustomerIndustry fromStringName(String name) {
        return Arrays.stream(values())
                .filter(e -> e.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("無效的行業類型名稱: '" + name + "'."));
    }

    public static List<String> getAllDisplayNames() {
        return Arrays.stream(values())
                .map(CustomerIndustry::getDisplayName)
                .collect(Collectors.toList());
    }
}
