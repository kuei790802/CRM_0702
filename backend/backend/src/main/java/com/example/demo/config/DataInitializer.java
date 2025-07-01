package com.example.demo.config;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

import com.example.demo.entity.*;
import com.example.demo.enums.AuthorityCode;
import com.example.demo.enums.VIPLevelEnum;
import com.example.demo.repository.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.github.javafaker.Faker;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@Slf4j
public class DataInitializer {


    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private ProductCategoryRepository categoryRepository;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthorityRepo authorityRepo;

    @Autowired
    private VIPLevelRepo vipLevelRepo;


    private static final Long SYSTEM_USER_ID = 1L;


    private static final String[] ICE_CREAM_PREFIXES = {"經典", "純濃", "雪藏", "夏日", "莊園", "極致", "鮮果"};
    private static final String[] ICE_CREAM_NAMES = {"香草", "巧克力","藍莓","花生","咖啡", "香蕉","薄荷巧克力","OREO","草莓", "抹茶", "蘭姆葡萄", "海鹽焦糖", "芒果優格", "豆乳芝麻", "燕麥奶"};
    private static final String[] ICE_CREAM_SUFFIXES = {"冰淇淋", "雪酪", "聖代", "冰棒", "雪糕"};


    @PostConstruct
    public void initCriticalData() {
        initAuthoritiesAndAdmin();
        createVipLevelsIfNotExist();
    }


    @Bean
    @Profile("dev")
    public CommandLineRunner initDatabase() {
        return args -> {
            log.info("DataInitializer started. Current profile check...");
            log.info("Product count: {}", productRepository.count());

            if (productRepository.count() > 0) {
                System.out.println("資料庫中已有商品，跳過假資料產生程序。");
                return;
            }

            log.info("Starting data initialization...");

            try {

                createSystemUserIfNotExists();

                List<Unit> savedUnits = createAndSaveUnits();
                log.info("Created {} units", savedUnits.size());

                List<ProductCategory> savedCategories = createAndSaveCategories();
                log.info("Created {} categories", savedCategories.size());

            System.out.println("偵測到為 dev 環境且無商品資料，開始產生50筆商品假資料...");

            Faker faker = new Faker(Locale.TAIWAN);
            List<Product> productList = new ArrayList<>();

            for (int i = 0; i < 50; i++) {
                Product product = new Product();

                String name = ICE_CREAM_PREFIXES[ThreadLocalRandom.current().nextInt(ICE_CREAM_PREFIXES.length)] +
                              ICE_CREAM_NAMES[ThreadLocalRandom.current().nextInt(ICE_CREAM_NAMES.length)] +
                              ICE_CREAM_SUFFIXES[ThreadLocalRandom.current().nextInt(ICE_CREAM_SUFFIXES.length)];

                String uniqueCode = "P" + String.format("%06d", i + 1);
                product.setProductCode(uniqueCode);
                product.setName(name + " " + uniqueCode);
                product.setDescription(faker.lorem().paragraph(2));


                Unit randomUnit = savedUnits.get(ThreadLocalRandom.current().nextInt(savedUnits.size()));
                ProductCategory randomCategory = savedCategories.get(ThreadLocalRandom.current().nextInt(savedCategories.size()));

                product.setUnit(randomUnit);
                product.setCategory(randomCategory);

                double price = ThreadLocalRandom.current().nextDouble(45.0, 350.0);
                product.setBasePrice(BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP
                ));
                product.setCostMethod("AVERAGE");
                product.setTaxType("TAXABLE");
                product.setSafetyStockQuantity(ThreadLocalRandom.current().nextInt(10, 100));

                product.setCreatedBy(SYSTEM_USER_ID);
                product.setUpdatedBy(SYSTEM_USER_ID);

                productList.add(product);
            }

            productRepository.saveAll(productList);
            System.out.println("50筆商品假資料已成功寫入資料庫！");

            } catch (Exception e) {
                log.error("Error during data initialization", e);
                throw e;
            }

        };
    }

    // 預先建立資料庫方法
    private void createSystemUserIfNotExists() {
        if (userRepository.findById(SYSTEM_USER_ID).isEmpty()) {
            log.info("Creating system user with ID: {}", SYSTEM_USER_ID);
            User systemUser = new User(); // Changed from Users to User
            systemUser.setUserId(SYSTEM_USER_ID); // Assuming User has setUserId or it's handled by DB
            systemUser.setUserName("system"); // Changed from setUsername
            systemUser.setAccount("system"); // User entity has 'account', Users had 'username' for this typically
            systemUser.setEmail("system@example.com");

            String systemPassword = "abc123456"; // You can change this to any password
            String hashedPassword = passwordEncoder.encode(systemPassword);
            systemUser.setPassword(hashedPassword); // Changed from setPasswordHash

            // systemUser.setRoleId(1L); // User entity uses List<Authority> authorities. Skipping for now.
            // TODO: Set authorities if needed, e.g. find or create an Authority and add to systemUser.getAuthorities()

            userRepository.save(systemUser); // Should now match UserRepository<User, Long>
            log.info("System user created successfully");
        } else {
            log.info("System user already exists");
        }
    }
    private List<Unit> createAndSaveUnits() {
        if (unitRepository.count() > 0) {
            return unitRepository.findAll();
        }
        System.out.println("建立商品單位...");
        List<Unit> units = new ArrayList<>();
        Arrays.asList("個", "箱", "打", "公克","支","杯","盒").forEach(name -> {
            Unit unit = new Unit();
            unit.setName(name);

            unit.setCreatedBy(SYSTEM_USER_ID);
            unit.setUpdatedBy(SYSTEM_USER_ID);
            units.add(unit);
        });
        return unitRepository.saveAll(units);
    }
    private List<ProductCategory> createAndSaveCategories() {
        if (categoryRepository.count() > 0) {
            return categoryRepository.findAll();
        }
        System.out.println("建立商品分類...");
        List<ProductCategory> categories = new ArrayList<>();
        Arrays.asList("經典冰淇淋", "水果雪酪","雪糕系列","巧酥雪糕系列", "季節限定", "純素系列", "品牌聯名系列","週邊商品").forEach(name -> {
            ProductCategory category = new ProductCategory();
            category.setName(name);

            category.setCreatedBy(SYSTEM_USER_ID);
            category.setUpdatedBy(SYSTEM_USER_ID);
            categories.add(category);
        });
        return categoryRepository.saveAll(categories);
    }
    private void initAuthoritiesAndAdmin(){
        // 1. 建立所有 Enum 權限
        for (AuthorityCode code : AuthorityCode.values()) {
            if (!authorityRepo.existsByCode(code.getCode())) {
                authorityRepo.save(code.toAuthorityEntity());
            }
        }

        // 2. 建立 admin 使用者
        if (userRepository.findByAccount("admin").isEmpty()) {
            List<Authority> allAuthorities = authorityRepo.findAll();
            User admin = User.builder()
                    .account("admin")
                    .userName("超級管理員")
                    .password(passwordEncoder.encode("Admin123!@#"))
                    .email("admin@system.com")
                    .roleName("ADMIN")
                    .authorities(allAuthorities)
                    .isActive(true)
                    .isDeleted(false)
                    .accessStartDate(LocalDate.now())
                    .accessEndDate(LocalDate.now().plusYears(10))
                    .build();
            userRepository.save(admin);
        }
    }
    private void createVipLevelsIfNotExist() {
        for (VIPLevelEnum levelEnum : VIPLevelEnum.values()) {
            if (!vipLevelRepo.existsById(levelEnum.name())) {
                vipLevelRepo.save(levelEnum.toEntity());
            }
        }
    }

}

