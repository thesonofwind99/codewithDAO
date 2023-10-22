/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.classdao;

import com.mycompany.entity.NguoiHoc;
import com.mycompany.extendent.JdbcHelper;
import com.mycompany.extendent.MsgBox;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class NguoiHocDAO extends EduSysDAO<NguoiHoc, String>{
    private String INSERT_SQL = "INSERT INTO NguoiHoc VALUES (?,?,?,?,?,?,?,?,?)";
    private String UPDATE_SQL = "UPDATE NguoiHoc SET HoTen = ?, GioiTinh = ?, NgaySinh = ?, DienThoai = ?, Email = ?, GhiChu = ?, MaNV = ?, NgayDK = ? WHERE MaNH = ?";
    private String DELETE_SQL = "DELETE FROM NguoiHoc WHERE MaNH = ?";
    private String SELECT_ALL_SQL = "SELECT * FROM NguoiHoc";
    private String SELECT_BY_ID_SQL = "SELECT * FROM NguoiHoc WHERE MaNH = ?";
    @Override
    public void insert(NguoiHoc entity) {
        JdbcHelper.execUpdate(INSERT_SQL, entity.getMaNH(), entity.getHoTen(), entity.isGioiTinh(),
                entity.getNgaySinh(), entity.getDienThoai(), entity.getEmail(), entity.getGhiChu(), entity.getMaNV(), entity.getNgayDK());
    }

    @Override
    public void update(NguoiHoc entity) {
        JdbcHelper.execUpdate(UPDATE_SQL, entity.getHoTen(), entity.isGioiTinh(),
                entity.getNgaySinh(), entity.getDienThoai(), entity.getEmail(), entity.getGhiChu(), entity.getMaNV(), entity.getNgayDK(), entity.getMaNH());
    }

    @Override
    public void delete(String id) {
        JdbcHelper.execUpdate(DELETE_SQL, id);
    }

    @Override
    public NguoiHoc selectById(String id) {
        List<NguoiHoc> list = this.selectBySql(SELECT_BY_ID_SQL, id);
        return list.size()>0?list.get(0):null;
    }

    @Override
    public List<NguoiHoc> selectAll() {
        return this.selectBySql(SELECT_ALL_SQL);
    }

    @Override
    protected List<NguoiHoc> selectBySql(String sql, Object... args) {
        List<NguoiHoc> list = new ArrayList<>();
        try {
            ResultSet rs = JdbcHelper.execQuery(sql, args);
            while (rs.next()) {                
                NguoiHoc nh = new NguoiHoc();
                nh.setMaNH(rs.getString(1));
                nh.setHoTen(rs.getString(2));
                nh.setGioiTinh(rs.getBoolean(3));
                nh.setNgaySinh(rs.getDate(4));
                nh.setDienThoai(rs.getString(5));
                nh.setEmail(rs.getString(6));
                nh.setGhiChu(rs.getString(7));
                nh.setMaNV(rs.getString(8));
                nh.setNgayDK(rs.getDate(9));
                list.add(nh);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception ex) {
            ex.getMessage();
        }
        return list;
    }
    
    public List<NguoiHoc> searchByKeyWord(String keyword){
        String sql = "SELECT * FROM NguoiHoc WHERE HoTen LIKE ?";
        String arg = "%"+keyword+"%";
        return selectBySql(sql, arg);
    }
    
    public List<NguoiHoc> selectNotInCourse(int maKH, String keyword){
        String sqlTen = "SELECT * FROM NguoiHoc WHERE HoTen LIKE ? AND MaNH NOT IN(SELECT MaNH FROM HocVien WHERE MaKH = ?)";
        String sqlNotTen = "SELECT * FROM NguoiHoc WHERE MaNH NOT IN(SELECT MaNH FROM HocVien WHERE MaKH = ?)";
        if(!keyword.equalsIgnoreCase("")){
            String arg = "%"+keyword+"%";
            return selectBySql(sqlTen, arg, maKH);
        }else{
            return selectBySql(sqlNotTen, maKH);
        }
    }
}
