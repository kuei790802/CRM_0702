-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- 主機： localhost
-- 產生時間： 2025 年 06 月 09 日 09:41
-- 伺服器版本： 10.4.28-MariaDB
-- PHP 版本： 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 資料庫： `crm`
--

-- --------------------------------------------------------

--
-- 資料表結構 `activities`
--

CREATE TABLE `activities` (
  `activtyid` int(11) NOT NULL COMMENT '活動編號',
  `relatedtype` varchar(50) DEFAULT NULL COMMENT '關聯類型(商機/客戶)',
  `relatedid` int(11) DEFAULT NULL COMMENT '關聯ID',
  `activtytype` varchar(50) DEFAULT NULL COMMENT '活動類型',
  `activtydate` datetime DEFAULT NULL COMMENT '活動時間',
  `activtynotes` text DEFAULT NULL COMMENT '備註',
  `salespersonname` varchar(100) DEFAULT NULL COMMENT '業務人員姓名',
  `activtystatus` varchar(50) DEFAULT NULL COMMENT '狀態'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- 資料表結構 `contacts`
--

CREATE TABLE `contacts` (
  `contactid` int(11) NOT NULL COMMENT '聯絡人編號',
  `customerid` int(11) NOT NULL COMMENT '所屬客戶編號',
  `contactname` varchar(100) DEFAULT NULL COMMENT '聯絡人姓名',
  `contacttitle` varchar(100) DEFAULT NULL COMMENT '職稱',
  `contactphone` varchar(50) DEFAULT NULL COMMENT '電話',
  `contactemail` varchar(150) DEFAULT NULL COMMENT '聯絡人信箱',
  `contactnotes` text DEFAULT NULL COMMENT '備註'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- 資料表結構 `customers`
--

CREATE TABLE `customers` (
  `customerid` int(11) NOT NULL COMMENT '客戶編號',
  `customername` varchar(150) NOT NULL COMMENT '客戶(公司)名稱',
  `industry` varchar(100) DEFAULT NULL COMMENT '產業類別',
  `customertype` varchar(50) DEFAULT NULL COMMENT '客戶類型',
  `sourceid` int(11) DEFAULT NULL COMMENT '所屬來源編號',
  `customerlevel` varchar(50) DEFAULT NULL COMMENT '客戶等級',
  `customeraddress` varchar(150) DEFAULT NULL COMMENT '客戶地址',
  `customertel` varchar(50) DEFAULT NULL COMMENT '客戶電話',
  `customeremail` varchar(150) DEFAULT NULL COMMENT '客戶信箱',
  `customercreated` datetime DEFAULT current_timestamp() COMMENT '建立時間',
  `customerupdated` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '更新時間'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- 資料表結構 `events`
--

CREATE TABLE `events` (
  `eventid` int(11) NOT NULL COMMENT '行事曆事件ID',
  `eventtitle` varchar(150) DEFAULT NULL COMMENT '事件標題',
  `eventstart` datetime DEFAULT NULL COMMENT '開始時間',
  `eventend` datetime DEFAULT NULL COMMENT '結束時間',
  `salespersonname` varchar(100) DEFAULT NULL COMMENT '業務人員姓名',
  `customerid` int(11) DEFAULT NULL COMMENT '關聯客戶',
  `opportunityid` int(11) DEFAULT NULL COMMENT '關聯商機',
  `eventnotes` text DEFAULT NULL COMMENT '備註'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- 資料表結構 `notes`
--

CREATE TABLE `notes` (
  `noteid` int(11) NOT NULL COMMENT '筆記 ID',
  `relatedtype` varchar(50) DEFAULT NULL COMMENT '關聯類型',
  `relatedid` int(11) DEFAULT NULL COMMENT '關聯ID',
  `notecontent` text DEFAULT NULL COMMENT '內容',
  `createdbyname` varchar(100) DEFAULT NULL COMMENT '建立者姓名',
  `notecreated` datetime DEFAULT current_timestamp() COMMENT '建立時間'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- 資料表結構 `opportunities`
--

CREATE TABLE `opportunities` (
  `opportuntiyid` int(11) NOT NULL COMMENT '商機編號',
  `customerid` int(11) NOT NULL COMMENT '所屬客戶編號',
  `opportuntiystage` varchar(50) DEFAULT NULL COMMENT '商機階段',
  `opportuntiystatus` varchar(50) DEFAULT NULL COMMENT '狀態',
  `opportuntiyclosedate` date DEFAULT NULL COMMENT '預計成交日',
  `opportuntiyamount` decimal(14,2) DEFAULT NULL COMMENT '預估金額',
  `salespersonname` varchar(100) DEFAULT NULL COMMENT '業務人員編號',
  `opportuntiycreated` datetime DEFAULT current_timestamp() COMMENT '建立時間',
  `opportuntiyupdatedd` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '更新時間'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- 資料表結構 `products`
--

CREATE TABLE `products` (
  `productid` int(11) NOT NULL COMMENT '產品編號',
  `productname` varchar(150) DEFAULT NULL COMMENT '產品名稱',
  `productsku` varchar(100) DEFAULT NULL COMMENT '庫存編號',
  `productcategory` varchar(100) DEFAULT NULL COMMENT '產品分類',
  `productunitprice` decimal(10,2) DEFAULT NULL COMMENT '單價',
  `productstockquantity` int(11) DEFAULT NULL COMMENT '庫存數量',
  `productdescription` text DEFAULT NULL COMMENT '說明'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- 資料表結構 `quotationdetail`
--

CREATE TABLE `quotationdetail` (
  `qutationdetailid` int(11) NOT NULL COMMENT '報價明細編號',
  `quoteid` int(11) NOT NULL COMMENT '報價單編號',
  `productid` int(11) NOT NULL COMMENT '產品編號',
  `qutationdetailquantity` int(11) DEFAULT NULL COMMENT '數量',
  `qutationdetailunitprice` decimal(10,2) DEFAULT NULL COMMENT '單價',
  `qutationdetaildiscount` decimal(5,2) DEFAULT NULL COMMENT '折扣',
  `subtotal` decimal(14,2) DEFAULT NULL COMMENT '小計'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- 資料表結構 `quotations`
--

CREATE TABLE `quotations` (
  `qutationid` int(11) NOT NULL COMMENT '報價單編號',
  `opportunityid` int(11) NOT NULL COMMENT '所屬商機',
  `qutationdate` date DEFAULT NULL COMMENT '報價日期',
  `qutationvalid` date DEFAULT NULL COMMENT '有效期限',
  `qutationtotal` decimal(14,2) DEFAULT NULL COMMENT '總金額'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- 資料表結構 `records`
--

CREATE TABLE `records` (
  `recordid` int(11) NOT NULL COMMENT '成交紀錄編號',
  `customerid` int(11) NOT NULL COMMENT '所屬客戶編號',
  `opportunityid` int(11) DEFAULT NULL COMMENT '所屬商機編號',
  `salesdate` date DEFAULT NULL COMMENT '成交日期',
  `totalamount` decimal(14,2) DEFAULT NULL COMMENT '成交金額',
  `salespersonname` varchar(100) DEFAULT NULL COMMENT '業務人員姓名'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- 資料表結構 `sources`
--

CREATE TABLE `sources` (
  `sourceid` int(11) NOT NULL COMMENT '來源編號',
  `sourcename` varchar(100) NOT NULL COMMENT '來源名稱',
  `sourcedescription` text DEFAULT NULL COMMENT '來源說明',
  `isactive` tinyint(1) DEFAULT 1 COMMENT '是否啟用'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- 資料表結構 `stages`
--

CREATE TABLE `stages` (
  `stageid` int(11) NOT NULL COMMENT '階段編號',
  `opportunityid` int(11) NOT NULL COMMENT '對應商機',
  `name` varchar(100) DEFAULT NULL COMMENT '階段名稱',
  `stagestart` date DEFAULT NULL COMMENT '開始日期',
  `stageend` date DEFAULT NULL COMMENT '結束日期',
  `stagenotes` text DEFAULT NULL COMMENT '備註'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- 資料表結構 `tagmap`
--

CREATE TABLE `tagmap` (
  `customerid` int(11) NOT NULL COMMENT '客戶編號',
  `tagid` int(11) NOT NULL COMMENT '標籤編號'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- 資料表結構 `tags`
--

CREATE TABLE `tags` (
  `tagid` int(11) NOT NULL COMMENT '標籤編號',
  `tagname` varchar(100) DEFAULT NULL COMMENT '標籤名稱'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 已傾印資料表的索引
--

--
-- 資料表索引 `activities`
--
ALTER TABLE `activities`
  ADD PRIMARY KEY (`activtyid`);

--
-- 資料表索引 `contacts`
--
ALTER TABLE `contacts`
  ADD PRIMARY KEY (`contactid`),
  ADD KEY `customerid` (`customerid`);

--
-- 資料表索引 `customers`
--
ALTER TABLE `customers`
  ADD PRIMARY KEY (`customerid`),
  ADD KEY `sourceid` (`sourceid`);

--
-- 資料表索引 `events`
--
ALTER TABLE `events`
  ADD PRIMARY KEY (`eventid`),
  ADD KEY `customerid` (`customerid`),
  ADD KEY `opportunityid` (`opportunityid`);

--
-- 資料表索引 `notes`
--
ALTER TABLE `notes`
  ADD PRIMARY KEY (`noteid`);

--
-- 資料表索引 `opportunities`
--
ALTER TABLE `opportunities`
  ADD PRIMARY KEY (`opportuntiyid`),
  ADD KEY `customerid` (`customerid`);

--
-- 資料表索引 `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`productid`),
  ADD UNIQUE KEY `sku` (`productsku`);

--
-- 資料表索引 `quotationdetail`
--
ALTER TABLE `quotationdetail`
  ADD PRIMARY KEY (`qutationdetailid`),
  ADD KEY `quoteid` (`quoteid`),
  ADD KEY `productid` (`productid`);

--
-- 資料表索引 `quotations`
--
ALTER TABLE `quotations`
  ADD PRIMARY KEY (`qutationid`),
  ADD KEY `opportunityid` (`opportunityid`);

--
-- 資料表索引 `records`
--
ALTER TABLE `records`
  ADD PRIMARY KEY (`recordid`),
  ADD KEY `customerid` (`customerid`),
  ADD KEY `opportunityid` (`opportunityid`);

--
-- 資料表索引 `sources`
--
ALTER TABLE `sources`
  ADD PRIMARY KEY (`sourceid`);

--
-- 資料表索引 `stages`
--
ALTER TABLE `stages`
  ADD PRIMARY KEY (`stageid`),
  ADD KEY `opportunityid` (`opportunityid`);

--
-- 資料表索引 `tagmap`
--
ALTER TABLE `tagmap`
  ADD PRIMARY KEY (`customerid`,`tagid`),
  ADD KEY `tagid` (`tagid`);

--
-- 資料表索引 `tags`
--
ALTER TABLE `tags`
  ADD PRIMARY KEY (`tagid`);

--
-- 在傾印的資料表使用自動遞增(AUTO_INCREMENT)
--

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `activities`
--
ALTER TABLE `activities`
  MODIFY `activtyid` int(11) NOT NULL AUTO_INCREMENT COMMENT '活動編號';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `contacts`
--
ALTER TABLE `contacts`
  MODIFY `contactid` int(11) NOT NULL AUTO_INCREMENT COMMENT '聯絡人編號';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `customers`
--
ALTER TABLE `customers`
  MODIFY `customerid` int(11) NOT NULL AUTO_INCREMENT COMMENT '客戶編號';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `events`
--
ALTER TABLE `events`
  MODIFY `eventid` int(11) NOT NULL AUTO_INCREMENT COMMENT '行事曆事件ID';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `notes`
--
ALTER TABLE `notes`
  MODIFY `noteid` int(11) NOT NULL AUTO_INCREMENT COMMENT '筆記 ID';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `opportunities`
--
ALTER TABLE `opportunities`
  MODIFY `opportuntiyid` int(11) NOT NULL AUTO_INCREMENT COMMENT '商機編號';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `products`
--
ALTER TABLE `products`
  MODIFY `productid` int(11) NOT NULL AUTO_INCREMENT COMMENT '產品編號';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `quotationdetail`
--
ALTER TABLE `quotationdetail`
  MODIFY `qutationdetailid` int(11) NOT NULL AUTO_INCREMENT COMMENT '報價明細編號';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `quotations`
--
ALTER TABLE `quotations`
  MODIFY `qutationid` int(11) NOT NULL AUTO_INCREMENT COMMENT '報價單編號';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `records`
--
ALTER TABLE `records`
  MODIFY `recordid` int(11) NOT NULL AUTO_INCREMENT COMMENT '成交紀錄編號';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `sources`
--
ALTER TABLE `sources`
  MODIFY `sourceid` int(11) NOT NULL AUTO_INCREMENT COMMENT '來源編號';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `stages`
--
ALTER TABLE `stages`
  MODIFY `stageid` int(11) NOT NULL AUTO_INCREMENT COMMENT '階段編號';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `tags`
--
ALTER TABLE `tags`
  MODIFY `tagid` int(11) NOT NULL AUTO_INCREMENT COMMENT '標籤編號';

--
-- 已傾印資料表的限制式
--

--
-- 資料表的限制式 `contacts`
--
ALTER TABLE `contacts`
  ADD CONSTRAINT `contacts_ibfk_1` FOREIGN KEY (`customerid`) REFERENCES `customers` (`customerid`);

--
-- 資料表的限制式 `customers`
--
ALTER TABLE `customers`
  ADD CONSTRAINT `customers_ibfk_1` FOREIGN KEY (`sourceid`) REFERENCES `sources` (`sourceid`);

--
-- 資料表的限制式 `events`
--
ALTER TABLE `events`
  ADD CONSTRAINT `events_ibfk_1` FOREIGN KEY (`customerid`) REFERENCES `customers` (`customerid`),
  ADD CONSTRAINT `events_ibfk_2` FOREIGN KEY (`opportunityid`) REFERENCES `opportunities` (`opportuntiyid`);

--
-- 資料表的限制式 `opportunities`
--
ALTER TABLE `opportunities`
  ADD CONSTRAINT `opportunities_ibfk_1` FOREIGN KEY (`customerid`) REFERENCES `customers` (`customerid`);

--
-- 資料表的限制式 `quotationdetail`
--
ALTER TABLE `quotationdetail`
  ADD CONSTRAINT `quotationdetail_ibfk_1` FOREIGN KEY (`quoteid`) REFERENCES `quotations` (`qutationid`),
  ADD CONSTRAINT `quotationdetail_ibfk_2` FOREIGN KEY (`productid`) REFERENCES `products` (`productid`);

--
-- 資料表的限制式 `quotations`
--
ALTER TABLE `quotations`
  ADD CONSTRAINT `quotations_ibfk_1` FOREIGN KEY (`opportunityid`) REFERENCES `opportunities` (`opportuntiyid`);

--
-- 資料表的限制式 `records`
--
ALTER TABLE `records`
  ADD CONSTRAINT `records_ibfk_1` FOREIGN KEY (`customerid`) REFERENCES `customers` (`customerid`),
  ADD CONSTRAINT `records_ibfk_2` FOREIGN KEY (`opportunityid`) REFERENCES `opportunities` (`opportuntiyid`);

--
-- 資料表的限制式 `stages`
--
ALTER TABLE `stages`
  ADD CONSTRAINT `stages_ibfk_1` FOREIGN KEY (`opportunityid`) REFERENCES `opportunities` (`opportuntiyid`);

--
-- 資料表的限制式 `tagmap`
--
ALTER TABLE `tagmap`
  ADD CONSTRAINT `tagmap_ibfk_1` FOREIGN KEY (`customerid`) REFERENCES `customers` (`customerid`),
  ADD CONSTRAINT `tagmap_ibfk_2` FOREIGN KEY (`tagid`) REFERENCES `tags` (`tagid`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
