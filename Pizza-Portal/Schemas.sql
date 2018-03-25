CREATE DATABASE  IF NOT EXISTS `PizzaBytes` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `PizzaBytes`;

-- MySQL dump 10.13  Distrib 5.7.17, for macos10.12 (x86_64)
--
-- Host: 127.0.0.1    Database: PizzaBytes
-- ------------------------------------------------------
-- Server version	5.7.20

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
-- Table structure for table `Order`
--

DROP TABLE IF EXISTS `Order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Order` (
  `OrderId` int(11) NOT NULL,
  `OrderTime` datetime NOT NULL,
  `DeliveredTime` datetime DEFAULT NULL,
  `DeliveryAddress` varchar(45) NOT NULL,
  `UId` int(11) DEFAULT NULL,
  `Discount` int(2) DEFAULT NULL,
  PRIMARY KEY (`OrderId`),
  KEY `User_Order_idx` (`UId`),
  CONSTRAINT `User_Order` FOREIGN KEY (`UId`) REFERENCES `User` (`UserId`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Pizza`
--

DROP TABLE IF EXISTS `Pizza`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Pizza` (
  `PizzaId` int(11) NOT NULL,
  `PizzaName` varchar(45) DEFAULT NULL,
  `SauceId` int(11) DEFAULT NULL,
  `SizeId` int(11) DEFAULT NULL,
  `IsDefault` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`PizzaId`),
  KEY `Pizza_Sauce_idx` (`SauceId`),
  KEY `Pizza_Size_idx` (`SizeId`),
  CONSTRAINT `Pizza_Sauce` FOREIGN KEY (`SauceId`) REFERENCES `Sauce` (`SauceId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `Pizza_Size` FOREIGN KEY (`SizeId`) REFERENCES `Size` (`SizeId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PizzaOrder`
--

DROP TABLE IF EXISTS `PizzaOrder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PizzaOrder` (
  `PId` int(11) NOT NULL,
  `OId` int(11) NOT NULL,
  `Quantity` int(11) NOT NULL,
  `Cost` int(11) DEFAULT NULL,
  PRIMARY KEY (`PId`,`OId`),
  KEY `O_Id_idx` (`OId`),
  CONSTRAINT `O_Id` FOREIGN KEY (`OId`) REFERENCES `Order` (`OrderId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `P_Id` FOREIGN KEY (`PId`) REFERENCES `Pizza` (`PizzaId`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PizzaTopping`
--

DROP TABLE IF EXISTS `PizzaTopping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PizzaTopping` (
  `PId` int(11) NOT NULL,
  `TId` int(11) NOT NULL,
  PRIMARY KEY (`PId`,`TId`),
  KEY `Topping_Id_idx` (`TId`),
  CONSTRAINT `Pizza_ID` FOREIGN KEY (`PId`) REFERENCES `Pizza` (`PizzaId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `Topping_Id` FOREIGN KEY (`TId`) REFERENCES `Topping` (`ToppingId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Sauce`
--

DROP TABLE IF EXISTS `Sauce`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Sauce` (
  `SauceId` int(11) NOT NULL,
  `SauceName` varchar(45) NOT NULL,
  PRIMARY KEY (`SauceId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Size`
--

DROP TABLE IF EXISTS `Size`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Size` (
  `SizeId` int(11) NOT NULL,
  `SizeName` varchar(45) NOT NULL,
  `PriceFactor` int(2) NOT NULL,
  PRIMARY KEY (`SizeId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Topping`
--

DROP TABLE IF EXISTS `Topping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Topping` (
  `ToppingId` int(11) NOT NULL,
  `ToppingName` varchar(45) NOT NULL,
  `ToppingPrice` int(2) NOT NULL,
  PRIMARY KEY (`ToppingId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `User` (
  `UserId` int(11) NOT NULL,
  `UserName` varchar(45) NOT NULL,
  `Password` varchar(45) NOT NULL,
  `PhoneNumber` varchar(10) DEFAULT NULL,
  `EmailId` varchar(45) NOT NULL,
  `Zipcode` int(5) DEFAULT NULL,
  `Street` varchar(45) DEFAULT NULL,
  `State` varchar(45) DEFAULT NULL,
  `City` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-12-03 20:35:36
