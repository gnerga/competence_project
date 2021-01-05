-- MySQL dump 10.13  Distrib 5.7.32, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: GoodHotDog
-- ------------------------------------------------------
-- Server version	8.0.22

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
-- Table structure for table `hot_spots`
--

DROP TABLE IF EXISTS `hot_spots`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hot_spots` (
  `name` varchar(30) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `longitude` float DEFAULT NULL,
  `latitude` float DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `phone_number` varchar(12) DEFAULT NULL,
  `profile` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

--
-- Struktura tabeli dla tabeli `fake_users`
--

DROP TABLE IF EXISTS `fake_users`;
CREATE TABLE `fake_users` (
  `id` int(11) NOT NULL,
  `phone_number` varchar(12) DEFAULT NULL,
  `profile` int(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


--
-- Struktura tabeli dla tabeli `lookup_table`
--

DROP TABLE IF EXISTS `lookup_table`;
CREATE TABLE `lookup_table` (
  `phone_number` varchar(12) NOT NULL,
  `fake_phone_number` varchar(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Dump completed on 2021-01-04 23:12:52
