/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.extendent;

import com.mycompany.entity.NhanVien;

/**
 *
 * @author Administrator
 */
public class Auth {
    public static NhanVien user = null;
    public static NhanVien clear(){
        return Auth.user = null;
    }
    public static boolean isLogin(){
        return Auth.user != null;
    }
    public static boolean isManager(){
        return Auth.isLogin()&&Auth.user.isVaiTro();
    }
}
