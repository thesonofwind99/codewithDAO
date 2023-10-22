/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.classdao;

import com.mycompany.entity.KhoaHoc;
import com.mycompany.extendent.JdbcHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class KhoaHocDAO extends EduSysDAO<KhoaHoc, Integer>{
    private String INSERT_SQL = "INSERT INTO KhoaHoc VALUES (?,?,?,?,?,?,?)";
    private String UPDATE_SQL = "UPDATE KhoaHoc SET MaCD = ?, HocPhi = ?, ThoiLuong = ?, NgayKG = ? "
            + ", GhiChu = ?, MaNV = ?, NgayTao = ? WHERE MaKH = ?";
    private String DELETE_SQL = "DELETE FROM KhoaHoc WHERE MaKH = ?";
    private String SELECT_ALL_SQL = "SELECT * FROM KhoaHoc";
    private String SELECT_BY_ID_SQL = "SELECT * FROM KhoaHoc WHERE MaKH = ?";
    @Override
    public void insert(KhoaHoc entity) {
        JdbcHelper.execUpdate(INSERT_SQL, entity.getMaCD(),entity.getHocPhi()
                , entity.getThoiLuong(), entity.getNgayKG(), entity.getGhiChu(), entity.getMaNV(), entity.getNgayTao());
    }

    @Override
    public void update(KhoaHoc entity) {
        JdbcHelper.execUpdate(UPDATE_SQL, entity.getMaCD(),entity.getHocPhi()
                , entity.getThoiLuong(), entity.getNgayKG(), entity.getGhiChu(), 
                entity.getMaNV(), entity.getNgayTao(), entity.getMaKH());
    }
    
    @Override
    public void delete(Integer id) {
        JdbcHelper.execUpdate(DELETE_SQL, id);
    }

    @Override
    public KhoaHoc selectById(Integer id) {
        List<KhoaHoc> list = this.selectBySql(SELECT_BY_ID_SQL, id);
        return list.size()>0?list.get(0):null;
    }

    @Override
    public List<KhoaHoc> selectAll() {
        return this.selectBySql(SELECT_ALL_SQL);
    }

    @Override
    protected List<KhoaHoc> selectBySql(String sql, Object... args) {
        List<KhoaHoc> list = new ArrayList<>();
        try {
            ResultSet rs = JdbcHelper.execQuery(sql, args);
            while (rs.next()) {                
                KhoaHoc kh = new KhoaHoc();
                kh.setMaKH(rs.getInt(1));
                kh.setMaCD(rs.getString(2));
                kh.setHocPhi(rs.getDouble(3));
                kh.setThoiLuong(rs.getInt(4));
                kh.setNgayKG(rs.getDate(5));
                kh.setGhiChu(rs.getString(6));
                kh.setMaNV(rs.getString(7));
                kh.setNgayTao(rs.getDate(8));
                list.add(kh);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception ex) {
            ex.getMessage();
        }
        return list;
    }
    public List<KhoaHoc> selectByCD(String maCD){
       String sql = "SELECT * FROM KhoaHoc WHERE MaCD = ?";
       return selectBySql(sql, maCD);
    }

}
