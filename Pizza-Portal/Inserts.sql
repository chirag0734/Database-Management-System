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
-- Dumping data for table `Order`
--

LOCK TABLES `Order` WRITE;
/*!40000 ALTER TABLE `Order` DISABLE KEYS */;
/*!40000 ALTER TABLE `Order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `Pizza`
--

LOCK TABLES `Pizza` WRITE;
/*!40000 ALTER TABLE `Pizza` DISABLE KEYS */;
INSERT INTO `Pizza` VALUES (2,'Memphis BBQ Chicken',1,2,'Y'),(5,'Buffalo Chicken',7,2,'Y'),(8,'Cali Chicken Bacon Ranch',6,2,'Y'),(11,'Spinach & Feta',13,2,'Y'),(14,'Pacific Veggie',8,2,'Y'),(17,'Ultimate Pepperoni',2,2,'Y'),(20,'Wisconsin 6 Cheese',16,2,'Y'),(23,'Philly Cheese Steak',15,2,'Y'),(26,'Deluxe',9,2,'Y'),(29,'Honolulu Hawaiian',2,2,'Y');
/*!40000 ALTER TABLE `Pizza` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `PizzaOrder`
--

LOCK TABLES `PizzaOrder` WRITE;
/*!40000 ALTER TABLE `PizzaOrder` DISABLE KEYS */;
/*!40000 ALTER TABLE `PizzaOrder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `PizzaTopping`
--

LOCK TABLES `PizzaTopping` WRITE;
/*!40000 ALTER TABLE `PizzaTopping` DISABLE KEYS */;
INSERT INTO `PizzaTopping` VALUES (2,1),(5,1),(11,1),(14,1),(17,1),(29,1),(14,2),(29,2),(11,5),(8,6),(2,8),(17,10),(2,11),(5,11),(8,11),(26,12),(26,15),(20,16),(17,18),(23,18),(20,21),(5,22),(29,22),(26,23),(23,24),(20,26),(23,26),(8,27),(11,28),(14,28);
/*!40000 ALTER TABLE `PizzaTopping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `Sauce`
--

LOCK TABLES `Sauce` WRITE;
/*!40000 ALTER TABLE `Sauce` DISABLE KEYS */;
INSERT INTO `Sauce` VALUES (1,'BBQ Sauce'),(2,'Kicker Hot Sauce'),(3,'Blue Cheese'),(4,'Icing Dipping Sauce'),(5,'Garlic Dipping Sauce'),(6,'Tomato Sauce'),(7,'Buffalo Sauce'),(8,'Tomato Garlic sauce'),(9,'Green Pepper Garlic Sauce'),(10,'Puttanesca Sauce'),(11,'Jamaican Jerk Sauce'),(12,'Mango Chutney Sauce'),(13,'Spinach Artichoke Sauce'),(14,'Jalapeño BBQ Sauce'),(15,'Chipotle BBQ Sauce'),(16,'Extra Hot Tomato Sauce'),(17,'Marinara sauce'),(18,'Mornay Sauce');
/*!40000 ALTER TABLE `Sauce` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `Size`
--

LOCK TABLES `Size` WRITE;
/*!40000 ALTER TABLE `Size` DISABLE KEYS */;
INSERT INTO `Size` VALUES (1,'S',3),(2,'M',5),(3,'L',7);
/*!40000 ALTER TABLE `Size` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `Topping`
--

LOCK TABLES `Topping` WRITE;
/*!40000 ALTER TABLE `Topping` DISABLE KEYS */;
INSERT INTO `Topping` VALUES (1,'Cheese',0),(2,'Pineapple',1),(3,'Green peppers',1),(4,'Black olives',1),(5,'Spinach',1),(6,'Bacon',1),(7,'Sausage',1),(8,'Onions',1),(9,'Mushrooms',1),(10,'Pepperoni',1),(11,'Chicken',1),(12,'Caramelized onions',1),(13,'Tomatoes',1),(14,'Green Pepper',1),(15,'Cheddar Cheese',1),(16,'Corn',1),(17,'Bacon',1),(18,'Red Onion',1),(19,'Grilled Chicken',1),(20,'Pancetta',1),(21,'Black Olives',1),(22,'Jalapenos',1),(23,'Portobello mushrooms',1),(24,'Steak',1),(25,'Sweet Potato',1),(26,'Extra Cheese',1),(27,'Ranch',1),(28,'Feta',1);
/*!40000 ALTER TABLE `Topping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-12-03 20:37:03
