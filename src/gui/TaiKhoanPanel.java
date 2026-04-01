package gui;

import dao.TaiKhoanDAO;
import model.TaiKhoan;
import utils.UIConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

/**
 * Màn hình quản lý tài khoản: thêm, sửa, xóa tài khoản đăng nhập.
 * 
 * @author Phạm Duy An - BIT240002
 */
public class TaiKhoanPanel extends JPanel {

    private JTextField txtTenDangNhap;
    private JPasswordField txtMatKhau;
    private JComboBox<String> cboVaiTro;
    private JTable bangTaiKhoan;
    private DefaultTableModel moHinhBang;
    private TaiKhoanDAO taiKhoanDAO;
    private int idDangChon = -1;
    private JTextField txtTimKiem;

    public TaiKhoanPanel() {
        taiKhoanDAO = new TaiKhoanDAO();
        setLayout(new BorderLayout(10, 10));
        setBackground(UIConstants.BG_MAIN);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        khoiTaoGiaoDien();
        taiDuLieu("");
    }

    private void khoiTaoGiaoDien() {
        // ===== TIÊU ĐỀ =====
        JLabel lblTieuDe = new JLabel("Quản lý Tài khoản");
        lblTieuDe.setFont(UIConstants.FONT_TITLE);
        lblTieuDe.setForeground(UIConstants.PRIMARY);
        lblTieuDe.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // ===== PANEL TRÊN (Form + Nút) =====
        JPanel panelTren = new JPanel(new BorderLayout(10, 10));
        panelTren.setBackground(Color.WHITE);
        panelTren.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(20, 25, 15, 25)
        ));

        // --- Form nhập liệu (2 hàng) ---
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Hàng 1: Tên đăng nhập + Vai trò
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel lblTenDN = new JLabel("Tên đăng nhập:");
        lblTenDN.setFont(UIConstants.FONT_LABEL);
        panelForm.add(lblTenDN, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        txtTenDangNhap = new JTextField();
        txtTenDangNhap.setFont(UIConstants.FONT_INPUT);
        txtTenDangNhap.setPreferredSize(new Dimension(200, 32));
        panelForm.add(txtTenDangNhap, gbc);

        gbc.gridx = 2; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel lblVaiTro = new JLabel("Vai trò:");
        lblVaiTro.setFont(UIConstants.FONT_LABEL);
        panelForm.add(lblVaiTro, gbc);

        gbc.gridx = 3; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 0.5;
        cboVaiTro = new JComboBox<>(new String[]{"Nhân viên", "Quản lý"});
        cboVaiTro.setFont(UIConstants.FONT_INPUT);
        cboVaiTro.setPreferredSize(new Dimension(150, 32));
        panelForm.add(cboVaiTro, gbc);

        // Hàng 2: Mật khẩu
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel lblMK = new JLabel("Mật khẩu:");
        lblMK.setFont(UIConstants.FONT_LABEL);
        panelForm.add(lblMK, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        txtMatKhau = new JPasswordField();
        txtMatKhau.setFont(UIConstants.FONT_INPUT);
        txtMatKhau.setPreferredSize(new Dimension(200, 32));
        panelForm.add(txtMatKhau, gbc);

        panelTren.add(panelForm, BorderLayout.CENTER);

        // --- Panel nút bấm ---
        JPanel panelNut = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelNut.setBackground(Color.WHITE);
        panelNut.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, UIConstants.BORDER_COLOR),
                BorderFactory.createEmptyBorder(10, 0, 0, 0)
        ));

        JButton btnThem = UIConstants.createStyledButton("Thêm", UIConstants.SUCCESS);
        JButton btnSua = UIConstants.createStyledButton("Sửa", UIConstants.WARNING);
        JButton btnXoa = UIConstants.createStyledButton("Xóa", UIConstants.DANGER);
        JButton btnLamMoi = UIConstants.createStyledButton("Làm mới", UIConstants.PRIMARY);

        panelNut.add(btnThem);
        panelNut.add(btnSua);
        panelNut.add(btnXoa);
        panelNut.add(btnLamMoi);

        panelTren.add(panelNut, BorderLayout.SOUTH);

        // ===== PANEL TRÊN GỘP (Tiêu đề + Form) =====
        JPanel panelTrenGop = new JPanel(new BorderLayout());
        panelTrenGop.setBackground(UIConstants.BG_MAIN);
        panelTrenGop.add(lblTieuDe, BorderLayout.NORTH);
        panelTrenGop.add(panelTren, BorderLayout.CENTER);

        // ===== THANH TÌM KIẾM =====
        JPanel panelTimKiem = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        panelTimKiem.setBackground(UIConstants.BG_MAIN);

        JLabel lblTimKiem = new JLabel("Tìm kiếm:");
        lblTimKiem.setFont(UIConstants.FONT_LABEL);
        panelTimKiem.add(lblTimKiem);

        txtTimKiem = new JTextField(20);
        txtTimKiem.setFont(UIConstants.FONT_INPUT);
        txtTimKiem.setPreferredSize(new Dimension(200, 32));
        txtTimKiem.addActionListener(e -> taiDuLieu(txtTimKiem.getText().trim()));
        panelTimKiem.add(txtTimKiem);

        JButton btnTimKiem = UIConstants.createStyledButton("Tìm", UIConstants.INFO);
        btnTimKiem.addActionListener(e -> taiDuLieu(txtTimKiem.getText().trim()));
        panelTimKiem.add(btnTimKiem);

        panelTrenGop.add(panelTimKiem, BorderLayout.SOUTH);
        add(panelTrenGop, BorderLayout.NORTH);

        // ===== BẢNG DỮ LIỆU =====
        String[] tenCot = {"STT", "Mã TK", "Tên đăng nhập", "Mật khẩu", "Vai trò"};
        moHinhBang = new DefaultTableModel(tenCot, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        bangTaiKhoan = new JTable(moHinhBang);
        bangTaiKhoan.setFont(UIConstants.FONT_TABLE);
        bangTaiKhoan.setRowHeight(32);
        bangTaiKhoan.setSelectionBackground(UIConstants.PRIMARY_LIGHT);
        bangTaiKhoan.setSelectionForeground(UIConstants.TEXT_DARK);
        bangTaiKhoan.setGridColor(UIConstants.BORDER_COLOR);
        bangTaiKhoan.setShowGrid(true);
        bangTaiKhoan.setIntercellSpacing(new Dimension(1, 1));

        JTableHeader headerBang = bangTaiKhoan.getTableHeader();
        headerBang.setFont(UIConstants.FONT_TABLE_HEADER);
        headerBang.setBackground(UIConstants.PRIMARY);
        headerBang.setForeground(Color.WHITE);
        headerBang.setPreferredSize(new Dimension(0, 36));
        headerBang.setReorderingAllowed(false);

        // Độ rộng cột
        bangTaiKhoan.getColumnModel().getColumn(0).setPreferredWidth(50);
        bangTaiKhoan.getColumnModel().getColumn(0).setMaxWidth(60);
        bangTaiKhoan.getColumnModel().getColumn(1).setPreferredWidth(60);
        bangTaiKhoan.getColumnModel().getColumn(1).setMaxWidth(80);
        bangTaiKhoan.getColumnModel().getColumn(2).setPreferredWidth(250);
        bangTaiKhoan.getColumnModel().getColumn(3).setPreferredWidth(200);
        bangTaiKhoan.getColumnModel().getColumn(4).setPreferredWidth(150);

        // Căn giữa cột STT và ID
        javax.swing.table.DefaultTableCellRenderer canGiua = new javax.swing.table.DefaultTableCellRenderer();
        canGiua.setHorizontalAlignment(SwingConstants.CENTER);
        bangTaiKhoan.getColumnModel().getColumn(0).setCellRenderer(canGiua);
        bangTaiKhoan.getColumnModel().getColumn(1).setCellRenderer(canGiua);


        JScrollPane cuonBang = new JScrollPane(bangTaiKhoan);
        cuonBang.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 1));
        cuonBang.getViewport().setBackground(Color.WHITE);
        add(cuonBang, BorderLayout.CENTER);

        // ===== SỰ KIỆN =====

        // Click vào hàng trong bảng → load lên form
        bangTaiKhoan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int dong = bangTaiKhoan.getSelectedRow();
                if (dong >= 0) {
                    idDangChon = Integer.parseInt(moHinhBang.getValueAt(dong, 1).toString().replace("TK-", ""));
                    txtTenDangNhap.setText(moHinhBang.getValueAt(dong, 2).toString());
                    txtMatKhau.setText(moHinhBang.getValueAt(dong, 3).toString());
                    cboVaiTro.setSelectedItem(moHinhBang.getValueAt(dong, 4).toString());
                }
            }
        });

        // Nút Thêm
        btnThem.addActionListener(e -> {
            String tenDN = txtTenDangNhap.getText().trim();
            String matKhau = new String(txtMatKhau.getPassword()).trim();
            String vaiTro = cboVaiTro.getSelectedItem().toString();

            if (!kiemTraFormTK(tenDN, matKhau)) return;

            // Kiểm tra trùng tên đăng nhập
            if (taiKhoanDAO.kiemTraTrungTenDN(tenDN, 0)) {
                JOptionPane.showMessageDialog(this, "Tên đăng nhập \"" + tenDN + "\" đã tồn tại!",
                        "Trùng tên đăng nhập", JOptionPane.WARNING_MESSAGE);
                txtTenDangNhap.requestFocusInWindow();
                return;
            }

            TaiKhoan tk = new TaiKhoan(tenDN, matKhau, vaiTro);
            if (taiKhoanDAO.them(tk)) {
                JOptionPane.showMessageDialog(this, "Thêm tài khoản thành công!",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                lamMoiForm();
                taiDuLieu("");
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Nút Sửa
        btnSua.addActionListener(e -> {
            if (idDangChon < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản cần sửa!",
                        "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String tenDN = txtTenDangNhap.getText().trim();
            String matKhau = new String(txtMatKhau.getPassword()).trim();
            String vaiTro = cboVaiTro.getSelectedItem().toString();

            if (!kiemTraFormTK(tenDN, matKhau)) return;

            // Kiểm tra trùng tên đăng nhập (bỏ qua chính TK đang sửa)
            if (taiKhoanDAO.kiemTraTrungTenDN(tenDN, idDangChon)) {
                JOptionPane.showMessageDialog(this, "Tên đăng nhập \"" + tenDN + "\" đã tồn tại!",
                        "Trùng tên đăng nhập", JOptionPane.WARNING_MESSAGE);
                txtTenDangNhap.requestFocusInWindow();
                return;
            }

            TaiKhoan tk = new TaiKhoan(idDangChon, tenDN, matKhau, vaiTro);
            if (taiKhoanDAO.capNhat(tk)) {
                JOptionPane.showMessageDialog(this, "Cập nhật tài khoản thành công!",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                lamMoiForm();
                taiDuLieu("");
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Nút Xóa
        btnXoa.addActionListener(e -> {
            if (idDangChon < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản cần xóa!",
                        "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean coLienKet = taiKhoanDAO.kiemTraLienKetNhanVien(idDangChon);
            String thongBao;
            if (coLienKet) {
                thongBao = "Tài khoản này đang liên kết với nhân viên.\n"
                         + "Nếu xóa, nhân viên liên quan sẽ mất liên kết đăng nhập.\n\n"
                         + "Bạn có chắc chắn muốn xóa?";
            } else {
                thongBao = "Bạn có chắc muốn xóa tài khoản này?";
            }

            int xacNhan = JOptionPane.showConfirmDialog(this, thongBao, "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (xacNhan == JOptionPane.YES_OPTION) {
                if (taiKhoanDAO.xoaCoLienKet(idDangChon)) {
                    JOptionPane.showMessageDialog(this, "Xóa tài khoản thành công!",
                            "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    lamMoiForm();
                    taiDuLieu("");
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại!",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Nút Làm mới
        btnLamMoi.addActionListener(e -> {
            lamMoiForm();
            taiDuLieu("");
        });
    }

    /** Tải dữ liệu từ database lên bảng (có hỗ trợ tìm kiếm). */
    private void taiDuLieu(String tuKhoa) {
        moHinhBang.setRowCount(0);
        List<TaiKhoan> danhSach;
        if (tuKhoa == null || tuKhoa.isEmpty()) {
            danhSach = taiKhoanDAO.layTatCa();
        } else {
            danhSach = taiKhoanDAO.timKiem(tuKhoa);
        }
        int stt = 1;
        for (TaiKhoan tk : danhSach) {
            moHinhBang.addRow(new Object[]{
                    stt++,
                    String.format("TK-%04d", tk.getId()),
                    tk.getTenDangNhap(),
                    tk.getMatKhau(),
                    tk.getVaiTro()
            });
        }
    }

    /** Xóa trắng form nhập liệu. */
    private void lamMoiForm() {
        txtTenDangNhap.setText("");
        txtMatKhau.setText("");
        cboVaiTro.setSelectedIndex(0);
        idDangChon = -1;
        bangTaiKhoan.clearSelection();
    }

    /** Kiểm tra form tài khoản trước khi thêm/sửa. */
    private boolean kiemTraFormTK(String tenDN, String matKhau) {
        if (tenDN.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên đăng nhập!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            txtTenDangNhap.requestFocusInWindow();
            return false;
        }
        if (tenDN.contains(" ")) {
            JOptionPane.showMessageDialog(this, "Tên đăng nhập không được chứa khoảng trắng!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            txtTenDangNhap.requestFocusInWindow();
            return false;
        }
        if (tenDN.length() < 4) {
            JOptionPane.showMessageDialog(this, "Tên đăng nhập phải có ít nhất 4 ký tự!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            txtTenDangNhap.requestFocusInWindow();
            return false;
        }
        if (matKhau.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            txtMatKhau.requestFocusInWindow();
            return false;
        }
        if (matKhau.length() < 6) {
            JOptionPane.showMessageDialog(this, "Mật khẩu phải có ít nhất 6 ký tự!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            txtMatKhau.requestFocusInWindow();
            return false;
        }
        return true;
    }
}
