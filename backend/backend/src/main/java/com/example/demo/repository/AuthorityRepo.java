package com.example.demo.repository;

import com.example.demo.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthorityRepo extends JpaRepository<Authority, Long> {

    Optional<Authority> findByCode(String code); // 用權限代碼查詢

    List<Authority> findByModule(String module); // 查某個模組下的所有權限

    List<Authority> findByModuleGroup(String moduleGroup); // 群組權限查詢（如 CMS, USER 等）

    boolean existsByCode(String code); // 是否已存在某代碼

    List<Authority> findByCodeIn(List<String> codes); // 一次查多筆

}
