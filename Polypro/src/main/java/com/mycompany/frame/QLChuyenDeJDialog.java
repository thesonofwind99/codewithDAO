/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.mycompany.frame;

import com.mycompany.classdao.ChuyenDeDAO;
import com.mycompany.entity.ChuyenDe;
import com.mycompany.extendent.Auth;
import com.mycompany.extendent.MsgBox;
import com.mycompany.extendent.ValidateClass;
import com.mycompany.extendent.XImage;
import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Administrator
 */
public class QLChuyenDeJDialog extends javax.swing.JDialog {
    private DefaultTableModel tblModel = new DefaultTableModel();
    private ChuyenDeDAO dao = new ChuyenDeDAO();
    private int row = 0;
    private StringBuilder error = new StringBuilder();
    /**
     * Creates new form QLChuyenDeJDialog
     */
    public QLChuyenDeJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        initTable();
        fillToTable();
        setStatus(true);
    }
    public void checkForm(){
        if(txtNameChuyenDe.getText().isEmpty()){
            error.append("Tên chuyên đề không được để trống\n");
        }else{
            if(!ValidateClass.isName(txtNameChuyenDe.getText())){
                error.append("Tên chuyên đề không đúng định dạng\n");
            }
        }    
        if(txtThoiLuong.getText().isEmpty()){
            error.append("Phải nhập thời lượng\n");
        }else{
            try {
                int thoiLuong = Integer.parseInt(txtThoiLuong.getText());
                if(thoiLuong<=0||thoiLuong>100){
                    error.append("Thời lượng phải lớn hơn 0 và nhỏ hơn 100\n");
                }
            } catch (Exception e) {
                error.append("Thời lượng phải là số nguyên\n");
            }
        }
        if(txtHocPhi.getText().isEmpty()){
            error.append("Học phí không được để trống");
        }else{
            try {
                double hocPhi = Double.parseDouble(txtHocPhi.getText());
                if(hocPhi<=0||hocPhi>1000){
                    error.append("Học phí phải lớn hơn 0 và nhỏ hơn 1000");
                }
            } catch (Exception e) {
                error.append("Học phí phải là số thực");
            }
        }
    }
    public boolean showError(){
        checkForm();
        if(error.length()>0){
            MsgBox.alert(this, error.toString());
            error.setLength(0);
            return false;
        }
        return true;
    }     
    public void initTable(){
        tblModel = (DefaultTableModel) tblDSCD.getModel();
        String columns[] = new String[]{
            "Số thứ tự", "Mã chuyên đề", "Tên chuyên đề", "Học phí", "Thời lượng", "Hình"
        };
        tblModel.setColumnIdentifiers(columns);
    }
    public void fillToTable(){
        try {
            tblModel.setRowCount(0);
            List<ChuyenDe> list = dao.selectAll();
            int i = 1;
            for (ChuyenDe cd : list) {
                tblModel.addRow(new Object[]{
                    i++, cd.getMaCD(), cd.getTenCD(), cd.getHocPhi(), cd.getThoiLuong(), cd.getHinh()
                });
            }
        } catch (Exception e) {
        }
    }
    public ChuyenDe getForm(){
        ChuyenDe cd = new ChuyenDe();
        cd.setMaCD(txtIDChuyenDe.getText());
        cd.setTenCD(txtNameChuyenDe.getText());
        if(!txtHocPhi.getText().isEmpty()){
            cd.setHocPhi(Double.parseDouble(txtHocPhi.getText()));
        }       
        cd.setMoTa(txtMoTaCD.getText());
        if(!txtThoiLuong.getText().isEmpty()){
            cd.setThoiLuong(Integer.parseInt(txtThoiLuong.getText()));
        }       
        cd.setHinh(lblPicture.getToolTipText());
        return cd;
    }
    public void setForm(ChuyenDe cd){
        txtIDChuyenDe.setText(cd.getMaCD());
        txtNameChuyenDe.setText(cd.getTenCD());
        txtHocPhi.setText(String.valueOf(cd.getHocPhi()));
        txtThoiLuong.setText(String.valueOf(cd.getThoiLuong()));
        txtMoTaCD.setText(cd.getMoTa());
        if(cd.getHinh()!=null){
            lblPicture.setIcon(XImage.read(cd.getHinh()));
            lblPicture.setToolTipText(cd.getHinh());
        }else{
            lblPicture.setIcon(null);
        }
    }
    public void setStatus(boolean insertable){
        txtIDChuyenDe.setEditable(insertable);
        btnAdd.setEnabled(insertable);
        btnDelete.setEnabled(!insertable);
        btnUpdate.setEnabled(!insertable);
        boolean first = (row>0);
        boolean last = (row<tblDSCD.getRowCount()-1);
        btnFirst.setEnabled(!insertable && first);
        btnPrevious.setEnabled(!insertable && first);
        btnNext.setEnabled(!insertable && last);
        btnLast.setEnabled(!insertable && last);
    }
    public void clearForm(){
        setForm(new ChuyenDe());
        setStatus(true);
    }
    public void edit(){
        String maCD = (String) tblDSCD.getValueAt(row, 1);
        ChuyenDe cd = dao.selectById(maCD);
        if (cd != null) {
            setForm(cd);
            setStatus(false);
        }
    }
    public void selectPicture(){
        JFileChooser fileChooser = new JFileChooser();
        if(fileChooser.showOpenDialog(this)==JFileChooser.APPROVE_OPTION){
            File file = fileChooser.getSelectedFile();
            XImage.save(file);//lưu vào thư mục logo
            ImageIcon icon = XImage.read(file.getName());//lấy hình từ thư mục logo
            lblPicture.setIcon(icon);
            lblPicture.setToolTipText(file.getName());
        }
    }
    public void insert(){
        ChuyenDe cd = getForm();
        if (txtIDChuyenDe.getText().equals("")) {
            error.append("Mã chuyên đề không được trống\n");
        } else {
            ChuyenDe cd1 = dao.selectById(cd.getMaCD());
            if (cd1 != null) {
                error.append("Mã chuyên đề đã tồn tại\n");
            }
        }
        if(!showError()){
            return;
        }
        try {
            dao.insert(cd);
            fillToTable();
            clearForm();
            MsgBox.alert(this, "Thêm mới thành công");
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi khi thêm mới");
        }
    }
    public void delete(){
        try {
            if (Auth.isManager()){
                String maCD = txtIDChuyenDe.getText();
                if (MsgBox.confirm(this, "Bạn muốn xoá chuyên đề này ?")) {
                    dao.delete(maCD);
                    fillToTable();
                    clearForm();
                    MsgBox.alert(this, "Xoá thành công");
                }
            }else{
                MsgBox.alert(this, "Nhân viên không có quyền xoá");
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi khi xoá");
        }
    }
    public void update(){
        if(!showError()){
            return;
        }
        ChuyenDe cd = getForm();
        try {
            if(MsgBox.confirm(this, "Bạn muốn cập nhật chuyên đề này ?")){
                dao.update(cd);
                fillToTable();
                clearForm();
                MsgBox.alert(this, "Cập nhật thành công");
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi cập nhật");
        }
    }
    public void first(){
        row = 0;
        edit();
    }
    public void pre(){
        row--;
        edit();
    }
    public void last(){
        row = tblDSCD.getRowCount()-1;
        edit();
    }
    public void next(){
        row++;
        edit();
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
        tabCD = new javax.swing.JTabbedPane();
        pnlCapnhat = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtIDChuyenDe = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtHocPhi = new javax.swing.JTextField();
        pnlControl = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        pnlDirect = new javax.swing.JPanel();
        btnFirst = new javax.swing.JButton();
        btnPrevious = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        lblPicture = new javax.swing.JLabel();
        txtNameChuyenDe = new javax.swing.JTextField();
        txtThoiLuong = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtMoTaCD = new javax.swing.JTextArea();
        pnlDanhsach = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDSCD = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        jLabel1.setText("QUẢN LÝ CHUYÊN ĐỀ");

        jLabel2.setText("Mã chuyên đề:");

        jLabel3.setText("Tên chuyên đề:");

        jLabel4.setText("Thời lượng:");

        jLabel5.setText("Học phí:");

        pnlControl.setLayout(new java.awt.GridLayout(1, 4, 8, 0));

        btnAdd.setText("Thêm");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        pnlControl.add(btnAdd);

        btnDelete.setText("Xoá");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        pnlControl.add(btnDelete);

        btnUpdate.setText("Sửa");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        pnlControl.add(btnUpdate);

        btnClear.setText("Mới");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        pnlControl.add(btnClear);

        pnlDirect.setLayout(new java.awt.GridLayout(1, 4, 8, 0));

        btnFirst.setText("|<");
        btnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
            }
        });
        pnlDirect.add(btnFirst);

        btnPrevious.setText("<<");
        btnPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviousActionPerformed(evt);
            }
        });
        pnlDirect.add(btnPrevious);

        btnNext.setText(">>");
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });
        pnlDirect.add(btnNext);

        btnLast.setText(">|");
        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });
        pnlDirect.add(btnLast);

        jLabel7.setText("Hình logo:");

        lblPicture.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblPicture.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblPictureMouseClicked(evt);
            }
        });

        jLabel6.setText("Mô tả chuyên đề:");

        txtMoTaCD.setColumns(20);
        txtMoTaCD.setRows(5);
        jScrollPane2.setViewportView(txtMoTaCD);

        javax.swing.GroupLayout pnlCapnhatLayout = new javax.swing.GroupLayout(pnlCapnhat);
        pnlCapnhat.setLayout(pnlCapnhatLayout);
        pnlCapnhatLayout.setHorizontalGroup(
            pnlCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCapnhatLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(pnlCapnhatLayout.createSequentialGroup()
                        .addComponent(lblPicture, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addGroup(pnlCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtIDChuyenDe)
                            .addComponent(txtHocPhi)
                            .addComponent(txtNameChuyenDe)
                            .addGroup(pnlCapnhatLayout.createSequentialGroup()
                                .addGroup(pnlCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txtThoiLuong)))
                    .addGroup(pnlCapnhatLayout.createSequentialGroup()
                        .addGroup(pnlCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlCapnhatLayout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(111, 111, 111)
                                .addComponent(jLabel2))
                            .addComponent(jLabel6)
                            .addGroup(pnlCapnhatLayout.createSequentialGroup()
                                .addComponent(pnlControl, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(pnlDirect, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlCapnhatLayout.setVerticalGroup(
            pnlCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCapnhatLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel7))
                .addGroup(pnlCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlCapnhatLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(txtIDChuyenDe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNameChuyenDe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtThoiLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtHocPhi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlCapnhatLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblPicture, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlControl, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlDirect, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        tabCD.addTab("CẬP NHẬT", pnlCapnhat);

        tblDSCD.setModel(new javax.swing.table.DefaultTableModel(
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
        tblDSCD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDSCDMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblDSCD);

        javax.swing.GroupLayout pnlDanhsachLayout = new javax.swing.GroupLayout(pnlDanhsach);
        pnlDanhsach.setLayout(pnlDanhsachLayout);
        pnlDanhsachLayout.setHorizontalGroup(
            pnlDanhsachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE)
        );
        pnlDanhsachLayout.setVerticalGroup(
            pnlDanhsachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)
        );

        tabCD.addTab("DANH SÁCH", pnlDanhsach);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tabCD)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabCD)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clearForm();
    }//GEN-LAST:event_btnClearActionPerformed

    private void lblPictureMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPictureMouseClicked
        selectPicture();
    }//GEN-LAST:event_lblPictureMouseClicked

    private void tblDSCDMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDSCDMousePressed
        if(evt.getClickCount()==2){
            row = tblDSCD.rowAtPoint(evt.getPoint());
            edit();
            tabCD.setSelectedIndex(0);
        }
    }//GEN-LAST:event_tblDSCDMousePressed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        insert();
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        delete();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        update();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
        first();
    }//GEN-LAST:event_btnFirstActionPerformed

    private void btnPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviousActionPerformed
        pre();
    }//GEN-LAST:event_btnPreviousActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        next();
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
        last();
    }//GEN-LAST:event_btnLastActionPerformed

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
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(QLChuyenDeJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QLChuyenDeJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QLChuyenDeJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QLChuyenDeJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                QLChuyenDeJDialog dialog = new QLChuyenDeJDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnFirst;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrevious;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblPicture;
    private javax.swing.JPanel pnlCapnhat;
    private javax.swing.JPanel pnlControl;
    private javax.swing.JPanel pnlDanhsach;
    private javax.swing.JPanel pnlDirect;
    private javax.swing.JTabbedPane tabCD;
    private javax.swing.JTable tblDSCD;
    private javax.swing.JTextField txtHocPhi;
    private javax.swing.JTextField txtIDChuyenDe;
    private javax.swing.JTextArea txtMoTaCD;
    private javax.swing.JTextField txtNameChuyenDe;
    private javax.swing.JTextField txtThoiLuong;
    // End of variables declaration//GEN-END:variables
}
