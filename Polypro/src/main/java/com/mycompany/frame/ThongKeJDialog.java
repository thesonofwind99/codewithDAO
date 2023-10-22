/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.mycompany.frame;

import com.mycompany.classdao.KhoaHocDAO;
import com.mycompany.classdao.ThongKeDAO;
import com.mycompany.entity.KhoaHoc;
import com.mycompany.extendent.Auth;
import com.mycompany.extendent.ExcelUntil;
import com.mycompany.extendent.MsgBox;
import static com.mycompany.frame.MainJFrame.logger;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author Administrator
 */
public class ThongKeJDialog extends javax.swing.JDialog {
    private DefaultTableModel tblModelBD = new DefaultTableModel();
    private DefaultTableModel tblModelNH = new DefaultTableModel();
    private DefaultTableModel tblModelDCD = new DefaultTableModel();
    private DefaultTableModel tblModelDT = new DefaultTableModel();
    private KhoaHocDAO khDao = new KhoaHocDAO();
    private ThongKeDAO tkDao = new ThongKeDAO();
    /**
     * Creates new form ThongKeJDialog
     */
    public ThongKeJDialog(java.awt.Frame parent, boolean modal, int i) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        initTable();
        tabTK.setSelectedIndex(i);
        initComboboxKH();
        initComboboxDT();
        fillTableNH();
        fillTableDCD();
        if(!Auth.isManager()){
            tabTK.remove(pnlDT);
        }
    }
    public void initTable(){
        tblModelBD = (DefaultTableModel) tblBD.getModel();
        String columnbd[] = new String[]{
            "Mã người học", "Họ và tên", "Điểm", "Xếp loại"
        };
        tblModelBD.setColumnIdentifiers(columnbd);
        tblModelNH = (DefaultTableModel) tblNH.getModel();
        String columnnh[] = new String[]{
            "Năm", "Số người học", "Đăng ký sớm nhất", "Đăng ký muộn nhất"
        };
        tblModelNH.setColumnIdentifiers(columnnh);
        tblModelDCD = (DefaultTableModel) tblDCD.getModel();
        String columndcd[] = new String[]{
            "Chuyên đề", "Số lượng học viên", "Điểm thấp nhất", "Điểm cao nhất", "Điểm trung bình"
        };
        tblModelDCD.setColumnIdentifiers(columndcd);
        tblModelDT = (DefaultTableModel) tblDT.getModel();
        String columndt[] = new String[]{
            "Chuyên đề", "Số khoá học", "Số học viên", "Doanh thu", "Học phí thấp nhất", "Học phí cao nhất", "Học phí trung bình"
        };
        tblModelDT.setColumnIdentifiers(columndt);          
    }
    public void initComboboxKH(){
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboKhoaHoc.getModel();
        model.removeAllElements();
        List<KhoaHoc> list = khDao.selectAll();
        for (KhoaHoc kh : list) {
            model.addElement(kh);
        }
        fillTableBD();
    }
    public void fillTableBD(){
        KhoaHoc kh = (KhoaHoc) cboKhoaHoc.getSelectedItem();
        tblModelBD.setRowCount(0);
        try {
            List<Object[]> list = tkDao.getBangDiem(kh.getMaKH());
            for (Object[] ob : list) {
                tblModelBD.addRow(ob);
            }
        tblModelBD.fireTableDataChanged();    
        } catch (SQLException ex) {
            Logger.getLogger(ThongKeJDialog.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }
    public void fillTableNH(){
        tblModelNH.setRowCount(0);
        try {
            List<Object[]> list = tkDao.getNguoiHoc();
            for (Object[] ob : list) {
                tblModelNH.addRow(ob);
            }
            tblModelNH.fireTableDataChanged();
        } catch (SQLException ex) {
            Logger.getLogger(ThongKeJDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void fillTableDCD(){
        tblModelDCD.setRowCount(0);
        try {
            List<Object[]> list = tkDao.getDiemChuyenDe();
            for (Object[] ob : list) {
                tblModelDCD.addRow(ob);
            }
            tblModelDCD.fireTableDataChanged();
        } catch (SQLException ex) {
            Logger.getLogger(ThongKeJDialog.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    public void initComboboxDT(){
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboNam.getModel();
        model.removeAllElements();
        List<Object> list = tkDao.getNam();
        for (Object ob : list) {
            model.addElement(ob);
        }
        fillTableDT();
    }
    public void fillTableDT(){
        tblModelDT.setRowCount(0);
        int nam = (int) cboNam.getSelectedItem();
        try {
            List<Object[]> list = tkDao.getDoanhThu(nam);
            for (Object[] ob : list) {
                tblModelDT.addRow(ob);
            }
            tblModelDT.fireTableDataChanged();
        } catch (SQLException ex) {
            Logger.getLogger(ThongKeJDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void chooseDirectoryToSave(Workbook workbook){
        JFileChooser choose = new JFileChooser();
        int x = choose.showSaveDialog(null);
        if(x == JFileChooser.APPROVE_OPTION){
            try {
                String file = choose.getSelectedFile().getAbsolutePath().toString();
                FileOutputStream outFile = new FileOutputStream(file);
                workbook.write(outFile);
                workbook.close();
                outFile.close();
                MsgBox.alert(this, "Xuất tệp Excel thành công!");
            } catch (IOException ex) {
                Logger.getLogger(ThongKeJDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tabTK = new javax.swing.JTabbedPane();
        pnlBD = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblBD = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        cboKhoaHoc = new javax.swing.JComboBox<>();
        btnExportBangDiem = new javax.swing.JButton();
        pnlNH = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblNH = new javax.swing.JTable();
        btnExportNguoiHoc = new javax.swing.JButton();
        pnlDCD = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblDCD = new javax.swing.JTable();
        btnExportDiemCD = new javax.swing.JButton();
        pnlDT = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblDT = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        cboNam = new javax.swing.JComboBox<>();
        btnExportDoanhThu = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        jLabel1.setText("TỔNG HỢP THỐNG KÊ");

        tblBD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tblBD);

        jLabel2.setText("KHOÁ HỌC:");

        cboKhoaHoc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboKhoaHoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboKhoaHocActionPerformed(evt);
            }
        });

        btnExportBangDiem.setText("Xuất bảng điểm ra tệp Excel (.xls)");
        btnExportBangDiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportBangDiemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlBDLayout = new javax.swing.GroupLayout(pnlBD);
        pnlBD.setLayout(pnlBDLayout);
        pnlBDLayout.setHorizontalGroup(
            pnlBDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 543, Short.MAX_VALUE)
            .addGroup(pnlBDLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboKhoaHoc, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(pnlBDLayout.createSequentialGroup()
                .addGap(166, 166, 166)
                .addComponent(btnExportBangDiem)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlBDLayout.setVerticalGroup(
            pnlBDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBDLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlBDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cboKhoaHoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnExportBangDiem))
        );

        tabTK.addTab("BẢNG ĐIỂM", pnlBD);

        tblNH.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblNH);

        btnExportNguoiHoc.setText("Xuất người học ra tệp Excel (.xls)");
        btnExportNguoiHoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportNguoiHocActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlNHLayout = new javax.swing.GroupLayout(pnlNH);
        pnlNH.setLayout(pnlNHLayout);
        pnlNHLayout.setHorizontalGroup(
            pnlNHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 543, Short.MAX_VALUE)
            .addGroup(pnlNHLayout.createSequentialGroup()
                .addGap(167, 167, 167)
                .addComponent(btnExportNguoiHoc)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlNHLayout.setVerticalGroup(
            pnlNHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNHLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 448, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(btnExportNguoiHoc))
        );

        tabTK.addTab("NGƯỜI HỌC", pnlNH);

        tblDCD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(tblDCD);

        btnExportDiemCD.setText("Xuất người học ra tệp Excel (.xls)");
        btnExportDiemCD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportDiemCDActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlDCDLayout = new javax.swing.GroupLayout(pnlDCD);
        pnlDCD.setLayout(pnlDCDLayout);
        pnlDCDLayout.setHorizontalGroup(
            pnlDCDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 543, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDCDLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnExportDiemCD)
                .addGap(172, 172, 172))
        );
        pnlDCDLayout.setVerticalGroup(
            pnlDCDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDCDLayout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnExportDiemCD))
        );

        tabTK.addTab("ĐIỂM CHUYÊN ĐỀ", pnlDCD);

        tblDT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(tblDT);

        jLabel3.setText("NĂM:");

        cboNam.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboNam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboNamActionPerformed(evt);
            }
        });

        btnExportDoanhThu.setText("Xuất doanh thu ra tệp Excel (.xls)");
        btnExportDoanhThu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportDoanhThuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlDTLayout = new javax.swing.GroupLayout(pnlDT);
        pnlDT.setLayout(pnlDTLayout);
        pnlDTLayout.setHorizontalGroup(
            pnlDTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 543, Short.MAX_VALUE)
            .addGroup(pnlDTLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboNam, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(pnlDTLayout.createSequentialGroup()
                .addGap(160, 160, 160)
                .addComponent(btnExportDoanhThu)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlDTLayout.setVerticalGroup(
            pnlDTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDTLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cboNam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnExportDoanhThu))
        );

        tabTK.addTab("DOANH THU", pnlDT);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tabTK, javax.swing.GroupLayout.PREFERRED_SIZE, 543, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabTK, javax.swing.GroupLayout.PREFERRED_SIZE, 509, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cboKhoaHocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboKhoaHocActionPerformed
        int i = cboKhoaHoc.getSelectedIndex();
        if(i>-1){
            fillTableBD();
        }    
    }//GEN-LAST:event_cboKhoaHocActionPerformed

    private void cboNamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboNamActionPerformed
        int i = cboNam.getSelectedIndex();
        if(i>-1){
            fillTableDT();
        }
    }//GEN-LAST:event_cboNamActionPerformed

    private void btnExportBangDiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportBangDiemActionPerformed
        try {
            Workbook workbook = ExcelUntil.printBangDiemKhoaHocToExcel(tblBD, cboKhoaHoc, tkDao);
            this.chooseDirectoryToSave(workbook);
            logger.info("Export file BangDiem successful!");
        } catch (SQLException ex) {
            Logger.getLogger(ThongKeJDialog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ThongKeJDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnExportBangDiemActionPerformed

    private void btnExportNguoiHocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportNguoiHocActionPerformed
        try {
            Workbook workbook = ExcelUntil.printNguoiHocToExcel(tblNH, tkDao);
            this.chooseDirectoryToSave(workbook);
            logger.info("Export file NguoiHoc successful!");
        } catch (SQLException ex) {
            Logger.getLogger(ThongKeJDialog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ThongKeJDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnExportNguoiHocActionPerformed

    private void btnExportDiemCDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportDiemCDActionPerformed
        try {
            Workbook workbook = ExcelUntil.printDiemChuyenDeToExcel(tblDCD, tkDao);
            this.chooseDirectoryToSave(workbook);
            logger.info("Export file DiemChuyenDe successful!");
        } catch (SQLException ex) {
            Logger.getLogger(ThongKeJDialog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ThongKeJDialog.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }//GEN-LAST:event_btnExportDiemCDActionPerformed

    private void btnExportDoanhThuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportDoanhThuActionPerformed
        try {
            Workbook workbook = ExcelUntil.printDoanhThuKhoaHocToExcel(tblDT, cboNam, tkDao);
            this.chooseDirectoryToSave(workbook);
            logger.info("Export file DoanhThu successful!");
        } catch (SQLException ex) {
            Logger.getLogger(ThongKeJDialog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ThongKeJDialog.class.getName()).log(Level.SEVERE, null, ex);
        }     
    }//GEN-LAST:event_btnExportDoanhThuActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ThongKeJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ThongKeJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ThongKeJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ThongKeJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ThongKeJDialog dialog = new ThongKeJDialog(new javax.swing.JFrame(), true, 0);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExportBangDiem;
    private javax.swing.JButton btnExportDiemCD;
    private javax.swing.JButton btnExportDoanhThu;
    private javax.swing.JButton btnExportNguoiHoc;
    private javax.swing.JComboBox<String> cboKhoaHoc;
    private javax.swing.JComboBox<String> cboNam;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JPanel pnlBD;
    private javax.swing.JPanel pnlDCD;
    private javax.swing.JPanel pnlDT;
    private javax.swing.JPanel pnlNH;
    private javax.swing.JTabbedPane tabTK;
    private javax.swing.JTable tblBD;
    private javax.swing.JTable tblDCD;
    private javax.swing.JTable tblDT;
    private javax.swing.JTable tblNH;
    // End of variables declaration//GEN-END:variables
}
