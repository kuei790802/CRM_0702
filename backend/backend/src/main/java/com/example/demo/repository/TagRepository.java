package com.example.demo.repository;

import com.example.demo.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    // 根據標籤名稱查找標籤，用於確保標籤名稱的唯一性或獲取現有標籤
    Optional<Tag> findByTagName(String tagName);

}
