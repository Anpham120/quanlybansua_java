package gui;

import dao.TaiKhoanDAO;
import model.TaiKhoan;
import utils.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Màn hình đăng nhập hệ thống.
 * 
 * @author Phạm Duy An - BIT240002
 */
public class LoginFrame extends JFrame {

    private JTextField txtTenDangNhap;
    private JPasswordField txtMatKhau;
    private JButton btnDangNhap;
    private JLabel lblThongBao;

    public LoginFrame() {
        setTitle("Đăng nhập - Quản lý Bán Sữa");
        setSize(420, 520);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        khoiTaoGiaoDien();
    }

    private void khoiTaoGiaoDien() {
        // Panel chính với nền xám nhạt
        JPanel panelChinh = new JPanel(new GridBagLayout());
        panelChinh.setBackground(UIConstants.BG_MAIN);

        // Thẻ đăng nhập (card)
        JPanel panelThe = new JPanel();
        panelThe.setLayout(new BoxLayout(panelThe, BoxLayout.Y_AXIS));
        panelThe.setBackground(Color.WHITE);
        panelThe.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(0, 0, 30, 0)
        ));
        panelThe.setPreferredSize(new Dimension(350, 430));

        // --- Phần header (nền xanh) ---
        JPanel panelHeader = new JPanel();
        panelHeader.setLayout(new BoxLayout(panelHeader, BoxLayout.Y_AXIS));
        panelHeader.setBackground(UIConstants.PRIMARY);
        panelHeader.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        panelHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));

        JLabel lblBieuTuong = new JLabel("\uD83E\uDD5B", SwingConstants.CENTER);
        lblBieuTuong.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        lblBieuTuong.setForeground(Color.WHITE);
        lblBieuTuong.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTieuDe = new JLabel("QUẢN LÝ BÁN SỮA");
        lblTieuDe.setFont(UIConstants.FONT_TITLE);
        lblTieuDe.setForeground(Color.WHITE);
        lblTieuDe.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblMoTa = new JLabel("Đăng nhập để tiếp tục");
        lblMoTa.setFont(UIConstants.FONT_SMALL);
        lblMoTa.setForeground(new Color(174, 214, 241));
        lblMoTa.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelHeader.add(lblBieuTuong);
        panelHeader.add(Box.createVerticalStrut(8));
        panelHeader.add(lblTieuDe);
        panelHeader.add(Box.createVerticalStrut(5));
        panelHeader.add(lblMoTa);

        // --- Phần form (dùng GridBagLayout để căn đều) ---
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(BorderFactory.createEmptyBorder(25, 35, 10, 35));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, 0, 0);

        // Tên đăng nhập
        JLabel lblTenDN = new JLabel("Tên đăng nhập");
        lblTenDN.setFont(UIConstants.FONT_LABEL);
        lblTenDN.setForeground(UIConstants.TEXT_DARK);

        txtTenDangNhap = new JTextField();
        txtTenDangNhap.setFont(UIConstants.FONT_INPUT);
        txtTenDangNhap.setPreferredSize(new Dimension(0, 38));

        // Mật khẩu
        JLabel lblMK = new JLabel("Mật khẩu");
        lblMK.setFont(UIConstants.FONT_LABEL);
        lblMK.setForeground(UIConstants.TEXT_DARK);

        txtMatKhau = new JPasswordField();
        txtMatKhau.setFont(UIConstants.FONT_INPUT);
        txtMatKhau.setPreferredSize(new Dimension(0, 38));

        // Nút đăng nhập
        btnDangNhap = new JButton("ĐĂNG NHẬP");
        btnDangNhap.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDangNhap.setForeground(Color.WHITE);
        btnDangNhap.setBackground(UIConstants.PRIMARY);
        btnDangNhap.setFocusPainted(false);
        btnDangNhap.setBorderPainted(false);
        btnDangNhap.setOpaque(true);
        btnDangNhap.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDangNhap.setPreferredSize(new Dimension(0, 42));

        // Hiệu ứng hover cho nút
        btnDangNhap.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnDangNhap.setBackground(UIConstants.PRIMARY_DARK);
            }
            public void mouseExited(MouseEvent e) {
                btnDangNhap.setBackground(UIConstants.PRIMARY);
            }
        });

        // Thông báo lỗi
        lblThongBao = new JLabel(" ");
        lblThongBao.setFont(UIConstants.FONT_SMALL);
        lblThongBao.setForeground(UIConstants.DANGER);

        // Sắp xếp vào form
        gbc.gridy = 0; gbc.insets = new Insets(0, 0, 5, 0);
        panelForm.add(lblTenDN, gbc);
        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 15, 0);
        panelForm.add(txtTenDangNhap, gbc);
        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 5, 0);
        panelForm.add(lblMK, gbc);
        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 8, 0);
        panelForm.add(txtMatKhau, gbc);
        gbc.gridy = 4; gbc.insets = new Insets(0, 0, 10, 0);
        panelForm.add(lblThongBao, gbc);
        gbc.gridy = 5; gbc.insets = new Insets(0, 0, 0, 0);
        panelForm.add(btnDangNhap, gbc);

        // Ghép header + form vào thẻ
        panelThe.add(panelHeader);
        panelThe.add(panelForm);

        panelChinh.add(panelThe);
        setContentPane(panelChinh);

        // === SỰ KIỆN ===
        btnDangNhap.addActionListener(e -> xuLyDangNhap());
        txtMatKhau.addActionListener(e -> xuLyDangNhap()); // Enter để đăng nhập

        // Focus vào ô tên đăng nhập khi mở
        SwingUtilities.invokeLater(() -> txtTenDangNhap.requestFocusInWindow());
    }

    private void xuLyDangNhap() {
        String tenDN = txtTenDangNhap.getText().trim();
        String matKhau = new String(txtMatKhau.getPassword()).trim();

        if (tenDN.isEmpty() || matKhau.isEmpty()) {
            lblThongBao.setText("Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();
        TaiKhoan taiKhoan = taiKhoanDAO.dangNhap(tenDN, matKhau);

        if (taiKhoan != null) {
            dispose(); // Đóng form đăng nhập
            new MainFrame(taiKhoan).setVisible(true);
        } else {
            lblThongBao.setText("Sai tên đăng nhập hoặc mật khẩu!");
            txtMatKhau.setText("");
            txtMatKhau.requestFocusInWindow();
        }
    }
}
