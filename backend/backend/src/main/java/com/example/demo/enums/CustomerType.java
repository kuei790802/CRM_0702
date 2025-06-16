package com.example.demo.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum CustomerType {
    PROSPECT("潛在客戶"),       // 尚未建立業務關係的客戶
    PARTNER("合作夥伴"),       // 與公司有合作關係的實體
    VENDOR("供應商"),         // 為公司提供產品或服務的供應商
    KEY_ACCOUNT("重要客戶"),    // 對公司業務收入或戰略意義重大的客戶
    REGULAR("一般客戶"),       // 普通的、穩定的客戶
    INACTIVE("非活躍客戶"),     // 曾經合作但目前不活躍的客戶
    LOST("流失客戶");         // 曾經合作但已確定流失的客戶


    private final String displayName;

    private static final Map<String, CustomerType> NAMES_TO_TYPES =
            Arrays.stream(values()).collect(Collectors.toMap(CustomerType::getDisplayName, Function.identity()));

    CustomerType(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static CustomerType fromDisplayName(String input) {
        CustomerType type = NAMES_TO_TYPES.get(input);
        if (type != null) {
            return type;
        }
        return Arrays.stream(values())
                .filter(e -> e.name().equalsIgnoreCase(input))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "無效的客戶類型: '" + input + "'. 有效類型為: " + getAllDisplayNames() + " 或其英文名稱。"
                ));
    }

    public static CustomerType fromStringName(String name) {
        return Arrays.stream(values())
                .filter(e -> e.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("無效的客戶類型名稱: '" + name + "'."));
    }

    public static List<String> getAllDisplayNames() {
        return Arrays.stream(values())
                .map(CustomerType::getDisplayName)
                .collect(Collectors.toList());
    }
}
