/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.classdao;

import com.mycompany.entity.NhanVien;
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
public class NhanVienDAO extends EduSysDAO<NhanVien, String>{
    private String INSERT_SQL = "INSERT INTO NhanVien VALUES (?,?,?,?)";
    private String UPDATE_SQL = "UPDATE NhanVien SET MatKhau = ?, HoTen = ?, VaiTro = ? WHERE MaNV = ?";
    private String DELETE_SQL = "DELETE FROM NhanVien WHERE MaNV = ?";
    private String SELECT_ALL_SQL = "SELECT * FROM NhanVien";
    private String SELECT_BY_ID_SQL = "SELECT * FROM NhanVien WHERE MaNV = ?";
    @Override
    public void insert(NhanVien entity) {
        JdbcHelper.execUpdate(INSERT_SQL, entity.getMaNV(), entity.getMatKhau(),entity.getHoTen(), entity.isVaiTro());
    }

    @Override
    public void update(NhanVien entity) {
        JdbcHelper.execUpdate(UPDATE_SQL, entity.getMatKhau(), entity.getHoTen(), entity.isVaiTro(), entity.getMaNV());
    }

    @Override
    public void delete(String id) {
        JdbcHelper.execUpdate(DELETE_SQL, id);
    }

    @Override
    public NhanVien selectById(String id) {
        List<NhanVien> list = this.selectBySql(SELECT_BY_ID_SQL, id);
        return list.size()>0 ? list.get(0): null;
    }

    @Override
    public List<NhanVien> selectAll() {
        return this.selectBySql(SELECT_ALL_SQL);
    }

    @Override
    protected List<NhanVien> selectBySql(String sql, Object... args) {
        List<NhanVien> list = new ArrayList<>();
        try {
            ResultSet rs = JdbcHelper.execQuery(sql, args);
            while (rs.next()) {                
                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getString(1));
                nv.setMatKhau(rs.getString(2));
                nv.setHoTen(rs.getString(3));
                nv.setVaiTro(rs.getBoolean(4));
                list.add(nv);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception ex) {
            ex.getMessage();
        }
        return list;
    }
    
}
