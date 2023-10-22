/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.classdao;

import com.mycompany.entity.HocVien;
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
public class HocVienDAO extends EduSysDAO<HocVien, Integer>{
    private String INSERT_SQL = "INSERT INTO HocVien VALUES (?,?,?)";
    private String UPDATE_SQL = "UPDATE HocVien SET MaKH = ?, MaNH = ?, Diem = ? WHERE MaHV = ?";
    private String DELETE_SQL = "DELETE FROM HocVien WHERE MaHV = ?";
    private String SELECT_ALL_SQL = "SELECT * FROM HocVien";
    private String SELECT_BY_ID_SQL = "SELECT * FROM HocVien WHERE MaHV = ?";
    @Override
    public void insert(HocVien entity) {
        JdbcHelper.execUpdate(INSERT_SQL,entity.getMaKH(),entity.getMaNH(), entity.getDiem());
    }

    @Override
    public void update(HocVien entity) {
        JdbcHelper.execUpdate(UPDATE_SQL, entity.getMaKH(),entity.getMaNH()
                , entity.getDiem(), entity.getMaHV());
    }
    
        @Override
    public void delete(Integer id) {
        JdbcHelper.execUpdate(DELETE_SQL, id);
    }

    @Override
    public HocVien selectById(Integer id) {
        List<HocVien> list = this.selectBySql(SELECT_BY_ID_SQL, id);
        return list.size()>0 ? list.get(0): null;
    }

    @Override
    public List<HocVien> selectAll() {
        return this.selectBySql(SELECT_ALL_SQL);
    }

    @Override
    protected List<HocVien> selectBySql(String sql, Object... args) {
        List<HocVien> list = new ArrayList<>();
        try {
            ResultSet rs = JdbcHelper.execQuery(sql, args);
            while (rs.next()) {                
                HocVien hv = new HocVien();
                hv.setMaHV(rs.getInt(1));
                hv.setMaKH(rs.getInt(2));
                hv.setMaNH(rs.getString(3));
                hv.setDiem(rs.getDouble(4));
                list.add(hv);;
            }
            rs.getStatement().getConnection().close();
        } catch (Exception ex) {
            ex.getMessage();
        }
        return list;
    }
    
    public List<HocVien> selectByKhoaHoc(int maKH){
        String sql = "SELECT * FROM HocVien WHERE MaKH = ?";
        return selectBySql(sql, maKH);
    }

}
