package com.example.demo.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum CustomerLevel {
    DIAMOND("鑽石級"),   // 極高價值客戶，高於白金級
    PLATINUM("白金級"), // 最高等級客戶
    GOLD("黃金級"),     // 次高級客戶
    SILVER("白銀級"),    // 普通客戶
    BRONZE("青銅級"),   // 潛力較小或普通新客戶
    NEW("新客戶");      // 剛建立關係的客戶

    private final String displayName;

    private static final Map<String, CustomerLevel> NAMES_TO_LEVELS =
            Arrays.stream(values()).collect(Collectors.toMap(CustomerLevel::getDisplayName, Function.identity()));

    CustomerLevel(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static CustomerLevel fromDisplayName(String input) {
        CustomerLevel customerLevel = NAMES_TO_LEVELS.get(input);
        if (customerLevel != null) {
            return customerLevel;
        }
        return Arrays.stream(values())
                .filter(e -> e.name().equalsIgnoreCase(input))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "無效的客戶等級: '" + input + "'. 有效等級為: " + getAllDisplayNames() + " 或其英文名稱。"
                ));
    }

    public static CustomerLevel fromStringName(String name) {
        return Arrays.stream(values())
                .filter(e -> e.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("無效的客戶等級名稱: '" + name + "'."));
    }

    public static List<String> getAllDisplayNames() {
        return Arrays.stream(values())
                .map(CustomerLevel::getDisplayName)
                .collect(Collectors.toList());
    }
}
