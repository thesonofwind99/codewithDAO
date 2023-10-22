/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.mycompany.frame;

import com.mycompany.classdao.NguoiHocDAO;
import com.mycompany.entity.NguoiHoc;
import com.mycompany.extendent.Auth;
import com.mycompany.extendent.MsgBox;
import com.mycompany.extendent.ValidateClass;
import com.mycompany.extendent.XDate;
import com.mycompany.extendent.XImage;
import static com.mycompany.frame.MainJFrame.logger;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author Administrator
 */
public class QLNguoiHocJDialog extends javax.swing.JDialog {
    private DefaultTableModel tblModel = new DefaultTableModel();
    private NguoiHocDAO dao = new NguoiHocDAO();
    private int row = 0;
    private StringBuilder error = new StringBuilder();     
    /**
     * Creates new form QLNguoiHocJDialog
     */
    public QLNguoiHocJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);        
        initTable();
        fillToTable();
        setStatus(true);
    }
    public void initTable(){
        tblModel = (DefaultTableModel) tblDSNH.getModel();
        String columns[] = new String[]{
            "Số thứ tự", "Mã người học", "Họ và tên", "Giới tính", "Ngày sinh", "Điện thoại", "Email", "Mã nhân viên", "Ngày nhập"
        };
        tblModel.setColumnIdentifiers(columns);
    }
    public void fillToTable(){
        tblModel.setRowCount(0);
        List<NguoiHoc> list = new ArrayList<>();
        if(txtFind==null){
            list = dao.selectAll();
        }else{
            String keyWord = txtFind.getText();
            list = dao.searchByKeyWord(keyWord);
        }
        int i = 1;
        for (NguoiHoc nh : list) {
            tblModel.addRow(new Object[]{
                i++, nh.getMaNH(), nh.getHoTen(), nh.isGioiTinh()?"Nam":"Nữ", XDate.toString(nh.getNgaySinh(), "dd-MM-yyyy"), 
                nh.getDienThoai(), nh.getEmail(), nh.getMaNV(), XDate.toString(nh.getNgayDK(), "dd-MM-yyyy")
            });
        }
    }
    public void checkForm(){
        NguoiHoc nh = getForm();
        if(txtNameNguoiHoc.getText().isEmpty()){
            error.append("Tên người học không được trống\n");
        }else{
            if(!ValidateClass.isName(txtNameNguoiHoc.getText())){
                error.append("Tên không đúng định dạng\n");
            }
        }
        if(txtEmail.getText().isEmpty()){
            error.append("Email không được trống\n");
        }else {
            if (!ValidateClass.isValidEmail(txtEmail.getText())) {
                error.append("Email không đúng định dạng\n");
            }
        }
        if(txtBirthday.getText().isEmpty()){
            error.append("Ngày sinh không được trống\n");
        }else{
            if(!ValidateClass.isOver18(nh.getNgaySinh())){
                error.append("Trên 18 tuổi mới có thể đăng ký\n");
            }
        }
        if(txtPhoneNumber.getText().isEmpty()){
            error.append("Số điện thoại không được trống\n");
        }else{
            if(!ValidateClass.isValidPhoneNumber(txtPhoneNumber.getText())){
                error.append("Số điện thoại không đúng định dạng\n");
            }
        }
        if(bgrGender.getSelection()==null){
            error.append("Phải chọn gới tính");
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
    public NguoiHoc getForm(){
        NguoiHoc nh = new NguoiHoc();
        nh.setMaNH(txtIDNguoiHoc.getText());
        nh.setHoTen(txtNameNguoiHoc.getText());
        nh.setGioiTinh(rdoNam.isSelected()?true:false);
        nh.setDienThoai(txtPhoneNumber.getText());
        nh.setEmail(txtEmail.getText());
        nh.setGhiChu(txtGhiChu.getText());
        if(txtBirthday.getText().isEmpty()){
            nh.setNgaySinh(null);
        }else{
            nh.setNgaySinh(XDate.toDate(txtBirthday.getText(), "dd-MM-yyyy"));
        }
        if(nh.getMaNV()==null){
            nh.setMaNV(Auth.user.getMaNV());
        }
        nh.setNgayDK(new Date());
        return nh;
    }
    public void setForm(NguoiHoc nh){
        txtIDNguoiHoc.setText(nh.getMaNH());
        txtNameNguoiHoc.setText(nh.getHoTen());
        txtEmail.setText(nh.getEmail());
        if (nh.getNgaySinh() != null) {
            txtBirthday.setText(XDate.toString(nh.getNgaySinh(), "dd-MM-yyyy"));
        } else {
            txtBirthday.setText(null);
        }
        txtGhiChu.setText(nh.getGhiChu());
        txtPhoneNumber.setText(nh.getDienThoai());
        rdoNam.setSelected(nh.isGioiTinh());
        rdoNu.setSelected(!nh.isGioiTinh());
    }
    public void clear(){
        setForm(new NguoiHoc());
        bgrGender.clearSelection();
        setStatus(true);
    }
    public void setStatus(boolean insertable){
        txtIDNguoiHoc.setEditable(insertable);
        btnAdd.setEnabled(insertable);
        btnDelete.setEnabled(!insertable);
        btnUpdate.setEnabled(!insertable);
        boolean first = (row>0);
        boolean last = (row<tblDSNH.getRowCount()-1);
        btnFirst.setEnabled(!insertable && first);
        btnPrevious.setEnabled(!insertable && first);
        btnLast.setEnabled(!insertable && last);
        btnNext.setEnabled(!insertable && last);
    }
    public void edit(){
        String maNH = (String) tblModel.getValueAt(row, 1);
        NguoiHoc nh = dao.selectById(maNH);
        if(nh!=null){
            setForm(nh);
            setStatus(false);
        }
    }
    public void insert(){
        NguoiHoc nh = getForm();
        if (txtIDNguoiHoc.getText().isEmpty()) {
            error.append("Mã người học không được trống\n");
        } else {
            NguoiHoc nh1 = dao.selectById(nh.getMaNH());
            if (nh1 != null) {
                error.append("Mã người học đã tồn tại\n");
            }
        }
        if(!showError()){
            return;
        }
        try {
            dao.insert(nh);
            fillToTable();
            clear();
            MsgBox.alert(this, "Thêm thành công");
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi thêm mới");
        }
    }
    public void delete(){
        try {
            String maNH = txtIDNguoiHoc.getText();
            if(Auth.isManager()){
                if(MsgBox.confirm(this, "Bạn có muốn xoá người học này ?")){
                    dao.delete(maNH);
                    fillToTable();
                    clear();
                    MsgBox.alert(this, "Xoá thành công");
                }else{
                    MsgBox.alert(this, "Nhân viên không được phép xoá");
                }
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi khi xoá");
        }
    }
    public void update(){
        if(!showError()){
            return;
        }
        String maNH = txtIDNguoiHoc.getText();
        NguoiHoc nh = getForm();
        try {
            if(MsgBox.confirm(this, "Bạn muốn cập nhật người học này ?")){
                dao.update(nh);
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
        row = tblDSNH.getRowCount()-1;
        edit();
    }
    public void next(){
        row++;
        edit();
    }
    public void tim(){
        fillToTable();
        txtFind.setText(null);
        clear();
        setStatus(false);
    }
    private File chonFileExcelImportNguoiHoc(){
        File excelFile = null;
        JFileChooser chooser = new JFileChooser();
        if(chooser.showOpenDialog(this)==JFileChooser.APPROVE_OPTION){
            File file = chooser.getSelectedFile();
            excelFile = XImage.saveExcel(file);
        }
        return excelFile;
    }
    private void importNguoiHocFromExcel(File excelFile) {
        NguoiHoc nh = new NguoiHoc();
        try {
            FileInputStream file = new FileInputStream(excelFile);
            HSSFWorkbook workbook = new HSSFWorkbook(file);
            HSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next();
            boolean isError = false;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                nh.setMaNH(row.getCell(0).getStringCellValue());
                nh.setHoTen(row.getCell(1).getStringCellValue());
                nh.setDienThoai(row.getCell(4).getStringCellValue());
                if (row.getCell(2).getStringCellValue() == "Nam") {
                    nh.setGioiTinh(true);
                } else {
                    nh.setGioiTinh(false);
                }
                nh.setEmail(row.getCell(5).getStringCellValue());
                nh.setNgaySinh(XDate.toDate(row.getCell(3).getStringCellValue(), "MM/dd/yyyy"));
                nh.setNgayDK(new Date());
                nh.setMaNV(Auth.user.getMaNV());
                if(dao.selectById(nh.getMaNH())!=null){
                    logger.error("Maybe records were duplicated!");
                    isError = true;
                    break;
                }else{                    
                    dao.insert(nh);
                }    
            }
            if(isError==false){
                MsgBox.alert(this, "Import danh sách người học thành công");
            }
            file.close();
            fillToTable();
        } catch (Exception e) {
            e.printStackTrace();
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

        bgrGender = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tabNH = new javax.swing.JTabbedPane();
        tabCapnhat = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtBirthday = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtGhiChu = new javax.swing.JTextArea();
        pnlControl = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        btnImport = new javax.swing.JButton();
        pnlDirect = new javax.swing.JPanel();
        btnFirst = new javax.swing.JButton();
        btnPrevious = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        txtIDNguoiHoc = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtNameNguoiHoc = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtPhoneNumber = new javax.swing.JTextField();
        rdoNam = new javax.swing.JRadioButton();
        rdoNu = new javax.swing.JRadioButton();
        tabDanhsach = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDSNH = new javax.swing.JTable();
        pnlFind = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtFind = new javax.swing.JTextField();
        btnFind = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Source Sans Pro Black", 0, 18)); // NOI18N
        jLabel1.setText("QUẢN LÝ NGƯỜI HỌC");

        jLabel3.setText("Ngày sinh:");

        jLabel4.setText("Địa chỉ email:");

        jLabel7.setText("Mã người học:");

        txtGhiChu.setColumns(20);
        txtGhiChu.setRows(5);
        jScrollPane2.setViewportView(txtGhiChu);

        pnlControl.setLayout(new java.awt.GridLayout(1, 5, 5, 0));

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

        btnImport.setText("Import");
        btnImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportActionPerformed(evt);
            }
        });
        pnlControl.add(btnImport);

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

        jLabel8.setText("Họ và tên:");

        jLabel9.setText("Giới tính:");

        jLabel10.setText("Ghi chú:");

        jLabel5.setText("Điện thoại:");

        bgrGender.add(rdoNam);
        rdoNam.setText("Nam");

        bgrGender.add(rdoNu);
        rdoNu.setText("Nữ");

        javax.swing.GroupLayout tabCapnhatLayout = new javax.swing.GroupLayout(tabCapnhat);
        tabCapnhat.setLayout(tabCapnhatLayout);
        tabCapnhatLayout.setHorizontalGroup(
            tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabCapnhatLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNameNguoiHoc)
                    .addComponent(txtIDNguoiHoc)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabCapnhatLayout.createSequentialGroup()
                        .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9)
                            .addComponent(jLabel7)
                            .addComponent(jLabel5)
                            .addGroup(tabCapnhatLayout.createSequentialGroup()
                                .addComponent(rdoNam)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rdoNu))
                            .addComponent(txtPhoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtBirthday, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEmail, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)))
                    .addGroup(tabCapnhatLayout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(tabCapnhatLayout.createSequentialGroup()
                        .addComponent(pnlControl, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pnlDirect, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        tabCapnhatLayout.setVerticalGroup(
            tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabCapnhatLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtIDNguoiHoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNameNguoiHoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBirthday, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdoNam)
                    .addComponent(rdoNu))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPhoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addGroup(tabCapnhatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlControl, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlDirect, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );

        tabNH.addTab("CẬP NHẬT", tabCapnhat);

        tblDSNH.setModel(new javax.swing.table.DefaultTableModel(
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
        tblDSNH.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDSNHMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblDSNH);

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
                .addComponent(txtFind)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnFind)
                .addGap(14, 14, 14))
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

        javax.swing.GroupLayout tabDanhsachLayout = new javax.swing.GroupLayout(tabDanhsach);
        tabDanhsach.setLayout(tabDanhsachLayout);
        tabDanhsachLayout.setHorizontalGroup(
            tabDanhsachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabDanhsachLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabDanhsachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlFind, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE))
                .addContainerGap())
        );
        tabDanhsachLayout.setVerticalGroup(
            tabDanhsachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabDanhsachLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlFind, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabNH.addTab("DANH SÁCH", tabDanhsach);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tabNH)
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
                .addComponent(tabNH)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clear();
    }//GEN-LAST:event_btnClearActionPerformed

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

    private void tblDSNHMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDSNHMousePressed
        if(evt.getClickCount()==2){
            row = tblDSNH.rowAtPoint(evt.getPoint());
            edit();
            tabNH.setSelectedIndex(0);
        }
    }//GEN-LAST:event_tblDSNHMousePressed

    private void btnFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindActionPerformed
        tim();
    }//GEN-LAST:event_btnFindActionPerformed

    private void btnImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportActionPerformed
        File file = this.chonFileExcelImportNguoiHoc();
        if(file==null){
            MsgBox.alert(this, "Lỗi đọc tin Excel");
        }else{
            this.importNguoiHocFromExcel(file);
        }
    }//GEN-LAST:event_btnImportActionPerformed

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
            java.util.logging.Logger.getLogger(QLNguoiHocJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QLNguoiHocJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QLNguoiHocJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QLNguoiHocJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                QLNguoiHocJDialog dialog = new QLNguoiHocJDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.ButtonGroup bgrGender;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnFind;
    private javax.swing.JButton btnFirst;
    private javax.swing.JButton btnImport;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrevious;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel pnlControl;
    private javax.swing.JPanel pnlDirect;
    private javax.swing.JPanel pnlFind;
    private javax.swing.JRadioButton rdoNam;
    private javax.swing.JRadioButton rdoNu;
    private javax.swing.JPanel tabCapnhat;
    private javax.swing.JPanel tabDanhsach;
    private javax.swing.JTabbedPane tabNH;
    private javax.swing.JTable tblDSNH;
    private javax.swing.JTextField txtBirthday;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtFind;
    private javax.swing.JTextArea txtGhiChu;
    private javax.swing.JTextField txtIDNguoiHoc;
    private javax.swing.JTextField txtNameNguoiHoc;
    private javax.swing.JTextField txtPhoneNumber;
    // End of variables declaration//GEN-END:variables
}
