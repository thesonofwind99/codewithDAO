/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.mycompany.frame;

import com.google.zxing.WriterException;
import com.mycompany.classdao.ChuyenDeDAO;
import com.mycompany.classdao.KhoaHocDAO;
import com.mycompany.entity.ChuyenDe;
import com.mycompany.entity.KhoaHoc;
import com.mycompany.extendent.Auth;
import com.mycompany.extendent.MsgBox;
import com.mycompany.extendent.QRCodeUtil;
import com.mycompany.extendent.ValidateClass;
import com.mycompany.extendent.XDate;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Administrator
 */
public class QLKhoaHocJDialog extends javax.swing.JDialog {
    private DefaultTableModel tblModel = new DefaultTableModel();
    private KhoaHocDAO khDao = new KhoaHocDAO();
    private ChuyenDeDAO cdDao = new ChuyenDeDAO();
    private int row = 0;
    private StringBuilder error = new StringBuilder();
    /**
     * Creates new form QLKhoaHocJDialog
     */
    public QLKhoaHocJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);        
        initTable();
        initCombobox();
        setDisable();
    }
    public void initCombobox(){
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboCD.getModel();
        model.removeAllElements();
        List<ChuyenDe> list = cdDao.selectAll();
        for (ChuyenDe cd : list) {
            model.addElement(cd);
        }
        fillToTable();
    }
    public void initTable(){
        tblModel = (DefaultTableModel) tblDSKH.getModel();
        String columns[] = new String[]{
            "Số thứ tự", "Mã khoá học", "Thời lượng", "Học phí", "Khai giảng", "Tạo bởi", "Ngày tạo"
        };
        tblModel.setColumnIdentifiers(columns);
    }
    public void checkForm(){
        KhoaHoc kh = getForm();
        if(kh.getNgayKG()==null){
            error.append("Ngày khai giảng không được trống\n");
        }else{
            try {
                Date ngayKhaiGiang = XDate.toDate(txtKhaiGiang.getText(), "dd-MM-yyyy");
                if(!ValidateClass.isOver_20days(ngayKhaiGiang, kh.getNgayTao())){
                    error.append("Ngày khai giảng phải lớn hơn ngày tạo 20 ngày");
                }
            } catch (Exception e) {
                error.append("Ngày khai giảng không đúng định dạng dd-MM-yyyy");
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
    public void setDisable(){
        txtNameChuyenDe.setEditable(false);
        txtThoiLuong.setEditable(false);
        txtHocPhi.setEditable(false);
        txtNguoiTao.setEditable(false);
        txtNgayTao.setEditable(false);
    }
    public void fillToTable(){
        tblModel.setRowCount(0);
        ChuyenDe cd = (ChuyenDe) cboCD.getSelectedItem();
        List<KhoaHoc> list = khDao.selectByCD(cd.getMaCD());
        int i = 1;
        for (KhoaHoc kh : list) {
            tblModel.addRow(new Object[]{
                i++, kh.getMaKH(), kh.getThoiLuong(), kh.getHocPhi(), XDate.toString(kh.getNgayKG(), "dd-MM-yyyy"), 
                kh.getMaNV(), XDate.toString(kh.getNgayTao(), "dd-MM-yyyy")
            });
        }      
    }
    public KhoaHoc getForm(){
        ChuyenDe cd = (ChuyenDe) cboCD.getSelectedItem();
        KhoaHoc kh = new KhoaHoc();
        kh.setMaCD(cd.getMaCD());
        kh.setMaNV(txtNguoiTao.getText());
        kh.setNgayTao(XDate.toDate(txtNgayTao.getText(), "dd-MM-yyyy"));
        if(txtKhaiGiang.getText().isEmpty()){
            kh.setNgayKG(null);
        }else{
            kh.setNgayKG(XDate.toDate(txtKhaiGiang.getText(), "dd-MM-yyyy"));
        }
        kh.setHocPhi(Double.parseDouble(txtHocPhi.getText()));
        kh.setThoiLuong(Integer.parseInt(txtThoiLuong.getText()));
        return kh;
    }
    public void setForm(KhoaHoc kh){
        if(kh.getMaCD()==null){
            ChuyenDe cd = (ChuyenDe) cboCD.getSelectedItem();
            txtHocPhi.setText(String.valueOf(cd.getHocPhi()));
            txtNguoiTao.setText(Auth.user.getMaNV());
            txtThoiLuong.setText(String.valueOf(cd.getThoiLuong()));
        }else{
            txtHocPhi.setText(String.valueOf(kh.getHocPhi()));
            txtNguoiTao.setText(kh.getMaNV());
            txtThoiLuong.setText(String.valueOf(kh.getThoiLuong()));
        }
        if (kh.getNgayKG() != null) {
            txtKhaiGiang.setText(XDate.toString(kh.getNgayKG(), "dd-MM-yyyy"));
        } else {
            txtKhaiGiang.setText(null);
        }
        if (kh.getNgayTao() != null) {
            txtNgayTao.setText(XDate.toString(kh.getNgayTao(), "dd-MM-yyyy"));
        } else {
            txtNgayTao.setText(XDate.toString(new Date(), "dd-MM-yyyy"));
        }        
        txtGhiChu.setText(kh.getGhiChu());

    }
    public void setStatus(boolean insertable){
        btnAdd.setEnabled(insertable);
        btnDelete.setEnabled(!insertable);
        btnUpdate.setEnabled(!insertable);
        boolean first = (row>0);
        boolean last = (row<tblDSKH.getRowCount()-1);
        btnFirst.setEnabled(!insertable && first);
        btnPrevious.setEnabled(!insertable && first);
        btnNext.setEnabled(!insertable && last);
        btnLast.setEnabled(!insertable && last);
    }
    public void selectCD(){
        ChuyenDe cd = (ChuyenDe) cboCD.getSelectedItem();
        txtNameChuyenDe.setText(cd.getTenCD());
        txtHocPhi.setText(String.valueOf(cd.getHocPhi()));
        txtThoiLuong.setText(String.valueOf(cd.getThoiLuong()));
        txtGhiChu.setText(cd.getTenCD());
        txtNguoiTao.setText(Auth.user.getMaNV());
        txtNgayTao.setText(XDate.toString(new Date(), "dd-MM-yyyy"));        
        fillToTable();
        setStatus(false);
        row = 0;
        tabKH.setSelectedIndex(1);
    }
    public void edit(){
        int maKH = (int) tblDSKH.getValueAt(row, 1);
        KhoaHoc kh = khDao.selectById(maKH);
        setForm(kh);
        setStatus(false);
    }
    public void clearForm(){
        setForm(new KhoaHoc());
        setStatus(true);
    }
    public void insert(){
        if(!showError()){
            return;
        }
        KhoaHoc kh = getForm();
        try {
            khDao.insert(kh);
            fillToTable();
            clearForm();
            MsgBox.alert(this, "Thêm thành công");
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi thêm mới");
        }
    }
    public void delete(){
        int maKH = (int) tblDSKH.getValueAt(row, 1);
        KhoaHoc kh = khDao.selectById(maKH);
        try {
            if (Auth.isManager()){
                if (MsgBox.confirm(this, "Bạn muốn xoá khoá học này ?")) {
                    khDao.delete(maKH);
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
        int maKH = (int) tblDSKH.getValueAt(row, 1);
        KhoaHoc kh = getForm();
        kh.setMaKH(maKH);
        try {
            if(MsgBox.confirm(this, "Bạn muốn cập nhật khoá học này ?")){
                khDao.update(kh);
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
        row = tblDSKH.getRowCount()-1;
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
        tabKH = new javax.swing.JTabbedPane();
        tabCapnhat = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtKhaiGiang = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtThoiLuong = new javax.swing.JTextField();
        txtNgayTao = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtGhiChu = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        btnFirst = new javax.swing.JButton();
        btnPrevious = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        txtNameChuyenDe = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtHocPhi = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtNguoiTao = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        tabDanhsach = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDSKH = new javax.swing.JTable();
        pnlCD = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        cboCD = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        jLabel1.setText("QUẢN LÝ KHOÁ HỌC");

        jLabel2.setText("Khai giảng:");

        txtKhaiGiang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKhaiGiangActionPerformed(evt);
            }
        });

        jLabel3.setText("Thời lượng (Giờ):");

        jLabel4.setText("Ngày tạo:");

        jLabel7.setText("Chuyên đề:");

        txtGhiChu.setColumns(20);
        txtGhiChu.setRows(5);
        jScrollPane2.setViewportView(txtGhiChu);

        jPanel2.setLayout(new java.awt.GridLayout(1, 5, 5, 0));

        btnAdd.setText("Thêm");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        jPanel2.add(btnAdd);

        btnDelete.setText("Xoá");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        jPanel2.add(btnDelete);

        btnUpdate.setText("Sửa");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        jPanel2.add(btnUpdate);

        btnClear.setText("Mới");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        jPanel2.add(btnClear);

        jPanel3.setLayout(new java.awt.GridLayout(1, 4, 8, 0));

        btnFirst.setText("|<");
        btnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
            }
        });
        jPanel3.add(btnFirst);

        btnPrevious.setText("<<");
        btnPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviousActionPerformed(evt);
            }
        });
        jPanel3.add(btnPrevious);

        btnNext.setText(">>");
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });
        jPanel3.add(btnNext);

        btnLast.setText(">|");
        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });
        jPanel3.add(btnLast);

        jLabel8.setText("Học phí:");

        jLabel9.setText("Người tạo:");

        jLabel10.setText("Ghi chú:");

        javax.swing.GroupLayout tabCapnhatLayout = new javax.swing.GroupLayout(tabCapnhat);
        tabCapnhat.setLayout(tabCapnhatLayout);
        tabCapnhatLayout.setHorizontalGroup(
            tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabCapnhatLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 662, Short.MAX_VALUE)
                    .addGroup(tabCapnhatLayout.createSequentialGroup()
                        .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9)
                            .addComponent(txtNameChuyenDe, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
                            .addComponent(txtHocPhi)
                            .addComponent(txtNguoiTao))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtThoiLuong)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNgayTao, javax.swing.GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE)
                            .addComponent(txtKhaiGiang)))
                    .addGroup(tabCapnhatLayout.createSequentialGroup()
                        .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel10))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(tabCapnhatLayout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        tabCapnhatLayout.setVerticalGroup(
            tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabCapnhatLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel7))
                .addGap(3, 3, 3)
                .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtKhaiGiang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNameChuyenDe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtThoiLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtHocPhi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNgayTao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNguoiTao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabCapnhatLayout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(tabCapnhatLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(16, 16, 16))
        );

        tabKH.addTab("CẬP NHẬT", tabCapnhat);

        tblDSKH.setModel(new javax.swing.table.DefaultTableModel(
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
        tblDSKH.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDSKHMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDSKHMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblDSKH);

        javax.swing.GroupLayout tabDanhsachLayout = new javax.swing.GroupLayout(tabDanhsach);
        tabDanhsach.setLayout(tabDanhsachLayout);
        tabDanhsachLayout.setHorizontalGroup(
            tabDanhsachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 674, Short.MAX_VALUE)
        );
        tabDanhsachLayout.setVerticalGroup(
            tabDanhsachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
        );

        tabKH.addTab("DANH SÁCH", tabDanhsach);

        pnlCD.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel6.setText("Chuyên đề:");

        cboCD.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboCD.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboCDItemStateChanged(evt);
            }
        });
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
                .addComponent(cboCD, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tabKH)
                    .addComponent(pnlCD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addGap(11, 11, 11)
                .addComponent(pnlCD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabKH, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void txtKhaiGiangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKhaiGiangActionPerformed
        
    }//GEN-LAST:event_txtKhaiGiangActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clearForm();
    }//GEN-LAST:event_btnClearActionPerformed

    private void cboCDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCDActionPerformed
        int i = cboCD.getSelectedIndex();
        if(i>-1){
            selectCD();
        }
    }//GEN-LAST:event_cboCDActionPerformed

    private void cboCDItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboCDItemStateChanged

    }//GEN-LAST:event_cboCDItemStateChanged

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        insert();
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        delete();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        update();        
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void tblDSKHMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDSKHMouseClicked
        //Trống rỗng
    }//GEN-LAST:event_tblDSKHMouseClicked

    private void tblDSKHMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDSKHMousePressed
        if(evt.getClickCount()==2){
            row = tblDSKH.rowAtPoint(evt.getPoint());
            edit();
            tabKH.setSelectedIndex(0);
        }
    }//GEN-LAST:event_tblDSKHMousePressed

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
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(QLKhoaHocJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QLKhoaHocJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QLKhoaHocJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QLKhoaHocJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                QLKhoaHocJDialog dialog = new QLKhoaHocJDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JComboBox<String> cboCD;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel pnlCD;
    private javax.swing.JPanel tabCapnhat;
    private javax.swing.JPanel tabDanhsach;
    private javax.swing.JTabbedPane tabKH;
    private javax.swing.JTable tblDSKH;
    private javax.swing.JTextArea txtGhiChu;
    private javax.swing.JTextField txtHocPhi;
    private javax.swing.JTextField txtKhaiGiang;
    private javax.swing.JTextField txtNameChuyenDe;
    private javax.swing.JTextField txtNgayTao;
    private javax.swing.JTextField txtNguoiTao;
    private javax.swing.JTextField txtThoiLuong;
    // End of variables declaration//GEN-END:variables
}
