package com.example.demo.service.impl;

import com.example.demo.dto.request.BCustomerRequest;
import com.example.demo.dto.response.BCustomerDto;
import com.example.demo.entity.BCustomer;
import com.example.demo.entity.Tag;
import com.example.demo.enums.BCustomerIndustry;
import com.example.demo.enums.BCustomerLevel;
import com.example.demo.enums.BCustomerType;
import com.example.demo.repository.BCustomerRepository;
import com.example.demo.repository.TagRepository;
import com.example.demo.service.BCustomerService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class BCustomerServiceImpl implements BCustomerService {

    private final BCustomerRepository bCustomerRepository;
    private final TagRepository tagRepository;

    public BCustomerServiceImpl(BCustomerRepository bCustomerRepository,
                                TagRepository tagRepository) {
        this.bCustomerRepository = bCustomerRepository;
        this.tagRepository = tagRepository;
    }

    /**
     * 以分頁的方式獲取所有客戶。
     * @param pageable 包含分頁和排序資訊的請求物件。
     * @return 包含客戶 DTO 列表和分頁資訊的 Page 物件。
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BCustomerDto> findAll(Pageable pageable) {
        return bCustomerRepository.findAll(pageable)
                .map(this::convertToDto);
    }

    /**
     * 根據客戶的唯一 ID 尋找客戶。
     * @param id 要尋找的客戶 ID。
     * @return 包含客戶詳細資訊的 DTO。
     * @throws EntityNotFoundException 如果找不到對應 ID 的客戶。
     */
    @Override
    @Transactional(readOnly = true)
    public BCustomerDto findById(Long id) {
        BCustomer bCustomer = bCustomerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("找不到客戶，ID: " + id));
        return convertToDto(bCustomer);
    }

    /**
     * 根據請求資料建立一個新的客戶。
     * @param request 包含新客戶所有必要資訊的請求 DTO。
     * @return 建立成功後，包含新客戶完整資訊（含 ID）的 DTO。
     */
    @Override
    @Transactional
    public BCustomerDto create(BCustomerRequest request) {
        BCustomer bCustomer = new BCustomer();
        mapRequestToEntity(request, bCustomer);

        if (bCustomer.getBCustomerType() == null) {
            bCustomer.setBCustomerType(BCustomerType.REGULAR); // 預設客戶類型
        }
        if (bCustomer.getBCustomerLevel() == null) {
            bCustomer.setBCustomerLevel(BCustomerLevel.BRONZE); // 預設客戶等級
        }
        if (bCustomer.getIndustry() == null) {
            bCustomer.setIndustry(BCustomerIndustry.OTHER); // 預設客戶行業
        }

        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            List<Tag> tags = tagRepository.findAllById(request.getTagIds());
            bCustomer.setTags(new HashSet<>(tags));
        }

        BCustomer savedBCustomer = bCustomerRepository.save(bCustomer);
        return convertToDto(savedBCustomer);
    }

    /**
     * 根據 ID 更新一個已存在的客戶資訊。
     * @param id 要更新的客戶 ID。
     * @param request 包含要更新欄位的請求 DTO。
     * @return 更新成功後，包含最新客戶資訊的 DTO。
     * @throws EntityNotFoundException 如果找不到對應 ID 的客戶。
     */
    @Override
    @Transactional
    public BCustomerDto update(Long id, BCustomerRequest request) {
        BCustomer bCustomer = bCustomerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("找不到客戶，ID: " + id));

        mapRequestToEntity(request, bCustomer);

        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            List<Tag> tags = tagRepository.findAllById(request.getTagIds());
            bCustomer.setTags(new HashSet<>(tags));
        }

        BCustomer updatedBCustomer = bCustomerRepository.save(bCustomer);
        return convertToDto(updatedBCustomer);
    }

    /**
     * 根據 ID 刪除一個客戶。
     * @param id 要刪除的客戶 ID。
     * @throws EntityNotFoundException 如果找不到對應 ID 的客戶。
     */
    @Override
    @Transactional
    public void delete(Long id) {
        if (!bCustomerRepository.existsById(id)) {
            throw new EntityNotFoundException("找不到客戶，ID: " + id);
        }
        bCustomerRepository.deleteById(id);
    }

    /**
     * 根據客戶名稱進行模糊查詢（不區分大小寫）。
     * @param name 客戶名稱的部分字串。
     * @param pageable 分頁和排序資訊。
     * @return 符合條件的客戶 DTO 分頁列表。
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BCustomerDto> findCustomersByNameContaining(String name, Pageable pageable) {
        return bCustomerRepository.findByCustomerNameContainingIgnoreCase(name, pageable)
                .map(this::convertToDto);
    }

    /**
     * 根據行業查詢客戶。
     * @param industry 行業名稱 Enum。
     * @param pageable 分頁和排序資訊。
     * @return 符合條件的客戶 DTO 分頁列表。
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BCustomerDto> findCustomersByIndustry(BCustomerIndustry industry, Pageable pageable) {
        return bCustomerRepository.findByIndustry(industry, pageable)
                .map(this::convertToDto);
    }

    /**
     * 根據客戶類型查詢客戶。
     * @param bCustomerType 客戶類型 Enum。
     * @param pageable 分頁和排序資訊。
     * @return 符合條件的客戶 DTO 分頁列表。
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BCustomerDto> findCustomersByCustomerType(BCustomerType bCustomerType, Pageable pageable) {
        return bCustomerRepository.findByBCustomerType(bCustomerType, pageable)
                .map(this::convertToDto);
    }

    /**
     * 根據客戶等級查詢客戶。
     * @param bCustomerLevel 客戶等級 Enum。
     * @param pageable 分頁和排序資訊。
     * @return 符合條件的客戶 DTO 分頁列表。
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BCustomerDto> findCustomersByCustomerLevel(BCustomerLevel bCustomerLevel, Pageable pageable) {
        return bCustomerRepository.findByBCustomerLevel(bCustomerLevel, pageable)
                .map(this::convertToDto);
    }

    // ----- 輔助方法 -----
    /**
     * 將 Customer 實體轉換為 CustomerDto。
     * 此方法用於向前端返回資料，只包含前端所需的資訊。
     * @param bCustomer 要轉換的 Customer 實體。
     * @return 轉換後的 CustomerDto。
     */
    private BCustomerDto convertToDto(BCustomer bCustomer) {
        BCustomerDto dto = new BCustomerDto();
        dto.setCustomerId(bCustomer.getCustomerId());
        // ✨ 修改 #1: 使用繼承自 CustomerBase 的 getter
        dto.setCustomerName(bCustomer.getName());
        dto.setIndustry(bCustomer.getIndustry());
        dto.setBCustomerType(bCustomer.getBCustomerType());
        dto.setBCustomerLevel(bCustomer.getBCustomerLevel());
        // ✨ 修改 #2: 使用繼承自 CustomerBase 的 getter
        dto.setCustomerAddress(bCustomer.getAddress());
        dto.setCustomerTel(bCustomer.getTel());
        dto.setCustomerEmail(bCustomer.getEmail());
        dto.setCreatedAt(bCustomer.getCreatedAt());
        dto.setUpdatedAt(bCustomer.getUpdatedAt());

        List<Long> tagIds = (bCustomer.getTags() != null && !bCustomer.getTags().isEmpty())
                ? bCustomer.getTags().stream()
                .map(Tag::getTagId)
                .collect(Collectors.toList())
                : List.of();
        dto.setTagIds(tagIds);
        return dto;
    }

    /**
     * 將 CustomerRequest DTO 中的資料映射到 Customer 實體。
     * 此方法主要處理簡單的屬性複製。
     * @param request 包含請求資料的 DTO。
     * @param bCustomer 要映射的目標 Customer 實體。
     */
    private void mapRequestToEntity(BCustomerRequest request, BCustomer bCustomer) {
        // ✨ 修改 #3: 使用繼承自 CustomerBase 的 setter
        bCustomer.setName(request.getCustomerName());
        bCustomer.setIndustry(request.getIndustry());
        bCustomer.setBCustomerType(request.getBCustomerType());
        bCustomer.setBCustomerLevel(request.getBCustomerLevel());
        // ✨ 修改 #4: 使用繼承自 CustomerBase 的 setter
        bCustomer.setAddress(request.getCustomerAddress());
        bCustomer.setTel(request.getCustomerTel());
        bCustomer.setEmail(request.getCustomerEmail());

        if (request.getTagIds() != null) {
            HashSet<Tag> tags = new HashSet<>();
            for (Long tagId : request.getTagIds()) {
                Tag tag = tagRepository.findById(tagId)
                        .orElseThrow(() -> new EntityNotFoundException("找不到標籤，ID: " + tagId));
                tags.add(tag);
            }
            bCustomer.setTags(tags);
        }
    }
}