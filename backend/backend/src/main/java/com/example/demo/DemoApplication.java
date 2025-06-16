package com.example.demo;

import com.example.demo.entity.Customer;
import com.example.demo.enums.CustomerIndustry; // 導入客戶行業 Enum
import com.example.demo.enums.CustomerLevel;    // 導入客戶等級 Enum
import com.example.demo.enums.CustomerType;     // 導入客戶類型 Enum
import com.example.demo.repository.CustomerRepository;
import com.github.javafaker.Faker; // 導入 Faker 類別
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile; // 用於控制數據填充只在特定環境運行
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Locale; // 導入 Locale 以指定 Faker 語言
import java.util.Random; // 用於隨機選擇 Enum 值

@EnableJpaAuditing
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	/**
	 * 創建一個 CommandLineRunner Bean，在應用程式啟動後執行。
	 * 這裡用於生成並保存假客戶數據。
	 * 使用 @Profile("dev") 註解，確保這個數據填充器只在 'dev' 環境配置文件被激活時運行。
	 */
	@Bean
	@Profile("dev") // 建議只在開發環境激活此功能，避免污染生產數據
	public CommandLineRunner demoDataSeeder(CustomerRepository customerRepository) {
		return args -> {
			// 初始化 Faker，可以指定語言環境，讓數據更具地域性
			Faker faker = new Faker(new Locale("en-US")); // 例如：new Faker(new Locale("zh-TW"))

			// 獲取所有 Enum 值的陣列，以便隨機選擇
			CustomerIndustry[] industries = CustomerIndustry.values();
			CustomerType[] customerTypes = CustomerType.values();
			CustomerLevel[] customerLevels = CustomerLevel.values();
			Random random = new Random();

			System.out.println("開始生成假客戶數據...");

			// 檢查資料庫中是否已有數據，避免重複生成
			// 這個檢查確保只有在客戶表為空時才執行數據生成
			if (customerRepository.count() == 0) { // 使用 count() 方法判斷表是否為空
				for (int i = 0; i < 50; i++) { // 生成 50 個假客戶
					Customer customer = new Customer();
					customer.setCustomerName(faker.company().name()); // 公司名稱
					customer.setCustomerEmail(faker.internet().emailAddress()); // 電子郵件
					customer.setCustomerTel(faker.phoneNumber().phoneNumber()); // 電話號碼
					customer.setCustomerAddress(faker.address().fullAddress()); // 完整地址

					// 隨機選擇 Enum 值
					customer.setIndustry(industries[random.nextInt(industries.length)]);
					customer.setCustomerType(customerTypes[random.nextInt(customerTypes.length)]);
					customer.setCustomerLevel(customerLevels[random.nextInt(customerLevels.length)]);

					customerRepository.save(customer); // 保存到資料庫
					System.out.println("生成客戶: " + customer.getCustomerName());
				}
			} else {
				System.out.println("資料庫中已有客戶數據，跳過數據生成。");
			}

			System.out.println("假客戶數據生成完成！");
		};
	}
}

