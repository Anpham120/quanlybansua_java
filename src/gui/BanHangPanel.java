package gui;

import dao.HoaDonDAO;
import dao.KhachHangDAO;
import dao.SanPhamDAO;
import model.ChiTietHoaDon;
import model.KhachHang;
import model.SanPham;
import model.TaiKhoan;
import utils.AppConstants;
import utils.EventBus;
import utils.UIConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * Panel Bán hàng (TV4): Chọn sản phẩm → Giỏ hàng → Thanh toán.
 * Tự động trừ tồn kho, cộng điểm khách hàng, tạo hóa đơn.
 * Hỗ trợ tìm khách theo SĐT, đăng ký nhanh thành viên, ưu đãi theo hạng.
 *
 * @author Nguyễn Quang Hiếu - BIT240082
 */
public class BanHangPanel extends JPanel {

    // ===== DATA =====
    private final SanPhamDAO sanPhamDAO = new SanPhamDAO();
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();
    private final HoaDonDAO hoaDonDAO = new HoaDonDAO();
    private final List<ChiTietHoaDon> gioHang = new ArrayList<>();
    private final int idNhanVien;
    private final NumberFormat fmtTien = NumberFormat.getNumberInstance(Locale.of("vi", "VN"));

    // ===== BẢNG SẢN PHẨM =====
    private JTable bangSP;
    private DefaultTableModel modelBangSP;
    private JTextField txtTimSP;
    private JSpinner spinSoLuong;

    // ===== GIỎ HÀNG =====
    private JTable bangGio;
    private DefaultTableModel modelBangGio;
    private JLabel lblTongTien;

    // ===== KHÁCH HÀNG =====
    private JComboBox<Object> cboKhachHang;
    private JTextField txtTimSDT;
    private boolean[] dangSync = {false};

    // Danh sách SP đang hiển thị trong bảng (để lấy giá mà không cần DB call thêm)
    private final List<SanPham> dsSPHienTai = new ArrayList<>();

    // ===== CONSTRUCTOR =====
    public BanHangPanel(TaiKhoan taiKhoan) {
        // Tìm ID nhân viên liên kết với tài khoản đăng nhập
        this.idNhanVien = hoaDonDAO.layIdNhanVienTheoTaiKhoan(taiKhoan.getId());
        setLayout(new BorderLayout(10, 10));
        setBackground(UIConstants.BG_MAIN);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ── Header ──
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(UIConstants.BG_MAIN);

        JLabel lblTieuDe = new JLabel("BÁN HÀNG");
        lblTieuDe.setFont(UIConstants.FONT_TITLE);
        lblTieuDe.setForeground(UIConstants.PRIMARY);
        panelHeader.add(lblTieuDe, BorderLayout.NORTH);

        // Cảnh báo nếu TK chưa liên kết nhân viên
        if (idNhanVien == 0) {
            JPanel panelCanhBao = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
            panelCanhBao.setBackground(new Color(255, 243, 205));
            panelCanhBao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 193, 7)),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
            ));
            JLabel lblCanhBao = new JLabel("[!] Tài khoản chưa liên kết nhân viên! Hóa đơn sẽ không ghi được tên người bán. Vui lòng liên hệ Quản lý.");
            lblCanhBao.setFont(UIConstants.FONT_TABLE);
            lblCanhBao.setForeground(new Color(133, 100, 4));
            panelCanhBao.add(lblCanhBao);
            panelHeader.add(panelCanhBao, BorderLayout.SOUTH);
        }

        add(panelHeader, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
            taoPanelSanPham(), taoPanelGioHang());
        split.setDividerLocation(420);
        split.setResizeWeight(0.45);
        add(split, BorderLayout.CENTER);

        taiSanPham("");
        taiDanhSachKhachHang();
    }

    // =========================================================
    // PHẦN TRÁI: Danh sách sản phẩm
    // =========================================================
    private JPanel taoPanelSanPham() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(UIConstants.BG_CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_COLOR),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Tiêu đề + tìm kiếm
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        top.setBackground(UIConstants.BG_CARD);
        top.add(new JLabel("Tìm sản phẩm:"));
        txtTimSP = new JTextField(14);
        txtTimSP.setFont(UIConstants.FONT_TABLE);
        txtTimSP.addActionListener(e -> taiSanPham(txtTimSP.getText().trim()));
        top.add(txtTimSP);
        JButton btnTim = UIConstants.createStyledButton("Tìm", UIConstants.INFO);
        btnTim.addActionListener(e -> taiSanPham(txtTimSP.getText().trim()));
        top.add(btnTim);
        panel.add(top, BorderLayout.NORTH);

        // Bảng sản phẩm
        String[] cotSP = {"STT", "Mã SP", "Tên sản phẩm", "Loại", "Giá (đ)", "Tồn"};
        modelBangSP = new DefaultTableModel(cotSP, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        bangSP = new JTable(modelBangSP);
        bangSP.setFont(UIConstants.FONT_TABLE);
        bangSP.setRowHeight(30);
        bangSP.getTableHeader().setFont(UIConstants.FONT_TABLE_HEADER);
        bangSP.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        int[] wSP = {35, 35, 200, 100, 100, 55};
        for (int i = 0; i < wSP.length; i++) bangSP.getColumnModel().getColumn(i).setPreferredWidth(wSP[i]);
        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(SwingConstants.RIGHT);
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        bangSP.getColumnModel().getColumn(0).setCellRenderer(center);
        bangSP.getColumnModel().getColumn(1).setCellRenderer(center);
        bangSP.getColumnModel().getColumn(4).setCellRenderer(right);
        bangSP.getColumnModel().getColumn(5).setCellRenderer(center);
        panel.add(new JScrollPane(bangSP), BorderLayout.CENTER);

        // Chọn số lượng + nút thêm vào giỏ
        JPanel bot = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 4));
        bot.setBackground(UIConstants.BG_CARD);
        bot.add(new JLabel("Số lượng:"));
        spinSoLuong = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        spinSoLuong.setPreferredSize(new Dimension(70, 30));
        spinSoLuong.setFont(UIConstants.FONT_TABLE);
        bot.add(spinSoLuong);
        JButton btnThem = taoNut("Thêm vào giỏ ▶", UIConstants.PRIMARY);
        btnThem.addActionListener(e -> themVaoGio());
        bot.add(btnThem);
        panel.add(bot, BorderLayout.SOUTH);

        return panel;
    }

    // =========================================================
    // PHẦN PHẢI: Giỏ hàng
    // =========================================================
    private JPanel taoPanelGioHang() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(UIConstants.BG_CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_COLOR),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // ── Tạo controls trước, wire listeners sau (tránh NPE/vòng lặp) ──
        cboKhachHang = new JComboBox<>();
        cboKhachHang.setFont(UIConstants.FONT_TABLE);
        cboKhachHang.setPreferredSize(new Dimension(230, 30));

        JLabel lblHangKH = new JLabel("");
        lblHangKH.setFont(new Font("Segoe UI", Font.BOLD, 13));

        txtTimSDT = new JTextField(12);
        txtTimSDT.setFont(UIConstants.FONT_TABLE);
        txtTimSDT.setPreferredSize(new Dimension(120, 28));
        txtTimSDT.setToolTipText("Nhập SĐT → tự động tìm khách hàng thành viên");

        // Chỉ cho nhập số vào txtTimSDT
        ((javax.swing.text.AbstractDocument) txtTimSDT.getDocument())
            .setDocumentFilter(new javax.swing.text.DocumentFilter() {
                public void insertString(FilterBypass fb, int o, String s,
                        javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                    if (s != null && s.matches("\\d*")) super.insertString(fb, o, s, a);
                }
                public void replace(FilterBypass fb, int o, int l, String s,
                        javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                    if (s != null && s.matches("\\d*")) super.replace(fb, o, l, s, a);
                }
            });

        // Listener: gõ SĐT → tự chọn ComboBox
        txtTimSDT.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void tim() {
                if (dangSync[0]) return;
                String sdt = txtTimSDT.getText().trim();
                if (sdt.length() == 10) {
                    for (int i = 1; i < cboKhachHang.getItemCount(); i++) {
                        Object item = cboKhachHang.getItemAt(i);
                        if (item instanceof KhachHang k && k.getSdt().equals(sdt)) {
                            dangSync[0] = true;
                            cboKhachHang.setSelectedIndex(i);
                            dangSync[0] = false;
                            return;
                        }
                    }
                    // Không tìm thấy → gợi ý đăng ký
                    int xn = JOptionPane.showConfirmDialog(BanHangPanel.this,
                        "Không tìm thấy khách hàng với SĐT: " + sdt
                        + "\nBạn có muốn đăng ký thành viên mới không?",
                        "Không tìm thấy", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (xn == JOptionPane.YES_OPTION) dangKyNhanhKH(sdt);
                } else if (sdt.isEmpty()) {
                    dangSync[0] = true;
                    cboKhachHang.setSelectedIndex(0);
                    dangSync[0] = false;
                }
            }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { tim(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { tim(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) {}
        });

        // Listener: chọn ComboBox → cập nhật badge hạng + sync SĐT
        cboKhachHang.addActionListener(e -> {
            if (dangSync[0]) return;
            Object sel = cboKhachHang.getSelectedItem();
            if (sel instanceof KhachHang k) {
                lblHangKH.setText(k.getHang());
                dangSync[0] = true;
                txtTimSDT.setText(k.getSdt());
                dangSync[0] = false;
            } else {
                lblHangKH.setText("");
                dangSync[0] = true;
                txtTimSDT.setText("");
                dangSync[0] = false;
            }
        });

        // ── Ghép layout panelTop ──
        JPanel panelTop = new JPanel();
        panelTop.setLayout(new BoxLayout(panelTop, BoxLayout.Y_AXIS));
        panelTop.setBackground(UIConstants.BG_CARD);

        // Hàng 1: Tiêu đề "Giỏ hàng"
        JLabel lblTitle = new JLabel("Giỏ hàng");
        lblTitle.setFont(UIConstants.FONT_MENU);
        lblTitle.setForeground(UIConstants.PRIMARY);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelTop.add(lblTitle);
        panelTop.add(Box.createVerticalStrut(4));

        // Hàng 2: Tìm theo SĐT + badge hạng
        JPanel panelTimSDT = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        panelTimSDT.setBackground(UIConstants.BG_CARD);
        panelTimSDT.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelTimSDT.add(new JLabel("\uD83D\uDD0D SĐT:"));
        panelTimSDT.add(txtTimSDT);
        panelTimSDT.add(lblHangKH);
        panelTop.add(panelTimSDT);
        panelTop.add(Box.createVerticalStrut(3));

        // Hàng 3: ComboBox chọn khách + nút đăng ký
        JPanel panelKH = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        panelKH.setBackground(UIConstants.BG_CARD);
        panelKH.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelKH.add(new JLabel("Khách hàng:"));
        panelKH.add(cboKhachHang);
        JButton btnDangKy = new JButton("+");
        btnDangKy.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnDangKy.setBackground(UIConstants.SUCCESS);
        btnDangKy.setForeground(Color.WHITE);
        btnDangKy.setPreferredSize(new Dimension(36, 30));
        btnDangKy.setToolTipText("Đăng ký khách hàng thành viên mới");
        btnDangKy.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDangKy.addActionListener(e -> dangKyNhanhKH(txtTimSDT.getText().trim()));
        panelKH.add(btnDangKy);
        panelTop.add(panelKH);

        panel.add(panelTop, BorderLayout.NORTH);

        // ── Bảng giỏ hàng ──
        String[] cotGio = {"#", "Tên sản phẩm", "SL", "Đơn giá (đ)", "Thành tiền (đ)"};
        modelBangGio = new DefaultTableModel(cotGio, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        bangGio = new JTable(modelBangGio);
        bangGio.setFont(UIConstants.FONT_TABLE);
        bangGio.setRowHeight(30);
        bangGio.getTableHeader().setFont(UIConstants.FONT_TABLE_HEADER);
        bangGio.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bangGio.getColumnModel().getColumn(0).setPreferredWidth(30);
        bangGio.getColumnModel().getColumn(1).setPreferredWidth(160);
        bangGio.getColumnModel().getColumn(2).setPreferredWidth(40);
        bangGio.getColumnModel().getColumn(3).setPreferredWidth(90);
        bangGio.getColumnModel().getColumn(4).setPreferredWidth(100);
        DefaultTableCellRenderer rightGio = new DefaultTableCellRenderer();
        rightGio.setHorizontalAlignment(SwingConstants.RIGHT);
        DefaultTableCellRenderer centerGio = new DefaultTableCellRenderer();
        centerGio.setHorizontalAlignment(SwingConstants.CENTER);
        bangGio.getColumnModel().getColumn(0).setCellRenderer(centerGio);
        bangGio.getColumnModel().getColumn(2).setCellRenderer(centerGio);
        bangGio.getColumnModel().getColumn(3).setCellRenderer(rightGio);
        bangGio.getColumnModel().getColumn(4).setCellRenderer(rightGio);
        panel.add(new JScrollPane(bangGio), BorderLayout.CENTER);

        // ── Footer: tổng tiền + nút ──
        JPanel footer = new JPanel(new BorderLayout(0, 4));
        footer.setBackground(UIConstants.BG_CARD);

        lblTongTien = new JLabel("Tổng cộng: 0 đ", SwingConstants.RIGHT);
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTongTien.setForeground(UIConstants.PRIMARY);
        footer.add(lblTongTien, BorderLayout.NORTH);

        JPanel panelNut = new JPanel(new GridLayout(1, 3, 6, 0));
        panelNut.setBackground(UIConstants.BG_CARD);
        JButton btnXoa = taoNut("Xóa dòng", UIConstants.WARNING);
        btnXoa.addActionListener(e -> xoaDongGio());
        JButton btnXoaHet = taoNut("Xóa hết", UIConstants.DANGER);
        btnXoaHet.addActionListener(e -> {
            if (gioHang.isEmpty()) return;
            gioHang.clear(); capNhatBangGio();
        });
        JButton btnThanhToan = new JButton("Thanh toán");
        btnThanhToan.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnThanhToan.setBackground(UIConstants.SUCCESS);
        btnThanhToan.setForeground(Color.WHITE);
        btnThanhToan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnThanhToan.addActionListener(e -> thanhToan());
        panelNut.add(btnXoa);
        panelNut.add(btnXoaHet);
        panelNut.add(btnThanhToan);
        footer.add(panelNut, BorderLayout.SOUTH);

        panel.add(footer, BorderLayout.SOUTH);
        return panel;
    }

    // =========================================================
    // CHỨC NĂNG
    // =========================================================

    private void taiSanPham(String tuKhoa) {
        modelBangSP.setRowCount(0);
        dsSPHienTai.clear();
        java.sql.Date homNay = new java.sql.Date(System.currentTimeMillis());
        int stt = 1;
        for (SanPham sp : sanPhamDAO.timKiem(tuKhoa, "Tất cả")) {
            // Bỏ qua SP hết hàng
            if (sp.getSoLuong() <= 0) continue;
            // Bỏ qua SP hết hạn sử dụng
            if (sp.getHanSuDung() != null && sp.getHanSuDung().before(homNay)) continue;

            dsSPHienTai.add(sp);
            modelBangSP.addRow(new Object[]{
                stt++, String.format("SP-%04d", sp.getId()), sp.getTenSP(), sp.getLoai(),
                fmtTien.format(sp.getDonGia()), sp.getSoLuong()
            });
        }
    }

    private void taiDanhSachKhachHang() {
        cboKhachHang.removeAllItems();
        cboKhachHang.addItem("-- Khách lẻ --");
        for (KhachHang kh : khachHangDAO.layTatCa()) {
            cboKhachHang.addItem(kh);
        }
    }

    private void themVaoGio() {
        int dong = bangSP.getSelectedRow();
        if (dong < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm từ danh sách.",
                "Chưa chọn", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idSP   = Integer.parseInt(modelBangSP.getValueAt(dong, 1).toString().replace("SP-", ""));
        String ten = (String) modelBangSP.getValueAt(dong, 2);
        int tonKho = (int) modelBangSP.getValueAt(dong, 5);
        int soLuong = (int) spinSoLuong.getValue();

        // Kiểm tra tồn kho (bao gồm cả lượng đã trong giỏ)
        int daCoTrongGio = gioHang.stream()
            .filter(ct -> ct.getIdSanPham() == idSP)
            .mapToInt(ChiTietHoaDon::getSoLuong).sum();
        if (daCoTrongGio + soLuong > tonKho) {
            JOptionPane.showMessageDialog(this,
                "Không đủ hàng! Tồn kho: " + tonKho + ", đã đặt: " + daCoTrongGio,
                "Vượt tồn kho", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Lấy giá từ danh sách song song (không cần gọi DB thêm)
        double gia = dsSPHienTai.get(dong).getDonGia();

        // Gộp nếu đã có trong giỏ — sử dụng Optional + luồng dữ liệu hàm
        Optional<ChiTietHoaDon> coSan = gioHang.stream()
                .filter(c -> c.getIdSanPham() == idSP)
                .findFirst();

        if (coSan.isPresent()) {
            ChiTietHoaDon ctGop = coSan.get();
            ctGop.setSoLuong(ctGop.getSoLuong() + soLuong);
            ctGop.setThanhTien(ctGop.getSoLuong() * ctGop.getDonGia());
            capNhatBangGio();
            return;
        }

        ChiTietHoaDon ct = new ChiTietHoaDon();
        ct.setIdSanPham(idSP);
        ct.setTenSanPham(ten);
        ct.setSoLuong(soLuong);
        ct.setDonGia(gia);
        ct.setThanhTien(gia * soLuong);
        gioHang.add(ct);
        capNhatBangGio();
    }

    private void xoaDongGio() {
        int dong = bangGio.getSelectedRow();
        if (dong < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa.",
                "Chưa chọn", JOptionPane.WARNING_MESSAGE);
            return;
        }
        gioHang.remove(dong);
        capNhatBangGio();
    }

    private void capNhatBangGio() {
        modelBangGio.setRowCount(0);

        // Sử dụng luồng số nguyên thay vì vòng lặp for truyền thống
        IntStream.range(0, gioHang.size()).forEach(i -> {
            ChiTietHoaDon ct = gioHang.get(i);
            modelBangGio.addRow(new Object[]{
                    i + 1, ct.getTenSanPham(), ct.getSoLuong(),
                    fmtTien.format((long) ct.getDonGia()),
                    fmtTien.format((long) ct.getThanhTien())
            });
        });

        // Tính tổng bằng luồng dữ liệu hàm
        double tong = gioHang.stream()
                .mapToDouble(ChiTietHoaDon::getThanhTien)
                .sum();
        lblTongTien.setText("Tổng cộng: " + fmtTien.format((long) tong) + " đ");
    }

    /** Xử lý thanh toán: tạo hóa đơn, trừ kho, cộng điểm, áp dụng ưu đãi hạng. */
    private void thanhToan() {
        if (gioHang.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng đang trống!",
                "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idKhachHang = 0;
        String tenKH = "Khách lẻ";
        double tyLeGiam = 0.0;
        Object objKH = cboKhachHang.getSelectedItem();
        if (objKH instanceof KhachHang kh) {
            idKhachHang = kh.getId();
            tenKH = kh.getHoTen();
            tyLeGiam = kh.getTyLeGiam();
        }

        double tongGoc = gioHang.stream().mapToDouble(ChiTietHoaDon::getThanhTien).sum();
        double soTienGiam = tongGoc * tyLeGiam;
        double tongThanhToan = tongGoc - soTienGiam;
        int diemThem = (int) (tongThanhToan / AppConstants.TIEN_MOI_DIEM);

        StringBuilder sb = new StringBuilder();
        sb.append("Xác nhận thanh toán?\n\n");
        sb.append(String.format("%-15s: %s\n", "Khách hàng", tenKH));
        sb.append(String.format("%-15s: %s đ\n", "Tổng tiền gốc", fmtTien.format((long) tongGoc)));
        if (tyLeGiam > 0) {
            int pct = (int)(tyLeGiam * 100);
            sb.append(String.format("%-15s: -%d%% = -%s đ\n", "Ưu đãi hạng",
                pct, fmtTien.format((long) soTienGiam)));
            sb.append(String.format("%-15s: %s đ\n", "Thực thanh toán",
                fmtTien.format((long) tongThanhToan)));
        }
        if (idKhachHang > 0 && diemThem > 0)
            sb.append(String.format("%-15s: +%d điểm\n", "Điểm cộng", diemThem));

        int xn = JOptionPane.showConfirmDialog(this, sb.toString(),
            "Xác nhận thanh toán", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (xn != JOptionPane.YES_OPTION) return;

        int idHD = hoaDonDAO.taoHoaDon(idNhanVien, idKhachHang, tongThanhToan, gioHang);
        if (idHD > 0) {
            String thongBao = "Thanh toán thành công!\n"
                + "Mã hóa đơn : HD-" + String.format("%04d", idHD) + "\n"
                + "Thực trả   : " + fmtTien.format((long) tongThanhToan) + " đ";
            if (tyLeGiam > 0)
                thongBao += "\nTiết kiệm  : " + fmtTien.format((long) soTienGiam) + " đ";
            if (idKhachHang > 0 && diemThem > 0)
                thongBao += "\nĐiểm cộng  : +" + diemThem + " điểm cho " + tenKH;
            JOptionPane.showMessageDialog(this, thongBao, "Thành công", JOptionPane.INFORMATION_MESSAGE);

            gioHang.clear();
            capNhatBangGio();
            cboKhachHang.setSelectedIndex(0);
            spinSoLuong.setValue(1);
            taiSanPham("");

            // Phát sự kiện qua EventBus — ThongKePanel và SanPhamPanel sẽ tự cập nhật
            EventBus.publish(EventBus.SU_KIEN_THANH_TOAN, idHD);
            EventBus.publish(EventBus.SU_KIEN_CAP_NHAT_SAN_PHAM, null);
        } else {
            JOptionPane.showMessageDialog(this,
                "Thanh toán thất bại!\nKiểm tra lại tồn kho hoặc kết nối cơ sở dữ liệu.",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =========================================================
    // HỖ TRỢ
    // =========================================================

    /**
     * Dialog đăng ký nhanh khách hàng thành viên tại quầy.
     * Sau khi lưu, tự động refresh ComboBox và chọn khách vừa đăng ký.
     */
    private void dangKyNhanhKH(String sdtMacDinh) {
        JDialog dlg = new JDialog((java.awt.Frame) SwingUtilities.getWindowAncestor(this),
                "Đăng ký khách hàng thành viên", true);
        dlg.setSize(420, 260);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(18, 20, 8, 20));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 6, 6, 6);
        g.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtTen = new JTextField(16); txtTen.setFont(UIConstants.FONT_TABLE);
        JTextField txtSdt = new JTextField(16); txtSdt.setFont(UIConstants.FONT_TABLE);
        JTextField txtDia = new JTextField(16); txtDia.setFont(UIConstants.FONT_TABLE);

        // Điền sẵn SĐT đã gõ trước đó
        if (sdtMacDinh != null && !sdtMacDinh.isEmpty()) {
            txtSdt.setText(sdtMacDinh);
        }

        ((javax.swing.text.AbstractDocument) txtSdt.getDocument())
            .setDocumentFilter(new javax.swing.text.DocumentFilter() {
                public void insertString(FilterBypass fb, int o, String s,
                        javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                    if (s != null && s.matches("\\d*")) super.insertString(fb, o, s, a);
                }
                public void replace(FilterBypass fb, int o, int l, String s,
                        javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                    if (s != null && s.matches("\\d*")) super.replace(fb, o, l, s, a);
                }
            });

        g.gridy = 0; g.gridx = 0; g.weightx = 0; form.add(new JLabel("Họ tên (*):"), g);
        g.gridx = 1; g.weightx = 1; form.add(txtTen, g);
        g.gridy = 1; g.gridx = 0; g.weightx = 0; form.add(new JLabel("SĐT (*):"), g);
        g.gridx = 1; g.weightx = 1; form.add(txtSdt, g);
        g.gridy = 2; g.gridx = 0; g.weightx = 0; form.add(new JLabel("Địa chỉ:"), g);
        g.gridx = 1; g.weightx = 1; form.add(txtDia, g);

        JPanel panelNut = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        panelNut.setBackground(Color.WHITE);
        JButton btnHuy = new JButton("Hủy");
        btnHuy.addActionListener(e -> dlg.dispose());
        JButton btnLuu = UIConstants.createStyledButton("Lưu & Chọn", new Color(39, 174, 96));
        btnLuu.addActionListener(e -> {
            String ten = txtTen.getText().trim();
            String sdt = txtSdt.getText().trim();
            String dia = txtDia.getText().trim();

            if (ten.isEmpty() || ten.length() < 2 || !ten.matches("^[\\p{L} ]+$")) {
                JOptionPane.showMessageDialog(dlg,
                    "Họ tên không hợp lệ (tối thiểu 2 ký tự, không chứa số).",
                    "Lỗi", JOptionPane.WARNING_MESSAGE);
                txtTen.requestFocus(); return;
            }
            if (!sdt.matches("^0\\d{9}$")) {
                JOptionPane.showMessageDialog(dlg,
                    "Số điện thoại phải gồm 10 chữ số và bắt đầu bằng số 0.",
                    "Lỗi", JOptionPane.WARNING_MESSAGE);
                txtSdt.requestFocus(); return;
            }
            if (khachHangDAO.kiemTraTrungSDT(sdt, 0)) {
                JOptionPane.showMessageDialog(dlg,
                    "Số điện thoại \"" + sdt + "\" đã tồn tại trong hệ thống!",
                    "Trùng SĐT", JOptionPane.WARNING_MESSAGE);
                txtSdt.requestFocus(); return;
            }

            KhachHang kh = new KhachHang();
            kh.setHoTen(ten); kh.setSdt(sdt);
            kh.setDiaChi(dia); kh.setDiemTichLuy(0);

            if (khachHangDAO.them(kh)) {
                dlg.dispose();
                // Chặn listener cascade khi refresh ComboBox
                dangSync[0] = true;
                taiDanhSachKhachHang();
                for (int i = 1; i < cboKhachHang.getItemCount(); i++) {
                    Object item = cboKhachHang.getItemAt(i);
                    if (item instanceof KhachHang k && k.getSdt().equals(sdt)) {
                        cboKhachHang.setSelectedIndex(i); break;
                    }
                }
                txtTimSDT.setText(sdt);
                dangSync[0] = false;
                JOptionPane.showMessageDialog(BanHangPanel.this,
                    "✅ Đăng ký thành viên thành công!\n" + ten + " đã được chọn.",
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(dlg, "Lưu thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        dlg.getRootPane().setDefaultButton(btnLuu);
        panelNut.add(btnHuy);
        panelNut.add(btnLuu);

        dlg.add(form, BorderLayout.CENTER);
        dlg.add(panelNut, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private JButton taoNut(String text, Color mauNen) {
        JButton btn = new JButton(text);
        btn.setFont(UIConstants.FONT_TABLE);
        btn.setBackground(mauNen);
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(120, 32));
        return btn;
    }
}
