package gui;

import dao.NhanVienDAO;
import dao.TaiKhoanDAO;
import model.NhanVien;
import model.TaiKhoan;
import utils.InputFilter;
import utils.TableHelper;
import utils.UIConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

/**
 * Panel quản lý nhân viên: Bảng, CRUD, tìm kiếm.
 * Phân quyền: Quản lý = Full CRUD, Nhân viên = Ẩn hoàn toàn (xử lý ở MainFrame).
 * 
 * @author Đỗ Tuấn Anh - BIT240015
 */
public class NhanVienPanel extends JPanel {

    @SuppressWarnings("unused")
    private String vaiTro;
    private NhanVienDAO nhanVienDAO = new NhanVienDAO();
    private TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();
    private JTable bangNV;
    private DefaultTableModel modelBang;

    private JTextField txtHoTen, txtSDT, txtDiaChi;
    private JSpinner spinNgaySinh;
    private JComboBox<String> cboGioiTinh;
    private JComboBox<Object> cboTaiKhoan; // ComboBox chọn TK đăng nhập
    private JTextField txtTimKiem;
    private javax.swing.border.Border borderMacDinh; // Border gốc của JTextField
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;

    public NhanVienPanel(String vaiTro) {
        this.vaiTro = vaiTro;
        setLayout(new BorderLayout(0, 10));
        setBackground(UIConstants.BG_MAIN);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Tiêu đề
        JLabel lblTieuDe = new JLabel("QUẢN LÝ NHÂN VIÊN");
        lblTieuDe.setFont(UIConstants.FONT_TITLE);
        lblTieuDe.setForeground(UIConstants.PRIMARY);
        add(lblTieuDe, BorderLayout.NORTH);

        // Form + Bảng
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, taoPanelForm(), taoPanelBang());
        splitPane.setDividerLocation(210);
        splitPane.setResizeWeight(0.0);
        add(splitPane, BorderLayout.CENTER);

        taiDSTaiKhoan(0); // Load danh sách TK chưa liên kết
        taiDuLieu();
    }

    private JPanel taoPanelForm() {
        JPanel panelChua = new JPanel(new BorderLayout(0, 10));
        panelChua.setBackground(UIConstants.BG_CARD);
        panelChua.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_COLOR),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        // Thanh tìm kiếm
        JPanel panelTimKiem = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelTimKiem.setBackground(UIConstants.BG_CARD);
        panelTimKiem.add(new JLabel("Tìm kiếm (tên/SĐT):"));
        txtTimKiem = new JTextField(20);
        txtTimKiem.setFont(UIConstants.FONT_TABLE);
        txtTimKiem.addActionListener(e -> timKiem());
        panelTimKiem.add(txtTimKiem);

        JButton btnTimKiem = UIConstants.createStyledButton("Tìm", UIConstants.INFO);
        btnTimKiem.addActionListener(e -> timKiem());
        panelTimKiem.add(btnTimKiem);
        panelChua.add(panelTimKiem, BorderLayout.NORTH);

        // Form nhập liệu (2 hàng)
        JPanel panelNhap = new JPanel(new GridBagLayout());
        panelNhap.setBackground(UIConstants.BG_CARD);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.LINE_END;

        // Helper tạo label không bị cắt
        // Hàng 1: Họ tên | SĐT | Giới tính
        txtHoTen = new JTextField(15);  txtHoTen.setFont(UIConstants.FONT_TABLE);
        borderMacDinh = txtHoTen.getBorder(); // Lưu border gốc
        txtSDT = new JTextField(12);    txtSDT.setFont(UIConstants.FONT_TABLE);
        // Chặn nhập chữ: chỉ cho phép nhập số
        InputFilter.applyDigitsOnly(txtSDT);
        cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        cboGioiTinh.setFont(UIConstants.FONT_TABLE);

        gbc.gridy = 0;
        gbc.gridx = 0; gbc.weightx = 0;
        panelNhap.add(taoLabel("Họ tên:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.LINE_START;
        panelNhap.add(txtHoTen, gbc);

        gbc.gridx = 2; gbc.weightx = 0; gbc.anchor = GridBagConstraints.LINE_END;
        panelNhap.add(taoLabel("SĐT:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.6; gbc.anchor = GridBagConstraints.LINE_START;
        panelNhap.add(txtSDT, gbc);

        gbc.gridx = 4; gbc.weightx = 0; gbc.anchor = GridBagConstraints.LINE_END;
        panelNhap.add(taoLabel("Giới tính:"), gbc);
        gbc.gridx = 5; gbc.weightx = 0.4; gbc.anchor = GridBagConstraints.LINE_START;
        panelNhap.add(cboGioiTinh, gbc);

        // Hàng 2: Ngày sinh | Địa chỉ | Tài khoản
        spinNgaySinh = taoSpinnerNgay();
        txtDiaChi = new JTextField(20);
        txtDiaChi.setFont(UIConstants.FONT_TABLE);
        cboTaiKhoan = new JComboBox<>();
        cboTaiKhoan.setFont(UIConstants.FONT_TABLE);

        gbc.gridy = 1;
        gbc.gridx = 0; gbc.weightx = 0; gbc.anchor = GridBagConstraints.LINE_END;
        panelNhap.add(taoLabel("Ngày sinh:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.3; gbc.anchor = GridBagConstraints.LINE_START;
        panelNhap.add(spinNgaySinh, gbc);

        gbc.gridx = 2; gbc.weightx = 0; gbc.anchor = GridBagConstraints.LINE_END;
        panelNhap.add(taoLabel("Địa chỉ:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.7; gbc.anchor = GridBagConstraints.LINE_START;
        panelNhap.add(txtDiaChi, gbc);

        gbc.gridx = 4; gbc.weightx = 0; gbc.anchor = GridBagConstraints.LINE_END;
        panelNhap.add(taoLabel("Tài khoản:"), gbc);
        gbc.gridx = 5; gbc.weightx = 0.4; gbc.anchor = GridBagConstraints.LINE_START;
        panelNhap.add(cboTaiKhoan, gbc);

        panelChua.add(panelNhap, BorderLayout.CENTER);

        // Nút CRUD
        JPanel panelNut = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelNut.setBackground(UIConstants.BG_CARD);
        btnThem = TableHelper.createSmallButton("Thêm", UIConstants.SUCCESS);
        btnSua = TableHelper.createSmallButton("Sửa", UIConstants.WARNING);
        btnXoa = TableHelper.createSmallButton("Xóa", UIConstants.DANGER);
        btnLamMoi = TableHelper.createSmallButton("Làm mới", UIConstants.INFO);

        btnThem.addActionListener(e -> themNV());
        btnSua.addActionListener(e -> suaNV());
        btnXoa.addActionListener(e -> xoaNV());
        btnLamMoi.addActionListener(e -> lamMoi());

        panelNut.add(btnThem);
        panelNut.add(btnSua);
        panelNut.add(btnXoa);
        panelNut.add(btnLamMoi);

        panelChua.add(panelNut, BorderLayout.SOUTH);
        return panelChua;
    }

    private JPanel taoPanelBang() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIConstants.BG_CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_COLOR),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        String[] cotBang = {"STT", "Mã NV", "Họ tên", "SĐT", "Giới tính", "Ngày sinh", "Địa chỉ", "Tài khoản"};
        modelBang = new DefaultTableModel(cotBang, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        bangNV = new JTable(modelBang);
        bangNV.setFont(UIConstants.FONT_TABLE);
        bangNV.setRowHeight(32);
        bangNV.getTableHeader().setFont(UIConstants.FONT_TABLE_HEADER);
        bangNV.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        int[] doRong = {40, 40, 180, 120, 70, 100, 250};
        for (int i = 0; i < doRong.length; i++) {
            bangNV.getColumnModel().getColumn(i).setPreferredWidth(doRong[i]);
        }

        // Căn giữa STT, ID, Giới tính
        DefaultTableCellRenderer canGiua = new DefaultTableCellRenderer();
        canGiua.setHorizontalAlignment(SwingConstants.CENTER);
        bangNV.getColumnModel().getColumn(0).setCellRenderer(canGiua);
        bangNV.getColumnModel().getColumn(1).setCellRenderer(canGiua);
        bangNV.getColumnModel().getColumn(4).setCellRenderer(canGiua);

        bangNV.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { dienFormTuBang(); }
        });

        panel.add(new JScrollPane(bangNV), BorderLayout.CENTER);
        return panel;
    }

    // ===== CHỨC NĂNG =====

    private void taiDuLieu() {
        modelBang.setRowCount(0);
        List<TaiKhoan> dsTK = taiKhoanDAO.layTatCa(); // Load 1 lần
        int stt = 1;
        for (NhanVien nv : nhanVienDAO.layTatCa()) {
            themDongBang(stt++, nv, dsTK);
        }
    }

    private void timKiem() {
        modelBang.setRowCount(0);
        List<TaiKhoan> dsTK = taiKhoanDAO.layTatCa();
        int stt = 1;
        for (NhanVien nv : nhanVienDAO.timKiem(txtTimKiem.getText().trim())) {
            themDongBang(stt++, nv, dsTK);
        }
    }

    private void themNV() {
        if (!kiemTraForm()) return;
        String sdt = txtSDT.getText().trim();
        if (!sdt.isEmpty() && nhanVienDAO.kiemTraTrungSDT(sdt, 0)) {
            JOptionPane.showMessageDialog(this, "SĐT \"" + sdt + "\" đã tồn tại!", "Trùng SĐT", JOptionPane.WARNING_MESSAGE);
            txtSDT.requestFocus();
            return;
        }
        NhanVien nv = docTuForm();
        if (nhanVienDAO.them(nv)) {
            JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void suaNV() {
        int dong = bangNV.getSelectedRow();
        if (dong < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần sửa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!kiemTraForm()) return;
        int id = Integer.parseInt(modelBang.getValueAt(dong, 1).toString().replace("NV-", ""));
        String sdt = txtSDT.getText().trim();
        if (!sdt.isEmpty() && nhanVienDAO.kiemTraTrungSDT(sdt, id)) {
            JOptionPane.showMessageDialog(this, "SĐT \"" + sdt + "\" đã tồn tại!", "Trùng SĐT", JOptionPane.WARNING_MESSAGE);
            txtSDT.requestFocus();
            return;
        }
        NhanVien nv = docTuForm();
        nv.setId(id);
        if (nhanVienDAO.capNhat(nv)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xoaNV() {
        int dong = bangNV.getSelectedRow();
        if (dong < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = Integer.parseInt(modelBang.getValueAt(dong, 1).toString().replace("NV-", ""));
        String ten = (String) modelBang.getValueAt(dong, 2);

        boolean coLienKet = nhanVienDAO.kiemTraLienKetHoaDon(id);
        String thongBao;
        if (coLienKet) {
            thongBao = "Nhân viên \"" + ten + "\" đã tạo hóa đơn trong hệ thống.\n"
                     + "Nếu xóa, các hóa đơn liên quan sẽ hiển thị '—' thay vì tên NV.\n\n"
                     + "Bạn có chắc chắn muốn xóa?";
        } else {
            thongBao = "Bạn có chắc muốn xóa nhân viên \"" + ten + "\"?";
        }

        int xn = JOptionPane.showConfirmDialog(this, thongBao,
            "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (xn == JOptionPane.YES_OPTION) {
            if (nhanVienDAO.xoaCoLienKet(id)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                lamMoi();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void lamMoi() {
        txtHoTen.setText("");
        txtSDT.setText("");
        txtDiaChi.setText("");
        cboGioiTinh.setSelectedIndex(0);
        // Reset ngày sinh về 20 năm trước
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -20);
        spinNgaySinh.setValue(cal.getTime());
        txtTimKiem.setText("");
        bangNV.clearSelection();
        xoaVienDo(); // Reset viền đỏ
        taiDSTaiKhoan(0); // Reload TK chưa liên kết
        taiDuLieu();
    }

    /** Xóa toàn bộ viền đỏ về mặc định. */
    private void xoaVienDo() {
        txtHoTen.setBorder(borderMacDinh);
        txtSDT.setBorder(borderMacDinh);
        txtDiaChi.setBorder(borderMacDinh);
        spinNgaySinh.setBorder(borderMacDinh);
    }

    /** Đổi viền ô thành màu đỏ khi lỗi. */
    private void dauVienDo(JComponent comp) {
        comp.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
    }

    // ===== HỖ TRỢ =====

    private void themDongBang(int stt, NhanVien nv, List<TaiKhoan> dsTK) {
        // Tìm tên TK liên kết
        String tenTK = "—";
        if (nv.getIdTaiKhoan() > 0) {
            for (TaiKhoan tk : dsTK) {
                if (tk.getId() == nv.getIdTaiKhoan()) {
                    tenTK = tk.getTenDangNhap();
                    break;
                }
            }
        }
        modelBang.addRow(new Object[]{
            stt, String.format("NV-%04d", nv.getId()), nv.getHoTen(), nv.getSdt(), nv.getGioiTinh(),
            nv.getNgaySinh() != null ? nv.getNgaySinh().toString() : "",
            nv.getDiaChi(), tenTK
        });
    }

    private void dienFormTuBang() {
        int dong = bangNV.getSelectedRow();
        if (dong < 0) return;
        txtHoTen.setText((String) modelBang.getValueAt(dong, 2));
        txtSDT.setText((String) modelBang.getValueAt(dong, 3));
        String gt = (String) modelBang.getValueAt(dong, 4);
        cboGioiTinh.setSelectedItem(gt);
        String nsStr = (String) modelBang.getValueAt(dong, 5);
        try {
            if (nsStr != null && !nsStr.isEmpty()) spinNgaySinh.setValue(Date.valueOf(nsStr));
        } catch (Exception e) { /* bỏ qua */ }
        txtDiaChi.setText((String) modelBang.getValueAt(dong, 6));

        // Tìm idTaiKhoan của NV đang chọn để load ComboBox đúng
        int idNV = Integer.parseInt(modelBang.getValueAt(dong, 1).toString().replace("NV-", ""));
        int idTKHienTai = 0;
        for (NhanVien nv : nhanVienDAO.layTatCa()) {
            if (nv.getId() == idNV) { idTKHienTai = nv.getIdTaiKhoan(); break; }
        }
        taiDSTaiKhoan(idTKHienTai);
        // Chọn đúng TK hiện tại trong ComboBox
        if (idTKHienTai > 0) {
            for (int i = 1; i < cboTaiKhoan.getItemCount(); i++) {
                Object item = cboTaiKhoan.getItemAt(i);
                if (item instanceof TaiKhoan tk && tk.getId() == idTKHienTai) {
                    cboTaiKhoan.setSelectedIndex(i); break;
                }
            }
        }
    }

    private boolean kiemTraForm() {
        xoaVienDo();
        boolean hopLe = true;
        StringBuilder loi = new StringBuilder();

        // --- Họ tên ---
        String hoTen = txtHoTen.getText().trim();
        if (hoTen.isEmpty()) {
            dauVienDo(txtHoTen); loi.append("• Vui lòng nhập họ tên.\n"); hopLe = false;
        } else if (hoTen.length() < 2) {
            dauVienDo(txtHoTen); loi.append("• Họ tên cần tối thiểu 2 ký tự.\n"); hopLe = false;
        } else if (!hoTen.matches("^[\\p{L} ]+$")) {
            dauVienDo(txtHoTen);
            loi.append("• Họ tên không hợp lệ. Vui lòng không nhập số hoặc ký tự đặc biệt.\n");
            hopLe = false;
        }

        // --- SĐT ---
        String sdt = txtSDT.getText().trim();
        if (sdt.isEmpty()) {
            dauVienDo(txtSDT); loi.append("• Vui lòng nhập số điện thoại.\n"); hopLe = false;
        } else if (!sdt.matches("^0\\d{9}$")) {
            dauVienDo(txtSDT);
            loi.append("• Số điện thoại phải gồm 10 chữ số và bắt đầu bằng số 0.\n");
            hopLe = false;
        }

        // --- Địa chỉ ---
        String diaChi = txtDiaChi.getText().trim();
        if (diaChi.isEmpty()) {
            dauVienDo(txtDiaChi); loi.append("• Vui lòng nhập địa chỉ cụ thể.\n"); hopLe = false;
        } else if (diaChi.length() < 10) {
            dauVienDo(txtDiaChi);
            loi.append("• Địa chỉ cần tối thiểu 10 ký tự (ví dụ: số nhà, đường, quận).\n");
            hopLe = false;
        }

        // --- Ngày sinh (≥ 18 tuổi) ---
        java.util.Date ngaySinh = (java.util.Date) spinNgaySinh.getValue();
        Calendar calSinh = Calendar.getInstance();
        calSinh.setTime(ngaySinh);
        Calendar calToiThieu = Calendar.getInstance();
        calToiThieu.add(Calendar.YEAR, -18);
        if (!calSinh.before(calToiThieu)) {
            dauVienDo(spinNgaySinh);
            loi.append("• Nhân viên phải từ đủ 18 tuổi.\n");
            hopLe = false;
        }

        if (!hopLe) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng kiểm tra lại các thông tin chưa hợp lệ:\n\n" + loi,
                "Thông tin không hợp lệ", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private NhanVien docTuForm() {
        NhanVien nv = new NhanVien();
        nv.setHoTen(txtHoTen.getText().trim());
        nv.setSdt(txtSDT.getText().trim());
        nv.setDiaChi(txtDiaChi.getText().trim());
        nv.setGioiTinh((String) cboGioiTinh.getSelectedItem());
        nv.setNgaySinh(new Date(((java.util.Date) spinNgaySinh.getValue()).getTime()));
        // Lấy TK đã chọn từ ComboBox
        Object tkChon = cboTaiKhoan.getSelectedItem();
        if (tkChon instanceof TaiKhoan tk) {
            nv.setIdTaiKhoan(tk.getId());
        } else {
            nv.setIdTaiKhoan(0);
        }
        return nv;
    }

    /** Load danh sách TK chưa liên kết vào ComboBox. */
    private void taiDSTaiKhoan(int idTKHienTai) {
        cboTaiKhoan.removeAllItems();
        cboTaiKhoan.addItem("-- Không liên kết --");
        for (TaiKhoan tk : taiKhoanDAO.layTKChuaLienKet(idTKHienTai)) {
            cboTaiKhoan.addItem(tk);
        }
    }

    private JLabel taoLabel(String text) {
        return TableHelper.createFormLabel(text);
    }

    private JSpinner taoSpinnerNgay() {
        // Giới hạn max = ngày hiện tại - 18 năm
        Calendar calMax = Calendar.getInstance();
        calMax.add(Calendar.YEAR, -18);
        // Giá trị mặc định = 20 năm trước
        Calendar calDef = Calendar.getInstance();
        calDef.add(Calendar.YEAR, -20);

        SpinnerDateModel model = new SpinnerDateModel(
            calDef.getTime(), null, calMax.getTime(),
            Calendar.DAY_OF_MONTH
        );
        JSpinner spinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "dd/MM/yyyy");
        spinner.setEditor(editor);
        editor.getTextField().setFont(UIConstants.FONT_TABLE);
        return spinner;
    }

}
