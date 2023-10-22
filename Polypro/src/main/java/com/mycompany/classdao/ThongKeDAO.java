/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.classdao;

import com.mycompany.extendent.JdbcHelper;
import com.mycompany.extendent.XDate;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class ThongKeDAO {
    public List<Object[]> getBangDiem(Integer maKH) throws SQLException{
        String sql = "{CALL TK_bangdiem(?)}";
        ResultSet rs = JdbcHelper.execQuery(sql, maKH);
        List<Object[]> list = new ArrayList<>();
        while (rs.next()) {            
            double Diem = rs.getDouble(3);
            String xepLoai = "";
            if(Diem<3){
                xepLoai = "Kém";
            }else if(Diem<5){
                xepLoai = "Yếu";
            }else if(Diem<6.5){
                xepLoai="Trung bình";
            }else if(Diem<7.5){
                xepLoai="Khá";
            }else if(Diem<9){
                xepLoai="Giỏi";
            }else if(Diem<0){
                xepLoai="Chưa nhập";
            }else if(Diem<=10){
                xepLoai="Xuất sắc";
            }
            list.add(new Object[]{rs.getString(1), rs.getString(2), Diem, xepLoai});
        }
        rs.getStatement().getConnection().close();
        return list;
    }
    public List<Object[]> getDiemChuyenDe() throws SQLException{
        List<Object[]> list = new ArrayList<>();
        String sql = "{CALL TK_diemchuyende}";
        ResultSet rs = JdbcHelper.execQuery(sql);
        while (rs.next()) {
            DecimalFormat format = new DecimalFormat("#.##");
            list.add(new Object[]{rs.getString(1), rs.getInt(2), rs.getDouble(3), rs.getDouble(4), Double.parseDouble(format.format(rs.getDouble(5)))});
        }
        rs.getStatement().getConnection().close();
        return list;
    }
    public List<Object[]> getNguoiHoc() throws SQLException{
        List<Object[]> list = new ArrayList<>();
        String sql = "{CALL TK_nguoihoc}";
        ResultSet rs = JdbcHelper.execQuery(sql);
        while (rs.next()) {            
           list.add(new Object[]{rs.getInt(1), rs.getInt(2), XDate.toString(rs.getDate(3), "dd-MM-yyyy"), XDate.toString(rs.getDate(4), "dd-MM-yyyy")});
        }
        rs.getStatement().getConnection().close();
        return list;
    }
    public List<Object[]> getDoanhThu(Integer nam) throws SQLException{
        List<Object[]> list = new ArrayList<>();
        String sql = "{CALL TK_doanhthu(?)}";
        ResultSet rs = JdbcHelper.execQuery(sql, nam);
        while (rs.next()) {
            DecimalFormat format = new DecimalFormat("#.##");
            list.add(new Object[]{rs.getString(1), rs.getInt(2), rs.getInt(3), rs.getDouble(4), rs.getDouble(5), rs.getDouble(6), Double.parseDouble(format.format(rs.getDouble(7)))});
        }
        rs.getStatement().getConnection().close();
        return list;
    }
        public List<Object> getNam(){
        String sql = "SELECT DISTINCT YEAR(NgayKG) FROM KhoaHoc";
            List<Object> list = new ArrayList<>();
        try {
            ResultSet rs = JdbcHelper.execQuery(sql);
            while(rs.next()){
                list.add(rs.getInt(1));
            }
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return list;
    }
}
