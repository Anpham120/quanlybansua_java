package gui;

import dao.SanPhamDAO;
import model.SanPham;
import utils.AppConstants;
import utils.EventBus;
import utils.InputFilter;
import utils.TableHelper;
import utils.UIConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Panel quản lý sản phẩm sữa: Hiển thị bảng, CRUD, tìm kiếm, lọc theo loại.
 * Phân quyền: Nhân viên chỉ xem + tìm kiếm, Quản lý full CRUD.
 * 
 * @author Bùi Đào Đức Anh - BIT240025
 */
public class SanPhamPanel extends JPanel {

    private String vaiTro;
    private SanPhamDAO sanPhamDAO = new SanPhamDAO();
    private JTable bangSanPham;
    private DefaultTableModel modelBang;

    // Form nhập liệu
    private JTextField txtTenSP, txtThuongHieu, txtDonGia, txtSoLuong;
    private JSpinner spinNSX, spinHSD;
    private JComboBox<String> cboLoai;
    private JTextField txtTimKiem;
    private JComboBox<String> cboLocLoai;

    // Nút CRUD
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;

    private DecimalFormat dinhDangTien = AppConstants.FORMAT_TIEN;

    private static final String[] DANH_SACH_LOAI = {
        "Sữa tươi", "Sữa bột", "Sữa chua", "Sữa hộp", "Sữa đặc", "Sữa hạt"
    };

    public SanPhamPanel(String vaiTro) {
        this.vaiTro = vaiTro;
        setLayout(new BorderLayout(0, 10));
        setBackground(UIConstants.BG_MAIN);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // === TIÊU ĐỀ ===
        JLabel lblTieuDe = new JLabel("QUẢN LÝ SẢN PHẨM SỮA");
        lblTieuDe.setFont(UIConstants.FONT_TITLE);
        lblTieuDe.setForeground(UIConstants.PRIMARY);
        add(lblTieuDe, BorderLayout.NORTH);

        // === PHẦN TRÊN: Form nhập liệu ===
        JPanel panelForm = taoPanelForm();
        
        // === PHẦN DƯỚI: Bảng ===
        JPanel panelBang = taoPanelBang();

        // SplitPane chia trên/dưới
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelForm, panelBang);
        splitPane.setDividerLocation(200);
        splitPane.setResizeWeight(0.0);
        add(splitPane, BorderLayout.CENTER);

        // Tải dữ liệu
        taiDuLieu();

        // Đăng ký lắng nghe sự kiện: tự cập nhật bảng khi thanh toán trừ kho
        EventBus.subscribe(EventBus.SU_KIEN_CAP_NHAT_SAN_PHAM, data -> taiDuLieu());
    }

    // ===== TẠO FORM NHẬP LIỆU =====
    private JPanel taoPanelForm() {
        JPanel panelChua = new JPanel(new BorderLayout(0, 10));
        panelChua.setBackground(UIConstants.BG_CARD);
        panelChua.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_COLOR),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        // --- Thanh tìm kiếm + lọc ---
        JPanel panelTimKiem = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelTimKiem.setBackground(UIConstants.BG_CARD);

        panelTimKiem.add(new JLabel("Tìm kiếm:"));
        txtTimKiem = new JTextField(15);
        txtTimKiem.setFont(UIConstants.FONT_TABLE);
        panelTimKiem.add(txtTimKiem);

        panelTimKiem.add(new JLabel("Loại:"));
        cboLocLoai = new JComboBox<>();
        cboLocLoai.addItem("Tất cả");
        for (String loai : DANH_SACH_LOAI) cboLocLoai.addItem(loai);
        cboLocLoai.setFont(UIConstants.FONT_TABLE);
        panelTimKiem.add(cboLocLoai);

        JButton btnTimKiem = new JButton("Tìm");
        btnTimKiem.setFont(UIConstants.FONT_TABLE);
        btnTimKiem.setBackground(UIConstants.INFO);
        btnTimKiem.setForeground(Color.WHITE);
        btnTimKiem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTimKiem.addActionListener(e -> timKiem());
        panelTimKiem.add(btnTimKiem);

        // Nhấn Enter trong ô tìm kiếm cũng tìm
        txtTimKiem.addActionListener(e -> timKiem());

        panelChua.add(panelTimKiem, BorderLayout.NORTH);

        // --- Form nhập liệu (2 hàng x 4 cột) ---
        JPanel panelNhap = new JPanel(new GridBagLayout());
        panelNhap.setBackground(UIConstants.BG_CARD);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Hàng 1: Tên SP | Loại | Thương hiệu | Đơn giá
        String[] nhan1 = {"Tên SP:", "Loại:", "Thương hiệu:", "Đơn giá:"};
        txtTenSP = new JTextField(12);
        cboLoai = new JComboBox<>(DANH_SACH_LOAI);
        txtThuongHieu = new JTextField(12);
        txtDonGia = new JTextField(10);

        JComponent[] truong1 = {txtTenSP, cboLoai, txtThuongHieu, txtDonGia};

        for (int i = 0; i < nhan1.length; i++) {
            gbc.gridx = i * 2;
            gbc.gridy = 0;
            gbc.weightx = 0;
            JLabel lbl = new JLabel(nhan1[i]);
            lbl.setFont(UIConstants.FONT_TABLE);
            panelNhap.add(lbl, gbc);

            gbc.gridx = i * 2 + 1;
            gbc.weightx = 1.0;
            if (truong1[i] instanceof JTextField) ((JTextField) truong1[i]).setFont(UIConstants.FONT_TABLE);
            else if (truong1[i] instanceof JComboBox) ((JComboBox<?>) truong1[i]).setFont(UIConstants.FONT_TABLE);
            panelNhap.add(truong1[i], gbc);
        }

        // Hàng 2: Số lượng | NSX | HSD
        txtSoLuong = new JTextField(10);
        txtSoLuong.setFont(UIConstants.FONT_TABLE);

        // Chặn nhập chữ vào ô Số lượng (chỉ cho nhập số)
        InputFilter.applyDigitsOnly(txtSoLuong);

        // Chặn nhập chữ vào ô Đơn giá (chỉ cho nhập số và dấu chấm phân cách)
        InputFilter.applyDigitsAndDot(txtDonGia);

        // JSpinner chọn ngày
        spinNSX = taoSpinnerNgay();
        spinHSD = taoSpinnerNgay();

        // Tự động format giá REAL-TIME khi gõ (20000 → 20.000)
        txtDonGia.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private boolean dangCapNhat = false;

            public void insertUpdate(javax.swing.event.DocumentEvent e) { formatGia(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { formatGia(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) {}

            private void formatGia() {
                if (dangCapNhat) return;
                SwingUtilities.invokeLater(() -> {
                    dangCapNhat = true;
                    try {
                        String text = txtDonGia.getText().replace(".", "").trim();
                        if (text.isEmpty()) { dangCapNhat = false; return; }
                        // Chỉ giữ ký tự số
                        String soOnly = text.replaceAll("[^0-9]", "");
                        if (soOnly.isEmpty()) { dangCapNhat = false; return; }
                        long gia = Long.parseLong(soOnly);
                        String formatted = dinhDangTien.format(gia);
                        // Đặt lại text và giữ con trỏ ở cuối
                        txtDonGia.setText(formatted);
                        txtDonGia.setCaretPosition(formatted.length());
                    } catch (Exception ex) { /* giữ nguyên */ }
                    dangCapNhat = false;
                });
            }
        });

        String[] nhan2 = {"Số lượng:", "NSX:", "HSD:"};
        JComponent[] truong2 = {txtSoLuong, spinNSX, spinHSD};

        for (int i = 0; i < nhan2.length; i++) {
            gbc.gridx = i * 2;
            gbc.gridy = 1;
            gbc.weightx = 0;
            JLabel lbl = new JLabel(nhan2[i]);
            lbl.setFont(UIConstants.FONT_TABLE);
            panelNhap.add(lbl, gbc);

            gbc.gridx = i * 2 + 1;
            gbc.weightx = 1.0;
            panelNhap.add(truong2[i], gbc);
        }

        panelChua.add(panelNhap, BorderLayout.CENTER);

        // --- Nút CRUD ---
        JPanel panelNut = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelNut.setBackground(UIConstants.BG_CARD);

        btnThem = taoNutChucNang("Thêm", UIConstants.SUCCESS);
        btnSua = taoNutChucNang("Sửa", UIConstants.WARNING);
        btnXoa = taoNutChucNang("Xóa", UIConstants.DANGER);
        btnLamMoi = taoNutChucNang("Làm mới", UIConstants.INFO);

        btnThem.addActionListener(e -> themSanPham());
        btnSua.addActionListener(e -> suaSanPham());
        btnXoa.addActionListener(e -> xoaSanPham());
        btnLamMoi.addActionListener(e -> lamMoi());

        panelNut.add(btnThem);
        panelNut.add(btnSua);
        panelNut.add(btnXoa);
        panelNut.add(btnLamMoi);

        // Phân quyền: Nhân viên ẩn nút Thêm/Sửa/Xóa
        if (!vaiTro.equals(AppConstants.VAI_TRO_QUAN_LY)) {
            btnThem.setVisible(false);
            btnSua.setVisible(false);
            btnXoa.setVisible(false);
            // Ẩn form nhập liệu cho nhân viên
            panelNhap.setVisible(false);
        }

        panelChua.add(panelNut, BorderLayout.SOUTH);
        return panelChua;
    }

    // ===== TẠO BẢNG SẢN PHẨM =====
    private JPanel taoPanelBang() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIConstants.BG_CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_COLOR),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        String[] cotBang = {"STT", "Mã SP", "Tên sản phẩm", "Loại", "Thương hiệu", "Đơn giá", "Số lượng", "NSX", "HSD"};
        modelBang = new DefaultTableModel(cotBang, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        bangSanPham = new JTable(modelBang);
        bangSanPham.setFont(UIConstants.FONT_TABLE);
        bangSanPham.setRowHeight(32);
        bangSanPham.getTableHeader().setFont(UIConstants.FONT_TABLE_HEADER);
        bangSanPham.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Độ rộng cột
        int[] doRong = {40, 40, 200, 90, 120, 100, 70, 90, 90};
        for (int i = 0; i < doRong.length; i++) {
            bangSanPham.getColumnModel().getColumn(i).setPreferredWidth(doRong[i]);
        }

        // Căn phải cột Đơn giá (index 4), căn giữa cột Số lượng (index 5) và ID (index 0)
        DefaultTableCellRenderer canPhai = new DefaultTableCellRenderer();
        canPhai.setHorizontalAlignment(SwingConstants.RIGHT);
        bangSanPham.getColumnModel().getColumn(4).setCellRenderer(canPhai);

        DefaultTableCellRenderer canGiua = new DefaultTableCellRenderer();
        canGiua.setHorizontalAlignment(SwingConstants.CENTER);
        bangSanPham.getColumnModel().getColumn(0).setCellRenderer(canGiua);
        bangSanPham.getColumnModel().getColumn(5).setCellRenderer(canGiua);

        // Click vào bảng → điền form
        bangSanPham.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                dienFormTuBang();
            }
        });

        JScrollPane scrollPane = new JScrollPane(bangSanPham);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    // ===== CÁC PHƯƠNG THỨC CHỨC NĂNG =====

    private void taiDuLieu() {
        modelBang.setRowCount(0);
        List<SanPham> ds = sanPhamDAO.layTatCa();
        int stt = 1;
        for (SanPham sp : ds) {
            themDongBang(stt++, sp);
        }
    }

    private void timKiem() {
        String tuKhoa = txtTimKiem.getText().trim();
        String loai = (String) cboLocLoai.getSelectedItem();
        modelBang.setRowCount(0);
        List<SanPham> ds = sanPhamDAO.timKiem(tuKhoa, loai);
        int stt = 1;
        for (SanPham sp : ds) {
            themDongBang(stt++, sp);
        }
    }

    private void themSanPham() {
        if (!kiemTraForm()) return;

        // Kiểm tra trùng tên
        String tenSP = txtTenSP.getText().trim();
        if (sanPhamDAO.kiemTraTrung(tenSP, 0)) {
            JOptionPane.showMessageDialog(this,
                "Sản phẩm \"" + tenSP + "\" đã tồn tại trong hệ thống!",
                "Trùng sản phẩm", JOptionPane.WARNING_MESSAGE);
            txtTenSP.requestFocus();
            return;
        }

        SanPham sp = docTuForm();
        if (sanPhamDAO.them(sp)) {
            JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm sản phẩm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void suaSanPham() {
        int dong = bangSanPham.getSelectedRow();
        if (dong < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần sửa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!kiemTraForm()) return;

        int id = Integer.parseInt(modelBang.getValueAt(dong, 1).toString().replace("SP-", ""));

        // Kiểm tra trùng tên (bỏ qua chính nó)
        String tenSP = txtTenSP.getText().trim();
        if (sanPhamDAO.kiemTraTrung(tenSP, id)) {
            JOptionPane.showMessageDialog(this,
                "Sản phẩm \"" + tenSP + "\" đã tồn tại trong hệ thống!",
                "Trùng sản phẩm", JOptionPane.WARNING_MESSAGE);
            txtTenSP.requestFocus();
            return;
        }

        SanPham sp = docTuForm();
        sp.setId(id);

        if (sanPhamDAO.capNhat(sp)) {
            JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xoaSanPham() {
        int dong = bangSanPham.getSelectedRow();
        if (dong < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = Integer.parseInt(modelBang.getValueAt(dong, 1).toString().replace("SP-", ""));
        String tenSP = (String) modelBang.getValueAt(dong, 2);

        // Kiểm tra xem sản phẩm có trong hóa đơn không
        boolean coTrongHoaDon = sanPhamDAO.kiemTraLienKetHoaDon(id);
        String thongBao;
        if (coTrongHoaDon) {
            thongBao = "Sản phẩm \"" + tenSP + "\" đang có trong hóa đơn.\n"
                     + "Nếu xóa, các chi tiết hóa đơn liên quan cũng sẽ bị xóa.\n\n"
                     + "Bạn có chắc chắn muốn xóa?";
        } else {
            thongBao = "Bạn có chắc muốn xóa sản phẩm \"" + tenSP + "\"?";
        }

        int xacNhan = JOptionPane.showConfirmDialog(this,
            thongBao,
            "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (xacNhan == JOptionPane.YES_OPTION) {
            if (sanPhamDAO.xoaCoLienKet(id)) {
                JOptionPane.showMessageDialog(this, "Xóa sản phẩm thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                lamMoi();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa sản phẩm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void lamMoi() {
        txtTenSP.setText("");
        txtThuongHieu.setText("");
        txtDonGia.setText("");
        txtSoLuong.setText("");

        spinNSX.setValue(new java.util.Date());
        spinHSD.setValue(new java.util.Date());
        cboLoai.setSelectedIndex(0);
        txtTimKiem.setText("");
        cboLocLoai.setSelectedIndex(0);
        bangSanPham.clearSelection();
        taiDuLieu();
    }

    // ===== HỖ TRỢ =====

    private void themDongBang(int stt, SanPham sp) {
        modelBang.addRow(new Object[]{
            stt,
            String.format("SP-%04d", sp.getId()),
            sp.getTenSP(),
            sp.getLoai(),
            sp.getThuongHieu(),
            dinhDangTien.format(sp.getDonGia()) + "đ",
            sp.getSoLuong(),
            sp.getNgaySanXuat() != null ? sp.getNgaySanXuat().toString() : "",
            sp.getHanSuDung() != null ? sp.getHanSuDung().toString() : ""
        });
    }

    private void dienFormTuBang() {
        int dong = bangSanPham.getSelectedRow();
        if (dong < 0) return;

        txtTenSP.setText((String) modelBang.getValueAt(dong, 2));
        String loai = (String) modelBang.getValueAt(dong, 3);
        cboLoai.setSelectedItem(loai);
        txtThuongHieu.setText((String) modelBang.getValueAt(dong, 4));

        String giaStr = ((String) modelBang.getValueAt(dong, 5)).replace("đ", "").replace(".", "");
        txtDonGia.setText(dinhDangTien.format(Double.parseDouble(giaStr)));

        txtSoLuong.setText(String.valueOf(modelBang.getValueAt(dong, 6)));

        // Set spinner ngày từ bảng
        String nsxStr = (String) modelBang.getValueAt(dong, 7);
        String hsdStr = (String) modelBang.getValueAt(dong, 8);
        try {
            if (!nsxStr.isEmpty()) spinNSX.setValue(Date.valueOf(nsxStr));
        } catch (Exception e) { /* bỏ qua */ }
        try {
            if (!hsdStr.isEmpty()) spinHSD.setValue(Date.valueOf(hsdStr));
        } catch (Exception e) { /* bỏ qua */ }
    }

    private boolean kiemTraForm() {
        if (txtTenSP.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên sản phẩm!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtTenSP.requestFocus();
            return false;
        }
        if (txtDonGia.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đơn giá!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtDonGia.requestFocus();
            return false;
        }
        try {
            double gia = Double.parseDouble(txtDonGia.getText().trim().replace(".", ""));
            if (gia <= 0) {
                JOptionPane.showMessageDialog(this, "Đơn giá phải lớn hơn 0!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                txtDonGia.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Đơn giá phải là số!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtDonGia.requestFocus();
            return false;
        }
        if (txtSoLuong.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtSoLuong.requestFocus();
            return false;
        }
        try {
            int sl = Integer.parseInt(txtSoLuong.getText().trim());
            if (sl < 0) {
                JOptionPane.showMessageDialog(this, "Số lượng không được âm!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                txtSoLuong.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtSoLuong.requestFocus();
            return false;
        }

        // Kiểm tra thương hiệu bắt buộc
        if (txtThuongHieu.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập thương hiệu!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtThuongHieu.requestFocus();
            return false;
        }

        // Kiểm tra HSD phải sau NSX (JSpinner luôn có giá trị hợp lệ)
        java.util.Date ngayNSX = (java.util.Date) spinNSX.getValue();
        java.util.Date ngayHSD = (java.util.Date) spinHSD.getValue();
        if (!ngayHSD.after(ngayNSX)) {
            JOptionPane.showMessageDialog(this, "Hạn sử dụng phải sau ngày sản xuất!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            spinHSD.requestFocus();
            return false;
        }

        return true;
    }

    private SanPham docTuForm() {
        SanPham sp = new SanPham();
        sp.setTenSP(txtTenSP.getText().trim());
        sp.setLoai((String) cboLoai.getSelectedItem());
        sp.setThuongHieu(txtThuongHieu.getText().trim());
        sp.setDonGia(Double.parseDouble(txtDonGia.getText().trim().replace(".", "")));
        sp.setSoLuong(Integer.parseInt(txtSoLuong.getText().trim()));
        sp.setMoTa("");

        // Lấy ngày từ JSpinner
        sp.setNgaySanXuat(new Date(((java.util.Date) spinNSX.getValue()).getTime()));
        sp.setHanSuDung(new Date(((java.util.Date) spinHSD.getValue()).getTime()));

        return sp;
    }

    /** Tạo JSpinner chọn ngày với định dạng dd/MM/yyyy. */
    private JSpinner taoSpinnerNgay() {
        SpinnerDateModel model = new SpinnerDateModel();
        JSpinner spinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "dd/MM/yyyy");
        spinner.setEditor(editor);
        editor.getTextField().setFont(UIConstants.FONT_TABLE);
        return spinner;
    }

    private JButton taoNutChucNang(String text, Color mauNen) {
        return TableHelper.createSmallButton(text, mauNen);
    }
}
