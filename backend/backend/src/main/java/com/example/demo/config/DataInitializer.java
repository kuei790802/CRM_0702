package com.example.demo.config;

import com.example.demo.entity.Authority;
import com.example.demo.entity.User;
import com.example.demo.enums.AuthorityCode;
import com.example.demo.repository.AuthorityRepo;
import com.example.demo.repository.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepo userRepo;
    private final AuthorityRepo authorityRepo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public DataInitializer(UserRepo userRepo, AuthorityRepo authorityRepo) {
        this.userRepo = userRepo;
        this.authorityRepo = authorityRepo;
    }

    @Override
    public void run(String... args) {
        // 確認所有 enum 權限是否已存在，若不存在就建立
        for (AuthorityCode code : AuthorityCode.values()) {
            if (!authorityRepo.existsByCode(code.getCode())) {
                authorityRepo.save(code.toAuthorityEntity());
            }
        }

        // 建立 admin 使用者，並賦予所有權限（從資料庫抓取）
        if (userRepo.findByAccount("admin").isEmpty()) {
            List<Authority> allAuthorities = authorityRepo.findAll();


//        if (userRepo.findByAccount("admin").isEmpty()) {
//            Authority allAccess = authorityRepo.save(
//                    Authority.builder()
//                            .code("ALL_ACCESS")
//                            .module("System")
//                            .description("全系統存取")
//                            .displayName("系統管理權限")
//                            .moduleGroup("SYSTEM")
//                            .build()
//            );

            User admin = User.builder()
                    .account("admin")
                    .userName("超級管理員")
                    .password(encoder.encode("Admin123!@#"))
                    .email("admin@system.com")
                    .roleName("ADMIN")
                    .authorities(allAuthorities)
                    .isActive(true)
                    .isDeleted(false)
                    .accessStartDate(LocalDate.now())
                    .accessEndDate(LocalDate.now().plusYears(10))
                    .build();

            userRepo.save(admin);
        }
    }
}