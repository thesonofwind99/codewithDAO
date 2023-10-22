CREATE DATABASE Polypro1
GO
USE Polypro1
--Tạo bảng Chuyên đề
CREATE TABLE ChuyenDe
(MaCD nchar(5) primary key,
TenCD nvarchar(50) not null,
HocPhi float not null,
ThoiLuong int not null,
Hinh nvarchar(50) not null,
MoTa nvarchar(255) not null)
GO
--Tạo bảng Khoá học
CREATE TABLE KhoaHoc
(MaKH int primary key IDENTITY,
MaCD nchar(5) not null,
HocPhi float not null,
ThoiLuong int not null,
NgayKG date not null,
GhiChu nvarchar(255) null,
MaNV nvarchar(20) not null,
NgayTao date not null)
GO
--Tạo bảng Nhân viên
CREATE TABLE NhanVien
(MaNV nvarchar(20) primary key,
MatKhau nvarchar(50) not null,
HoTen nvarchar(50) not null,
VaiTro bit not null)
GO
--Tạo bảng người học
CREATE TABLE NguoiHoc
(MaNH nchar(7) primary key,
HoTen nvarchar(50) not null,
GioiTinh bit not null,
NgaySinh date not null,
DienThoai nvarchar(24) not null,
Email nvarchar(50) not null,
GhiChu nvarchar(255) null,
MaNV nvarchar(20) not null,
NgayDK date not null)
GO
--Tạo bảng học viên
CREATE TABLE HocVien
(MaHV int primary key IDENTITY,
MaKH int not null,
MaNH nchar(7) not null,
Diem float not null)
GO
--THỦ TỤC LƯU
--Thống kê bảng điểm
IF OBJECT_ID(N'TK_bangdiem') IS NOT NULL
DROP PROC TK_bangdiem
GO
CREATE PROC TK_bangdiem (@MaKH int)
AS
BEGIN
	SELECT NguoiHoc.MaNH, HoTen, Diem FROM HocVien 
	JOIN NguoiHoc ON HocVien.MaNH = NguoiHoc.MaNH 
	WHERE MaKH = @MaKH 
	ORDER BY Diem DESC
END
GO
--Thống kê điểm chuyên đề
IF OBJECT_ID(N'TK_diemchuyende') IS NOT NULL
DROP PROC TK_diemchuyende
GO
CREATE PROC TK_diemchuyende
AS
BEGIN
	SELECT TenCD, COUNT(MaHV) SoHV, MIN(Diem) ThapNhat, MAX(Diem) CaoNhat, AVG(Diem) TrungBinh
	FROM KhoaHoc kh
	JOIN HocVien hv ON kh.MaKH = hv.MaKH
	JOIN ChuyenDe cd ON cd.MaCD = kh.MaCD
	GROUP BY TenCD
END
GO
--Thống kê doanh thu
IF OBJECT_ID(N'TK_doanhthu') IS NOT NULL
DROP PROC TK_doanhthu
GO
CREATE PROC TK_doanhthu(@Year int)
AS
BEGIN
	SELECT TenCD, COUNT(DISTINCT kh.MaKH) SoKH , COUNT(hv.MaHV) SoHV, SUM(kh.HocPhi) DoanhThu, MIN(kh.HocPhi) HocPhiThapNhat, MAX(kh.HocPhi) HocPhiCaoNhat, AVG(kh.HocPhi) HocPhiTB
	FROM KhoaHoc kh
	JOIN HocVien hv ON kh.MaKH = hv.MaKH
	JOIN ChuyenDe cd ON cd.MaCD = kh.MaCD
	WHERE YEAR(NgayKG) = @Year
	GROUP BY TenCD
END
GO
--Thống kê người học
IF OBJECT_ID(N'TK_nguoihoc') IS NOT NULL
	DROP PROC TK_nguoihoc
GO
CREATE PROC TK_nguoihoc
AS
BEGIN
	SELECT YEAR(NgayDK) Nam, COUNT(*) SoLuong, MIN(NgayDK) DauTien, MAX(NgayDK) CuoiCung
	FROM NguoiHoc
	GROUP BY YEAR(NgayDK)
END
GO
