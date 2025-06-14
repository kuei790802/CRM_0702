package com.example.demo.dto.request;

import java.util.Set;

public class UpdateTagsRequest {

    /**
     * 【新增】用於接收「更新標籤」時的請求資料。
     */

    private Set<Long> tagIds;

    // Getter and Setter
    public Set<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(Set<Long> tagIds) {
        this.tagIds = tagIds;
    }
}