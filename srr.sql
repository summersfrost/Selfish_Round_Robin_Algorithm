-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 16, 2024 at 12:39 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.0.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `srr`
--

-- --------------------------------------------------------

--
-- Table structure for table `gantt`
--

CREATE TABLE `gantt` (
  `name` int(11) NOT NULL,
  `consumed` int(11) NOT NULL,
  `processTime` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `gantt`
--

INSERT INTO `gantt` (`name`, `consumed`, `processTime`) VALUES
(1, 2, 2),
(2, 2, 5),
(5, 2, 8),
(4, 2, 11),
(3, 2, 14),
(1, 2, 17),
(2, 2, 20),
(5, 2, 23),
(4, 2, 26),
(3, 2, 29),
(1, 2, 32),
(2, 1, 34),
(5, 2, 37),
(4, 2, 40),
(3, 2, 43),
(1, 2, 46),
(4, 2, 49),
(3, 2, 52),
(1, 2, 55),
(3, 2, 58),
(3, 2, 61),
(3, 2, 64);

-- --------------------------------------------------------

--
-- Table structure for table `ready`
--

CREATE TABLE `ready` (
  `ProcessName` varchar(20) NOT NULL,
  `Burst` int(11) NOT NULL,
  `Time` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `ready`
--

INSERT INTO `ready` (`ProcessName`, `Burst`, `Time`) VALUES
('1', 8, 0),
('1', 8, 6),
('1', 8, 9),
('1', 8, 12),
('1', 8, 15),
('1', 6, 18),
('2', 3, 4),
('5', 4, 15),
('4', 6, 15),
('3', 12, 15),
('1', 6, 21),
('2', 1, 21),
('5', 4, 21),
('4', 6, 21),
('3', 12, 21),
('1', 6, 24),
('2', 1, 24),
('5', 2, 24),
('4', 6, 24),
('3', 12, 24),
('1', 6, 27),
('2', 1, 27),
('5', 2, 27),
('4', 4, 27),
('3', 12, 27),
('1', 6, 30),
('2', 1, 30),
('5', 2, 30),
('4', 4, 30),
('3', 10, 30),
('1', 4, 33),
('2', 1, 33),
('5', 2, 33),
('4', 4, 33),
('3', 10, 33),
('1', 4, 35),
('5', 2, 35),
('4', 4, 35),
('3', 10, 35),
('1', 4, 38),
('4', 4, 38),
('3', 10, 38),
('1', 4, 41),
('4', 2, 41),
('3', 10, 41),
('1', 4, 44),
('4', 2, 44),
('3', 8, 44),
('1', 2, 47),
('4', 2, 47),
('3', 8, 47),
('1', 2, 50),
('3', 8, 50),
('1', 2, 53),
('3', 6, 53),
('3', 6, 56),
('3', 4, 59),
('3', 2, 62);

-- --------------------------------------------------------

--
-- Table structure for table `rr`
--

CREATE TABLE `rr` (
  `process_name` varchar(10) NOT NULL,
  `arrival` int(11) NOT NULL,
  `burst` int(11) NOT NULL,
  `turnaround` int(11) DEFAULT NULL,
  `waiting` int(11) DEFAULT NULL,
  `ready` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `temprr`
--

CREATE TABLE `temprr` (
  `process_name` varchar(10) NOT NULL,
  `arrival` int(11) NOT NULL,
  `burst` int(11) NOT NULL,
  `turnaround` int(11) DEFAULT NULL,
  `waiting` int(11) DEFAULT NULL,
  `ready` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `temprr`
--

INSERT INTO `temprr` (`process_name`, `arrival`, `burst`, `turnaround`, `waiting`, `ready`) VALUES
('1', 0, 10, 55, 55, NULL),
('2', 4, 5, 34, 30, NULL),
('5', 15, 6, 37, 22, NULL),
('4', 15, 8, 49, 34, NULL),
('3', 15, 14, 64, 49, NULL);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
