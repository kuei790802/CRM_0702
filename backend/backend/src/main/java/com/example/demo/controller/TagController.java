package com.example.demo.controller;

import com.example.demo.dto.request.TagRequest;
import com.example.demo.dto.response.TagDto;
import com.example.demo.service.TagService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * 獲取所有標籤。
     * HTTP GET 請求到 /api/tags
     * @return 所有標籤的列表 (HTTP 200 OK)。
     */
    @GetMapping
    public ResponseEntity<List<TagDto>> getAll() {
        List<TagDto> tags = tagService.findAll();
        return ResponseEntity.ok(tags);
    }

    /**
     * 根據 ID 獲取單個標籤。
     * HTTP GET 請求到 /api/tags/{id}
     * @param id 標籤的唯一識別碼。
     * @return 對應的標籤 DTO (HTTP 200 OK) 或 404 Not Found 響應。
     */
    @GetMapping("/{id}")
    public ResponseEntity<TagDto> getById(@PathVariable Long id) {
        return tagService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 根據名稱查找標籤。
     * HTTP GET 請求到 /api/tags/byName?name=高潛力
     * @param tagName 標籤名稱。
     * @return 對應的標籤 DTO (HTTP 200 OK) 或 404 Not Found 響應。
     */
    @GetMapping("/byName")
    public ResponseEntity<TagDto> getTagByName(@RequestParam String tagName) {
        return tagService.findByTagName(tagName)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    /**
     * 創建新標籤。
     * HTTP POST 請求到 /api/tags
     * @param request 標籤請求 DTO。
     * @return 創建後的標籤 DTO (HTTP 201 Created)。
     */
    @PostMapping
    public ResponseEntity<TagDto> create(@Valid @RequestBody TagRequest request) {
        TagDto createdTag = tagService.create(request);
        return new ResponseEntity<>(createdTag, HttpStatus.CREATED);
    }

    /**
     * 更新現有標籤。
     * HTTP PUT 請求到 /api/tags/{id}
     * @param id 標籤的唯一識別碼。
     * @param request 標籤請求 DTO。
     * @return 更新後的標籤 DTO (HTTP 200 OK)。
     */
    @PutMapping("/{id}")
    public ResponseEntity<TagDto> update(@PathVariable Long id, @Valid @RequestBody TagRequest request) {
        TagDto updatedTag = tagService.update(id, request);
        return ResponseEntity.ok(updatedTag);
    }

    /**
     * 根據 ID 刪除標籤。
     * HTTP DELETE 請求到 /api/tags/{id}
     * @param id 標籤的唯一識別碼。
     * @return 204 No Content 響應。
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tagService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
