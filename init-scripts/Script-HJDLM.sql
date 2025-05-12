CREATE DATABASE  IF NOT EXISTS `hoteljb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `hoteljb`;
-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: hoteljb
-- ------------------------------------------------------
-- Server version	8.4.3

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `category_id` int NOT NULL AUTO_INCREMENT,
  `name_es` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `img` varchar(256) DEFAULT NULL,
  `name_en` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`category_id`),
  UNIQUE KEY `name` (`name_es`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'Lujo','luxury.jpg','luxury'),(2,'EconÃ³mica','economy.jpg','economic'),(3,'Familiar','family.jpg','family');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category_room`
--

DROP TABLE IF EXISTS `category_room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category_room` (
  `category_room_id` int NOT NULL AUTO_INCREMENT,
  `name_category_en` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `description_es` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `name_category_es` varchar(50) DEFAULT NULL,
  `description_en` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`category_room_id`),
  UNIQUE KEY `name_category` (`name_category_en`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category_room`
--

LOCK TABLES `category_room` WRITE;
/*!40000 ALTER TABLE `category_room` DISABLE KEYS */;
INSERT INTO `category_room` VALUES (1,'Individual','sdasdasd','single','ingles'),(2,'Doble','fasdadsa','double','ingles'),(3,'Suite','sdasdasdas','Suite','ingles');
/*!40000 ALTER TABLE `category_room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contact`
--

DROP TABLE IF EXISTS `contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contact` (
  `contact_id` int NOT NULL AUTO_INCREMENT,
  `telephone` varchar(15) DEFAULT NULL,
  `telephone2` varchar(15) DEFAULT NULL,
  `address` varchar(250) DEFAULT NULL,
  `address_url` varchar(250) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `instagram_username` varchar(50) DEFAULT NULL,
  `facebook_username` varchar(50) DEFAULT NULL,
  `facebook_url` varchar(256) DEFAULT NULL,
  `tiktok` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`contact_id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `instagram_username` (`instagram_username`),
  UNIQUE KEY `facebook_username` (`facebook_username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contact`
--

LOCK TABLES `contact` WRITE;
/*!40000 ALTER TABLE `contact` DISABLE KEYS */;
INSERT INTO `contact` VALUES (1,'a','+503 7777-8888','jfdjfklñajdfklajdfkljdñl',NULL,'contacto@hoteljb.com','hoteljb_oficial_inta','Hotel Jardines de las Marías','kld ljrihe  hkjhfkajhds','tiktok jardines');
/*!40000 ALTER TABLE `contact` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detail_room`
--

DROP TABLE IF EXISTS `detail_room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detail_room` (
  `detail_room_id` int NOT NULL AUTO_INCREMENT,
  `detail_name_es` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `icon` varchar(50) DEFAULT NULL,
  `detail_name_en` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`detail_room_id`),
  UNIQUE KEY `detail_name` (`detail_name_es`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detail_room`
--

LOCK TABLES `detail_room` WRITE;
/*!40000 ALTER TABLE `detail_room` DISABLE KEYS */;
INSERT INTO `detail_room` VALUES (1,'WiFi',NULL,'ingles'),(2,'TV',NULL,'ingles'),(3,'Minibar',NULL,'ingles');
/*!40000 ALTER TABLE `detail_room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detail_roomxroom`
--

DROP TABLE IF EXISTS `detail_roomxroom`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detail_roomxroom` (
  `detail_roomxroom_id` int NOT NULL AUTO_INCREMENT,
  `detail_room_id` int DEFAULT NULL,
  `room_id` int DEFAULT NULL,
  PRIMARY KEY (`detail_roomxroom_id`),
  KEY `room_id` (`room_id`),
  KEY `detail_room_id` (`detail_room_id`),
  CONSTRAINT `detail_roomxroom_ibfk_1` FOREIGN KEY (`room_id`) REFERENCES `room` (`room_id`) ON DELETE CASCADE,
  CONSTRAINT `detail_roomxroom_ibfk_2` FOREIGN KEY (`detail_room_id`) REFERENCES `detail_room` (`detail_room_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detail_roomxroom`
--

LOCK TABLES `detail_roomxroom` WRITE;
/*!40000 ALTER TABLE `detail_roomxroom` DISABLE KEYS */;
INSERT INTO `detail_roomxroom` VALUES (1,1,1),(2,2,2),(3,3,3);
/*!40000 ALTER TABLE `detail_roomxroom` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gallery`
--

DROP TABLE IF EXISTS `gallery`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gallery` (
  `gallery_id` int NOT NULL AUTO_INCREMENT,
  `name_img` varchar(25) DEFAULT NULL,
  `path` varchar(256) DEFAULT NULL,
  `category_id` int DEFAULT NULL,
  PRIMARY KEY (`gallery_id`),
  UNIQUE KEY `name_img` (`name_img`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `gallery_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gallery`
--

LOCK TABLES `gallery` WRITE;
/*!40000 ALTER TABLE `gallery` DISABLE KEYS */;
INSERT INTO `gallery` VALUES (1,'Lobby','/images/lobby.jpg',1),(2,'Piscina','/images/pool.jpg',2),(3,'Spa','/images/spa.jpg',3),(4,'hotel','/uploads/image13.jpeg',1);
/*!40000 ALTER TABLE `gallery` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `img`
--

DROP TABLE IF EXISTS `img`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `img` (
  `img_id` int NOT NULL AUTO_INCREMENT,
  `name_img` varchar(50) DEFAULT NULL,
  `path` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`img_id`),
  UNIQUE KEY `name_img` (`name_img`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `img`
--

LOCK TABLES `img` WRITE;
/*!40000 ALTER TABLE `img` DISABLE KEYS */;
INSERT INTO `img` VALUES (1,'room_101.jpg','/images/room_101.jpg'),(2,'room_202.jpg','/images/room_202.jpg'),(3,'room_303.jpg','/images/room_303.jpg');
/*!40000 ALTER TABLE `img` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu`
--

DROP TABLE IF EXISTS `menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menu` (
  `menu_id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(50) DEFAULT NULL,
  `path_pdf` varchar(256) DEFAULT NULL,
  `schedule` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`menu_id`),
  UNIQUE KEY `title` (`title`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu`
--

LOCK TABLES `menu` WRITE;
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` VALUES (1,'MenÃº Desayuno','/pdfs/breakfast_menu.pdf','07:00 - 10:00 AM'),(2,'MenÃº Almuerzo','/pdfs/lunch_menu.pdf','12:00 - 15:00 PM'),(3,'MenÃº Cena','/pdfs/dinner_menu.pdf','18:00 - 22:00 PM');
/*!40000 ALTER TABLE `menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `policies`
--

DROP TABLE IF EXISTS `policies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `policies` (
  `policies_id` int NOT NULL AUTO_INCREMENT,
  `warranty_es` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `cancellation_es` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `warranty_en` varchar(100) DEFAULT NULL,
  `cancellation_en` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`policies_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `policies`
--

LOCK TABLES `policies` WRITE;
/*!40000 ALTER TABLE `policies` DISABLE KEYS */;
INSERT INTO `policies` VALUES (1,'Reembolso del 100% hasta 48 horas antes del check-in','Cargo del 50% si cancela en menos de 48 horas','100% refund up to 48 hours before check-in','50% charge if you cancel within 48 hours'),(2,'Reembolso del 75% hasta 72 horas antes del check-in','Cargo del 25% si cancela en menos de 72 horas','75% refund up to 72 hours before check-in','25% charge if you cancel within 72 hours'),(3,'Sin reembolso en cancelaciones de última hora','Sin cargo si cancela con 5 días de anticipación','No refund for last minute cancellations','No charge if you cancel 5 days in advance');
/*!40000 ALTER TABLE `policies` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post`
--

DROP TABLE IF EXISTS `post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post` (
  `post_id` int NOT NULL AUTO_INCREMENT,
  `title_es` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `description_es` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `path_image` varchar(256) DEFAULT NULL,
  `category_id` int DEFAULT NULL,
  `title_en` varchar(100) DEFAULT NULL,
  `description_en` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`post_id`),
  UNIQUE KEY `title` (`title_es`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `post_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post`
--

LOCK TABLES `post` WRITE;
/*!40000 ALTER TABLE `post` DISABLE KEYS */;
INSERT INTO `post` VALUES (1,'Nueva Suite','Disfruta de nuestra nueva suite de lujo','/images/suite.jpg',1,NULL,NULL),(2,'Oferta Especial','Descuento del 20% en estadÃ­as de mÃ¡s de 3 noches','/images/offer.jpg',2,NULL,NULL),(3,'Evento Gourmet','Cena exclusiva con chef internacional','/images/dinner.jpg',2,NULL,NULL);
/*!40000 ALTER TABLE `post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservation`
--

DROP TABLE IF EXISTS `reservation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservation` (
  `reservation_id` int NOT NULL AUTO_INCREMENT,
  `init_date` date DEFAULT NULL,
  `finish_date` date DEFAULT NULL,
  `cant_people` int DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `payment` float DEFAULT NULL,
  `category_room_id` int DEFAULT NULL,
  `room_id` int DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `creation_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`reservation_id`),
  KEY `category_room_id` (`category_room_id`),
  CONSTRAINT `reservation_ibfk_1` FOREIGN KEY (`category_room_id`) REFERENCES `category_room` (`category_room_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservation`
--

LOCK TABLES `reservation` WRITE;
/*!40000 ALTER TABLE `reservation` DISABLE KEYS */;
INSERT INTO `reservation` VALUES (1,'2025-03-01','2025-03-02',2,'cliente@ejemplo.com','+503 2222-3333',500,1,1,NULL,'2025-02-20 22:19:36');
/*!40000 ALTER TABLE `reservation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room` (
  `room_id` int NOT NULL AUTO_INCREMENT,
  `name_es` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `max_capacity` int DEFAULT NULL,
  `description_es` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `price` float DEFAULT NULL,
  `img_id` int DEFAULT NULL,
  `category_room_id` int DEFAULT NULL,
  `size_bed` varchar(100) DEFAULT NULL,
  `name_en` varchar(100) DEFAULT NULL,
  `description_en` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`room_id`),
  UNIQUE KEY `name` (`name_es`),
  KEY `category_room_id` (`category_room_id`),
  CONSTRAINT `room_ibfk_1` FOREIGN KEY (`category_room_id`) REFERENCES `category_room` (`category_room_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
INSERT INTO `room` VALUES (1,'HabitaciÃ³n 101',1,'HabitaciÃ³n individual con vista al mar',120.5,1,1,NULL,NULL,NULL),(2,'HabitaciÃ³n 202',2,'HabitaciÃ³n doble con balcÃ³n',150.75,2,2,NULL,NULL,NULL),(3,'HabitaciÃ³n 303',4,'Suite con jacuzzi',300,3,3,NULL,NULL,NULL),(4,'Suite Ejecutiva',4,'Suite de lujo con vista panorámica y jacuzzi.',250,NULL,1,'3 mt',NULL,NULL),(6,'Suite Ejecutiva2',4,'Suite de lujo con vista panorámica y jacuzzi.',250,NULL,1,'3 mt',NULL,'ingles'),(8,'Suite Ejecutiva3',4,'Suite de lujo con vista panorámica y jacuzzi.',250,NULL,1,'3 mt',NULL,'ingles'),(9,'Suite Ejecutiva4',4,'Suite de lujo con vista panorámica y jacuzzi.',250,NULL,1,'3 mt',NULL,'ingles'),(10,'Suite Ejecutiva5',4,'Suite de lujo con vista panorámica y jacuzzi.',250,NULL,1,'3 mt',NULL,'suite');
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roomximg`
--

DROP TABLE IF EXISTS `roomximg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roomximg` (
  `room_img_id` int NOT NULL AUTO_INCREMENT,
  `room_id` int DEFAULT NULL,
  `img_id` int DEFAULT NULL,
  PRIMARY KEY (`room_img_id`),
  KEY `room_id` (`room_id`),
  KEY `img_id` (`img_id`),
  CONSTRAINT `roomximg_ibfk_1` FOREIGN KEY (`room_id`) REFERENCES `room` (`room_id`) ON DELETE CASCADE,
  CONSTRAINT `roomximg_ibfk_2` FOREIGN KEY (`img_id`) REFERENCES `img` (`img_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roomximg`
--

LOCK TABLES `roomximg` WRITE;
/*!40000 ALTER TABLE `roomximg` DISABLE KEYS */;
INSERT INTO `roomximg` VALUES (1,1,1),(2,2,2),(3,3,3),(4,1,2);
/*!40000 ALTER TABLE `roomximg` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sections`
--

DROP TABLE IF EXISTS `sections`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sections` (
  `section_id` int NOT NULL AUTO_INCREMENT,
  `title_es` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `description_es` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `category_id` int DEFAULT NULL,
  `title_en` varchar(100) DEFAULT NULL,
  `description_en` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`section_id`),
  UNIQUE KEY `title` (`title_es`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `sections_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sections`
--

LOCK TABLES `sections` WRITE;
/*!40000 ALTER TABLE `sections` DISABLE KEYS */;
INSERT INTO `sections` VALUES (1,'Servicios','Información sobre los servicios disponibles',1,'Services','Information on available services'),(2,'Instalaciones','Detalle de las instalaciones del hotel',2,'Instalations','Hotel facilities details'),(3,'Atracciones','Lugares cercanos y actividades recomendadas',2,'Atracctions','Nearby places and recommended activities');
/*!40000 ALTER TABLE `sections` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `token`
--

DROP TABLE IF EXISTS `token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `token` (
  `token_id` int NOT NULL AUTO_INCREMENT,
  `f_creation` datetime DEFAULT NULL,
  `content` varchar(250) DEFAULT NULL,
  `active` tinyint(1) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`token_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `token_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user_` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `token`
--

LOCK TABLES `token` WRITE;
/*!40000 ALTER TABLE `token` DISABLE KEYS */;
INSERT INTO `token` VALUES (1,NULL,'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTczOTg5NTA3NywiZXhwIjoxNzM5OTgxNDc3fQ.I9UhByy5XVASS_MOa4e1gzrxHunMOIBNb1ugKbHuqrBiBTDARoi1JQGVFocXa1fHoCpqsJ-rop8honozS8oF7g',0,1),(2,NULL,'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTczOTk4NjIzMCwiZXhwIjoxNzQwMDcyNjMwfQ.Tms41g8x1dGI8ZgGK-zZ3FhzoqU2jGdH1VX45t9IICmxg335AfdSsqECQABYdEq0qSOPbUi0-S99oN8IGZ77tA',0,1),(3,NULL,'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc0MDA5MDA0OSwiZXhwIjoxNzQwMTc2NDQ5fQ.mrfXPri5guYAhqLxq1YVQ2LWWFGbCtz2sDWGdUabaWGi0zeCLnMxWhei2ap-vsdacN2Eby64cMbli5uT7JXP0g',0,1),(4,NULL,'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc0MDE1OTMzMiwiZXhwIjoxNzQwMjQ1NzMyfQ.9VAJ1_oCBk6yMQxtjQLahC0ujTCpkYCNzMPseDpRsnZLChhtlddfUdTPtF-Og7cr5_WutKrOuZmA08599SFGng',0,1),(5,NULL,'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc0MDU4ODY0NiwiZXhwIjoxNzQwNjc1MDQ2fQ.b6AmXS_iN_ITwgh81yu3QHAToJ3HNrGmslqHqB-F281a3CgL_KGB4wykLS9xv6meduEfpkVtHKiRSzrnZJ53gg',1,1);
/*!40000 ALTER TABLE `token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `translations`
--

DROP TABLE IF EXISTS `translations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `translations` (
  `translation_id` int NOT NULL AUTO_INCREMENT,
  `entity_name` varchar(50) DEFAULT NULL,
  `entity_id` int DEFAULT NULL,
  `language_code` varchar(10) DEFAULT NULL,
  `field_name` varchar(50) DEFAULT NULL,
  `translated_text` text,
  PRIMARY KEY (`translation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `translations`
--

LOCK TABLES `translations` WRITE;
/*!40000 ALTER TABLE `translations` DISABLE KEYS */;
/*!40000 ALTER TABLE `translations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_`
--

DROP TABLE IF EXISTS `user_`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_`
--

LOCK TABLES `user_` WRITE;
/*!40000 ALTER TABLE `user_` DISABLE KEYS */;
INSERT INTO `user_` VALUES (1,'admin','$2a$12$jwx9edibNiYFdCUpYJxvWejBwb/6GsGnw.rk62YVpJAF5CfVR69HO'),(2,'guest1','guestpass'),(3,'guest2','guestpass2');
/*!40000 ALTER TABLE `user_` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'hoteljb'
--

--
-- Dumping routines for database 'hoteljb'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-04 13:45:47
