/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.extendent;

import java.awt.Component;
import java.awt.Image;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;

/**
 *
 * @author Administrator
 */
public class SetIcon {
    public static void icon(Component com, String file, int width, int height){
        try {
            Image image = ImageIO.read(new File(file));
            Image resizedImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            if(com instanceof JLabel){
                JLabel lable = (JLabel) com;
                lable.setIcon(new ImageIcon(resizedImage));
            }else if(com instanceof JButton){
                JButton button = (JButton) com;
                button.setIcon(new ImageIcon(resizedImage));
            }else if(com instanceof JMenuItem){
                JMenuItem menuItem = (JMenuItem) com;
                menuItem.setIcon(new ImageIcon(resizedImage));
            }
        } catch (Exception e) {
        }
    }
}
