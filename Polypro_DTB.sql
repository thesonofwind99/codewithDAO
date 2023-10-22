CREATE DATABASE Polypro
GO
USE Polypro
GO
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
--Tạo ràng buộc vai trò luôn là nhân viên cho bảng Nhân Viên
ALTER TABLE NhanVien ADD CONSTRAINT DF_ROLE DEFAULT 0 FOR VaiTro
GO
--Tạo ràng buộc ngày đăng ký luôn là ngày hiện tại cho bảng Người học
ALTER TABLE NguoiHoc ADD CONSTRAINT DF_GETDATE DEFAULT GETDATE() FOR NgayDK
GO
--Tạo ràng buộc điểm của học viên mới chèn dữ liệu là 0 trong bảng Học viên
ALTER TABLE HocVien ADD CONSTRAINT DF_Diem DEFAULT -1 FOR Diem
GO
--Tạo ràng buộc ngày tạo của Khoá Học luôn là ngày hiện tại
ALTER TABLE KhoaHoc ADD CONSTRAINT DF_GETDATE_KH DEFAULT GETDATE() FOR NgayTao
GO
--Tạo ràng buộc CHECK học phí và thời lượng phải >= 0 ở bảng Khoá học
ALTER TABLE KhoaHoc ADD CONSTRAINT CHK_HP_TL_KH CHECK(HocPhi >= 0 AND ThoiLuong>0)
GO
--Tạo ràng buộc CHECK học phí và thời lượng phải >=0 ở bảng Chuyên đề
ALTER TABLE ChuyenDe ADD CONSTRAINT CHK_HP_TL_CD CHECK(HocPhi >= 0 AND ThoiLuong>0)
GO
--Tạo ràng buộc Khoá ngoại cho bảng Khoá học
ALTER TABLE KhoaHoc ADD CONSTRAINT FK_CD_KH FOREIGN KEY (MaCD) REFERENCES ChuyenDe(MaCD) ON DELETE NO ACTION ON UPDATE CASCADE
GO
ALTER TABLE KhoaHoc ADD CONSTRAINT FK_NV_KH FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV) ON DELETE NO ACTION ON UPDATE CASCADE
GO
--Tạo ràng buộc Khoá ngoại cho bảng Người học
ALTER TABLE NguoiHoc ADD CONSTRAINT FK_NV_NH FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV) ON DELETE NO ACTION ON UPDATE CASCADE
GO
--Tạo ràng buộc khoá ngoại cho bảng Học Viên
ALTER TABLE HocVien ADD CONSTRAINT FK_NH_HV FOREIGN KEY (MaNH) REFERENCES NguoiHoc(MaNH) ON DELETE NO ACTION ON UPDATE CASCADE
GO
ALTER TABLE HocVien ADD CONSTRAINT FK_KH_HV FOREIGN KEY (MaKH) REFERENCES KhoaHoc(MaKH) ON DELETE NO ACTION ON UPDATE NO ACTION
GO
--Mã lệnh SQL Cơ bản
INSERT INTO NhanVien (MaNV, MatKhau, HoTen, VaiTro) VALUES (?, ?, ?, ?)
UPDATE NhanVien SET MatKhau = ?, HoTen = ?, VaiTro = ? WHERE MaNV = ?
DELETE FROM NhanVien WHERE MaNV = ?
SELECT * FROM NhanVien WHERE MaNV = ?
--Sửa ràng buộc từ NOT NULL -> NULL
ALTER TABLE ChuyenDe ALTER COLUMN MoTa nvarchar(255) NULL
GO
--Chèn dữ liệu vào bảng
INSERT INTO ChuyenDe VALUES --INSERT RỒI
(N'JAV01', N'Lập trình Java cơ bản', 250, 90, N'GAME.png', N'JAV01 - Lập trình Java cơ bản'),
(N'JAV02', N'Lập trình Java nâng cao', 300, 90, N'HTCS.jpg', N'JAV02 - Lập trình Java nâng cao'),
(N'JAV03', N'Lập trình mạng với Java', 200, 70, N'INMA.jpg', N'JAV03 - Lập trình mạng với Java'),
(N'JAV04', N'Lập trình desktop với Swing', 200, 70, N'ADAV.jpg', N'JAV04 - Lập trình desktop với Swing'),
(N'PRO01', N'Dự án với công nghệ MS.NET MVC', 300, 90, N'MOWE.png', N'PRO01 - Dự án với công nghệ MS.NET MVC'),
(N'PRO02', N'Dự án với công nghệ Spring MVC', 300, 90, N'Subject.png', N'PRO02 - Dự án với công nghệ Spring MVC'),
(N'PRO03', N'Dự án với công nghệ Servlet/JSP', 300, 90, N'GAME.png', N'PRO03 - Dự án với công nghệ Servlet/JSP'),
(N'PRO04', N'Dự án với AngularJS & WebAPI', 300, 90, N'HTCS.jpg', N'PRO04 - Dự án với AngularJS & WebAPI'),
(N'PRO05', N'Dự án với Swing & JDBC', 300, 90, N'INMA.jpg', N'PRO05 - Dự án với Swing & JDBC'),
(N'PRO06', N'Dự án với WindowForm', 300, 90, N'LAYO.jpg', N'PRO06 - Dự án với WindowForm'),
(N'RDB01', N'Cơ sở dữ liệu SQL Server', 100, 50, N'MOWE.png', N'RDB01 - Cơ sở dữ liệu SQL Server'),
(N'RDB02', N'Lập trình JDBC', 150, 60, N'Subject.png', N'RDB02 - Lập trình JDBC'),
(N'RDB03', N'Lập trình cơ sở dữ liệu Hibernate', 250, 80, N'GAME.png', N'RDB03 - Lập trình cơ sở dữ liệu Hibernate'),
(N'SER01', N'Lập trình web với Servlet/JSP', 350, 100, N'HTCS.jpg', N'SER01 - Lập trình web với Servlet/JSP'),
(N'SER02', N'Lập trình Spring MVC', 400, 110, N'INMA.jpg', N'SER02 - Lập trình Spring MVC'),
(N'SER03', N'Lập trình MS.NET MVC', 400, 110, N'LAYO.jpg', N'SER03 - Lập trình MS.NET MVC'),
(N'SER04', N'Xây dựng Web API với Spring MVC & ASP.NET MVC', 200, 70, N'MOWE.png', N'SER04 - Xây dựng Web API với Spring MVC & ASP.NET MVC'),
(N'WEB01', N'Thiết kế web với HTML và CSS', 200, 70, N'Subject.png', N'WEB01 - Thiết kế web với HTML và CSS'),
(N'WEB02', N'Thiết kế web với Bootstrap', 0, 40, N'GAME.png', N'WEB02 - Thiết kế web với Bootstrap'),
(N'WEB03', N'Lập trình front-end với JavaScript và jQuery', 150, 60, N'HTCS.jpg', N'WEB03 - Lập trình front-end với JavaScript và jQuery'),
(N'WEB04', N'Lập trình AngularJS', 250, 80, N'INMA.jpg', N'WEB04 - Lập trình AngularJS')
GO
INSERT INTO NhanVien VALUES --INSERT RỒI
(N'LongNDH', N'songlong', N'Nguyễn Đình Hoàng Long', 0),
(N'LongNDT', N'songlong', N'Nguyễn Đình Thiên Long', 0),
(N'NghiemN', N'songlong', N'Nguyễn Nghiệm', 1),
(N'NoPT', N'123456', N'Phạm Thị Nở', 0),
(N'PheoNC', N'123456', N'Nguyễn Chí Phèo', 0),
(N'TeoNV', N'songlong', N'Nguyễn Văn Tèo', 1),
(N'ThaoLTH', N'songlong', N'Lê Thị Hương Thảo', 0)
GO
INSERT INTO NguoiHoc VALUES --INSERT RỒI
(N'PS01638', N'LỮ HUY CƯỜNG', 1, CAST(0xAF170B00 AS Date), N'0928768265', N'PS01638@fpt.edu.vn', N'LỮ HUY CƯỜNG', N'TeoNV', '2022-10-19'),
(N'PS02037', N'ĐỖ VĂN MINH', 1, CAST(0xC6190B00 AS Date), N'0968095685', N'PS02037@fpt.edu.vn', N'ĐỖ VĂN MINH', N'NghiemN', '2023-04-11'),
(N'PS02771', N'NGUYỄN TẤN HIẾU', 1, CAST(0x2E220B00 AS Date), N'0927594734', N'PS02771@fpt.edu.vn', N'NGUYỄN TẤN HIẾU', N'LongNDH', '2023-07-25'),
(N'PS02867', N'NGUYỄN HỮU TRÍ', 1, CAST(0xEB200B00 AS Date), N'0946984711', N'PS02867@fpt.edu.vn', N'NGUYỄN HỮU TRÍ', N'NhanDT', '2023-06-07'),
(N'PS02930', N'TRẦN VĂN NAM', 1, CAST(0xA1240B00 AS Date), N'0924774498', N'PS02930@fpt.edu.vn', N'TRẦN VĂN NAM', N'TeoNV', '2022-11-25'),
(N'PS02979', N'ĐOÀN TRẦN NHẬT VŨ', 1, CAST(0x671C0B00 AS Date), N'0912374818', N'PS02979@fpt.edu.vn', N'0ĐOÀN TRẦN NHẬT VŨ', N'NoPT', '2023-05-30'),
(N'PS02983', N'NGUYỄN HOÀNG THIÊN PHƯỚC', 1, CAST(0x681A0B00 AS Date), N'0912499836', N'PS02983@fpt.edu.vn', N'0NGUYỄN HOÀNG THIÊN PHƯỚC', N'ThaoLTH', '2023-09-11'),
(N'PS02983', N'HỒ HỮU HẬU', 1, CAST(0x311A0B00 AS Date), N'0924984876', N'PS02988@fpt.edu.vn', N'HỒ HỮU HẬU', N'TeoNV', '2022-12-11'),
(N'PS03031', N'PHAN TẤN VIỆT', 1, CAST(0x21160B00 AS Date), N'0924832716', N'PS03031@fpt.edu.vn', N'PHAN TẤN VIỆT', N'LongNDT', '2022-10-20'),
(N'PS03046', N'NGUYỄN CAO PHƯỚC', 1, CAST(0xDE150B00 AS Date), N'0977117727', N'PS03046@fpt.edu.vn', N'NGUYỄN CAO PHƯỚC', N'NhanDT', '2022-10-29'),
(N'PS03080', N'HUỲNH THANH HUY', 1, CAST(0x701C0B00 AS Date), N'0916436052', N'PS03080@fpt.edu.vn', N'HUỲNH THANH HUY', N'NoPT', '2023-05-29'),
(N'PS03088', N'NGUYỄN HOÀNG TRUNG', 1, CAST(0x24180B00 AS Date), N'0938101529', N'PS03088@fpt.edu.vn', N'NGUYỄN HOÀNG TRUNG', N'ThaoLTH', '2022-10-03'),
(N'PS03096', N'ĐOÀN HỮU KHANG', 1, CAST(0xAB1B0B00 AS Date), N'0945196719', N'PS03096@fpt.edu.vn', N'ĐOÀN HỮU KHANG', N'NghiemN', '2023-02-17'),
(N'PS03104', N'LÊ THÀNH PHƯƠNG', 1, CAST(0x3E1A0B00 AS Date), N'0922948096', N'PS03104@fpt.edu.vn', N'LÊ THÀNH PHƯƠNG', N'TeoNV', '2023-10-02'),
(N'PS03120', N'PHẠM NGỌC NHẬT TRƯỜNG', 1, CAST(0x48230B00 AS Date), N'0994296169', N'PS03120@fpt.edu.vn', N'PHẠM NGỌC NHẬT TRƯỜNG', N'LongNDT', '2023-07-03'),
(N'PS03130', N'ĐẶNG BẢO VIỆT', 1, CAST(0xEF150B00 AS Date), N'0917749344', N'PS03130@fpt.edu.vn', N'ĐẶNG BẢO VIỆT', N'ThaoLTH', '2023-04-10'),
(N'PS03134', N'LÊ DUY BẢO', 1, CAST(0x2E1F0B00 AS Date), N'0926714368', N'PS03134@fpt.edu.vn', N'LÊ DUY BẢO', N'LongNDH', '2022-11-09'),
(N'PS03172', N'NGUYỄN ANH TUẤN', 1, CAST(0xCA180B00 AS Date), N'0920020472', N'PS03172@fpt.edu.vn', N'NGUYỄN ANH TUẤN', N'NhanDT', '2023-09-08'),
(N'PS03202', N'PHAN QUỐC QUI', 1, CAST(0x741E0B00 AS Date), N'0930649274', N'PS03202@fpt.edu.vn', N'PHAN QUỐC QUI', N'NoPT', '2023-07-14'),
(N'PS03203', N'ĐẶNG LÊ QUANG VINH', 1, CAST(0xC4150B00 AS Date), N'0920197355', N'PS03203@fpt.edu.vn', N'ĐẶNG LÊ QUANG VINH', N'NghiemN', '2023-04-30'),
(N'PS03205', N'NGUYỄN MINH SANG', 1, CAST(0x5E1D0B00 AS Date), N'0967006218', N'PS03205@fpt.edu.vn', N'NGUYỄN MINH SANG', N'TeoNV', '2022-10-27'),
(N'PS03222', N'TRẦM MINH MẪN', 1, CAST(0xE71F0B00 AS Date), N'0911183649', N'PS03222@fpt.edu.vn', N'TRẦM MINH MẪN', N'ThaoLTH', '2023-02-12'),
(N'PS03640', N'LƯU THANH NGỌC', 0,CAST(0x591B0B00 AS Date), N'0918358164', N'PS03640@fpt.edu.vn', N'LƯU THANH NGỌC', N'TeoNV', '2023-07-09'),
(N'PS03603', N'LÊ PHẠM KIM THANH', 0,CAST(0x501C0B00 AS Date), N'0924696779', N'PS03603@fpt.edu.vn', N'LÊ PHẠM KIM THANH', N'ThaoLTH', '2023-09-28'),
(N'PS03488', N'NGUYỄN NHƯ NGỌC', 0,CAST(0x651D0B00 AS Date), N'0912880267', N'PS03488@fpt.edu.vn', N'NGUYỄN NHƯ NGỌC', N'NoPT', '2023-01-12')
GO
INSERT INTO KhoaHoc ([MaCD], [HocPhi], [ThoiLuong], [NgayTao], [GhiChu], [MaNV], [NgayKG]) VALUES --INSERT RỒI
(N'PRO04', 300, 90, CAST(0xD3400B00 AS Date), N'', N'TeoNV', CAST(0xE6400B00 AS Date)),
(N'JAV01', 300, 90, CAST(0xF6400B00 AS Date), N'Lập trình Java cơ bản', N'TeoNV', CAST(0x0A410B00 AS Date)),
(N'JAV02', 300, 90, CAST(0xF6400B00 AS Date), N'Lập trình Java nâng cao', N'TeoNV', CAST(0x0A410B00 AS Date)),
(N'JAV03', 200, 70, CAST(0xF6400B00 AS Date), N'Lập trình mạng với Java', N'TeoNV', CAST(0x0A410B00 AS Date)),
(N'JAV04', 200, 70, CAST(0xF6400B00 AS Date), N'Lập trình ứng dụng Desktop với Java', N'TeoNV', CAST(0x0A410B00 AS Date)),
(N'PRO01', 300, 90, CAST(0xF6400B00 AS Date), N'Lập trình .NET MVC', N'TeoNV', CAST(0x0A410B00 AS Date)),
(N'PRO02', 300, 90, CAST(0xF6400B00 AS Date), N'Lập trình Spring MVC', N'TeoNV', CAST(0x0A410B00 AS Date)),
(N'PRO03', 300, 90, CAST(0xF6400B00 AS Date), N'Làm dự án với Servlet và JSP', N'TeoNV', CAST(0x0A410B00 AS Date)),
(N'PRO04', 300, 90, CAST(0xF6400B00 AS Date), N'Làm dự án với REST API và AngularJS', N'TeoNV', CAST(0x0A410B00 AS Date)),
(N'JAV01', 300, 90, CAST(0xF4400B00 AS Date), N'Lập trình Java cơ bản', N'TeoNV', CAST(0x0A410B00 AS Date)),
(N'JAV01', 250, 90, CAST(0xF2400B00 AS Date), N'Lập trình Java cơ bản', N'TeoNV', CAST(0x0A410B00 AS Date))
GO
DELETE FROM KhoaHoc
insert into HocVien (MaKH, MaNH, Diem) values (31, 'PS03080', 9);
insert into HocVien (MaKH, MaNH, Diem) values (31, 'PS02983', 5);
insert into HocVien (MaKH, MaNH, Diem) values (29, 'PS02867', 7);
insert into HocVien (MaKH, MaNH, Diem) values (29, 'PS02771', 6);
insert into HocVien (MaKH, MaNH, Diem) values (22, 'PS02979', 10);
insert into HocVien (MaKH, MaNH, Diem) values (26, 'PS02983', 10);
insert into HocVien (MaKH, MaNH, Diem) values (27, 'PS03088', 6);
insert into HocVien (MaKH, MaNH, Diem) values (30, 'PS02979', 6);
insert into HocVien (MaKH, MaNH, Diem) values (22, 'PS02983', 2);
insert into HocVien (MaKH, MaNH, Diem) values (29, 'PS03046', 3);
insert into HocVien (MaKH, MaNH, Diem) values (31, 'PS03088', 5);
insert into HocVien (MaKH, MaNH, Diem) values (27, 'PS02867', 5);
insert into HocVien (MaKH, MaNH, Diem) values (25, 'PS02037', 9);
insert into HocVien (MaKH, MaNH, Diem) values (29, 'PS02979', 8);
insert into HocVien (MaKH, MaNH, Diem) values (23, 'PS03080', 5);
insert into HocVien (MaKH, MaNH, Diem) values (28, 'PS01638', 10);
insert into HocVien (MaKH, MaNH, Diem) values (29, 'PS02037', 2);
insert into HocVien (MaKH, MaNH, Diem) values (30, 'PS02037', 4);
insert into HocVien (MaKH, MaNH, Diem) values (21, 'PS03046', 10);
insert into HocVien (MaKH, MaNH, Diem) values (22, 'PS02930', 10);
insert into HocVien (MaKH, MaNH, Diem) values (26, 'PS02979', 7);
insert into HocVien (MaKH, MaNH, Diem) values (22, 'PS02037', 9);
insert into HocVien (MaKH, MaNH, Diem) values (24, 'PS03046', 2);
insert into HocVien (MaKH, MaNH, Diem) values (27, 'PS02037', 2);
insert into HocVien (MaKH, MaNH, Diem) values (21, 'PS01638', 8);
insert into HocVien (MaKH, MaNH, Diem) values (23, 'PS03088', 3);
insert into HocVien (MaKH, MaNH, Diem) values (25, 'PS02979', 3);
insert into HocVien (MaKH, MaNH, Diem) values (30, 'PS03046', 7);
insert into HocVien (MaKH, MaNH, Diem) values (28, 'PS02037', 3);
insert into HocVien (MaKH, MaNH, Diem) values (24, 'PS02979', 6);
insert into HocVien (MaKH, MaNH, Diem) values (23, 'PS02979', 4);
insert into HocVien (MaKH, MaNH, Diem) values (28, 'PS03088', 5);
insert into HocVien (MaKH, MaNH, Diem) values (28, 'PS03046', 4);
insert into HocVien (MaKH, MaNH, Diem) values (23, 'PS02983', 4);
insert into HocVien (MaKH, MaNH, Diem) values (23, 'PS02037', 3);
insert into HocVien (MaKH, MaNH, Diem) values (27, 'PS02983', 4);
insert into HocVien (MaKH, MaNH, Diem) values (27, 'PS02771', 1);
insert into HocVien (MaKH, MaNH, Diem) values (29, 'PS03080', 7);
insert into HocVien (MaKH, MaNH, Diem) values (21, 'PS02983', 5);
insert into HocVien (MaKH, MaNH, Diem) values (22, 'PS03080', 3);
insert into HocVien (MaKH, MaNH, Diem) values (23, 'PS01638', 8);
insert into HocVien (MaKH, MaNH, Diem) values (28, 'PS02979', 6);
insert into HocVien (MaKH, MaNH, Diem) values (27, 'PS01638', 2);
insert into HocVien (MaKH, MaNH, Diem) values (24, 'PS03031', 8);
insert into HocVien (MaKH, MaNH, Diem) values (24, 'PS02983', 7);
insert into HocVien (MaKH, MaNH, Diem) values (21, 'PS03088', 9);
insert into HocVien (MaKH, MaNH, Diem) values (31, 'PS03046', 9);
insert into HocVien (MaKH, MaNH, Diem) values (29, 'PS02983', 9);
insert into HocVien (MaKH, MaNH, Diem) values (28, 'PS02867', 9);
insert into HocVien (MaKH, MaNH, Diem) values (21, 'PS02867', 3);
insert into HocVien (MaKH, MaNH, Diem) values (21, 'PS02771', 4);
insert into HocVien (MaKH, MaNH, Diem) values (30, 'PS03080', 8);
insert into HocVien (MaKH, MaNH, Diem) values (22, 'PS03046', 4);
insert into HocVien (MaKH, MaNH, Diem) values (21, 'PS02037', 9);
insert into HocVien (MaKH, MaNH, Diem) values (31, 'PS01638', 7);
insert into HocVien (MaKH, MaNH, Diem) values (25, 'PS03031', 9);
insert into HocVien (MaKH, MaNH, Diem) values (24, 'PS01638', 1);
SELECT * FROM NguoiHoc WHERE HoTen LIKE '%c%' AND MaNH NOT IN(SELECT MaNH FROM HocVien WHERE MaKH = 6)
select * from NhanVien
SELECT HocVien.*, HoTen FROM HocVien JOIN NguoiHoc ON HocVien.MaNH = NguoiHoc.MaNH WHERE MaKH = 6;
SELECT DISTINCT YEAR(NgayKG) FROM KhoaHoc
SELECT * FROM NguoiHoc

