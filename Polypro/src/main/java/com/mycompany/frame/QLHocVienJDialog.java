/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.mycompany.frame;

import com.mycompany.classdao.ChuyenDeDAO;
import com.mycompany.classdao.HocVienDAO;
import com.mycompany.classdao.KhoaHocDAO;
import com.mycompany.classdao.NguoiHocDAO;
import com.mycompany.entity.ChuyenDe;
import com.mycompany.entity.HocVien;
import com.mycompany.entity.KhoaHoc;
import com.mycompany.entity.NguoiHoc;
import com.mycompany.extendent.Auth;
import com.mycompany.extendent.MsgBox;
import com.mycompany.extendent.XDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Administrator
 */
public class QLHocVienJDialog extends javax.swing.JDialog {
    private DefaultTableModel tblModelHV = new DefaultTableModel();
    private DefaultTableModel tblModelNH = new DefaultTableModel();
    private HocVienDAO hvDao = new HocVienDAO();
    private ChuyenDeDAO cdDao = new ChuyenDeDAO();
    private NguoiHocDAO  nhDao = new NguoiHocDAO();
    private KhoaHocDAO khDao = new KhoaHocDAO();
    /**
     * Creates new form QLHocVienJDialog
     */
    public QLHocVienJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        initTable();
        initComboboxCD();
        tblHV.setEnabled(true);
        tblHV.setCellSelectionEnabled(true);
    }
    public void initTable(){
        tblModelHV = (DefaultTableModel) tblHV.getModel();
        String columnhv[] = new String[]{
            "Thứ tự", "Mã học viên", "Mã người học", "Họ tên", "Điểm"
        };
        tblModelHV.setColumnIdentifiers(columnhv);
        tblModelNH = (DefaultTableModel) tblNH.getModel();
        String columnnh[] = new String[]{
            "Số thứ tự", "Mã người học", "Họ và tên", "Giới tính", "Ngày sinh", "Điện thoại", "Email"
        };
        tblModelNH.setColumnIdentifiers(columnnh);        
    }
    public void initComboboxCD(){
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboCD.getModel();
        model.removeAllElements();
        List<ChuyenDe> list = cdDao.selectAll();
        for (ChuyenDe cd : list) {
            model.addElement(cd);
        }
        initComboboxKH();
    }
    public void initComboboxKH(){
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboKH.getModel();
        model.removeAllElements();
        ChuyenDe cd = (ChuyenDe) cboCD.getSelectedItem();
        if(cd!=null){
            List<KhoaHoc> list = khDao.selectByCD(cd.getMaCD());
            for (KhoaHoc kh : list) {
                model.addElement(kh);
            }
            fillTableHocVien();
        }
    }
    public void fillTableHocVien(){
        tblModelHV.setRowCount(0);
        KhoaHoc kh = (KhoaHoc) cboKH.getSelectedItem();
        if(kh!=null){
            List<HocVien> list = hvDao.selectByKhoaHoc(kh.getMaKH());
            for(int i=0; i<list.size(); i++){
                HocVien hv = list.get(i);
                String hoTen = nhDao.selectById(hv.getMaNH()).getHoTen();
                tblModelHV.addRow(new Object[]{
                    i+1, hv.getMaHV(), hv.getMaNH(), hoTen, hv.getDiem()
                });
            }
            fillTableNguoiHoc();
        }
    }
    public void fillTableNguoiHoc(){
        tblModelNH.setRowCount(0);
        KhoaHoc kh = (KhoaHoc) cboKH.getSelectedItem();
        String keyWord = txtFind.getText();
        if(kh!=null){
            List<NguoiHoc> list = nhDao.selectNotInCourse(kh.getMaKH(), keyWord);
            int i = 1;
            for (NguoiHoc nh : list) {
                tblModelNH.addRow(new Object[]{
                    i++, nh.getMaNH(), nh.getHoTen(), nh.isGioiTinh()?"Nam":"Nữ", 
                    XDate.toString(nh.getNgaySinh(), "dd-MM-yyyy"), nh.getDienThoai(), nh.getEmail()
                });
            }
        }
    }
    public void addHV(){
        KhoaHoc kh = (KhoaHoc) cboKH.getSelectedItem();
        for(int row : tblNH.getSelectedRows()){
            HocVien hv = new HocVien();
            hv.setMaKH(kh.getMaKH());
            hv.setMaNH((String) tblNH.getValueAt(row, 1));
            hv.setDiem(0);
            hvDao.insert(hv);
        }
        fillTableHocVien();
        tabHV.setSelectedIndex(0);
    }
    public void removeHV(){
        if(Auth.isManager()){
            if (MsgBox.confirm(this, "Bạn muốn xoá các học viên được chọn ?")) {
                for (int row : tblHV.getSelectedRows()) {
                    int mahv = (int) tblHV.getValueAt(row, 1);
                    hvDao.delete(mahv);
                }
                fillTableHocVien();
            }
        }else{
            MsgBox.alert(this, "Nhân viên không có quyền xoá!");
        }
    }
    public void updateDiem(){
        int n = tblHV.getRowCount();
        for(int i = 0; i < n; i++){
            int maHV = (Integer) tblModelHV.getValueAt(i, 1);
            double diem = 0;
            try {
                diem = Double.parseDouble(tblModelHV.getValueAt(i, 4).toString());
                if(diem<0 || diem>10){
                    MsgBox.alert(this, "Điểm phải lớn hơn 0 hoặc nhỏ hơn 10");
                    return;
                }
            } catch (Exception e) {
                MsgBox.alert(this, "Điểm phải là số thực");
                return;
            }
            HocVien hv = hvDao.selectById(maHV);
            hv.setDiem(diem);
            hvDao.update(hv);
        }
        MsgBox.alert(this, "Cập nhật thành công");
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
        tabHV = new javax.swing.JTabbedPane();
        pnlHocVien = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        btnDelete = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblHV = new javax.swing.JTable();
        pnlNH = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblNH = new javax.swing.JTable();
        pnlFind = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtFind = new javax.swing.JTextField();
        btnFind = new javax.swing.JButton();
        btnThem = new javax.swing.JButton();
        pnlCD = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        cboCD = new javax.swing.JComboBox<>();
        pnlKH = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        cboKH = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        jLabel1.setText("QUẢN LÝ HỌC VIÊN");

        jPanel2.setLayout(new java.awt.GridLayout(1, 4, 8, 0));

        btnDelete.setText("Xoá khỏi khoá học");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        jPanel2.add(btnDelete);

        btnUpdate.setText("Cập nhật điểm");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        jPanel2.add(btnUpdate);

        tblHV.setModel(new javax.swing.table.DefaultTableModel(
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
        tblHV.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHVMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblHV);

        javax.swing.GroupLayout pnlHocVienLayout = new javax.swing.GroupLayout(pnlHocVien);
        pnlHocVien.setLayout(pnlHocVienLayout);
        pnlHocVienLayout.setHorizontalGroup(
            pnlHocVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
            .addGroup(pnlHocVienLayout.createSequentialGroup()
                .addGap(110, 110, 110)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlHocVienLayout.setVerticalGroup(
            pnlHocVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHocVienLayout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        tabHV.addTab("HỌC VIÊN", pnlHocVien);

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

        pnlFind.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel2.setText("Tìm kiếm:");

        btnFind.setText("Tìm");
        btnFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlFindLayout = new javax.swing.GroupLayout(pnlFind);
        pnlFind.setLayout(pnlFindLayout);
        pnlFindLayout.setHorizontalGroup(
            pnlFindLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFindLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtFind, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(btnFind)
                .addContainerGap(21, Short.MAX_VALUE))
        );
        pnlFindLayout.setVerticalGroup(
            pnlFindLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFindLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(pnlFindLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtFind, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFind))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        btnThem.setText("Thêm vào khoá học");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlNHLayout = new javax.swing.GroupLayout(pnlNH);
        pnlNH.setLayout(pnlNHLayout);
        pnlNHLayout.setHorizontalGroup(
            pnlNHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addComponent(pnlFind, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlNHLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnThem)
                .addContainerGap())
        );
        pnlNHLayout.setVerticalGroup(
            pnlNHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlNHLayout.createSequentialGroup()
                .addComponent(pnlFind, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnThem)
                .addGap(13, 13, 13))
        );

        tabHV.addTab("NGƯỜI HỌC", pnlNH);

        pnlCD.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel6.setText("Chuyên đề:");

        cboCD.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboCD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCDActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlCDLayout = new javax.swing.GroupLayout(pnlCD);
        pnlCD.setLayout(pnlCDLayout);
        pnlCDLayout.setHorizontalGroup(
            pnlCDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCDLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboCD, 0, 156, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlCDLayout.setVerticalGroup(
            pnlCDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCDLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(pnlCDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cboCD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        pnlKH.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel11.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel11.setText("Khoá học:");

        cboKH.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboKHActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlKHLayout = new javax.swing.GroupLayout(pnlKH);
        pnlKH.setLayout(pnlKHLayout);
        pnlKHLayout.setHorizontalGroup(
            pnlKHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlKHLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboKH, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlKHLayout.setVerticalGroup(
            pnlKHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlKHLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(pnlKHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(cboKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tabHV, javax.swing.GroupLayout.PREFERRED_SIZE, 530, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(pnlCD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pnlKH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlCD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabHV, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindActionPerformed
        fillTableNguoiHoc();
    }//GEN-LAST:event_btnFindActionPerformed

    private void cboCDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCDActionPerformed
        int i = cboCD.getSelectedIndex();
        if(i>-1){
            initComboboxKH();
        }
    }//GEN-LAST:event_cboCDActionPerformed

    private void cboKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboKHActionPerformed
        int i = cboKH.getSelectedIndex();
        if(i>-1){
            fillTableHocVien();
        }
    }//GEN-LAST:event_cboKHActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        addHV();
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        removeHV();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        updateDiem();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void tblHVMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHVMouseClicked
        
    }//GEN-LAST:event_tblHVMouseClicked

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
            java.util.logging.Logger.getLogger(QLHocVienJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QLHocVienJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QLHocVienJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QLHocVienJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                QLHocVienJDialog dialog = new QLHocVienJDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnFind;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cboCD;
    private javax.swing.JComboBox<String> cboKH;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPanel pnlCD;
    private javax.swing.JPanel pnlFind;
    private javax.swing.JPanel pnlHocVien;
    private javax.swing.JPanel pnlKH;
    private javax.swing.JPanel pnlNH;
    private javax.swing.JTabbedPane tabHV;
    private javax.swing.JTable tblHV;
    private javax.swing.JTable tblNH;
    private javax.swing.JTextField txtFind;
    // End of variables declaration//GEN-END:variables
}
