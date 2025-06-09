-- MySQL dump 10.13  Distrib 8.0.36, for Linux (x86_64)
--
-- Host: localhost    Database: pokemondb
-- ------------------------------------------------------
-- Server version	8.0.36-2ubuntu3

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
-- Table structure for table `ataques`
--

DROP TABLE IF EXISTS `ataques`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ataques` (
  `idAtaque` int NOT NULL AUTO_INCREMENT,
  `nombreAtaque` varchar(30) NOT NULL,
  `tipoDeAtaque` varchar(30) NOT NULL,
  `categoriaAtaque` varchar(20) NOT NULL,
  `efecto` varchar(100) NOT NULL,
  `potencia` int NOT NULL,
  `PP` int NOT NULL,
  PRIMARY KEY (`idAtaque`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ataques`
--

LOCK TABLES `ataques` WRITE;
/*!40000 ALTER TABLE `ataques` DISABLE KEYS */;
INSERT INTO `ataques` VALUES (1,'Rayo','Electrico','Especial','Ataca y puede paralizar al objetivo',90,15),(2,'Hidrobomba','Agua','Especial','Ataque de gran potencia',110,5),(3,'Lanzallamas','Fuego','Especial','Ataca y puede quemar al objetivo',95,15),(4,'Golpe Cuerpo','Normal','Fisico','Ataca con el cuerpo y puede paralizar al objetivo',70,30),(5,'Esfera Aural','Lucha','Especial','Ataque que nunca falla',85,20),(6,'Golpe Roca','Lucha','Fisico','Ataca y puede bajarle la defensa al objetivo',40,30),(7,'Triturar','Siniestro','Fisico','Ataca y puede hacer retroceder al objetivo',80,20);
/*!40000 ALTER TABLE `ataques` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entrenador`
--

DROP TABLE IF EXISTS `entrenador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `entrenador` (
  `idEntrenador` int NOT NULL AUTO_INCREMENT,
  `nomEntrenador` varchar(30) NOT NULL,
  PRIMARY KEY (`idEntrenador`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entrenador`
--

LOCK TABLES `entrenador` WRITE;
/*!40000 ALTER TABLE `entrenador` DISABLE KEYS */;
INSERT INTO `entrenador` VALUES (1,'Ash'),(2,'Rojo'),(3,'Brock'),(4,'Maya'),(5,'Cinthia'),(6,'N');
/*!40000 ALTER TABLE `entrenador` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `habilidad`
--

DROP TABLE IF EXISTS `habilidad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `habilidad` (
  `idHabilidad` int NOT NULL AUTO_INCREMENT,
  `nombreHabilidad` varchar(30) DEFAULT NULL,
  `descripcionHabilidad` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`idHabilidad`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `habilidad`
--

LOCK TABLES `habilidad` WRITE;
/*!40000 ALTER TABLE `habilidad` DISABLE KEYS */;
INSERT INTO `habilidad` VALUES (1,'Levitacion','Hace que el Pokémon sea inmune a los ataques de tierra.'),(2,'Impulso','Aumenta la velocidad cada turno'),(3,'Intimidacion','Baja en un nivel el ataque del rival'),(4,'Punto Toxico','Tiene posibilidad de envenenar al contacto'),(5,'Presion','El pokemon enemigo consume 2 pp'),(6,'Absorber agua','El portador se cura si recibe un ataque de tipo agua');
/*!40000 ALTER TABLE `habilidad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pokemon`
--

DROP TABLE IF EXISTS `pokemon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pokemon` (
  `idPokemon` int NOT NULL AUTO_INCREMENT,
  `numero_pokedex` int NOT NULL,
  `nombre_pokemon` varchar(30) NOT NULL,
  `alias` varchar(30) DEFAULT 'Sin alias',
  `tipoPokemon` varchar(15) NOT NULL,
  `segundo_tipo` varchar(15) DEFAULT NULL,
  `nivel` int NOT NULL,
  `idEntrenador` int DEFAULT NULL,
  `idHabilidad` int DEFAULT NULL,
  PRIMARY KEY (`idPokemon`),
  UNIQUE KEY `numero_pokedex` (`numero_pokedex`),
  KEY `fk_entrenador` (`idEntrenador`),
  KEY `fk_habilidad` (`idHabilidad`),
  CONSTRAINT `fk_entrenador` FOREIGN KEY (`idEntrenador`) REFERENCES `entrenador` (`idEntrenador`),
  CONSTRAINT `fk_habilidad` FOREIGN KEY (`idHabilidad`) REFERENCES `habilidad` (`idHabilidad`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pokemon`
--

LOCK TABLES `pokemon` WRITE;
/*!40000 ALTER TABLE `pokemon` DISABLE KEYS */;
INSERT INTO `pokemon` VALUES (1,1,'Gengar',NULL,'Fantasma','Veneno',37,1,1),(2,2,'Lucario',NULL,'Lucha','Acero',73,2,2),(3,3,'Pikachu',NULL,'Electrico',NULL,25,1,2),(4,4,'Charizard',NULL,'Fuego','Volador',37,3,1),(5,5,'Incineroar',NULL,'Fuego','Siniestro',39,2,6),(6,6,'Darkrai',NULL,'Siniestro',NULL,70,1,2),(7,7,'Sylveon',NULL,'Hada',NULL,50,3,5),(8,8,'Spinarak',NULL,'Bicho','Veneno',5,3,3),(9,9,'Corviknight',NULL,'Volador','Acero',30,2,4),(10,10,'Torterra',NULL,'Planta','Tierra',42,1,2),(11,11,'Salamence',NULL,'Dragon','Volador',64,2,4),(12,12,'tinkaton',NULL,'acero','Hada',56,2,4);
/*!40000 ALTER TABLE `pokemon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pokemon_ataque`
--

DROP TABLE IF EXISTS `pokemon_ataque`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pokemon_ataque` (
  `id_pokemon` int NOT NULL,
  `id_ataque` int NOT NULL,
  `metodoAprendizaje` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id_pokemon`,`id_ataque`),
  KEY `fk_ataque` (`id_ataque`),
  CONSTRAINT `fk_ataque` FOREIGN KEY (`id_ataque`) REFERENCES `ataques` (`idAtaque`),
  CONSTRAINT `fk_pokemon` FOREIGN KEY (`id_pokemon`) REFERENCES `pokemon` (`idPokemon`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pokemon_ataque`
--

LOCK TABLES `pokemon_ataque` WRITE;
/*!40000 ALTER TABLE `pokemon_ataque` DISABLE KEYS */;
/*!40000 ALTER TABLE `pokemon_ataque` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-26 14:00:02
