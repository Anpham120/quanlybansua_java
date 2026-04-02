package gui;

import dao.HoaDonDAO;
import utils.InputFilter;
import utils.UIConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

/**
 * Panel Lịch sử Hóa đơn (TV5).
 * Hiển thị danh sách hóa đơn, lọc theo ngày / SĐT,
 * click chọn hàng → xem chi tiết sản phẩm bên dưới.
 *
 * @author Nguyễn Quang Hiếu - BIT240091
 */
public class HoaDonPanel extends JPanel {

    private final HoaDonDAO hoaDonDAO = new HoaDonDAO();
    private final NumberFormat fmt = NumberFormat.getNumberInstance(Locale.of("vi", "VN"));

    // Bảng hóa đơn
    private DefaultTableModel modelHD;
    private JTable bangHD;

    // Bảng chi tiết
    private DefaultTableModel modelCT;

    // Filter controls
    private JTextField txtSDT, txtTuNgay, txtDenNgay;
    private JLabel lblTongLoc;

    public HoaDonPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(UIConstants.BG_MAIN);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Tiêu đề
        JLabel lblTieuDe = new JLabel("LỊCH SỬ HÓA ĐƠN");
        lblTieuDe.setFont(UIConstants.FONT_TITLE);
        lblTieuDe.setForeground(UIConstants.PRIMARY);
        add(lblTieuDe, BorderLayout.NORTH);

        // Nội dung: trên (bảng HD) + dưới (chi tiết)
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
            taoPanelTren(), taoPanelChiTiet());
        split.setDividerLocation(320);
        split.setResizeWeight(0.6);
        add(split, BorderLayout.CENTER);

        taiDuLieu(null, null, null);
    }

    // =========================================================
    // PHẦN TRÊN: Bộ lọc + bảng hóa đơn
    // =========================================================
    private JPanel taoPanelTren() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(UIConstants.BG_CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_COLOR),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // ── Filter bar ──
        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        filterBar.setBackground(UIConstants.BG_CARD);

        filterBar.add(new JLabel("SĐT khách:"));
        txtSDT = new JTextField(10);
        txtSDT.setFont(UIConstants.FONT_TABLE);
        // Chỉ cho nhập số vào ô SĐT
        InputFilter.applyDigitsOnly(txtSDT);
        filterBar.add(txtSDT);

        filterBar.add(new JLabel("Từ ngày:"));
        txtTuNgay = new JTextField("", 8);
        txtTuNgay.setFont(UIConstants.FONT_TABLE);
        txtTuNgay.setToolTipText("dd/MM/yyyy");
        filterBar.add(txtTuNgay);

        filterBar.add(new JLabel("Đến ngày:"));
        txtDenNgay = new JTextField("", 8);
        txtDenNgay.setFont(UIConstants.FONT_TABLE);
        txtDenNgay.setToolTipText("dd/MM/yyyy");
        filterBar.add(txtDenNgay);

        JButton btnLoc = UIConstants.createStyledButton("Lọc", UIConstants.INFO);
        btnLoc.addActionListener(e -> xuLyLoc());
        filterBar.add(btnLoc);

        JButton btnReset = UIConstants.createStyledButton("Tất cả", UIConstants.WARNING);
        btnReset.addActionListener(e -> {
            txtSDT.setText(""); txtTuNgay.setText(""); txtDenNgay.setText("");
            taiDuLieu(null, null, null);
        });
        filterBar.add(btnReset);

        panel.add(filterBar, BorderLayout.NORTH);

        // ── Bảng hóa đơn ──
        String[] cot = {"STT", "Mã HD", "Ngày lập", "Nhân viên", "Khách hàng", "Tổng tiền (đ)"};
        modelHD = new DefaultTableModel(cot, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        bangHD = new JTable(modelHD);
        bangHD.setFont(UIConstants.FONT_TABLE);
        bangHD.setRowHeight(28);
        bangHD.getTableHeader().setFont(UIConstants.FONT_TABLE_HEADER);
        bangHD.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bangHD.setGridColor(UIConstants.BORDER_COLOR);

        int[] w = {40, 60, 130, 130, 150, 120};
        for (int i = 0; i < w.length; i++) bangHD.getColumnModel().getColumn(i).setPreferredWidth(w[i]);

        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(SwingConstants.RIGHT);
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        bangHD.getColumnModel().getColumn(0).setCellRenderer(center);
        bangHD.getColumnModel().getColumn(1).setCellRenderer(center);
        bangHD.getColumnModel().getColumn(5).setCellRenderer(right);

        // Zebra stripes
        bangHD.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                if (sel) {
                    setBackground(UIConstants.PRIMARY_LIGHT);
                    setForeground(UIConstants.TEXT_DARK);
                } else {
                    setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                    setForeground(UIConstants.TEXT_DARK);
                }
                if (col == 0 || col == 1 || col == 2) setHorizontalAlignment(SwingConstants.CENTER);
                else if (col == 5) setHorizontalAlignment(SwingConstants.RIGHT);
                else setHorizontalAlignment(SwingConstants.LEFT);
                return this;
            }
        });

        // Click vào hàng → load chi tiết
        bangHD.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && bangHD.getSelectedRow() >= 0)
                hienChiTiet(bangHD.getSelectedRow());
        });

        panel.add(new JScrollPane(bangHD), BorderLayout.CENTER);

        // ── Footer: tổng ──
        lblTongLoc = new JLabel("Tổng doanh thu: 0 đ  |  0 hóa đơn", SwingConstants.RIGHT);
        lblTongLoc.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTongLoc.setForeground(UIConstants.PRIMARY);
        lblTongLoc.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 4));
        panel.add(lblTongLoc, BorderLayout.SOUTH);

        return panel;
    }

    // =========================================================
    // PHẦN DƯỚI: chi tiết hóa đơn được chọn
    // =========================================================
    private JPanel taoPanelChiTiet() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBackground(UIConstants.BG_CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_COLOR),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        JLabel lbl = new JLabel("Chi tiết sản phẩm");
        lbl.setFont(UIConstants.FONT_SUBTITLE);
        lbl.setForeground(UIConstants.PRIMARY);
        panel.add(lbl, BorderLayout.NORTH);

        String[] cotCT = {"STT", "Tên sản phẩm", "Số lượng", "Đơn giá (đ)", "Thành tiền (đ)"};
        modelCT = new DefaultTableModel(cotCT, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable bangCT = new JTable(modelCT);
        bangCT.setFont(UIConstants.FONT_TABLE);
        bangCT.setRowHeight(26);
        bangCT.getTableHeader().setFont(UIConstants.FONT_TABLE_HEADER);
        bangCT.setGridColor(UIConstants.BORDER_COLOR);
        bangCT.getColumnModel().getColumn(0).setPreferredWidth(40);
        bangCT.getColumnModel().getColumn(1).setPreferredWidth(230);
        bangCT.getColumnModel().getColumn(2).setPreferredWidth(70);
        bangCT.getColumnModel().getColumn(3).setPreferredWidth(110);
        bangCT.getColumnModel().getColumn(4).setPreferredWidth(120);

        DefaultTableCellRenderer right2 = new DefaultTableCellRenderer();
        right2.setHorizontalAlignment(SwingConstants.RIGHT);
        DefaultTableCellRenderer center2 = new DefaultTableCellRenderer();
        center2.setHorizontalAlignment(SwingConstants.CENTER);
        bangCT.getColumnModel().getColumn(0).setCellRenderer(center2);
        bangCT.getColumnModel().getColumn(2).setCellRenderer(center2);
        bangCT.getColumnModel().getColumn(3).setCellRenderer(right2);
        bangCT.getColumnModel().getColumn(4).setCellRenderer(right2);

        panel.add(new JScrollPane(bangCT), BorderLayout.CENTER);
        return panel;
    }

    // =========================================================
    // XỬ LÝ NGHIỆP VỤ
    // =========================================================

    /** Validate ngày rồi mới lọc. */
    private void xuLyLoc() {
        String tuNgay = txtTuNgay.getText().trim();
        String denNgay = txtDenNgay.getText().trim();
        String sdt = txtSDT.getText().trim();

        // Validate ngày nếu có nhập
        if (!tuNgay.isEmpty() && !kiemTraNgay(tuNgay)) {
            JOptionPane.showMessageDialog(this,
                "\"Từ ngày\" không hợp lệ!\nVui lòng nhập đúng định dạng dd/MM/yyyy (ví dụ: 01/03/2026)",
                "Sai định dạng ngày", JOptionPane.WARNING_MESSAGE);
            txtTuNgay.requestFocusInWindow();
            return;
        }
        if (!denNgay.isEmpty() && !kiemTraNgay(denNgay)) {
            JOptionPane.showMessageDialog(this,
                "\"Đến ngày\" không hợp lệ!\nVui lòng nhập đúng định dạng dd/MM/yyyy (ví dụ: 28/03/2026)",
                "Sai định dạng ngày", JOptionPane.WARNING_MESSAGE);
            txtDenNgay.requestFocusInWindow();
            return;
        }

        // Kiểm tra "Từ ngày" không được sau "Đến ngày"
        if (!tuNgay.isEmpty() && !denNgay.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                sdf.setLenient(false);
                java.util.Date dTu = sdf.parse(tuNgay);
                java.util.Date dDen = sdf.parse(denNgay);
                if (dTu.after(dDen)) {
                    JOptionPane.showMessageDialog(this,
                        "\"Từ ngày\" phải trước hoặc bằng \"Đến ngày\"!",
                        "Khoảng ngày không hợp lệ", JOptionPane.WARNING_MESSAGE);
                    txtTuNgay.requestFocusInWindow();
                    return;
                }
            } catch (ParseException ex) { /* Đã validate ở trên */ }
        }

        taiDuLieu(tuNgay, denNgay, sdt);
    }

    /** Kiểm tra chuỗi có đúng định dạng dd/MM/yyyy không. */
    private boolean kiemTraNgay(String ngay) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false); // Không cho phép ngày không hợp lệ (32/13/2026)
        try {
            sdf.parse(ngay);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private void taiDuLieu(String tuNgay, String denNgay, String sdt) {
        modelHD.setRowCount(0);
        List<Object[]> ds = (tuNgay == null && denNgay == null && (sdt == null || sdt.isEmpty()))
            ? hoaDonDAO.layTatCa()
            : hoaDonDAO.loc(tuNgay, denNgay, sdt);

        double tongDT = 0;
        int stt = 1;
        for (Object[] row : ds) {
            double tt = (double) row[4];
            tongDT += tt;
            modelHD.addRow(new Object[]{
                stt++,
                String.format("HD-%04d", (int) row[0]),
                row[1], row[2], row[3],
                fmt.format((long) tt)
            });
        }
        lblTongLoc.setText(String.format("Tổng doanh thu: %s đ  |  %d hóa đơn",
            fmt.format((long) tongDT), ds.size()));
        modelCT.setRowCount(0);
    }

    private void hienChiTiet(int dongDuocChon) {
        modelCT.setRowCount(0);
        // Lấy ID từ cột 0 (dạng "HD-0001")
        String maHD = (String) modelHD.getValueAt(dongDuocChon, 1);
        int idHD = Integer.parseInt(maHD.replace("HD-", "").trim());

        int sttCT = 1;
        for (Object[] ct : hoaDonDAO.layChiTiet(idHD)) {
            modelCT.addRow(new Object[]{
                sttCT++,
                ct[0],
                ct[1],
                fmt.format((long)(double) ct[2]),
                fmt.format((long)(double) ct[3])
            });
        }
    }
}
