-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- 主機： localhost:8889
-- 產生時間： 2025 年 06 月 09 日 03:17
-- 伺服器版本： 8.0.40
-- PHP 版本： 8.3.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 資料庫： `erp_module`
--

-- --------------------------------------------------------

--
-- 資料表結構 `contact_persons`
--

CREATE TABLE `contact_persons` (
  `contact_person_id` bigint NOT NULL COMMENT '聯絡人唯一識別碼',
  `customer_id` bigint NOT NULL COMMENT '關聯到 customers 表的 ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '聯絡人姓名',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '職稱',
  `phone_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '電話號碼',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '電子郵件',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最後更新時間'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客戶聯絡人資訊表';

-- --------------------------------------------------------

--
-- 資料表結構 `customers`
--

CREATE TABLE `customers` (
  `customer_id` bigint NOT NULL COMMENT '客戶唯一識別碼',
  `customer_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客戶代號',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客戶名稱',
  `contact_person_id` bigint DEFAULT NULL COMMENT '關聯到 contact_persons 表的 ID',
  `phone_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '電話號碼',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '電子郵件',
  `address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '客戶地址',
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '客戶是否啟用',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最後更新時間'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客戶基本資料表';

-- --------------------------------------------------------

--
-- 資料表結構 `inventories`
--

CREATE TABLE `inventories` (
  `inventory_id` bigint NOT NULL COMMENT '庫存記錄唯一識別碼',
  `product_id` bigint NOT NULL COMMENT '關聯到 products 表',
  `warehouse_id` bigint NOT NULL COMMENT '關聯到 warehouses 表',
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
-- 資料表結構 `inventory_adjustments`
--

CREATE TABLE `inventory_adjustments` (
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
-- 資料表結構 `inventory_adjustment_details`
--

CREATE TABLE `inventory_adjustment_details` (
  `item_id` bigint NOT NULL COMMENT '明細項目唯一識別碼',
  `adjustment_id` bigint NOT NULL COMMENT '關聯到 inventory_adjustments 表',
  `item_sequence` int NOT NULL COMMENT '明細項目的序號',
  `product_id` bigint NOT NULL COMMENT '關聯到 products 表',
  `specification` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品規格 (冗餘自 products 表)',
  `unit_id` bigint NOT NULL COMMENT '關聯到 units 表',
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
-- 資料表結構 `inventory_movements`
--

CREATE TABLE `inventory_movements` (
  `movement_id` bigint NOT NULL COMMENT '異動記錄唯一識別碼',
  `product_id` bigint NOT NULL COMMENT '關聯到 products 表',
  `warehouse_id` bigint NOT NULL COMMENT '關聯到 warehouses 表',
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
-- 資料表結構 `products`
--

CREATE TABLE `products` (
  `product_id` bigint NOT NULL COMMENT '商品唯一識別碼',
  `product_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品代號',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品名稱',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '商品描述',
  `category_id` bigint DEFAULT NULL COMMENT '商品分類 ID',
  `unit_id` bigint NOT NULL COMMENT '商品單位 ID',
  `is_purchasable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否採購品',
  `is_salable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否銷售品',
  `sourcing_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PURCHASED' COMMENT '採購類型 (PURCHASED: 外購品, SUBASSEMBLY: 半成品, MANUFACTURED: 自製品)',
  `cost_method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '成本方式(Default AVERAGE)',
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

-- --------------------------------------------------------

--
-- 資料表結構 `product_categories`
--

CREATE TABLE `product_categories` (
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

-- --------------------------------------------------------

--
-- 資料表結構 `product_images`
--

CREATE TABLE `product_images` (
  `image_id` bigint NOT NULL COMMENT '圖片唯一識別碼',
  `product_id` bigint NOT NULL COMMENT '關聯到 products 表',
  `image_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '圖片儲存的路徑或 URL',
  `is_main_image` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否為主圖',
  `sort_order` int NOT NULL DEFAULT '0' COMMENT '顯示排序',
  `uploaded_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上傳時間',
  `uploaded_by` bigint NOT NULL COMMENT '上傳人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品圖片表';

-- --------------------------------------------------------

--
-- 資料表結構 `product_specs`
--

CREATE TABLE `product_specs` (
  `spec_id` bigint NOT NULL COMMENT '規格唯一識別碼',
  `product_id` bigint NOT NULL COMMENT '關聯到 products 表',
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
-- 資料表結構 `purchase_orders`
--

CREATE TABLE `purchase_orders` (
  `purchase_order_id` bigint NOT NULL COMMENT '進貨單唯一識別碼',
  `order_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '進貨單號',
  `purchase_request_id` bigint DEFAULT NULL COMMENT '關聯到 purchase_requests 表 (表示此採購訂單是由哪個採購申請轉化而來)',
  `order_date` date NOT NULL COMMENT '進貨日期',
  `supplier_id` bigint NOT NULL COMMENT '關聯到 suppliers 表',
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
-- 資料表結構 `purchase_order_details`
--

CREATE TABLE `purchase_order_details` (
  `item_id` bigint NOT NULL COMMENT '明細項目唯一識別碼',
  `purchase_order_id` bigint NOT NULL COMMENT '關聯到 purchase_orders 表',
  `purchase_request_item_id` bigint DEFAULT NULL COMMENT '關聯到 purchase_request_details 表 (表示此採購訂單明細是來自哪個採購申請明細)',
  `item_sequence` int NOT NULL COMMENT '明細項目的序號',
  `product_id` bigint NOT NULL COMMENT '關聯到 products 表',
  `unit_price` decimal(18,2) NOT NULL COMMENT '商品單價',
  `quantity` decimal(18,2) NOT NULL COMMENT '進貨數量',
  `item_amount` decimal(18,2) NOT NULL COMMENT '明細項目金額',
  `item_tax_amount` decimal(18,2) NOT NULL COMMENT '明細項目稅額',
  `item_net_amount` decimal(18,2) NOT NULL COMMENT '明細項目未稅金額',
  `warehouse_id` bigint NOT NULL COMMENT '關聯到 warehouses 表',
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
-- 資料表結構 `purchase_requests`
--

CREATE TABLE `purchase_requests` (
  `request_id` bigint NOT NULL COMMENT '採購申請單唯一識別碼',
  `request_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '採購申請單號',
  `request_date` date NOT NULL COMMENT '申請日期',
  `sales_order_id` bigint DEFAULT NULL COMMENT '關聯到 sales_orders 表 (表示此採購申請是因哪個銷售訂單而發起)',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'DRAFT' COMMENT '採購申請狀態 (例如：DRAFT, PENDING_APPROVAL, APPROVED, CONVERTED_TO_PO, CANCELLED)',
  `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '備註',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
  `created_by` bigint NOT NULL COMMENT '建立人',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最後更新時間',
  `updated_by` bigint NOT NULL COMMENT '最後更新人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='採購申請單主表';

-- --------------------------------------------------------

--
-- 資料表結構 `purchase_request_details`
--

CREATE TABLE `purchase_request_details` (
  `item_id` bigint NOT NULL COMMENT '明細項目唯一識別碼',
  `request_id` bigint NOT NULL COMMENT '關聯到 purchase_requests 表',
  `item_sequence` int NOT NULL COMMENT '明細項目的序號',
  `product_id` bigint NOT NULL COMMENT '關聯到 products 表',
  `requested_quantity` decimal(18,2) NOT NULL COMMENT '申請採購數量',
  `unit_id` bigint NOT NULL COMMENT '關聯到 units 表',
  `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '明細備註',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
  `created_by` bigint NOT NULL COMMENT '建立人',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最後更新時間',
  `updated_by` bigint NOT NULL COMMENT '最後更新人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='採購申請單明細表';

-- --------------------------------------------------------

--
-- 資料表結構 `purchase_returns`
--

CREATE TABLE `purchase_returns` (
  `purchase_return_id` bigint NOT NULL COMMENT '進貨退出單唯一識別碼',
  `return_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '進貨退出單號',
  `return_date` date NOT NULL COMMENT '退回日期',
  `supplier_id` bigint NOT NULL COMMENT '關聯到 suppliers 表',
  `original_purchase_order_id` bigint DEFAULT NULL COMMENT '關聯到原進貨單',
  `contact_person_id` bigint DEFAULT NULL COMMENT '關聯到 contact_persons 表',
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
-- 資料表結構 `purchase_return_details`
--

CREATE TABLE `purchase_return_details` (
  `item_id` bigint NOT NULL COMMENT '明細項目唯一識別碼',
  `purchase_return_id` bigint NOT NULL COMMENT '關聯到 purchase_returns 表',
  `item_sequence` int NOT NULL COMMENT '明細項目的序號',
  `product_id` bigint NOT NULL COMMENT '關聯到 products 表',
  `specification` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品規格 (冗餘自 products 表)',
  `unit_id` bigint NOT NULL COMMENT '關聯到 units 表',
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
-- 資料表結構 `quotations`
--

CREATE TABLE `quotations` (
  `quotation_id` bigint NOT NULL COMMENT '報價單唯一識別碼',
  `quotation_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '報價單號',
  `quotation_date` date NOT NULL COMMENT '報價日期',
  `customer_id` bigint NOT NULL COMMENT '關聯到 customers 表',
  `contact_person_id` bigint DEFAULT NULL COMMENT '關聯到 contact_persons 表',
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
-- 資料表結構 `quotation_details`
--

CREATE TABLE `quotation_details` (
  `item_id` bigint NOT NULL COMMENT '明細項目唯一識別碼',
  `quotation_id` bigint NOT NULL COMMENT '關聯到 quotations 表',
  `item_sequence` int NOT NULL COMMENT '明細項目的序號',
  `product_id` bigint NOT NULL COMMENT '關聯到 products 表',
  `specification` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品規格 (冗餘自 products 表)',
  `unit_id` bigint NOT NULL COMMENT '關聯到 units 表',
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
-- 資料表結構 `sales_orders`
--

CREATE TABLE `sales_orders` (
  `sales_order_id` bigint NOT NULL COMMENT '銷售訂單唯一識別碼',
  `order_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '銷售訂單號',
  `order_date` date NOT NULL COMMENT '訂單日期',
  `customer_id` bigint NOT NULL COMMENT '關聯到 customers 表',
  `contact_person_id` bigint DEFAULT NULL COMMENT '關聯到 contact_persons 表',
  `quotation_id` bigint DEFAULT NULL COMMENT '關聯到 quotations 表 (表示訂單可能來自某份報價單)',
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
-- 資料表結構 `sales_order_details`
--

CREATE TABLE `sales_order_details` (
  `item_id` bigint NOT NULL COMMENT '明細項目唯一識別碼',
  `sales_order_id` bigint NOT NULL COMMENT '關聯到 sales_orders 表',
  `item_sequence` int NOT NULL COMMENT '明細項目的序號',
  `product_id` bigint NOT NULL COMMENT '關聯到 products 表',
  `specification` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品規格 (冗餘自 products 表)',
  `unit_id` bigint NOT NULL COMMENT '關聯到 units 表',
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
-- 資料表結構 `sales_returns`
--

CREATE TABLE `sales_returns` (
  `sales_return_id` bigint NOT NULL COMMENT '銷貨退回單唯一識別碼',
  `return_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '銷貨退回單號',
  `return_date` date NOT NULL COMMENT '退回日期',
  `customer_id` bigint NOT NULL COMMENT '關聯到 customers 表',
  `original_sales_order_id` bigint DEFAULT NULL COMMENT '關聯到原銷售訂單',
  `original_shipment_id` bigint DEFAULT NULL COMMENT '關聯到原出貨單',
  `contact_person_id` bigint DEFAULT NULL COMMENT '關聯到 contact_persons 表',
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
-- 資料表結構 `sales_return_details`
--

CREATE TABLE `sales_return_details` (
  `item_id` bigint NOT NULL COMMENT '明細項目唯一識別碼',
  `sales_return_id` bigint NOT NULL COMMENT '關聯到 sales_returns 表',
  `item_sequence` int NOT NULL COMMENT '明細項目的序號',
  `product_id` bigint NOT NULL COMMENT '關聯到 products 表',
  `specification` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品規格 (冗餘自 products 表)',
  `unit_id` bigint NOT NULL COMMENT '關聯到 units 表',
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
-- 資料表結構 `sales_shipments`
--

CREATE TABLE `sales_shipments` (
  `shipment_id` bigint NOT NULL COMMENT '出貨單唯一識別碼',
  `shipment_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '出貨單號',
  `sales_order_id` bigint DEFAULT NULL COMMENT '關聯到 sales_orders 表 (表示出貨單可能來自某份銷售訂單)',
  `shipment_date` date NOT NULL COMMENT '出貨日期',
  `customer_id` bigint NOT NULL COMMENT '關聯到 customers 表',
  `warehouse_id` bigint NOT NULL COMMENT '關聯到 warehouses 表 (從哪個倉庫出貨)',
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
-- 資料表結構 `sales_shipment_details`
--

CREATE TABLE `sales_shipment_details` (
  `item_id` bigint NOT NULL COMMENT '明細項目唯一識別碼',
  `shipment_id` bigint NOT NULL COMMENT '關聯到 sales_shipments 表',
  `sales_order_item_id` bigint DEFAULT NULL COMMENT '關聯到 sales_order_details 表 (用於分批出貨追溯)',
  `product_id` bigint NOT NULL COMMENT '關聯到 products 表',
  `shipped_quantity` decimal(18,2) NOT NULL COMMENT '實際出貨數量',
  `unit_id` bigint NOT NULL COMMENT '關聯到 units 表',
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
-- 資料表結構 `suppliers`
--

CREATE TABLE `suppliers` (
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
-- 資料表結構 `units`
--

CREATE TABLE `units` (
  `unit_id` bigint NOT NULL COMMENT '單位唯一識別碼',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '單位名稱 (如：個, 箱, 公斤)',
  `is_base_unit` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否為基本計量單位',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
  `created_by` bigint NOT NULL COMMENT '建立人',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最後更新時間',
  `updated_by` bigint NOT NULL COMMENT '最後更新人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品單位表';

-- --------------------------------------------------------

--
-- 資料表結構 `unit_conversions`
--

CREATE TABLE `unit_conversions` (
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
-- 資料表結構 `users`
--

CREATE TABLE `users` (
  `user_id` bigint NOT NULL COMMENT '使用者唯一識別碼',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '使用者名稱',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '電子郵件',
  `password_hash` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密碼哈希值',
  `role_id` bigint NOT NULL COMMENT '關聯到 Role 表 (權限管理) 的 ID',
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否啟用',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最後更新時間'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系統使用者資訊表';

-- --------------------------------------------------------

--
-- 資料表結構 `warehouses`
--

CREATE TABLE `warehouses` (
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
-- 已傾印資料表的索引
--

--
-- 資料表索引 `contact_persons`
--
ALTER TABLE `contact_persons`
  ADD PRIMARY KEY (`contact_person_id`),
  ADD KEY `fk_contact_persons_customer_id` (`customer_id`);

--
-- 資料表索引 `customers`
--
ALTER TABLE `customers`
  ADD PRIMARY KEY (`customer_id`),
  ADD UNIQUE KEY `uk_customers_customer_code` (`customer_code`),
  ADD KEY `fk_customers_contact_person_id` (`contact_person_id`);

--
-- 資料表索引 `inventories`
--
ALTER TABLE `inventories`
  ADD PRIMARY KEY (`inventory_id`),
  ADD UNIQUE KEY `uk_inventories_product_id_warehouse_id` (`product_id`,`warehouse_id`),
  ADD KEY `fk_inventories_warehouse_id` (`warehouse_id`),
  ADD KEY `fk_inventories_created_by_user_id` (`created_by`),
  ADD KEY `fk_inventories_updated_by_user_id` (`updated_by`);

--
-- 資料表索引 `inventory_adjustments`
--
ALTER TABLE `inventory_adjustments`
  ADD PRIMARY KEY (`adjustment_id`),
  ADD UNIQUE KEY `uk_inventory_adjustments_adjustment_number` (`adjustment_number`),
  ADD KEY `fk_inventory_adjustments_warehouse_id` (`warehouse_id`),
  ADD KEY `fk_inventory_adjustments_created_by_user_id` (`created_by`),
  ADD KEY `fk_inventory_adjustments_updated_by_user_id` (`updated_by`);

--
-- 資料表索引 `inventory_adjustment_details`
--
ALTER TABLE `inventory_adjustment_details`
  ADD PRIMARY KEY (`item_id`),
  ADD KEY `fk_inventory_adjustment_details_adjustment_id` (`adjustment_id`),
  ADD KEY `fk_inventory_adjustment_details_product_id` (`product_id`),
  ADD KEY `fk_inventory_adjustment_details_unit_id` (`unit_id`),
  ADD KEY `fk_inventory_adjustment_details_created_by_user_id` (`created_by`),
  ADD KEY `fk_inventory_adjustment_details_updated_by_user_id` (`updated_by`);

--
-- 資料表索引 `inventory_movements`
--
ALTER TABLE `inventory_movements`
  ADD PRIMARY KEY (`movement_id`),
  ADD KEY `fk_inventory_movements_product_id` (`product_id`),
  ADD KEY `fk_inventory_movements_warehouse_id` (`warehouse_id`),
  ADD KEY `fk_inventory_movements_recorded_by_user_id` (`recorded_by`);

--
-- 資料表索引 `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`product_id`),
  ADD UNIQUE KEY `uk_products_product_code` (`product_code`),
  ADD KEY `fk_products_category_id` (`category_id`),
  ADD KEY `fk_products_unit_id` (`unit_id`),
  ADD KEY `fk_products_created_by_user_id` (`created_by`),
  ADD KEY `fk_products_updated_by_user_id` (`updated_by`);

--
-- 資料表索引 `product_categories`
--
ALTER TABLE `product_categories`
  ADD PRIMARY KEY (`category_id`),
  ADD UNIQUE KEY `uk_product_categories_name` (`name`),
  ADD KEY `fk_product_categories_parent_category_id` (`parent_category_id`),
  ADD KEY `fk_product_categories_created_by_user_id` (`created_by`),
  ADD KEY `fk_product_categories_updated_by_user_id` (`updated_by`);

--
-- 資料表索引 `product_images`
--
ALTER TABLE `product_images`
  ADD PRIMARY KEY (`image_id`),
  ADD KEY `fk_product_images_product_id` (`product_id`),
  ADD KEY `fk_product_images_uploaded_by_user_id` (`uploaded_by`);

--
-- 資料表索引 `product_specs`
--
ALTER TABLE `product_specs`
  ADD PRIMARY KEY (`spec_id`),
  ADD UNIQUE KEY `uk_product_specs_sku_code` (`sku_code`),
  ADD KEY `fk_product_specs_product_id` (`product_id`),
  ADD KEY `fk_product_specs_created_by_user_id` (`created_by`),
  ADD KEY `fk_product_specs_updated_by_user_id` (`updated_by`);

--
-- 資料表索引 `purchase_orders`
--
ALTER TABLE `purchase_orders`
  ADD PRIMARY KEY (`purchase_order_id`),
  ADD UNIQUE KEY `uk_purchase_orders_order_number` (`order_number`),
  ADD KEY `fk_purchase_orders_purchase_request_id` (`purchase_request_id`),
  ADD KEY `fk_purchase_orders_supplier_id` (`supplier_id`),
  ADD KEY `fk_purchase_orders_created_by_user_id` (`created_by`),
  ADD KEY `fk_purchase_orders_updated_by_user_id` (`updated_by`);

--
-- 資料表索引 `purchase_order_details`
--
ALTER TABLE `purchase_order_details`
  ADD PRIMARY KEY (`item_id`),
  ADD KEY `fk_purchase_order_details_purchase_order_id` (`purchase_order_id`),
  ADD KEY `fk_purchase_order_details_purchase_request_item_id` (`purchase_request_item_id`),
  ADD KEY `fk_purchase_order_details_product_id` (`product_id`),
  ADD KEY `fk_purchase_order_details_warehouse_id` (`warehouse_id`),
  ADD KEY `fk_purchase_order_details_created_by_user_id` (`created_by`),
  ADD KEY `fk_purchase_order_details_updated_by_user_id` (`updated_by`);

--
-- 資料表索引 `purchase_requests`
--
ALTER TABLE `purchase_requests`
  ADD PRIMARY KEY (`request_id`),
  ADD UNIQUE KEY `uk_purchase_requests_request_number` (`request_number`),
  ADD KEY `fk_purchase_requests_sales_order_id` (`sales_order_id`),
  ADD KEY `fk_purchase_requests_created_by_user_id` (`created_by`),
  ADD KEY `fk_purchase_requests_updated_by_user_id` (`updated_by`);

--
-- 資料表索引 `purchase_request_details`
--
ALTER TABLE `purchase_request_details`
  ADD PRIMARY KEY (`item_id`),
  ADD KEY `fk_purchase_request_details_request_id` (`request_id`),
  ADD KEY `fk_purchase_request_details_product_id` (`product_id`),
  ADD KEY `fk_purchase_request_details_unit_id` (`unit_id`),
  ADD KEY `fk_purchase_request_details_created_by_user_id` (`created_by`),
  ADD KEY `fk_purchase_request_details_updated_by_user_id` (`updated_by`);

--
-- 資料表索引 `purchase_returns`
--
ALTER TABLE `purchase_returns`
  ADD PRIMARY KEY (`purchase_return_id`),
  ADD UNIQUE KEY `uk_purchase_returns_return_number` (`return_number`),
  ADD KEY `fk_purchase_returns_supplier_id` (`supplier_id`),
  ADD KEY `fk_purchase_returns_original_purchase_order_id` (`original_purchase_order_id`),
  ADD KEY `fk_purchase_returns_contact_person_id` (`contact_person_id`),
  ADD KEY `fk_purchase_returns_created_by_user_id` (`created_by`),
  ADD KEY `fk_purchase_returns_updated_by_user_id` (`updated_by`);

--
-- 資料表索引 `purchase_return_details`
--
ALTER TABLE `purchase_return_details`
  ADD PRIMARY KEY (`item_id`),
  ADD KEY `fk_purchase_return_details_purchase_return_id` (`purchase_return_id`),
  ADD KEY `fk_purchase_return_details_product_id` (`product_id`),
  ADD KEY `fk_purchase_return_details_unit_id` (`unit_id`),
  ADD KEY `fk_purchase_return_details_returned_from_warehouse_id` (`returned_from_warehouse_id`),
  ADD KEY `fk_purchase_return_details_created_by_user_id` (`created_by`),
  ADD KEY `fk_purchase_return_details_updated_by_user_id` (`updated_by`);

--
-- 資料表索引 `quotations`
--
ALTER TABLE `quotations`
  ADD PRIMARY KEY (`quotation_id`),
  ADD UNIQUE KEY `uk_quotations_quotation_number` (`quotation_number`),
  ADD KEY `fk_quotations_customer_id` (`customer_id`),
  ADD KEY `fk_quotations_contact_person_id` (`contact_person_id`),
  ADD KEY `fk_quotations_created_by_user_id` (`created_by`),
  ADD KEY `fk_quotations_updated_by_user_id` (`updated_by`);

--
-- 資料表索引 `quotation_details`
--
ALTER TABLE `quotation_details`
  ADD PRIMARY KEY (`item_id`),
  ADD KEY `fk_quotation_details_quotation_id` (`quotation_id`),
  ADD KEY `fk_quotation_details_product_id` (`product_id`),
  ADD KEY `fk_quotation_details_unit_id` (`unit_id`),
  ADD KEY `fk_quotation_details_created_by_user_id` (`created_by`),
  ADD KEY `fk_quotation_details_updated_by_user_id` (`updated_by`);

--
-- 資料表索引 `sales_orders`
--
ALTER TABLE `sales_orders`
  ADD PRIMARY KEY (`sales_order_id`),
  ADD UNIQUE KEY `uk_sales_orders_order_number` (`order_number`),
  ADD KEY `fk_sales_orders_customer_id` (`customer_id`),
  ADD KEY `fk_sales_orders_contact_person_id` (`contact_person_id`),
  ADD KEY `fk_sales_orders_quotation_id` (`quotation_id`),
  ADD KEY `fk_sales_orders_created_by_user_id` (`created_by`),
  ADD KEY `fk_sales_orders_updated_by_user_id` (`updated_by`);

--
-- 資料表索引 `sales_order_details`
--
ALTER TABLE `sales_order_details`
  ADD PRIMARY KEY (`item_id`),
  ADD KEY `fk_sales_order_details_sales_order_id` (`sales_order_id`),
  ADD KEY `fk_sales_order_details_product_id` (`product_id`),
  ADD KEY `fk_sales_order_details_unit_id` (`unit_id`),
  ADD KEY `fk_sales_order_details_created_by_user_id` (`created_by`),
  ADD KEY `fk_sales_order_details_updated_by_user_id` (`updated_by`);

--
-- 資料表索引 `sales_returns`
--
ALTER TABLE `sales_returns`
  ADD PRIMARY KEY (`sales_return_id`),
  ADD UNIQUE KEY `uk_sales_returns_return_number` (`return_number`),
  ADD KEY `fk_sales_returns_customer_id` (`customer_id`),
  ADD KEY `fk_sales_returns_original_sales_order_id` (`original_sales_order_id`),
  ADD KEY `fk_sales_returns_original_shipment_id` (`original_shipment_id`),
  ADD KEY `fk_sales_returns_contact_person_id` (`contact_person_id`),
  ADD KEY `fk_sales_returns_created_by_user_id` (`created_by`),
  ADD KEY `fk_sales_returns_updated_by_user_id` (`updated_by`);

--
-- 資料表索引 `sales_return_details`
--
ALTER TABLE `sales_return_details`
  ADD PRIMARY KEY (`item_id`),
  ADD KEY `fk_sales_return_details_sales_return_id` (`sales_return_id`),
  ADD KEY `fk_sales_return_details_product_id` (`product_id`),
  ADD KEY `fk_sales_return_details_unit_id` (`unit_id`),
  ADD KEY `fk_sales_return_details_returned_to_warehouse_id` (`returned_to_warehouse_id`),
  ADD KEY `fk_sales_return_details_created_by_user_id` (`created_by`),
  ADD KEY `fk_sales_return_details_updated_by_user_id` (`updated_by`);

--
-- 資料表索引 `sales_shipments`
--
ALTER TABLE `sales_shipments`
  ADD PRIMARY KEY (`shipment_id`),
  ADD UNIQUE KEY `uk_sales_shipments_shipment_number` (`shipment_number`),
  ADD KEY `fk_sales_shipments_sales_order_id` (`sales_order_id`),
  ADD KEY `fk_sales_shipments_customer_id` (`customer_id`),
  ADD KEY `fk_sales_shipments_warehouse_id` (`warehouse_id`),
  ADD KEY `fk_sales_shipments_created_by_user_id` (`created_by`),
  ADD KEY `fk_sales_shipments_updated_by_user_id` (`updated_by`);

--
-- 資料表索引 `sales_shipment_details`
--
ALTER TABLE `sales_shipment_details`
  ADD PRIMARY KEY (`item_id`),
  ADD KEY `fk_sales_shipment_details_shipment_id` (`shipment_id`),
  ADD KEY `fk_sales_shipment_details_sales_order_item_id` (`sales_order_item_id`),
  ADD KEY `fk_sales_shipment_details_product_id` (`product_id`),
  ADD KEY `fk_sales_shipment_details_unit_id` (`unit_id`),
  ADD KEY `fk_sales_shipment_details_created_by_user_id` (`created_by`),
  ADD KEY `fk_sales_shipment_details_updated_by_user_id` (`updated_by`);

--
-- 資料表索引 `suppliers`
--
ALTER TABLE `suppliers`
  ADD PRIMARY KEY (`supplier_id`),
  ADD UNIQUE KEY `uk_suppliers_supplier_code` (`supplier_code`),
  ADD KEY `fk_suppliers_created_by_user_id` (`created_by`),
  ADD KEY `fk_suppliers_updated_by_user_id` (`updated_by`);

--
-- 資料表索引 `units`
--
ALTER TABLE `units`
  ADD PRIMARY KEY (`unit_id`),
  ADD UNIQUE KEY `uk_units_name` (`name`),
  ADD KEY `fk_units_created_by_user_id` (`created_by`),
  ADD KEY `fk_units_updated_by_user_id` (`updated_by`);

--
-- 資料表索引 `unit_conversions`
--
ALTER TABLE `unit_conversions`
  ADD PRIMARY KEY (`conversion_id`),
  ADD UNIQUE KEY `uk_unit_conversions_from_unit_id_to_unit_id` (`from_unit_id`,`to_unit_id`),
  ADD KEY `fk_unit_conversions_to_unit_id` (`to_unit_id`),
  ADD KEY `fk_unit_conversions_created_by_user_id` (`created_by`),
  ADD KEY `fk_unit_conversions_updated_by_user_id` (`updated_by`);

--
-- 資料表索引 `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `uk_users_username` (`username`),
  ADD UNIQUE KEY `uk_users_email` (`email`);

--
-- 資料表索引 `warehouses`
--
ALTER TABLE `warehouses`
  ADD PRIMARY KEY (`warehouse_id`),
  ADD UNIQUE KEY `uk_warehouses_name` (`name`),
  ADD KEY `fk_warehouses_created_by_user_id` (`created_by`),
  ADD KEY `fk_warehouses_updated_by_user_id` (`updated_by`);

--
-- 在傾印的資料表使用自動遞增(AUTO_INCREMENT)
--

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `contact_persons`
--
ALTER TABLE `contact_persons`
  MODIFY `contact_person_id` bigint NOT NULL AUTO_INCREMENT COMMENT '聯絡人唯一識別碼';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `customers`
--
ALTER TABLE `customers`
  MODIFY `customer_id` bigint NOT NULL AUTO_INCREMENT COMMENT '客戶唯一識別碼';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `inventories`
--
ALTER TABLE `inventories`
  MODIFY `inventory_id` bigint NOT NULL AUTO_INCREMENT COMMENT '庫存記錄唯一識別碼';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `inventory_adjustments`
--
ALTER TABLE `inventory_adjustments`
  MODIFY `adjustment_id` bigint NOT NULL AUTO_INCREMENT COMMENT '庫存調整單唯一識別碼';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `inventory_adjustment_details`
--
ALTER TABLE `inventory_adjustment_details`
  MODIFY `item_id` bigint NOT NULL AUTO_INCREMENT COMMENT '明細項目唯一識別碼';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `inventory_movements`
--
ALTER TABLE `inventory_movements`
  MODIFY `movement_id` bigint NOT NULL AUTO_INCREMENT COMMENT '異動記錄唯一識別碼';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `products`
--
ALTER TABLE `products`
  MODIFY `product_id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品唯一識別碼';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `product_categories`
--
ALTER TABLE `product_categories`
  MODIFY `category_id` bigint NOT NULL AUTO_INCREMENT COMMENT '分類唯一識別碼';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `product_images`
--
ALTER TABLE `product_images`
  MODIFY `image_id` bigint NOT NULL AUTO_INCREMENT COMMENT '圖片唯一識別碼';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `product_specs`
--
ALTER TABLE `product_specs`
  MODIFY `spec_id` bigint NOT NULL AUTO_INCREMENT COMMENT '規格唯一識別碼';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `purchase_orders`
--
ALTER TABLE `purchase_orders`
  MODIFY `purchase_order_id` bigint NOT NULL AUTO_INCREMENT COMMENT '進貨單唯一識別碼';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `purchase_order_details`
--
ALTER TABLE `purchase_order_details`
  MODIFY `item_id` bigint NOT NULL AUTO_INCREMENT COMMENT '明細項目唯一識別碼';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `purchase_requests`
--
ALTER TABLE `purchase_requests`
  MODIFY `request_id` bigint NOT NULL AUTO_INCREMENT COMMENT '採購申請單唯一識別碼';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `purchase_request_details`
--
ALTER TABLE `purchase_request_details`
  MODIFY `item_id` bigint NOT NULL AUTO_INCREMENT COMMENT '明細項目唯一識別碼';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `purchase_returns`
--
ALTER TABLE `purchase_returns`
  MODIFY `purchase_return_id` bigint NOT NULL AUTO_INCREMENT COMMENT '進貨退出單唯一識別碼';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `purchase_return_details`
--
ALTER TABLE `purchase_return_details`
  MODIFY `item_id` bigint NOT NULL AUTO_INCREMENT COMMENT '明細項目唯一識別碼';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `quotations`
--
ALTER TABLE `quotations`
  MODIFY `quotation_id` bigint NOT NULL AUTO_INCREMENT COMMENT '報價單唯一識別碼';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `quotation_details`
--
ALTER TABLE `quotation_details`
  MODIFY `item_id` bigint NOT NULL AUTO_INCREMENT COMMENT '明細項目唯一識別碼';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `sales_orders`
--
ALTER TABLE `sales_orders`
  MODIFY `sales_order_id` bigint NOT NULL AUTO_INCREMENT COMMENT '銷售訂單唯一識別碼';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `sales_order_details`
--
ALTER TABLE `sales_order_details`
  MODIFY `item_id` bigint NOT NULL AUTO_INCREMENT COMMENT '明細項目唯一識別碼';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `sales_returns`
--
ALTER TABLE `sales_returns`
  MODIFY `sales_return_id` bigint NOT NULL AUTO_INCREMENT COMMENT '銷貨退回單唯一識別碼';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `sales_return_details`
--
ALTER TABLE `sales_return_details`
  MODIFY `item_id` bigint NOT NULL AUTO_INCREMENT COMMENT '明細項目唯一識別碼';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `sales_shipments`
--
ALTER TABLE `sales_shipments`
  MODIFY `shipment_id` bigint NOT NULL AUTO_INCREMENT COMMENT '出貨單唯一識別碼';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `sales_shipment_details`
--
ALTER TABLE `sales_shipment_details`
  MODIFY `item_id` bigint NOT NULL AUTO_INCREMENT COMMENT '明細項目唯一識別碼';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `suppliers`
--
ALTER TABLE `suppliers`
  MODIFY `supplier_id` bigint NOT NULL AUTO_INCREMENT COMMENT '供應商唯一識別碼';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `units`
--
ALTER TABLE `units`
  MODIFY `unit_id` bigint NOT NULL AUTO_INCREMENT COMMENT '單位唯一識別碼';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `unit_conversions`
--
ALTER TABLE `unit_conversions`
  MODIFY `conversion_id` bigint NOT NULL AUTO_INCREMENT COMMENT '換算關係唯一識別碼';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `users`
--
ALTER TABLE `users`
  MODIFY `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '使用者唯一識別碼';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `warehouses`
--
ALTER TABLE `warehouses`
  MODIFY `warehouse_id` bigint NOT NULL AUTO_INCREMENT COMMENT '倉庫唯一識別碼';

--
-- 已傾印資料表的限制式
--

--
-- 資料表的限制式 `contact_persons`
--
ALTER TABLE `contact_persons`
  ADD CONSTRAINT `fk_contact_persons_customer_id` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- 資料表的限制式 `customers`
--
ALTER TABLE `customers`
  ADD CONSTRAINT `fk_customers_contact_person_id` FOREIGN KEY (`contact_person_id`) REFERENCES `contact_persons` (`contact_person_id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- 資料表的限制式 `inventories`
--
ALTER TABLE `inventories`
  ADD CONSTRAINT `fk_inventories_created_by_user_id` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_inventories_product_id` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_inventories_updated_by_user_id` FOREIGN KEY (`updated_by`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_inventories_warehouse_id` FOREIGN KEY (`warehouse_id`) REFERENCES `warehouses` (`warehouse_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- 資料表的限制式 `inventory_adjustments`
--
ALTER TABLE `inventory_adjustments`
  ADD CONSTRAINT `fk_inventory_adjustments_created_by_user_id` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_inventory_adjustments_updated_by_user_id` FOREIGN KEY (`updated_by`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_inventory_adjustments_warehouse_id` FOREIGN KEY (`warehouse_id`) REFERENCES `warehouses` (`warehouse_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- 資料表的限制式 `inventory_adjustment_details`
--
ALTER TABLE `inventory_adjustment_details`
  ADD CONSTRAINT `fk_inventory_adjustment_details_adjustment_id` FOREIGN KEY (`adjustment_id`) REFERENCES `inventory_adjustments` (`adjustment_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_inventory_adjustment_details_created_by_user_id` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_inventory_adjustment_details_product_id` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_inventory_adjustment_details_unit_id` FOREIGN KEY (`unit_id`) REFERENCES `units` (`unit_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_inventory_adjustment_details_updated_by_user_id` FOREIGN KEY (`updated_by`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- 資料表的限制式 `inventory_movements`
--
ALTER TABLE `inventory_movements`
  ADD CONSTRAINT `fk_inventory_movements_product_id` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_inventory_movements_recorded_by_user_id` FOREIGN KEY (`recorded_by`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_inventory_movements_warehouse_id` FOREIGN KEY (`warehouse_id`) REFERENCES `warehouses` (`warehouse_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- 資料表的限制式 `products`
--
ALTER TABLE `products`
  ADD CONSTRAINT `fk_products_category_id` FOREIGN KEY (`category_id`) REFERENCES `product_categories` (`category_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_products_created_by_user_id` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_products_unit_id` FOREIGN KEY (`unit_id`) REFERENCES `units` (`unit_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_products_updated_by_user_id` FOREIGN KEY (`updated_by`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- 資料表的限制式 `product_categories`
--
ALTER TABLE `product_categories`
  ADD CONSTRAINT `fk_product_categories_created_by_user_id` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_product_categories_parent_category_id` FOREIGN KEY (`parent_category_id`) REFERENCES `product_categories` (`category_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_product_categories_updated_by_user_id` FOREIGN KEY (`updated_by`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- 資料表的限制式 `product_images`
--
ALTER TABLE `product_images`
  ADD CONSTRAINT `fk_product_images_product_id` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_product_images_uploaded_by_user_id` FOREIGN KEY (`uploaded_by`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- 資料表的限制式 `product_specs`
--
ALTER TABLE `product_specs`
  ADD CONSTRAINT `fk_product_specs_created_by_user_id` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_product_specs_product_id` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_product_specs_updated_by_user_id` FOREIGN KEY (`updated_by`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
