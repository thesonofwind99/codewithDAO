/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.mycompany.frame;

import com.google.zxing.WriterException;
import com.mycompany.classdao.NhanVienDAO;
import com.mycompany.entity.NhanVien;
import com.mycompany.extendent.Auth;
import com.mycompany.extendent.MsgBox;
import com.mycompany.extendent.QRCodeUtil;
import com.mycompany.extendent.ValidateClass;
import static com.mycompany.frame.MainJFrame.logger;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author Administrator
 */
public class QLNhanVienJDialog extends javax.swing.JDialog {
    private DefaultTableModel tblModel = new DefaultTableModel();
    private NhanVienDAO dao = new NhanVienDAO();
    private int row = 0;
    private StringBuilder error = new StringBuilder();
    public static String qrCodeImage = "";
    private int i = 0;
    java.awt.Frame parent = null;    
    /**
     * Creates new form QLNhanVienJDialog
     */
    public QLNhanVienJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        initTable();
        fillToTable();
        setStatus(true);
        this.parent = parent;
    }
    public void initTable(){
        tblModel = (DefaultTableModel) tblDS.getModel();
        String columns[] = new String[]{
            "Số thứ tự","Mã nhân viên", "Mật khẩu", "Họ và tên", "Vai trò"
        };
        tblModel.setColumnIdentifiers(columns);
    }
    public void fillToTable(){//hàm này lấy dữ liệu từ database đổ trực tiếp lên bảng mà ko có dùng for_each luôn nho
        try {
            tblModel.setRowCount(0);
            List<NhanVien> list = dao.selectAll();
            int i =1;
            for (NhanVien nv : list) {
                tblModel.addRow(new Object[]{
                    i++, nv.getMaNV(), "*".repeat(nv.getMatKhau().length()), nv.getHoTen(), nv.isVaiTro()?"Trưởng phòng":"Nhân viên"
                });
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn");
        }
    }
    public void checkForm(){
        if (txtPassword.getText().isEmpty()) {
            error.append("Mật khẩu không được trống\n");
        } else {
            if (!ValidateClass.isValidPassWord(txtPassword.getText())) {
                error.append("Mật khẩu phải từ 6 đến 10 ký tự và không chứa ký tự tiếng Việt\n");
            } else {
                if (txtPasswordAgain.getText().isEmpty()) {
                    error.append("Vui lòng xác nhận lại mật khẩu\n");
                }else{
                    if(!txtPassword.getText().equals(txtPasswordAgain.getText())){
                        error.append("Mật khẩu và mật khẩu xác nhận không trùng nhau\n");
                    }
                }    
            }
        }
        if(txtName.getText().isEmpty()){
            error.append("Họ tên không được để trống\n");
        }else{
            if(!ValidateClass.isName(txtName.getText())){
                error.append("Họ tên không đúng định dạng\n");
            }
        }    
        if(bgrRole.getSelection()==null){
            error.append("Bạn phải chọn vai trò");
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
    public NhanVien getForm(){//hàm này là để lấy dữ liệu từ form truyền vào biến NhanVien
        NhanVien nv = new NhanVien();
        nv.setMaNV(txtID.getText());
        nv.setHoTen(txtName.getText());
        nv.setMatKhau(txtPassword.getText());
        nv.setVaiTro(rdoTP.isSelected()?true:false);
        return nv;
    }
    public void setForm(NhanVien nv){//hàm này để lấy dữ liệu từ NhanVien đổ lên form
        txtID.setText(nv.getMaNV());
        txtName.setText(nv.getHoTen());
        txtPassword.setText(nv.getMatKhau());
        txtPasswordAgain.setText(nv.getMatKhau());
        rdoTP.setSelected(nv.isVaiTro());
        rdoNV.setSelected(!nv.isVaiTro());
    }
    public void setStatus(boolean insertable){//ở đây insertable nghĩa là trạng thái cập nhật (chèn, xoá, sửa)
        txtID.setEditable(insertable);      //nếu insertable là true thì nghĩa là đang ở trạng thái cập nhật
        btnAdd.setEnabled(insertable);      //còn insertable là false thì nghĩa là đang ở trạng thái xem (những nút điều hướng)
        btnUpdate.setEnabled(!insertable);
        btnDelete.setEnabled(!insertable);
        boolean first = (row>0);
        boolean last = (row<tblDS.getRowCount()-1);
        btnFirst.setEnabled(!insertable && first);
        btnPrevious.setEnabled(!insertable && first);
        btnLast.setEnabled(!insertable && last);
        btnNext.setEnabled(!insertable && last);
    }
    public void edit(){
        String maNV = (String) tblModel.getValueAt(row, 1);
        NhanVien nv = dao.selectById(maNV);
        if(nv!=null){
            setStatus(false);
            setForm(nv);
        }
    }
    public void clear(){
        setForm(new NhanVien());//trường hợp này là khi tạo nhân viên mới (new NhanVien) thì những thuộc tính của nó sẽ trống rỗng
        setStatus(true);//vì thế sự trống rỗng đó đồng nghĩa như là clear toàn bộ form nhập
        bgrRole.clearSelection();//việc clear form thường để nhằm mục đích là cập nhật (chèn, xoá, sửa) nên mới để cho giá trị
    }                            //insertable = true (đang ở trạng thái cập nhật vừa nói ở phương thức setStatus ở trên)                           
    public void insert(){
        NhanVien nv = getForm();
        if (txtID.getText().equals("")) {
            error.append("Mã nhân viên không được trống\n");
        } else {
            NhanVien nv1 = dao.selectById(nv.getMaNV());
            if (nv1 != null) {
                error.append("Mã nhân viên đã tồn tại\n");
            }else{
                String regrex = "^(?=.*[a-z])(?=.*[A-Z]).{6,8}$";
                if(!txtID.getText().matches(regrex)){
                    error.append("Mã nhân viên không đúng theo cấu trúc yêu cầu\n");
                }
            }
        }
        if (!showError()) {
            return;
        }
        try {
            dao.insert(nv);
            fillToTable();
            clear();
            MsgBox.alert(this, "Thêm mới thành công");
            logger.info("A new employee was added by "+ Auth.user.getMaNV());
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi khi thêm mới");
        }
    }
    public void delete(){
        try {
            if (Auth.isManager()){
                String maNV = txtID.getText();
                if (MsgBox.confirm(this, "Bạn muốn xoá nhân viên này ?")) {
                    if(!maNV.equalsIgnoreCase(Auth.user.getMaNV())){
                        dao.delete(maNV);
                        fillToTable();
                        clear();
                        MsgBox.alert(this, "Xoá thành công");
                    }else{
                        MsgBox.alert(this, "Bạn không thể xoá chính bạn");
                    }
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
        NhanVien nv = getForm();
        try {
            if(MsgBox.confirm(this, "Bạn muốn cập nhật cho nhân viên này ?")){
                dao.update(nv);
                fillToTable();
                clear();
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
        row = tblDS.getRowCount()-1;
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

        bgrRole = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tabNV = new javax.swing.JTabbedPane();
        pnlCapnhat = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtID = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        jLabel4 = new javax.swing.JLabel();
        txtPasswordAgain = new javax.swing.JPasswordField();
        jLabel5 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        rdoTP = new javax.swing.JRadioButton();
        rdoNV = new javax.swing.JRadioButton();
        pnlControl = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        btnQRCode = new javax.swing.JButton();
        pnlDirect = new javax.swing.JPanel();
        btnFirst = new javax.swing.JButton();
        btnPrevious = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        lblhidePass = new javax.swing.JLabel();
        pnlDanhsach = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDS = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        jLabel1.setText("QUẢN LÝ NHÂN VIÊN ");

        jLabel2.setText("Mã nhân viên:");

        txtID.setToolTipText("Mã nhân viên phải từ 6 đến 8 ký tự và có ít nhất 1 chữ Hoa và 1 chữ thường");

        jLabel3.setText("Mật khẩu:");

        jLabel4.setText("Xác nhận mật khẩu:");

        jLabel5.setText("Họ và tên:");

        jLabel6.setText("Vai trò:");

        bgrRole.add(rdoTP);
        rdoTP.setText("Trưởng phòng");

        bgrRole.add(rdoNV);
        rdoNV.setText("Nhân viên");

        pnlControl.setLayout(new java.awt.GridLayout(1, 5, 8, 0));

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

        btnQRCode.setText("QR Code");
        btnQRCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQRCodeActionPerformed(evt);
            }
        });
        pnlControl.add(btnQRCode);

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

        lblhidePass.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-hide-24.png"))); // NOI18N
        lblhidePass.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblhidePassMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlCapnhatLayout = new javax.swing.GroupLayout(pnlCapnhat);
        pnlCapnhat.setLayout(pnlCapnhatLayout);
        pnlCapnhatLayout.setHorizontalGroup(
            pnlCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCapnhatLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtID)
                    .addComponent(txtPasswordAgain)
                    .addComponent(txtName)
                    .addGroup(pnlCapnhatLayout.createSequentialGroup()
                        .addComponent(pnlControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pnlDirect, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlCapnhatLayout.createSequentialGroup()
                        .addGroup(pnlCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addGroup(pnlCapnhatLayout.createSequentialGroup()
                                .addComponent(rdoNV)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rdoTP))
                            .addGroup(pnlCapnhatLayout.createSequentialGroup()
                                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 610, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblhidePass, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlCapnhatLayout.setVerticalGroup(
            pnlCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCapnhatLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblhidePass, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(txtPassword))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPasswordAgain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlCapnhatLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rdoTP)
                            .addComponent(rdoNV))
                        .addGap(18, 18, 18)
                        .addComponent(pnlControl, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnlDirect, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        tabNV.addTab("CẬP NHẬT", pnlCapnhat);

        tblDS.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, true, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDSMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDSMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblDS);

        javax.swing.GroupLayout pnlDanhsachLayout = new javax.swing.GroupLayout(pnlDanhsach);
        pnlDanhsach.setLayout(pnlDanhsachLayout);
        pnlDanhsachLayout.setHorizontalGroup(
            pnlDanhsachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE)
        );
        pnlDanhsachLayout.setVerticalGroup(
            pnlDanhsachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
        );

        tabNV.addTab("DANH SÁCH", pnlDanhsach);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tabNV)
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
                .addComponent(tabNV, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        update();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clear();
    }//GEN-LAST:event_btnClearActionPerformed

    private void tblDSMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDSMouseClicked
        //Nếu nhấn hai lần vào dòng của bảng thì mới khai triển bên trong hàm if

    }//GEN-LAST:event_tblDSMouseClicked

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        insert();
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        delete();
    }//GEN-LAST:event_btnDeleteActionPerformed

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

    private void tblDSMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDSMousePressed
        if (evt.getClickCount() == 2) {
            row = tblDS.rowAtPoint(evt.getPoint());
            edit();
            tabNV.setSelectedIndex(0);//Tự động chuyển tab
        }        
    }//GEN-LAST:event_tblDSMousePressed

    private void btnQRCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQRCodeActionPerformed
        try {
            String maNV = (String) tblDS.getValueAt(this.row, 1);
            NhanVien nv = dao.selectById(maNV);
            String password = nv.getMatKhau();
            String qrCodeText = maNV +"."+password;
            String filePath = maNV + "_" + password + "_" + ".jpg";
            File destination = new File("storeFiles", filePath);
            filePath = Paths.get(destination.getAbsolutePath()).toString();
            int size = 400;
            String fileType = "jpg";
            File qrFile = new File(filePath);
            QRCodeUtil.createQRImage(qrFile, qrCodeText, size, fileType);
            this.qrCodeImage = filePath;
            new QRCodeJDialog(this.parent, true).setVisible(true);
        } catch (WriterException ex) {
        } catch (IOException ex) {
        }
    }//GEN-LAST:event_btnQRCodeActionPerformed

    private void lblhidePassMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblhidePassMouseClicked
        if(Auth.isManager()){
                    if(i == 0){
            txtPassword.setEchoChar((char)0);
            lblhidePass.setIcon(new ImageIcon("src//main//resources//icon//icons8-eye-24.png"));
            i++;
        }else{
            txtPassword.setEchoChar('\u25CF');
            lblhidePass.setIcon(new ImageIcon("src//main//resources//icon//icons8-hide-24.png"));
            i=0;
        }
        }else{
            MsgBox.alert(this, "Nhân viên không thể xem mật khẩu");
        }    
    }//GEN-LAST:event_lblhidePassMouseClicked

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
            java.util.logging.Logger.getLogger(QLNhanVienJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QLNhanVienJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QLNhanVienJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QLNhanVienJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                QLNhanVienJDialog dialog = new QLNhanVienJDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.ButtonGroup bgrRole;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnFirst;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrevious;
    private javax.swing.JButton btnQRCode;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblhidePass;
    private javax.swing.JPanel pnlCapnhat;
    private javax.swing.JPanel pnlControl;
    private javax.swing.JPanel pnlDanhsach;
    private javax.swing.JPanel pnlDirect;
    private javax.swing.JRadioButton rdoNV;
    private javax.swing.JRadioButton rdoTP;
    private javax.swing.JTabbedPane tabNV;
    private javax.swing.JTable tblDS;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtName;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JPasswordField txtPasswordAgain;
    // End of variables declaration//GEN-END:variables
}
