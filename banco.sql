-- MySQL dump 10.13  Distrib 8.0.36, for Linux (x86_64)
--
-- Host: localhost    Database: ementor
-- ------------------------------------------------------
-- Server version	8.0.46-0ubuntu0.24.04.2

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
-- Table structure for table `Aluno`
--

DROP TABLE IF EXISTS `Aluno`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Aluno` (
  `Matricula` varchar(20) NOT NULL,
  `CPF_Pessoa` varchar(11) NOT NULL,
  `Periodo` int DEFAULT NULL,
  `CodigoTurma` int DEFAULT NULL,
  PRIMARY KEY (`Matricula`),
  KEY `CPF_Pessoa` (`CPF_Pessoa`),
  KEY `CodigoTurma` (`CodigoTurma`),
  CONSTRAINT `Aluno_ibfk_1` FOREIGN KEY (`CPF_Pessoa`) REFERENCES `Pessoa` (`CPF`),
  CONSTRAINT `Aluno_ibfk_2` FOREIGN KEY (`CodigoTurma`) REFERENCES `Turma` (`CodigoTurma`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Aluno`
--

LOCK TABLES `Aluno` WRITE;
/*!40000 ALTER TABLE `Aluno` DISABLE KEYS */;
/*!40000 ALTER TABLE `Aluno` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Egresso`
--

DROP TABLE IF EXISTS `Egresso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Egresso` (
  `Matricula_Aluno` varchar(20) NOT NULL,
  `ProfissaoAtual` varchar(100) DEFAULT NULL,
  `FaixaSalarial` decimal(10,2) DEFAULT NULL,
  `CursoAnterior` varchar(100) DEFAULT NULL,
  `CursoAtual` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`Matricula_Aluno`),
  CONSTRAINT `Egresso_ibfk_1` FOREIGN KEY (`Matricula_Aluno`) REFERENCES `Aluno` (`Matricula`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Egresso`
--

LOCK TABLES `Egresso` WRITE;
/*!40000 ALTER TABLE `Egresso` DISABLE KEYS */;
/*!40000 ALTER TABLE `Egresso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Notas_Aluno`
--

DROP TABLE IF EXISTS `Notas_Aluno`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Notas_Aluno` (
  `ID_Nota` int NOT NULL AUTO_INCREMENT,
  `Matricula_Aluno` varchar(20) NOT NULL,
  `Posicao_Vetor` int DEFAULT NULL,
  `Valor_Nota` decimal(5,2) DEFAULT NULL,
  PRIMARY KEY (`ID_Nota`),
  KEY `Matricula_Aluno` (`Matricula_Aluno`),
  CONSTRAINT `Notas_Aluno_ibfk_1` FOREIGN KEY (`Matricula_Aluno`) REFERENCES `Aluno` (`Matricula`),
  CONSTRAINT `Notas_Aluno_chk_1` CHECK ((`Posicao_Vetor` between 0 and 9))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Notas_Aluno`
--

LOCK TABLES `Notas_Aluno` WRITE;
/*!40000 ALTER TABLE `Notas_Aluno` DISABLE KEYS */;
/*!40000 ALTER TABLE `Notas_Aluno` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Pessoa`
--

DROP TABLE IF EXISTS `Pessoa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Pessoa` (
  `CPF` varchar(11) NOT NULL,
  `Nome` varchar(100) NOT NULL,
  `DataNascimento` date DEFAULT NULL,
  `Telefone` varchar(15) DEFAULT NULL,
  `Rua` varchar(100) DEFAULT NULL,
  `Bairro` varchar(50) DEFAULT NULL,
  `Cidade` varchar(50) DEFAULT NULL,
  `Estado` varchar(2) DEFAULT NULL,
  PRIMARY KEY (`CPF`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Pessoa`
--

LOCK TABLES `Pessoa` WRITE;
/*!40000 ALTER TABLE `Pessoa` DISABLE KEYS */;
/*!40000 ALTER TABLE `Pessoa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Professor`
--

DROP TABLE IF EXISTS `Professor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Professor` (
  `CPF_Pessoa` varchar(11) NOT NULL,
  `DataAdmissao` date DEFAULT NULL,
  `CargoChefia` tinyint(1) DEFAULT NULL,
  `CargoCoordenacao` tinyint(1) DEFAULT NULL,
  `SalarioBruto` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`CPF_Pessoa`),
  CONSTRAINT `Professor_ibfk_1` FOREIGN KEY (`CPF_Pessoa`) REFERENCES `Pessoa` (`CPF`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Professor`
--

LOCK TABLES `Professor` WRITE;
/*!40000 ALTER TABLE `Professor` DISABLE KEYS */;
/*!40000 ALTER TABLE `Professor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Turma`
--

DROP TABLE IF EXISTS `Turma`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Turma` (
  `CodigoTurma` int NOT NULL,
  `NomeTurma` varchar(50) NOT NULL,
  PRIMARY KEY (`CodigoTurma`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Turma`
--

LOCK TABLES `Turma` WRITE;
/*!40000 ALTER TABLE `Turma` DISABLE KEYS */;
/*!40000 ALTER TABLE `Turma` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Usuario`
--

DROP TABLE IF EXISTS `Usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Usuario` (
  `NomeUsuario` varchar(50) NOT NULL,
  `Senha` varchar(50) NOT NULL,
  `NivelAcesso` int NOT NULL,
  PRIMARY KEY (`NomeUsuario`),
  CONSTRAINT `Usuario_chk_1` CHECK ((`NivelAcesso` between 1 and 3))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Usuario`
--

LOCK TABLES `Usuario` WRITE;
/*!40000 ALTER TABLE `Usuario` DISABLE KEYS */;
INSERT INTO `Usuario` VALUES ('admin','1234',1);
/*!40000 ALTER TABLE `Usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-13 21:58:16