//package com.example.demo.config;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Locale;
//import java.util.concurrent.ThreadLocalRandom;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//
//import com.example.demo.entity.Product;
//import com.example.demo.entity.ProductCategory;
//import com.example.demo.entity.Unit;
//import com.example.demo.repository.ProductCategoryRepository;
//import com.example.demo.repository.ProductRepository;
//import com.example.demo.repository.UnitRepository;
//import com.github.javafaker.Faker;
//
//@Configuration
//public class DataInitializer {
//
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private UnitRepository unitRepository;
//
//    @Autowired
//    private ProductCategoryRepository categoryRepository;
//
//    private static final Long SYSTEM_USER_ID = 1L;
//
//
//    private static final String[] ICE_CREAM_PREFIXES = {"經典", "純濃", "雪藏", "夏日", "莊園", "極致", "鮮果"};
//    private static final String[] ICE_CREAM_NAMES = {"香草", "巧克力","藍莓","花生","咖啡", "香蕉","薄荷巧克力","OREO","草莓", "抹茶", "蘭姆葡萄", "海鹽焦糖", "芒果優格", "豆乳芝麻", "燕麥奶"};
//    private static final String[] ICE_CREAM_SUFFIXES = {"冰淇淋", "雪酪", "聖代", "冰棒", "雪糕"};
//
//    @Bean
//    @Profile("dev")
//    public CommandLineRunner initDatabase() {
//        return args -> {
//            if (productRepository.count() > 0) {
//                System.out.println("資料庫中已有商品，跳過假資料產生程序。");
//                return;
//            }
//
//
//            List<Unit> savedUnits = createAndSaveUnits();
//            List<ProductCategory> savedCategories = createAndSaveCategories();
//
//            System.out.println("偵測到為 dev 環境且無商品資料，開始產生50筆商品假資料...");
//
//            Faker faker = new Faker(Locale.TAIWAN);
//            List<Product> productList = new ArrayList<>();
//
//            for (int i = 0; i < 50; i++) {
//                Product product = new Product();
//
//                String name = ICE_CREAM_PREFIXES[ThreadLocalRandom.current().nextInt(ICE_CREAM_PREFIXES.length)] +
//                              ICE_CREAM_NAMES[ThreadLocalRandom.current().nextInt(ICE_CREAM_NAMES.length)] +
//                              ICE_CREAM_SUFFIXES[ThreadLocalRandom.current().nextInt(ICE_CREAM_SUFFIXES.length)];
//
//                String uniqueCode = "P" + String.format("%06d", i + 1);
//                product.setProductCode(uniqueCode);
//                product.setName(name + " " + uniqueCode);
//                product.setDescription(faker.lorem().paragraph(2));
//
//
//                Unit randomUnit = savedUnits.get(ThreadLocalRandom.current().nextInt(savedUnits.size()));
//                ProductCategory randomCategory = savedCategories.get(ThreadLocalRandom.current().nextInt(savedCategories.size()));
//
//                product.setUnit(randomUnit);
//                product.setCategory(randomCategory);
//
//                double price = ThreadLocalRandom.current().nextDouble(45.0, 350.0);
//                product.setBasePrice(BigDecimal.valueOf(price).setScale(2, BigDecimal.ROUND_HALF_UP));
//                product.setCostMethod("AVERAGE");
//                product.setTaxType("TAXABLE");
//                product.setSafetyStockQuantity(ThreadLocalRandom.current().nextInt(10, 100));
//
//                product.setCreatedBy(SYSTEM_USER_ID);
//                product.setUpdatedBy(SYSTEM_USER_ID);
//
//                productList.add(product);
//            }
//
//            productRepository.saveAll(productList);
//            System.out.println("50筆商品假資料已成功寫入資料庫！");
//        };
//    }
//
//    private List<Unit> createAndSaveUnits() {
//        if (unitRepository.count() > 0) {
//            return unitRepository.findAll();
//        }
//        System.out.println("建立商品單位...");
//        List<Unit> units = new ArrayList<>();
//        Arrays.asList("個", "箱", "打", "公克","支","杯","盒").forEach(name -> {
//            Unit unit = new Unit();
//            unit.setName(name);
//
//            unit.setCreatedBy(SYSTEM_USER_ID);
//            unit.setUpdatedBy(SYSTEM_USER_ID);
//            units.add(unit);
//        });
//        return unitRepository.saveAll(units);
//    }
//
//
//    private List<ProductCategory> createAndSaveCategories() {
//        if (categoryRepository.count() > 0) {
//            return categoryRepository.findAll();
//        }
//        System.out.println("建立商品分類...");
//        List<ProductCategory> categories = new ArrayList<>();
//        Arrays.asList("經典冰淇淋", "水果雪酪","雪糕系列","巧酥雪糕系列", "季節限定", "純素系列", "品牌聯名系列","週邊商品").forEach(name -> {
//            ProductCategory category = new ProductCategory();
//            category.setName(name);
//
//            category.setCreatedBy(SYSTEM_USER_ID);
//            category.setUpdatedBy(SYSTEM_USER_ID);
//            categories.add(category);
//        });
//        return categoryRepository.saveAll(categories);
//    }
//}