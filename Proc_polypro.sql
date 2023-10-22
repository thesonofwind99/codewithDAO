-- ================================================
-- Template generated from Template Explorer using:
-- Create Procedure (New Menu).SQL
--
-- Use the Specify Values for Template Parameters 
-- command (Ctrl-Shift-M) to fill in the parameter 
-- values below.
--
-- This block of comments will not be included in
-- the definition of the procedure.
-- ================================================
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
USE Polypro
GO
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

