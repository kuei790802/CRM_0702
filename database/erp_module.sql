-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost:8889
-- Generation Time: Jun 08, 2025 at 10:28 AM
-- Server version: 8.0.40
-- PHP Version: 8.3.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ERP_Model`
--

-- --------------------------------------------------------

--
-- Table structure for table `ContactPerson`
--

CREATE TABLE `ContactPerson` (
  `contact_person_id` bigint NOT NULL COMMENT '聯絡人唯一識別碼',
  `customer_id` bigint NOT NULL COMMENT '關聯到 Customer 表的 ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '聯絡人姓名',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '職稱',
  `phone_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '電話號碼',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '電子郵件',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最後更新時間'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客戶聯絡人資訊表';

-- --------------------------------------------------------

--
-- Table structure for table `Customer`
--

CREATE TABLE `Customer` (
  `customer_id` bigint NOT NULL COMMENT '客戶唯一識別碼',
  `customer_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客戶代號',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客戶名稱',
  `contact_person_id` bigint DEFAULT NULL COMMENT '關聯到 ContactPerson 表的 ID',
  `phone_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '電話號碼',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '電子郵件',
  `address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '客戶地址',
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '客戶是否啟用',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最後更新時間'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客戶基本資料表';

-- --------------------------------------------------------

--
-- Table structure for table `Inventory`
--

CREATE TABLE `Inventory` (
  `inventory_id` bigint NOT NULL COMMENT '庫存記錄唯一識別碼',
  `product_id` bigint NOT NULL COMMENT '關聯到 Product 表',
  `warehouse_id` bigint NOT NULL COMMENT '關聯到 Warehouse 表',
  `current_stock` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '當前庫存數量',
  `average_cost` decimal(18,4) NOT NULL DEFAULT '0.0000' COMMENT '當前平均成本',
  `last_updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最後更新時間',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
  `created_by` bigint NOT NULL COMMENT '建立人',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最後更新時間',
  `updated_by` bigint NOT NULL COMMENT '最後更新人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='庫存主表 (實時庫存)';

-- --------------------------------------------------------

--
-- Table structure for table `InventoryAdjustment`
--

CREATE TABLE `InventoryAdjustment` (
  `adjustment_id` bigint NOT NULL COMMENT '庫存調整單唯一識別碼',
  `adjustment_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '調整單號',
  `adjustment_date` date NOT NULL COMMENT '調整日期',
  `adjustment_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '調整類型 (例如：GAIN, LOSS, SCRAP, CONSUMPTION)',
  `warehouse_id` bigint NOT NULL COMMENT '調整所屬的倉庫 ID',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'DRAFT' COMMENT '調整單狀態 (例如：DRAFT, PENDING_APPROVAL, APPROVED, EXECUTED, CANCELLED)',
  `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '備註',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
  `created_by` bigint NOT NULL COMMENT '建立人',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最後更新時間',
  `updated_by` bigint NOT NULL COMMENT '最後更新人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='庫存調整單主表';

-- --------------------------------------------------------

--
-- Table structure for table `InventoryAdjustmentDetail`
--

CREATE TABLE `InventoryAdjustmentDetail` (
  `item_id` bigint NOT NULL COMMENT '明細項目唯一識別碼',
  `adjustment_id` bigint NOT NULL COMMENT '關聯到 InventoryAdjustment 表',
  `item_sequence` int NOT NULL COMMENT '明細項目的序號',
  `product_id` bigint NOT NULL COMMENT '關聯到 Product 表',
  `specification` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品規格 (冗餘自 Product 表)',
  `unit_id` bigint NOT NULL COMMENT '關聯到 Unit 表',
  `adjusted_quantity` decimal(18,2) NOT NULL COMMENT '調整數量 (正數為增加，負數為減少)',
  `unit_cost_at_adjustment` decimal(18,4) NOT NULL COMMENT '調整時該商品的單位成本',
  `total_cost_change` decimal(18,4) NOT NULL COMMENT '調整時的總成本變化',
  `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '明細備註',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
  `created_by` bigint NOT NULL COMMENT '建立人',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最後更新時間',
  `updated_by` bigint NOT NULL COMMENT '最後更新人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='庫存調整單明細表';

-- --------------------------------------------------------

--
-- Table structure for table `InventoryMovement`
--

CREATE TABLE `InventoryMovement` (
  `movement_id` bigint NOT NULL COMMENT '異動記錄唯一識別碼',
  `product_id` bigint NOT NULL COMMENT '關聯到 Product 表',
  `warehouse_id` bigint NOT NULL COMMENT '關聯到 Warehouse 表',
  `movement_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '異動發生日期和時間',
  `movement_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '異動類型 (例如：IN_PURCHASE, OUT_SALE, ADJUSTMENT_IN, ADJUSTMENT_OUT, RETURN_IN, RETURN_OUT, TRANSFER_IN, TRANSFER_OUT)',
  `document_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '關聯單據類型 (如 PurchaseOrder, SalesOrder, InventoryAdjustment, SalesReturn, PurchaseReturn)',
  `document_id` bigint DEFAULT NULL COMMENT '關聯單據的主鍵 ID',
  `document_item_id` bigint DEFAULT NULL COMMENT '關聯單據明細的主鍵 ID',
  `quantity_change` decimal(18,2) NOT NULL COMMENT '數量異動 (正數為增加，負數為減少)',
  `unit_cost_at_movement` decimal(18,4) NOT NULL COMMENT '異動發生時的單位成本',
  `total_cost_change` decimal(18,4) NOT NULL COMMENT '異動時的總成本變化',
  `current_stock_after_movement` decimal(18,2) NOT NULL COMMENT '異動後的庫存數量',
  `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '異動備註',
  `recorded_by` bigint NOT NULL COMMENT '記錄人',
  `recorded_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '記錄時間'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='庫存異動明細表';

-- --------------------------------------------------------

--
-- Table structure for table `product`
--

CREATE TABLE `product` (
  `product_id` bigint NOT NULL COMMENT '商品唯一識別碼',
  `product_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品代號',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品名稱',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '商品描述',
  `category_id` bigint DEFAULT NULL COMMENT '商品分類 ID',
  `unit_id` bigint NOT NULL COMMENT '商品單位 ID',
  `is_purchasable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否採購品',
  `is_salable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否銷售品',
  `cost_method` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `base_price` decimal(18,2) NOT NULL COMMENT '基本售價',
  `tax_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '稅別 (例如：TAXABLE, EXEMPT)',
  `safety_stock_quantity` int NOT NULL DEFAULT '0' COMMENT '安全庫存量',
  `min_order_quantity` int NOT NULL DEFAULT '1' COMMENT '最小訂購量',
  `max_order_quantity` int NOT NULL DEFAULT '9999' COMMENT '最大訂購量',
  `lead_time_days` int DEFAULT NULL COMMENT '前置天數 (採購到貨所需天數)',
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '商品是否啟用',
  `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '備註',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
  `created_by` bigint NOT NULL COMMENT '建立人',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最後更新時間',
  `updated_by` bigint NOT NULL COMMENT '最後更新人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品主資料表';

--
-- Dumping data for table `product`
--

INSERT INTO `product` (`product_id`, `product_code`, `name`, `description`, `category_id`, `unit_id`, `is_purchasable`, `is_salable`, `cost_method`, `base_price`, `tax_type`, `safety_stock_quantity`, `min_order_quantity`, `max_order_quantity`, `lead_time_days`, `is_active`, `remarks`, `created_at`, `created_by`, `updated_at`, `updated_by`) VALUES
(11, 'P000001', '鮮果香草雪糕 P000001', 'string', 2, 2, 1, 1, 'AVERAGE', 90.00, 'string', 50, 1, 9999, NULL, 0, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 03:08:01', 1),
(12, 'P000002', '經典抹茶聖代 P000002', 'Quia vero fuga. Nobis exercitationem consequuntur non.', 8, 5, 1, 1, 'AVERAGE', 181.03, 'TAXABLE', 42, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(13, 'P000003', '極致豆乳芝麻冰棒 P000003', 'Laboriosam quo quod est voluptatum alias. Nihil nisi ut. Voluptatem omnis laborum quia.', 9, 2, 1, 1, 'AVERAGE', 46.06, 'TAXABLE', 71, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(14, 'P000004', '純濃薄荷巧克力雪糕 P000004', 'Eius est non. Culpa qui vitae fugit.', 2, 4, 1, 1, 'AVERAGE', 339.28, 'TAXABLE', 38, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(15, 'P000005', '雪藏OREO雪糕 P000005', 'Qui veniam magnam consectetur. Perferendis laborum doloremque. Consequuntur ea voluptatem et. Porro asperiores commodi tempora rem.', 5, 7, 1, 1, 'AVERAGE', 129.96, 'TAXABLE', 53, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(16, 'P000006', '莊園芒果優格雪糕 P000006', 'Dolor numquam commodi eaque voluptatum eligendi mollitia. Voluptas nihil maiores quis qui animi.', 7, 6, 1, 1, 'AVERAGE', 47.12, 'TAXABLE', 29, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(17, 'P000007', '經典豆乳芝麻聖代 P000007', 'Occaecati et ducimus voluptatem qui cum. Nesciunt in aut.', 5, 8, 1, 1, 'AVERAGE', 185.32, 'TAXABLE', 69, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(18, 'P000008', '莊園豆乳芝麻冰淇淋 P000008', 'Inventore ipsam maxime sint culpa. Pariatur quisquam quo ut omnis non optio. Ab quas velit est ipsa laborum expedita nisi. Sed accusantium quaerat eligendi voluptate autem consequatur consequuntur.', 9, 7, 1, 1, 'AVERAGE', 297.79, 'TAXABLE', 12, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(19, 'P000009', '純濃蘭姆葡萄雪酪 P000009', 'Eligendi culpa sint mollitia perferendis ea. Accusantium id natus sit quibusdam architecto et incidunt.', 5, 8, 1, 1, 'AVERAGE', 234.27, 'TAXABLE', 26, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(20, 'P000010', '純濃OREO聖代 P000010', 'Quasi exercitationem rerum. Suscipit incidunt dolor sint alias voluptatem dignissimos distinctio.', 5, 7, 1, 1, 'AVERAGE', 240.34, 'TAXABLE', 76, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(21, 'P000011', '鮮果香草冰棒 P000011', 'Iusto sed recusandae sed. Dignissimos in rem voluptate dolorum est et.', 5, 6, 1, 1, 'AVERAGE', 237.12, 'TAXABLE', 14, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(22, 'P000012', '雪藏藍莓雪糕 P000012', 'Explicabo sit voluptate. Reprehenderit enim blanditiis. Quia magnam aliquam.', 3, 4, 1, 1, 'AVERAGE', 134.25, 'TAXABLE', 80, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(23, 'P000013', '雪藏咖啡雪酪 P000013', 'Provident velit quos non. Id architecto autem. Excepturi ut molestias et reprehenderit.', 2, 8, 1, 1, 'AVERAGE', 300.55, 'TAXABLE', 82, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(24, 'P000014', '極致巧克力冰淇淋 P000014', 'Molestias vel ducimus minima. Iste odio ut. Facilis magnam illo dolores ut optio dignissimos accusamus.', 8, 6, 1, 1, 'AVERAGE', 92.64, 'TAXABLE', 87, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(25, 'P000015', '極致海鹽焦糖冰淇淋 P000015', 'Fugit nemo nostrum mollitia commodi neque et. Omnis quia numquam facere architecto.', 8, 4, 1, 1, 'AVERAGE', 143.32, 'TAXABLE', 98, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(26, 'P000016', '極致花生冰淇淋 P000016', 'Hic sed ut deleniti ad voluptatibus nam. Nihil id quaerat. Neque delectus amet consequatur aut debitis.', 3, 8, 1, 1, 'AVERAGE', 149.66, 'TAXABLE', 18, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(27, 'P000017', '經典抹茶冰棒 P000017', 'Cumque aut excepturi. Eum molestiae et reprehenderit voluptatum veniam.', 4, 7, 1, 1, 'AVERAGE', 342.99, 'TAXABLE', 35, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(28, 'P000018', '經典薄荷巧克力雪酪 P000018', 'Qui saepe delectus repellat excepturi ullam est. Quia iusto impedit optio commodi. Ea veritatis cupiditate saepe soluta.', 2, 4, 1, 1, 'AVERAGE', 157.04, 'TAXABLE', 22, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(29, 'P000019', '經典芒果優格冰棒 P000019', 'Velit voluptate quam eos tempore ducimus. Non omnis consequuntur corporis sed natus ut sunt. Ab libero sunt.', 9, 3, 1, 1, 'AVERAGE', 237.70, 'TAXABLE', 90, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(30, 'P000020', '鮮果豆乳芝麻冰淇淋 P000020', 'Explicabo corporis rem. Ut velit quo.', 4, 3, 1, 1, 'AVERAGE', 195.63, 'TAXABLE', 39, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(31, 'P000021', '極致蘭姆葡萄聖代 P000021', 'Est quidem assumenda. Iusto sed dicta eum sit et. In blanditiis tenetur.', 8, 4, 1, 1, 'AVERAGE', 136.75, 'TAXABLE', 48, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(32, 'P000022', '莊園咖啡雪糕 P000022', 'Consequuntur voluptatem dolorem nesciunt laborum corrupti. Deleniti at aut omnis dolorem qui minima.', 4, 6, 1, 1, 'AVERAGE', 92.02, 'TAXABLE', 79, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(33, 'P000023', '莊園薄荷巧克力雪酪 P000023', 'Nemo quo et enim voluptas. Sit labore et ab distinctio et.', 7, 7, 1, 1, 'AVERAGE', 62.13, 'TAXABLE', 30, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(34, 'P000024', '極致抹茶雪糕 P000024', 'Sit architecto ut optio quaerat unde adipisci. Dolore provident modi porro a.', 7, 2, 1, 1, 'AVERAGE', 230.73, 'TAXABLE', 53, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(35, 'P000025', '夏日蘭姆葡萄聖代 P000025', 'Modi officia nisi ut perferendis. A id animi optio.', 4, 4, 1, 1, 'AVERAGE', 231.93, 'TAXABLE', 35, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(36, 'P000026', '經典咖啡冰淇淋 P000026', 'Minus eos iusto totam dolore asperiores provident. Adipisci et deleniti commodi reprehenderit sequi error.', 8, 6, 1, 1, 'AVERAGE', 189.38, 'TAXABLE', 84, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(37, 'P000027', '純濃OREO雪糕 P000027', 'Quaerat possimus beatae sed adipisci. Nulla est maiores saepe incidunt perspiciatis expedita. Aliquam fugit quo et consequatur. Mollitia nam ducimus at.', 9, 5, 1, 1, 'AVERAGE', 231.91, 'TAXABLE', 39, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(38, 'P000028', '經典蘭姆葡萄聖代 P000028', 'Voluptate dolores optio. Blanditiis unde eos sint ex maxime quia.', 6, 6, 1, 1, 'AVERAGE', 184.21, 'TAXABLE', 75, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(39, 'P000029', '極致豆乳芝麻雪糕 P000029', 'Ut nesciunt soluta. Quia quibusdam iusto est ipsum.', 2, 2, 1, 1, 'AVERAGE', 125.26, 'TAXABLE', 73, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(40, 'P000030', '極致香蕉冰棒 P000030', 'Quibusdam odit tenetur molestiae architecto voluptas. Cumque dicta rerum cumque. Corrupti modi non aut aut.', 2, 7, 1, 1, 'AVERAGE', 170.10, 'TAXABLE', 87, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(41, 'P000031', '鮮果花生雪糕 P000031', 'Est quae aut assumenda. Deserunt omnis hic at. Accusantium sunt exercitationem reprehenderit omnis ut.', 4, 5, 1, 1, 'AVERAGE', 164.42, 'TAXABLE', 61, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(42, 'P000032', '鮮果豆乳芝麻雪酪 P000032', 'Et doloremque at voluptas sit est temporibus similique. Adipisci tenetur eum sit ut neque minus voluptas. Magni provident nihil laudantium exercitationem non omnis.', 8, 7, 1, 1, 'AVERAGE', 171.15, 'TAXABLE', 21, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(43, 'P000033', '極致蘭姆葡萄聖代 P000033', 'Quod et quo maxime blanditiis molestiae iusto omnis. Enim fugiat sequi.', 6, 3, 1, 1, 'AVERAGE', 160.71, 'TAXABLE', 19, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(44, 'P000034', '雪藏草莓冰棒 P000034', 'Deleniti non fuga quis id ut repellendus eligendi. Hic minus dolorem asperiores.', 2, 7, 1, 1, 'AVERAGE', 57.32, 'TAXABLE', 32, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(45, 'P000035', '雪藏燕麥奶雪酪 P000035', 'Velit error voluptatem rerum. Et modi ut quos.', 6, 7, 1, 1, 'AVERAGE', 315.85, 'TAXABLE', 47, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(46, 'P000036', '夏日藍莓聖代 P000036', 'Tenetur assumenda nisi quis odio quaerat. Ea consequuntur et aut quibusdam. Perspiciatis quisquam consequuntur error culpa.', 8, 8, 1, 1, 'AVERAGE', 268.79, 'TAXABLE', 25, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(47, 'P000037', '莊園藍莓冰淇淋 P000037', 'Molestiae earum expedita qui. Perspiciatis in modi.', 5, 3, 1, 1, 'AVERAGE', 288.35, 'TAXABLE', 65, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(48, 'P000038', '鮮果草莓雪糕 P000038', 'Quaerat nihil non qui velit necessitatibus voluptates numquam. Id corrupti natus suscipit. Quia nam velit quas nihil autem delectus. Et laborum beatae consectetur aliquam porro molestiae.', 3, 5, 1, 1, 'AVERAGE', 220.01, 'TAXABLE', 73, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(49, 'P000039', '鮮果燕麥奶雪酪 P000039', 'Soluta velit ut rem soluta perspiciatis. Voluptatem ut aut consectetur aliquam ea iste ut.', 4, 7, 1, 1, 'AVERAGE', 233.96, 'TAXABLE', 14, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(50, 'P000040', '夏日豆乳芝麻雪酪 P000040', 'Blanditiis qui aliquam est illo. Praesentium ipsa eum consequuntur est quis.', 9, 7, 1, 1, 'AVERAGE', 303.00, 'TAXABLE', 89, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(51, 'P000041', '莊園薄荷巧克力聖代 P000041', 'Sunt voluptates placeat. Ea est eveniet hic. Voluptates sed autem aliquam aut sed expedita. Amet rerum et qui omnis earum.', 6, 2, 1, 1, 'AVERAGE', 338.93, 'TAXABLE', 82, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(52, 'P000042', '鮮果OREO冰棒 P000042', 'Praesentium aliquam aut quaerat. Sint molestiae error nemo earum esse tenetur nemo. Ut saepe ut iusto sed ipsam.', 4, 6, 1, 1, 'AVERAGE', 161.68, 'TAXABLE', 76, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(53, 'P000043', '夏日巧克力雪酪 P000043', 'Nisi ut incidunt maiores. Cum ut officia est vero nihil. Nulla aspernatur optio est totam incidunt et repellat. Voluptas iure id in.', 9, 6, 1, 1, 'AVERAGE', 79.16, 'TAXABLE', 41, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(54, 'P000044', '夏日草莓冰淇淋 P000044', 'Autem corporis cumque et incidunt asperiores commodi. Placeat quo ea fugiat omnis sapiente. Maxime atque aut aut non aut debitis.', 8, 6, 1, 1, 'AVERAGE', 198.31, 'TAXABLE', 73, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(55, 'P000045', '純濃芒果優格冰棒 P000045', 'Id commodi quia rerum. Doloremque ea veritatis et aut.', 6, 4, 1, 1, 'AVERAGE', 127.04, 'TAXABLE', 98, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(56, 'P000046', '經典香蕉冰棒 P000046', 'Nemo consequatur minus similique velit laudantium. Vel ea totam aperiam rem et consequatur. Error consequatur sit eum.', 3, 4, 1, 1, 'AVERAGE', 123.79, 'TAXABLE', 77, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(57, 'P000047', '雪藏芒果優格冰淇淋 P000047', 'Aut esse quasi dolorem numquam. Quaerat aliquid et vitae quia corrupti placeat quia. Molestiae molestiae ad esse incidunt reprehenderit et. Ad dolor ducimus voluptatum provident et culpa suscipit.', 8, 3, 1, 1, 'AVERAGE', 236.54, 'TAXABLE', 98, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(58, 'P000048', '純濃OREO雪糕 P000048', 'Autem modi quis unde. Veniam delectus neque aut aspernatur quas ut.', 7, 7, 1, 1, 'AVERAGE', 325.69, 'TAXABLE', 57, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(59, 'P000049', '鮮果香草雪酪 P000049', 'Nisi facere consectetur non aperiam quia sint. Veritatis hic est esse provident.', 2, 5, 1, 1, 'AVERAGE', 218.56, 'TAXABLE', 20, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(60, 'P000050', '夏日豆乳芝麻冰淇淋 P000050', 'Iste magni ab. Officia accusantium ea vero reiciendis. Porro voluptatum molestiae iure voluptas consectetur eligendi repudiandae.', 5, 7, 1, 1, 'AVERAGE', 152.02, 'TAXABLE', 59, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(61, 'P000051', '極致草莓雪糕 P000051', 'Enim distinctio tempore repellat. Unde consequatur magni est dolor natus. Dolor nisi beatae eos quia. Nihil eveniet reiciendis ipsum et dolore.', 6, 8, 1, 1, 'AVERAGE', 238.95, 'TAXABLE', 78, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(62, 'P000052', '經典芒果優格冰淇淋 P000052', 'Dicta dolorem est necessitatibus officia fugiat. Aut porro voluptatem tenetur et. Sed dolorem exercitationem assumenda qui blanditiis. Impedit earum in porro ea quia dolores.', 9, 3, 1, 1, 'AVERAGE', 318.26, 'TAXABLE', 94, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(63, 'P000053', '經典薄荷巧克力雪糕 P000053', 'Optio doloremque sapiente aliquam. Perspiciatis quia neque vero natus ipsam eveniet. Consequuntur aut in.', 8, 3, 1, 1, 'AVERAGE', 330.31, 'TAXABLE', 13, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(64, 'P000054', '極致香蕉冰棒 P000054', 'Tempora nam quibusdam et consequatur. Pariatur et aspernatur qui.', 5, 8, 1, 1, 'AVERAGE', 342.22, 'TAXABLE', 49, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(65, 'P000055', '莊園OREO冰棒 P000055', 'Nihil eum adipisci magnam odit. Porro autem aut voluptatibus et consequatur accusamus. Provident neque tempore quas dolorem mollitia pariatur.', 6, 4, 1, 1, 'AVERAGE', 161.37, 'TAXABLE', 93, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(66, 'P000056', '莊園巧克力雪糕 P000056', 'Soluta iure blanditiis voluptas aut iure eum molestiae. Eum sed et labore facilis et. Alias aut ipsam alias.', 8, 7, 1, 1, 'AVERAGE', 277.93, 'TAXABLE', 45, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(67, 'P000057', '極致海鹽焦糖聖代 P000057', 'Et a libero deserunt. Tempore quis reprehenderit consequuntur assumenda quibusdam voluptatem.', 2, 8, 1, 1, 'AVERAGE', 97.81, 'TAXABLE', 12, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(68, 'P000058', '雪藏花生雪酪 P000058', 'Maxime quasi qui ut numquam mollitia repellat. Accusantium voluptas modi reiciendis ut.', 2, 3, 1, 1, 'AVERAGE', 299.33, 'TAXABLE', 53, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(69, 'P000059', '極致燕麥奶雪酪 P000059', 'Dolores tempora esse illum laborum at et sapiente. Cupiditate est ea provident sapiente. Voluptate quia ea qui cupiditate qui eligendi eius.', 2, 8, 1, 1, 'AVERAGE', 187.91, 'TAXABLE', 82, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(70, 'P000060', '莊園薄荷巧克力雪酪 P000060', 'Et quasi error. Assumenda aut adipisci mollitia ut voluptatem. Voluptatem omnis quae amet sed quis.', 4, 6, 1, 1, 'AVERAGE', 181.51, 'TAXABLE', 48, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(71, 'P000061', '純濃香蕉冰淇淋 P000061', 'Consequuntur nobis qui. Asperiores vel omnis aspernatur laudantium eos dolor. Possimus ut sequi in numquam ut tenetur ab. Consequatur voluptatibus omnis qui repellendus.', 9, 2, 1, 1, 'AVERAGE', 99.25, 'TAXABLE', 41, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(72, 'P000062', '鮮果香草雪糕 P000062', 'In corrupti eveniet eum sit et. Vero rerum voluptate suscipit placeat saepe accusantium.', 6, 2, 1, 1, 'AVERAGE', 298.29, 'TAXABLE', 70, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(73, 'P000063', '鮮果咖啡雪酪 P000063', 'Perspiciatis voluptatem et officiis molestias a sit. Adipisci mollitia ipsa illum sit libero perspiciatis molestiae.', 7, 2, 1, 1, 'AVERAGE', 247.97, 'TAXABLE', 45, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(74, 'P000064', '極致花生冰棒 P000064', 'Magnam facilis et. Ex reprehenderit aperiam rem veritatis et. Enim et dolore nostrum et magnam rem voluptatem. Dolores dolorem incidunt reprehenderit rerum nesciunt aspernatur.', 2, 5, 1, 1, 'AVERAGE', 326.40, 'TAXABLE', 87, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(75, 'P000065', '雪藏藍莓聖代 P000065', 'Quo optio dolorum. Repellendus nisi dolor placeat qui. Error et tempora non omnis molestiae. Nihil incidunt qui non.', 7, 8, 1, 1, 'AVERAGE', 341.91, 'TAXABLE', 36, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(76, 'P000066', '極致藍莓冰棒 P000066', 'Sint non doloremque. Dolor architecto ut recusandae quae molestiae qui. Officia porro atque qui hic vero.', 7, 4, 1, 1, 'AVERAGE', 292.26, 'TAXABLE', 76, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(77, 'P000067', '極致抹茶冰淇淋 P000067', 'Iste officiis pariatur. Minus omnis atque est odio ab cumque voluptatibus. Eius modi voluptatum.', 7, 2, 1, 1, 'AVERAGE', 126.34, 'TAXABLE', 29, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(78, 'P000068', '經典海鹽焦糖聖代 P000068', 'Accusamus ratione sapiente quia cupiditate. Repellendus alias animi rerum quibusdam sed.', 5, 3, 1, 1, 'AVERAGE', 208.60, 'TAXABLE', 73, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(79, 'P000069', '雪藏抹茶冰棒 P000069', 'Corrupti sunt natus aut. Quas eum sapiente enim est.', 3, 6, 1, 1, 'AVERAGE', 273.40, 'TAXABLE', 22, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(80, 'P000070', '鮮果巧克力冰淇淋 P000070', 'Voluptatem est veritatis ut voluptatum doloremque. Quaerat odio voluptatibus ut. Rerum ipsa non beatae.', 3, 8, 1, 1, 'AVERAGE', 147.52, 'TAXABLE', 78, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(81, 'P000071', '純濃巧克力雪酪 P000071', 'Aut repudiandae ut repudiandae omnis eum. Reprehenderit illo corporis aliquam blanditiis magni quia et. Repellendus provident et dolore dolore. Laboriosam sequi dolore.', 8, 8, 1, 1, 'AVERAGE', 342.89, 'TAXABLE', 21, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(82, 'P000072', '極致燕麥奶雪酪 P000072', 'Tempora eligendi voluptatem aperiam sunt ut soluta. Quia corrupti error fuga. Et tempore quidem pariatur qui provident. Inventore sit eius quia et dolorem velit.', 5, 2, 1, 1, 'AVERAGE', 211.93, 'TAXABLE', 21, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(83, 'P000073', '極致香草聖代 P000073', 'Totam et et expedita culpa odio. Rem porro ipsam odio.', 6, 5, 1, 1, 'AVERAGE', 162.42, 'TAXABLE', 57, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(84, 'P000074', '雪藏芒果優格冰棒 P000074', 'Quod dolorem id incidunt mollitia. Omnis enim excepturi non nihil sunt. Enim rem eum id velit animi fugit. Laudantium mollitia voluptatem.', 3, 2, 1, 1, 'AVERAGE', 50.69, 'TAXABLE', 92, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(85, 'P000075', '純濃香草冰棒 P000075', 'Repellendus qui tenetur nulla earum. Ipsam ad veritatis earum consequatur assumenda dolorem culpa.', 2, 5, 1, 1, 'AVERAGE', 260.95, 'TAXABLE', 86, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(86, 'P000076', '極致香蕉冰淇淋 P000076', 'Et reiciendis ea id et saepe saepe. Et nihil amet rerum temporibus ullam.', 7, 2, 1, 1, 'AVERAGE', 230.57, 'TAXABLE', 25, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(87, 'P000077', '純濃草莓雪酪 P000077', 'Et voluptatem nostrum voluptate odio veritatis. Eum et quo necessitatibus.', 6, 5, 1, 1, 'AVERAGE', 338.42, 'TAXABLE', 76, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(88, 'P000078', '雪藏芒果優格冰淇淋 P000078', 'Eum saepe sed fugiat debitis. Sunt dolor voluptates veritatis aut quo aut ratione. Provident aspernatur dolores rerum rerum est quasi ullam.', 5, 2, 1, 1, 'AVERAGE', 120.14, 'TAXABLE', 65, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(89, 'P000079', '純濃抹茶冰淇淋 P000079', 'Sit molestiae voluptatem aperiam. Porro voluptas accusantium unde a neque modi voluptates. Et unde eligendi quia.', 7, 8, 1, 1, 'AVERAGE', 162.91, 'TAXABLE', 79, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(90, 'P000080', '莊園花生冰棒 P000080', 'Excepturi qui mollitia voluptas et dicta et sint. Eum aliquam dolorem.', 5, 8, 1, 1, 'AVERAGE', 103.28, 'TAXABLE', 23, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(91, 'P000081', '純濃薄荷巧克力冰淇淋 P000081', 'Voluptate ab eaque sed voluptas dolor vel. Aut at repellat dolor autem eos quisquam. Omnis qui vero reprehenderit. Et quae necessitatibus aut ab.', 4, 7, 1, 1, 'AVERAGE', 81.66, 'TAXABLE', 91, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(92, 'P000082', '莊園抹茶冰淇淋 P000082', 'Eaque repudiandae et. Deserunt nostrum ullam nesciunt. Possimus ullam quia voluptas hic et rerum ea. Omnis repellat distinctio laudantium.', 3, 6, 1, 1, 'AVERAGE', 155.41, 'TAXABLE', 71, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(93, 'P000083', '夏日蘭姆葡萄聖代 P000083', 'Harum non inventore ea consequatur. Magnam occaecati mollitia corporis rem quibusdam quia. Voluptas accusantium impedit et nobis vitae consectetur.', 3, 7, 1, 1, 'AVERAGE', 286.23, 'TAXABLE', 49, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(94, 'P000084', '極致巧克力聖代 P000084', 'Unde ipsa voluptas quod minima autem. Vel perferendis est qui. Dicta autem minima id temporibus temporibus excepturi autem.', 7, 6, 1, 1, 'AVERAGE', 168.03, 'TAXABLE', 18, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(95, 'P000085', '純濃芒果優格雪酪 P000085', 'Quis nulla sed qui consectetur laboriosam. Quaerat ullam et omnis. Quia et iusto voluptas voluptatem maxime nulla.', 2, 5, 1, 1, 'AVERAGE', 107.22, 'TAXABLE', 47, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(96, 'P000086', '莊園燕麥奶冰淇淋 P000086', 'A eligendi est magnam nobis earum. Repudiandae praesentium expedita ut deleniti repellendus expedita. Odit excepturi ut. Ex quaerat ut ducimus suscipit quo.', 7, 6, 1, 1, 'AVERAGE', 105.51, 'TAXABLE', 58, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(97, 'P000087', '雪藏海鹽焦糖冰棒 P000087', 'Nobis accusantium voluptas ipsum alias ratione debitis rerum. Rem consequatur sed consequuntur repellendus dolore. Aut ullam tenetur autem facilis at. Consequatur voluptas ullam dignissimos possimus ullam.', 9, 7, 1, 1, 'AVERAGE', 64.39, 'TAXABLE', 31, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(98, 'P000088', '極致薄荷巧克力冰棒 P000088', 'Neque sapiente assumenda qui asperiores dolor. Officia eveniet quia et nulla. Voluptas aspernatur neque.', 7, 4, 1, 1, 'AVERAGE', 96.47, 'TAXABLE', 14, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(99, 'P000089', '夏日芒果優格聖代 P000089', 'Rerum eum necessitatibus eum pariatur rem. Sequi consectetur eum ut distinctio. Doloremque vel officia non repellendus enim. Omnis vero hic corporis fugit rem dolorum odit.', 4, 2, 1, 1, 'AVERAGE', 209.78, 'TAXABLE', 89, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(100, 'P000090', '莊園香蕉聖代 P000090', 'Optio possimus eum quia ducimus quia. Enim nobis qui deleniti. Quia eaque sed sit corrupti nihil dolor.', 5, 3, 1, 1, 'AVERAGE', 221.55, 'TAXABLE', 22, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(101, 'P000091', '極致豆乳芝麻冰棒 P000091', 'Quis aut exercitationem exercitationem facilis ut consectetur. Id ipsam quo eius. Eum et rem ratione et qui. Accusantium quia rerum deleniti quidem maxime.', 7, 5, 1, 1, 'AVERAGE', 342.77, 'TAXABLE', 42, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(102, 'P000092', '極致海鹽焦糖雪酪 P000092', 'Maiores doloremque iure. Consequuntur cumque delectus.', 9, 3, 1, 1, 'AVERAGE', 161.16, 'TAXABLE', 31, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(103, 'P000093', '純濃香蕉雪糕 P000093', 'Laborum voluptas sapiente accusamus non voluptatem. Eos et voluptatem. Enim beatae voluptatem enim beatae. Ea magnam repellat debitis tempore quia necessitatibus laborum.', 7, 4, 1, 1, 'AVERAGE', 346.37, 'TAXABLE', 75, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(104, 'P000094', '經典巧克力雪酪 P000094', 'Sapiente dolor harum quod incidunt sit. Id provident et at laboriosam enim fuga tempore.', 7, 8, 1, 1, 'AVERAGE', 337.26, 'TAXABLE', 20, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(105, 'P000095', '夏日燕麥奶冰棒 P000095', 'Repellat quam ipsa voluptatem velit aut. Necessitatibus ut ad aliquid. Ducimus sed ullam. Quae aut veniam rerum enim sed aut fuga.', 3, 5, 1, 1, 'AVERAGE', 74.91, 'TAXABLE', 68, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(106, 'P000096', '雪藏草莓冰棒 P000096', 'Incidunt quia aut perspiciatis distinctio. Voluptatibus voluptas velit nisi. Eveniet eligendi a nesciunt voluptatem et sint dolor. In quia inventore unde eaque.', 3, 6, 1, 1, 'AVERAGE', 74.89, 'TAXABLE', 92, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(107, 'P000097', '夏日海鹽焦糖冰棒 P000097', 'Dicta assumenda omnis voluptatibus et. Minima voluptatem impedit occaecati veniam adipisci officia. Inventore dolor eius fuga molestiae et aliquam rerum. Tempora sed ratione similique ut voluptate rerum.', 7, 2, 1, 1, 'AVERAGE', 289.42, 'TAXABLE', 97, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(108, 'P000098', '雪藏芒果優格冰淇淋 P000098', 'Esse inventore aliquam suscipit. Esse iure veritatis aut quae facere at. Fugit id dolore ut. Facilis et quibusdam quo commodi eum dolorem dolores.', 6, 8, 1, 1, 'AVERAGE', 285.40, 'TAXABLE', 89, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(109, 'P000099', '雪藏海鹽焦糖雪酪 P000099', 'Dicta sapiente eos fugit accusantium culpa. Et esse neque quo at.', 3, 5, 1, 1, 'AVERAGE', 218.03, 'TAXABLE', 30, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(110, 'P000100', '純濃海鹽焦糖雪酪 P000100', 'Dolor excepturi ut similique enim ut. Est corporis qui voluptatem beatae.', 4, 4, 1, 1, 'AVERAGE', 189.23, 'TAXABLE', 18, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(111, 'P000101', '雪藏蘭姆葡萄冰淇淋 P000101', 'Illo et aspernatur aut ut. Asperiores et eos dolorem sed id dolorem. Placeat sed aut deleniti et quos.', 2, 7, 1, 1, 'AVERAGE', 215.99, 'TAXABLE', 96, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(112, 'P000102', '莊園燕麥奶雪酪 P000102', 'Eos repellendus voluptas est atque fuga. Repellendus ipsum iusto magni sed fugiat velit id. Deleniti ut qui sed. Sint numquam similique sed ratione in fugiat aliquam.', 3, 4, 1, 1, 'AVERAGE', 66.48, 'TAXABLE', 12, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(113, 'P000103', '莊園香蕉聖代 P000103', 'Consequatur eligendi consequatur. Iure voluptas et mollitia delectus sint.', 5, 5, 1, 1, 'AVERAGE', 251.60, 'TAXABLE', 19, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(114, 'P000104', '純濃薄荷巧克力冰棒 P000104', 'Amet ipsa in ut eum temporibus at. Error quas alias exercitationem quod et.', 6, 3, 1, 1, 'AVERAGE', 57.78, 'TAXABLE', 98, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(115, 'P000105', '鮮果抹茶雪酪 P000105', 'Aut sint atque est perspiciatis incidunt. Qui suscipit consequuntur tenetur ullam quae quo. Incidunt et saepe. Voluptatibus sint non enim ducimus.', 2, 6, 1, 1, 'AVERAGE', 241.43, 'TAXABLE', 63, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(116, 'P000106', '雪藏咖啡雪酪 P000106', 'Reiciendis dicta sit mollitia. Voluptate inventore laborum et est qui. Accusantium modi a sed consequatur. Recusandae labore facilis necessitatibus quo non in.', 6, 3, 1, 1, 'AVERAGE', 70.94, 'TAXABLE', 24, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(117, 'P000107', '純濃咖啡雪糕 P000107', 'Voluptatum et repellat fugiat et dolorem. Amet consequuntur placeat quas sed.', 5, 6, 1, 1, 'AVERAGE', 153.97, 'TAXABLE', 89, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(118, 'P000108', '極致草莓聖代 P000108', 'Ducimus ut consequatur eius officia explicabo id. Eum nam ut.', 4, 2, 1, 1, 'AVERAGE', 273.91, 'TAXABLE', 59, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(119, 'P000109', '經典OREO冰棒 P000109', 'Ut unde cumque rem. Voluptatem eveniet dolore eum dolor. Architecto atque perspiciatis molestiae iure.', 6, 3, 1, 1, 'AVERAGE', 238.47, 'TAXABLE', 70, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(120, 'P000110', '鮮果燕麥奶冰棒 P000110', 'Eius omnis animi sunt quaerat officia nihil. Soluta dignissimos fugiat. Inventore deleniti qui dolor facilis inventore. Iure corporis sint ut.', 3, 8, 1, 1, 'AVERAGE', 141.99, 'TAXABLE', 65, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(121, 'P000111', '極致海鹽焦糖冰淇淋 P000111', 'Aspernatur magnam et. Nihil et assumenda amet beatae aliquam esse et. Perspiciatis recusandae tempore voluptas. Eius et et eos fugit.', 3, 4, 1, 1, 'AVERAGE', 251.55, 'TAXABLE', 38, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(122, 'P000112', '雪藏海鹽焦糖聖代 P000112', 'Est iure quasi qui explicabo. Voluptatem sed rerum esse et impedit ullam.', 5, 2, 1, 1, 'AVERAGE', 93.52, 'TAXABLE', 68, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(123, 'P000113', '鮮果芒果優格雪糕 P000113', 'Quo in debitis iusto culpa similique et fuga. Consectetur quas qui dolor officiis quidem ut voluptas. Blanditiis doloribus est ut incidunt dignissimos incidunt.', 5, 5, 1, 1, 'AVERAGE', 317.52, 'TAXABLE', 86, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(124, 'P000114', '純濃蘭姆葡萄冰棒 P000114', 'Qui omnis quia. Sit numquam cupiditate tenetur minima. Error quidem omnis est qui.', 5, 2, 1, 1, 'AVERAGE', 327.25, 'TAXABLE', 17, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(125, 'P000115', '極致蘭姆葡萄雪酪 P000115', 'Sed inventore labore illum quia delectus voluptatibus. Maxime quia magnam est fugit quaerat eum. Aut perspiciatis consequatur quo. Qui quos ducimus animi veritatis voluptatibus et.', 9, 4, 1, 1, 'AVERAGE', 298.35, 'TAXABLE', 13, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(126, 'P000116', '雪藏蘭姆葡萄聖代 P000116', 'Modi repellat illo odio odio adipisci quos. Deleniti fugiat eos provident ea.', 4, 3, 1, 1, 'AVERAGE', 255.75, 'TAXABLE', 29, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(127, 'P000117', '經典花生冰棒 P000117', 'Repellendus praesentium consequatur modi earum rerum. Eos nobis natus et. Voluptatem eum voluptas totam quis ab consequatur itaque.', 7, 5, 1, 1, 'AVERAGE', 206.19, 'TAXABLE', 21, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(128, 'P000118', '雪藏抹茶聖代 P000118', 'Optio ipsa placeat magni. Quam ut qui optio ullam. Cupiditate dolorem voluptatum totam magni. Incidunt aliquid tenetur dolorem accusantium nemo.', 4, 7, 1, 1, 'AVERAGE', 141.06, 'TAXABLE', 72, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(129, 'P000119', '經典咖啡聖代 P000119', 'Ad officia minima eum repudiandae perferendis qui iure. Ut dolore commodi ut. Aut asperiores eligendi dolor qui consequatur.', 7, 5, 1, 1, 'AVERAGE', 79.52, 'TAXABLE', 32, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(130, 'P000120', '經典抹茶冰淇淋 P000120', 'Saepe qui quaerat doloribus dolor occaecati consequatur voluptatem. Earum consectetur possimus doloremque. Accusamus aliquid adipisci nemo consequatur.', 7, 7, 1, 1, 'AVERAGE', 199.95, 'TAXABLE', 78, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(131, 'P000121', '鮮果薄荷巧克力聖代 P000121', 'Est nulla cupiditate omnis. Quaerat quo sit inventore molestiae facere porro. Vel omnis autem sapiente. Excepturi impedit nostrum odit animi inventore accusantium et.', 8, 8, 1, 1, 'AVERAGE', 250.07, 'TAXABLE', 56, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(132, 'P000122', '夏日海鹽焦糖冰淇淋 P000122', 'Quo eius libero maiores eligendi eos. A in modi. Id id aspernatur. Quod eum natus quia eius minus quis.', 9, 3, 1, 1, 'AVERAGE', 139.40, 'TAXABLE', 89, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(133, 'P000123', '夏日芒果優格雪酪 P000123', 'Ut ut ducimus autem nisi officiis minima. Temporibus necessitatibus aut dolores. Quo qui illum.', 2, 6, 1, 1, 'AVERAGE', 70.90, 'TAXABLE', 29, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(134, 'P000124', '莊園香草雪酪 P000124', 'Est illo ex fugiat quisquam sint sapiente veritatis. Mollitia nihil culpa. Sint quis est tempora.', 9, 5, 1, 1, 'AVERAGE', 182.26, 'TAXABLE', 29, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(135, 'P000125', '雪藏海鹽焦糖雪糕 P000125', 'Corrupti quia est ipsa voluptate similique quaerat. Sed nesciunt sapiente vel iusto culpa natus aut.', 3, 5, 1, 1, 'AVERAGE', 122.64, 'TAXABLE', 80, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(136, 'P000126', '鮮果藍莓冰棒 P000126', 'Ut dolores atque dolores sit officia assumenda. Aut voluptatum dolore.', 8, 5, 1, 1, 'AVERAGE', 324.24, 'TAXABLE', 76, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(137, 'P000127', '純濃豆乳芝麻冰棒 P000127', 'Mollitia adipisci porro vero qui aut hic eligendi. Quos accusamus id consectetur magni. Laboriosam cumque esse.', 3, 3, 1, 1, 'AVERAGE', 78.20, 'TAXABLE', 73, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(138, 'P000128', '莊園蘭姆葡萄雪酪 P000128', 'Eveniet dignissimos quia consequatur ut et. Quod voluptas officiis omnis rerum. Quas maiores qui perspiciatis aut nihil. Quas iste deserunt et asperiores.', 8, 8, 1, 1, 'AVERAGE', 328.18, 'TAXABLE', 67, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(139, 'P000129', '極致藍莓雪糕 P000129', 'Est rerum et aut eum. Corrupti est voluptates ullam a reprehenderit. Aut quo at ut corporis ad. Suscipit minima totam error aperiam.', 2, 2, 1, 1, 'AVERAGE', 221.63, 'TAXABLE', 86, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(140, 'P000130', '雪藏OREO冰棒 P000130', 'Ducimus ipsam et. Sed quasi ad amet. Incidunt quos harum placeat. Rerum aut non inventore quo accusamus quae.', 3, 4, 1, 1, 'AVERAGE', 110.08, 'TAXABLE', 49, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(141, 'P000131', '夏日薄荷巧克力雪酪 P000131', 'Nemo provident in illum illum sunt. Nesciunt officiis adipisci consequatur quia delectus quae mollitia. Iure tempora tenetur.', 2, 3, 1, 1, 'AVERAGE', 228.20, 'TAXABLE', 53, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(142, 'P000132', '經典巧克力冰棒 P000132', 'Accusamus sit adipisci. In hic laboriosam sint inventore. Sint exercitationem aut atque sed accusantium. Veritatis dolorem dolorum et repudiandae voluptas odit at.', 3, 8, 1, 1, 'AVERAGE', 124.39, 'TAXABLE', 22, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(143, 'P000133', '莊園薄荷巧克力冰淇淋 P000133', 'Sit perspiciatis est aperiam neque et. Accusantium nesciunt illo ut voluptatem. Debitis quia repudiandae.', 9, 4, 1, 1, 'AVERAGE', 348.91, 'TAXABLE', 77, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(144, 'P000134', '純濃草莓聖代 P000134', 'Sit reprehenderit quasi facere sed iste. Est aut sit ut.', 7, 2, 1, 1, 'AVERAGE', 88.49, 'TAXABLE', 30, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(145, 'P000135', '純濃草莓雪酪 P000135', 'Rerum unde eius voluptas est quidem eum quia. Doloremque fuga vero adipisci natus voluptatem perspiciatis. Libero sunt dolorum et veniam culpa cum.', 9, 2, 1, 1, 'AVERAGE', 275.06, 'TAXABLE', 41, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(146, 'P000136', '雪藏海鹽焦糖雪酪 P000136', 'Eos omnis a ut quae molestiae. Eveniet consequatur est autem et. Qui ratione quas qui. Veritatis animi dolores rerum maiores alias.', 4, 2, 1, 1, 'AVERAGE', 107.84, 'TAXABLE', 64, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(147, 'P000137', '夏日咖啡冰淇淋 P000137', 'Nemo consectetur explicabo. Nesciunt aliquam animi saepe ut enim aut.', 5, 7, 1, 1, 'AVERAGE', 225.58, 'TAXABLE', 38, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(148, 'P000138', '莊園芒果優格聖代 P000138', 'Cum dolore fugit unde repellendus. Aliquid omnis corrupti sequi nulla quia amet ea. Nam atque aut non.', 8, 5, 1, 1, 'AVERAGE', 130.10, 'TAXABLE', 87, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(149, 'P000139', '鮮果芒果優格冰棒 P000139', 'Eaque vero at quia cupiditate tempora. Dignissimos aspernatur quibusdam accusamus deserunt voluptatibus culpa.', 3, 4, 1, 1, 'AVERAGE', 93.52, 'TAXABLE', 58, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(150, 'P000140', '經典香草冰棒 P000140', 'Assumenda quaerat rerum dolorum similique. Aperiam eius autem aliquid dolor culpa.', 2, 6, 1, 1, 'AVERAGE', 300.73, 'TAXABLE', 19, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(151, 'P000141', '莊園花生雪糕 P000141', 'Eos inventore mollitia rerum. Eveniet eveniet incidunt ullam quasi deserunt amet facilis. Atque qui magni laudantium sunt corrupti et molestiae.', 5, 8, 1, 1, 'AVERAGE', 218.58, 'TAXABLE', 49, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(152, 'P000142', '純濃香草聖代 P000142', 'Vel omnis porro. Nobis sit voluptas harum facere quaerat quos voluptates.', 5, 4, 1, 1, 'AVERAGE', 213.04, 'TAXABLE', 41, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(153, 'P000143', '經典花生冰淇淋 P000143', 'Molestiae numquam vel nesciunt qui id facilis. Sit maxime repellat maiores est.', 6, 2, 1, 1, 'AVERAGE', 85.92, 'TAXABLE', 84, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(154, 'P000144', '經典香草聖代 P000144', 'Quo natus et. Consequatur et dolorum.', 4, 8, 1, 1, 'AVERAGE', 251.09, 'TAXABLE', 44, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(155, 'P000145', '莊園豆乳芝麻雪酪 P000145', 'Repellat veniam fugit ut eius fuga odit. In placeat dolores ut sed reiciendis.', 4, 7, 1, 1, 'AVERAGE', 325.91, 'TAXABLE', 53, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(156, 'P000146', '經典抹茶雪糕 P000146', 'Eligendi enim amet libero molestias. Aperiam iusto iusto voluptatibus a. Est totam iusto. Amet sit neque impedit rerum perferendis molestiae et.', 7, 2, 1, 1, 'AVERAGE', 75.05, 'TAXABLE', 57, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(157, 'P000147', '夏日抹茶冰棒 P000147', 'Unde suscipit et consectetur repellat temporibus optio quidem. Nesciunt occaecati ratione voluptatum dicta.', 6, 7, 1, 1, 'AVERAGE', 334.53, 'TAXABLE', 10, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(158, 'P000148', '經典香蕉聖代 P000148', 'Nobis neque nemo. Aut nisi fuga modi mollitia non voluptas vero. Omnis perspiciatis accusantium veniam cum nostrum rerum aliquid.', 5, 3, 1, 1, 'AVERAGE', 79.48, 'TAXABLE', 48, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(159, 'P000149', '夏日抹茶雪糕 P000149', 'Quod et corrupti vero id quae recusandae. Saepe quae qui nihil consequatur at. Eos ut dicta qui possimus. Mollitia nobis praesentium asperiores voluptatibus ea nam.', 3, 2, 1, 1, 'AVERAGE', 308.47, 'TAXABLE', 29, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(160, 'P000150', '極致燕麥奶雪酪 P000150', 'Nobis corrupti qui sunt et qui. Autem in aut excepturi dolor. Ipsa excepturi facilis dolorem eum ab quisquam vel.', 9, 6, 1, 1, 'AVERAGE', 132.26, 'TAXABLE', 82, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(161, 'P000151', '鮮果豆乳芝麻聖代 P000151', 'Sit quas harum aliquid et ex et. Voluptatem cumque ratione quis qui quas. Ea autem ut eaque dolorem quia.', 3, 8, 1, 1, 'AVERAGE', 213.85, 'TAXABLE', 30, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(162, 'P000152', '夏日薄荷巧克力雪糕 P000152', 'Provident veniam tenetur alias possimus quidem. Est est qui id incidunt laborum ex reprehenderit. Et et sint omnis quia ipsum minus ut. Inventore voluptas quam tempora.', 4, 6, 1, 1, 'AVERAGE', 288.52, 'TAXABLE', 51, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(163, 'P000153', '雪藏巧克力雪酪 P000153', 'Rerum eius voluptas. In et est ipsum voluptates. Dolor sapiente quis autem autem.', 8, 8, 1, 1, 'AVERAGE', 144.18, 'TAXABLE', 88, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(164, 'P000154', '鮮果薄荷巧克力雪酪 P000154', 'Et ut adipisci minus quas quidem at non. Voluptatibus non dolor voluptas harum et exercitationem. Deserunt possimus voluptas est et rem praesentium id.', 8, 3, 1, 1, 'AVERAGE', 117.09, 'TAXABLE', 10, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(165, 'P000155', '鮮果咖啡聖代 P000155', 'Soluta ex distinctio. Ut aliquam nostrum deleniti provident adipisci ab. Facilis asperiores libero in quam.', 8, 8, 1, 1, 'AVERAGE', 189.30, 'TAXABLE', 22, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(166, 'P000156', '莊園草莓雪酪 P000156', 'Deserunt quia tempora. Dolor rerum unde molestiae in quae. Autem quibusdam eum pariatur.', 3, 6, 1, 1, 'AVERAGE', 53.50, 'TAXABLE', 23, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(167, 'P000157', '鮮果抹茶冰淇淋 P000157', 'Dolorem non debitis adipisci consequuntur laboriosam quas omnis. Officia assumenda veniam molestiae.', 3, 3, 1, 1, 'AVERAGE', 222.64, 'TAXABLE', 36, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(168, 'P000158', '鮮果花生雪酪 P000158', 'Itaque dolores laborum voluptate culpa officiis ipsum. Pariatur commodi quas eius nemo rerum. Suscipit sit tempora temporibus dignissimos.', 7, 5, 1, 1, 'AVERAGE', 65.90, 'TAXABLE', 51, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(169, 'P000159', '夏日抹茶冰棒 P000159', 'Dolore fugiat vel illo et. Quo tempore molestiae minus tenetur. Nulla ipsa quam perferendis. Saepe nemo ex a.', 6, 8, 1, 1, 'AVERAGE', 340.53, 'TAXABLE', 63, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(170, 'P000160', '夏日蘭姆葡萄雪糕 P000160', 'Fugit quia mollitia. Accusantium cum asperiores placeat nisi id qui. Consectetur aut voluptatem ipsa quam repellat omnis.', 8, 8, 1, 1, 'AVERAGE', 240.51, 'TAXABLE', 79, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(171, 'P000161', '極致薄荷巧克力冰淇淋 P000161', 'A deleniti illum id. Velit deserunt deserunt placeat velit quasi ut. Perferendis omnis pariatur quam consequatur. Magni officiis minima sed expedita.', 5, 8, 1, 1, 'AVERAGE', 347.01, 'TAXABLE', 94, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(172, 'P000162', '夏日抹茶聖代 P000162', 'Asperiores et voluptatem ullam repellendus voluptatum non quis. Sit praesentium ea ex odit ad.', 2, 4, 1, 1, 'AVERAGE', 294.58, 'TAXABLE', 62, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(173, 'P000163', '極致薄荷巧克力雪糕 P000163', 'Et quisquam est aut deserunt culpa unde. Consequuntur consequuntur et autem.', 2, 2, 1, 1, 'AVERAGE', 195.16, 'TAXABLE', 27, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(174, 'P000164', '純濃燕麥奶冰淇淋 P000164', 'Aut sunt voluptatum alias vel ut distinctio architecto. Sed qui rerum eius illo in necessitatibus nihil. Exercitationem non voluptate ut nam.', 5, 8, 1, 1, 'AVERAGE', 208.76, 'TAXABLE', 31, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(175, 'P000165', '極致海鹽焦糖雪酪 P000165', 'Minima tempore omnis. Deleniti in eum recusandae quam placeat numquam quo. Veniam sit laborum rerum ratione occaecati. Alias nostrum excepturi consectetur voluptas.', 8, 8, 1, 1, 'AVERAGE', 109.68, 'TAXABLE', 17, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(176, 'P000166', '鮮果豆乳芝麻聖代 P000166', 'Voluptatibus aliquam cumque deserunt quas ea. Accusantium eum nostrum voluptatem ab aut. In placeat aspernatur natus quam et quo. Repellendus non suscipit.', 6, 3, 1, 1, 'AVERAGE', 150.49, 'TAXABLE', 51, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(177, 'P000167', '極致藍莓聖代 P000167', 'Necessitatibus exercitationem et modi. Repudiandae ut quae excepturi vero quas et. Dolor assumenda officiis fuga voluptas.', 7, 7, 1, 1, 'AVERAGE', 116.31, 'TAXABLE', 12, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(178, 'P000168', '純濃藍莓冰棒 P000168', 'Earum quam repellendus qui recusandae nulla ipsa libero. Quia maiores iure voluptas necessitatibus enim. Dolorem eos praesentium odit aut.', 3, 6, 1, 1, 'AVERAGE', 149.19, 'TAXABLE', 37, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(179, 'P000169', '夏日草莓冰棒 P000169', 'Et quisquam odit quo sit molestiae laudantium. Ut omnis omnis consequuntur laboriosam magnam saepe iusto. Sunt et natus molestias aut tempora adipisci.', 5, 3, 1, 1, 'AVERAGE', 178.56, 'TAXABLE', 61, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(180, 'P000170', '純濃咖啡冰淇淋 P000170', 'Fuga earum ea consequatur. Quo recusandae velit et eos ut dolorem. Laudantium eos dicta consequuntur.', 4, 7, 1, 1, 'AVERAGE', 71.84, 'TAXABLE', 15, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(181, 'P000171', '夏日巧克力冰淇淋 P000171', 'Et quos nobis aliquam commodi aut atque quia. Et fugiat sed rerum qui. Repudiandae optio ducimus et molestiae. Eum non quidem quam fuga saepe.', 6, 5, 1, 1, 'AVERAGE', 105.59, 'TAXABLE', 70, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(182, 'P000172', '純濃抹茶冰淇淋 P000172', 'Illo consequatur ea eaque a rem praesentium. Iste eligendi doloremque eos dolore ratione. Laudantium dolor rerum quia deleniti expedita aperiam.', 4, 8, 1, 1, 'AVERAGE', 73.79, 'TAXABLE', 23, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(183, 'P000173', '夏日芒果優格聖代 P000173', 'Voluptatem delectus quaerat. Dolore minima aut officiis qui eum qui.', 9, 3, 1, 1, 'AVERAGE', 123.30, 'TAXABLE', 47, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(184, 'P000174', '經典草莓雪糕 P000174', 'Laboriosam voluptas illo vitae dolor. Nam reprehenderit et ea placeat. Numquam deserunt eveniet suscipit consequatur.', 5, 5, 1, 1, 'AVERAGE', 120.16, 'TAXABLE', 28, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(185, 'P000175', '鮮果薄荷巧克力雪糕 P000175', 'Qui omnis totam fugiat praesentium. Hic perspiciatis voluptatem nobis explicabo. Aut tenetur beatae rerum eaque totam repellat ab. Qui natus quia.', 9, 5, 1, 1, 'AVERAGE', 185.06, 'TAXABLE', 81, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(186, 'P000176', '極致燕麥奶聖代 P000176', 'Non et quia. Rem illo praesentium molestiae sunt consequatur harum. Voluptatem enim voluptas sunt est. Ex ut ut consequuntur.', 3, 8, 1, 1, 'AVERAGE', 250.07, 'TAXABLE', 97, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(187, 'P000177', '雪藏薄荷巧克力冰棒 P000177', 'Corporis ut itaque. Voluptas ut aut. Sed occaecati voluptatem.', 8, 4, 1, 1, 'AVERAGE', 147.92, 'TAXABLE', 74, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(188, 'P000178', '夏日豆乳芝麻雪糕 P000178', 'Laudantium distinctio dolore dicta corporis a similique. Amet quia sed voluptatem aut explicabo.', 3, 3, 1, 1, 'AVERAGE', 129.50, 'TAXABLE', 28, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(189, 'P000179', '極致抹茶冰棒 P000179', 'Voluptas voluptate eaque et sed voluptatem sit quisquam. Commodi culpa aliquam nemo et. Delectus molestiae reiciendis dolor. Odit vel ea perspiciatis odit natus consequatur unde.', 4, 4, 1, 1, 'AVERAGE', 182.22, 'TAXABLE', 99, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(190, 'P000180', '雪藏薄荷巧克力雪酪 P000180', 'Consequatur occaecati laborum aut voluptas nihil. Quod voluptatem aliquam temporibus optio. Animi aut nulla quia odit molestiae at veritatis. Sed nesciunt rerum repudiandae deleniti.', 7, 8, 1, 1, 'AVERAGE', 68.14, 'TAXABLE', 42, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(191, 'P000181', '純濃海鹽焦糖聖代 P000181', 'Ipsum dolor ea facere rerum. Voluptatem qui aut quaerat. Laborum exercitationem et dolores ut dolor ipsam qui. Delectus non commodi.', 9, 4, 1, 1, 'AVERAGE', 143.37, 'TAXABLE', 91, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(192, 'P000182', '經典巧克力冰淇淋 P000182', 'Minima ipsam quaerat inventore hic. Dolor consequuntur ut. Quae dolores iusto et necessitatibus quisquam.', 5, 8, 1, 1, 'AVERAGE', 298.88, 'TAXABLE', 54, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1);
INSERT INTO `product` (`product_id`, `product_code`, `name`, `description`, `category_id`, `unit_id`, `is_purchasable`, `is_salable`, `cost_method`, `base_price`, `tax_type`, `safety_stock_quantity`, `min_order_quantity`, `max_order_quantity`, `lead_time_days`, `is_active`, `remarks`, `created_at`, `created_by`, `updated_at`, `updated_by`) VALUES
(193, 'P000183', '鮮果咖啡雪酪 P000183', 'Consequatur eos laborum omnis necessitatibus atque quia. Quibusdam et minima odit est ut. Sit et voluptas aut. Non qui quo praesentium et.', 8, 5, 1, 1, 'AVERAGE', 185.72, 'TAXABLE', 90, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(194, 'P000184', '雪藏芒果優格聖代 P000184', 'Dolorem asperiores et natus adipisci ut saepe. Ut pariatur et qui pariatur debitis.', 7, 2, 1, 1, 'AVERAGE', 217.46, 'TAXABLE', 99, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(195, 'P000185', '夏日香蕉雪酪 P000185', 'Temporibus molestiae officia natus qui. Asperiores recusandae fugiat. Eos occaecati voluptas voluptas illo vel ipsum neque.', 7, 2, 1, 1, 'AVERAGE', 341.04, 'TAXABLE', 74, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(196, 'P000186', '極致草莓雪糕 P000186', 'Cumque aliquam vitae et neque. Consequatur veniam est. Asperiores ea at et praesentium illo.', 4, 6, 1, 1, 'AVERAGE', 144.76, 'TAXABLE', 56, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(197, 'P000187', '極致海鹽焦糖聖代 P000187', 'Reprehenderit eaque dolore omnis. Optio et cumque. Expedita voluptates et sit numquam vitae.', 4, 5, 1, 1, 'AVERAGE', 234.19, 'TAXABLE', 29, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(198, 'P000188', '莊園芒果優格冰淇淋 P000188', 'Iste ea tenetur sapiente aut fugiat ut cum. Veniam qui ea tenetur animi qui tempore enim. Exercitationem eum voluptatem autem vero ut.', 6, 8, 1, 1, 'AVERAGE', 88.20, 'TAXABLE', 74, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(199, 'P000189', '鮮果抹茶雪酪 P000189', 'Repellat odio asperiores temporibus eius iure esse necessitatibus. Aut corporis occaecati autem sit repellat aut eligendi.', 7, 5, 1, 1, 'AVERAGE', 296.79, 'TAXABLE', 79, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(200, 'P000190', '夏日花生雪糕 P000190', 'Quisquam consequatur qui est. Similique ea doloremque. Est magnam nisi veniam modi.', 9, 2, 1, 1, 'AVERAGE', 335.88, 'TAXABLE', 45, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(201, 'P000191', '雪藏香蕉雪糕 P000191', 'Esse accusamus temporibus et. Placeat tempore sint vel consequuntur.', 9, 6, 1, 1, 'AVERAGE', 156.53, 'TAXABLE', 50, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(202, 'P000192', '極致香草聖代 P000192', 'Et nemo quas tempore aspernatur amet. Nostrum nihil natus. Dolorem sed nihil.', 3, 8, 1, 1, 'AVERAGE', 51.02, 'TAXABLE', 39, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(203, 'P000193', '純濃抹茶冰淇淋 P000193', 'Fugiat debitis aut ut. Quibusdam aliquid impedit perferendis repellat voluptatem.', 6, 2, 1, 1, 'AVERAGE', 207.34, 'TAXABLE', 45, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(204, 'P000194', '夏日蘭姆葡萄冰棒 P000194', 'Quibusdam ea deleniti odit molestiae alias beatae. Ut voluptatem eos ex fugit. Possimus qui excepturi veniam dolorem. Asperiores nihil ratione aliquid consequuntur ut magni.', 4, 7, 1, 1, 'AVERAGE', 236.05, 'TAXABLE', 86, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(205, 'P000195', '純濃香草雪糕 P000195', 'Et aut nihil nam ut quasi dolore similique. Laudantium rem ex nulla.', 8, 6, 1, 1, 'AVERAGE', 166.70, 'TAXABLE', 18, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(206, 'P000196', '雪藏咖啡雪糕 P000196', 'Alias nisi eum quaerat voluptatem fugiat. Laboriosam consequuntur voluptatem voluptatibus rerum quam tempore tempora. Adipisci porro illo consequatur repudiandae harum quia.', 3, 7, 1, 1, 'AVERAGE', 146.35, 'TAXABLE', 95, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(207, 'P000197', '純濃薄荷巧克力冰淇淋 P000197', 'Esse omnis provident est id vel voluptatem. Voluptatem et est consequatur.', 2, 5, 1, 1, 'AVERAGE', 265.62, 'TAXABLE', 45, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(208, 'P000198', '雪藏香蕉聖代 P000198', 'Error rerum quia expedita inventore ut est non. Laboriosam quisquam veritatis iste.', 4, 7, 1, 1, 'AVERAGE', 191.11, 'TAXABLE', 44, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(209, 'P000199', '夏日豆乳芝麻冰棒 P000199', 'Consequatur architecto dolores soluta harum et illo culpa. Minus non labore autem voluptatem ut.', 6, 6, 1, 1, 'AVERAGE', 324.48, 'TAXABLE', 42, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(210, 'P000200', '極致薄荷巧克力冰淇淋 P000200', 'Nobis quaerat voluptatem sed eligendi aliquid a. Exercitationem et hic velit rerum.', 2, 8, 1, 1, 'AVERAGE', 193.78, 'TAXABLE', 74, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(211, 'P000201', '夏日香蕉冰淇淋 P000201', 'Est eum beatae maiores et. Dolor fugiat laudantium eum maiores unde non eveniet.', 6, 7, 1, 1, 'AVERAGE', 56.41, 'TAXABLE', 90, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(212, 'P000202', '經典巧克力雪糕 P000202', 'Ex enim corrupti nihil voluptatem cumque. Est omnis quis ipsam soluta. Officia cum necessitatibus dignissimos ea repellendus itaque illum. Quis fugit nisi eligendi molestiae.', 6, 8, 1, 1, 'AVERAGE', 154.02, 'TAXABLE', 48, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(213, 'P000203', '鮮果海鹽焦糖雪糕 P000203', 'Sed sunt doloremque at. Dignissimos eos dolore. Molestiae iste dolores blanditiis. Adipisci voluptatem in.', 8, 8, 1, 1, 'AVERAGE', 172.72, 'TAXABLE', 98, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(214, 'P000204', '雪藏抹茶雪酪 P000204', 'Odit consequatur minus sit. Quo excepturi laborum.', 2, 7, 1, 1, 'AVERAGE', 157.02, 'TAXABLE', 34, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(215, 'P000205', '經典抹茶聖代 P000205', 'Sunt ad consequatur corporis. Voluptate cupiditate et quos. Voluptas sequi natus alias occaecati commodi necessitatibus error. Aut quae natus nobis voluptatum.', 4, 2, 1, 1, 'AVERAGE', 234.92, 'TAXABLE', 47, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(216, 'P000206', '極致咖啡雪糕 P000206', 'Non et est occaecati qui eos commodi. Quo aut sit maiores ex voluptatem voluptatibus. Et libero cupiditate et.', 9, 2, 1, 1, 'AVERAGE', 151.02, 'TAXABLE', 27, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(217, 'P000207', '純濃香草冰淇淋 P000207', 'Sed quos officiis cum rerum nesciunt consequatur. Deleniti delectus consequatur officiis ut delectus nisi. Porro cumque quia ipsa aut dolor ab neque.', 7, 6, 1, 1, 'AVERAGE', 105.75, 'TAXABLE', 51, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(218, 'P000208', '經典藍莓冰棒 P000208', 'Laudantium omnis ut. Molestiae et consequatur voluptatibus sed nulla et quibusdam.', 9, 2, 1, 1, 'AVERAGE', 138.26, 'TAXABLE', 49, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(219, 'P000209', '鮮果花生雪糕 P000209', 'Aut deleniti architecto aperiam et consequatur et doloremque. Ea laborum inventore dignissimos sit voluptatem inventore sint. Quo similique vel ducimus. Aperiam earum dignissimos ut voluptate.', 7, 8, 1, 1, 'AVERAGE', 208.99, 'TAXABLE', 47, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(220, 'P000210', '鮮果抹茶冰淇淋 P000210', 'Sed sed ex consequatur modi reprehenderit. In nihil voluptates officia culpa. Unde iste aspernatur dicta velit et quaerat.', 4, 8, 1, 1, 'AVERAGE', 206.62, 'TAXABLE', 63, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(221, 'P000211', '夏日豆乳芝麻冰淇淋 P000211', 'Possimus doloribus in et earum accusamus. Assumenda voluptatibus fugiat dolorum voluptatem.', 4, 8, 1, 1, 'AVERAGE', 164.51, 'TAXABLE', 92, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(222, 'P000212', '純濃草莓冰淇淋 P000212', 'Molestiae et quis consequuntur. Saepe natus tempore necessitatibus. Sapiente quas accusamus. Id perferendis sunt aut voluptas.', 9, 5, 1, 1, 'AVERAGE', 84.31, 'TAXABLE', 97, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(223, 'P000213', '極致花生雪糕 P000213', 'Maiores ut ad. Suscipit sunt quo numquam minima porro. Placeat temporibus ea quod necessitatibus est magnam. Eaque culpa quia velit reprehenderit eius.', 4, 5, 1, 1, 'AVERAGE', 207.73, 'TAXABLE', 66, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(224, 'P000214', '夏日豆乳芝麻聖代 P000214', 'Error veniam pariatur temporibus aut dolores. Et quis saepe ullam voluptatem facilis. Sapiente et quia. Quasi in ut quo quo voluptate fugiat.', 2, 5, 1, 1, 'AVERAGE', 83.09, 'TAXABLE', 19, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(225, 'P000215', '鮮果芒果優格冰棒 P000215', 'Maxime consequatur corporis sunt ea aspernatur non ipsum. Enim molestiae et consequatur quidem. Iure est ad accusantium. Harum sint voluptatem.', 2, 5, 1, 1, 'AVERAGE', 194.37, 'TAXABLE', 19, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(226, 'P000216', '夏日燕麥奶雪酪 P000216', 'Voluptas ex debitis id amet laboriosam atque. Consequatur aut expedita. Est officia architecto unde harum deserunt eum eum.', 3, 5, 1, 1, 'AVERAGE', 293.38, 'TAXABLE', 27, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(227, 'P000217', '經典燕麥奶冰淇淋 P000217', 'Enim quas molestiae corporis ut. Non dolorem et voluptatibus vero quo qui. Ex vero vel enim.', 4, 3, 1, 1, 'AVERAGE', 202.15, 'TAXABLE', 48, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(228, 'P000218', '雪藏香草冰棒 P000218', 'Tempore magni culpa est praesentium. Ipsum eveniet mollitia. Dicta a provident.', 3, 3, 1, 1, 'AVERAGE', 178.97, 'TAXABLE', 86, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(229, 'P000219', '純濃咖啡冰淇淋 P000219', 'Similique ab recusandae eum. Animi ipsa dolores. Tempora et autem est consequatur ut ratione quod. In quis est praesentium magni aut rerum mollitia.', 3, 7, 1, 1, 'AVERAGE', 154.20, 'TAXABLE', 26, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(230, 'P000220', '極致OREO冰淇淋 P000220', 'Numquam quod dicta nisi fuga. Quam quis voluptas sint modi quo. Iusto fugit repellat et aut velit a earum. Ut velit quia architecto quia.', 8, 5, 1, 1, 'AVERAGE', 312.23, 'TAXABLE', 12, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(231, 'P000221', '極致香蕉冰棒 P000221', 'Unde non dignissimos aut. Sit veniam natus magni repudiandae quam fugit. Voluptatem officiis et est et odit quo dignissimos. Iure ab dolorum et.', 8, 5, 1, 1, 'AVERAGE', 182.11, 'TAXABLE', 82, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(232, 'P000222', '莊園豆乳芝麻雪酪 P000222', 'Accusantium quia quae. Id quod ad eveniet velit. Maiores et velit atque magni fugit ex. Blanditiis ut aut inventore omnis.', 9, 3, 1, 1, 'AVERAGE', 234.20, 'TAXABLE', 67, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(233, 'P000223', '夏日咖啡聖代 P000223', 'Aperiam omnis voluptates autem qui mollitia perspiciatis. Ducimus et eos facere ullam ut porro mollitia. Voluptatibus eum similique.', 7, 5, 1, 1, 'AVERAGE', 209.16, 'TAXABLE', 81, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(234, 'P000224', '夏日蘭姆葡萄雪酪 P000224', 'Velit necessitatibus accusantium totam quia. Est repellat hic. Quos et quibusdam perferendis similique fugit. Nisi magni tempore veniam odit.', 8, 3, 1, 1, 'AVERAGE', 272.71, 'TAXABLE', 70, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(235, 'P000225', '莊園OREO雪酪 P000225', 'Iure mollitia ad minus quo quae. Aut sint temporibus. Soluta nesciunt consequatur iure placeat quis. Odio dolor tempora perferendis accusamus voluptatibus.', 5, 4, 1, 1, 'AVERAGE', 183.57, 'TAXABLE', 27, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(236, 'P000226', '莊園花生冰淇淋 P000226', 'Est vero tenetur et laboriosam ab soluta. Quas dolorum sit eaque.', 6, 3, 1, 1, 'AVERAGE', 341.27, 'TAXABLE', 11, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(237, 'P000227', '純濃薄荷巧克力冰棒 P000227', 'Accusamus numquam amet. Excepturi mollitia qui eveniet fugit. Deserunt ipsum molestias accusamus aspernatur et magni commodi. Quo laboriosam adipisci ducimus.', 9, 4, 1, 1, 'AVERAGE', 235.24, 'TAXABLE', 74, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(238, 'P000228', '鮮果花生冰棒 P000228', 'Dolorem voluptatem dolorem dolorum eos magni. Ut voluptatem dolore.', 7, 7, 1, 1, 'AVERAGE', 334.87, 'TAXABLE', 82, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(239, 'P000229', '鮮果巧克力雪酪 P000229', 'Provident consequatur ullam. Omnis doloremque voluptatem qui facere eum.', 4, 3, 1, 1, 'AVERAGE', 217.74, 'TAXABLE', 13, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(240, 'P000230', '經典OREO聖代 P000230', 'Eveniet corporis dolorem sequi. Commodi vero distinctio sint placeat aut nulla.', 2, 3, 1, 1, 'AVERAGE', 313.65, 'TAXABLE', 23, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(241, 'P000231', '鮮果OREO冰淇淋 P000231', 'Nihil repellat et voluptas dolores autem dolores. Error voluptas ab quo optio et sint.', 7, 8, 1, 1, 'AVERAGE', 268.05, 'TAXABLE', 92, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(242, 'P000232', '經典巧克力雪酪 P000232', 'Vel omnis qui nisi similique. Quis ut ea autem qui harum. Molestias vitae rem sapiente.', 2, 8, 1, 1, 'AVERAGE', 262.74, 'TAXABLE', 56, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(243, 'P000233', '極致咖啡聖代 P000233', 'Est eum placeat qui quo. Adipisci omnis veritatis rerum dolorem necessitatibus. Veniam qui mollitia rerum at. Quidem impedit ipsum velit.', 9, 6, 1, 1, 'AVERAGE', 283.85, 'TAXABLE', 24, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(244, 'P000234', '鮮果薄荷巧克力冰棒 P000234', 'Voluptas error rerum voluptate aut fuga. Qui quia sit adipisci beatae sequi sit. Quia rerum unde atque. Eaque molestiae dolores molestiae numquam.', 3, 6, 1, 1, 'AVERAGE', 222.76, 'TAXABLE', 62, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(245, 'P000235', '經典巧克力雪糕 P000235', 'Nobis amet rerum voluptate. Veritatis atque maiores. Sit atque dolorem et.', 5, 4, 1, 1, 'AVERAGE', 247.57, 'TAXABLE', 76, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(246, 'P000236', '夏日草莓雪糕 P000236', 'Eum sit voluptates quo. Perferendis sed eveniet et adipisci dolores labore doloremque.', 6, 3, 1, 1, 'AVERAGE', 69.49, 'TAXABLE', 97, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(247, 'P000237', '經典藍莓雪糕 P000237', 'Perspiciatis quam non qui. Voluptatibus saepe enim et maxime. Quia non optio iusto fugiat.', 9, 8, 1, 1, 'AVERAGE', 184.22, 'TAXABLE', 39, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(248, 'P000238', '夏日芒果優格雪酪 P000238', 'Maxime voluptatem enim quibusdam. Sit natus ut eos eos. Nisi qui et voluptatem aspernatur aut officia quam.', 4, 5, 1, 1, 'AVERAGE', 328.31, 'TAXABLE', 49, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(249, 'P000239', '雪藏薄荷巧克力冰棒 P000239', 'Reprehenderit eaque reprehenderit voluptas velit ipsum enim sit. Quae voluptatem accusamus sapiente modi. Ab est rerum aut eaque aut est ea. Optio non saepe consequatur repellendus sit nam itaque.', 7, 4, 1, 1, 'AVERAGE', 223.83, 'TAXABLE', 23, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(250, 'P000240', '莊園香草冰棒 P000240', 'Aliquam cum beatae molestias. Culpa aspernatur et et soluta.', 6, 4, 1, 1, 'AVERAGE', 212.81, 'TAXABLE', 53, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(251, 'P000241', '夏日燕麥奶雪酪 P000241', 'Fugit eius nemo maiores. Voluptas quidem delectus eveniet.', 9, 8, 1, 1, 'AVERAGE', 241.01, 'TAXABLE', 81, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(252, 'P000242', '鮮果燕麥奶雪酪 P000242', 'Labore ad atque ad eveniet. Assumenda illum facilis officiis mollitia.', 3, 2, 1, 1, 'AVERAGE', 173.08, 'TAXABLE', 76, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(253, 'P000243', '純濃咖啡雪糕 P000243', 'Dicta sapiente placeat minima in. Sint ea illum et doloribus esse. Voluptate ipsum atque est ipsam sint odio libero. Illo minima voluptate minus dicta.', 4, 7, 1, 1, 'AVERAGE', 279.00, 'TAXABLE', 24, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(254, 'P000244', '極致燕麥奶聖代 P000244', 'Nostrum et et rerum. Est accusamus nemo nulla ullam et inventore eum. In quod voluptates error et atque dolore.', 9, 5, 1, 1, 'AVERAGE', 70.59, 'TAXABLE', 53, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(255, 'P000245', '雪藏咖啡冰棒 P000245', 'Nihil commodi asperiores est animi mollitia. Aut velit est. Dolorem unde est. Iure sed accusamus rerum quisquam at.', 8, 8, 1, 1, 'AVERAGE', 168.18, 'TAXABLE', 92, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(256, 'P000246', '莊園香蕉冰棒 P000246', 'Rem qui aliquam natus optio. Rerum iste soluta deleniti. Et saepe vel. Fugiat illum odit distinctio reiciendis pariatur vel.', 7, 5, 1, 1, 'AVERAGE', 334.43, 'TAXABLE', 41, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(257, 'P000247', '經典燕麥奶雪糕 P000247', 'Velit nostrum quibusdam distinctio aut reprehenderit ut. Occaecati nobis eum excepturi beatae et. Sint unde dolores dolores. Fugiat optio eos aut a magnam ut.', 4, 6, 1, 1, 'AVERAGE', 156.42, 'TAXABLE', 31, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(258, 'P000248', '莊園海鹽焦糖聖代 P000248', 'Enim libero similique optio temporibus quasi aut magni. Modi eum vel labore.', 3, 7, 1, 1, 'AVERAGE', 293.11, 'TAXABLE', 65, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(259, 'P000249', '雪藏燕麥奶冰棒 P000249', 'Excepturi ut et omnis non. Delectus ab dignissimos voluptatem qui. Et sint delectus velit repellendus at.', 3, 5, 1, 1, 'AVERAGE', 227.64, 'TAXABLE', 90, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(260, 'P000250', '莊園海鹽焦糖雪糕 P000250', 'Esse dolorem quia id quidem. Qui rerum iusto nostrum ut.', 3, 3, 1, 1, 'AVERAGE', 145.29, 'TAXABLE', 79, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(261, 'P000251', '雪藏香蕉聖代 P000251', 'Accusamus incidunt sunt ratione. Numquam qui velit eaque. Et autem laudantium aliquid magni ut non.', 8, 6, 1, 1, 'AVERAGE', 277.12, 'TAXABLE', 64, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(262, 'P000252', '夏日OREO雪酪 P000252', 'Sint impedit est rem cumque quo id. Aliquam eveniet architecto odit in. Sint est numquam at voluptatem atque.', 6, 6, 1, 1, 'AVERAGE', 77.06, 'TAXABLE', 81, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(263, 'P000253', '鮮果薄荷巧克力雪酪 P000253', 'Alias ut necessitatibus soluta ea error ullam. Et non sequi iure nihil sunt ducimus. Mollitia voluptas velit repellat quia. Inventore perspiciatis repellendus fuga facere natus corrupti.', 7, 6, 1, 1, 'AVERAGE', 140.48, 'TAXABLE', 78, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(264, 'P000254', '純濃巧克力冰棒 P000254', 'Dolor quo facilis adipisci et illo qui. Et voluptas et impedit temporibus quam corrupti.', 6, 4, 1, 1, 'AVERAGE', 331.30, 'TAXABLE', 27, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(265, 'P000255', '雪藏抹茶聖代 P000255', 'Vero natus modi vel nostrum. Ratione minus iusto et aperiam sequi. Culpa hic dolor.', 3, 2, 1, 1, 'AVERAGE', 90.98, 'TAXABLE', 13, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(266, 'P000256', '雪藏燕麥奶冰淇淋 P000256', 'Accusantium voluptatem necessitatibus animi consectetur ad reprehenderit. Perspiciatis odit voluptatem laborum aut. Facilis qui provident impedit eius esse. Voluptatem cupiditate praesentium dolor assumenda repudiandae.', 3, 8, 1, 1, 'AVERAGE', 142.33, 'TAXABLE', 80, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(267, 'P000257', '雪藏蘭姆葡萄冰棒 P000257', 'Numquam minima quidem adipisci aut. Porro reiciendis qui nesciunt qui magnam. Tenetur nulla dignissimos. Dignissimos occaecati veritatis est suscipit eos.', 6, 8, 1, 1, 'AVERAGE', 85.95, 'TAXABLE', 41, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(268, 'P000258', '莊園巧克力雪酪 P000258', 'Aspernatur et reprehenderit eius vitae maiores ut. Quibusdam esse vitae. Dolorem et ipsum occaecati et odio. Beatae assumenda autem enim et et ab tempora.', 7, 8, 1, 1, 'AVERAGE', 138.97, 'TAXABLE', 63, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(269, 'P000259', '純濃香蕉聖代 P000259', 'Ex in minima. Voluptates sit dolores ut eum omnis quo pariatur.', 9, 4, 1, 1, 'AVERAGE', 61.58, 'TAXABLE', 65, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(270, 'P000260', '夏日香蕉雪酪 P000260', 'Aperiam provident sint. Et commodi maiores repudiandae. Suscipit necessitatibus recusandae quae.', 5, 3, 1, 1, 'AVERAGE', 162.15, 'TAXABLE', 89, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(271, 'P000261', '鮮果抹茶冰淇淋 P000261', 'Voluptatum sunt quia ut. Eaque molestias est.', 9, 3, 1, 1, 'AVERAGE', 214.30, 'TAXABLE', 53, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(272, 'P000262', '夏日花生雪酪 P000262', 'Libero quis sed exercitationem quo mollitia autem. Iure corporis quisquam qui adipisci. Iure aperiam et officiis delectus qui.', 2, 8, 1, 1, 'AVERAGE', 303.13, 'TAXABLE', 56, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(273, 'P000263', '純濃花生冰棒 P000263', 'Enim distinctio aspernatur maiores velit. Neque officiis error. Doloribus expedita soluta placeat perspiciatis distinctio ut. Facilis et debitis suscipit rerum qui consequatur id.', 3, 2, 1, 1, 'AVERAGE', 150.50, 'TAXABLE', 26, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(274, 'P000264', '雪藏香草聖代 P000264', 'Velit et ut eum minima est. Inventore mollitia sit facere et qui reiciendis. Eligendi ducimus cum rerum quas.', 4, 3, 1, 1, 'AVERAGE', 302.32, 'TAXABLE', 84, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(275, 'P000265', '莊園香草冰棒 P000265', 'Aut pariatur suscipit. Deserunt commodi tempore. Incidunt aperiam quis possimus earum perferendis quod eos.', 9, 8, 1, 1, 'AVERAGE', 195.80, 'TAXABLE', 21, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(276, 'P000266', '純濃芒果優格雪糕 P000266', 'Inventore ut quia eligendi officiis. Iste ex sit.', 2, 3, 1, 1, 'AVERAGE', 146.65, 'TAXABLE', 95, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(277, 'P000267', '純濃豆乳芝麻聖代 P000267', 'Aut fuga autem magni vero ad. Unde qui pariatur minima ducimus est dolores quia. Voluptatem ipsum aut et dolorem corrupti facere non. Vero mollitia reiciendis.', 2, 7, 1, 1, 'AVERAGE', 303.34, 'TAXABLE', 23, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(278, 'P000268', '極致燕麥奶聖代 P000268', 'Et laboriosam aut quasi veritatis autem. Nulla possimus perspiciatis ullam dolore sapiente. Ea nesciunt reiciendis nobis in eaque consequatur. Cumque expedita nemo et.', 6, 3, 1, 1, 'AVERAGE', 46.76, 'TAXABLE', 88, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(279, 'P000269', '夏日OREO雪糕 P000269', 'Dolores eum aliquid adipisci sapiente sed. Minus iure eveniet.', 9, 4, 1, 1, 'AVERAGE', 316.22, 'TAXABLE', 85, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(280, 'P000270', '純濃藍莓聖代 P000270', 'Facere labore vel blanditiis velit rem alias. Occaecati unde quisquam ab libero consequatur quaerat. At non corrupti laboriosam et.', 4, 4, 1, 1, 'AVERAGE', 323.34, 'TAXABLE', 46, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(281, 'P000271', '夏日薄荷巧克力聖代 P000271', 'Blanditiis quam aut perferendis vero eos fugit. Dolorem ullam expedita quod. Quod qui consectetur mollitia officiis.', 9, 6, 1, 1, 'AVERAGE', 268.99, 'TAXABLE', 99, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(282, 'P000272', '夏日燕麥奶冰淇淋 P000272', 'Sed sit soluta. Veniam dolorem accusantium est sit.', 2, 5, 1, 1, 'AVERAGE', 160.12, 'TAXABLE', 39, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(283, 'P000273', '純濃花生雪酪 P000273', 'Harum quia ut totam repellendus ab voluptas unde. Possimus sed provident ea qui nemo.', 7, 6, 1, 1, 'AVERAGE', 249.05, 'TAXABLE', 76, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(284, 'P000274', '經典藍莓雪酪 P000274', 'Dolor qui ipsum. Libero quas qui qui ullam. Sed omnis voluptatem illo a aut. Et est eum ea quis eveniet vero voluptatum.', 9, 3, 1, 1, 'AVERAGE', 235.57, 'TAXABLE', 56, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(285, 'P000275', '經典蘭姆葡萄冰棒 P000275', 'Iste similique quia sed. Voluptas quo ab impedit dolores.', 7, 2, 1, 1, 'AVERAGE', 53.35, 'TAXABLE', 12, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(286, 'P000276', '純濃咖啡雪糕 P000276', 'Est eos consectetur ratione. Quam labore distinctio harum natus molestiae atque minus. Id quos esse dolor voluptatem.', 6, 4, 1, 1, 'AVERAGE', 131.19, 'TAXABLE', 69, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(287, 'P000277', '經典燕麥奶冰淇淋 P000277', 'Ex ut veniam vel modi eaque tempore. Voluptatem totam omnis sapiente sit totam ipsum.', 2, 2, 1, 1, 'AVERAGE', 148.06, 'TAXABLE', 23, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(288, 'P000278', '夏日蘭姆葡萄聖代 P000278', 'Impedit qui beatae et labore. Maiores nobis et sed quae praesentium. Iste sequi minima sunt amet et sed. Dolore aspernatur sint nisi autem.', 3, 7, 1, 1, 'AVERAGE', 154.14, 'TAXABLE', 91, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(289, 'P000279', '經典花生雪糕 P000279', 'Ut iure exercitationem eum animi nihil. Quia ad eos suscipit eaque qui necessitatibus.', 3, 6, 1, 1, 'AVERAGE', 168.50, 'TAXABLE', 64, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(290, 'P000280', '夏日香草聖代 P000280', 'Enim iste rem quia et cupiditate blanditiis. Quas dolor optio enim voluptas veritatis odit odit. Nesciunt sit tenetur non.', 5, 3, 1, 1, 'AVERAGE', 188.73, 'TAXABLE', 64, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(291, 'P000281', '莊園咖啡聖代 P000281', 'Accusamus ut tempora non. Iure nihil tempore laborum reiciendis molestiae.', 4, 5, 1, 1, 'AVERAGE', 83.31, 'TAXABLE', 10, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(292, 'P000282', '莊園香草雪酪 P000282', 'Cupiditate quos recusandae. Qui consequuntur cum ratione et qui quis deleniti. Voluptate nihil et sit quia non nobis. Nemo ea rerum numquam vero saepe quas.', 4, 3, 1, 1, 'AVERAGE', 248.94, 'TAXABLE', 79, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(293, 'P000283', '純濃香草雪糕 P000283', 'Eligendi rerum aut doloribus esse non. Ratione rem velit. Illo est aut et ea. Corporis in ea dolores incidunt.', 8, 8, 1, 1, 'AVERAGE', 224.94, 'TAXABLE', 34, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(294, 'P000284', '極致芒果優格聖代 P000284', 'Quia voluptas dolorem. Tempore ex hic culpa porro. Aut voluptatem quos. Ad praesentium magni dolores velit praesentium.', 7, 8, 1, 1, 'AVERAGE', 170.69, 'TAXABLE', 59, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(295, 'P000285', '莊園抹茶雪酪 P000285', 'Omnis asperiores et facilis voluptatibus error amet. Rerum fugit mollitia. Dolorum sint minima et fugiat commodi. Voluptatem alias ut.', 5, 3, 1, 1, 'AVERAGE', 306.70, 'TAXABLE', 75, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(296, 'P000286', '莊園芒果優格雪糕 P000286', 'Laudantium aliquam natus sapiente omnis saepe. Velit odio natus voluptatem sapiente ipsa voluptas voluptatem. Corporis eos ipsam dolor amet.', 9, 5, 1, 1, 'AVERAGE', 91.58, 'TAXABLE', 18, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(297, 'P000287', '雪藏花生雪酪 P000287', 'Velit sapiente rerum. Sunt ut id. Voluptatibus consectetur adipisci ipsum et incidunt omnis et.', 7, 4, 1, 1, 'AVERAGE', 117.63, 'TAXABLE', 31, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(298, 'P000288', '純濃OREO聖代 P000288', 'Perspiciatis suscipit error amet quod. Temporibus laboriosam cum occaecati. Dolor omnis eius. Molestiae tempore enim.', 8, 4, 1, 1, 'AVERAGE', 64.44, 'TAXABLE', 58, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(299, 'P000289', '經典豆乳芝麻冰棒 P000289', 'Possimus dolores reprehenderit non. Est repellendus officiis qui. Enim qui inventore incidunt esse odit deserunt beatae. Sequi praesentium nemo quam ea.', 8, 5, 1, 1, 'AVERAGE', 149.45, 'TAXABLE', 32, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(300, 'P000290', '經典豆乳芝麻聖代 P000290', 'Rerum itaque animi non. Hic repudiandae eos soluta ea et. Consequuntur omnis similique quaerat voluptatem officia dolores vitae.', 3, 3, 1, 1, 'AVERAGE', 342.35, 'TAXABLE', 58, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(301, 'P000291', '夏日薄荷巧克力聖代 P000291', 'Animi cupiditate sapiente deserunt ducimus fuga. Voluptatem dolorem sunt aperiam quis provident.', 4, 4, 1, 1, 'AVERAGE', 112.24, 'TAXABLE', 28, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(302, 'P000292', '極致燕麥奶冰淇淋 P000292', 'Quae rerum illum natus. Rerum iusto ab est voluptatem. Aut ea voluptatum minus ab excepturi voluptas rerum. Nihil beatae ut recusandae.', 3, 2, 1, 1, 'AVERAGE', 219.84, 'TAXABLE', 13, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(303, 'P000293', '鮮果花生雪酪 P000293', 'Dicta sunt omnis voluptatem ab possimus. Adipisci blanditiis quisquam aut. Est doloremque voluptas officiis culpa perspiciatis exercitationem. Quia ut corporis.', 6, 5, 1, 1, 'AVERAGE', 248.61, 'TAXABLE', 45, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(304, 'P000294', '純濃巧克力雪糕 P000294', 'Voluptatem eum voluptatem ea illum eaque saepe. Iure iusto corporis similique eum aut.', 2, 5, 1, 1, 'AVERAGE', 155.75, 'TAXABLE', 68, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(305, 'P000295', '經典OREO雪酪 P000295', 'Nihil eveniet expedita. Velit libero quos non in pariatur. Necessitatibus dignissimos assumenda. Eos pariatur ab nihil provident repellat enim.', 9, 3, 1, 1, 'AVERAGE', 56.93, 'TAXABLE', 42, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(306, 'P000296', '極致蘭姆葡萄聖代 P000296', 'Consectetur ut quasi sit. Neque quia perspiciatis. Doloremque quasi libero veniam unde. Dicta impedit molestias consequatur quia impedit.', 9, 2, 1, 1, 'AVERAGE', 165.95, 'TAXABLE', 86, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(307, 'P000297', '莊園OREO聖代 P000297', 'Optio iure et. Sapiente rerum nisi et. Eos necessitatibus dignissimos labore sit et qui quia.', 3, 3, 1, 1, 'AVERAGE', 65.55, 'TAXABLE', 75, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(308, 'P000298', '雪藏豆乳芝麻雪糕 P000298', 'Totam et est qui aliquam sit. Explicabo molestiae rerum qui tempore sit. Sint voluptatibus minima ea et. Qui labore laboriosam laborum nulla molestiae suscipit.', 9, 3, 1, 1, 'AVERAGE', 129.62, 'TAXABLE', 59, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(309, 'P000299', '雪藏海鹽焦糖聖代 P000299', 'Maiores suscipit atque est. Recusandae necessitatibus quia. Dolores sapiente odit possimus. Perspiciatis est voluptas ipsam.', 6, 4, 1, 1, 'AVERAGE', 197.15, 'TAXABLE', 22, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(310, 'P000300', '雪藏豆乳芝麻冰棒 P000300', 'Nesciunt eos voluptates a natus. Quia delectus et est ab ipsa quos. Distinctio culpa quam et est ipsum perferendis. Quia possimus deserunt voluptas eveniet ratione at delectus.', 4, 3, 1, 1, 'AVERAGE', 305.18, 'TAXABLE', 64, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(311, 'P000301', '莊園咖啡冰棒 P000301', 'Ut molestias assumenda dolorem qui eum in optio. Non omnis aspernatur. Quis occaecati commodi reiciendis velit distinctio.', 3, 4, 1, 1, 'AVERAGE', 205.71, 'TAXABLE', 87, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(312, 'P000302', '夏日OREO聖代 P000302', 'Ab est et architecto. Soluta libero qui suscipit et laudantium tempore temporibus.', 7, 7, 1, 1, 'AVERAGE', 278.24, 'TAXABLE', 38, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(313, 'P000303', '鮮果芒果優格冰棒 P000303', 'Sed dolores ut ut. Velit quis autem omnis.', 6, 7, 1, 1, 'AVERAGE', 99.81, 'TAXABLE', 18, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(314, 'P000304', '雪藏豆乳芝麻冰棒 P000304', 'Quo molestias repudiandae expedita voluptatem. Quae ullam ipsum in at. Ea assumenda vel pariatur aliquam.', 6, 7, 1, 1, 'AVERAGE', 268.59, 'TAXABLE', 20, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(315, 'P000305', '純濃咖啡雪酪 P000305', 'Dignissimos nesciunt ratione illo asperiores. Ut iste sunt. Non rerum sed vel consequatur ut est nulla. Tenetur et aspernatur ipsum.', 6, 2, 1, 1, 'AVERAGE', 316.20, 'TAXABLE', 99, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(316, 'P000306', '夏日香草聖代 P000306', 'Autem omnis placeat voluptate voluptas. Est voluptas nostrum et ut aut. Id iusto quia iure. Maiores magnam delectus.', 9, 6, 1, 1, 'AVERAGE', 220.94, 'TAXABLE', 57, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(317, 'P000307', '極致藍莓冰淇淋 P000307', 'Sequi et temporibus ut. Qui et ipsa.', 7, 2, 1, 1, 'AVERAGE', 256.79, 'TAXABLE', 54, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(318, 'P000308', '經典咖啡冰棒 P000308', 'Commodi quod a. Qui cumque expedita cumque aspernatur. Quaerat praesentium enim dicta molestiae fugiat. Nemo dicta aperiam et vel ut consequuntur.', 2, 5, 1, 1, 'AVERAGE', 67.81, 'TAXABLE', 56, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(319, 'P000309', '雪藏蘭姆葡萄冰淇淋 P000309', 'Eos exercitationem cumque repellendus totam dicta nulla voluptatem. Hic debitis quis ducimus molestiae.', 3, 3, 1, 1, 'AVERAGE', 310.02, 'TAXABLE', 37, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(320, 'P000310', '鮮果抹茶冰淇淋 P000310', 'Cumque voluptatem eum facere illum dolor. Eum odio quas. Vero inventore reiciendis impedit ipsam sit maxime natus. Molestiae aut totam id corrupti dicta dolorem.', 3, 5, 1, 1, 'AVERAGE', 111.09, 'TAXABLE', 96, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(321, 'P000311', '莊園蘭姆葡萄雪酪 P000311', 'Quisquam impedit quisquam at sed et consequatur nihil. Error earum aliquid recusandae in eos non cum.', 9, 8, 1, 1, 'AVERAGE', 187.21, 'TAXABLE', 22, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(322, 'P000312', '鮮果咖啡冰淇淋 P000312', 'In voluptate et iste non natus. Aperiam nam consequuntur asperiores autem.', 2, 8, 1, 1, 'AVERAGE', 186.73, 'TAXABLE', 39, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(323, 'P000313', '純濃草莓雪酪 P000313', 'Commodi voluptatem tempore sapiente itaque non facere. Sit voluptas aspernatur voluptatem voluptatem.', 9, 6, 1, 1, 'AVERAGE', 245.98, 'TAXABLE', 82, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(324, 'P000314', '純濃OREO雪酪 P000314', 'Vel facere nobis. Voluptas vel voluptatum consequatur eum cumque. Aut voluptatum molestiae qui nobis.', 9, 8, 1, 1, 'AVERAGE', 279.27, 'TAXABLE', 81, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(325, 'P000315', '經典燕麥奶雪糕 P000315', 'Quia amet veniam delectus officia eveniet occaecati. Voluptate accusamus at deserunt.', 6, 8, 1, 1, 'AVERAGE', 126.85, 'TAXABLE', 97, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(326, 'P000316', '夏日燕麥奶聖代 P000316', 'Occaecati neque quaerat repellat ut ut. Consequatur modi error velit est laudantium sit. Id iste facere illo consequuntur. Ut neque quasi omnis error.', 2, 2, 1, 1, 'AVERAGE', 66.47, 'TAXABLE', 14, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(327, 'P000317', '經典燕麥奶聖代 P000317', 'Dolor asperiores aut excepturi et. Autem nostrum quos non.', 8, 3, 1, 1, 'AVERAGE', 213.18, 'TAXABLE', 21, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(328, 'P000318', '雪藏海鹽焦糖冰棒 P000318', 'Voluptatem similique iste non. Qui blanditiis omnis quia hic.', 4, 2, 1, 1, 'AVERAGE', 119.55, 'TAXABLE', 45, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(329, 'P000319', '鮮果蘭姆葡萄冰棒 P000319', 'Magnam recusandae maiores. Eaque cupiditate ut omnis explicabo iusto. Illo delectus et natus.', 4, 6, 1, 1, 'AVERAGE', 161.91, 'TAXABLE', 94, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(330, 'P000320', '純濃香蕉雪糕 P000320', 'Qui fuga aut aut voluptate sit quo. Qui aut et pariatur sint.', 3, 6, 1, 1, 'AVERAGE', 147.28, 'TAXABLE', 69, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(331, 'P000321', '經典巧克力雪酪 P000321', 'Quia asperiores sed et perspiciatis magni beatae. Velit et dolor ratione maiores sint est.', 4, 4, 1, 1, 'AVERAGE', 201.54, 'TAXABLE', 23, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(332, 'P000322', '鮮果豆乳芝麻雪酪 P000322', 'Vero explicabo quasi voluptatem omnis atque incidunt. Iure dolorum iusto totam at fugit asperiores. Doloribus in natus quia nemo pariatur et ratione.', 7, 7, 1, 1, 'AVERAGE', 189.21, 'TAXABLE', 81, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(333, 'P000323', '雪藏抹茶聖代 P000323', 'Sed ratione maiores ducimus iste delectus. Consequuntur dolor ipsum illum quia. Consequatur illum nemo id. Voluptatibus quos modi explicabo tempora qui nostrum.', 9, 5, 1, 1, 'AVERAGE', 154.00, 'TAXABLE', 25, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(334, 'P000324', '雪藏香草聖代 P000324', 'Quis voluptates voluptatibus qui asperiores. Molestias est est blanditiis. Rem voluptates optio.', 4, 2, 1, 1, 'AVERAGE', 209.88, 'TAXABLE', 41, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(335, 'P000325', '莊園咖啡聖代 P000325', 'Animi numquam corrupti sit nulla officiis. Rerum dolorem aut quas cumque quidem sunt tempora.', 8, 3, 1, 1, 'AVERAGE', 279.64, 'TAXABLE', 73, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(336, 'P000326', '夏日抹茶聖代 P000326', 'Velit beatae sint occaecati ex facilis illum molestiae. In cupiditate qui explicabo minima. Quam maxime ipsa delectus ad. Eos id ullam consectetur.', 9, 5, 1, 1, 'AVERAGE', 245.06, 'TAXABLE', 44, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(337, 'P000327', '極致海鹽焦糖冰棒 P000327', 'Sit nihil molestias. Inventore rem fugiat commodi doloribus rerum.', 9, 3, 1, 1, 'AVERAGE', 245.20, 'TAXABLE', 13, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(338, 'P000328', '極致香蕉聖代 P000328', 'Maiores architecto vitae mollitia. Eos assumenda et nisi qui omnis voluptatem odit. Non itaque veniam. Laboriosam libero id.', 4, 3, 1, 1, 'AVERAGE', 277.61, 'TAXABLE', 19, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(339, 'P000329', '純濃咖啡冰淇淋 P000329', 'Tempore consequatur culpa distinctio ea ut dicta. Beatae consequatur velit hic eum qui corporis enim.', 7, 5, 1, 1, 'AVERAGE', 150.08, 'TAXABLE', 81, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(340, 'P000330', '鮮果燕麥奶冰棒 P000330', 'Exercitationem autem sequi natus vel nihil ipsa eos. In accusantium libero natus repellendus maxime dolor.', 5, 4, 1, 1, 'AVERAGE', 282.99, 'TAXABLE', 65, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(341, 'P000331', '雪藏巧克力雪酪 P000331', 'Iste dolorum at voluptatum dolor. Earum culpa rem libero aperiam.', 7, 6, 1, 1, 'AVERAGE', 341.05, 'TAXABLE', 62, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(342, 'P000332', '夏日蘭姆葡萄冰棒 P000332', 'Repudiandae odit asperiores quo. Ipsum dolores similique repellat maiores. Quo doloribus at veniam sunt tempore minima.', 3, 5, 1, 1, 'AVERAGE', 337.52, 'TAXABLE', 78, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(343, 'P000333', '鮮果巧克力冰棒 P000333', 'Qui optio fuga. Eius quibusdam earum. Aperiam reprehenderit similique dolor vitae.', 9, 7, 1, 1, 'AVERAGE', 102.71, 'TAXABLE', 99, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(344, 'P000334', '夏日抹茶聖代 P000334', 'Quia sit ratione. Eos dolores itaque ex cum delectus libero id. Ut voluptatem voluptatem alias. Tenetur voluptatem molestiae quod nam nemo quo.', 4, 5, 1, 1, 'AVERAGE', 200.33, 'TAXABLE', 24, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(345, 'P000335', '純濃薄荷巧克力冰淇淋 P000335', 'Eligendi consectetur quia soluta harum. Veniam inventore autem reprehenderit ea omnis et.', 4, 8, 1, 1, 'AVERAGE', 259.24, 'TAXABLE', 96, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(346, 'P000336', '極致豆乳芝麻冰淇淋 P000336', 'Similique ratione vero dolore sint qui non. Eaque omnis consectetur qui soluta eveniet voluptatem. Quo aut numquam.', 2, 7, 1, 1, 'AVERAGE', 284.57, 'TAXABLE', 26, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(347, 'P000337', '純濃草莓冰棒 P000337', 'Amet esse vel ducimus distinctio numquam. Ullam libero consequuntur enim cumque est. Sequi cum quo.', 6, 3, 1, 1, 'AVERAGE', 185.50, 'TAXABLE', 57, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(348, 'P000338', '雪藏香蕉冰棒 P000338', 'Quia natus quo. Aut est et laudantium veritatis. Et in qui nostrum sit et cupiditate. Ullam quibusdam voluptas.', 4, 5, 1, 1, 'AVERAGE', 236.85, 'TAXABLE', 79, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(349, 'P000339', '雪藏薄荷巧克力聖代 P000339', 'Autem eum qui maxime consequatur aliquam. Necessitatibus in laborum.', 6, 3, 1, 1, 'AVERAGE', 338.76, 'TAXABLE', 70, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(350, 'P000340', '純濃薄荷巧克力冰淇淋 P000340', 'Adipisci officia amet. Eos cum eligendi exercitationem.', 9, 7, 1, 1, 'AVERAGE', 242.41, 'TAXABLE', 88, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(351, 'P000341', '雪藏蘭姆葡萄冰淇淋 P000341', 'Officiis ut at maiores. Error voluptates officia ipsa.', 8, 2, 1, 1, 'AVERAGE', 259.43, 'TAXABLE', 40, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(352, 'P000342', '夏日燕麥奶雪酪 P000342', 'Sed iste dolore voluptas culpa aut. Quo quo est atque quae officiis aut id. In omnis odit accusantium est et sint inventore.', 7, 6, 1, 1, 'AVERAGE', 87.77, 'TAXABLE', 43, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(353, 'P000343', '夏日花生雪糕 P000343', 'Sequi ut occaecati harum. Sed et ut eum dolore sequi rem. Corporis sit est ut qui temporibus qui. Eos eos sit vel.', 7, 8, 1, 1, 'AVERAGE', 258.61, 'TAXABLE', 90, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(354, 'P000344', '莊園海鹽焦糖雪酪 P000344', 'Et et aliquam beatae qui consequatur. Nihil voluptas veniam eveniet labore dolorem. Explicabo accusamus asperiores quibusdam blanditiis libero sequi rerum.', 4, 7, 1, 1, 'AVERAGE', 320.82, 'TAXABLE', 60, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(355, 'P000345', '經典燕麥奶雪酪 P000345', 'Neque in inventore aut enim cumque. In rerum fugiat dolores assumenda cumque quis quis. Qui veritatis aut sit ut itaque. Saepe quos ut sit aut.', 5, 2, 1, 1, 'AVERAGE', 209.67, 'TAXABLE', 45, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(356, 'P000346', '經典咖啡冰淇淋 P000346', 'Magni ut architecto sunt omnis soluta quia. Doloremque quaerat sunt. Laudantium dolore quasi sint repellat. Voluptas fuga voluptas et et.', 8, 8, 1, 1, 'AVERAGE', 282.46, 'TAXABLE', 68, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(357, 'P000347', '鮮果巧克力冰棒 P000347', 'Et eos est reiciendis cum. Non est rerum ipsum quo consectetur velit. Omnis commodi sapiente officiis in molestias. Illum nesciunt non ipsum nemo laboriosam veritatis distinctio.', 3, 7, 1, 1, 'AVERAGE', 220.62, 'TAXABLE', 27, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(358, 'P000348', '莊園燕麥奶聖代 P000348', 'Tempora voluptatem molestiae sit dolorem. Magnam enim quisquam et ut est et.', 4, 5, 1, 1, 'AVERAGE', 198.17, 'TAXABLE', 61, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(359, 'P000349', '純濃芒果優格冰棒 P000349', 'Sed aspernatur cupiditate ut iusto asperiores modi. Iusto illo ea voluptatem ut sapiente. Perspiciatis iure debitis est iure.', 4, 6, 1, 1, 'AVERAGE', 243.30, 'TAXABLE', 22, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(360, 'P000350', '純濃芒果優格冰棒 P000350', 'A placeat illo eaque sunt neque dolorem. Ab aut aut ex ea numquam architecto. Explicabo et animi laudantium nisi aut. Ut tempora similique rerum hic.', 4, 7, 1, 1, 'AVERAGE', 215.25, 'TAXABLE', 70, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(361, 'P000351', '純濃藍莓聖代 P000351', 'Aut nisi itaque quo facilis. Earum exercitationem doloremque et illum quaerat qui sit. Et sapiente rerum fugit asperiores rerum.', 2, 7, 1, 1, 'AVERAGE', 127.51, 'TAXABLE', 74, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(362, 'P000352', '純濃抹茶聖代 P000352', 'Ea in hic corrupti. Non aut voluptate et earum.', 6, 8, 1, 1, 'AVERAGE', 82.21, 'TAXABLE', 14, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(363, 'P000353', '雪藏咖啡冰淇淋 P000353', 'Et quibusdam in. Et molestiae saepe quod sit.', 6, 8, 1, 1, 'AVERAGE', 130.05, 'TAXABLE', 61, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(364, 'P000354', '雪藏燕麥奶冰淇淋 P000354', 'Autem quasi exercitationem ut non. Quidem repellendus voluptas fugit ut voluptate. Repudiandae qui excepturi debitis et autem consequatur et. Nihil exercitationem amet velit autem sapiente.', 8, 5, 1, 1, 'AVERAGE', 51.02, 'TAXABLE', 47, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(365, 'P000355', '莊園草莓冰淇淋 P000355', 'Porro autem ut et fugit odit nulla fugiat. Eum nihil debitis praesentium. Rem facere qui. Aliquid rerum aliquam aperiam.', 8, 3, 1, 1, 'AVERAGE', 261.80, 'TAXABLE', 17, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(366, 'P000356', '鮮果燕麥奶冰棒 P000356', 'Distinctio soluta nisi fugiat et quia sit. Sint ut repudiandae repellat. Eius facere deleniti.', 6, 8, 1, 1, 'AVERAGE', 309.83, 'TAXABLE', 66, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(367, 'P000357', '莊園藍莓雪酪 P000357', 'Doloribus sit rerum voluptatibus quisquam nihil. Laborum corporis quia praesentium impedit.', 9, 7, 1, 1, 'AVERAGE', 163.58, 'TAXABLE', 29, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(368, 'P000358', '極致花生冰棒 P000358', 'Quia perspiciatis ullam quidem. Voluptate rerum corrupti.', 9, 7, 1, 1, 'AVERAGE', 343.94, 'TAXABLE', 95, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(369, 'P000359', '純濃香草冰棒 P000359', 'Accusantium similique rerum ratione temporibus autem. Laboriosam dolorum enim atque temporibus omnis excepturi optio. Occaecati voluptas ducimus ea amet accusantium. Sit minus accusamus ducimus dolorum ratione expedita voluptates.', 6, 4, 1, 1, 'AVERAGE', 62.31, 'TAXABLE', 41, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(370, 'P000360', '雪藏草莓雪酪 P000360', 'Sed ea dolorem. Porro nihil qui ducimus aliquam. Iste sequi qui et pariatur dicta facere. Quod vero aut quod inventore perspiciatis facere aliquid.', 6, 6, 1, 1, 'AVERAGE', 244.59, 'TAXABLE', 73, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(371, 'P000361', '莊園香蕉雪酪 P000361', 'Ab facere provident officiis odio quas. Quia dicta unde et quo quis voluptas in.', 7, 4, 1, 1, 'AVERAGE', 294.58, 'TAXABLE', 97, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(372, 'P000362', '極致抹茶雪糕 P000362', 'Molestiae non quis ad illum. Eos voluptas tempora. Dolorem quia incidunt. Id rerum consequatur dolor.', 2, 5, 1, 1, 'AVERAGE', 313.12, 'TAXABLE', 92, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(373, 'P000363', '經典海鹽焦糖雪酪 P000363', 'Voluptatem consequatur consequatur dolorem qui quia voluptas. Incidunt id eum porro qui. Earum maiores molestiae debitis vel sint. Sed ea repellat iure iure voluptatum quam aut.', 2, 2, 1, 1, 'AVERAGE', 82.29, 'TAXABLE', 33, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1);
INSERT INTO `product` (`product_id`, `product_code`, `name`, `description`, `category_id`, `unit_id`, `is_purchasable`, `is_salable`, `cost_method`, `base_price`, `tax_type`, `safety_stock_quantity`, `min_order_quantity`, `max_order_quantity`, `lead_time_days`, `is_active`, `remarks`, `created_at`, `created_by`, `updated_at`, `updated_by`) VALUES
(374, 'P000364', '夏日香蕉冰淇淋 P000364', 'Perferendis porro pariatur ut. Mollitia architecto non impedit sit dolor ipsum. Aspernatur nobis vel consequuntur. Deleniti similique nostrum maiores nesciunt minus optio.', 3, 2, 1, 1, 'AVERAGE', 49.48, 'TAXABLE', 78, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(375, 'P000365', '純濃藍莓冰棒 P000365', 'Rerum quo nostrum aut et impedit. Minima perferendis mollitia ea minus et.', 6, 4, 1, 1, 'AVERAGE', 94.39, 'TAXABLE', 81, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(376, 'P000366', '雪藏OREO雪糕 P000366', 'Nihil in animi est corporis veritatis voluptates. Occaecati aut amet in.', 4, 2, 1, 1, 'AVERAGE', 76.18, 'TAXABLE', 60, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(377, 'P000367', '雪藏花生聖代 P000367', 'Voluptatem fugit laudantium dolorum maiores consequatur. Consectetur veniam sunt.', 5, 5, 1, 1, 'AVERAGE', 249.07, 'TAXABLE', 29, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(378, 'P000368', '極致香蕉雪酪 P000368', 'Dolorum adipisci est libero quia ut. Modi minus ut eos deserunt officiis. Ipsum iusto voluptas et quia repellendus sunt.', 3, 3, 1, 1, 'AVERAGE', 111.83, 'TAXABLE', 64, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(379, 'P000369', '純濃花生雪糕 P000369', 'Perspiciatis architecto sed ducimus consequatur perspiciatis corrupti. Blanditiis nemo asperiores fugiat est voluptatem non qui. Sit vero labore iure vero veniam ipsam voluptatem.', 7, 7, 1, 1, 'AVERAGE', 280.04, 'TAXABLE', 61, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(380, 'P000370', '極致豆乳芝麻冰淇淋 P000370', 'Veritatis quo in consectetur. Et natus delectus consectetur non quae quibusdam eveniet.', 3, 7, 1, 1, 'AVERAGE', 91.52, 'TAXABLE', 71, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(381, 'P000371', '鮮果藍莓雪酪 P000371', 'Occaecati harum perspiciatis sunt voluptas non eum. Dolores necessitatibus enim voluptatem rerum quia nihil. Nisi ut voluptas doloremque.', 3, 2, 1, 1, 'AVERAGE', 252.13, 'TAXABLE', 48, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(382, 'P000372', '雪藏薄荷巧克力冰棒 P000372', 'Iusto quisquam rerum voluptate. Qui minima quasi.', 6, 7, 1, 1, 'AVERAGE', 188.67, 'TAXABLE', 57, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(383, 'P000373', '鮮果香蕉聖代 P000373', 'Minima cupiditate deserunt. Iusto quam earum voluptatem deserunt vero. Quis soluta non aut neque accusamus consequatur qui. Nemo et ut quaerat.', 6, 8, 1, 1, 'AVERAGE', 218.27, 'TAXABLE', 61, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(384, 'P000374', '鮮果香草聖代 P000374', 'Id ullam veritatis. Animi nihil est libero necessitatibus saepe sit.', 2, 7, 1, 1, 'AVERAGE', 89.79, 'TAXABLE', 56, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(385, 'P000375', '莊園藍莓聖代 P000375', 'Rerum laboriosam repellendus porro dolores sunt. Modi omnis molestiae velit. Nam officiis aut molestiae fuga. Adipisci quaerat unde.', 3, 2, 1, 1, 'AVERAGE', 299.22, 'TAXABLE', 84, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(386, 'P000376', '夏日藍莓冰棒 P000376', 'Fugiat ipsam aperiam rerum. Asperiores incidunt sed aliquid omnis assumenda. Recusandae officia ut. Eligendi rerum assumenda culpa.', 4, 6, 1, 1, 'AVERAGE', 83.63, 'TAXABLE', 27, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(387, 'P000377', '極致咖啡雪酪 P000377', 'Repellendus a minus unde et quia. Rerum iusto delectus earum. Voluptatem aliquid omnis omnis a blanditiis.', 3, 5, 1, 1, 'AVERAGE', 228.46, 'TAXABLE', 11, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(388, 'P000378', '極致草莓雪糕 P000378', 'Nostrum vel eos facilis. Dolorum asperiores amet ducimus. Dolor voluptatem facilis est est minus.', 5, 8, 1, 1, 'AVERAGE', 182.82, 'TAXABLE', 24, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(389, 'P000379', '莊園巧克力雪糕 P000379', 'Libero omnis dolor facilis aut rerum. Velit ea delectus in asperiores rerum quaerat voluptas. Quia quidem aut sint qui exercitationem aut modi.', 9, 2, 1, 1, 'AVERAGE', 280.34, 'TAXABLE', 87, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(390, 'P000380', '極致OREO雪酪 P000380', 'Eos doloribus sed. Nostrum ut necessitatibus perferendis. Sed voluptas dolor dolorum velit quisquam. Voluptas non sed consequatur sed accusantium.', 4, 7, 1, 1, 'AVERAGE', 242.86, 'TAXABLE', 95, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(391, 'P000381', '鮮果燕麥奶冰棒 P000381', 'Rerum id consequuntur molestiae voluptatem est incidunt. Et voluptate repudiandae ut dolorum velit corporis maiores. Libero enim odit fugit.', 2, 3, 1, 1, 'AVERAGE', 312.06, 'TAXABLE', 76, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(392, 'P000382', '純濃燕麥奶雪糕 P000382', 'Et possimus provident molestiae. Corrupti nihil nesciunt sed maxime vel eum ut.', 7, 7, 1, 1, 'AVERAGE', 184.85, 'TAXABLE', 91, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(393, 'P000383', '經典香蕉雪酪 P000383', 'Architecto totam et aliquid aliquam quaerat enim. Commodi sit inventore nostrum optio. Odit ut architecto fugiat culpa.', 7, 5, 1, 1, 'AVERAGE', 88.95, 'TAXABLE', 45, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(394, 'P000384', '夏日芒果優格雪糕 P000384', 'Repellat atque et voluptates est. Quas consequuntur omnis itaque libero et sed. Aut ullam dolore enim omnis neque qui at. Voluptatem cumque provident adipisci qui nihil fugit.', 5, 6, 1, 1, 'AVERAGE', 293.42, 'TAXABLE', 99, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(395, 'P000385', '極致薄荷巧克力雪糕 P000385', 'Error voluptas quis deleniti. Quasi blanditiis quis laborum omnis ipsum molestiae.', 4, 7, 1, 1, 'AVERAGE', 340.07, 'TAXABLE', 77, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(396, 'P000386', '極致芒果優格雪酪 P000386', 'Maxime qui nobis voluptate quaerat ut. Aliquid quis omnis assumenda officiis impedit maiores. Doloremque ipsa hic voluptas et est. Ea sint et ut.', 6, 2, 1, 1, 'AVERAGE', 237.40, 'TAXABLE', 51, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(397, 'P000387', '純濃蘭姆葡萄雪酪 P000387', 'Odio modi distinctio saepe aut laboriosam eos quis. Alias itaque sit dolores nemo.', 6, 2, 1, 1, 'AVERAGE', 302.46, 'TAXABLE', 61, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(398, 'P000388', '夏日芒果優格聖代 P000388', 'Repellat inventore consectetur cum aut pariatur consectetur tempore. Voluptatibus quis quae accusamus aliquid beatae beatae ratione.', 7, 7, 1, 1, 'AVERAGE', 292.15, 'TAXABLE', 15, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(399, 'P000389', '純濃草莓雪酪 P000389', 'Voluptas sunt blanditiis est reiciendis magnam soluta laudantium. Neque blanditiis est ut delectus. Quae debitis quis adipisci perferendis ut dolorem eos.', 5, 2, 1, 1, 'AVERAGE', 173.34, 'TAXABLE', 90, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(400, 'P000390', '經典香蕉雪糕 P000390', 'Quas et ut nulla eveniet consequatur enim error. Est distinctio ut.', 5, 5, 1, 1, 'AVERAGE', 212.21, 'TAXABLE', 12, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(401, 'P000391', '雪藏芒果優格雪酪 P000391', 'Numquam omnis est voluptatem. Laboriosam praesentium sint quisquam dolore recusandae et. Ut perspiciatis sed ut.', 8, 5, 1, 1, 'AVERAGE', 216.50, 'TAXABLE', 20, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(402, 'P000392', '夏日豆乳芝麻雪糕 P000392', 'Maiores qui quo praesentium dolores possimus iusto. Veniam adipisci incidunt. Sapiente porro officiis. Necessitatibus maxime assumenda dolores.', 9, 3, 1, 1, 'AVERAGE', 90.99, 'TAXABLE', 24, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(403, 'P000393', '極致草莓冰棒 P000393', 'Cumque ducimus inventore voluptatem corporis fugit inventore dolorem. Doloremque nihil voluptatem dolores aperiam omnis sed. Quod iusto omnis aspernatur eos.', 2, 7, 1, 1, 'AVERAGE', 265.38, 'TAXABLE', 60, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(404, 'P000394', '純濃芒果優格雪酪 P000394', 'Dicta molestiae quia mollitia ab non. A consequatur natus delectus sapiente eveniet id odit.', 8, 8, 1, 1, 'AVERAGE', 151.56, 'TAXABLE', 27, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(405, 'P000395', '莊園燕麥奶冰淇淋 P000395', 'Numquam nihil consequuntur et recusandae. Eos non explicabo perspiciatis deserunt.', 8, 7, 1, 1, 'AVERAGE', 123.48, 'TAXABLE', 38, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(406, 'P000396', '雪藏海鹽焦糖冰棒 P000396', 'Alias aut et labore est. Et maxime laborum.', 4, 8, 1, 1, 'AVERAGE', 124.77, 'TAXABLE', 24, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(407, 'P000397', '極致抹茶聖代 P000397', 'Et eligendi qui. Voluptas earum inventore vel sed.', 8, 4, 1, 1, 'AVERAGE', 150.02, 'TAXABLE', 56, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(408, 'P000398', '鮮果薄荷巧克力雪酪 P000398', 'Necessitatibus exercitationem sunt corporis rem iure. Aut quia dolores. Itaque repudiandae quasi aut asperiores eum vero in. Voluptas non minima est ratione voluptatem corrupti.', 5, 5, 1, 1, 'AVERAGE', 137.63, 'TAXABLE', 62, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(409, 'P000399', '夏日花生冰淇淋 P000399', 'Debitis excepturi asperiores quibusdam voluptas. Id aliquam consectetur. Optio non aut repudiandae saepe dolores labore. Debitis mollitia quaerat.', 8, 6, 1, 1, 'AVERAGE', 113.65, 'TAXABLE', 54, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(410, 'P000400', '純濃薄荷巧克力雪糕 P000400', 'Et omnis odio. Corporis consequatur aut saepe et voluptatem.', 2, 2, 1, 1, 'AVERAGE', 173.20, 'TAXABLE', 52, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(411, 'P000401', '純濃咖啡雪糕 P000401', 'Ut quod id corrupti sequi asperiores tempora. In eligendi voluptates eum tenetur.', 7, 5, 1, 1, 'AVERAGE', 89.57, 'TAXABLE', 30, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(412, 'P000402', '夏日OREO冰淇淋 P000402', 'Voluptatem minus corrupti architecto vel quam. Velit aspernatur sapiente aut.', 8, 3, 1, 1, 'AVERAGE', 79.78, 'TAXABLE', 95, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(413, 'P000403', '莊園OREO聖代 P000403', 'Tempore reprehenderit natus a quisquam consequatur velit. Aliquid similique magnam est dolorem aut aut tempora. Possimus corporis sed perspiciatis qui et soluta cupiditate.', 8, 6, 1, 1, 'AVERAGE', 230.40, 'TAXABLE', 58, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(414, 'P000404', '夏日香草冰棒 P000404', 'Dignissimos et hic vitae dolorum aut. Expedita illo et rerum fugit. Accusamus dicta natus aspernatur voluptas.', 2, 7, 1, 1, 'AVERAGE', 203.66, 'TAXABLE', 63, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(415, 'P000405', '莊園薄荷巧克力聖代 P000405', 'Sit ipsum odit nam qui laboriosam. Voluptatum fugit illo laborum harum mollitia dolorum rerum.', 8, 8, 1, 1, 'AVERAGE', 142.83, 'TAXABLE', 58, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(416, 'P000406', '夏日咖啡冰淇淋 P000406', 'Consequatur temporibus sed. Saepe harum in eius ut. Quas sit et fugiat adipisci corrupti. Doloribus voluptas tempore.', 6, 7, 1, 1, 'AVERAGE', 96.18, 'TAXABLE', 70, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(417, 'P000407', '純濃豆乳芝麻冰棒 P000407', 'Vel corrupti pariatur dolore molestiae et cupiditate. Eligendi minus non ea qui voluptatem dolor fuga.', 4, 6, 1, 1, 'AVERAGE', 86.26, 'TAXABLE', 94, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(418, 'P000408', '雪藏抹茶雪酪 P000408', 'At et non non. Consequatur nesciunt aut error.', 3, 6, 1, 1, 'AVERAGE', 267.00, 'TAXABLE', 89, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(419, 'P000409', '極致抹茶冰棒 P000409', 'Molestiae ab velit repudiandae necessitatibus. Voluptatum est sapiente. Aperiam culpa laudantium quos.', 9, 5, 1, 1, 'AVERAGE', 155.89, 'TAXABLE', 10, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(420, 'P000410', '純濃抹茶聖代 P000410', 'Omnis nihil tenetur velit consequatur. Voluptas aut doloremque omnis similique et dolores.', 4, 4, 1, 1, 'AVERAGE', 149.76, 'TAXABLE', 56, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(421, 'P000411', '純濃抹茶雪糕 P000411', 'Possimus error modi. Nemo est numquam. Cum consequatur sapiente aut explicabo. Qui eum id consectetur quaerat illo.', 8, 4, 1, 1, 'AVERAGE', 198.75, 'TAXABLE', 86, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(422, 'P000412', '極致草莓聖代 P000412', 'Veniam quaerat eum. Et cupiditate sed mollitia non cupiditate.', 2, 2, 1, 1, 'AVERAGE', 307.70, 'TAXABLE', 42, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(423, 'P000413', '極致咖啡雪酪 P000413', 'Aliquam recusandae vitae molestias quisquam suscipit necessitatibus eius. Repellat atque fuga. Veritatis libero vel ex perferendis.', 7, 4, 1, 1, 'AVERAGE', 235.61, 'TAXABLE', 14, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(424, 'P000414', '經典燕麥奶雪糕 P000414', 'Doloremque earum sed adipisci iste dolor dolorem. Facilis reiciendis nisi iusto aliquam ipsam accusamus magni. Et omnis nobis enim sint facilis. Minus sit est.', 8, 6, 1, 1, 'AVERAGE', 176.82, 'TAXABLE', 45, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(425, 'P000415', '極致海鹽焦糖雪酪 P000415', 'Molestias hic in. Cupiditate quia a provident.', 3, 3, 1, 1, 'AVERAGE', 294.49, 'TAXABLE', 22, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(426, 'P000416', '莊園豆乳芝麻冰淇淋 P000416', 'Voluptatum consectetur magnam eum deserunt. Ut facere animi ipsam quia maxime. Quos eveniet explicabo non et harum qui nulla.', 2, 7, 1, 1, 'AVERAGE', 346.68, 'TAXABLE', 36, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(427, 'P000417', '莊園香蕉冰棒 P000417', 'Laboriosam dignissimos ratione sed sed cum optio repellat. Officia doloremque eius dignissimos perspiciatis assumenda. Doloremque magni natus. Numquam eius ut accusamus tempore illo sunt.', 3, 3, 1, 1, 'AVERAGE', 184.92, 'TAXABLE', 77, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(428, 'P000418', '鮮果草莓雪酪 P000418', 'Ipsam similique vero. Consequatur exercitationem esse delectus expedita et accusantium aliquam. Non harum aut occaecati.', 6, 7, 1, 1, 'AVERAGE', 322.85, 'TAXABLE', 74, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(429, 'P000419', '經典藍莓雪酪 P000419', 'Laboriosam in eaque adipisci illum blanditiis quae. Aut et eligendi suscipit commodi.', 3, 3, 1, 1, 'AVERAGE', 151.23, 'TAXABLE', 77, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(430, 'P000420', '夏日海鹽焦糖雪糕 P000420', 'Occaecati libero mollitia quibusdam et velit porro veniam. Aut sed voluptatem quis ut illum laboriosam.', 8, 3, 1, 1, 'AVERAGE', 168.78, 'TAXABLE', 42, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(431, 'P000421', '極致草莓冰淇淋 P000421', 'Magni ea corporis velit. Doloremque corrupti tempora. Unde temporibus voluptatem ut enim.', 5, 5, 1, 1, 'AVERAGE', 160.91, 'TAXABLE', 68, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(432, 'P000422', '經典薄荷巧克力聖代 P000422', 'Explicabo non tempore repellat. Amet dolores cum.', 8, 3, 1, 1, 'AVERAGE', 58.93, 'TAXABLE', 64, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(433, 'P000423', '經典藍莓雪酪 P000423', 'Ipsa tempore aperiam autem hic ratione. Quod fugit ab. In maxime nihil provident vitae autem ea.', 8, 4, 1, 1, 'AVERAGE', 87.11, 'TAXABLE', 90, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(434, 'P000424', '夏日花生聖代 P000424', 'Quam quas sit tenetur. Minus eius consequatur dolor molestiae et.', 4, 7, 1, 1, 'AVERAGE', 246.39, 'TAXABLE', 86, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(435, 'P000425', '經典豆乳芝麻聖代 P000425', 'Dolores consequatur et vel et qui soluta laudantium. Ea explicabo ut.', 9, 4, 1, 1, 'AVERAGE', 305.33, 'TAXABLE', 33, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(436, 'P000426', '雪藏咖啡雪酪 P000426', 'Et id expedita vel. Et ut quae et amet. Quam fuga quis eos. Quae a non rerum.', 3, 4, 1, 1, 'AVERAGE', 109.48, 'TAXABLE', 73, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(437, 'P000427', '雪藏草莓聖代 P000427', 'Asperiores veniam rem iure itaque omnis. Inventore perspiciatis hic. Dolores voluptatem vel vel id. Asperiores quod vel.', 6, 8, 1, 1, 'AVERAGE', 171.75, 'TAXABLE', 64, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(438, 'P000428', '鮮果香蕉冰棒 P000428', 'Quae odit voluptatem fugit est sit alias. Omnis dolores quia.', 4, 8, 1, 1, 'AVERAGE', 147.15, 'TAXABLE', 90, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(439, 'P000429', '純濃薄荷巧克力冰淇淋 P000429', 'Provident sint animi aspernatur. Id voluptatibus quam.', 5, 6, 1, 1, 'AVERAGE', 314.87, 'TAXABLE', 60, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(440, 'P000430', '極致薄荷巧克力聖代 P000430', 'Qui quia ducimus incidunt. Et voluptatem molestiae et occaecati nobis. Commodi ipsa voluptas similique.', 9, 6, 1, 1, 'AVERAGE', 260.71, 'TAXABLE', 74, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(441, 'P000431', '鮮果抹茶雪糕 P000431', 'Maxime facilis corporis iste itaque ad incidunt. Ex sed ab nihil corrupti. Tenetur quis deserunt quaerat perferendis et fuga quas. Et aperiam dignissimos officia quia quibusdam alias consectetur.', 6, 3, 1, 1, 'AVERAGE', 240.37, 'TAXABLE', 17, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(442, 'P000432', '莊園薄荷巧克力雪糕 P000432', 'Sint vel velit. Quia rerum ut aut voluptas vel dolorem. Assumenda aliquid aut fuga.', 9, 8, 1, 1, 'AVERAGE', 210.56, 'TAXABLE', 82, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(443, 'P000433', '經典香草聖代 P000433', 'Ipsa reprehenderit magni laboriosam veritatis. Nisi facilis eos vel non.', 7, 8, 1, 1, 'AVERAGE', 158.66, 'TAXABLE', 90, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(444, 'P000434', '純濃海鹽焦糖冰棒 P000434', 'Aut commodi id voluptatum enim et laborum. Possimus iure et quaerat accusantium deleniti.', 3, 7, 1, 1, 'AVERAGE', 74.60, 'TAXABLE', 71, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(445, 'P000435', '雪藏海鹽焦糖雪酪 P000435', 'Dolor odio quo aperiam. Vel dolor numquam et provident. Blanditiis error ut voluptatem.', 9, 4, 1, 1, 'AVERAGE', 322.76, 'TAXABLE', 92, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(446, 'P000436', '鮮果巧克力雪糕 P000436', 'Omnis et asperiores molestiae optio accusamus incidunt. Quae voluptatibus ab quibusdam placeat ducimus.', 4, 7, 1, 1, 'AVERAGE', 175.22, 'TAXABLE', 38, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(447, 'P000437', '莊園咖啡雪酪 P000437', 'Saepe fuga nesciunt eum a harum doloremque. Dolorem ut et nihil ad consequatur unde. Aut officia aspernatur neque quia. Dolores sunt quibusdam est et numquam voluptatem labore.', 8, 8, 1, 1, 'AVERAGE', 70.61, 'TAXABLE', 49, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(448, 'P000438', '經典抹茶雪糕 P000438', 'Rerum in sit eligendi voluptate ut voluptate explicabo. Delectus non aliquam dignissimos delectus. Mollitia facilis qui. Sunt omnis non temporibus illo officia.', 7, 4, 1, 1, 'AVERAGE', 262.01, 'TAXABLE', 46, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(449, 'P000439', '經典蘭姆葡萄聖代 P000439', 'Accusamus ipsa excepturi odit eveniet nam est aut. Ea accusamus eligendi quia aut.', 6, 4, 1, 1, 'AVERAGE', 340.78, 'TAXABLE', 51, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(450, 'P000440', '夏日海鹽焦糖雪酪 P000440', 'Aut dignissimos incidunt officia consectetur non dolores. Est mollitia facere omnis eum. Provident ipsa quae enim cupiditate.', 7, 5, 1, 1, 'AVERAGE', 89.97, 'TAXABLE', 10, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(451, 'P000441', '莊園藍莓聖代 P000441', 'Autem qui et reprehenderit at fugit laboriosam cumque. Suscipit rerum pariatur est aperiam eaque maiores porro. Id magnam non.', 4, 6, 1, 1, 'AVERAGE', 71.35, 'TAXABLE', 82, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(452, 'P000442', '純濃香蕉聖代 P000442', 'Illum est ducimus ipsum sapiente nesciunt tenetur ipsa. Porro voluptas atque est voluptas eos. Nam est aut necessitatibus officia iusto rerum. Rerum molestiae autem sit.', 3, 3, 1, 1, 'AVERAGE', 178.24, 'TAXABLE', 85, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(453, 'P000443', '雪藏蘭姆葡萄冰棒 P000443', 'Voluptatum dolore est et consequatur. Omnis et dolores. Illum quis est. Facilis voluptas sit sapiente quo velit unde.', 7, 2, 1, 1, 'AVERAGE', 153.99, 'TAXABLE', 40, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(454, 'P000444', '鮮果香草冰棒 P000444', 'Ut similique modi eos earum voluptatibus. Atque sint minima.', 5, 8, 1, 1, 'AVERAGE', 256.93, 'TAXABLE', 40, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(455, 'P000445', '鮮果藍莓雪糕 P000445', 'Eum qui modi officia sit. Laborum maxime quas facilis et.', 9, 4, 1, 1, 'AVERAGE', 293.58, 'TAXABLE', 34, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(456, 'P000446', '極致香草聖代 P000446', 'Voluptatum eaque doloremque harum laboriosam minus enim. Rem dolor et sint nam. Enim blanditiis id laudantium amet maxime dolor. Qui consequatur cum aspernatur beatae repellendus.', 7, 7, 1, 1, 'AVERAGE', 134.97, 'TAXABLE', 64, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(457, 'P000447', '純濃蘭姆葡萄雪酪 P000447', 'Et distinctio commodi. Rem ducimus ipsam repellendus commodi. Nihil facilis adipisci ab.', 4, 6, 1, 1, 'AVERAGE', 275.31, 'TAXABLE', 90, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(458, 'P000448', '極致巧克力雪糕 P000448', 'Non fugiat quia nihil at sint. Quia magnam perferendis labore. Voluptatem fuga corrupti iusto et. Praesentium minus ut.', 4, 2, 1, 1, 'AVERAGE', 266.15, 'TAXABLE', 66, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(459, 'P000449', '經典咖啡冰棒 P000449', 'Qui sed doloremque alias nihil labore ullam. Consectetur et harum aspernatur non sint repellat.', 2, 2, 1, 1, 'AVERAGE', 247.69, 'TAXABLE', 98, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(460, 'P000450', '夏日香蕉雪酪 P000450', 'Quia libero enim. Est ratione iusto.', 6, 8, 1, 1, 'AVERAGE', 180.61, 'TAXABLE', 96, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(461, 'P000451', '鮮果藍莓冰淇淋 P000451', 'Et laudantium ut quo est nulla corporis. Corrupti veritatis ipsam et ea et eveniet deserunt. Suscipit at sunt et. Quo eum nostrum est est totam velit culpa.', 4, 5, 1, 1, 'AVERAGE', 209.24, 'TAXABLE', 70, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(462, 'P000452', '夏日薄荷巧克力雪酪 P000452', 'Accusantium aspernatur dolorem sunt laborum rerum harum. Repellendus sint a laudantium aut aut.', 9, 4, 1, 1, 'AVERAGE', 135.68, 'TAXABLE', 69, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(463, 'P000453', '鮮果燕麥奶雪糕 P000453', 'Ad dolor et illum laborum tenetur. Quis et non sint non reiciendis.', 7, 5, 1, 1, 'AVERAGE', 324.92, 'TAXABLE', 76, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(464, 'P000454', '極致巧克力冰棒 P000454', 'Soluta distinctio voluptas voluptates. Molestias iste ut rerum iusto aut. Quisquam enim veniam est commodi ut a aut. Quo itaque voluptatem cumque doloribus qui.', 4, 7, 1, 1, 'AVERAGE', 270.29, 'TAXABLE', 44, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(465, 'P000455', '雪藏豆乳芝麻冰棒 P000455', 'Quae tempore nihil assumenda quo. Qui recusandae tempora. Quos aut distinctio cumque. Officiis occaecati ut et eos aut natus quia.', 5, 6, 1, 1, 'AVERAGE', 178.72, 'TAXABLE', 79, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(466, 'P000456', '莊園薄荷巧克力聖代 P000456', 'Numquam omnis et. Ullam tempora qui sunt et.', 7, 3, 1, 1, 'AVERAGE', 299.99, 'TAXABLE', 64, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(467, 'P000457', '莊園燕麥奶聖代 P000457', 'Porro fuga pariatur quia. Quae et vero sed et aperiam.', 6, 8, 1, 1, 'AVERAGE', 233.66, 'TAXABLE', 93, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(468, 'P000458', '極致咖啡冰淇淋 P000458', 'Ullam eos maxime explicabo fugiat ab. Aut ut aliquam deserunt labore et. Est necessitatibus et tenetur voluptas et.', 2, 3, 1, 1, 'AVERAGE', 168.32, 'TAXABLE', 78, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(469, 'P000459', '極致OREO聖代 P000459', 'Molestiae ut iure neque et. Dolorem est accusamus.', 9, 6, 1, 1, 'AVERAGE', 107.35, 'TAXABLE', 71, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(470, 'P000460', '經典香草冰淇淋 P000460', 'Voluptatem consequatur molestias aliquid quod nam sint amet. Reiciendis voluptates reprehenderit accusantium distinctio sed. Quos sit commodi ad earum natus ratione recusandae. Velit ea possimus minima.', 7, 2, 1, 1, 'AVERAGE', 45.98, 'TAXABLE', 80, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(471, 'P000461', '極致巧克力冰棒 P000461', 'Rerum dolor suscipit quia sit eum non officia. Vitae nihil quis expedita vel illum cum qui.', 6, 8, 1, 1, 'AVERAGE', 317.96, 'TAXABLE', 15, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(472, 'P000462', '雪藏花生雪糕 P000462', 'Consequuntur eum dolorem aut nulla nobis temporibus odio. Voluptas excepturi id voluptatem laborum.', 4, 3, 1, 1, 'AVERAGE', 201.36, 'TAXABLE', 74, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(473, 'P000463', '經典芒果優格聖代 P000463', 'Ut porro et qui ut odit in omnis. Necessitatibus quae numquam non laborum quas perspiciatis.', 5, 6, 1, 1, 'AVERAGE', 111.97, 'TAXABLE', 85, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(474, 'P000464', '雪藏香蕉聖代 P000464', 'Quidem porro amet aut. Voluptatibus dignissimos sed. Consectetur ea non voluptatem eum architecto maiores. Corrupti consequatur inventore hic.', 4, 6, 1, 1, 'AVERAGE', 152.67, 'TAXABLE', 24, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(475, 'P000465', '經典燕麥奶雪糕 P000465', 'Id voluptate placeat. Molestias dolor adipisci et itaque consequuntur.', 6, 4, 1, 1, 'AVERAGE', 161.03, 'TAXABLE', 11, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(476, 'P000466', '鮮果香蕉冰棒 P000466', 'Similique aut non. Ratione temporibus quia neque dolorem asperiores. Alias ab beatae tempora consequatur. Exercitationem non nihil impedit aut voluptatibus.', 9, 6, 1, 1, 'AVERAGE', 80.69, 'TAXABLE', 53, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(477, 'P000467', '鮮果海鹽焦糖雪酪 P000467', 'Voluptatem non provident nihil aperiam aut fuga. Consequatur delectus praesentium possimus sint nulla omnis sapiente. Autem illum est culpa omnis.', 5, 7, 1, 1, 'AVERAGE', 84.06, 'TAXABLE', 17, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(478, 'P000468', '純濃豆乳芝麻雪糕 P000468', 'Beatae velit nemo quisquam. Aliquam fugiat molestiae et. Ex accusantium voluptatibus iure temporibus sit tenetur soluta.', 3, 8, 1, 1, 'AVERAGE', 63.99, 'TAXABLE', 19, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(479, 'P000469', '雪藏豆乳芝麻冰淇淋 P000469', 'Alias ut totam. Enim ipsum blanditiis minus esse.', 8, 6, 1, 1, 'AVERAGE', 217.47, 'TAXABLE', 87, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(480, 'P000470', '雪藏燕麥奶雪糕 P000470', 'Dolores sit voluptates quia adipisci quae et. Expedita qui dolore. Similique cumque unde blanditiis delectus.', 4, 4, 1, 1, 'AVERAGE', 50.38, 'TAXABLE', 22, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(481, 'P000471', '夏日香蕉聖代 P000471', 'Architecto non quas accusantium facilis enim eius molestias. Voluptatem porro repudiandae cumque suscipit et temporibus.', 3, 2, 1, 1, 'AVERAGE', 129.46, 'TAXABLE', 26, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(482, 'P000472', '雪藏芒果優格冰棒 P000472', 'Eaque nam tempora et animi eos. In qui illo et perferendis quisquam laudantium.', 4, 8, 1, 1, 'AVERAGE', 251.70, 'TAXABLE', 74, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(483, 'P000473', '夏日薄荷巧克力雪糕 P000473', 'Iure qui enim aut earum dolorum id. Ullam eum atque similique sint voluptas a. Quibusdam cumque aspernatur ipsum ad consequatur facere.', 4, 2, 1, 1, 'AVERAGE', 174.66, 'TAXABLE', 19, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(484, 'P000474', '夏日豆乳芝麻聖代 P000474', 'Dolorum et natus quo officia et eaque quisquam. Earum excepturi id vel quia. Ut repellat occaecati ut.', 6, 2, 1, 1, 'AVERAGE', 281.58, 'TAXABLE', 48, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(485, 'P000475', '鮮果豆乳芝麻聖代 P000475', 'Alias blanditiis repellat omnis. Consequatur qui ipsum. Qui minus id nisi saepe.', 9, 5, 1, 1, 'AVERAGE', 309.74, 'TAXABLE', 93, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(486, 'P000476', '莊園抹茶雪糕 P000476', 'Aperiam quisquam voluptatem reprehenderit quisquam totam impedit. Optio modi culpa hic et iusto odit et.', 4, 7, 1, 1, 'AVERAGE', 138.66, 'TAXABLE', 11, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(487, 'P000477', '經典芒果優格聖代 P000477', 'Quia aut et. Dignissimos dicta voluptas velit vel eos. Temporibus voluptatem eius ratione iusto qui et suscipit.', 9, 8, 1, 1, 'AVERAGE', 213.04, 'TAXABLE', 43, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(488, 'P000478', '純濃芒果優格雪酪 P000478', 'Velit accusamus quaerat ipsum possimus labore consectetur. Quia omnis sequi accusantium nisi aliquid ut. Dolorum veniam nihil ad. Sint assumenda consequuntur.', 8, 3, 1, 1, 'AVERAGE', 130.04, 'TAXABLE', 70, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(489, 'P000479', '鮮果燕麥奶冰棒 P000479', 'Est aut sed illum accusamus necessitatibus. Eligendi consequuntur quaerat. Ab voluptatem velit. Officiis omnis inventore alias eveniet earum aliquam dolorem.', 2, 2, 1, 1, 'AVERAGE', 68.00, 'TAXABLE', 64, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(490, 'P000480', '經典芒果優格冰棒 P000480', 'Omnis delectus ipsam dolorum. Et iste minima autem neque corrupti sapiente.', 3, 7, 1, 1, 'AVERAGE', 252.68, 'TAXABLE', 15, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(491, 'P000481', '夏日燕麥奶雪糕 P000481', 'Aut nemo ullam ut eaque facere aspernatur. Labore minima et et eum minus et ab. Repellat at autem voluptatibus qui iusto. Vel nostrum natus.', 3, 7, 1, 1, 'AVERAGE', 168.39, 'TAXABLE', 82, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(492, 'P000482', '莊園花生雪糕 P000482', 'Qui neque rerum necessitatibus nulla qui dicta aut. Est omnis tenetur et iste sed ex quo.', 3, 4, 1, 1, 'AVERAGE', 179.82, 'TAXABLE', 74, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(493, 'P000483', '極致藍莓冰淇淋 P000483', 'Quas omnis aut ea. Fugit voluptate explicabo id hic et. Adipisci quia dolorem ducimus iste at corrupti distinctio.', 2, 4, 1, 1, 'AVERAGE', 197.56, 'TAXABLE', 61, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(494, 'P000484', '經典豆乳芝麻雪酪 P000484', 'Et voluptatum error. Et optio ut accusamus atque ullam quasi libero. Et aliquam quam et dolores. Dolorem iure ullam esse voluptatem ea.', 9, 7, 1, 1, 'AVERAGE', 110.98, 'TAXABLE', 31, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(495, 'P000485', '莊園薄荷巧克力聖代 P000485', 'Sunt quos saepe dicta et reprehenderit. Ea expedita et nobis aperiam non rerum velit.', 6, 6, 1, 1, 'AVERAGE', 321.56, 'TAXABLE', 81, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(496, 'P000486', '極致海鹽焦糖冰棒 P000486', 'Magni alias quod dolor quae voluptatibus minima. Dolore qui consequatur quos maxime quia voluptas praesentium.', 8, 7, 1, 1, 'AVERAGE', 232.69, 'TAXABLE', 16, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(497, 'P000487', '夏日花生雪糕 P000487', 'Qui totam dignissimos sit ea sequi. Odit aut et quia veritatis libero ipsam.', 2, 8, 1, 1, 'AVERAGE', 100.07, 'TAXABLE', 53, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(498, 'P000488', '夏日花生冰淇淋 P000488', 'Sequi dolor corporis aut fuga veritatis dolores. Est dolores laborum dolorum iure. Ipsa iure architecto iste pariatur dolores autem sit. Voluptas exercitationem enim voluptatem natus quasi odio.', 9, 6, 1, 1, 'AVERAGE', 66.11, 'TAXABLE', 78, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(499, 'P000489', '極致巧克力雪酪 P000489', 'Itaque mollitia impedit reprehenderit inventore quis eum. Perspiciatis ex odio voluptas.', 7, 4, 1, 1, 'AVERAGE', 206.13, 'TAXABLE', 75, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(500, 'P000490', '經典薄荷巧克力冰棒 P000490', 'Veniam ut similique temporibus. Itaque iste et et velit molestias. Aliquam iusto dolore architecto modi quae aspernatur.', 6, 3, 1, 1, 'AVERAGE', 171.90, 'TAXABLE', 17, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(501, 'P000491', '極致香草雪酪 P000491', 'Aut esse in sed aspernatur cupiditate sint. Sit unde iure excepturi.', 3, 7, 1, 1, 'AVERAGE', 219.05, 'TAXABLE', 69, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(502, 'P000492', '夏日香蕉雪酪 P000492', 'Animi eaque reprehenderit deserunt aut. Vitae dolore deserunt. Expedita voluptas quaerat aut sunt.', 9, 3, 1, 1, 'AVERAGE', 225.35, 'TAXABLE', 28, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(503, 'P000493', '夏日燕麥奶雪酪 P000493', 'Sit recusandae necessitatibus ipsam repudiandae assumenda. Possimus itaque aspernatur et velit. Enim rerum vel unde aliquam qui. Nemo dolorem exercitationem ipsa voluptatem quis.', 2, 6, 1, 1, 'AVERAGE', 242.96, 'TAXABLE', 42, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(504, 'P000494', '經典抹茶聖代 P000494', 'Eligendi eos consequatur. Dignissimos eum earum corporis debitis delectus occaecati. Autem nemo laborum.', 6, 2, 1, 1, 'AVERAGE', 233.97, 'TAXABLE', 47, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(505, 'P000495', '夏日豆乳芝麻雪酪 P000495', 'Voluptatem at laborum. Libero dolores perferendis. Possimus architecto dignissimos. Eius sit ipsum.', 6, 3, 1, 1, 'AVERAGE', 131.30, 'TAXABLE', 33, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(506, 'P000496', '經典海鹽焦糖雪糕 P000496', 'Voluptates enim excepturi culpa et quasi et quis. Non non voluptas vel. Reprehenderit eaque numquam illum qui aut. Perferendis velit excepturi doloremque eos pariatur consequatur.', 6, 6, 1, 1, 'AVERAGE', 315.66, 'TAXABLE', 24, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(507, 'P000497', '夏日抹茶冰淇淋 P000497', 'Consequuntur a qui. Aperiam modi harum fugit voluptates. Et ex perferendis.', 3, 6, 1, 1, 'AVERAGE', 82.27, 'TAXABLE', 64, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(508, 'P000498', '莊園豆乳芝麻雪酪 P000498', 'Aut dicta consectetur odit sint ut autem sint. Delectus perferendis sit ipsam eveniet.', 7, 4, 1, 1, 'AVERAGE', 118.89, 'TAXABLE', 17, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(509, 'P000499', '雪藏OREO雪糕 P000499', 'Dolor quo est sint repudiandae suscipit perspiciatis sit. Non perspiciatis voluptas natus sed et ullam ut.', 5, 3, 1, 1, 'AVERAGE', 169.71, 'TAXABLE', 62, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(510, 'P000500', '經典草莓雪糕 P000500', 'A aut velit ex voluptatibus vel. Eum sed atque aut aperiam eum eum laboriosam. Minus possimus perspiciatis voluptatibus eligendi quia saepe.', 2, 2, 1, 1, 'AVERAGE', 251.65, 'TAXABLE', 63, 1, 9999, NULL, 1, NULL, '2025-06-08 02:11:07', 1, '2025-06-08 02:11:07', 1),
(513, 'P000001-1', '鮮果香草雪糕 P0000011', 'string', 2, 2, 1, 1, 'AVERAGE', 90.00, 'string', 50, 1, 9999, NULL, 0, NULL, '2025-06-08 02:11:06', 1, '2025-06-08 03:08:01', 1);

-- --------------------------------------------------------

--
-- Table structure for table `productcategory`
--

CREATE TABLE `productcategory` (
  `category_id` bigint NOT NULL COMMENT '分類唯一識別碼',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分類名稱',
  `parent_category_id` bigint DEFAULT NULL COMMENT '父分類 ID，自關聯，用於多層級分類',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '分類描述',
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否啟用',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
  `created_by` bigint NOT NULL COMMENT '建立人',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最後更新時間',
  `updated_by` bigint NOT NULL COMMENT '最後更新人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品分類表';

--
-- Dumping data for table `productcategory`
--

INSERT INTO `productcategory` (`category_id`, `name`, `parent_category_id`, `description`, `is_active`, `created_at`, `created_by`, `updated_at`, `updated_by`) VALUES
(2, '經典冰淇淋', NULL, NULL, 1, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(3, '水果雪酪', NULL, NULL, 1, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(4, '雪糕系列', NULL, NULL, 1, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(5, '巧酥雪糕系列', NULL, NULL, 1, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(6, '季節限定', NULL, NULL, 1, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(7, '純素系列', NULL, NULL, 1, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(8, '品牌聯名系列', NULL, NULL, 1, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1),
(9, '週邊商品', NULL, NULL, 1, '2025-06-08 02:11:06', 1, '2025-06-08 02:11:06', 1);

-- --------------------------------------------------------

--
-- Table structure for table `ProductImage`
--

CREATE TABLE `ProductImage` (
  `image_id` bigint NOT NULL COMMENT '圖片唯一識別碼',
  `product_id` bigint NOT NULL COMMENT '關聯到 Product 表',
  `image_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '圖片儲存的路徑或 URL',
  `is_main_image` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否為主圖',
  `sort_order` int NOT NULL DEFAULT '0' COMMENT '顯示排序',
  `uploaded_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上傳時間',
  `uploaded_by` bigint NOT NULL COMMENT '上傳人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品圖片表';

-- --------------------------------------------------------

--
-- Table structure for table `ProductSpec`
--

CREATE TABLE `ProductSpec` (
  `spec_id` bigint NOT NULL COMMENT '規格唯一識別碼',
  `product_id` bigint NOT NULL COMMENT '關聯到 Product 表',
  `sku_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'SKU 編碼',
  `spec_name_1` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '規格名稱一 (例如：顏色)',
  `spec_value_1` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '規格值一 (例如：紅色)',
  `spec_name_2` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '規格名稱二 (例如：尺寸)',
  `spec_value_2` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '規格值二 (例如：L)',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
  `created_by` bigint NOT NULL COMMENT '建立人',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最後更新時間',
  `updated_by` bigint NOT NULL COMMENT '最後更新人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品規格表';

-- --------------------------------------------------------

--
-- Table structure for table `PurchaseOrder`
--

CREATE TABLE `PurchaseOrder` (
  `purchase_order_id` bigint NOT NULL COMMENT '進貨單唯一識別碼',
  `order_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '進貨單號',
  `purchase_request_id` bigint DEFAULT NULL COMMENT '關聯到 PurchaseRequest 表 (表示此採購訂單是由哪個採購申請轉化而來)',
  `order_date` date NOT NULL COMMENT '進貨日期',
  `supplier_id` bigint NOT NULL COMMENT '關聯到 Supplier 表',
  `currency` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '幣別',
  `total_amount` decimal(18,2) NOT NULL COMMENT '進貨單總金額 (含稅)',
  `total_tax_amount` decimal(18,2) NOT NULL COMMENT '進貨單總稅額',
  `total_net_amount` decimal(18,2) NOT NULL COMMENT '進貨單總未稅金額',
  `total_cost_amount` decimal(18,2) NOT NULL COMMENT '進貨單總費用金額 (例如：運費)',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'DRAFT' COMMENT '進貨單狀態 (例如：DRAFT, PENDING_CONFIRM, CONFIRMED, PARTIAL_RECEIVED, RECEIVED, CANCELLED)',
  `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '備註',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
  `created_by` bigint NOT NULL COMMENT '建立人',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最後更新時間',
  `updated_by` bigint NOT NULL COMMENT '最後更新人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='進貨單主表';

-- --------------------------------------------------------

--
-- Table structure for table `PurchaseOrderDetail`
--

CREATE TABLE `PurchaseOrderDetail` (
  `item_id` bigint NOT NULL COMMENT '明細項目唯一識別碼',
  `purchase_order_id` bigint NOT NULL COMMENT '關聯到 PurchaseOrder 表',
  `purchase_request_item_id` bigint DEFAULT NULL COMMENT '關聯到 PurchaseRequestDetail 表 (表示此採購訂單明細是來自哪個採購申請明細)',
  `item_sequence` int NOT NULL COMMENT '明細項目的序號',
  `product_id` bigint NOT NULL COMMENT '關聯到 Product 表',
  `unit_price` decimal(18,2) NOT NULL COMMENT '商品單價',
  `quantity` decimal(18,2) NOT NULL COMMENT '進貨數量',
  `item_amount` decimal(18,2) NOT NULL COMMENT '明細項目金額',
  `item_tax_amount` decimal(18,2) NOT NULL COMMENT '明細項目稅額',
  `item_net_amount` decimal(18,2) NOT NULL COMMENT '明細項目未稅金額',
  `warehouse_id` bigint NOT NULL COMMENT '關聯到 Warehouse 表',
  `is_gift` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否為贈品',
  `batch_number` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '批號',
  `weight` decimal(18,2) DEFAULT NULL COMMENT '重量',
  `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '明細備註',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
  `created_by` bigint NOT NULL COMMENT '建立人',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最後更新時間',
  `updated_by` bigint NOT NULL COMMENT '最後更新人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='進貨單明細表';

-- --------------------------------------------------------

--
-- Table structure for table `PurchaseRequest`
--

CREATE TABLE `PurchaseRequest` (
  `request_id` bigint NOT NULL COMMENT '採購申請單唯一識別碼',
  `request_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '採購申請單號',
  `request_date` date NOT NULL COMMENT '申請日期',
  `sales_order_id` bigint DEFAULT NULL COMMENT '關聯到 SalesOrder 表 (表示此採購申請是因哪個銷售訂單而發起)',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'DRAFT' COMMENT '採購申請狀態 (例如：DRAFT, PENDING_APPROVAL, APPROVED, CONVERTED_TO_PO, CANCELLED)',
  `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '備註',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
  `created_by` bigint NOT NULL COMMENT '建立人',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最後更新時間',
  `updated_by` bigint NOT NULL COMMENT '最後更新人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='採購申請單主表';

-- --------------------------------------------------------

--
-- Table structure for table `PurchaseRequestDetail`
--

CREATE TABLE `PurchaseRequestDetail` (
  `item_id` bigint NOT NULL COMMENT '明細項目唯一識別碼',
  `request_id` bigint NOT NULL COMMENT '關聯到 PurchaseRequest 表',
  `item_sequence` int NOT NULL COMMENT '明細項目的序號',
  `product_id` bigint NOT NULL COMMENT '關聯到 Product 表',
  `requested_quantity` decimal(18,2) NOT NULL COMMENT '申請採購數量',
  `unit_id` bigint NOT NULL COMMENT '關聯到 Unit 表',
  `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '明細備註',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
  `created_by` bigint NOT NULL COMMENT '建立人',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最後更新時間',
  `updated_by` bigint NOT NULL COMMENT '最後更新人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='採購申請單明細表';

-- --------------------------------------------------------

--
-- Table structure for table `PurchaseReturn`
--

CREATE TABLE `PurchaseReturn` (
  `purchase_return_id` bigint NOT NULL COMMENT '進貨退出單唯一識別碼',
  `return_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '進貨退出單號',
  `return_date` date NOT NULL COMMENT '退回日期',
  `supplier_id` bigint NOT NULL COMMENT '關聯到 Supplier 表',
  `original_purchase_order_id` bigint DEFAULT NULL COMMENT '關聯到原進貨單',
  `contact_person_id` bigint DEFAULT NULL COMMENT '關聯到 ContactPerson 表',
  `currency` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '幣別',
  `total_amount` decimal(18,2) NOT NULL COMMENT '退回總金額 (含稅)',
  `total_tax_amount` decimal(18,2) NOT NULL COMMENT '退回總稅額',
  `total_net_amount` decimal(18,2) NOT NULL COMMENT '退回總未稅金額',
  `return_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '退回原因',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'DRAFT' COMMENT '進貨退出單狀態 (例如：DRAFT, PENDING_SHIP, SHIPPED, COMPLETED, CANCELLED)',
  `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '備註',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
  `created_by` bigint NOT NULL COMMENT '建立人',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最後更新時間',
  `updated_by` bigint NOT NULL COMMENT '最後更新人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='進貨退出單主表';

-- --------------------------------------------------------

--
-- Table structure for table `PurchaseReturnDetail`
--

CREATE TABLE `PurchaseReturnDetail` (
  `item_id` bigint NOT NULL COMMENT '明細項目唯一識別碼',
  `purchase_return_id` bigint NOT NULL COMMENT '關聯到 PurchaseReturn 表',
  `item_sequence` int NOT NULL COMMENT '明細項目的序號',
  `product_id` bigint NOT NULL COMMENT '關聯到 Product 表',
  `specification` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品規格 (冗餘自 Product 表)',
  `unit_id` bigint NOT NULL COMMENT '關聯到 Unit 表',
  `returned_quantity` decimal(18,2) NOT NULL COMMENT '退回數量',
  `unit_price` decimal(18,2) NOT NULL COMMENT '退回時的單價',
  `item_amount` decimal(18,2) NOT NULL COMMENT '明細項目金額',
  `item_net_amount` decimal(18,2) NOT NULL COMMENT '明細項目未稅金額',
  `item_tax_amount` decimal(18,2) NOT NULL COMMENT '明細項目稅額',
  `returned_from_warehouse_id` bigint NOT NULL COMMENT '商品從哪個倉庫退回',
  `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '明細備註',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
  `created_by` bigint NOT NULL COMMENT '建立人',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最後更新時間',
  `updated_by` bigint NOT NULL COMMENT '最後更新人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='進貨退出單明細表';

-- --------------------------------------------------------

--
-- Table structure for table `Quotation`
--

CREATE TABLE `Quotation` (
  `quotation_id` bigint NOT NULL COMMENT '報價單唯一識別碼',
  `quotation_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '報價單號',
  `quotation_date` date NOT NULL COMMENT '報價日期',
  `customer_id` bigint NOT NULL COMMENT '關聯到 Customer 表',
  `contact_person_id` bigint DEFAULT NULL COMMENT '關聯到 ContactPerson 表',
  `valid_until_date` date DEFAULT NULL COMMENT '報價有效期限',
  `currency` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '幣別',
  `total_amount` decimal(18,2) NOT NULL COMMENT '報價單總金額 (含稅)',
  `total_tax_amount` decimal(18,2) NOT NULL COMMENT '報價單總稅額',
  `total_net_amount` decimal(18,2) NOT NULL COMMENT '報價單總未稅金額',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'DRAFT' COMMENT '報價單狀態 (例如：DRAFT, QUOTED, ACCEPTED, REJECTED, CONVERTED_TO_SO, EXPIRED)',
  `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '備註',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
  `created_by` bigint NOT NULL COMMENT '建立人',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最後更新時間',
  `updated_by` bigint NOT NULL COMMENT '最後更新人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='報價單主表';

-- --------------------------------------------------------

--
-- Table structure for table `QuotationDetail`
--

CREATE TABLE `QuotationDetail` (
  `item_id` bigint NOT NULL COMMENT '明細項目唯一識別碼',
  `quotation_id` bigint NOT NULL COMMENT '關聯到 Quotation 表',
  `item_sequence` int NOT NULL COMMENT '明細項目的序號',
  `product_id` bigint NOT NULL COMMENT '關聯到 Product 表',
  `specification` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品規格 (冗餘自 Product 表)',
  `unit_id` bigint NOT NULL COMMENT '關聯到 Unit 表',
  `unit_price` decimal(18,2) NOT NULL COMMENT '商品單價',
  `quantity` decimal(18,2) NOT NULL COMMENT '報價數量',
  `item_amount` decimal(18,2) NOT NULL COMMENT '明細項目金額',
  `discount_rate` decimal(5,2) NOT NULL COMMENT '折扣率',
  `item_net_amount` decimal(18,2) NOT NULL COMMENT '明細項目未稅金額',
  `item_tax_amount` decimal(18,2) NOT NULL COMMENT '明細項目稅額',
  `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '明細備註',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
  `created_by` bigint NOT NULL COMMENT '建立人',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最後更新時間',
  `updated_by` bigint NOT NULL COMMENT '最後更新人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='報價單明細表';

-- --------------------------------------------------------

--
-- Table structure for table `SalesOrder`
--

CREATE TABLE `SalesOrder` (
  `sales_order_id` bigint NOT NULL COMMENT '銷售訂單唯一識別碼',
  `order_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '銷售訂單號',
  `order_date` date NOT NULL COMMENT '訂單日期',
  `customer_id` bigint NOT NULL COMMENT '關聯到 Customer 表',
  `contact_person_id` bigint DEFAULT NULL COMMENT '關聯到 ContactPerson 表',
  `quotation_id` bigint DEFAULT NULL COMMENT '關聯到 Quotation 表 (表示訂單可能來自某份報價單)',
  `currency` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '幣別',
  `total_amount` decimal(18,2) NOT NULL COMMENT '訂單總金額 (含稅)',
  `total_tax_amount` decimal(18,2) NOT NULL COMMENT '訂單總稅額',
  `total_net_amount` decimal(18,2) NOT NULL COMMENT '訂單總未稅金額',
  `payment_method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '付款方式',
  `payment_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'UNPAID' COMMENT '付款狀態 (例如：UNPAID, PARTIAL_PAID, PAID)',
  `shipping_method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '物流方式',
  `shipping_address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '送貨地址',
  `estimated_delivery_date` date DEFAULT NULL COMMENT '預計交貨日',
  `order_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'DRAFT' COMMENT '銷售訂單狀態 (例如：DRAFT, PENDING_CONFIRM, CONFIRMED, PARTIAL_SHIPPED, SHIPPED, COMPLETED, CANCELLED)',
  `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '備註',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
  `created_by` bigint NOT NULL COMMENT '建立人',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最後更新時間',
  `updated_by` bigint NOT NULL COMMENT '最後更新人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='銷售訂單主表';

-- --------------------------------------------------------

--
-- Table structure for table `SalesOrderDetail`
--

CREATE TABLE `SalesOrderDetail` (
  `item_id` bigint NOT NULL COMMENT '明細項目唯一識別碼',
  `sales_order_id` bigint NOT NULL COMMENT '關聯到 SalesOrder 表',
  `item_sequence` int NOT NULL COMMENT '明細項目的序號',
  `product_id` bigint NOT NULL COMMENT '關聯到 Product 表',
  `specification` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品規格 (冗餘自 Product 表)',
  `unit_id` bigint NOT NULL COMMENT '關聯到 Unit 表',
  `unit_price` decimal(18,2) NOT NULL COMMENT '商品單價',
  `quantity` decimal(18,2) NOT NULL COMMENT '訂單數量',
  `item_amount` decimal(18,2) NOT NULL COMMENT '明細項目金額',
  `discount_rate` decimal(5,2) NOT NULL COMMENT '折扣率',
  `item_net_amount` decimal(18,2) NOT NULL COMMENT '明細項目未稅金額',
  `item_tax_amount` decimal(18,2) NOT NULL COMMENT '明細項目稅額',
  `e_commerce_platform` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '電商平台名稱 (例如：蝦皮購物, Pinkoi)',
  `shop_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商店名稱',
  `transaction_number` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '交易序號',
  `platform_order_number` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '電商平台訂單號碼',
  `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '明細備註',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
  `created_by` bigint NOT NULL COMMENT '建立人',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最後更新時間',
  `updated_by` bigint NOT NULL COMMENT '最後更新人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='銷售訂單明細表';

-- --------------------------------------------------------

--
-- Table structure for table `SalesReturn`
--

CREATE TABLE `SalesReturn` (
  `sales_return_id` bigint NOT NULL COMMENT '銷貨退回單唯一識別碼',
  `return_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '銷貨退回單號',
  `return_date` date NOT NULL COMMENT '退回日期',
  `customer_id` bigint NOT NULL COMMENT '關聯到 Customer 表',
  `original_sales_order_id` bigint DEFAULT NULL COMMENT '關聯到原銷售訂單',
  `original_shipment_id` bigint DEFAULT NULL COMMENT '關聯到原出貨單',
  `contact_person_id` bigint DEFAULT NULL COMMENT '關聯到 ContactPerson 表',
  `currency` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '幣別',
  `total_amount` decimal(18,2) NOT NULL COMMENT '退回總金額 (含稅)',
  `total_tax_amount` decimal(18,2) NOT NULL COMMENT '退回總稅額',
  `total_net_amount` decimal(18,2) NOT NULL COMMENT '退回總未稅金額',
  `return_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '退回原因',
  `return_method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '退回方式 (例如：REFUND, EXCHANGE)',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'DRAFT' COMMENT '銷貨退回單狀態 (例如：DRAFT, PENDING_RECEIVE, RECEIVED, COMPLETED, CANCELLED)',
  `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '備註',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
  `created_by` bigint NOT NULL COMMENT '建立人',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最後更新時間',
  `updated_by` bigint NOT NULL COMMENT '最後更新人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='銷貨退回單主表';

-- --------------------------------------------------------

--
-- Table structure for table `SalesReturnDetail`
--

CREATE TABLE `SalesReturnDetail` (
  `item_id` bigint NOT NULL COMMENT '明細項目唯一識別碼',
  `sales_return_id` bigint NOT NULL COMMENT '關聯到 SalesReturn 表',
  `item_sequence` int NOT NULL COMMENT '明細項目的序號',
  `product_id` bigint NOT NULL COMMENT '關聯到 Product 表',
  `specification` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品規格 (冗餘自 Product 表)',
  `unit_id` bigint NOT NULL COMMENT '關聯到 Unit 表',
  `returned_quantity` decimal(18,2) NOT NULL COMMENT '退回數量',
  `unit_price` decimal(18,2) NOT NULL COMMENT '退回時的單價',
  `item_amount` decimal(18,2) NOT NULL COMMENT '明細項目金額',
  `item_net_amount` decimal(18,2) NOT NULL COMMENT '明細項目未稅金額',
  `item_tax_amount` decimal(18,2) NOT NULL COMMENT '明細項目稅額',
  `returned_to_warehouse_id` bigint NOT NULL COMMENT '商品退回哪個倉庫',
  `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '明細備註',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
  `created_by` bigint NOT NULL COMMENT '建立人',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最後更新時間',
  `updated_by` bigint NOT NULL COMMENT '最後更新人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='銷貨退回單明細表';

-- --------------------------------------------------------

--
-- Table structure for table `SalesShipment`
--

CREATE TABLE `SalesShipment` (
  `shipment_id` bigint NOT NULL COMMENT '出貨單唯一識別碼',
  `shipment_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '出貨單號',
  `sales_order_id` bigint DEFAULT NULL COMMENT '關聯到 SalesOrder 表 (表示出貨單可能來自某份銷售訂單)',
  `shipment_date` date NOT NULL COMMENT '出貨日期',
  `customer_id` bigint NOT NULL COMMENT '關聯到 Customer 表',
  `warehouse_id` bigint NOT NULL COMMENT '關聯到 Warehouse 表 (從哪個倉庫出貨)',
  `shipping_method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '物流方式',
  `tracking_number` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '物流追蹤碼',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'DRAFT' COMMENT '出貨單狀態 (例如：DRAFT, PENDING_PICK, PICKED, SHIPPED, CANCELLED)',
  `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '備註',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
  `created_by` bigint NOT NULL COMMENT '建立人',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最後更新時間',
  `updated_by` bigint NOT NULL COMMENT '最後更新人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='銷貨出貨單主表';

-- --------------------------------------------------------

--
-- Table structure for table `SalesShipmentDetail`
--

CREATE TABLE `SalesShipmentDetail` (
  `item_id` bigint NOT NULL COMMENT '明細項目唯一識別碼',
  `shipment_id` bigint NOT NULL COMMENT '關聯到 SalesShipment 表',
  `sales_order_item_id` bigint DEFAULT NULL COMMENT '關聯到 SalesOrderDetail 表 (用於分批出貨追溯)',
  `product_id` bigint NOT NULL COMMENT '關聯到 Product 表',
  `shipped_quantity` decimal(18,2) NOT NULL COMMENT '實際出貨數量',
  `unit_id` bigint NOT NULL COMMENT '關聯到 Unit 表',
  `unit_price` decimal(18,2) NOT NULL COMMENT '出貨時的單價',
  `item_amount` decimal(18,2) NOT NULL COMMENT '明細項目金額',
  `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '明細備註',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
  `created_by` bigint NOT NULL COMMENT '建立人',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最後更新時間',
  `updated_by` bigint NOT NULL COMMENT '最後更新人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='銷貨出貨單明細表';

-- --------------------------------------------------------

--
-- Table structure for table `Supplier`
--

CREATE TABLE `Supplier` (
  `supplier_id` bigint NOT NULL COMMENT '供應商唯一識別碼',
  `supplier_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '供應商代號',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '供應商名稱',
  `contact_person` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '聯絡人姓名',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '職稱',
  `phone_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '電話號碼',
  `mobile_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手機號碼',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '電子郵件',
  `company_address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '公司地址',
  `shipping_address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '送貨地址',
  `invoice_address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '發票地址',
  `payment_terms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '付款條件',
  `invoice_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '發票抬頭',
  `tax_id_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '統一編號',
  `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '備註',
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '供應商是否啟用',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
  `created_by` bigint NOT NULL COMMENT '建立人',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最後更新時間',
  `updated_by` bigint NOT NULL COMMENT '最後更新人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='供應商基本資料表';

-- --------------------------------------------------------

--
-- Table structure for table `unit`
--

CREATE TABLE `unit` (
  `unit_id` bigint NOT NULL COMMENT '單位唯一識別碼',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '單位名稱 (如：個, 箱, 公斤)',
  `is_base_unit` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否為基本計量單位',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
  `created_by` bigint NOT NULL COMMENT '建立人',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最後更新時間',
  `updated_by` bigint NOT NULL COMMENT '最後更新人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品單位表';

--
-- Dumping data for table `unit`
--

INSERT INTO `unit` (`unit_id`, `name`, `is_base_unit`, `created_at`, `created_by`, `updated_at`, `updated_by`) VALUES
(2, '個', 0, '2025-06-08 02:07:29', 1, '2025-06-08 02:07:29', 1),
(3, '箱', 0, '2025-06-08 02:07:29', 1, '2025-06-08 02:07:29', 1),
(4, '打', 0, '2025-06-08 02:07:29', 1, '2025-06-08 02:07:29', 1),
(5, '公克', 0, '2025-06-08 02:07:29', 1, '2025-06-08 02:07:29', 1),
(6, '支', 0, '2025-06-08 02:07:29', 1, '2025-06-08 02:07:29', 1),
(7, '杯', 0, '2025-06-08 02:07:29', 1, '2025-06-08 02:07:29', 1),
(8, '盒', 0, '2025-06-08 02:07:29', 1, '2025-06-08 02:07:29', 1);

-- --------------------------------------------------------

--
-- Table structure for table `UnitConversion`
--

CREATE TABLE `UnitConversion` (
  `conversion_id` bigint NOT NULL COMMENT '換算關係唯一識別碼',
  `from_unit_id` bigint NOT NULL COMMENT '來源單位 ID (例如：箱)',
  `to_unit_id` bigint NOT NULL COMMENT '目標單位 ID (例如：個)',
  `conversion_rate` decimal(18,6) NOT NULL COMMENT '換算數量 (例如：1 箱 = 12 個，則為 12)',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
  `created_by` bigint NOT NULL COMMENT '建立人',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最後更新時間',
  `updated_by` bigint NOT NULL COMMENT '最後更新人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='單位換算關係表';

-- --------------------------------------------------------

--
-- Table structure for table `User`
--

CREATE TABLE `User` (
  `user_id` bigint NOT NULL COMMENT '使用者唯一識別碼',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '使用者名稱',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '電子郵件',
  `password_hash` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密碼哈希值',
  `role_id` bigint NOT NULL COMMENT '關聯到 Role 表 (權限管理) 的 ID',
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否啟用',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最後更新時間'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系統使用者資訊表';

--
-- Dumping data for table `User`
--

INSERT INTO `User` (`user_id`, `username`, `email`, `password_hash`, `role_id`, `is_active`, `created_at`, `updated_at`) VALUES
(1, 'admin', 'admin@yourcompany.com', '$2a$10$abcdefghijklmnopqrstuv', 1, 1, '2025-06-07 18:31:44', '2025-06-07 18:31:44');

-- --------------------------------------------------------

--
-- Table structure for table `Warehouse`
--

CREATE TABLE `Warehouse` (
  `warehouse_id` bigint NOT NULL COMMENT '倉庫唯一識別碼',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '倉庫名稱',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '倉庫地點/地址',
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否啟用',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
  `created_by` bigint NOT NULL COMMENT '建立人',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最後更新時間',
  `updated_by` bigint NOT NULL COMMENT '最後更新人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='倉庫資訊表';

--
-- Indexes for dumped tables
--

--
-- Indexes for table `ContactPerson`
--
ALTER TABLE `ContactPerson`
  ADD PRIMARY KEY (`contact_person_id`),
  ADD KEY `fk_contact_person_customer` (`customer_id`);

--
-- Indexes for table `Customer`
--
ALTER TABLE `Customer`
  ADD PRIMARY KEY (`customer_id`),
  ADD UNIQUE KEY `customer_code` (`customer_code`);

--
-- Indexes for table `Inventory`
--
ALTER TABLE `Inventory`
  ADD PRIMARY KEY (`inventory_id`),
  ADD UNIQUE KEY `uk_product_warehouse_unique` (`product_id`,`warehouse_id`),
  ADD KEY `fk_inventory_warehouse` (`warehouse_id`),
  ADD KEY `fk_inventory_created_by_user` (`created_by`),
  ADD KEY `fk_inventory_updated_by_user` (`updated_by`);

--
-- Indexes for table `InventoryAdjustment`
--
ALTER TABLE `InventoryAdjustment`
  ADD PRIMARY KEY (`adjustment_id`),
  ADD UNIQUE KEY `adjustment_number` (`adjustment_number`),
  ADD KEY `fk_inventory_adjustment_warehouse` (`warehouse_id`),
  ADD KEY `fk_inventory_adjustment_created_by_user` (`created_by`),
  ADD KEY `fk_inventory_adjustment_updated_by_user` (`updated_by`);

--
-- Indexes for table `InventoryAdjustmentDetail`
--
ALTER TABLE `InventoryAdjustmentDetail`
  ADD PRIMARY KEY (`item_id`),
  ADD KEY `fk_inventory_adjustment_detail_adjustment` (`adjustment_id`),
  ADD KEY `fk_inventory_adjustment_detail_product` (`product_id`),
  ADD KEY `fk_inventory_adjustment_detail_unit` (`unit_id`),
  ADD KEY `fk_inventory_adjustment_detail_created_by_user` (`created_by`),
  ADD KEY `fk_inventory_adjustment_detail_updated_by_user` (`updated_by`);

--
-- Indexes for table `InventoryMovement`
--
ALTER TABLE `InventoryMovement`
  ADD PRIMARY KEY (`movement_id`),
  ADD KEY `fk_inventory_movement_product` (`product_id`),
  ADD KEY `fk_inventory_movement_warehouse` (`warehouse_id`),
  ADD KEY `fk_inventory_movement_recorded_by_user` (`recorded_by`);

--
-- Indexes for table `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`product_id`),
  ADD UNIQUE KEY `product_code` (`product_code`),
  ADD UNIQUE KEY `UKhcpr86kgtroqvxu1mxoyx4ahm` (`product_code`),
  ADD KEY `fk_product_category` (`category_id`),
  ADD KEY `fk_product_unit` (`unit_id`),
  ADD KEY `fk_product_created_by_user` (`created_by`),
  ADD KEY `fk_product_updated_by_user` (`updated_by`);

--
-- Indexes for table `productcategory`
--
ALTER TABLE `productcategory`
  ADD PRIMARY KEY (`category_id`),
  ADD UNIQUE KEY `name` (`name`),
  ADD UNIQUE KEY `UKbcs1cbm8toqqwsx93k6yue7g7` (`name`),
  ADD KEY `fk_product_category_parent` (`parent_category_id`),
  ADD KEY `fk_product_category_created_by_user` (`created_by`),
  ADD KEY `fk_product_category_updated_by_user` (`updated_by`);

--
-- Indexes for table `ProductImage`
--
ALTER TABLE `ProductImage`
  ADD PRIMARY KEY (`image_id`),
  ADD KEY `fk_product_image_product` (`product_id`),
  ADD KEY `fk_product_image_uploaded_by_user` (`uploaded_by`);

--
-- Indexes for table `ProductSpec`
--
ALTER TABLE `ProductSpec`
  ADD PRIMARY KEY (`spec_id`),
  ADD UNIQUE KEY `sku_code` (`sku_code`),
  ADD KEY `fk_product_spec_product` (`product_id`),
  ADD KEY `fk_product_spec_created_by_user` (`created_by`),
  ADD KEY `fk_product_spec_updated_by_user` (`updated_by`);

--
-- Indexes for table `PurchaseOrder`
--
ALTER TABLE `PurchaseOrder`
  ADD PRIMARY KEY (`purchase_order_id`),
  ADD UNIQUE KEY `order_number` (`order_number`),
  ADD KEY `fk_purchase_order_purchase_request` (`purchase_request_id`),
  ADD KEY `fk_purchase_order_supplier` (`supplier_id`),
  ADD KEY `fk_purchase_order_created_by_user` (`created_by`),
  ADD KEY `fk_purchase_order_updated_by_user` (`updated_by`);

--
-- Indexes for table `PurchaseOrderDetail`
--
ALTER TABLE `PurchaseOrderDetail`
  ADD PRIMARY KEY (`item_id`),
  ADD KEY `fk_purchase_order_detail_purchase_order` (`purchase_order_id`),
  ADD KEY `fk_purchase_order_detail_purchase_request_item` (`purchase_request_item_id`),
  ADD KEY `fk_purchase_order_detail_product` (`product_id`),
  ADD KEY `fk_purchase_order_detail_warehouse` (`warehouse_id`),
  ADD KEY `fk_purchase_order_detail_created_by_user` (`created_by`),
  ADD KEY `fk_purchase_order_detail_updated_by_user` (`updated_by`);

--
-- Indexes for table `PurchaseRequest`
--
ALTER TABLE `PurchaseRequest`
  ADD PRIMARY KEY (`request_id`),
  ADD UNIQUE KEY `request_number` (`request_number`),
  ADD KEY `fk_purchase_request_sales_order` (`sales_order_id`),
  ADD KEY `fk_purchase_request_created_by_user` (`created_by`),
  ADD KEY `fk_purchase_request_updated_by_user` (`updated_by`);

--
-- Indexes for table `PurchaseRequestDetail`
--
ALTER TABLE `PurchaseRequestDetail`
  ADD PRIMARY KEY (`item_id`),
  ADD KEY `fk_purchase_request_detail_request` (`request_id`),
  ADD KEY `fk_purchase_request_detail_product` (`product_id`),
  ADD KEY `fk_purchase_request_detail_unit` (`unit_id`),
  ADD KEY `fk_purchase_request_detail_created_by_user` (`created_by`),
  ADD KEY `fk_purchase_request_detail_updated_by_user` (`updated_by`);

--
-- Indexes for table `PurchaseReturn`
--
ALTER TABLE `PurchaseReturn`
  ADD PRIMARY KEY (`purchase_return_id`),
  ADD UNIQUE KEY `return_number` (`return_number`),
  ADD KEY `fk_purchase_return_supplier` (`supplier_id`),
  ADD KEY `fk_purchase_return_original_po` (`original_purchase_order_id`),
  ADD KEY `fk_purchase_return_contact_person` (`contact_person_id`),
  ADD KEY `fk_purchase_return_created_by_user` (`created_by`),
  ADD KEY `fk_purchase_return_updated_by_user` (`updated_by`);

--
-- Indexes for table `PurchaseReturnDetail`
--
ALTER TABLE `PurchaseReturnDetail`
  ADD PRIMARY KEY (`item_id`),
  ADD KEY `fk_purchase_return_detail_purchase_return` (`purchase_return_id`),
  ADD KEY `fk_purchase_return_detail_product` (`product_id`),
  ADD KEY `fk_purchase_return_detail_unit` (`unit_id`),
  ADD KEY `fk_purchase_return_detail_warehouse` (`returned_from_warehouse_id`),
  ADD KEY `fk_purchase_return_detail_created_by_user` (`created_by`),
  ADD KEY `fk_purchase_return_detail_updated_by_user` (`updated_by`);

--
-- Indexes for table `Quotation`
--
ALTER TABLE `Quotation`
  ADD PRIMARY KEY (`quotation_id`),
  ADD UNIQUE KEY `quotation_number` (`quotation_number`),
  ADD KEY `fk_quotation_customer` (`customer_id`),
  ADD KEY `fk_quotation_contact_person` (`contact_person_id`),
  ADD KEY `fk_quotation_created_by_user` (`created_by`),
  ADD KEY `fk_quotation_updated_by_user` (`updated_by`);

--
-- Indexes for table `QuotationDetail`
--
ALTER TABLE `QuotationDetail`
  ADD PRIMARY KEY (`item_id`),
  ADD KEY `fk_quotation_detail_quotation` (`quotation_id`),
  ADD KEY `fk_quotation_detail_product` (`product_id`),
  ADD KEY `fk_quotation_detail_unit` (`unit_id`),
  ADD KEY `fk_quotation_detail_created_by_user` (`created_by`),
  ADD KEY `fk_quotation_detail_updated_by_user` (`updated_by`);

--
-- Indexes for table `SalesOrder`
--
ALTER TABLE `SalesOrder`
  ADD PRIMARY KEY (`sales_order_id`),
  ADD UNIQUE KEY `order_number` (`order_number`),
  ADD KEY `fk_sales_order_customer` (`customer_id`),
  ADD KEY `fk_sales_order_contact_person` (`contact_person_id`),
  ADD KEY `fk_sales_order_quotation` (`quotation_id`),
  ADD KEY `fk_sales_order_created_by_user` (`created_by`),
  ADD KEY `fk_sales_order_updated_by_user` (`updated_by`);

--
-- Indexes for table `SalesOrderDetail`
--
ALTER TABLE `SalesOrderDetail`
  ADD PRIMARY KEY (`item_id`),
  ADD KEY `fk_sales_order_detail_sales_order` (`sales_order_id`),
  ADD KEY `fk_sales_order_detail_product` (`product_id`),
  ADD KEY `fk_sales_order_detail_unit` (`unit_id`),
  ADD KEY `fk_sales_order_detail_created_by_user` (`created_by`),
  ADD KEY `fk_sales_order_detail_updated_by_user` (`updated_by`);

--
-- Indexes for table `SalesReturn`
--
ALTER TABLE `SalesReturn`
  ADD PRIMARY KEY (`sales_return_id`),
  ADD UNIQUE KEY `return_number` (`return_number`),
  ADD KEY `fk_sales_return_customer` (`customer_id`),
  ADD KEY `fk_sales_return_original_so` (`original_sales_order_id`),
  ADD KEY `fk_sales_return_original_shipment` (`original_shipment_id`),
  ADD KEY `fk_sales_return_contact_person` (`contact_person_id`),
  ADD KEY `fk_sales_return_created_by_user` (`created_by`),
  ADD KEY `fk_sales_return_updated_by_user` (`updated_by`);

--
-- Indexes for table `SalesReturnDetail`
--
ALTER TABLE `SalesReturnDetail`
  ADD PRIMARY KEY (`item_id`),
  ADD KEY `fk_sales_return_detail_sales_return` (`sales_return_id`),
  ADD KEY `fk_sales_return_detail_product` (`product_id`),
  ADD KEY `fk_sales_return_detail_unit` (`unit_id`),
  ADD KEY `fk_sales_return_detail_warehouse` (`returned_to_warehouse_id`),
  ADD KEY `fk_sales_return_detail_created_by_user` (`created_by`),
  ADD KEY `fk_sales_return_detail_updated_by_user` (`updated_by`);

--
-- Indexes for table `SalesShipment`
--
ALTER TABLE `SalesShipment`
  ADD PRIMARY KEY (`shipment_id`),
  ADD UNIQUE KEY `shipment_number` (`shipment_number`),
  ADD KEY `fk_sales_shipment_sales_order` (`sales_order_id`),
  ADD KEY `fk_sales_shipment_customer` (`customer_id`),
  ADD KEY `fk_sales_shipment_warehouse` (`warehouse_id`),
  ADD KEY `fk_sales_shipment_created_by_user` (`created_by`),
  ADD KEY `fk_sales_shipment_updated_by_user` (`updated_by`);

--
-- Indexes for table `SalesShipmentDetail`
--
ALTER TABLE `SalesShipmentDetail`
  ADD PRIMARY KEY (`item_id`),
  ADD KEY `fk_sales_shipment_detail_shipment` (`shipment_id`),
  ADD KEY `fk_sales_shipment_detail_sales_order_item` (`sales_order_item_id`),
  ADD KEY `fk_sales_shipment_detail_product` (`product_id`),
  ADD KEY `fk_sales_shipment_detail_unit` (`unit_id`),
  ADD KEY `fk_sales_shipment_detail_created_by_user` (`created_by`),
  ADD KEY `fk_sales_shipment_detail_updated_by_user` (`updated_by`);

--
-- Indexes for table `Supplier`
--
ALTER TABLE `Supplier`
  ADD PRIMARY KEY (`supplier_id`),
  ADD UNIQUE KEY `supplier_code` (`supplier_code`),
  ADD KEY `fk_supplier_created_by_user` (`created_by`),
  ADD KEY `fk_supplier_updated_by_user` (`updated_by`);

--
-- Indexes for table `unit`
--
ALTER TABLE `unit`
  ADD PRIMARY KEY (`unit_id`),
  ADD UNIQUE KEY `name` (`name`),
  ADD UNIQUE KEY `UKaa58c9de9eu0v585le47w25my` (`name`),
  ADD KEY `fk_unit_created_by_user` (`created_by`),
  ADD KEY `fk_unit_updated_by_user` (`updated_by`);

--
-- Indexes for table `UnitConversion`
--
ALTER TABLE `UnitConversion`
  ADD PRIMARY KEY (`conversion_id`),
  ADD UNIQUE KEY `uk_unit_conversion_pair` (`from_unit_id`,`to_unit_id`),
  ADD KEY `fk_unit_conversion_to_unit` (`to_unit_id`),
  ADD KEY `fk_unit_conversion_created_by_user` (`created_by`),
  ADD KEY `fk_unit_conversion_updated_by_user` (`updated_by`);

--
-- Indexes for table `User`
--
ALTER TABLE `User`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `Warehouse`
--
ALTER TABLE `Warehouse`
  ADD PRIMARY KEY (`warehouse_id`),
  ADD UNIQUE KEY `name` (`name`),
  ADD KEY `fk_warehouse_created_by_user` (`created_by`),
  ADD KEY `fk_warehouse_updated_by_user` (`updated_by`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `ContactPerson`
--
ALTER TABLE `ContactPerson`
  MODIFY `contact_person_id` bigint NOT NULL AUTO_INCREMENT COMMENT '聯絡人唯一識別碼';

--
-- AUTO_INCREMENT for table `Customer`
--
ALTER TABLE `Customer`
  MODIFY `customer_id` bigint NOT NULL AUTO_INCREMENT COMMENT '客戶唯一識別碼';

--
-- AUTO_INCREMENT for table `Inventory`
--
ALTER TABLE `Inventory`
  MODIFY `inventory_id` bigint NOT NULL AUTO_INCREMENT COMMENT '庫存記錄唯一識別碼';

--
-- AUTO_INCREMENT for table `InventoryAdjustment`
--
ALTER TABLE `InventoryAdjustment`
  MODIFY `adjustment_id` bigint NOT NULL AUTO_INCREMENT COMMENT '庫存調整單唯一識別碼';

--
-- AUTO_INCREMENT for table `InventoryAdjustmentDetail`
--
ALTER TABLE `InventoryAdjustmentDetail`
  MODIFY `item_id` bigint NOT NULL AUTO_INCREMENT COMMENT '明細項目唯一識別碼';

--
-- AUTO_INCREMENT for table `InventoryMovement`
--
ALTER TABLE `InventoryMovement`
  MODIFY `movement_id` bigint NOT NULL AUTO_INCREMENT COMMENT '異動記錄唯一識別碼';

--
-- AUTO_INCREMENT for table `product`
--
ALTER TABLE `product`
  MODIFY `product_id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品唯一識別碼', AUTO_INCREMENT=514;

--
-- AUTO_INCREMENT for table `productcategory`
--
ALTER TABLE `productcategory`
  MODIFY `category_id` bigint NOT NULL AUTO_INCREMENT COMMENT '分類唯一識別碼', AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `ProductImage`
--
ALTER TABLE `ProductImage`
  MODIFY `image_id` bigint NOT NULL AUTO_INCREMENT COMMENT '圖片唯一識別碼';

--
-- AUTO_INCREMENT for table `ProductSpec`
--
ALTER TABLE `ProductSpec`
  MODIFY `spec_id` bigint NOT NULL AUTO_INCREMENT COMMENT '規格唯一識別碼';

--
-- AUTO_INCREMENT for table `PurchaseOrder`
--
ALTER TABLE `PurchaseOrder`
  MODIFY `purchase_order_id` bigint NOT NULL AUTO_INCREMENT COMMENT '進貨單唯一識別碼';

--
-- AUTO_INCREMENT for table `PurchaseOrderDetail`
--
ALTER TABLE `PurchaseOrderDetail`
  MODIFY `item_id` bigint NOT NULL AUTO_INCREMENT COMMENT '明細項目唯一識別碼';

--
-- AUTO_INCREMENT for table `PurchaseRequest`
--
ALTER TABLE `PurchaseRequest`
  MODIFY `request_id` bigint NOT NULL AUTO_INCREMENT COMMENT '採購申請單唯一識別碼';

--
-- AUTO_INCREMENT for table `PurchaseRequestDetail`
--
ALTER TABLE `PurchaseRequestDetail`
  MODIFY `item_id` bigint NOT NULL AUTO_INCREMENT COMMENT '明細項目唯一識別碼';

--
-- AUTO_INCREMENT for table `PurchaseReturn`
--
ALTER TABLE `PurchaseReturn`
  MODIFY `purchase_return_id` bigint NOT NULL AUTO_INCREMENT COMMENT '進貨退出單唯一識別碼';

--
-- AUTO_INCREMENT for table `PurchaseReturnDetail`
--
ALTER TABLE `PurchaseReturnDetail`
  MODIFY `item_id` bigint NOT NULL AUTO_INCREMENT COMMENT '明細項目唯一識別碼';

--
-- AUTO_INCREMENT for table `Quotation`
--
ALTER TABLE `Quotation`
  MODIFY `quotation_id` bigint NOT NULL AUTO_INCREMENT COMMENT '報價單唯一識別碼';

--
-- AUTO_INCREMENT for table `QuotationDetail`
--
ALTER TABLE `QuotationDetail`
  MODIFY `item_id` bigint NOT NULL AUTO_INCREMENT COMMENT '明細項目唯一識別碼';

--
-- AUTO_INCREMENT for table `SalesOrder`
--
ALTER TABLE `SalesOrder`
  MODIFY `sales_order_id` bigint NOT NULL AUTO_INCREMENT COMMENT '銷售訂單唯一識別碼';

--
-- AUTO_INCREMENT for table `SalesOrderDetail`
--
ALTER TABLE `SalesOrderDetail`
  MODIFY `item_id` bigint NOT NULL AUTO_INCREMENT COMMENT '明細項目唯一識別碼';

--
-- AUTO_INCREMENT for table `SalesReturn`
--
ALTER TABLE `SalesReturn`
  MODIFY `sales_return_id` bigint NOT NULL AUTO_INCREMENT COMMENT '銷貨退回單唯一識別碼';

--
-- AUTO_INCREMENT for table `SalesReturnDetail`
--
ALTER TABLE `SalesReturnDetail`
  MODIFY `item_id` bigint NOT NULL AUTO_INCREMENT COMMENT '明細項目唯一識別碼';

--
-- AUTO_INCREMENT for table `SalesShipment`
--
ALTER TABLE `SalesShipment`
  MODIFY `shipment_id` bigint NOT NULL AUTO_INCREMENT COMMENT '出貨單唯一識別碼';

--
-- AUTO_INCREMENT for table `SalesShipmentDetail`
--
ALTER TABLE `SalesShipmentDetail`
  MODIFY `item_id` bigint NOT NULL AUTO_INCREMENT COMMENT '明細項目唯一識別碼';

--
-- AUTO_INCREMENT for table `Supplier`
--
ALTER TABLE `Supplier`
  MODIFY `supplier_id` bigint NOT NULL AUTO_INCREMENT COMMENT '供應商唯一識別碼';

--
-- AUTO_INCREMENT for table `unit`
--
ALTER TABLE `unit`
  MODIFY `unit_id` bigint NOT NULL AUTO_INCREMENT COMMENT '單位唯一識別碼', AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `UnitConversion`
--
ALTER TABLE `UnitConversion`
  MODIFY `conversion_id` bigint NOT NULL AUTO_INCREMENT COMMENT '換算關係唯一識別碼';

--
-- AUTO_INCREMENT for table `User`
--
ALTER TABLE `User`
  MODIFY `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '使用者唯一識別碼', AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `Warehouse`
--
ALTER TABLE `Warehouse`
  MODIFY `warehouse_id` bigint NOT NULL AUTO_INCREMENT COMMENT '倉庫唯一識別碼';

--
-- Constraints for dumped tables
--

--
-- Constraints for table `ContactPerson`
--
ALTER TABLE `ContactPerson`
  ADD CONSTRAINT `fk_contact_person_customer` FOREIGN KEY (`customer_id`) REFERENCES `Customer` (`customer_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Inventory`
--
ALTER TABLE `Inventory`
  ADD CONSTRAINT `fk_inventory_created_by_user` FOREIGN KEY (`created_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_inventory_product` FOREIGN KEY (`product_id`) REFERENCES `Product` (`product_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_inventory_updated_by_user` FOREIGN KEY (`updated_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_inventory_warehouse` FOREIGN KEY (`warehouse_id`) REFERENCES `Warehouse` (`warehouse_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `InventoryAdjustment`
--
ALTER TABLE `InventoryAdjustment`
  ADD CONSTRAINT `fk_inventory_adjustment_created_by_user` FOREIGN KEY (`created_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_inventory_adjustment_updated_by_user` FOREIGN KEY (`updated_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_inventory_adjustment_warehouse` FOREIGN KEY (`warehouse_id`) REFERENCES `Warehouse` (`warehouse_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `InventoryAdjustmentDetail`
--
ALTER TABLE `InventoryAdjustmentDetail`
  ADD CONSTRAINT `fk_inventory_adjustment_detail_adjustment` FOREIGN KEY (`adjustment_id`) REFERENCES `InventoryAdjustment` (`adjustment_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_inventory_adjustment_detail_created_by_user` FOREIGN KEY (`created_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_inventory_adjustment_detail_product` FOREIGN KEY (`product_id`) REFERENCES `Product` (`product_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_inventory_adjustment_detail_unit` FOREIGN KEY (`unit_id`) REFERENCES `Unit` (`unit_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_inventory_adjustment_detail_updated_by_user` FOREIGN KEY (`updated_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `InventoryMovement`
--
ALTER TABLE `InventoryMovement`
  ADD CONSTRAINT `fk_inventory_movement_product` FOREIGN KEY (`product_id`) REFERENCES `Product` (`product_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_inventory_movement_recorded_by_user` FOREIGN KEY (`recorded_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_inventory_movement_warehouse` FOREIGN KEY (`warehouse_id`) REFERENCES `Warehouse` (`warehouse_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `product`
--
ALTER TABLE `product`
  ADD CONSTRAINT `fk_product_category` FOREIGN KEY (`category_id`) REFERENCES `ProductCategory` (`category_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_product_created_by_user` FOREIGN KEY (`created_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_product_unit` FOREIGN KEY (`unit_id`) REFERENCES `Unit` (`unit_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_product_updated_by_user` FOREIGN KEY (`updated_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `productcategory`
--
ALTER TABLE `productcategory`
  ADD CONSTRAINT `fk_product_category_created_by_user` FOREIGN KEY (`created_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_product_category_parent` FOREIGN KEY (`parent_category_id`) REFERENCES `ProductCategory` (`category_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_product_category_updated_by_user` FOREIGN KEY (`updated_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `ProductImage`
--
ALTER TABLE `ProductImage`
  ADD CONSTRAINT `fk_product_image_product` FOREIGN KEY (`product_id`) REFERENCES `Product` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_product_image_uploaded_by_user` FOREIGN KEY (`uploaded_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `ProductSpec`
--
ALTER TABLE `ProductSpec`
  ADD CONSTRAINT `fk_product_spec_created_by_user` FOREIGN KEY (`created_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_product_spec_product` FOREIGN KEY (`product_id`) REFERENCES `Product` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_product_spec_updated_by_user` FOREIGN KEY (`updated_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `PurchaseOrder`
--
ALTER TABLE `PurchaseOrder`
  ADD CONSTRAINT `fk_purchase_order_created_by_user` FOREIGN KEY (`created_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_purchase_order_purchase_request` FOREIGN KEY (`purchase_request_id`) REFERENCES `PurchaseRequest` (`request_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_purchase_order_supplier` FOREIGN KEY (`supplier_id`) REFERENCES `Supplier` (`supplier_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_purchase_order_updated_by_user` FOREIGN KEY (`updated_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `PurchaseOrderDetail`
--
ALTER TABLE `PurchaseOrderDetail`
  ADD CONSTRAINT `fk_purchase_order_detail_created_by_user` FOREIGN KEY (`created_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_purchase_order_detail_product` FOREIGN KEY (`product_id`) REFERENCES `Product` (`product_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_purchase_order_detail_purchase_order` FOREIGN KEY (`purchase_order_id`) REFERENCES `PurchaseOrder` (`purchase_order_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_purchase_order_detail_purchase_request_item` FOREIGN KEY (`purchase_request_item_id`) REFERENCES `PurchaseRequestDetail` (`item_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_purchase_order_detail_updated_by_user` FOREIGN KEY (`updated_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_purchase_order_detail_warehouse` FOREIGN KEY (`warehouse_id`) REFERENCES `Warehouse` (`warehouse_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `PurchaseRequest`
--
ALTER TABLE `PurchaseRequest`
  ADD CONSTRAINT `fk_purchase_request_created_by_user` FOREIGN KEY (`created_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_purchase_request_sales_order` FOREIGN KEY (`sales_order_id`) REFERENCES `SalesOrder` (`sales_order_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_purchase_request_updated_by_user` FOREIGN KEY (`updated_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `PurchaseRequestDetail`
--
ALTER TABLE `PurchaseRequestDetail`
  ADD CONSTRAINT `fk_purchase_request_detail_created_by_user` FOREIGN KEY (`created_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_purchase_request_detail_product` FOREIGN KEY (`product_id`) REFERENCES `Product` (`product_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_purchase_request_detail_request` FOREIGN KEY (`request_id`) REFERENCES `PurchaseRequest` (`request_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_purchase_request_detail_unit` FOREIGN KEY (`unit_id`) REFERENCES `Unit` (`unit_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_purchase_request_detail_updated_by_user` FOREIGN KEY (`updated_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `PurchaseReturn`
--
ALTER TABLE `PurchaseReturn`
  ADD CONSTRAINT `fk_purchase_return_contact_person` FOREIGN KEY (`contact_person_id`) REFERENCES `ContactPerson` (`contact_person_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_purchase_return_created_by_user` FOREIGN KEY (`created_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_purchase_return_original_po` FOREIGN KEY (`original_purchase_order_id`) REFERENCES `PurchaseOrder` (`purchase_order_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_purchase_return_supplier` FOREIGN KEY (`supplier_id`) REFERENCES `Supplier` (`supplier_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_purchase_return_updated_by_user` FOREIGN KEY (`updated_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `PurchaseReturnDetail`
--
ALTER TABLE `PurchaseReturnDetail`
  ADD CONSTRAINT `fk_purchase_return_detail_created_by_user` FOREIGN KEY (`created_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_purchase_return_detail_product` FOREIGN KEY (`product_id`) REFERENCES `Product` (`product_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_purchase_return_detail_purchase_return` FOREIGN KEY (`purchase_return_id`) REFERENCES `PurchaseReturn` (`purchase_return_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_purchase_return_detail_unit` FOREIGN KEY (`unit_id`) REFERENCES `Unit` (`unit_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_purchase_return_detail_updated_by_user` FOREIGN KEY (`updated_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_purchase_return_detail_warehouse` FOREIGN KEY (`returned_from_warehouse_id`) REFERENCES `Warehouse` (`warehouse_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `Quotation`
--
ALTER TABLE `Quotation`
  ADD CONSTRAINT `fk_quotation_contact_person` FOREIGN KEY (`contact_person_id`) REFERENCES `ContactPerson` (`contact_person_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_quotation_created_by_user` FOREIGN KEY (`created_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_quotation_customer` FOREIGN KEY (`customer_id`) REFERENCES `Customer` (`customer_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_quotation_updated_by_user` FOREIGN KEY (`updated_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `QuotationDetail`
--
ALTER TABLE `QuotationDetail`
  ADD CONSTRAINT `fk_quotation_detail_created_by_user` FOREIGN KEY (`created_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_quotation_detail_product` FOREIGN KEY (`product_id`) REFERENCES `Product` (`product_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_quotation_detail_quotation` FOREIGN KEY (`quotation_id`) REFERENCES `Quotation` (`quotation_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_quotation_detail_unit` FOREIGN KEY (`unit_id`) REFERENCES `Unit` (`unit_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_quotation_detail_updated_by_user` FOREIGN KEY (`updated_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `SalesOrder`
--
ALTER TABLE `SalesOrder`
  ADD CONSTRAINT `fk_sales_order_contact_person` FOREIGN KEY (`contact_person_id`) REFERENCES `ContactPerson` (`contact_person_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_sales_order_created_by_user` FOREIGN KEY (`created_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_sales_order_customer` FOREIGN KEY (`customer_id`) REFERENCES `Customer` (`customer_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_sales_order_quotation` FOREIGN KEY (`quotation_id`) REFERENCES `Quotation` (`quotation_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_sales_order_updated_by_user` FOREIGN KEY (`updated_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `SalesOrderDetail`
--
ALTER TABLE `SalesOrderDetail`
  ADD CONSTRAINT `fk_sales_order_detail_created_by_user` FOREIGN KEY (`created_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_sales_order_detail_product` FOREIGN KEY (`product_id`) REFERENCES `Product` (`product_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_sales_order_detail_sales_order` FOREIGN KEY (`sales_order_id`) REFERENCES `SalesOrder` (`sales_order_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_sales_order_detail_unit` FOREIGN KEY (`unit_id`) REFERENCES `Unit` (`unit_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_sales_order_detail_updated_by_user` FOREIGN KEY (`updated_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `SalesReturn`
--
ALTER TABLE `SalesReturn`
  ADD CONSTRAINT `fk_sales_return_contact_person` FOREIGN KEY (`contact_person_id`) REFERENCES `ContactPerson` (`contact_person_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_sales_return_created_by_user` FOREIGN KEY (`created_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_sales_return_customer` FOREIGN KEY (`customer_id`) REFERENCES `Customer` (`customer_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_sales_return_original_shipment` FOREIGN KEY (`original_shipment_id`) REFERENCES `SalesShipment` (`shipment_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_sales_return_original_so` FOREIGN KEY (`original_sales_order_id`) REFERENCES `SalesOrder` (`sales_order_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_sales_return_updated_by_user` FOREIGN KEY (`updated_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `SalesReturnDetail`
--
ALTER TABLE `SalesReturnDetail`
  ADD CONSTRAINT `fk_sales_return_detail_created_by_user` FOREIGN KEY (`created_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_sales_return_detail_product` FOREIGN KEY (`product_id`) REFERENCES `Product` (`product_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_sales_return_detail_sales_return` FOREIGN KEY (`sales_return_id`) REFERENCES `SalesReturn` (`sales_return_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_sales_return_detail_unit` FOREIGN KEY (`unit_id`) REFERENCES `Unit` (`unit_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_sales_return_detail_updated_by_user` FOREIGN KEY (`updated_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_sales_return_detail_warehouse` FOREIGN KEY (`returned_to_warehouse_id`) REFERENCES `Warehouse` (`warehouse_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `SalesShipment`
--
ALTER TABLE `SalesShipment`
  ADD CONSTRAINT `fk_sales_shipment_created_by_user` FOREIGN KEY (`created_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_sales_shipment_customer` FOREIGN KEY (`customer_id`) REFERENCES `Customer` (`customer_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_sales_shipment_sales_order` FOREIGN KEY (`sales_order_id`) REFERENCES `SalesOrder` (`sales_order_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_sales_shipment_updated_by_user` FOREIGN KEY (`updated_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_sales_shipment_warehouse` FOREIGN KEY (`warehouse_id`) REFERENCES `Warehouse` (`warehouse_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `SalesShipmentDetail`
--
ALTER TABLE `SalesShipmentDetail`
  ADD CONSTRAINT `fk_sales_shipment_detail_created_by_user` FOREIGN KEY (`created_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_sales_shipment_detail_product` FOREIGN KEY (`product_id`) REFERENCES `Product` (`product_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_sales_shipment_detail_sales_order_item` FOREIGN KEY (`sales_order_item_id`) REFERENCES `SalesOrderDetail` (`item_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_sales_shipment_detail_shipment` FOREIGN KEY (`shipment_id`) REFERENCES `SalesShipment` (`shipment_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_sales_shipment_detail_unit` FOREIGN KEY (`unit_id`) REFERENCES `Unit` (`unit_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_sales_shipment_detail_updated_by_user` FOREIGN KEY (`updated_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `Supplier`
--
ALTER TABLE `Supplier`
  ADD CONSTRAINT `fk_supplier_created_by_user` FOREIGN KEY (`created_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_supplier_updated_by_user` FOREIGN KEY (`updated_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `unit`
--
ALTER TABLE `unit`
  ADD CONSTRAINT `fk_unit_created_by_user` FOREIGN KEY (`created_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_unit_updated_by_user` FOREIGN KEY (`updated_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `UnitConversion`
--
ALTER TABLE `UnitConversion`
  ADD CONSTRAINT `fk_unit_conversion_created_by_user` FOREIGN KEY (`created_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_unit_conversion_from_unit` FOREIGN KEY (`from_unit_id`) REFERENCES `Unit` (`unit_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_unit_conversion_to_unit` FOREIGN KEY (`to_unit_id`) REFERENCES `Unit` (`unit_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_unit_conversion_updated_by_user` FOREIGN KEY (`updated_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `Warehouse`
--
ALTER TABLE `Warehouse`
  ADD CONSTRAINT `fk_warehouse_created_by_user` FOREIGN KEY (`created_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_warehouse_updated_by_user` FOREIGN KEY (`updated_by`) REFERENCES `User` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
