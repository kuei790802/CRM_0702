package com.example.demo.enums;

import com.fasterxml.jackson.annotation.JsonValue;


public enum OpportunityStage {

    INITIAL_CONTACT("初步接洽"), // 初步接洽
    NEEDS_ANALYSIS("需求分析"), // 需求分析
    PROPOSAL("提案"), // 提案
    NEGOTIATION("談判"), // 談判
    CLOSED_WON("已成交"), // 已成交
    CLOSED_LOST("已丟失"); // 已丟失

    private final String displayName;

    OpportunityStage(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
