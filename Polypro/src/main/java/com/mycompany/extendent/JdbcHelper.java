/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.extendent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class JdbcHelper {
   static String databaseName = "Polypro";
   static String user = "sa";
   static String password = "nhan0944340821";
   static String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
   static String dburl = "jdbc:sqlserver://localhost:1433;databaseName="
                            +databaseName+";user="
                            +user+";password="
                            +password
                            +";encrypt=true;trustServerCertificate=true";
   static{
       try {
           Class.forName(driver);//nạp driver
       } catch (Exception e) {
           e.getMessage();
       }   
   }
//Phương thức này dùng để xác định dòng sql nhận được là lệnh gọi thủ tục hay là truy vấn khác, sau đó add những giá trị
//của từng cột vào từng tham số, sau đó nó sẽ được dùng trong các phương thức ở dưới   
   private static PreparedStatement getStmt(String sql, Object...args) throws SQLException{
       Connection cons = DriverManager.getConnection(dburl);
       PreparedStatement stm;
       if(sql.startsWith("{")){
           stm = cons.prepareCall(sql);
       }else{
           stm = cons.prepareStatement(sql);
       }
       for(int i = 0; i<args.length; i++){
           stm.setObject(i+1, args[i]);
       }
       return stm;
   }
//Phương thức dùng để thực hiện cái câu truy vấn INSERT, UPDATE, DELETE   
   public static int execUpdate(String sql, Object...args){
       int numRowUpdate = 0;
       try {
           PreparedStatement stm = getStmt(sql, args);
           try {
               numRowUpdate = stm.executeUpdate();
//               return stm.executeUpdate();
           } finally {
               stm.getConnection().close();
           }
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
       return numRowUpdate;
   }
//Phương thức dùng để trả về kết quả (dữ liệu của từng bảng, từng thủ tục lưu hay là các câu truy vấn khác)   
    public static ResultSet execQuery(String sql, Object...args) throws SQLException {
        PreparedStatement stm = getStmt(sql, args);
        return stm.executeQuery();
    }
//Tạo phương thức dùng chỉ để lấy giá trị cho 1 cột thông qua khoá chính     
   public static Object execValue(String sql, Object...args) throws SQLException{
       ResultSet rs = execQuery(sql, args);
       if(rs.next()){
         return rs.getObject(0);
       }
       rs.getStatement().getConnection().close();
       return null;
   } 
}    
    
