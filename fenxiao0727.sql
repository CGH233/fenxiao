-- MySQL dump 10.13  Distrib 5.7.25-ndb-7.6.9, for Win64 (x86_64)
--
-- Host: 101.132.192.66    Database: fenxiao
-- ------------------------------------------------------
-- Server version	5.7.22-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createDate` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `version` int(11) NOT NULL,
  `juri` int(11) NOT NULL DEFAULT '0',
  `lastLoginIp` varchar(255) DEFAULT NULL,
  `lastLoginTime` datetime DEFAULT NULL,
  `loginCount` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES (1,'2014-10-25 20:28:38',_binary '\0',200,1,'0:0:0:0:0:0:0:1','2019-07-26 11:38:21',184,'admin','21232f297a57a5a743894a0e4a801fc3',1),(2,'2019-06-27 17:34:52',_binary '\0',1,0,NULL,NULL,0,'test1','e10adc3949ba59abbe56e057f20f883e',1);
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `article`
--

DROP TABLE IF EXISTS `article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `article` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createDate` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `version` int(11) NOT NULL,
  `content` text,
  `status` int(11) NOT NULL DEFAULT '0',
  `summary` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `views` int(11) DEFAULT NULL,
  `article_cate` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKD458CCF6D5046BB1` (`article_cate`),
  KEY `FKD458CCF6823F9286` (`article_cate`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article`
--

LOCK TABLES `article` WRITE;
/*!40000 ALTER TABLE `article` DISABLE KEYS */;
/*!40000 ALTER TABLE `article` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `article_cate`
--

DROP TABLE IF EXISTS `article_cate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `article_cate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createDate` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `version` int(11) NOT NULL,
  `fatherId` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article_cate`
--

LOCK TABLES `article_cate` WRITE;
/*!40000 ALTER TABLE `article_cate` DISABLE KEYS */;
/*!40000 ALTER TABLE `article_cate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `commission`
--

DROP TABLE IF EXISTS `commission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `commission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createDate` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `version` int(11) NOT NULL,
  `lowerLevelNo` varchar(255) DEFAULT NULL,
  `money` double DEFAULT NULL,
  `no` varchar(255) DEFAULT NULL,
  `operator` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `user` int(11) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3CB17CEB3519E4C2` (`user`),
  KEY `FK3CB17CEB3C8D938D` (`user`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `commission`
--

LOCK TABLES `commission` WRITE;
/*!40000 ALTER TABLE `commission` DISABLE KEYS */;
INSERT INTO `commission` VALUES (1,'2019-06-26 15:20:39',_binary '\0',0,NULL,3.75,'1561533643811','test01','第1级用户:编号【1000001】购买商品奖励',1,1,1),(2,'2019-06-26 18:29:49',_binary '\0',0,NULL,1.5,'1561544990553','test02','第1级用户:编号【1000002】购买商品奖励',1,2,1),(3,'2019-06-26 18:29:49',_binary '\0',0,NULL,1,'1561544990941','test02','第2级用户:编号【1000002】购买商品奖励',1,1,2),(4,'2019-06-27 17:57:41',_binary '\0',0,NULL,3.75,'1561629462196','test03','第1级用户:编号【556135】购买商品奖励',1,4,1),(5,'2019-06-27 17:57:41',_binary '\0',0,NULL,2.5,'1561629462484','test03','第2级用户:编号【556135】购买商品奖励',1,2,2),(6,'2019-06-27 17:57:41',_binary '\0',0,NULL,1.25,'1561629462768','test03','第3级用户:编号【556135】购买商品奖励',1,1,3);
/*!40000 ALTER TABLE `commission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `config`
--

DROP TABLE IF EXISTS `config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createDate` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `enAddress` varchar(255) DEFAULT NULL,
  `hrEmail` varchar(255) DEFAULT NULL,
  `logo` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `qq` varchar(255) DEFAULT NULL,
  `sendEmail` varchar(255) DEFAULT NULL,
  `sendEmailPass` varchar(255) DEFAULT NULL,
  `sendEmailSmtp` varchar(255) DEFAULT NULL,
  `siteDescription` varchar(255) DEFAULT NULL,
  `siteEnDescription` varchar(255) DEFAULT NULL,
  `siteEnKeys` varchar(255) DEFAULT NULL,
  `siteEnName` varchar(255) DEFAULT NULL,
  `siteKeys` varchar(255) DEFAULT NULL,
  `siteName` varchar(255) DEFAULT NULL,
  `siteUrl` varchar(255) DEFAULT NULL,
  `weibo` varchar(255) DEFAULT NULL,
  `weixin` varchar(255) DEFAULT NULL,
  `zixunEmail` varchar(255) DEFAULT NULL,
  `firstLevel` double DEFAULT NULL,
  `secondLevel` double DEFAULT NULL,
  `thirdLevel` double DEFAULT NULL,
  `downloadUrl` varchar(255) DEFAULT NULL,
  `alipayKey` varchar(255) DEFAULT NULL,
  `alipayPartner` varchar(255) DEFAULT NULL,
  `alipaySellerEmail` varchar(255) DEFAULT NULL,
  `onlinePayIsOpen` int(11) DEFAULT NULL,
  `rechargeCardIsOpen` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config`
--

LOCK TABLES `config` WRITE;
/*!40000 ALTER TABLE `config` DISABLE KEYS */;
INSERT INTO `config` VALUES (1,'2015-06-14 20:42:42',_binary '\0',12,'0',NULL,'0','0','upload/20150626/fea511e1-26f6-4410-8f2f-4754a8e408b8.png','0','0',NULL,NULL,NULL,'0','0','0','0','0','测试商店','0',NULL,'upload/20150626/768bcbc7-ebf2-4bb8-a213-7b64bb490949.png','0',0.15,0.1,0.05,'https://www.dnspod.cn/Domain','72e2043ee4e74164b66f8fb9d1545248','285684519923417943','ishangluo1@qq.com',1,1);
/*!40000 ALTER TABLE `config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `financial`
--

DROP TABLE IF EXISTS `financial`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `financial` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createDate` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `version` int(11) NOT NULL,
  `balance` double DEFAULT NULL,
  `money` double DEFAULT NULL,
  `no` varchar(255) DEFAULT NULL,
  `operator` varchar(255) DEFAULT NULL,
  `payment` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `user` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK154FDC893519E4C2` (`user`),
  KEY `FK154FDC893C8D938D` (`user`)
) ENGINE=MyISAM AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `financial`
--

LOCK TABLES `financial` WRITE;
/*!40000 ALTER TABLE `financial` DISABLE KEYS */;
INSERT INTO `financial` VALUES (1,'2016-04-06 17:02:39',_binary '\0',0,1521,-10,'1459933359608','admin','????','????',0,1),(2,'2016-04-06 17:04:26',_binary '\0',0,1621,100,'1459933466948','admin','????','????',1,1),(3,'2019-06-25 20:26:03',_binary '\0',0,1631,10,'1561465563300','admin','佣金转入','佣金转入',1,1),(4,'2019-06-26 15:13:41',_binary '\0',0,1606,-25,'1561533221655','admin','余额付款','购买香橙',0,1),(5,'2019-06-26 15:20:40',_binary '\0',0,975,-25,'1561533640956','test01','余额付款','购买香橙',0,2),(6,'2019-06-26 18:29:50',_binary '\0',0,90,-10,'1561544990111','test02','余额付款','购买新衅菜',0,4),(7,'2019-06-27 17:57:41',_binary '\0',0,75,-25,'1561629461738','test03','余额付款','购买香橙',0,5),(8,'2019-06-28 09:47:17',_binary '\0',0,124,100,'1561686437698','test01','提现','提现',0,2),(9,'2019-07-01 17:36:52',_binary '\0',0,1581,-25,'1561973812990','admin','余额付款','购买香橙',0,1),(10,'2019-07-05 14:59:26',_binary '\0',0,1582,1,'156230995549117127','admin','支付宝付款','在线充值',1,1),(11,'2019-07-06 11:24:42',_binary '\0',0,1583,1,'156238339197910909','admin','支付宝付款','在线充值',1,1),(12,'2019-07-06 11:30:46',_binary '\0',0,1603,20,'156238381503410912','admin','支付宝付款','在线充值',1,1),(13,'2019-07-06 15:19:34',_binary '\0',0,24,100,'1562397574933','test01','提现','提现',0,2),(14,'2019-07-06 15:58:54',_binary '\0',0,1623,20,'156239990523418299','admin','支付宝付款','在线充值',1,1),(15,'2019-07-06 17:57:31',_binary '\0',0,1633,10,'156240696476011767','admin','支付宝付款','在线充值',1,1);
/*!40000 ALTER TABLE `financial` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kami`
--

DROP TABLE IF EXISTS `kami`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `kami` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createDate` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `version` int(11) NOT NULL,
  `no` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `saleTime` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `product` int(11) DEFAULT NULL,
  `ordersNo` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK321D725CEC6D32` (`product`),
  KEY `FK321D728C0E4687` (`product`)
) ENGINE=MyISAM AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kami`
--

LOCK TABLES `kami` WRITE;
/*!40000 ALTER TABLE `kami` DISABLE KEYS */;
INSERT INTO `kami` VALUES (1,'2016-04-06 17:02:27',_binary '\0',1,'123','123','2016-04-06 17:02:39',1,1,'145993315759413283'),(2,'2019-06-24 20:42:51',_binary '\0',1,'123456','123456','2019-06-26 15:13:41',1,4,'156153321520016343'),(3,'2019-06-24 20:42:51',_binary '\0',1,'000002','123','2019-06-26 15:20:39',1,4,'156153362751016203'),(4,'2019-06-26 18:26:57',_binary '\0',1,'1','1;','2019-06-27 17:57:41',1,4,'156162945925019995'),(5,'2019-06-26 18:26:57',_binary '\0',1,'2','2;','2019-07-01 17:36:52',1,4,'156197379811116582'),(6,'2019-06-26 18:26:57',_binary '\0',0,'3','3;',NULL,0,4,NULL),(7,'2019-06-26 18:26:57',_binary '\0',0,'4','4;',NULL,0,4,NULL),(8,'2019-06-26 18:26:57',_binary '\0',0,'5','5;',NULL,0,4,NULL),(9,'2019-06-26 18:27:36',_binary '\0',0,'1','1;',NULL,0,3,NULL),(10,'2019-06-26 18:27:36',_binary '\0',0,'2','2;',NULL,0,3,NULL),(11,'2019-06-26 18:27:36',_binary '\0',0,'3','3;',NULL,0,3,NULL),(12,'2019-06-26 18:27:36',_binary '\0',0,'4','4;',NULL,0,3,NULL),(13,'2019-06-26 18:27:37',_binary '\0',0,'5','5;',NULL,0,3,NULL),(14,'2019-06-26 18:27:48',_binary '\0',1,'1','1;','2019-06-26 18:29:49',1,2,'156154498771814776'),(15,'2019-06-26 18:27:48',_binary '\0',0,'2','2;',NULL,0,2,NULL),(16,'2019-06-26 18:27:48',_binary '\0',0,'3','3;',NULL,0,2,NULL),(17,'2019-06-26 18:27:48',_binary '\0',0,'4','4;',NULL,0,2,NULL),(18,'2019-06-26 18:27:48',_binary '\0',0,'5','5;',NULL,0,2,NULL),(19,'2019-06-26 18:27:55',_binary '\0',0,'1','1;',NULL,0,1,NULL),(20,'2019-06-26 18:27:55',_binary '\0',0,'2','2;',NULL,0,1,NULL),(21,'2019-06-26 18:27:55',_binary '\0',0,'3','3;',NULL,0,1,NULL),(22,'2019-06-26 18:27:55',_binary '\0',0,'4','4;',NULL,0,1,NULL),(23,'2019-06-26 18:27:56',_binary '\0',0,'5','5;',NULL,0,1,NULL),(24,'2019-07-09 16:23:54',_binary '\0',0,'123456789','',NULL,0,5,NULL);
/*!40000 ALTER TABLE `kami` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createDate` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `version` int(11) NOT NULL,
  `content` varchar(255) DEFAULT NULL,
  `reply` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
/*!40000 ALTER TABLE `message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orders` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createDate` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `version` int(11) NOT NULL,
  `money` double DEFAULT NULL,
  `no` varchar(255) DEFAULT NULL,
  `productId` varchar(255) DEFAULT NULL,
  `productName` varchar(255) DEFAULT NULL,
  `productNum` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `summary` varchar(255) DEFAULT NULL,
  `user` int(11) DEFAULT NULL,
  `productMoney` double DEFAULT NULL,
  `payDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKC3DF62E53519E4C2` (`user`),
  KEY `FKC3DF62E53C8D938D` (`user`)
) ENGINE=MyISAM AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,'2016-04-06 16:59:17',_binary '\0',1,10,'145993315759413283','1','??',1,1,'????:<br/>??:123,??:123<br/>',1,10,'2016-04-06 17:02:39'),(2,'2016-04-06 17:03:15',_binary '\0',0,10,'145993339577715307','1','??',1,0,NULL,1,10,NULL),(3,'2019-06-23 22:27:04',_binary '\0',0,25,'156130002407515406','4','香橙',1,0,NULL,4,25,NULL),(4,'2019-06-23 22:29:18',_binary '\0',0,25,'156130015879819108','4','香橙',1,0,NULL,4,25,NULL),(5,'2019-06-26 11:27:02',_binary '\0',0,25,'156151962248018628','4','香橙',1,0,NULL,2,25,NULL),(6,'2019-06-26 15:13:35',_binary '\0',1,25,'156153321520016343','4','香橙',1,1,'卡密信息:<br/>卡号:123456,密码:123456<br/>',1,25,'2019-06-26 15:13:41'),(7,'2019-06-26 15:19:53',_binary '\0',0,15,'156153359353613719','3','水果',1,0,NULL,2,15,NULL),(8,'2019-06-26 15:20:27',_binary '\0',1,25,'156153362751016203','4','香橙',1,1,'卡密信息:<br/>卡号:000002,密码:123<br/>',2,25,'2019-06-26 15:20:39'),(9,'2019-06-26 15:28:00',_binary '\0',0,25,'156153408046412358','4','香橙',1,0,NULL,4,25,NULL),(10,'2019-06-26 18:29:47',_binary '\0',1,10,'156154498771814776','2','新衅菜',1,1,'卡密信息:<br/>卡号:1,密码:1;<br/>',4,10,'2019-06-26 18:29:49'),(11,'2019-06-27 17:57:39',_binary '\0',1,25,'156162945925019995','4','香橙',1,1,'卡密信息:<br/>卡号:1,密码:1;<br/>',5,25,'2019-06-27 17:57:41'),(12,'2019-07-01 17:36:38',_binary '\0',1,25,'156197379811116582','4','香橙',1,1,'卡密信息:<br/>卡号:2,密码:2;<br/>',1,25,'2019-07-01 17:36:52'),(13,'2019-07-09 16:25:21',_binary '\0',0,12,'156266072112616355','5','苹果三个',1,0,NULL,8,12,NULL);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `phone_validate_code`
--

DROP TABLE IF EXISTS `phone_validate_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `phone_validate_code` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createDate` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `version` int(11) NOT NULL,
  `code` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phone_validate_code`
--

LOCK TABLES `phone_validate_code` WRITE;
/*!40000 ALTER TABLE `phone_validate_code` DISABLE KEYS */;
INSERT INTO `phone_validate_code` VALUES (1,'2019-06-27 20:45:26',_binary '\0',0,'174637','13212345678',0),(2,'2019-06-27 20:47:18',_binary '\0',0,'181032','13212345678',0),(3,'2019-06-27 20:48:05',_binary '\0',0,'171580','13212345678',0);
/*!40000 ALTER TABLE `phone_validate_code` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createDate` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `version` int(11) NOT NULL,
  `content` text,
  `picture` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `product_cate` int(11) DEFAULT NULL,
  `bills` double DEFAULT NULL,
  `money` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKED8DCCEF9F4FDAD1` (`product_cate`),
  KEY `FKED8DCCEF4C8B01A6` (`product_cate`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'2016-04-06 16:52:26',_binary '\0',5,'<h3 style=\"font-weight:500;color:#333333;text-align:-webkit-center;\">\r\n	??\r\n</h3>','upload/20170423/197826c9-e5c0-4f3e-b822-6753e06ee92c.jpg','新衅虾',2,10,10),(2,'2017-04-23 00:12:13',_binary '\0',0,'???','upload/20170423/3b16b9d4-89ca-440f-934b-d46683142d07.jpg','新衅菜',2,10,10),(3,'2017-04-23 00:13:20',_binary '\0',0,'??','upload/20170423/e2be30de-26c9-4a4b-988c-a9c5a6a571af.jpg','水果',2,15,15),(4,'2017-04-23 00:15:23',_binary '\0',0,'??','upload/20170423/afa8ae3c-3a95-46fe-9413-2d5c81986215.jpg','香橙',2,25,25);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_cate`
--

DROP TABLE IF EXISTS `product_cate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_cate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createDate` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `version` int(11) NOT NULL,
  `fatherId` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_cate`
--

LOCK TABLES `product_cate` WRITE;
/*!40000 ALTER TABLE `product_cate` DISABLE KEYS */;
INSERT INTO `product_cate` VALUES (2,'2016-04-06 16:51:51',_binary '\0',2,0,'????'),(3,'2019-06-24 20:45:58',_binary '\0',0,0,'水果');
/*!40000 ALTER TABLE `product_cate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recharge`
--

DROP TABLE IF EXISTS `recharge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `recharge` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createDate` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `version` int(11) NOT NULL,
  `money` double DEFAULT NULL,
  `no` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `user` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKCFF27EA73519E4C2` (`user`),
  KEY `FKCFF27EA73C8D938D` (`user`)
) ENGINE=MyISAM AUTO_INCREMENT=81 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recharge`
--

LOCK TABLES `recharge` WRITE;
/*!40000 ALTER TABLE `recharge` DISABLE KEYS */;
INSERT INTO `recharge` VALUES (1,'2019-06-26 13:52:27',_binary '\0',0,6666,'156152834741512010',0,2),(2,'2019-06-26 13:59:55',_binary '\0',0,6666,'156152879515717811',0,2),(3,'2019-06-26 14:02:42',_binary '\0',0,0.1,'156152896242415377',0,2),(4,'2019-06-26 14:03:12',_binary '\0',0,1,'156152899203818030',0,2),(5,'2019-06-26 14:03:39',_binary '\0',0,0,'156152901937818496',0,2),(6,'2019-06-26 14:03:58',_binary '\0',0,0,'156152903783616881',0,2),(7,'2019-06-26 14:05:20',_binary '\0',0,0,'156152912032319566',0,2),(8,'2019-06-26 14:12:25',_binary '\0',0,0.1,'156152954559716101',0,2),(9,'2019-06-26 14:16:14',_binary '\0',0,6666,'156152977439613741',0,2),(10,'2019-06-26 14:22:08',_binary '\0',0,6666,'156153012829118463',0,2),(11,'2019-06-26 14:23:21',_binary '\0',0,66666,'156153020089919003',0,2),(12,'2019-06-26 14:28:00',_binary '\0',0,66666,'156153048012311589',0,2),(13,'2019-06-26 14:38:18',_binary '\0',0,6,'156153108989016660',0,2),(14,'2019-06-26 14:38:22',_binary '\0',0,6,'156153109917119794',0,2),(15,'2019-06-26 14:38:42',_binary '\0',0,6,'156153112000516006',0,2),(16,'2019-06-26 14:39:59',_binary '\0',0,6,'156153119976512243',0,2),(17,'2019-06-26 14:41:59',_binary '\0',0,6,'156153131928310868',0,2),(18,'2019-06-27 13:51:10',_binary '\0',0,6666,'156161466995011896',0,1),(19,'2019-06-27 21:13:47',_binary '\0',0,0.1,'156164122755517313',0,1),(20,'2019-06-27 21:32:21',_binary '\0',0,0,'156164234107116867',0,1),(21,'2019-06-27 21:32:31',_binary '\0',0,0,'156164235097013390',0,1),(22,'2019-06-27 21:32:53',_binary '\0',0,0,'156164237351014396',0,1),(23,'2019-06-27 21:33:03',_binary '\0',0,0,'156164238364210610',0,1),(24,'2019-06-27 21:35:28',_binary '\0',0,0,'156164252865119932',0,1),(25,'2019-06-27 21:35:48',_binary '\0',0,0,'156164254868214522',0,1),(26,'2019-06-28 10:11:12',_binary '\0',0,666,'156168787273215218',0,2),(27,'2019-07-01 19:36:48',_binary '\0',0,0.01,'156198100832210820',0,1),(28,'2019-07-02 10:33:11',_binary '\0',0,6,'156203479112119819',0,2),(29,'2019-07-02 10:57:41',_binary '\0',0,6666,'156203626161610802',0,1),(30,'2019-07-02 11:15:19',_binary '\0',0,666,'156203731920413943',0,2),(31,'2019-07-02 11:17:32',_binary '\0',0,666,'156203745171817950',0,2),(32,'2019-07-02 11:17:38',_binary '\0',0,666,'156203745837515154',0,2),(33,'2019-07-02 11:24:04',_binary '\0',0,555,'156203784469613512',0,1),(34,'2019-07-02 12:14:03',_binary '\0',0,1,'156204084260611078',0,1),(35,'2019-07-02 13:33:28',_binary '\0',0,66,'156204560820618859',0,1),(36,'2019-07-02 13:36:00',_binary '\0',0,66,'156204576077214152',0,1),(37,'2019-07-02 16:37:55',_binary '\0',0,666,'156205667511910377',0,1),(38,'2019-07-02 17:02:48',_binary '\0',0,1,'156205816859819364',0,1),(39,'2019-07-02 17:07:26',_binary '\0',0,1,'156205844671713462',0,1),(40,'2019-07-02 17:18:03',_binary '\0',0,1,'156205908377111749',0,2),(41,'2019-07-02 17:25:19',_binary '\0',0,1,'156205951980212397',0,1),(42,'2019-07-02 17:37:20',_binary '\0',0,1,'156206024051618052',0,2),(43,'2019-07-03 09:53:43',_binary '\0',0,1,'156211882346911174',0,1),(44,'2019-07-03 16:52:05',_binary '\0',0,1,'156214392499218711',0,1),(45,'2019-07-03 16:52:41',_binary '\0',0,1,'156214396148219201',0,1),(46,'2019-07-05 09:36:25',_binary '\0',0,1,'156229058580413125',0,2),(47,'2019-07-05 09:47:53',_binary '\0',0,1,'156229127323513651',0,2),(48,'2019-07-05 09:56:24',_binary '\0',0,1,'156229178411714475',0,2),(49,'2019-07-05 09:56:42',_binary '\0',0,1,'156229180277717364',0,2),(50,'2019-07-05 11:27:19',_binary '\0',0,1,'156229723961519275',0,1),(51,'2019-07-05 13:26:26',_binary '\0',0,1,'156230438631312250',0,1),(52,'2019-07-05 13:33:42',_binary '\0',0,1,'156230482229011133',0,1),(53,'2019-07-05 13:35:45',_binary '\0',0,1,'156230494532013838',0,1),(54,'2019-07-05 13:38:45',_binary '\0',0,1,'156230512588912195',0,1),(55,'2019-07-05 13:41:57',_binary '\0',0,1,'156230531784916544',0,1),(56,'2019-07-05 14:02:02',_binary '\0',0,1,'156230652229913054',0,1),(57,'2019-07-05 14:12:30',_binary '\0',0,1,'156230715092212454',0,1),(58,'2019-07-05 14:13:55',_binary '\0',0,1,'156230723508314206',0,4),(59,'2019-07-05 14:38:24',_binary '\0',0,1,'156230870428014756',0,1),(60,'2019-07-05 14:48:22',_binary '\0',0,1,'156230930211510768',0,1),(61,'2019-07-05 14:55:20',_binary '\0',0,1,'156230972033711196',0,1),(62,'2019-07-05 14:57:14',_binary '\0',0,1,'156230983421514493',0,1),(63,'2019-07-05 14:59:15',_binary '\0',1,1,'156230995549117127',1,1),(64,'2019-07-05 21:40:08',_binary '\0',0,1,'156233400800911910',0,1),(65,'2019-07-05 21:44:33',_binary '\0',0,1,'156233427309618776',0,1),(66,'2019-07-05 21:46:31',_binary '\0',0,1,'156233439124819558',0,1),(67,'2019-07-06 10:21:12',_binary '\0',0,1,'156237967212016783',0,1),(68,'2019-07-06 10:23:32',_binary '\0',0,1,'156237981220612017',0,1),(69,'2019-07-06 10:50:53',_binary '\0',0,1,'156238145332018821',0,1),(70,'2019-07-06 11:23:11',_binary '\0',1,1,'156238339197910909',1,1),(71,'2019-07-06 11:23:56',_binary '\0',0,1,'156238343635611667',0,2),(72,'2019-07-06 11:30:15',_binary '\0',1,20,'156238381503410912',1,1),(73,'2019-07-06 15:38:09',_binary '\0',0,1,'156239868939716615',0,2),(74,'2019-07-06 15:58:25',_binary '\0',1,20,'156239990523418299',1,1),(75,'2019-07-06 16:14:06',_binary '\0',0,1,'156240084696417352',0,1),(76,'2019-07-06 17:56:04',_binary '\0',1,10,'156240696476011767',1,1),(77,'2019-07-06 18:02:59',_binary '\0',0,11,'156240737947616567',0,1),(78,'2019-07-06 18:03:11',_binary '\0',0,11,'156240739158219862',0,1),(79,'2019-07-07 22:58:37',_binary '\0',0,11,'156251151782212353',0,1),(80,'2019-07-07 22:58:52',_binary '\0',0,11,'156251153296818554',0,1);
/*!40000 ALTER TABLE `recharge` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recharge_card`
--

DROP TABLE IF EXISTS `recharge_card`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `recharge_card` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createDate` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `version` int(11) NOT NULL,
  `money` double NOT NULL,
  `no` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `useTime` varchar(255) DEFAULT NULL,
  `useUserNo` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recharge_card`
--

LOCK TABLES `recharge_card` WRITE;
/*!40000 ALTER TABLE `recharge_card` DISABLE KEYS */;
/*!40000 ALTER TABLE `recharge_card` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_c3p0`
--

DROP TABLE IF EXISTS `test_c3p0`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test_c3p0` (
  `a` char(1) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_c3p0`
--

LOCK TABLES `test_c3p0` WRITE;
/*!40000 ALTER TABLE `test_c3p0` DISABLE KEYS */;
/*!40000 ALTER TABLE `test_c3p0` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createDate` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `version` int(11) NOT NULL,
  `balance` double(11,2) DEFAULT '0.00',
  `commission` double(11,2) DEFAULT '0.00',
  `lastLoginIp` varchar(255) DEFAULT NULL,
  `lastLoginTime` datetime DEFAULT NULL,
  `loginCount` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `registerIp` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `superior` varchar(255) DEFAULT NULL,
  `no` varchar(255) DEFAULT NULL,
  `statusDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'2015-06-16 22:32:59',_binary '\0',279,1633.00,6.50,'127.0.0.1','2019-07-12 13:05:03',226,'admin','21232f297a57a5a743894a0e4a801fc3','18705080055','192.168.0.100',1,NULL,'100000',NULL),(2,'2019-06-17 19:50:24',_binary '\0',32,975.00,24.00,'127.0.0.1','2019-07-06 15:18:38',26,'test01','e10adc3949ba59abbe56e057f20f883e','13212345678','127.0.0.1',1,';100000;','1000001',NULL),(5,'2019-06-27 17:25:17',_binary '\0',3,75.00,0.00,'0:0:0:0:0:0:0:1','2019-06-27 17:51:49',2,'test03','e10adc3949ba59abbe56e057f20f883e','13211112222','0:0:0:0:0:0:0:1',1,';100000;1000001;1000002;','556135','2019-06-27 17:57:41'),(8,'2019-07-09 16:25:09',_binary '\0',7,0.00,0.00,'0:0:0:0:0:0:0:1','2019-07-17 18:19:33',4,'庄兄','25f9e794323b453885f5181f1b624d0b','17390941753','223.104.20.81',1,';100000;','346321',NULL),(4,'2019-06-17 20:21:22',_binary '\0',8,90.00,3.75,'117.136.52.202','2019-07-05 14:13:46',6,'test02','e10adc3949ba59abbe56e057f20f883e','13332142256','127.0.0.1',1,';100000;1000001;','1000002',NULL),(6,'2019-06-27 17:43:21',_binary '\0',2,0.00,0.00,'0:0:0:0:0:0:0:1','2019-06-27 17:50:48',2,'test11','e10adc3949ba59abbe56e057f20f883e','15322223333','0:0:0:0:0:0:0:1',0,';100000;','319017',NULL),(7,'2019-06-27 18:04:15',_binary '\0',1,0.00,0.00,'0:0:0:0:0:0:0:1','2019-06-27 18:04:15',1,'test04','e10adc3949ba59abbe56e057f20f883e','15322223333','0:0:0:0:0:0:0:1',0,';1000001;1000002;556135;','149679',NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `withdraw`
--

DROP TABLE IF EXISTS `withdraw`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `withdraw` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createDate` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `version` int(11) NOT NULL,
  `bank` varchar(255) DEFAULT NULL,
  `bankAddress` varchar(255) DEFAULT NULL,
  `bankName` varchar(255) DEFAULT NULL,
  `bankNo` varchar(255) DEFAULT NULL,
  `money` double DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `user` int(11) DEFAULT NULL,
  `no` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKC7F50B0A3519E4C2` (`user`),
  KEY `FKC7F50B0A3C8D938D` (`user`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `withdraw`
--

LOCK TABLES `withdraw` WRITE;
/*!40000 ALTER TABLE `withdraw` DISABLE KEYS */;
INSERT INTO `withdraw` VALUES (1,'2019-06-28 09:47:17',_binary '\0',1,'支付宝',NULL,'test01','13212345678',100,'13212345678',1,2,''),(2,'2019-07-06 15:19:34',_binary '\0',1,'支付宝',NULL,'qqq','13544222233',100,'13544222233',1,2,NULL);
/*!40000 ALTER TABLE `withdraw` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-07-27 11:34:54
