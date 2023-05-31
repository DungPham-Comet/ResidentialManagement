-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th1 18, 2023 lúc 02:40 PM
-- Phiên bản máy phục vụ: 10.4.25-MariaDB
-- Phiên bản PHP: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `quan_ly_to`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `cccd`
--

CREATE TABLE `cccd` (
  `ID` int(11) NOT NULL,
  `idNhankhau` int(11) NOT NULL,
  `CCCD` varchar(15) NOT NULL,
  `NgayCap` date DEFAULT '2020-01-01',
  `NoiCap` text 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `cccd`
--

INSERT INTO `cccd` (`ID`, `idNhankhau`, `CCCD`, `NgayCap`, `NoiCap`) VALUES
(6, 1, '123456789258', '2020-01-01', 'Hà Nội'),
(15, 23, '26202244', '0000-00-00', ''),
(19, 32, '25678916', '2020-01-01', 'Hà Nội'),
(26, 53, '26202001235', '2020-01-01', 'Hà Nội'),
(28, 57, '12345678915', '2020-01-01', 'Hà Nội'),
(35, 65, '19274772366', '2020-01-01', 'Hà Nội'),
(36, 66, '019207774621', '2020-01-01', 'Hà Nội'),
(37, 67, '019202007421', '2020-01-01', 'Hà Nội'),
(40, 70, '019202002411', '2020-01-01', 'Hà Nội'),
(41, 71, '019202008372', '2020-01-01', 'Hà Nội');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `cosovatchat`
--

CREATE TABLE `cosovatchat` (
  `MaDoDung` int(11) NOT NULL,
  `TenDoDung` varchar(30) NOT NULL,
  `SoLuong` int(11) NOT NULL,
  `SoLuongKhaDung` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `cosovatchat`
--

INSERT INTO `cosovatchat` (`MaDoDung`, `TenDoDung`, `SoLuong`, `SoLuongKhaDung`) VALUES
(134658, 'ADF', 86, 6345),
(213354, 'SDAF', 35, 123),
(232986, 'MÁY ẢNH', 3, 1),
(265756, 'Nến', 50, 50),
(315468, 'ASDFLJK', 123, 123),
(336721, 'Bàn chải sạch', 5, 5),
(476093, 'LOA', 4, 4),
(476530, 'MÁY CHIẾU', 2, 0),
(521616, 'BÀN', 12, 8),
(610329, 'Laptop', 100, 100),
(637670, 'LAPTOP', 4, 2),
(687545, 'GHẾ', 100, 100),
(888397, 'PHÔNG BẠT', 6, 4),
(934848, 'ĐÈN', 16, 16);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `hoatdong_cosovatchat`
--

CREATE TABLE `hoatdong_cosovatchat` (
  `MaHoatDong` int(11) NOT NULL,
  `MaDoDung` int(11) NOT NULL,
  `SoLuong` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `khaitu`
--

CREATE TABLE `khaitu` (
  `ID` int(11) NOT NULL,
  `idNguoiKhai` int(11) NOT NULL,
  `idNguoiChet` int(11) NOT NULL,
  `ngayKhai` date NOT NULL,
  `lyDoChet` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `lichhoatdong`
--

CREATE TABLE `lichhoatdong` (
  `MaHoatDong` int(11) NOT NULL,
  `TenHoatDong` text NOT NULL,
  `ThoiGianBatDau` datetime NOT NULL,
  `ThoiGianKetThuc` datetime NOT NULL,
  `DuocDuyet` varchar(15) NOT NULL,
  `ThoiGianTao` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `MaNguoiTao` int(12) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `nhankhau`
--

CREATE TABLE `nhankhau` (
  `ID` int(11) NOT NULL,
  `HoTen` text NOT NULL,
  `BiDanh` text DEFAULT NULL,
  `NgaySinh` date NOT NULL,
  `NoiSinh` text NOT NULL,
  `GioiTinh` text NOT NULL,
  `NguyenQuan` text NOT NULL,
  `DanToc` text NOT NULL,
  `NoiThuongTru` text NOT NULL,
  `TonGiao` text DEFAULT NULL,
  `QuocTich` text DEFAULT 'Việt Nam',
  `DiaChiHienNay` text NOT NULL,
  `NgheNghiep` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `nhankhau`
--

INSERT INTO `nhankhau` (`ID`, `HoTen`, `BiDanh`, `NgaySinh`, `NoiSinh`, `GioiTinh`, `NguyenQuan`, `DanToc`, `NoiThuongTru`, `TonGiao`, `QuocTich`, `DiaChiHienNay`, `NgheNghiep`) VALUES
(1, 'dung', 'hh', '2023-01-05', 'hh', 'hhh', 'hhh', 'hh', 'hh', 'hh', 'Việt Nahhhhm', 'hh', 'sinh viên'),
(17, 'ddd', 'dd', '2023-01-03', 'dd', 'Nam', 'd', 'd', 'd', 'd', 'd', 'd', 'd'),
(18, 'bbb', 'bb', '2023-02-11', 'bb', 'Nam', 'b', 'bb', 'b', 'b', 'b', 'b', 'b'),
(19, 'vvv', 'vv', '2023-01-21', 'vv', 'Nam', 'vv', 'vv', 'vv', 'vv', 'vv', 'vvv', 'vv'),
(20, 'vv', 'vv', '2023-02-01', 'v', 'Nam', 'v', 'v', 'v', 'v', 'v', 'v', 'v'),
(21, 'nnn', 'nn', '2023-01-25', 'nn', 'Nam', 'nn', 'nn', 'nnnn', 'nn', 'nn', 'nnn', 'n'),
(22, 'ggg', 'gg', '2023-01-25', 'ggg', 'Nam', 'gg', 'gg', 'gg', 'gg', 'gg', 'g', 'gg'),
(23, 'ddd', 'ddd', '2023-02-01', 'dd', 'Nam', 'd', 'ddd', 'd', 'd', 'dd', 'd', 'dd'),
(24, 'tttt', 'tt', '2023-01-25', 'tttt', 'Nam', 'ttt', 'tttt', 'ttt', 'tttt', 'tttt', 'tttt', 'ttt'),
(25, 'dung dung dung', 'bbb', '2023-01-25', 'bb', 'Nam', 'bbb', 'bb', 'bb', 'bb', 'bb', 'bbb', 'bb'),
(26, 'Dang', 'vvv', '2023-02-02', 'vv', 'Nam', 'v', 'vv', 'v', 'v', 'v', 'v', 'v'),
(27, 'ttt', 'tt', '2023-01-28', 'tt', 'Nam', 'ttt', 'tt', 'tt', 'tt', 'tt', 'ttt', 'ttt'),
(28, 'Hung', 'ff', '2023-01-17', 'ff', 'Nam', 'f', 'f', 'f', 'f', 'f', 'f', 'f'),
(29, 'Dung2', 'f', '2023-01-11', 'f', 'Nam', 'nn', 'f', 'nn', 'f', 'f', 'nnn', 'f'),
(30, 'bach', '1111', '2023-01-19', '111', 'Nam', 'nnn', '111', 'nnn', 'nnn', 'nn', 'nnnn', 'nnn'),
(31, 'freefire', 'ff', '2023-01-18', 'fff', 'Nam', 'f', 'ff', 'ff', 'ff', 'ff', 'fff', 'fff'),
(32, 'bbbbdung', 'h', '2023-01-18', 'h', 'Nam', 'h', 'h', 'h', 'h', 'h', 'h', 'h'),
(33, 'vvvvbbbbbbb', 'b', '2023-01-17', 'b', 'Nam', 'b', 'b', 'b', 'bb', 'b', 'b', 'bb'),
(34, 'rrrr', 'rr', '2023-01-26', 'rr', 'Nam', 'rr', 'rr', 'rr', 'rrr', 'rr', 'rr', 'rrr'),
(35, 'bbb', 'bb', '2023-01-24', 'bb', 'Nam', 'b', 'bb', 'bbbb', 'bbb', 'bbb', 'bbb', 'bb'),
(36, 'hao', 'v', '2023-01-17', 'vv', 'Nam', 'v', 'v', 'vv', 'v', 'v', 'v', 'vvv'),
(37, 'hoan', 't', '2023-01-18', 'tt', 'Nam', 'tt', 't', 't', 'Không', 'Việt Nam', 't', 't'),
(38, 'abc', 't', '2023-01-18', 't', 'Nam', 't', 't', 'tt', 'Không', 'Việt Nam', 't', 't'),
(39, 'ddddd', 't', '2023-01-10', 't', 'Nam', 'tt', 't', 't', 't', 't', 't', 't'),
(40, 'xads', 'tttt', '2023-01-18', 'tt', 'Nam', 'ttt', 'tttt', 'tttttt', 'tttt', 'tttt', 'tttt', 'ttttt'),
(41, 'nfnnfnffnfnn', 't', '2023-02-01', 'ttt', 'Nam', 'tttt', 't', 'tt', 'Không', 'Việt Nam', 'ttt', 't'),
(42, 'nfnnfnffnfnn', 't', '2023-02-01', 'ttt', 'Nam', 'tttt', 't', 'tt', 'Không', 'Việt Nam', 'ttt', 't'),
(43, 'tester', 'vI', '2023-02-01', '  V', 'Nam', 'c', ' c', 'c', 'c', 'c', 'c', 'c'),
(44, 'ggggggggg', 'gg', '2023-01-23', 'g', 'Nam', 'g', 'gg', 'g', 'g', 'g', 'g', 'g'),
(45, 'ggggggggg', 'gg', '2023-01-23', 'g', 'Nam', 'g', 'gg', 'g', 'g', 'g', 'g', 'g'),
(46, 'dunghhhrrggvggg', 'ggg', '2023-01-25', 'gg', 'Nam', 't', 'gg', 't', 'gg', 'gg', 't', 'ggg'),
(47, 'dunghhhrrggvggg', 'ggg', '2023-01-25', 'gg', 'Nam', 't', 'gg', 't', 'gg', 'gg', 't', 'ggg'),
(48, 'gggggggg', 'ggg', '2023-01-23', 'ggg', 'Nam', 'gg', 'gg', 'gg', 'gg', 'gg', 'ggg', 'ggg'),
(49, 'Phạm Trung Dũng', 'Dũng', '2023-01-23', 'vp', 'Nam', 'vp', 'kinh', 'hn', 'ko', 'vn', 'hn', 'sv'),
(50, 'Phạm Trung Dũng', 'Dũng', '2023-01-23', 'vp', 'Nam', 'vp', 'kinh', 'hn', 'ko', 'vn', 'hn', 'sv'),
(51, 'Phạm Trung Bahc', 'v', '2023-01-10', 'v', 'Nam', 'v', 'v', 'v', 'v', 'v', 'v', 'v'),
(52, 'Phạm Trung Bahc', 'v', '2023-01-10', 'v', 'Nam', 'v', 'v', 'v', 'v', 'v', 'v', 'v'),
(53, 'ttttttttt', 't', '2023-01-17', 't', 'Nam', 't', 't', 't', 't', 't', 't', 't'),
(54, 'ttttttttt', 't', '2023-01-17', 't', 'Nam', 't', 't', 't', 't', 't', 't', 't'),
(55, 'vvvv', 'v', '2023-01-18', 'v', 'Nam', 'v', 'v', 'v', 'v', 'v', 'v', 'v'),
(56, 'vvvv', 'v', '2023-01-18', 'v', 'Nam', 'v', 'v', 'v', 'v', 'v', 'v', 'v'),
(57, 'Phạm Trung Dũng', 'hh', '2023-02-01', 'hhh', 'Nam', 'hh', 'hh', 'hh', 'hh', 'hh', 'hh', 'hhhhh'),
(58, 'Phạm Trung Dũng', 'hh', '2023-02-01', 'hhh', 'Nam', 'hh', 'hh', 'hh', 'hh', 'hh', 'hh', 'hhhhh'),
(60, 'Dũng đây chứ đâu', 'b', '2023-01-10', 'b', 'Nam', 'b', 'b', 'b', 'b', 'b', 'b', 'b'),
(61, 'adsfads', 'adfa', '2023-01-10', 'ads', 'Nam', 'ads', 'ád', 'ad', 'ads', 'ads', 'sad', 'dsa'),
(62, 'Lê Văn A', '12', '2023-01-04', 'adsf', 'Nam', 'adsf', 'adf', 'ádf', 'adsf', 'adf', 'ads', 'adsf'),
(63, 'SKT', 'Tuấn', '2023-01-05', 'adsf', 'Nam', 'adsf', 'adsfa', 'ádfa', 'adsfa', 'dsfasdf', 'ádf', 'dfsd'),
(64, 'Developer', '123', '2023-01-05', 'adsf', 'Nam', 'fadsf', 'adfsa', 'à', 'dfads', 'fasd', 'adsf', 'dsfa'),
(65, 'AHAHAHA', 'aa', '2023-01-11', 'adsf', 'Nam', 'sdfa', 'adsfa', 'dsfad', 'dsfa', 'sdfa', 'sfadsfadfa', 'dsfa'),
(66, 'adsfa', 'đầ', '2023-01-04', 'adf', 'Nam', 'adsf', 'ádf', 'adsf', 'ads', 'ádf', 'ádf', 'ádf'),
(67, 'adf', 'sad', '2023-01-11', 'ad', 'Nam', 'a', 'ads', 'sda', 'ád', 'ád', 'dsa', 'ads'),
(70, 'adfad', '12', '2023-01-05', 'adsadsfadsfad', 'Nam', 'fadsfadf', 'sfadsfasdfads', 'adsfasdfa', 'dsfadsfas', 'dfadsfadsfads', 'sdfadsfadsfadsf', 'fadsfa'),
(71, '4.0 GPA', 'd', '2023-02-02', 'adsfadsf', 'Nam', 'dsfadsf', 'dfadfadfadsf', 'adsfads', 'fadsfa', 'dsfadsfa', 'fadsfadf', 'adsfads');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sohokhau`
--

CREATE TABLE `sohokhau` (
  `ID` int(11) NOT NULL,
  `MaHoKhau` text NOT NULL,
  `DiaChi` text NOT NULL DEFAULT 'phố 7 phường La Khê',
  `MaChuHo` int(12) NOT NULL,
  `NgayLap` date DEFAULT NULL,
  `NgayChuyenDi` date DEFAULT NULL,
  `LyDoChuyen` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `sohokhau`
--

INSERT INTO `sohokhau` (`ID`, `MaHoKhau`, `DiaChi`, `MaChuHo`, `NgayLap`, `NgayChuyenDi`, `LyDoChuyen`) VALUES
(5, '123321', 'Dinh Hoa', 66, '2013-01-02', '0000-00-00', ''),
(999, '123123123', 'phố 7 phường La Khê', 25, '2019-02-09', '0000-00-00', ''),
(1000, '1234', 'so 8 la khe', 70, '2020-09-09', '0000-00-00', ''),
(1001, '777', 'dia chi day', 1, '2012-02-28', '0000-00-00', ''),
(1002, '987', 'thanh', 1, '2014-02-02', '0000-00-00', ''),
(1004, '98', 'thanh', 1, '2011-01-01', NULL, NULL),
(1005, '999', 'thanhh', 1, '2013-12-12', NULL, NULL),
(1006, '3434', 'dhtn', 1, '2023-01-17', NULL, NULL),
(1007, '789', 'Hai Duong', 30, '2023-01-17', NULL, NULL),
(1008, 'aa', 'a', 30, '2023-01-17', NULL, NULL),
(1009, 'hhh', 'hhh', 51, '2023-01-17', NULL, NULL),
(1011, '188063175', 'ĐHTN', 53, '2023-01-17', NULL, NULL),
(1012, '364464530', 'KAKAKKA', 23, '2023-01-17', NULL, NULL),
(1013, '261287022', 'hihihihihihi', 57, '2023-01-17', NULL, NULL),
(1015, '306037256', 'CHỢ CHU', 63, '2023-01-17', NULL, NULL),
(1016, '513316587', 'JJJJJ', 64, '2023-01-17', NULL, NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `tamtru`
--

CREATE TABLE `tamtru` (
  `ID` int(11) NOT NULL,
  `idNhankhau` int(11) NOT NULL,
  `sdtNgDangKi` int(11) NOT NULL,
  `noiTamTru` text NOT NULL,
  `tuNgay` date NOT NULL,
  `denNgay` date NOT NULL,
  `lido` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `tamvang`
--

CREATE TABLE `tamvang` (
  `ID` int(11) NOT NULL,
  `idNhankhau` int(11) NOT NULL,
  `tuNgay` date NOT NULL,
  `denNgay` date NOT NULL,
  `lydo` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `thanhviencuaho`
--

CREATE TABLE `thanhviencuaho` (
  `idNhanKhau` int(11) NOT NULL,
  `idHoKhau` int(11) NOT NULL,
  `quanHeVoiChuHo` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `thanhviencuaho`
--

INSERT INTO `thanhviencuaho` (`idNhanKhau`, `idHoKhau`, `quanHeVoiChuHo`) VALUES
(32, 1001, 'Bo'),
(61, 1008, 'bo'),
(65, 5, 'aa'),
(71, 1000, '1a');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `user`
--

CREATE TABLE `user` (
  `userId` int(11) NOT NULL,
  `username` text NOT NULL,
  `password` text NOT NULL,
  `role` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `user`
--

INSERT INTO `user` (`userId`, `username`, `password`, `role`) VALUES
(1, 'admin', '12112312f12917a1571a51a714318914a10e14a18011f1c3', 'totruong'),
(2, 'admin2', '1c81421581e91c31901591a819a1b717d18416d1da1b9109', 'canbo'),
(3, 'cocc', '1a11191391a31011c31ae11510118d1f51591a81a21b1102', 'canbo');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `cccd`
--
ALTER TABLE `cccd`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `idNhankhau_2` (`idNhankhau`),
  ADD KEY `idNhankhau` (`idNhankhau`);

--
-- Chỉ mục cho bảng `cosovatchat`
--
ALTER TABLE `cosovatchat`
  ADD PRIMARY KEY (`MaDoDung`);

--
-- Chỉ mục cho bảng `hoatdong_cosovatchat`
--
ALTER TABLE `hoatdong_cosovatchat`
  ADD PRIMARY KEY (`MaHoatDong`,`MaDoDung`),
  ADD KEY `MaHoatDong` (`MaHoatDong`),
  ADD KEY `MaDoDung` (`MaDoDung`);

--
-- Chỉ mục cho bảng `khaitu`
--
ALTER TABLE `khaitu`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `idNguoiChet` (`idNguoiChet`),
  ADD KEY `idNguoiKhai` (`idNguoiKhai`);

--
-- Chỉ mục cho bảng `lichhoatdong`
--
ALTER TABLE `lichhoatdong`
  ADD PRIMARY KEY (`MaHoatDong`),
  ADD KEY `MaNguoiTao` (`MaNguoiTao`);

--
-- Chỉ mục cho bảng `nhankhau`
--
ALTER TABLE `nhankhau`
  ADD PRIMARY KEY (`ID`);

--
-- Chỉ mục cho bảng `sohokhau`
--
ALTER TABLE `sohokhau`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `MaChuHo` (`MaChuHo`);

--
-- Chỉ mục cho bảng `tamtru`
--
ALTER TABLE `tamtru`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `idNhankhau` (`idNhankhau`);

--
-- Chỉ mục cho bảng `tamvang`
--
ALTER TABLE `tamvang`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `idNhankhau` (`idNhankhau`);

--
-- Chỉ mục cho bảng `thanhviencuaho`
--
ALTER TABLE `thanhviencuaho`
  ADD PRIMARY KEY (`idNhanKhau`,`idHoKhau`),
  ADD KEY `idHoKhau` (`idHoKhau`);

--
-- Chỉ mục cho bảng `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`userId`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `cccd`
--
ALTER TABLE `cccd`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=42;

--
-- AUTO_INCREMENT cho bảng `nhankhau`
--
ALTER TABLE `nhankhau`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=72;

--
-- AUTO_INCREMENT cho bảng `sohokhau`
--
ALTER TABLE `sohokhau`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1017;

--
-- AUTO_INCREMENT cho bảng `user`
--
ALTER TABLE `user`
  MODIFY `userId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `cccd`
--
ALTER TABLE `cccd`
  ADD CONSTRAINT `cccd_ibfk_1` FOREIGN KEY (`idNhankhau`) REFERENCES `nhankhau` (`ID`);

--
-- Các ràng buộc cho bảng `hoatdong_cosovatchat`
--
ALTER TABLE `hoatdong_cosovatchat`
  ADD CONSTRAINT `hoatdong_cosovatchat_ibfk_1` FOREIGN KEY (`MaHoatDong`) REFERENCES `lichhoatdong` (`MaHoatDong`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `hoatdong_cosovatchat_ibfk_2` FOREIGN KEY (`MaDoDung`) REFERENCES `cosovatchat` (`MaDoDung`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `khaitu`
--
ALTER TABLE `khaitu`
  ADD CONSTRAINT `khaitu_ibfk_1` FOREIGN KEY (`idNguoiKhai`) REFERENCES `nhankhau` (`ID`),
  ADD CONSTRAINT `khaitu_ibfk_2` FOREIGN KEY (`idNguoiChet`) REFERENCES `nhankhau` (`ID`);

--
-- Các ràng buộc cho bảng `lichhoatdong`
--
ALTER TABLE `lichhoatdong`
  ADD CONSTRAINT `lichhoatdong_ibfk_1` FOREIGN KEY (`MaNguoiTao`) REFERENCES `nhankhau` (`ID`);

--
-- Các ràng buộc cho bảng `sohokhau`
--
ALTER TABLE `sohokhau`
  ADD CONSTRAINT `sohokhau_ibfk_1` FOREIGN KEY (`MaChuHo`) REFERENCES `nhankhau` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `tamtru`
--
ALTER TABLE `tamtru`
  ADD CONSTRAINT `tamtru_ibfk_1` FOREIGN KEY (`idNhankhau`) REFERENCES `nhankhau` (`ID`);

--
-- Các ràng buộc cho bảng `tamvang`
--
ALTER TABLE `tamvang`
  ADD CONSTRAINT `tamvang_ibfk_1` FOREIGN KEY (`idNhankhau`) REFERENCES `nhankhau` (`ID`);

--
-- Các ràng buộc cho bảng `thanhviencuaho`
--
ALTER TABLE `thanhviencuaho`
  ADD CONSTRAINT `thanhviencuaho_ibfk_3` FOREIGN KEY (`idNhanKhau`) REFERENCES `nhankhau` (`ID`),
  ADD CONSTRAINT `thanhviencuaho_ibfk_4` FOREIGN KEY (`idHoKhau`) REFERENCES `sohokhau` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
