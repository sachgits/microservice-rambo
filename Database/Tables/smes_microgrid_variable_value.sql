CREATE DATABASE  IF NOT EXISTS `smes_microgrid` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `smes_microgrid`;
-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: localhost    Database: smes_microgrid
-- ------------------------------------------------------
-- Server version	5.7.13-log

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
-- Table structure for table `variable_value`
--

DROP TABLE IF EXISTS `variable_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `variable_value` (
  `timestamp` datetime NOT NULL,
  `variable_id` int(11) NOT NULL,
  `value` decimal(59,6) DEFAULT NULL,
  KEY `fk_variable_value_variable_1` (`variable_id`) USING BTREE,
  CONSTRAINT `fk_variable_value_variable_1` FOREIGN KEY (`variable_id`) REFERENCES `variable` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `variable_value`
--

LOCK TABLES `variable_value` WRITE;
/*!40000 ALTER TABLE `variable_value` DISABLE KEYS */;
INSERT INTO `variable_value` VALUES ('2016-10-05 10:02:42',20,0.000000),('2016-10-05 10:02:42',21,0.000000),('2016-10-05 10:03:12',20,0.000000),('2016-10-05 10:03:12',21,0.000000),('2016-10-05 10:03:43',20,0.000000),('2016-10-05 10:03:43',21,0.000000),('2016-10-05 10:04:13',20,0.000000),('2016-10-05 10:04:13',21,0.000000),('2016-10-05 10:04:43',20,0.000000),('2016-10-05 10:04:43',21,0.000000),('2016-10-05 10:05:13',20,0.000000),('2016-10-05 10:05:13',21,0.000000),('2016-10-05 10:05:42',20,0.000000),('2016-10-05 10:05:42',21,0.000000),('2016-10-05 10:06:12',20,0.000000),('2016-10-05 10:06:12',21,0.000000),('2016-10-05 10:06:42',21,0.000000),('2016-10-05 10:06:42',20,0.000000),('2016-10-05 10:07:12',21,0.000000),('2016-10-05 10:07:12',20,0.000000),('2016-10-05 10:07:42',21,0.000000),('2016-10-05 10:07:42',20,0.000000),('2016-10-05 10:08:12',21,0.000000),('2016-10-05 10:08:12',20,0.000000),('2016-10-05 10:08:42',20,0.000000),('2016-10-05 10:08:42',21,0.000000),('2016-10-05 10:09:12',20,0.000000),('2016-10-05 10:09:12',21,0.000000),('2016-10-05 10:09:42',21,0.000000),('2016-10-05 10:09:42',20,0.000000),('2016-10-05 10:10:12',20,0.000000),('2016-10-05 10:10:12',21,0.000000),('2016-10-05 10:10:42',21,0.000000),('2016-10-05 10:10:42',20,0.000000),('2016-10-05 10:11:12',21,10.093000),('2016-10-05 10:11:12',20,0.000000),('2016-10-05 10:11:42',20,10.062000),('2016-10-05 10:11:42',21,0.000000),('2016-10-05 10:12:12',21,10.062000),('2016-10-05 10:12:12',20,0.000000),('2016-10-05 10:12:42',21,10.093000),('2016-10-05 10:12:42',20,0.004680),('2016-10-05 10:13:12',21,10.062000),('2016-10-05 10:13:12',20,0.000000),('2016-10-05 10:13:42',21,10.093000),('2016-10-05 10:13:42',20,0.000000),('2016-10-05 10:14:12',21,10.031000),('2016-10-05 10:14:12',20,0.000000);
/*!40000 ALTER TABLE `variable_value` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-10-06  9:05:57
