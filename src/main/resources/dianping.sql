-- --------------------------------------------------------
-- 主机:                           106.52.236.197
-- 服务器版本:                        5.7.29-0ubuntu0.18.04.1-log - (Ubuntu)
-- 服务器OS:                        Linux
-- HeidiSQL 版本:                  10.2.0.5599
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for dianping
CREATE DATABASE IF NOT EXISTS `dianping` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;
USE `dianping`;

-- Dumping structure for table dianping.category
CREATE TABLE IF NOT EXISTS `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `icon_url` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `sort` int(11) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Dumping data for table dianping.category: ~8 rows (大约)
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` (`id`, `name`, `icon_url`, `sort`, `create_time`, `update_time`) VALUES
	(1, '美食', '/static/image/firstpage/food_u.png', 99, '2020-05-10 18:00:04', '2020-05-10 18:00:04'),
	(2, '酒店', '/static/image/firstpage/snack_u.png', 98, '2020-05-10 18:00:04', '2020-05-10 18:00:04'),
	(3, '休闲娱乐', '/static/image/firstpage/bar_o.png', 97, '2020-05-10 18:00:04', '2020-05-10 18:00:04'),
	(4, '结婚', '/static/image/firstpage/jiehun.png', 96, '2020-05-10 18:00:04', '2020-05-10 18:00:04'),
	(5, '足疗按摩', '/static/image/firstpage/zuliao.png', 96, '2020-05-10 18:00:04', '2020-05-10 18:00:04'),
	(6, 'KTV', '/static/image/firstpage/ktv_u.png', 95, '2020-05-10 18:00:04', '2020-05-10 18:00:04'),
	(7, '景点', '/static/image/firstpage/jingdian.png', 94, '2020-05-10 18:00:04', '2020-05-10 18:00:04'),
	(8, '丽人', '/static/image/firstpage/liren.png', 93, '2020-05-10 18:00:04', '2020-05-10 18:00:04');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;

-- Dumping structure for table dianping.seller
CREATE TABLE IF NOT EXISTS `seller` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(80) COLLATE utf8_unicode_ci NOT NULL COMMENT '商户名称',
  `remark_score` decimal(2,1) NOT NULL DEFAULT '0.0' COMMENT '商户评分，表示商户的服务能力，其值为所有门店评分的平均值',
  `disabled_flag` int(11) NOT NULL DEFAULT '0' COMMENT '0启用，1禁用',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='商户表';

-- Dumping data for table dianping.seller: ~26 rows (大约)
/*!40000 ALTER TABLE `seller` DISABLE KEYS */;
INSERT INTO `seller` (`id`, `name`, `remark_score`, `disabled_flag`, `create_time`, `update_time`) VALUES
	(1, '江苏和府餐饮管理有限公司', 2.5, 0, '2020-05-10 18:00:04', '2020-05-10 18:00:04'),
	(2, '北京烤鸭有限公司', 2.0, 0, '2020-05-10 18:00:04', '2020-05-10 18:00:04'),
	(3, '合肥食品制造有限公司', 2.6, 0, '2020-05-10 18:00:04', '2020-05-10 18:00:04'),
	(4, '青岛啤酒厂', 0.9, 0, '2020-05-10 18:00:04', '2020-05-10 18:00:04'),
	(5, '杭州轻食有限公司', 3.0, 0, '2020-05-10 18:00:04', '2020-05-10 18:00:04'),
	(6, '九竹食品加工公司', 5.0, 0, '2020-05-10 18:00:04', '2020-05-10 18:00:04'),
	(7, '奔潮食品加工公司', 2.7, 0, '2020-05-10 18:00:04', '2020-05-10 18:00:04'),
	(8, '百沐食品加工公司', 2.0, 0, '2020-05-10 18:00:04', '2020-05-10 18:00:04'),
	(9, '韩蒂衣食品加工公司', 1.5, 0, '2020-05-10 18:00:04', '2020-05-10 18:00:04'),
	(10, '城外食品加工公司', 1.8, 0, '2020-05-10 18:00:05', '2020-05-10 18:00:05'),
	(11, '雪兔食品加工公司', 4.6, 0, '2020-05-10 18:00:05', '2020-05-10 18:00:05'),
	(12, '琳德食品公司', 5.0, 0, '2020-05-10 18:00:05', '2020-05-10 18:00:05'),
	(13, '深圳市盛华莲蓉食品厂', 0.7, 0, '2020-05-10 18:00:05', '2020-05-10 18:00:05'),
	(14, '桂林聚德苑食品有限公司', 5.0, 0, '2020-05-10 18:00:05', '2020-05-10 18:00:05'),
	(15, '天津达瑞仿真蛋糕模型厂', 1.7, 0, '2020-05-10 18:00:05', '2020-05-10 18:00:05'),
	(16, '上海镭德杰喷码技术有限公司', 5.0, 0, '2020-05-10 18:00:05', '2020-05-10 18:00:05'),
	(17, '凯悦饭店集团', 3.0, 0, '2020-05-10 18:00:05', '2020-05-10 18:00:05'),
	(18, '卡尔森环球酒店公司', 3.1, 0, '2020-05-10 18:00:05', '2020-05-10 18:00:05'),
	(19, '喜达屋酒店集团', 0.2, 0, '2020-05-10 18:00:05', '2020-05-10 18:00:05'),
	(20, '最佳西方国际集团', 3.8, 0, '2020-05-10 18:00:06', '2020-05-10 18:00:06'),
	(21, '精品国际饭店公司', 0.2, 0, '2020-05-10 18:00:06', '2020-05-10 18:00:06'),
	(22, '希尔顿集团', 1.7, 0, '2020-05-10 18:00:06', '2020-05-10 18:00:06'),
	(23, '雅高集团', 1.8, 0, '2020-05-10 18:00:06', '2020-05-10 18:00:06'),
	(24, '万豪国际集团', 4.1, 0, '2020-05-10 18:00:06', '2020-05-10 18:00:06'),
	(25, '胜腾酒店集团', 3.0, 0, '2020-05-10 18:00:06', '2020-05-10 18:00:06'),
	(26, '洲际酒店集团', 2.8, 0, '2020-05-10 18:00:06', '2020-05-10 18:00:06');
/*!40000 ALTER TABLE `seller` ENABLE KEYS */;

-- Dumping structure for table dianping.shop
CREATE TABLE IF NOT EXISTS `shop` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT '门店名称',
  `remark_score` decimal(2,1) NOT NULL DEFAULT '0.0',
  `price_per_man` int(11) NOT NULL COMMENT '平均消费',
  `latitude` decimal(10,6) NOT NULL COMMENT '纬度',
  `longitude` decimal(10,6) NOT NULL COMMENT '经度',
  `category_id` int(11) NOT NULL COMMENT '类目',
  `tags` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '标签',
  `start_time` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '营业开始时间',
  `end_time` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '营业结束时间',
  `address` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `seller_id` int(11) NOT NULL,
  `icon_url` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Dumping data for table dianping.shop: ~16 rows (大约)
/*!40000 ALTER TABLE `shop` DISABLE KEYS */;
INSERT INTO `shop` (`id`, `name`, `remark_score`, `price_per_man`, `latitude`, `longitude`, `category_id`, `tags`, `start_time`, `end_time`, `address`, `seller_id`, `icon_url`, `create_time`, `update_time`) VALUES
	(1, '和府捞面(正大乐城店)', 4.9, 156, 31.195341, 120.915855, 1, '新开业 人气爆棚', '10:00', '22:00', '船厂路36号', 1, '/static/image/shopcover/xchg.jpg', '2020-05-10 18:05:13', '2020-05-10 18:05:13'),
	(2, '和府捞面(飞洲国际店)', 0.4, 79, 31.189323, 121.443550, 1, '强烈推荐要点小食', '10:00', '22:00', '零陵路899号', 1, '/static/image/shopcover/zoocoffee.jpg', '2020-05-10 18:05:41', '2020-05-10 18:05:41'),
	(3, '和府捞面(百脑汇店)', 4.7, 101, 31.189323, 121.443550, 1, '有大桌 有WIFI', '10:00', '22:00', '漕溪北路339号', 1, '/static/image/shopcover/six.jpg', '2020-05-10 18:05:42', '2020-05-10 18:05:42'),
	(4, '花悦庭果木烤鸭', 2.0, 152, 31.306419, 121.524878, 1, '落地大窗 有WIFI', '11:00', '21:00', '翔殷路1099号', 2, '/static/image/shopcover/yjbf.jpg', '2020-05-10 18:05:42', '2020-05-10 18:05:42'),
	(5, '德泰丰北京烤鸭', 2.3, 187, 31.305236, 121.519875, 1, '五花肉味道', '11:00', '21:00', '邯郸路国宾路路口', 2, '/static/image/shopcover/jbw.jpg', '2020-05-10 18:05:42', '2020-05-10 18:05:42'),
	(6, '烧肉居酒屋', 2.3, 78, 31.306419, 121.524878, 1, '有包厢', '11:00', '21:00', '翔殷路1099号', 4, '/static/image/shopcover/mwsk.jpg', '2020-05-10 18:05:42', '2020-05-10 18:05:42'),
	(7, '西界', 4.7, 100, 31.309411, 121.515074, 1, '帅哥多', '11:00', '21:00', '大学路246号', 4, '/static/image/shopcover/lsy.jpg', '2020-05-10 18:05:42', '2020-05-10 18:05:42'),
	(8, 'LAVA酒吧', 2.0, 152, 31.308370, 121.521360, 1, '帅哥多', '11:00', '21:00', '淞沪路98号', 4, '/static/image/shopcover/jtyjj.jpg', '2020-05-10 18:05:42', '2020-05-10 18:05:42'),
	(9, '凯悦酒店', 2.2, 176, 31.306172, 121.525843, 2, '落地大窗', '11:00', '21:00', '国定东路88号', 17, '/static/image/shopcover/dfjzw.jpg', '2020-05-10 18:05:42', '2020-05-10 18:05:42'),
	(10, '凯悦嘉轩酒店', 0.5, 182, 31.196742, 121.322846, 2, '自助餐', '11:00', '21:00', '申虹路9号', 17, '/static/image/shopcover/secretroom09.jpg', '2020-05-10 18:05:42', '2020-05-10 18:05:42'),
	(11, '新虹桥凯悦酒店', 1.0, 74, 31.156899, 121.238362, 2, '自助餐', '11:00', '21:00', '沪青平公路2799弄', 17, '/static/image/shopcover/secretroom08.jpg', '2020-05-10 18:05:43', '2020-05-10 18:05:43'),
	(12, '凯悦咖啡(新建西路店)', 2.0, 71, 30.679819, 121.651921, 1, '有包厢', '11:00', '21:00', '南桥环城西路665号', 17, '/static/image/shopcover/secretroom07.jpg', '2020-05-10 18:05:43', '2020-05-10 18:05:43'),
	(13, '上海虹桥元希尔顿酒店', 4.5, 96, 31.193517, 121.401270, 2, '2019年上海必住酒店', '11:00', '21:00', '红松东路1116号', 22, '/static/image/shopcover/secretroom06.jpg', '2020-05-10 18:05:43', '2020-05-10 18:05:43'),
	(14, '国家会展中心希尔顿欢朋酒店', 1.7, 176, 30.953049, 121.053774, 2, '高档', '11:00', '21:00', '华漕镇盘阳路59弄', 22, '/static/image/shopcover/secretroom05.jpg', '2020-05-10 18:05:43', '2020-05-10 18:05:43'),
	(15, '上海绿地万豪酒店', 4.1, 187, 31.197688, 121.479098, 2, '高档', '11:00', '21:00', '江滨路99号', 23, '/static/image/shopcover/secretroom04.jpg', '2020-05-10 18:05:43', '2020-05-10 18:05:43'),
	(16, '上海宝华万豪酒店', 3.0, 163, 31.285934, 121.452481, 2, '高档', '11:00', '21:00', '广中西路333号', 23, '/static/image/shopcover/secretroom03.jpg', '2020-05-10 18:05:43', '2020-05-10 18:05:43');
/*!40000 ALTER TABLE `shop` ENABLE KEYS */;

-- Dumping structure for table dianping.user
CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `telphone` varchar(40) COLLATE utf8_unicode_ci NOT NULL DEFAULT '0' COMMENT '手机号',
  `password` varchar(200) COLLATE utf8_unicode_ci NOT NULL DEFAULT '0' COMMENT '密码',
  `nick_name` varchar(40) COLLATE utf8_unicode_ci NOT NULL DEFAULT '0' COMMENT '昵称',
  `gender` int(11) NOT NULL DEFAULT '0' COMMENT '性别',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `telphone` (`telphone`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Dumping data for table dianping.user: ~4 rows (大约)
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`id`, `telphone`, `password`, `nick_name`, `gender`, `create_time`, `update_time`) VALUES
	(1, '18688881234', '123456', '团长', 1, '2020-04-23 23:53:11', '2020-04-23 23:53:11'),
	(2, '18668152160', '4QrcOUm6Wau+VuBX8g+IPg==', '团长', 1, '2020-04-25 19:59:06', '2020-04-25 19:59:06'),
	(11, '18668152161', '4QrcOUm6Wau+VuBX8g+IPg==', 'hahah', 1, '2020-04-25 20:54:39', '2020-04-25 20:54:39'),
	(13, '18668152162', '4QrcOUm6Wau+VuBX8g+IPg==', 'sdfsdf', 1, '2020-05-07 08:30:08', '2020-05-07 08:30:08');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
