/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.extendent;
import java.awt.Image;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.swing.ImageIcon;
/**
 *
 * @author Administrator
 */
public class XImage {
    public static Image getAppIcon() {
        URL url = XImage.class.getResource("/icon/FPTLogo.png");
        return new ImageIcon(url).getImage();
    }
    public static boolean save(File src) {
        File dst = new File("src\\main\\resources\\icon", src.getName());
        if (!dst.getParentFile().exists()) {
            dst.getParentFile().mkdirs();// tạo thư mục
        }

        try {
            Path from = Paths.get(src.getAbsolutePath());
            Path to = Paths.get(dst.getAbsolutePath());
            Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            return false;
        }
    }
     public static ImageIcon read(String fileName) {
        File path = new File("src\\main\\resources\\icon", fileName);
        Image image = new ImageIcon(path.getAbsolutePath()).getImage();
        Image icon = image.getScaledInstance(140, 169, Image.SCALE_SMOOTH);
        return new ImageIcon(icon);
    }
     public static File saveExcel(File src){
         File dst = new File("src\\main\\resources\\icon", src.getName());
        if (!dst.getParentFile().exists()) {
            dst.getParentFile().mkdirs();// tạo thư mục
        }

        try {
            Path from = Paths.get(src.getAbsolutePath());
            Path to = Paths.get(dst.getAbsolutePath());
            Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
            return dst;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
     }
}
