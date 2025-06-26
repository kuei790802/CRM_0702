package com.example.demo.config;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

import com.example.demo.config.util.CCustomerFaker;
import com.example.demo.config.util.InventoryFaker;
import com.example.demo.config.util.ProductFaker;
import com.example.demo.config.util.SalesOrderFaker;
import com.example.demo.dto.erp.ProductResponseDTO;
import com.example.demo.entity.*;
import com.example.demo.entity.Inventory;
import com.example.demo.repository.*;
import com.example.demo.service.erp.InventoryService;
import com.example.demo.service.erp.ProductService;
import com.example.demo.service.erp.SalesOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.github.javafaker.Faker;

// Imports from initializer/DataInitializer
import com.example.demo.dto.request.BCustomerRequest;
import com.example.demo.dto.request.CCustomerRegisterRequest;
import com.example.demo.dto.request.ContactRequest;
import com.example.demo.dto.request.OpportunityRequest;
import com.example.demo.dto.request.TagRequest;
import com.example.demo.dto.erp.ProductCreateDTO;
import com.example.demo.dto.erp.SalesOrderCreateDTO;
import com.example.demo.dto.response.BCustomerDto;
import com.example.demo.dto.response.ContactDto;
import com.example.demo.dto.response.OpportunityDto;
import com.example.demo.dto.response.TagDto;
// Entities already imported mostly, ensure all are covered
// Repositories
import com.example.demo.repository.SalesOrderRepository;
import com.example.demo.repository.WarehouseRepository;
import com.example.demo.repository.InventoryRepository;
import com.example.demo.repository.CCustomerRepo;
// Services
import com.example.demo.service.*;
// Utils (Fakers)
import com.example.demo.util.*;
import jakarta.persistence.EntityNotFoundException;

import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;


@Configuration
@Slf4j
public class DataInitializer {

    // Original dependencies
    @Autowired private ProductRepository productRepository;
    @Autowired private UnitRepository unitRepository;
    @Autowired private ProductCategoryRepository categoryRepository;
    @Autowired private UserRepo userRepository;
    @Autowired private BCryptPasswordEncoder passwordEncoder;

    // Dependencies from initializer/DataInitializer
    @Autowired private BCustomerService bCustomerService;
    @Autowired private CCustomerService cCustomerService;
    @Autowired private ContactService contactService;
    @Autowired private OpportunityService opportunityService;
    @Autowired private TagService tagService;
    @Autowired private ProductService productService; // Note: ProductRepository also exists
    @Autowired private InventoryService inventoryService;
    @Autowired private SalesOrderService salesOrderService;

    @Autowired private BCustomerFaker bCustomerFaker;
    @Autowired private CCustomerFaker cCustomerFaker;
    @Autowired private ContactFaker contactFaker;
    @Autowired private OpportunityFaker opportunityFaker;
    @Autowired private TagFaker tagFaker;
    @Autowired private ProductFaker productFaker; // Note: separate from the Product generation below
    @Autowired private InventoryFaker inventoryFaker;
    @Autowired private SalesOrderFaker salesOrderFaker;

    @Autowired private WarehouseRepository warehouseRepository;
    @Autowired private InventoryRepository inventoryRepository;
    @Autowired private CCustomerRepo cCustomerRepo;
    @Autowired private SalesOrderRepository salesOrderRepository;

    private final Random random = new Random();
    private static final Long SYSTEM_USER_ID = 1L;

    private static final String[] ICE_CREAM_PREFIXES = {"經典", "純濃", "雪藏", "夏日", "莊園", "極致", "鮮果"};
    private static final String[] ICE_CREAM_NAMES = {"香草", "巧克力","藍莓","花生","咖啡", "香蕉","薄荷巧克力","OREO","草莓", "抹茶", "蘭姆葡萄", "海鹽焦糖", "芒果優格", "豆乳芝麻", "燕麥奶"};
    private static final String[] ICE_CREAM_SUFFIXES = {"冰淇淋", "雪酪", "聖代", "冰棒", "雪糕"};

    @Bean
    @Profile("dev")
    @Transactional // Added transactional for the entire initialization
    public CommandLineRunner initDatabase() {
        return args -> {
            log.info("DataInitializer started. Current profile check...");

            // --- Original config/DataInitializer logic first ---
            createSystemUserIfNotExists();
            List<Unit> savedUnits = createAndSaveUnits();
            log.info("Created {} units or found existing.", savedUnits.size());
            List<ProductCategory> savedCategories = createAndSaveCategories();
            log.info("Created {} categories or found existing.", savedCategories.size());

            if (productRepository.count() == 0) {
                log.info("No products found. Generating 50 initial ice cream products...");
                Faker iceCreamFaker = new Faker(Locale.TAIWAN);
                List<Product> productList = new ArrayList<>();
                for (int i = 0; i < 50; i++) {
                    Product product = new Product();
                    String name = ICE_CREAM_PREFIXES[ThreadLocalRandom.current().nextInt(ICE_CREAM_PREFIXES.length)] +
                            ICE_CREAM_NAMES[ThreadLocalRandom.current().nextInt(ICE_CREAM_NAMES.length)] +
                            ICE_CREAM_SUFFIXES[ThreadLocalRandom.current().nextInt(ICE_CREAM_SUFFIXES.length)];
                    String uniqueCode = "P" + String.format("%06d", i + 1);
                    product.setProductCode(uniqueCode);
                    product.setName(name + " " + uniqueCode);
                    product.setDescription(iceCreamFaker.lorem().paragraph(2));
                    Unit randomUnit = savedUnits.get(ThreadLocalRandom.current().nextInt(savedUnits.size()));
                    ProductCategory randomCategory = savedCategories.get(ThreadLocalRandom.current().nextInt(savedCategories.size()));
                    product.setUnit(randomUnit);
                    product.setCategory(randomCategory);
                    double price = ThreadLocalRandom.current().nextDouble(45.0, 350.0);
                    product.setBasePrice(BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP));
                    product.setCostMethod("AVERAGE");
                    product.setTaxType("TAXABLE");
                    product.setSafetyStockQuantity(ThreadLocalRandom.current().nextInt(10, 100));
                    product.setCreatedBy(SYSTEM_USER_ID);
                    product.setUpdatedBy(SYSTEM_USER_ID);
                    productList.add(product);
                }
                productRepository.saveAll(productList);
                log.info("50 initial ice cream products successfully written to the database!");
            } else {
                log.info("Products already exist in the database (count: {}), skipping initial 50 ice cream product generation.", productRepository.count());
            }

            // --- Merged logic from initializer/DataInitializer ---
            log.info("--- Starting ERP data generation (dev profile only) ---");

            // 0. Generate Tags
            int numTags = 5;
            List<TagRequest> fakeTagRequests = tagFaker.generateFakeTagRequests(numTags);
            List<Long> tagIds = new ArrayList<>();
            log.info("Generating and saving {} fake tags...", numTags);
            for (TagRequest request : fakeTagRequests) {
                try {
                    TagDto createdTag = tagService.create(request);
                    tagIds.add(createdTag.getTagId());
                    log.info("Successfully created tag: {} (ID: {})", createdTag.getTagName(), createdTag.getTagId());
                } catch (IllegalArgumentException e) {
                    log.warn("Failed to create tag (name {} already exists)", request.getTagName());
                    tagService.findByTagName(request.getTagName()).ifPresent(tag -> tagIds.add(tag.getTagId()));
                } catch (Exception e) {
                    log.error("Failed to create tag: {}", e.getMessage(), e);
                }
            }
            if (tagIds.isEmpty()) {
                log.error("No tags were created. Customers and opportunities cannot be associated with tags.");
            }
            log.info("Tag data generation complete. Created tag IDs: {}", tagIds);

            // 1. Generate BCustomer data
            int numBCustomers = 50;
            List<BCustomerRequest> fakeBCustomerRequests = bCustomerFaker.generateFakeBCustomerRequests(numBCustomers);
            List<Long> bCustomerIds = new ArrayList<>();
            log.info("Generating and saving {} fake B2B customers...", numBCustomers);
            for (BCustomerRequest request : fakeBCustomerRequests) {
                List<Long> associatedTagIdsForCustomer = new ArrayList<>();
                if (!tagIds.isEmpty()) {
                    int tagsToAssociate = random.nextInt(Math.min(4, tagIds.size() + 1));
                    Collections.shuffle(tagIds);
                    for (int i = 0; i < tagsToAssociate; i++) {
                        associatedTagIdsForCustomer.add(tagIds.get(i));
                    }
                }
                request.setTagIds(associatedTagIdsForCustomer.stream().distinct().collect(Collectors.toList()));
                try {
                    BCustomerDto createdCustomer = bCustomerService.create(request);
                    bCustomerIds.add(createdCustomer.getCustomerId());
                    log.info("Successfully created B2B customer: {} (ID: {}), associated tag IDs: {}", createdCustomer.getCustomerName(), createdCustomer.getCustomerId(), request.getTagIds());
                } catch (Exception e) {
                    log.error("Failed to create B2B customer: {}", e.getMessage(), e);
                }
            }
            if (bCustomerIds.isEmpty()) {
                log.error("No B2B customers were created. Cannot proceed with generating contacts and opportunities.");
            } else {
                log.info("B2B customer data generation complete. Created B2B customer IDs: {}", bCustomerIds);
            }

            // 2. Generate CCustomer data
            int numCCustomers = 50;
            List<CCustomerRegisterRequest> fakeCCustomerRequests = cCustomerFaker.generateFakeCCustomerRequests(numCCustomers);
            List<Long> cCustomerIds = new ArrayList<>();
            log.info("Generating and saving {} fake B2C customers...", numCCustomers);
            for (CCustomerRegisterRequest request : fakeCCustomerRequests) {
                try {
                    CCustomer createdCCustomer = cCustomerService.register(
                            request.getAccount(),
                            request.getCustomerName(), // Corrected: CCustomerRegisterRequest has getCustomerName()
                            request.getPassword(),
                            request.getEmail(),          // Corrected: RegisterRequest (superclass) has getEmail()
                            request.getAddress(),        // Corrected: CCustomerRegisterRequest has getAddress()
                            request.getBirthday());
                    cCustomerIds.add(createdCCustomer.getCustomerId()); // CCustomer inherits getCustomerId from CustomerBase
                    log.info("Successfully created B2C customer: {} (ID: {})", createdCCustomer.getCustomerName(), createdCCustomer.getCustomerId());
                } catch (Exception e) {
                    log.error("Failed to create B2C customer: {}", e.getMessage(), e);
                }
            }
            if (cCustomerIds.isEmpty()) {
                log.error("No B2C customers were created.");
            } else {
                log.info("B2C customer data generation complete. Created B2C customer IDs: {}", cCustomerIds);
            }

            List<Long> allCustomerIds = new ArrayList<>();
            allCustomerIds.addAll(bCustomerIds);
            allCustomerIds.addAll(cCustomerIds);

            // 3. Generate Contact data (associated with BCustomer)
            if (!bCustomerIds.isEmpty()) {
                int numContacts = bCustomerIds.size() * 2;
                List<ContactRequest> fakeContactRequests = contactFaker.generateFakeContactRequests(numContacts, bCustomerIds);
                List<Long> contactIds = new ArrayList<>(); // This list is not used later, but kept for logging consistency
                log.info("Generating and saving {} fake contacts...", numContacts);
                for (ContactRequest request : fakeContactRequests) {
                    try {
                        ContactDto createdContact = contactService.create(request);
                        contactIds.add(createdContact.getContactId());
                        log.info("Successfully created contact: {} (ID: {}) for B2B customer ID: {}", createdContact.getContactName(), createdContact.getContactId(), request.getCustomerId());
                    } catch (EntityNotFoundException e) {
                        log.warn("Failed to create contact (BCustomer ID {} not found): {}", request.getCustomerId(), e.getMessage());
                    } catch (Exception e) {
                        log.error("Error creating contact: {}", e.getMessage(), e);
                    }
                }
                log.info("Contact data generation complete. Created contact IDs: {}", contactIds);
            } else {
                log.warn("No B2B customers available, skipping contact generation.");
            }

            // 4. Generate Additional Products (ERP specific, using ProductFaker)
            int numErpProducts = 20;
            List<ProductCreateDTO> fakeErpProductRequests = productFaker.generateFakeProductCreateDTOs(numErpProducts);
            List<Long> erpProductIds = new ArrayList<>();
            log.info("Generating and saving {} additional ERP fake products...", numErpProducts);
            for (ProductCreateDTO request : fakeErpProductRequests) {
                try {
                    // Corrected call to use createProductFromDTO and pass SYSTEM_USER_ID
                    ProductResponseDTO createdProductDTO = productService.createProductFromDTO(request, SYSTEM_USER_ID);
                    erpProductIds.add(createdProductDTO.getProductId()); // Assuming ProductResponseDTO has getProductId()
                    log.info("Successfully created ERP product: {} (ID: {})", createdProductDTO.getName(), createdProductDTO.getProductId());
                } catch (Exception e) {
                    log.error("Failed to create ERP product: {}", e.getMessage(), e);
                }
            }
            if (erpProductIds.isEmpty()) {
                log.error("No ERP products were created. This might affect inventory and sales order generation for these specific products.");
            } else {
                log.info("ERP Product data generation complete. Created ERP product IDs: {}", erpProductIds);
            }

            // Combine all product IDs for inventory and sales orders (initial + ERP)
            List<Long> allProductIds = new ArrayList<>(erpProductIds);
            // If you want to include the initially generated 50 products for inventory/sales orders:
            // productRepository.findAll().forEach(p -> allProductIds.add(p.getProductId()));
            // For now, SalesOrders will use the 20 ERP products. If the initial 50 are needed, they need to be fetched.
            // Let's fetch all product IDs from the repository to ensure all available products can be used.
            allProductIds.clear(); // Clear previous list
            productRepository.findAll().forEach(p -> allProductIds.add(p.getProductId()));
            if(allProductIds.isEmpty()){
                log.error("No products found in repository after generation attempts. Cannot proceed with inventory/sales orders.");
                return; // Critical to stop if no products
            }


            // 5. Generate Inventory data
            Warehouse defaultWarehouse = warehouseRepository.findById(1L)
                    .orElseGet(() -> {
                        Warehouse wh = new Warehouse();
                        wh.setName("Default Warehouse");
                        // wh.setLocation("Default Location"); // Field does not exist
                        wh.setCreatedAt(java.time.LocalDateTime.now());
                        // wh.setUpdatedAt(java.time.LocalDateTime.now()); // Field does not exist
                        wh.setCreatedBy(SYSTEM_USER_ID);
                        // wh.setUpdatedBy(SYSTEM_USER_ID); // Field does not exist
                        return warehouseRepository.save(wh);
                    });

            log.info("Generating inventory data for {} products in warehouse ID: {}...", allProductIds.size(), defaultWarehouse.getWarehouseId());
            for (Long productId : allProductIds) {
                try {
                    Product product = productRepository.findById(productId)
                            .orElseThrow(() -> new EntityNotFoundException("Product not found for ID: " + productId + " during inventory generation."));

                    Inventory inventory = inventoryRepository.findByProductAndWarehouse(product, defaultWarehouse)
                            .orElseGet(() -> inventoryFaker.createFakeInventory(product, defaultWarehouse));

                    // Ensure stock is at least 50
                    BigDecimal currentStock = inventory.getCurrentStock() != null ? inventory.getCurrentStock() : BigDecimal.ZERO;
                    if (currentStock.compareTo(BigDecimal.valueOf(50)) < 0) {
                        inventory.setCurrentStock(BigDecimal.valueOf(50 + random.nextInt(51))); // Stock between 50 and 100
                    } else {
                        inventory.setCurrentStock(currentStock.add(BigDecimal.valueOf(random.nextInt(51)))); // Add some more random stock
                    }

                    if (inventory.getAverageCost() == null || inventory.getAverageCost().compareTo(BigDecimal.ZERO) == 0) {
                        inventory.setAverageCost(product.getBasePrice() != null && product.getBasePrice().compareTo(BigDecimal.ZERO) > 0 ?
                                product.getBasePrice().multiply(BigDecimal.valueOf(0.8)) :
                                BigDecimal.valueOf(random.nextDouble(5.0, 50.0)).setScale(2, RoundingMode.HALF_UP)); // Fallback average cost
                    }
                    inventory.setCreatedBy(SYSTEM_USER_ID); // Assuming Inventory entity has this
                    inventory.setUpdatedBy(SYSTEM_USER_ID); // Assuming Inventory entity has this
                    inventoryRepository.save(inventory); // Use repository directly
                    log.info("Inventory updated/created for Product ID: {}, Warehouse ID: {}. Current stock: {}", productId, defaultWarehouse.getWarehouseId(), inventory.getCurrentStock());
                } catch (Exception e) {
                    log.error("Failed to create/update inventory for product ID {}: {}", productId, e.getMessage(), e);
                }
            }
            log.info("Inventory data generation complete.");

            // 6. Generate SalesOrder data
            int numSalesOrders = 500;
            log.info("Generating and saving {} fake sales orders...", numSalesOrders);
            if (allCustomerIds.isEmpty() || allProductIds.isEmpty()) {
                log.error("Cannot generate sales orders due to missing customers or products.");
            } else {
                List<SalesOrderCreateDTO> fakeSalesOrderRequests = salesOrderFaker.generateFakeSalesOrderCreateDTOs(numSalesOrders, allCustomerIds, allProductIds, cCustomerRepo, bCustomerService);
                for (SalesOrderCreateDTO request : fakeSalesOrderRequests) {
                    try {
                        // Corrected call to pass SYSTEM_USER_ID
                        SalesOrder createdSalesOrder = salesOrderService.createSalesOrder(request, SYSTEM_USER_ID);
                        log.info("Successfully created sales order: {} (ID: {}) for Customer ID: {}", createdSalesOrder.getOrderNumber(), createdSalesOrder.getSalesOrderId(), request.getCustomerId());
                    } catch (Exception e) {
                        log.error("Failed to create sales order for customer ID {}: {}", request.getCustomerId(), e.getMessage(), e);
                    }
                }
                log.info("Sales order data generation complete.");
            }

            // Legacy Opportunity and Rating code
            if (!bCustomerIds.isEmpty()) {
                int numOpportunities = bCustomerIds.size();
                List<OpportunityRequest> fakeOpportunityRequests = opportunityFaker.generateFakeOpportunityRequests(numOpportunities, bCustomerIds, new ArrayList<>());
                List<Long> opportunityIds = new ArrayList<>();
                log.info("Generating and saving {} fake opportunities for B2B customers...", numOpportunities);
                for (OpportunityRequest request : fakeOpportunityRequests) {
                    List<Long> associatedTagIdsForOpportunity = new ArrayList<>();
                    if (!tagIds.isEmpty()) {
                        int tagsToAssociate = random.nextInt(Math.min(4, tagIds.size() + 1));
                        Collections.shuffle(tagIds);
                        for (int i = 0; i < tagsToAssociate; i++) {
                            associatedTagIdsForOpportunity.add(tagIds.get(i));
                        }
                    }
                    request.setTagIds(associatedTagIdsForOpportunity.stream().distinct().collect(Collectors.toList()));
                    try {
                        OpportunityDto createdOpportunity = opportunityService.create(request);
                        opportunityIds.add(createdOpportunity.getOpportunityId());
                        log.info("Successfully created opportunity: {} (ID: {}), associated tag IDs: {}", createdOpportunity.getOpportunityName(), createdOpportunity.getOpportunityId(), request.getTagIds());
                    } catch (EntityNotFoundException e) {
                        log.warn("Failed to create opportunity (associated entity not found): {}", e.getMessage());
                    } catch (Exception e) {
                        log.error("Error creating opportunity: {}", e.getMessage(), e);
                    }
                }
                log.info("Opportunity data generation complete. Created opportunity IDs: {}", opportunityIds);

                log.info("--- Starting random rating for generated opportunities ---");
                if (opportunityIds.isEmpty()) {
                    log.warn("No opportunity IDs, skipping rating generation.");
                } else {
                    for (Long oppId : opportunityIds) {
                        int numRatings = random.nextInt(5) + 1;
                        for (int i = 0; i < numRatings; i++) {
                            int ratingScore = random.nextInt(3) + 1;
                            try {
                                opportunityService.rateOpportunity(oppId, SYSTEM_USER_ID, ratingScore);
                                log.info("Opportunity ID {} received rating: {}", oppId, ratingScore);
                            } catch (EntityNotFoundException e) {
                                log.warn("Failed to rate opportunity (Opportunity ID {} not found): {}", oppId, e.getMessage());
                            } catch (IllegalArgumentException e) {
                                log.warn("Failed to rate opportunity (invalid rating score): {}", e.getMessage());
                            } catch (Exception e) {
                                log.error("Error rating opportunity (ID: {}): {}", oppId, e.getMessage(), e);
                            }
                        }
                    }
                }
            } else {
                log.warn("No B2B customers, skipping opportunity and rating generation.");
            }
            log.info("--- Full data generation process complete ---");
        };
    }

    private void createSystemUserIfNotExists() {
        if (userRepository.findById(SYSTEM_USER_ID).isEmpty()) {
            log.info("Creating system user with ID: {}", SYSTEM_USER_ID);
            User systemUser = new User();
            systemUser.setUserId(SYSTEM_USER_ID); // Assuming User has setUserId or it's handled by DB
            systemUser.setUserName("system");
            systemUser.setAccount("system");
            systemUser.setEmail("system@example.com");
            String systemPassword = "abc123456";
            String hashedPassword = passwordEncoder.encode(systemPassword);
            systemUser.setPassword(hashedPassword);
            // TODO: Set authorities if needed
            userRepository.save(systemUser);
            log.info("System user created successfully");
        } else {
            log.info("System user already exists");
        }
    }

    private List<Unit> createAndSaveUnits() {
        if (unitRepository.count() > 0) {
            log.info("Units already exist, skipping creation.");
            return unitRepository.findAll();
        }
        log.info("Creating default units...");
        List<Unit> units = new ArrayList<>();
        Arrays.asList("個", "箱", "打", "公克","支","杯","盒").forEach(name -> {
            Unit unit = new Unit();
            unit.setName(name); // Assuming Unit has setName, might be setUnitName
//            unit.setDescription(name); // Simple description
            unit.setCreatedBy(SYSTEM_USER_ID);
            unit.setUpdatedBy(SYSTEM_USER_ID);
//            unit.setCreatedAt(LocalDateTime.now());
//            unit.setUpdatedAt(LocalDateTime.now());
            units.add(unit);
        });
        return unitRepository.saveAll(units);
    }

    private List<ProductCategory> createAndSaveCategories() {
        if (categoryRepository.count() > 0) {
            log.info("Product categories already exist, skipping creation.");
            return categoryRepository.findAll();
        }
        log.info("Creating default product categories...");
        List<ProductCategory> categories = new ArrayList<>();
        Arrays.asList("經典冰淇淋", "水果雪酪","雪糕系列","巧酥雪糕系列", "季節限定", "純素系列", "品牌聯名系列","週邊商品").forEach(name -> {
            ProductCategory category = new ProductCategory();
            category.setName(name); // Assuming ProductCategory has setName, might be setCategoryName
//            category.setDescription(name); // Simple description
            category.setCreatedBy(SYSTEM_USER_ID);
            category.setUpdatedBy(SYSTEM_USER_ID);
            // category.setCreatedAt(LocalDateTime.now()); // If these fields exist and are not auto-set
            // category.setUpdatedAt(LocalDateTime.now());
            categories.add(category);
        });
        return categoryRepository.saveAll(categories);
    }
}