package com.example.demo.enums;

import com.example.demo.entity.Authority;
import lombok.Getter;

@Getter
public enum AuthorityCode {
    USER_READ("USER_READ", "使用者管理", "檢視帳號", "檢視使用者", "SYSTEM"),
    USER_CREATE("USER_CREATE", "使用者管理", "建立帳號", "新增使用者", "SYSTEM"),
    USER_UPDATE("USER_UPDATE", "使用者管理", "修改帳號", "編輯使用者", "SYSTEM"),
    USER_DELETE("USER_DELETE", "使用者管理", "刪除帳號", "移除使用者", "SYSTEM"),
    CUSTOMER_READ("CUSTOMER_READ", "客戶管理", "檢視客戶", "查詢客戶資料", "CRM"),
    CUSTOMER_CREATE("CUSTOMER_CREATE", "客戶管理", "建立客戶", "建立客戶資料", "CRM"),
    CUSTOMER_UPDATE("CUSTOMER_UPDATE", "客戶管理", "更新客戶", "更改客戶資料", "CRM"),
    CUSTOMER_DELETE("CUSTOMER_DELETE", "客戶管理", "刪除客戶", "刪除客戶資料", "CRM"),
    CUSTOMER_SUPPORT("CUSTOMER_SUPPORT", "客戶管理", "客戶服務", "客戶需求支援", "CRM"),
    ORDER_READ("ORDER_READ", "訂單管理", "檢視訂單", "檢視訂單", "ORDER"),
    ORDER_CREATE("ORDER_CREATE", "訂單管理", "建立訂單", "建立訂單", "ORDER"),
    ORDER_UPDATE("ORDER_UPDATE", "訂單管理", "更新訂單", "更改訂單", "ORDER"),
    ORDER_DELETE("ORDER_DELETE", "訂單管理", "刪除訂單", "刪除訂單", "ORDER"),
    ARTICLE_READ("ARTICLE_READ", "文章管理", "檢視文章", "檢視文章", "CMS"),
    ARTICLE_CREATE("ARTICLE_CREATE", "文章管理", "建立文章", "新增文章", "CMS"),
    ARTICLE_UPDATE("ARTICLE_UPDATE", "文章管理", "更新文章", "更改文章", "CMS"),
    ARTICLE_DELETE("ARTICLE_DELETE", "文章管理", "刪除文章", "刪除文章", "CMS"),
    REPORT_VIEW("REPORT_VIEW", "報表管理", "查看報表", "查詢分析", "ANALYTICS"),
    LOG_VIEW("LOG_VIEW", "操作紀錄管理", "查看操作紀錄", "操作紀錄分析", "ANALYTICS");


    private final String code;
    private final String module;
    private final String description;
    private final String displayName;
    private final String moduleGroup;

    AuthorityCode(String code, String module, String description, String displayName, String moduleGroup) {
        this.code = code;
        this.module = module;
        this.description = description;
        this.displayName = displayName;
        this.moduleGroup = moduleGroup;
    }

    public Authority toAuthorityEntity() {
        return Authority.builder()
                .code(code)
                .module(module)
                .description(description)
                .displayName(displayName)
                .moduleGroup(moduleGroup)
                .build();
    }
}
