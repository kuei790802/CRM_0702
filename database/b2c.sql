-- phpMyAdmin SQL Dump
-- version 5.1.2
-- https://www.phpmyadmin.net/
--
-- 主機： localhost:3306
-- 產生時間： 2025-06-08 19:27:20
-- 伺服器版本： 5.7.24
-- PHP 版本： 8.3.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 資料庫: `icepop`
--

-- --------------------------------------------------------

--
-- 資料表結構 `employees`
--

CREATE TABLE `employees` (
  `employeeid` int(10) UNSIGNED NOT NULL,
  `name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `phone` varchar(100) NOT NULL,
  `title` varchar(100) NOT NULL,
  `hiredate` date NOT NULL COMMENT '雇用日期',
  `leavedate` date DEFAULT NULL COMMENT '離職日期',
  `createat` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updateat` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 傾印資料表的資料 `employees`
--

INSERT INTO `employees` (`employeeid`, `name`, `email`, `phone`, `title`, `hiredate`, `leavedate`, `createat`, `updateat`) VALUES
(1, '王大明', 'wang@icepop.com', '0912-345-678', '店長', '2023-01-15', NULL, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(2, '李小美', 'lee@icepop.com', '0923-456-789', '副店長', '2023-02-01', NULL, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(3, '張志強', 'zhang@icepop.com', '0934-567-890', '銷售員', '2023-03-10', NULL, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(4, '陳雅婷', 'chen@icepop.com', '0945-678-901', '收銀員', '2023-04-05', NULL, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(5, '劉建國', 'liu@icepop.com', '0956-789-012', '倉管員', '2023-05-20', NULL, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(6, '黃淑芬', 'huang@icepop.com', '0967-890-123', '銷售員', '2024-01-10', '2024-12-31', '2025-06-08 22:35:32', '2025-06-08 22:35:32');

-- --------------------------------------------------------

--
-- 資料表結構 `inventory`
--

CREATE TABLE `inventory` (
  `inventoryid` int(11) UNSIGNED NOT NULL,
  `productid` int(11) UNSIGNED NOT NULL,
  `location` varchar(100) NOT NULL,
  `unitsinstock` int(11) NOT NULL,
  `unitsinreserved` int(11) NOT NULL,
  `lastrestockdate` date DEFAULT NULL,
  `updateat` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 傾印資料表的資料 `inventory`
--

INSERT INTO `inventory` (`inventoryid`, `productid`, `location`, `unitsinstock`, `unitsinreserved`, `lastrestockdate`, `updateat`) VALUES
(1, 1, '冷凍庫A區', 150, 20, '2025-06-07', '2025-06-08 22:35:32'),
(2, 2, '冷凍庫A區', 120, 15, '2025-06-06', '2025-06-08 22:35:32'),
(3, 3, '冷凍庫B區', 80, 10, '2025-06-05', '2025-06-08 22:35:32'),
(4, 4, '冷凍庫B區', 200, 25, '2025-06-08', '2025-06-08 22:35:32'),
(5, 5, '冷凍庫C區', 90, 12, '2025-06-04', '2025-06-08 22:35:32'),
(6, 6, '冷凍庫C區', 110, 18, '2025-06-07', '2025-06-08 22:35:32'),
(7, 7, '冷凍庫D區', 0, 0, '2025-05-30', '2025-06-08 22:35:32');

-- --------------------------------------------------------

--
-- 資料表結構 `orderdetails`
--

CREATE TABLE `orderdetails` (
  `orderid` int(10) UNSIGNED NOT NULL,
  `productid` int(11) UNSIGNED NOT NULL,
  `quantity` int(11) NOT NULL,
  `unitprice` double NOT NULL,
  `createat` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updateat` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 傾印資料表的資料 `orderdetails`
--

INSERT INTO `orderdetails` (`orderid`, `productid`, `quantity`, `unitprice`, `createat`, `updateat`) VALUES
(1, 1, 2, 25, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(1, 2, 1, 30, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(1, 3, 1, 35, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(2, 4, 3, 32, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(2, 5, 2, 38, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(3, 1, 1, 25, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(3, 6, 2, 28, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(4, 2, 4, 30, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(4, 3, 2, 35, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(5, 5, 1, 38, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(5, 6, 3, 28, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(6, 1, 5, 25, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(7, 4, 2, 32, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(7, 5, 1, 38, '2025-06-08 22:35:32', '2025-06-08 22:35:32');

-- --------------------------------------------------------

--
-- 資料表結構 `orders`
--

CREATE TABLE `orders` (
  `orderid` int(10) UNSIGNED NOT NULL,
  `shopid` int(10) UNSIGNED NOT NULL,
  `userid` int(10) UNSIGNED NOT NULL,
  `orderdate` date NOT NULL,
  `orderstatus` varchar(100) NOT NULL COMMENT '訂單狀態',
  `paymethod` varchar(100) NOT NULL COMMENT '付款方式',
  `paystatus` varchar(100) NOT NULL COMMENT '付款狀態',
  `addressid` int(10) UNSIGNED NOT NULL COMMENT '送貨地址',
  `createat` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updateat` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 傾印資料表的資料 `orders`
--

INSERT INTO `orders` (`orderid`, `shopid`, `userid`, `orderdate`, `orderstatus`, `paymethod`, `paystatus`, `addressid`, `createat`, `updateat`) VALUES
(1, 1, 1, '2025-06-08', '已完成', '信用卡', '已付款', 1, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(2, 1, 2, '2025-06-08', '配送中', '現金', '已付款', 2, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(3, 2, 3, '2025-06-07', '已完成', 'LINE Pay', '已付款', 3, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(4, 3, 4, '2025-06-07', '處理中', '信用卡', '已付款', 4, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(5, 4, 5, '2025-06-06', '已完成', '街口支付', '已付款', 5, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(6, 1, 1, '2025-06-05', '已取消', '信用卡', '已退款', 6, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(7, 2, 2, '2025-06-04', '已完成', '現金', '已付款', 7, '2025-06-08 22:35:32', '2025-06-08 22:35:32');

-- --------------------------------------------------------

--
-- 資料表結構 `productimgs`
--

CREATE TABLE `productimgs` (
  `imgid` int(11) UNSIGNED NOT NULL,
  `productid` int(11) UNSIGNED NOT NULL,
  `imgurl` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 傾印資料表的資料 `productimgs`
--

INSERT INTO `productimgs` (`imgid`, `productid`, `imgurl`) VALUES
(1, 1, '/images/products/redbean_01.jpg'),
(2, 1, '/images/products/redbean_02.jpg'),
(3, 2, '/images/products/mango_01.jpg'),
(4, 2, '/images/products/mango_02.jpg'),
(5, 3, '/images/products/matcha_01.jpg'),
(6, 4, '/images/products/strawberry_01.jpg'),
(7, 5, '/images/products/chocolate_01.jpg'),
(8, 6, '/images/products/coconut_01.jpg');

-- --------------------------------------------------------

--
-- 資料表結構 `products`
--

CREATE TABLE `products` (
  `productid` int(11) UNSIGNED NOT NULL,
  `productname` varchar(100) NOT NULL,
  `description` text NOT NULL,
  `quantityperunit` varchar(100) NOT NULL,
  `unitprice` double NOT NULL,
  `isactive` tinyint(1) NOT NULL,
  `createat` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updateat` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 傾印資料表的資料 `products`
--

INSERT INTO `products` (`productid`, `productname`, `description`, `quantityperunit`, `unitprice`, `isactive`, `createat`, `updateat`) VALUES
(1, '經典紅豆冰棒', '濃郁紅豆香味，口感綿密的經典冰棒', '1支/包', 25, 1, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(2, '芒果優格冰棒', '新鮮芒果搭配優格，酸甜清爽', '1支/包', 30, 1, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(3, '抹茶牛奶冰棒', '京都抹茶粉製作，奶香濃郁', '1支/包', 35, 1, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(4, '草莓煉乳冰棒', '季節限定草莓搭配香濃煉乳', '1支/包', 32, 1, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(5, '巧克力脆片冰棒', '比利時巧克力加上酥脆餅乾片', '1支/包', 38, 1, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(6, '椰子水果冰棒', '天然椰子水搭配綜合水果粒', '1支/包', 28, 1, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(7, '檸檬蜂蜜冰棒', '檸檬清香搭配天然蜂蜜', '1支/包', 26, 0, '2025-06-08 22:35:32', '2025-06-08 22:35:32');

-- --------------------------------------------------------

--
-- 資料表結構 `shops`
--

CREATE TABLE `shops` (
  `shopid` int(10) UNSIGNED NOT NULL,
  `shopname` varchar(100) NOT NULL,
  `employeeid` int(10) UNSIGNED NOT NULL,
  `createat` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updateat` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 傾印資料表的資料 `shops`
--

INSERT INTO `shops` (`shopid`, `shopname`, `employeeid`, `createat`, `updateat`) VALUES
(1, '冰棒天堂-台北信義店', 1, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(2, '冰棒天堂-新北板橋店', 2, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(3, '冰棒天堂-桃園中壢店', 3, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(4, '冰棒天堂-台中逢甲店', 4, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(5, '冰棒天堂-高雄夢時代店', 5, '2025-06-08 22:35:32', '2025-06-08 22:35:32');

-- --------------------------------------------------------

--
-- 資料表結構 `useraddress`
--

CREATE TABLE `useraddress` (
  `addressid` int(10) UNSIGNED NOT NULL,
  `userid` int(10) UNSIGNED NOT NULL,
  `name` varchar(100) NOT NULL COMMENT '收件人姓名',
  `phone` varchar(100) NOT NULL COMMENT '收件人電話',
  `street` varchar(100) NOT NULL COMMENT '街道地址',
  `city` varchar(100) NOT NULL COMMENT '城市',
  `district` varchar(100) NOT NULL COMMENT '行政區',
  `zipcode` varchar(50) NOT NULL COMMENT '郵遞區號',
  `isdefault` tinyint(1) NOT NULL COMMENT '是否為默認地址',
  `createat` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updateat` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 傾印資料表的資料 `useraddress`
--

INSERT INTO `useraddress` (`addressid`, `userid`, `name`, `phone`, `street`, `city`, `district`, `zipcode`, `isdefault`, `createat`, `updateat`) VALUES
(1, 1, '張小華', '0912-111-222', '忠孝東路四段200號12樓', '台北市', '大安區', '106', 1, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(2, 2, '李大偉', '0923-333-444', '中正路123號5樓', '新北市', '板橋區', '220', 1, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(3, 3, '王美玲', '0934-555-666', '中山路456號', '桃園市', '中壢區', '320', 1, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(4, 4, '陳志豪', '0945-777-888', '逢甲路789號3樓', '台中市', '西屯區', '407', 1, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(5, 5, '林雅琪', '0956-999-000', '夢時代購物中心1樓', '高雄市', '前鎮區', '806', 1, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(6, 1, '張小華', '0912-111-222', '信義路五段150號', '台北市', '信義區', '110', 0, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(7, 2, '李大偉', '0923-333-444', '公司地址-民權東路300號', '台北市', '中山區', '104', 0, '2025-06-08 22:35:32', '2025-06-08 22:35:32');

-- --------------------------------------------------------

--
-- 資料表結構 `users`
--

CREATE TABLE `users` (
  `userid` int(10) UNSIGNED NOT NULL,
  `name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `phone` varchar(100) NOT NULL,
  `password` varchar(255) DEFAULT NULL COMMENT '沒註冊就沒有',
  `registrationdate` date DEFAULT NULL COMMENT '沒註冊就沒有',
  `lastlogindate` date DEFAULT NULL,
  `isactive` tinyint(1) NOT NULL,
  `createat` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updateat` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 傾印資料表的資料 `users`
--

INSERT INTO `users` (`userid`, `name`, `email`, `phone`, `password`, `registrationdate`, `lastlogindate`, `isactive`, `createat`, `updateat`) VALUES
(1, '張小華', 'zhang.xiaohua@gmail.com', '0912-111-222', '$2y$10$example1hash', '2024-01-15', '2025-06-08', 1, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(2, '李大偉', 'li.dawei@yahoo.com', '0923-333-444', '$2y$10$example2hash', '2024-02-20', '2025-06-07', 1, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(3, '王美玲', 'wang.meiling@hotmail.com', '0934-555-666', '$2y$10$example3hash', '2024-03-10', '2025-06-06', 1, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(4, '陳志豪', 'chen.zhihao@gmail.com', '0945-777-888', '$2y$10$example4hash', '2024-04-25', '2025-06-05', 1, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(5, '林雅琪', 'lin.yaqi@gmail.com', '0956-999-000', '$2y$10$example5hash', '2024-05-30', '2025-06-08', 1, '2025-06-08 22:35:32', '2025-06-08 22:35:32'),
(6, '黃建明', 'huang.jianming@gmail.com', '0967-123-456', NULL, NULL, '2025-06-01', 1, '2025-06-08 22:35:32', '2025-06-08 22:35:32');

--
-- 已傾印資料表的索引
--

--
-- 資料表索引 `employees`
--
ALTER TABLE `employees`
  ADD PRIMARY KEY (`employeeid`),
  ADD KEY `name` (`name`);

--
-- 資料表索引 `inventory`
--
ALTER TABLE `inventory`
  ADD PRIMARY KEY (`inventoryid`),
  ADD KEY `fk_inventory_productid` (`productid`);

--
-- 資料表索引 `orderdetails`
--
ALTER TABLE `orderdetails`
  ADD PRIMARY KEY (`orderid`,`productid`),
  ADD KEY `fk_orderdetail_productid` (`productid`);

--
-- 資料表索引 `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`orderid`),
  ADD KEY `fk_orders_shopid` (`shopid`),
  ADD KEY `fk_orders_userid` (`userid`),
  ADD KEY `fk_orders_addressid` (`addressid`);

--
-- 資料表索引 `productimgs`
--
ALTER TABLE `productimgs`
  ADD PRIMARY KEY (`imgid`),
  ADD KEY `fk_productsimg_productid` (`productid`);

--
-- 資料表索引 `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`productid`),
  ADD KEY `productname` (`productname`);

--
-- 資料表索引 `shops`
--
ALTER TABLE `shops`
  ADD PRIMARY KEY (`shopid`),
  ADD KEY `shopname` (`shopname`),
  ADD KEY `fk_shops_employeeid` (`employeeid`);

--
-- 資料表索引 `useraddress`
--
ALTER TABLE `useraddress`
  ADD PRIMARY KEY (`addressid`),
  ADD KEY `fk_useraddress_userid` (`userid`);

--
-- 資料表索引 `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`userid`),
  ADD KEY `name` (`name`),
  ADD KEY `email` (`email`,`phone`);

--
-- 在傾印的資料表使用自動遞增(AUTO_INCREMENT)
--

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `employees`
--
ALTER TABLE `employees`
  MODIFY `employeeid` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `inventory`
--
ALTER TABLE `inventory`
  MODIFY `inventoryid` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `orders`
--
ALTER TABLE `orders`
  MODIFY `orderid` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `productimgs`
--
ALTER TABLE `productimgs`
  MODIFY `imgid` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `products`
--
ALTER TABLE `products`
  MODIFY `productid` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `shops`
--
ALTER TABLE `shops`
  MODIFY `shopid` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `useraddress`
--
ALTER TABLE `useraddress`
  MODIFY `addressid` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `users`
--
ALTER TABLE `users`
  MODIFY `userid` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- 已傾印資料表的限制式
--

--
-- 資料表的限制式 `inventory`
--
ALTER TABLE `inventory`
  ADD CONSTRAINT `fk_inventory_productid` FOREIGN KEY (`productid`) REFERENCES `products` (`productid`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- 資料表的限制式 `orderdetails`
--
ALTER TABLE `orderdetails`
  ADD CONSTRAINT `fk_orderdetail_orderid` FOREIGN KEY (`orderid`) REFERENCES `orders` (`orderid`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_orderdetail_productid` FOREIGN KEY (`productid`) REFERENCES `products` (`productid`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- 資料表的限制式 `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `fk_orders_addressid` FOREIGN KEY (`addressid`) REFERENCES `useraddress` (`addressid`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_orders_shopid` FOREIGN KEY (`shopid`) REFERENCES `shops` (`shopid`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_orders_userid` FOREIGN KEY (`userid`) REFERENCES `users` (`userid`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- 資料表的限制式 `productimgs`
--
ALTER TABLE `productimgs`
  ADD CONSTRAINT `fk_productsimg_productid` FOREIGN KEY (`productid`) REFERENCES `products` (`productid`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- 資料表的限制式 `shops`
--
ALTER TABLE `shops`
  ADD CONSTRAINT `fk_shops_employeeid` FOREIGN KEY (`employeeid`) REFERENCES `employees` (`employeeid`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- 資料表的限制式 `useraddress`
--
ALTER TABLE `useraddress`
  ADD CONSTRAINT `fk_useraddress_userid` FOREIGN KEY (`userid`) REFERENCES `users` (`userid`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
