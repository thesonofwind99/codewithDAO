/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.extendent;

import com.mycompany.classdao.ThongKeDAO;
import com.mycompany.entity.KhoaHoc;
import java.sql.SQLException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JTable;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author Administrator
 */
public class ExcelUntil {
    public static Workbook printBangDiemKhoaHocToExcel(javax.swing.JTable tblBangDiem, javax.swing.JComboBox<String> cbKhoaHoc, ThongKeDAO tkDAO) throws SQLException, FileNotFoundException, IOException{
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Thống kê bảng điểm");
        KhoaHoc kh = (KhoaHoc) cbKhoaHoc.getSelectedItem();
        List<Object[]> list = tkDAO.getBangDiem(kh.getMaKH());
        int rownum = 0;
        Cell cell;
        Row row;
        HSSFCellStyle style = createStyleForTitle(workbook);
        row = sheet.createRow(rownum);
        //Mã người học
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("Mã người học");
        cell.setCellStyle(style);
        //Họ Tên
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("Họ tên");
        cell.setCellStyle(style);
        //Điểm
        cell = row.createCell(2, CellType.NUMERIC);
        cell.setCellValue("Điểm");
        cell.setCellStyle(style);
        //Xếp loại
        cell = row.createCell(3, CellType.STRING);
        cell.setCellValue("Xếp loại");
        cell.setCellStyle(style);
        for(int i = 0; i<list.size(); i++){
            rownum++;
            row = sheet.createRow(rownum);
            //ID Student
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue((String) tblBangDiem.getValueAt(i, 0));
            //Full name
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue((String) tblBangDiem.getValueAt(i, 1));
            //Point
            cell = row.createCell(2, CellType.STRING);
            cell.setCellValue((double) tblBangDiem.getValueAt(i, 2));
            //CLASSIFICATION
            cell = row.createCell(3, CellType.STRING);
            cell.setCellValue((String) tblBangDiem.getValueAt(i, 3));            
        }
        return workbook;
    }
    public static Workbook printNguoiHocToExcel(javax.swing.JTable tblNguoiHoc, ThongKeDAO tkDao) throws SQLException, FileNotFoundException, IOException{
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Thống kê người học");
        List<Object[]> list = tkDao.getNguoiHoc();
        int rownum = 0;
        Cell cell;
        Row row;
        HSSFCellStyle style = createStyleForTitle(workbook);
        row = sheet.createRow(rownum);
        //Năm
        cell = row.createCell(0, CellType.NUMERIC);
        cell.setCellValue("Năm");
        cell.setCellStyle(style);
        //Số người học
        cell = row.createCell(1, CellType.NUMERIC);
        cell.setCellValue("Số người học");
        cell.setCellStyle(style);
        //Đăng ký sớm nhất
        cell = row.createCell(2, CellType.STRING);
        cell.setCellValue("Đăng ký sớm nhất");
        cell.setCellStyle(style);
        //Đăng ký muộn nhất
        cell = row.createCell(3, CellType.STRING);
        cell.setCellValue("Đăng ký muộn nhất");
        cell.setCellStyle(style);
        for(int i = 0; i<list.size(); i++){
            rownum++;
            row = sheet.createRow(rownum);
            //Năm
            cell = row.createCell(0, CellType.NUMERIC);
            cell.setCellValue((int) tblNguoiHoc.getValueAt(i, 0));
            //Số người học
            cell = row.createCell(1, CellType.NUMERIC);
            cell.setCellValue((int) tblNguoiHoc.getValueAt(i, 1));
            //Đăng ký sớm nhất
            cell = row.createCell(2, CellType.STRING);
            cell.setCellValue((String) tblNguoiHoc.getValueAt(i, 2));
            //Đăng ký muộn nhất
            cell = row.createCell(3, CellType.STRING);
            cell.setCellValue((String) tblNguoiHoc.getValueAt(i, 3));            
        }
        return workbook;
    }
    public static Workbook printDiemChuyenDeToExcel(javax.swing.JTable tblDCD, ThongKeDAO tkDao) throws SQLException, FileNotFoundException, IOException{
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Thống kê người học");
        List<Object[]> list = tkDao.getDiemChuyenDe();
        int rownum = 0;
        Cell cell;
        Row row;
        HSSFCellStyle style = createStyleForTitle(workbook);
        row = sheet.createRow(rownum);
        //Chuyên đề
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("Chuyên đề");
        cell.setCellStyle(style);
        //Số lượng học viên
        cell = row.createCell(1, CellType.NUMERIC);
        cell.setCellValue("Số lượng học viên");
        cell.setCellStyle(style);
        //Điểm thấp nhất
        cell = row.createCell(2, CellType.NUMERIC);
        cell.setCellValue("Điểm thấp nhất");
        cell.setCellStyle(style);
        //Điểm cao nhất
        cell = row.createCell(3, CellType.NUMERIC);
        cell.setCellValue("Điểm cao nhất");
        cell.setCellStyle(style);
        //Điểm trung bình
        cell = row.createCell(4, CellType.NUMERIC);
        cell.setCellValue("Điểm trung bình");
        cell.setCellStyle(style);        
        for(int i = 0; i<list.size(); i++){
            rownum++;
            row = sheet.createRow(rownum);
            //Chuyên đề
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue((String) tblDCD.getValueAt(i, 0));
            //Số lượng học viên
            cell = row.createCell(1, CellType.NUMERIC);
            cell.setCellValue((int) tblDCD.getValueAt(i, 1));
            //Điểm thấp nhất
            cell = row.createCell(2, CellType.NUMERIC);
            cell.setCellValue((double) tblDCD.getValueAt(i, 2));
            //Điểm cao nhất
            cell = row.createCell(3, CellType.NUMERIC);
            cell.setCellValue((double) tblDCD.getValueAt(i, 3));
            //Điểm trung bình
            cell = row.createCell(4, CellType.NUMERIC);
            cell.setCellValue((double) tblDCD.getValueAt(i, 4));             
        }
        return workbook;
    }
    public static Workbook printDoanhThuKhoaHocToExcel(javax.swing.JTable tblDT, javax.swing.JComboBox<String> cboNam, ThongKeDAO tkDAO) throws SQLException, FileNotFoundException, IOException{
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Thống kê doanh thu");       
        List<Object[]> list = tkDAO.getDoanhThu((Integer) cboNam.getSelectedItem());
        int rownum = 0;
        Cell cell;
        Row row;
        HSSFCellStyle style = createStyleForTitle(workbook);
        row = sheet.createRow(rownum);
        //Chuyên đề
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("Mã người học");
        cell.setCellStyle(style);
        //Số khoá học
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("Số khoá học");
        cell.setCellStyle(style);
        //Số học viên
        cell = row.createCell(2, CellType.STRING);
        cell.setCellValue("Số học viên");
        cell.setCellStyle(style);
        //Doanh thu
        cell = row.createCell(3, CellType.STRING);
        cell.setCellValue("Doanh thu");
        cell.setCellStyle(style);
        //Học phí thấp nhất
        cell = row.createCell(4, CellType.STRING);
        cell.setCellValue("Học phí thấp nhất");
        cell.setCellStyle(style);
        //Học phí cao nhất
        cell = row.createCell(5, CellType.STRING);
        cell.setCellValue("Học phí cao nhất");
        cell.setCellStyle(style);
        //Học phí trung bình
        cell = row.createCell(6, CellType.STRING);
        cell.setCellValue("Học phí trung bình");
        cell.setCellStyle(style);        
        for(int i = 0; i<list.size(); i++){
            rownum++;
            row = sheet.createRow(rownum);
            //Chuyên đề
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue((String) tblDT.getValueAt(i, 0));
            //Số khoá học
            cell = row.createCell(1, CellType.NUMERIC);
            cell.setCellValue((int) tblDT.getValueAt(i, 1));
            //Số học viên
            cell = row.createCell(2, CellType.NUMERIC);
            cell.setCellValue((int) tblDT.getValueAt(i, 2));
            //Doanh thu
            cell = row.createCell(3, CellType.NUMERIC);
            cell.setCellValue((double) tblDT.getValueAt(i, 3));            
            //Học phí thấp nhất
            cell = row.createCell(4, CellType.NUMERIC);
            cell.setCellValue((double) tblDT.getValueAt(i, 4));
            //Học phí cao nhất
            cell = row.createCell(5, CellType.NUMERIC);
            cell.setCellValue((double) tblDT.getValueAt(i, 5));
            //Học phí trung bình
            cell = row.createCell(6, CellType.NUMERIC);
            cell.setCellValue((double) tblDT.getValueAt(i, 6));           
        }
        return workbook;
    }    
    public static HSSFCellStyle createStyleForTitle(HSSFWorkbook workbook){
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        return style;
    }
}
