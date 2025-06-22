package com.example.demo.service.impl;

import com.example.demo.dto.request.TagRequest;
import com.example.demo.dto.response.TagDto;
import com.example.demo.entity.Tag;
import com.example.demo.mapper.TagMapper;
import com.example.demo.repository.TagRepository;
import com.example.demo.service.TagService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    public TagServiceImpl(TagRepository tagRepository, TagMapper tagMapper) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TagDto> findAll() {
        return tagRepository.findAll().stream()
                .map(tagMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TagDto> findById(Long id) {
        return tagRepository.findById(id)
                .map(tagMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TagDto> findByTagName(String tagName) {
        return tagRepository.findByTagName(tagName)
                .map(tagMapper::toResponse);
    }

    @Override
    @Transactional
    public TagDto create(TagRequest request) {
        if (tagRepository.findByTagName(request.getTagName()).isPresent()) {
            throw new IllegalArgumentException("標籤名稱 '" + request.getTagName() + "' 已存在。");
        }
        Tag tag = tagMapper.toEntity(request);
        Tag savedTag = tagRepository.save(tag);
        return tagMapper.toResponse(savedTag);
    }

    @Override
    @Transactional
    public TagDto update(Long id, TagRequest request) {
        Tag existingTag = tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("找不到 ID 為 " + id + " 的標籤"));

        Optional<Tag> tagWithSameName = tagRepository.findByTagName(request.getTagName());
        if (tagWithSameName.isPresent() && !tagWithSameName.get().getTagId().equals(id)) {
            throw new IllegalArgumentException("標籤名稱 '" + request.getTagName() + "' 已被其他標籤使用。");
        }

        tagMapper.updateEntityFromRequest(existingTag, request);
        Tag updatedTag = tagRepository.save(existingTag);
        return tagMapper.toResponse(updatedTag);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new EntityNotFoundException("找不到 ID 為 " + id + " 的標籤進行刪除");
        }
        tagRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tag> getTagsByIds(List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return List.of();
        }

        List<Tag> tags = tagRepository.findAllById(tagIds);
        if (tags.size() != tagIds.size()) {
            Set<Long> foundIds = tags.stream().map(Tag::getTagId).collect(Collectors.toSet());
            String missingIds = tagIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));
            throw new EntityNotFoundException("找不到部分標籤 ID: " + missingIds);
        }
        return tags;
    }
}

