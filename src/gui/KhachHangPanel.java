package gui;

import dao.KhachHangDAO;
import model.KhachHang;
import utils.AppConstants;
import utils.InputFilter;
import utils.TableHelper;
import utils.UIConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;

/**
 * Panel quản lý khách hàng: Bảng, CRUD, tìm kiếm.
 * Phân quyền: Quản lý = Full CRUD, Nhân viên = Xem + Thêm mới.
 * Điểm tích lũy: Read-only với Nhân viên, chỉ Quản lý mới sửa được.
 *
 * @author Đỗ Tuấn Anh - BIT240131
 */
public class KhachHangPanel extends JPanel {

    private String vaiTro;
    private KhachHangDAO khachHangDAO = new KhachHangDAO();
    private JTable bangKH;
    private DefaultTableModel modelBang;

    private JTextField txtHoTen, txtSDT, txtDiaChi, txtDiemTichLuy;
    private JTextField txtTimKiem;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;
    private javax.swing.border.Border borderMacDinh; // Border gốc của JTextField

    public KhachHangPanel(String vaiTro) {
        this.vaiTro = vaiTro;
        setLayout(new BorderLayout(0, 10));
        setBackground(UIConstants.BG_MAIN);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Tiêu đề
        JLabel lblTieuDe = new JLabel("QUẢN LÝ KHÁCH HÀNG");
        lblTieuDe.setFont(UIConstants.FONT_TITLE);
        lblTieuDe.setForeground(UIConstants.PRIMARY);
        add(lblTieuDe, BorderLayout.NORTH);

        // Form + Bảng
        JPanel panelForm = taoPanelForm();
        JPanel panelBang = taoPanelBang();
        panelForm.setMinimumSize(new Dimension(0, 185));
        panelBang.setMinimumSize(new Dimension(0, 120));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelForm, panelBang);
        splitPane.setDividerLocation(195);
        splitPane.setResizeWeight(0.0);
        add(splitPane, BorderLayout.CENTER);

        taiDuLieu();
    }

    private JPanel taoPanelForm() {
        JPanel panelChua = new JPanel(new BorderLayout(0, 8));
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

        // Form nhập liệu
        JPanel panelNhap = new JPanel(new GridBagLayout());
        panelNhap.setBackground(UIConstants.BG_CARD);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Tạo các ô ---
        txtHoTen = new JTextField(15); txtHoTen.setFont(UIConstants.FONT_TABLE);
        borderMacDinh = txtHoTen.getBorder(); // Lưu border gốc
        txtSDT   = new JTextField(12); txtSDT.setFont(UIConstants.FONT_TABLE);
        txtDiaChi = new JTextField(20); txtDiaChi.setFont(UIConstants.FONT_TABLE);
        txtDiemTichLuy = new JTextField(8);  txtDiemTichLuy.setFont(UIConstants.FONT_TABLE);
        txtDiemTichLuy.setText("0");

        // Chỉ cho nhập số vào ô SĐT
        InputFilter.applyDigitsOnly(txtSDT);

        // Điểm tích lũy: Nhân viên không được sửa, Quản lý mới sửa được
        if (!vaiTro.equals(AppConstants.VAI_TRO_QUAN_LY)) {
            txtDiemTichLuy.setEditable(false);
            txtDiemTichLuy.setBackground(new Color(240, 240, 240));
            txtDiemTichLuy.setToolTipText("Điểm tích lũy do hệ thống tự tính theo lịch sử mua hàng");
        } else {
            // QUản lý chỉ nhập số >= 0
            InputFilter.applyDigitsOnly(txtDiemTichLuy);
        }

        // Hàng 1: Họ tên | SĐT
        gbc.gridy = 0;
        gbc.gridx = 0; gbc.weightx = 0; panelNhap.add(taoLabel("Họ tên:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; panelNhap.add(txtHoTen, gbc);
        gbc.gridx = 2; gbc.weightx = 0; panelNhap.add(taoLabel("SĐT:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.7; panelNhap.add(txtSDT, gbc);

        // Hàng 2: Địa chỉ | Điểm tích lũy
        gbc.gridy = 1;
        gbc.gridx = 0; gbc.weightx = 0; panelNhap.add(taoLabel("Địa chỉ:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; panelNhap.add(txtDiaChi, gbc);
        gbc.gridx = 2; gbc.weightx = 0;

        JLabel lblDiem = taoLabel("Điểm tích lũy:");
        if (!vaiTro.equals(AppConstants.VAI_TRO_QUAN_LY)) {
            lblDiem.setForeground(Color.GRAY);
        }
        panelNhap.add(lblDiem, gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; panelNhap.add(txtDiemTichLuy, gbc);

        panelChua.add(panelNhap, BorderLayout.CENTER);

        // Nút CRUD
        JPanel panelNut = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelNut.setBackground(UIConstants.BG_CARD);
        btnThem   = TableHelper.createSmallButton("Thêm",     UIConstants.SUCCESS);
        btnSua    = TableHelper.createSmallButton("Sửa",      UIConstants.WARNING);
        btnXoa    = TableHelper.createSmallButton("Xóa",      UIConstants.DANGER);
        btnLamMoi = TableHelper.createSmallButton("Làm mới",  UIConstants.INFO);

        btnThem.addActionListener(e   -> themKH());
        btnSua.addActionListener(e    -> suaKH());
        btnXoa.addActionListener(e    -> xoaKH());
        btnLamMoi.addActionListener(e -> lamMoi());

        panelNut.add(btnThem);
        panelNut.add(btnSua);
        panelNut.add(btnXoa);
        panelNut.add(btnLamMoi);

        // Phân quyền: Nhân viên ẩn Sửa/Xóa
        if (!vaiTro.equals(AppConstants.VAI_TRO_QUAN_LY)) {
            btnSua.setVisible(false);
            btnXoa.setVisible(false);
        }

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

        String[] cotBang = {"STT", "Mã KH", "Họ tên", "SĐT", "Địa chỉ", "Điểm", "Hạng"};
        modelBang = new DefaultTableModel(cotBang, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        bangKH = new JTable(modelBang);
        bangKH.setFont(UIConstants.FONT_TABLE);
        bangKH.setRowHeight(32);
        bangKH.getTableHeader().setFont(UIConstants.FONT_TABLE_HEADER);
        bangKH.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        int[] doRong = {40, 40, 175, 110, 200, 65, 130};
        for (int i = 0; i < doRong.length; i++) {
            bangKH.getColumnModel().getColumn(i).setPreferredWidth(doRong[i]);
        }

        // Renderer màu toàn dòng theo hạng khách hàng
        bangKH.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                if (!isSelected) {
                    Object hang = table.getModel().getValueAt(row, 6);
                    if (hang != null) {
                        switch (hang.toString()) {
                            case "Kim cương" -> setBackground(new Color(197, 225, 255));
                            case "Vàng"      -> setBackground(new Color(255, 250, 190));
                            case "Bạc"       -> setBackground(new Color(235, 235, 235));
                            default          -> setBackground(Color.WHITE);
                        }
                    } else {
                        setBackground(Color.WHITE);
                    }
                    setForeground(Color.BLACK);
                }
                // Căn giữa cột STT, ID, Điểm, Hạng
                if (col == 0 || col == 1 || col == 5 || col == 6) {
                    setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }
                return this;
            }
        });

        bangKH.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { dienFormTuBang(); }
        });

        panel.add(new JScrollPane(bangKH), BorderLayout.CENTER);
        return panel;
    }

    // ===== CHỨC NĂNG =====

    private void taiDuLieu() {
        modelBang.setRowCount(0);
        int stt = 1;
        for (KhachHang kh : khachHangDAO.layTatCa()) {
            themDongBang(stt++, kh);
        }
    }

    private void timKiem() {
        String tuKhoa = txtTimKiem.getText().trim();
        modelBang.setRowCount(0);
        int stt = 1;
        for (KhachHang kh : khachHangDAO.timKiem(tuKhoa)) {
            themDongBang(stt++, kh);
        }
    }

    private void themKH() {
        if (!kiemTraForm()) return;

        // Kiểm tra trùng SĐT
        String sdt = txtSDT.getText().trim();
        if (khachHangDAO.kiemTraTrungSDT(sdt, 0)) {
            dauVienDo(txtSDT);
            JOptionPane.showMessageDialog(this,
                "Số điện thoại \"" + sdt + "\" đã tồn tại trong hệ thống!",
                "Trùng SĐT", JOptionPane.WARNING_MESSAGE);
            txtSDT.requestFocus();
            return;
        }

        KhachHang kh = docTuForm();
        if (khachHangDAO.them(kh)) {
            JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void suaKH() {
        int dong = bangKH.getSelectedRow();
        if (dong < 0) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn một khách hàng từ danh sách bên dưới để thao tác.",
                "Chưa chọn", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!kiemTraForm()) return;

        int id = Integer.parseInt(modelBang.getValueAt(dong, 1).toString().replace("KH-", ""));
        String sdt = txtSDT.getText().trim();
        if (khachHangDAO.kiemTraTrungSDT(sdt, id)) {
            dauVienDo(txtSDT);
            JOptionPane.showMessageDialog(this,
                "Số điện thoại \"" + sdt + "\" đã tồn tại trong hệ thống!",
                "Trùng SĐT", JOptionPane.WARNING_MESSAGE);
            txtSDT.requestFocus();
            return;
        }

        KhachHang kh = docTuForm();
        kh.setId(id);
        if (khachHangDAO.capNhat(kh)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xoaKH() {
        int dong = bangKH.getSelectedRow();
        if (dong < 0) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn một khách hàng từ danh sách bên dưới để thao tác.",
                "Chưa chọn", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id  = Integer.parseInt(modelBang.getValueAt(dong, 1).toString().replace("KH-", ""));
        String ten = (String) modelBang.getValueAt(dong, 2);

        // Kiểm tra liên kết hóa đơn
        boolean coLienKet = khachHangDAO.kiemTraLienKetHoaDon(id);
        String thongBao;
        if (coLienKet) {
            thongBao = "Khách hàng \"" + ten + "\" đã có lịch sử mua hàng.\n"
                     + "Nếu xóa, các hóa đơn liên quan sẽ chuyển thành 'Khách lẻ'.\n\n"
                     + "Bạn có chắc chắn muốn xóa?";
        } else {
            thongBao = "Bạn có chắc chắn muốn xóa khách hàng \"" + ten + "\" không?\nDữ liệu không thể khôi phục.";
        }

        int xn = JOptionPane.showConfirmDialog(this, thongBao,
            "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (xn != JOptionPane.YES_OPTION) return;

        if (khachHangDAO.xoaCoLienKet(id)) {
            JOptionPane.showMessageDialog(this, "Xóa thành công!");
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(this, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void lamMoi() {
        txtHoTen.setText("");
        txtSDT.setText("");
        txtDiaChi.setText("");
        txtDiemTichLuy.setText("0");
        txtTimKiem.setText("");
        bangKH.clearSelection();
        xoaVienDo();
        taiDuLieu();
    }

    // ===== VALIDATION =====

    private boolean kiemTraForm() {
        xoaVienDo();
        boolean hopLe = true;
        StringBuilder loi = new StringBuilder();

        // --- Họ tên ---
        String hoTen = txtHoTen.getText().trim();
        if (hoTen.isEmpty()) {
            dauVienDo(txtHoTen); loi.append("• Vui lòng nhập họ tên khách hàng.\n"); hopLe = false;
        } else if (hoTen.length() < 2) {
            dauVienDo(txtHoTen); loi.append("• Họ tên cần tối thiểu 2 ký tự.\n"); hopLe = false;
        } else if (!hoTen.matches("^[\\p{L} ]+$")) {
            dauVienDo(txtHoTen);
            loi.append("• Họ tên không hợp lệ. Không được chứa số hoặc ký tự đặc biệt.\n");
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

        // --- Điểm tích lũy (chỉ Quản lý mới nhập) ---
        if (vaiTro.equals(AppConstants.VAI_TRO_QUAN_LY)) {
            try {
                int diem = Integer.parseInt(txtDiemTichLuy.getText().trim());
                if (diem < 0) {
                    dauVienDo(txtDiemTichLuy);
                    loi.append("• Điểm tích lũy phải là số và không được âm.\n");
                    hopLe = false;
                }
            } catch (NumberFormatException ex) {
                dauVienDo(txtDiemTichLuy);
                loi.append("• Điểm tích lũy phải là số và không được âm.\n");
                hopLe = false;
            }
        }

        if (!hopLe) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng kiểm tra lại các thông tin chưa hợp lệ:\n\n" + loi,
                "Thông tin không hợp lệ", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    // ===== HỖ TRỢ =====

    private void themDongBang(int stt, KhachHang kh) {
        modelBang.addRow(new Object[]{
            stt, String.format("KH-%04d", kh.getId()), kh.getHoTen(), kh.getSdt(),
            kh.getDiaChi(), kh.getDiemTichLuy(), kh.getHang()
        });
    }

    private void dienFormTuBang() {
        int dong = bangKH.getSelectedRow();
        if (dong < 0) return;
        xoaVienDo();
        txtHoTen.setText((String) modelBang.getValueAt(dong, 2));
        txtSDT.setText((String) modelBang.getValueAt(dong, 3));
        txtDiaChi.setText((String) modelBang.getValueAt(dong, 4));
        txtDiemTichLuy.setText(String.valueOf(modelBang.getValueAt(dong, 5)));
    }

    private KhachHang docTuForm() {
        KhachHang kh = new KhachHang();
        kh.setHoTen(txtHoTen.getText().trim());
        kh.setSdt(txtSDT.getText().trim());
        kh.setDiaChi(txtDiaChi.getText().trim());
        try {
            kh.setDiemTichLuy(Integer.parseInt(txtDiemTichLuy.getText().trim()));
        } catch (NumberFormatException e) {
            kh.setDiemTichLuy(0);
        }
        return kh;
    }

    private void xoaVienDo() {
        txtHoTen.setBorder(borderMacDinh);
        txtSDT.setBorder(borderMacDinh);
        txtDiaChi.setBorder(borderMacDinh);
        txtDiemTichLuy.setBorder(borderMacDinh);
    }

    private void dauVienDo(JComponent comp) {
        comp.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
    }

    private JLabel taoLabel(String text) {
        return TableHelper.createFormLabel(text);
    }
}
