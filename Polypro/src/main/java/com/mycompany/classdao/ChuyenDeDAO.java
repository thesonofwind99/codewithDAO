/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.classdao;

import com.mycompany.entity.ChuyenDe;
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
public class ChuyenDeDAO extends EduSysDAO<ChuyenDe, String>{
    private String INSERT_SQL = "INSERT INTO ChuyenDe VALUES (?,?,?,?,?,?)";
    private String UPDATE_SQL = "UPDATE ChuyenDe SET TenCD = ?, HocPhi = ?, ThoiLuong = ?, Hinh = ?, MoTa = ? WHERE MaCD = ?";
    private String DELETE_SQL = "DELETE FROM ChuyenDe WHERE MaCD = ?";
    private String SELECT_ALL_SQL = "SELECT * FROM ChuyenDe";
    private String SELECT_BY_ID_SQL = "SELECT * FROM ChuyenDe WHERE MaCD = ?";
    @Override
    public void insert(ChuyenDe entity) {
        JdbcHelper.execUpdate(INSERT_SQL, entity.getMaCD(), entity.getTenCD(), entity.getHocPhi(),
                entity.getThoiLuong(), entity.getHinh(), entity.getMoTa());
    }

    @Override
    public void update(ChuyenDe entity) {
        JdbcHelper.execUpdate(UPDATE_SQL, entity.getTenCD(), entity.getHocPhi(), entity.getThoiLuong(),
                entity.getHinh(), entity.getMoTa(), entity.getMaCD());
    }

    @Override
    public void delete(String id) {
        JdbcHelper.execUpdate(DELETE_SQL, id);
    }

    @Override
    public ChuyenDe selectById(String id) {
        List<ChuyenDe> list = this.selectBySql(SELECT_BY_ID_SQL, id);
        return list.size()>0?list.get(0):null;
    }

    @Override
    public List<ChuyenDe> selectAll() {
        return this.selectBySql(SELECT_ALL_SQL);
    }

    @Override
    protected List<ChuyenDe> selectBySql(String sql, Object... args) {
        List<ChuyenDe> list = new ArrayList<>();
        try {
            ResultSet rs = JdbcHelper.execQuery(sql, args);
            while (rs.next()) {                
                ChuyenDe cd = new ChuyenDe();
                cd.setMaCD(rs.getString(1));
                cd.setTenCD(rs.getString(2));
                cd.setHocPhi(rs.getDouble(3));
                cd.setThoiLuong(rs.getInt(4));
                cd.setHinh(rs.getString(5));
                cd.setMoTa(rs.getString(6));
                list.add(cd);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception ex) {
            ex.getMessage();
        }
        return list;
    }
    
}
