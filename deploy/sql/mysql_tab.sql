-- 订单表

CREATE TABLE `order_tab` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `order_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `order_status` tinyint unsigned NOT NULL DEFAULT '0',
  `create_time` bigint unsigned NOT NULL DEFAULT '0',
  `update_time` bigint unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_no_uk` (`order_no`),
  KEY `order_tab_create_time_idx` (`create_time`) USING BTREE,
  KEY `order_tab_update_time_idx` (`update_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;