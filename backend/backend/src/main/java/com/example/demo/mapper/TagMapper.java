package com.example.demo.mapper;

import com.example.demo.dto.request.TagRequest;
import com.example.demo.dto.response.TagDto;
import com.example.demo.entity.Tag;
import org.springframework.stereotype.Component;


@Component
public class TagMapper {

    /**
     * 將 Tag 實體轉換為 TagDto。
     * @param tag 要轉換的 Tag 實體。
     * @return 轉換後的 TagDto。
     */
    public TagDto toResponse(Tag tag) {
        if (tag == null) {
            return null;
        }
        return TagDto.builder()
                .tagId(tag.getTagId())
                .tagName(tag.getTagName())
                .color(tag.getColor())
                .build();
    }

    /**
     * 將 TagRequest DTO 轉換為 Tag 實體 (用於創建)。
     * @param request 標籤請求 DTO。
     * @return 轉換後的 Tag 實體。
     */
    public Tag toEntity(TagRequest request) {
        if (request == null) {
            return null;
        }
        return Tag.builder()
                .tagName(request.getTagName())
                .color(request.getColor())
                .build();
    }

    /**
     * 更新現有 Tag 實體的屬性。
     * 此方法將 TagRequest 中的數據複製到已存在的 Tag 實體中。
     * @param tag 要更新的現有 Tag 實體。
     * @param request 包含更新數據的 TagRequest DTO。
     */
    public void updateEntityFromRequest(Tag tag, TagRequest request) {
        if (tag == null || request == null) {
            return;
        }
        tag.setTagName(request.getTagName());
        tag.setColor(request.getColor());
    }

}
