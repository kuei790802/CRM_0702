package com.example.demo;

import com.example.demo.entity.Contact;
import com.example.demo.entity.Customer;
import com.example.demo.enums.CustomerIndustry;
import com.example.demo.enums.CustomerLevel;
import com.example.demo.enums.CustomerType;
import com.example.demo.repository.ContactRepository;
import com.example.demo.repository.CustomerRepository;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Locale;
import java.util.Random;

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
	public CommandLineRunner demoDataSeeder(CustomerRepository customerRepository,
											ContactRepository contactRepository) {
		return args -> {
			// 初始化 Faker，可以指定語言環境，讓數據更具地域性
			Faker faker = new Faker(new Locale("zh-TW")); // 例如：new Faker(new Locale("zh-TW"))

			// 獲取所有 Enum 值的陣列，以便隨機選擇
			CustomerIndustry[] industries = CustomerIndustry.values();
			CustomerType[] customerTypes = CustomerType.values();
			CustomerLevel[] customerLevels = CustomerLevel.values();
			Random random = new Random();

			System.out.println("開始生成假客戶和聯絡人數據...");

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
					// 為每個客戶生成 1-3 個聯絡人
					int numberOfContacts = random.nextInt(3) + 1; // 1 到 3 個聯絡人
					for (int j = 0; j < numberOfContacts; j++) {
						Contact contact = new Contact();
						contact.setContactName(faker.name().fullName());
						contact.setContactTitle(faker.job().title());
						contact.setEmail(faker.internet().emailAddress());
						contact.setContactPhone(faker.phoneNumber().phoneNumber());
						contact.setContactNotes(faker.lorem().sentence());

						// 將聯絡人關聯到當前客戶
						// 使用 Customer 實體中的 addContact 輔助方法來維護雙向關聯
						customer.addContact(contact); // 這會自動設定 contact.setCustomer(customer);

						// 因為 Customer 實體的 contacts 集合上設置了 cascade = CascadeType.ALL，
						// 當 customer 被保存時，其關聯的 contacts 也會自動被持久化。
						// 所以這裡不需要單獨的 contactRepository.save(contact);
						// 但為了明確，如果您不確定級聯設置，可以加上。
						// contactRepository.save(contact); // 如果沒有級聯設定，則需要這行
						System.out.println("  > 生成聯絡人: " + contact.getContactName() + " (屬於客戶: " + customer.getCustomerName() + ")");
					}
					// 再次保存客戶，以確保所有級聯操作被觸發（如果 contacts 集合被修改）。
					// 在此情境下，由於 addContact() 已經修改了 contact 的 customer 屬性，
					// 並且 Customer 對 Contact 是 @OneToMany(cascade=ALL)，
					// 重新保存 customer 會確保聯絡人被持久化。
					customerRepository.save(customer); // 保存客戶和所有關聯的聯絡人
					System.out.println("生成客戶: " + customer.getCustomerName() + " 完成。");
				}
			} else {
				System.out.println("資料庫中已有客戶數據，跳過數據生成。");
			}

			System.out.println("假客戶和聯絡人數據生成完成！");
		};
	}
}

