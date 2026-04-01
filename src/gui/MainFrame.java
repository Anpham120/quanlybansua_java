package gui;

import model.TaiKhoan;
import utils.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Cửa sổ chính của ứng dụng với thanh menu bên trái và nội dung bên phải.
 * Sử dụng CardLayout để chuyển đổi giữa các màn hình.
 * 
 * @author Phạm Duy An - BIT240002
 */
public class MainFrame extends JFrame {

    private TaiKhoan taiKhoanHienTai;
    private JPanel panelNoiDung;
    private CardLayout cardLayout;
    private JPanel nutDangChon = null;

    // Danh sách tên các thẻ cho CardLayout
    private static final String THE_SAN_PHAM = "SanPham";
    private static final String THE_KHACH_HANG = "KhachHang";
    private static final String THE_NHAN_VIEN = "NhanVien";
    private static final String THE_BAN_HANG = "BanHang";
    private static final String THE_HOA_DON = "HoaDon";
    private static final String THE_THONG_KE = "ThongKe";
    private static final String THE_TAI_KHOAN = "TaiKhoan";

    public MainFrame(TaiKhoan taiKhoan) {
        this.taiKhoanHienTai = taiKhoan;

        setTitle("Phần mềm Quản lý Bán Sữa");
        setSize(1200, 720);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1000, 600));

        khoiTaoGiaoDien();
    }

    private void khoiTaoGiaoDien() {
        setLayout(new BorderLayout());

        // ===== THANH MENU BÊN TRÁI =====
        JPanel panelMenu = taoThanhMenu();
        add(panelMenu, BorderLayout.WEST);

        // ===== PHẦN BÊN PHẢI =====
        JPanel panelPhai = new JPanel(new BorderLayout());

        // --- Header trên cùng ---
        JPanel panelHeader = taoHeader();
        panelPhai.add(panelHeader, BorderLayout.NORTH);

        // --- Nội dung chính (CardLayout) ---
        cardLayout = new CardLayout();
        panelNoiDung = new JPanel(cardLayout);
        panelNoiDung.setBackground(UIConstants.BG_MAIN);

        // Thêm các màn hình
        String vaiTro = taiKhoanHienTai.getVaiTro();
        panelNoiDung.add(new SanPhamPanel(vaiTro), THE_SAN_PHAM);                          // TV2
        panelNoiDung.add(new KhachHangPanel(vaiTro), THE_KHACH_HANG);                      // TV3
        panelNoiDung.add(new NhanVienPanel(vaiTro), THE_NHAN_VIEN);                        // TV3
        panelNoiDung.add(new BanHangPanel(taiKhoanHienTai), THE_BAN_HANG);               // TV4
        panelNoiDung.add(new HoaDonPanel(),  THE_HOA_DON);               // TV5
        panelNoiDung.add(new ThongKePanel(), THE_THONG_KE);               // TV5
        panelNoiDung.add(new TaiKhoanPanel(), THE_TAI_KHOAN);                             // TV1

        panelPhai.add(panelNoiDung, BorderLayout.CENTER);
        add(panelPhai, BorderLayout.CENTER);

        // Hiển thị đúng màn hình mặc định theo vai trò
        // (CardLayout mặc định show component đầu tiên = SanPhamPanel, không đúng cho Nhân viên)
        String cardMacDinh = taiKhoanHienTai.getVaiTro().equals("Quản lý")
                ? THE_SAN_PHAM
                : THE_BAN_HANG;
        cardLayout.show(panelNoiDung, cardMacDinh);
    }

    // ===== TẠO THANH MENU BÊN TRÁI =====
    private JPanel taoThanhMenu() {
        JPanel panelMenu = new JPanel();
        panelMenu.setLayout(new BoxLayout(panelMenu, BoxLayout.Y_AXIS));
        panelMenu.setBackground(UIConstants.BG_SIDEBAR);
        panelMenu.setPreferredSize(new Dimension(UIConstants.SIDEBAR_WIDTH, 0));

        // --- Logo ---
        JPanel panelLogo = new JPanel(new GridBagLayout());
        panelLogo.setBackground(UIConstants.PRIMARY_DARK);
        panelLogo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        panelLogo.setPreferredSize(new Dimension(UIConstants.SIDEBAR_WIDTH, 90));

        JPanel panelLogoND = new JPanel();
        panelLogoND.setLayout(new BoxLayout(panelLogoND, BoxLayout.Y_AXIS));
        panelLogoND.setBackground(UIConstants.PRIMARY_DARK);

        JLabel lblLogo = new JLabel("QUẢN LÝ BÁN SỮA");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblPhienBan = new JLabel("Phiên bản 1.0");
        lblPhienBan.setFont(UIConstants.FONT_SMALL);
        lblPhienBan.setForeground(new Color(149, 165, 166));
        lblPhienBan.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelLogoND.add(lblLogo);
        panelLogoND.add(Box.createVerticalStrut(5));
        panelLogoND.add(lblPhienBan);
        panelLogo.add(panelLogoND);

        panelMenu.add(panelLogo);
        panelMenu.add(Box.createVerticalStrut(15));

        // --- Các mục menu (phân quyền theo vai trò) ---
        boolean laQuanLy = taiKhoanHienTai.getVaiTro().equals("Quản lý");

        String[][] menuItems = laQuanLy
            ? new String[][] {
                {"Sản phẩm", THE_SAN_PHAM},
                {"Khách hàng", THE_KHACH_HANG},
                {"Nhân viên", THE_NHAN_VIEN},
                {"Hóa đơn", THE_HOA_DON},
                {"Thống kê", THE_THONG_KE},
                {"Tài khoản", THE_TAI_KHOAN}
            }
            : new String[][] {
                {"Bán hàng",  THE_BAN_HANG},
                {"Sản phẩm",  THE_SAN_PHAM}
            };

        JPanel nutMacDinh = null;
        for (String[] item : menuItems) {
            JPanel mucMenu = taoMucMenu(item[0], item[1]);
            panelMenu.add(mucMenu);
            if (nutMacDinh == null) nutMacDinh = mucMenu;
        }

        // Đặt Sản phẩm là mặc định
        if (nutMacDinh != null) chonNutMenu(nutMacDinh);

        // --- Khoảng trống giãn ---
        panelMenu.add(Box.createVerticalGlue());

        // --- Nút đăng xuất (dùng JPanel để tránh Nimbus) ---
        JPanel panelDangXuat = new JPanel(new GridBagLayout());
        panelDangXuat.setBackground(UIConstants.DANGER);
        panelDangXuat.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelDangXuat.setMaximumSize(new Dimension(Integer.MAX_VALUE, UIConstants.MENU_ITEM_HEIGHT));
        panelDangXuat.setPreferredSize(new Dimension(UIConstants.SIDEBAR_WIDTH, UIConstants.MENU_ITEM_HEIGHT));

        JLabel lblDangXuat = new JLabel("Đăng xuất");
        lblDangXuat.setFont(UIConstants.FONT_MENU);
        lblDangXuat.setForeground(Color.WHITE);
        panelDangXuat.add(lblDangXuat);

        panelDangXuat.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int chon = JOptionPane.showConfirmDialog(MainFrame.this,
                        "Bạn có chắc muốn đăng xuất?", "Xác nhận",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (chon == JOptionPane.YES_OPTION) {
                    dispose();
                    new LoginFrame().setVisible(true);
                }
            }
            public void mouseEntered(MouseEvent e) {
                panelDangXuat.setBackground(new Color(192, 57, 43));
            }
            public void mouseExited(MouseEvent e) {
                panelDangXuat.setBackground(UIConstants.DANGER);
            }
        });

        panelMenu.add(panelDangXuat);
        panelMenu.add(Box.createVerticalStrut(15));

        return panelMenu;
    }

    // ===== TẠO MỤC MENU (dùng JPanel + JLabel, tránh Nimbus) =====
    private JPanel taoMucMenu(String tieuDe, String tenThe) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UIConstants.BG_SIDEBAR);
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, UIConstants.MENU_ITEM_HEIGHT));
        panel.setPreferredSize(new Dimension(UIConstants.SIDEBAR_WIDTH, UIConstants.MENU_ITEM_HEIGHT));

        JLabel lbl = new JLabel(tieuDe);
        lbl.setFont(UIConstants.FONT_MENU);
        lbl.setForeground(UIConstants.TEXT_LIGHT);
        panel.add(lbl);

        // Hiệu ứng hover
        panel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (panel != nutDangChon) {
                    panel.setBackground(UIConstants.BG_SIDEBAR_HOVER);
                }
            }
            public void mouseExited(MouseEvent e) {
                if (panel != nutDangChon) {
                    panel.setBackground(UIConstants.BG_SIDEBAR);
                }
            }
            public void mouseClicked(MouseEvent e) {
                chonNutMenu(panel);
                cardLayout.show(panelNoiDung, tenThe);
            }
        });

        return panel;
    }

    // Đánh dấu mục đang được chọn
    private void chonNutMenu(JPanel panel) {
        if (nutDangChon != null) {
            nutDangChon.setBackground(UIConstants.BG_SIDEBAR);
        }
        nutDangChon = panel;
        nutDangChon.setBackground(UIConstants.BG_SIDEBAR_ACTIVE);
    }

    // ===== TẠO HEADER =====
    private JPanel taoHeader() {
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(Color.WHITE);
        panelHeader.setPreferredSize(new Dimension(0, UIConstants.HEADER_HEIGHT));
        panelHeader.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UIConstants.BORDER_COLOR),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        JLabel lblXinChao = new JLabel("Xin chào, " + taiKhoanHienTai.getTenDangNhap()
                + " (" + taiKhoanHienTai.getVaiTro() + ")");
        lblXinChao.setFont(UIConstants.FONT_SUBTITLE);
        lblXinChao.setForeground(UIConstants.TEXT_DARK);

        // Panel phải: nút đổi MK + ngày
        JPanel panelPhai = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        panelPhai.setBackground(Color.WHITE);

        // Chỉ Nhân viên mới cần nút đổi MK (QL đã có tab Tài khoản)
        if (!taiKhoanHienTai.getVaiTro().equals("Quản lý")) {
            JButton btnDoiMK = UIConstants.createStyledButton("Đổi mật khẩu", new Color(52, 73, 94));
            btnDoiMK.setFont(UIConstants.FONT_SMALL);
            btnDoiMK.addActionListener(e -> {
                new DoiMatKhauDialog(MainFrame.this, taiKhoanHienTai.getId()).setVisible(true);
            });
            panelPhai.add(btnDoiMK);
        }

        String ngayHomNay = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        JLabel lblNgay = new JLabel("Ngày: " + ngayHomNay);
        lblNgay.setFont(UIConstants.FONT_LABEL);
        lblNgay.setForeground(new Color(127, 140, 141));
        panelPhai.add(lblNgay);

        panelHeader.add(lblXinChao, BorderLayout.WEST);
        panelHeader.add(panelPhai, BorderLayout.EAST);

        return panelHeader;
    }
}
