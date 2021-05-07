-- phpMyAdmin SQL Dump
-- version 4.9.0.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 09-02-2021 a las 16:25:45
-- Versión del servidor: 10.4.6-MariaDB
-- Versión de PHP: 7.3.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `inventory`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categories`
--

CREATE TABLE `categories` (
  `id` int(11) NOT NULL,
  `type` varchar(100) NOT NULL,
  `description` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `categories`
--

INSERT INTO `categories` (`id`, `type`, `description`) VALUES
(1, 'Shampoo', 'Hair Product'),
(2, 'Gel', 'Hair styling'),
(3, 'Cosmetics', 'Grooming products'),
(4, 'Color', 'Hair colour and dyes');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `employees`
--

CREATE TABLE `employees` (
  `id` int(11) NOT NULL,
  `firstname` varchar(100) NOT NULL,
  `lastname` varchar(100) NOT NULL,
  `username` varchar(40) NOT NULL,
  `password` varchar(100) NOT NULL,
  `phone` varchar(100) NOT NULL,
  `address` text NOT NULL,
  `type` enum('admin','employee') NOT NULL DEFAULT 'employee'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `employees`
--

INSERT INTO `employees` (`id`, `firstname`, `lastname`, `username`, `password`, `phone`, `address`, `type`) VALUES
(1, 'john', 'cena', 'admin', 'd033e22ae348aeb5660fc2140aec35850c4da997', '0099887766', 'New York, USA', 'admin'),
(2, 'Martha', 'Jones', 'user', '7110eda4d09e062aa5e4a390b0a572ac0d2c0220', '123456789', 'Seattle', 'employee');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `invoices`
--

CREATE TABLE `invoices` (
  `id` varchar(13) NOT NULL,
  `employeeId` int(11) NOT NULL,
  `total` double NOT NULL,
  `vat` double NOT NULL,
  `discount` double NOT NULL,
  `payable` double NOT NULL,
  `paid` double NOT NULL,
  `returned` double NOT NULL,
  `datetime` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `invoices`
--

INSERT INTO `invoices` (`id`, `employeeId`, `total`, `vat`, `discount`, `payable`, `paid`, `returned`, `datetime`) VALUES
('1491729973342', 2, 760, 19, 5, 774, 800, 26, '2017-01-09 15:26:13'),
('1491730560516', 2, 370, 9.25, 5, 374.25, 375, 0.75, '2017-01-09 15:36:00'),
('1492165305284', 2, 270, 6.75, 5, 271.75, 280, 8.25, '2017-01-14 16:21:45'),
('1492189349464', 2, 490, 12.25, 5, 497.25, 500, 2.75, '2017-02-14 23:02:29'),
('1492189946488', 2, 190, 4.75, 5, 189.75, 200, 10.25, '2017-02-14 23:12:26'),
('1492190099626', 2, 120, 3, 5, 118, 120, 2, '2017-04-14 23:14:59'),
('1492190341116', 2, 65, 1.625, 5, 61.625, 62, 0.375, '2017-04-14 23:19:01'),
('1492191099328', 2, 190, 4.75, 5, 189.75, 190, 0.25, '2017-04-14 23:31:39'),
('1492192975710', 2, 770, 19.25, 5, 784.25, 1000, 215.75, '2017-04-15 00:02:55'),
('1492193361407', 2, 865, 21.625, 5, 881.625, 900, 18.375, '2017-03-15 00:09:21'),
('1492313070538', 2, 275, 6.875, 5, 276.875, 300, 23.125, '2017-03-16 09:24:30'),
('1493699328760', 2, 70, 1.75, 5, 66.75, 70, 3.25, '2017-05-02 10:28:48'),
('1493699482352', 2, 190, 4.75, 5, 189.75, 190, 0.25, '2017-05-02 10:31:22'),
('1612370572705', 2, 460, 73.60000000000001, 0, 533.6, 550, 16.399999999999977, '2021-02-03 10:42:52'),
('1612673508903', 2, 285, 45.6, 0, 330.6, 350, 19.399999999999977, '2021-02-06 22:51:49'),
('1612673541112', 2, 120, 19.2, 0, 139.2, 150, 10.800000000000011, '2021-02-06 22:52:21'),
('1612673579117', 2, 830, 132.8, 0, 962.8, 1000, 37.200000000000045, '2021-02-06 22:52:59'),
('1612673600926', 2, 320, 51.2, 0, 371.2, 400, 28.80000000000001, '2021-02-06 22:53:20'),
('1612677451298', 2, 330, 52.800000000000004, 0, 382.8, 400, 17.2, '2021-02-06 23:57:31'),
('1612677573962', 2, 955, 152.8, 0, 1107.8, 1120, 12.2, '2021-02-06 23:59:33'),
('1612679217918', 2, 2000, 320, 0, 2320, 2500, 180, '2021-02-07 00:26:57');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `products`
--

CREATE TABLE `products` (
  `id` int(11) NOT NULL,
  `categoryId` int(11) NOT NULL,
  `supplierId` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `price` double NOT NULL,
  `quantity` double NOT NULL,
  `description` text NOT NULL,
  `codigo_barra` int(11) NOT NULL,
  `costo_adqui` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `products`
--

INSERT INTO `products` (`id`, `categoryId`, `supplierId`, `name`, `price`, `quantity`, `description`, `codigo_barra`, `costo_adqui`) VALUES
(1, 1, 1, 'Clinical Solutions', 165, 40, 'Anti-dandruff shampoo', 4525, 35.42),
(2, 1, 1, 'Classic Clean', 120, 140, 'General shampoo', 24434, 67.84),
(3, 4, 2, 'HiColor', 190, 31, 'Red Color 50g box', 87675, 24.5),
(4, 2, 2, 'Wax', 65, 35, 'Hair wax', 5434, 40.16),
(5, 3, 3, 'Power Light', 70, 88, 'Freshness Cream', 65543, 55.55),
(6, 3, 3, 'Oil Clear', 160, 285, 'Face Wash', 32435, 33.44),
(7, 3, 6, 'Brylcreem (Red)', 300, 174, 'Light glossy hold', 21245, 60.66),
(8, 3, 1, 'Brylcreem (Green)', 105, 50, 'Anti-dandruff', 75437, 77.77),
(9, 3, 5, 'TAZA BIRCON', 50, 48, 'TAZA PARA TOMAR CAFE', 4543234, 40);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `purchases`
--

CREATE TABLE `purchases` (
  `id` int(11) NOT NULL,
  `productId` int(11) NOT NULL,
  `supplierId` int(11) NOT NULL,
  `quantity` double NOT NULL,
  `price` double NOT NULL,
  `total` double NOT NULL,
  `datetime` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `purchases`
--

INSERT INTO `purchases` (`id`, `productId`, `supplierId`, `quantity`, `price`, `total`, `datetime`) VALUES
(1, 1, 1, 5, 165, 825, '2017-03-14 00:00:00'),
(2, 2, 2, 6, 120, 720, '2017-03-09 00:00:00'),
(3, 1, 1, 1, 24, 24, '2017-05-02 10:02:47'),
(4, 1, 1, 2, 20, 40, '2017-05-02 10:10:37'),
(5, 9, 5, 50, 40, 2000, '2021-02-03 11:43:09'),
(6, 7, 2, 50, 50, 2500, '2021-02-06 22:55:40'),
(7, 8, 3, 50, 120, 6000, '2021-02-06 22:56:33');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `sales`
--

CREATE TABLE `sales` (
  `id` int(11) NOT NULL,
  `invoiceId` varchar(13) NOT NULL,
  `productId` int(11) NOT NULL,
  `quantity` double NOT NULL,
  `price` int(11) NOT NULL,
  `total` double NOT NULL,
  `datetime` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `sales`
--

INSERT INTO `sales` (`id`, `invoiceId`, `productId`, `quantity`, `price`, `total`, `datetime`) VALUES
(1, '1491729973342', 7, 2, 300, 600, '2017-04-09 15:26:13'),
(2, '1491729973342', 6, 1, 160, 160, '2017-04-09 15:26:13'),
(3, '1491730560516', 2, 2, 120, 240, '2017-04-09 15:36:00'),
(4, '1491730560516', 4, 2, 65, 130, '2017-04-09 15:36:00'),
(5, '1492165305284', 5, 2, 70, 140, '2017-04-14 16:21:45'),
(6, '1492165305284', 4, 2, 65, 130, '2017-04-14 16:21:45'),
(7, '1492189349464', 1, 2, 165, 330, '2017-01-14 23:02:29'),
(8, '1492189349464', 6, 1, 160, 160, '2017-04-14 23:02:29'),
(9, '1492189946488', 3, 1, 190, 190, '2017-04-14 23:12:26'),
(10, '1492190099626', 2, 1, 120, 120, '2017-04-14 23:14:59'),
(11, '1492190341116', 4, 1, 65, 65, '2017-04-14 23:19:01'),
(12, '1492191099328', 3, 1, 190, 190, '2017-04-14 23:31:39'),
(13, '1492192975710', 6, 2, 160, 320, '2017-04-15 00:02:55'),
(14, '1492192975710', 2, 1, 120, 120, '2017-04-15 00:02:55'),
(15, '1492192975710', 1, 2, 165, 330, '2017-02-15 00:02:55'),
(16, '1492193361407', 3, 2, 190, 380, '2017-04-15 00:09:21'),
(17, '1492193361407', 1, 1, 165, 165, '2017-03-15 00:09:21'),
(18, '1492193361407', 6, 2, 160, 320, '2017-04-15 00:09:21'),
(19, '1492313070538', 5, 3, 70, 210, '2017-04-16 09:24:30'),
(20, '1492313070538', 4, 1, 65, 65, '2017-04-16 09:24:30'),
(21, '1493699482352', 3, 1, 190, 190, '2017-05-02 10:31:22'),
(22, '1612370572705', 1, 2, 165, 330, '2021-02-03 10:42:53'),
(23, '1612370572705', 4, 2, 65, 130, '2021-02-03 10:42:53'),
(24, '1612673508903', 1, 1, 165, 165, '2021-02-06 22:51:49'),
(25, '1612673508903', 2, 1, 120, 120, '2021-02-06 22:51:49'),
(26, '1612673541112', 5, 1, 70, 70, '2021-02-06 22:52:21'),
(27, '1612673541112', 9, 1, 50, 50, '2021-02-06 22:52:21'),
(28, '1612673579117', 7, 2, 300, 600, '2021-02-06 22:52:59'),
(29, '1612673579117', 1, 1, 165, 165, '2021-02-06 22:52:59'),
(30, '1612673579117', 4, 1, 65, 65, '2021-02-06 22:52:59'),
(31, '1612673600926', 6, 2, 160, 320, '2021-02-06 22:53:21'),
(32, '1612677451298', 3, 1, 190, 190, '2021-02-06 23:57:31'),
(33, '1612677451298', 5, 2, 70, 140, '2021-02-06 23:57:31'),
(34, '1612677573962', 9, 1, 50, 50, '2021-02-06 23:59:34'),
(35, '1612677573962', 6, 2, 160, 320, '2021-02-06 23:59:34'),
(36, '1612677573962', 2, 1, 120, 120, '2021-02-06 23:59:34'),
(37, '1612677573962', 1, 1, 165, 165, '2021-02-06 23:59:34'),
(38, '1612677573962', 7, 1, 300, 300, '2021-02-06 23:59:34'),
(39, '1612679217918', 1, 2, 165, 330, '2021-02-07 00:26:58'),
(40, '1612679217918', 2, 2, 120, 240, '2021-02-07 00:26:58'),
(41, '1612679217918', 6, 5, 160, 800, '2021-02-07 00:26:58'),
(42, '1612679217918', 5, 2, 70, 140, '2021-02-07 00:26:58'),
(43, '1612679217918', 7, 1, 300, 300, '2021-02-07 00:26:58'),
(44, '1612679217918', 3, 1, 190, 190, '2021-02-07 00:26:59');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `suppliers`
--

CREATE TABLE `suppliers` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `phone` varchar(100) NOT NULL,
  `address` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `suppliers`
--

INSERT INTO `suppliers` (`id`, `name`, `phone`, `address`) VALUES
(1, 'Head & Shoulder', '00000000', 'USA'),
(2, 'Loreal', '1111111', 'France'),
(3, 'Garnier', '22222222', 'France'),
(4, 'Set Wet', '444444', 'India'),
(5, 'Layer', '555555', 'India'),
(6, 'Brylcreem', '777777', 'UK'),
(7, 'Gatsby', '8888888', 'Canada');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `employees`
--
ALTER TABLE `employees`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `invoices`
--
ALTER TABLE `invoices`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id`),
  ADD KEY `categoryId` (`categoryId`),
  ADD KEY `supplierId` (`supplierId`);

--
-- Indices de la tabla `purchases`
--
ALTER TABLE `purchases`
  ADD PRIMARY KEY (`id`),
  ADD KEY `productId` (`productId`),
  ADD KEY `supplierId` (`supplierId`);

--
-- Indices de la tabla `sales`
--
ALTER TABLE `sales`
  ADD PRIMARY KEY (`id`),
  ADD KEY `productId` (`productId`),
  ADD KEY `invoiceId` (`invoiceId`);

--
-- Indices de la tabla `suppliers`
--
ALTER TABLE `suppliers`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `categories`
--
ALTER TABLE `categories`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `employees`
--
ALTER TABLE `employees`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `products`
--
ALTER TABLE `products`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT de la tabla `purchases`
--
ALTER TABLE `purchases`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de la tabla `sales`
--
ALTER TABLE `sales`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=45;

--
-- AUTO_INCREMENT de la tabla `suppliers`
--
ALTER TABLE `suppliers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `products`
--
ALTER TABLE `products`
  ADD CONSTRAINT `products_ibfk_1` FOREIGN KEY (`categoryId`) REFERENCES `categories` (`id`),
  ADD CONSTRAINT `products_ibfk_2` FOREIGN KEY (`supplierId`) REFERENCES `suppliers` (`id`);

--
-- Filtros para la tabla `purchases`
--
ALTER TABLE `purchases`
  ADD CONSTRAINT `purchases_ibfk_1` FOREIGN KEY (`productId`) REFERENCES `products` (`id`),
  ADD CONSTRAINT `purchases_ibfk_2` FOREIGN KEY (`supplierId`) REFERENCES `suppliers` (`id`);

--
-- Filtros para la tabla `sales`
--
ALTER TABLE `sales`
  ADD CONSTRAINT `sales_ibfk_1` FOREIGN KEY (`productId`) REFERENCES `products` (`id`),
  ADD CONSTRAINT `sales_ibfk_2` FOREIGN KEY (`invoiceId`) REFERENCES `invoices` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
