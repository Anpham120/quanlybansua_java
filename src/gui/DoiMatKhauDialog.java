package gui;

import dao.TaiKhoanDAO;
import utils.UIConstants;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog đổi mật khẩu cho người dùng hiện tại.
 */
public class DoiMatKhauDialog extends JDialog {

    private JPasswordField txtMatKhauCu, txtMatKhauMoi, txtXacNhan;
    private final int idTaiKhoan;
    private final TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();

    public DoiMatKhauDialog(JFrame parent, int idTaiKhoan) {
        super(parent, "Đổi mật khẩu", true);
        this.idTaiKhoan = idTaiKhoan;

        setSize(400, 280);
        setLocationRelativeTo(parent);
        setResizable(false);
        khoiTaoGiaoDien();
    }

    private void khoiTaoGiaoDien() {
        JPanel main = new JPanel(new GridBagLayout());
        main.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        main.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Mật khẩu cũ
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        main.add(taoLabel("Mật khẩu cũ:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtMatKhauCu = new JPasswordField(20);
        txtMatKhauCu.setFont(UIConstants.FONT_TABLE);
        main.add(txtMatKhauCu, gbc);

        // Mật khẩu mới
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        main.add(taoLabel("Mật khẩu mới:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtMatKhauMoi = new JPasswordField(20);
        txtMatKhauMoi.setFont(UIConstants.FONT_TABLE);
        main.add(txtMatKhauMoi, gbc);

        // Xác nhận
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        main.add(taoLabel("Xác nhận:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtXacNhan = new JPasswordField(20);
        txtXacNhan.setFont(UIConstants.FONT_TABLE);
        main.add(txtXacNhan, gbc);

        // Nút
        JPanel panelNut = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelNut.setBackground(Color.WHITE);
        JButton btnDoi = UIConstants.createStyledButton("Đổi mật khẩu", UIConstants.SUCCESS);
        JButton btnHuy = UIConstants.createStyledButton("Hủy", UIConstants.DANGER);
        btnDoi.addActionListener(e -> doiMatKhau());
        btnHuy.addActionListener(e -> dispose());
        panelNut.add(btnDoi);
        panelNut.add(btnHuy);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        main.add(panelNut, gbc);

        setContentPane(main);
    }

    private void doiMatKhau() {
        String mkCu = new String(txtMatKhauCu.getPassword()).trim();
        String mkMoi = new String(txtMatKhauMoi.getPassword()).trim();
        String xacNhan = new String(txtXacNhan.getPassword()).trim();

        if (mkCu.isEmpty() || mkMoi.isEmpty() || xacNhan.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng điền đầy đủ thông tin.", "Thiếu thông tin",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (mkMoi.length() < 4) {
            JOptionPane.showMessageDialog(this,
                "Mật khẩu mới phải có ít nhất 4 ký tự.", "Mật khẩu quá ngắn",
                JOptionPane.WARNING_MESSAGE);
            txtMatKhauMoi.requestFocus();
            return;
        }

        if (!mkMoi.equals(xacNhan)) {
            JOptionPane.showMessageDialog(this,
                "Mật khẩu mới và xác nhận không khớp.", "Không khớp",
                JOptionPane.WARNING_MESSAGE);
            txtXacNhan.requestFocus();
            return;
        }

        if (mkCu.equals(mkMoi)) {
            JOptionPane.showMessageDialog(this,
                "Mật khẩu mới phải khác mật khẩu cũ.", "Trùng mật khẩu",
                JOptionPane.WARNING_MESSAGE);
            txtMatKhauMoi.requestFocus();
            return;
        }

        if (taiKhoanDAO.doiMatKhau(idTaiKhoan, mkCu, mkMoi)) {
            JOptionPane.showMessageDialog(this,
                "Đổi mật khẩu thành công!", "Thành công",
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Mật khẩu cũ không đúng!", "Thất bại",
                JOptionPane.ERROR_MESSAGE);
            txtMatKhauCu.requestFocus();
        }
    }

    private JLabel taoLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(UIConstants.FONT_TABLE);
        return lbl;
    }
}
