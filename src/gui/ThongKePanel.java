package gui;

import dao.HoaDonDAO;
import dao.SanPhamDAO;
import utils.EventBus;
import utils.UIConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Panel Thống kê doanh thu nâng cao.
 * - 4 thẻ KPI (doanh thu, hóa đơn, SP sắp hết hạn, SP hết hàng)
 * - Lọc theo khoảng ngày
 * - Biểu đồ cột (Graphics2D)
 * - Top 5 SP bán chạy
 * - Xuất báo cáo CSV
 *
 * @author Nguyễn Quang Hiếu - BIT240082
 */
public class ThongKePanel extends JPanel {

    private final HoaDonDAO hoaDonDAO = new HoaDonDAO();
    private final SanPhamDAO sanPhamDAO = new SanPhamDAO();
    private final NumberFormat fmt = NumberFormat.getNumberInstance(Locale.of("vi", "VN"));

    // Nhãn KPI
    private JLabel lblDoanhThu, lblSoHD, lblSapHetHan, lblHetHang;

    // Bộ lọc ngày
    private JSpinner spinTuNgay, spinDenNgay;

    // Biểu đồ + bảng
    private BarChartPanel chartPanel;
    private DefaultTableModel modelTop;

    // Trạng thái lọc
    private boolean dangLocKhoang = false;

    public ThongKePanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(UIConstants.BG_MAIN);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(taoHeader());
        add(Box.createVerticalStrut(8));
        add(taoHangLocNgay());
        add(Box.createVerticalStrut(8));
        add(taoHangKPI());
        add(Box.createVerticalStrut(10));
        add(taoHangDuoi());

        taiDuLieu();

        // Tự động refresh khi chuyển sang tab Thống kê
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                if (!dangLocKhoang) taiDuLieu();
            }
        });

        // Đăng ký lắng nghe sự kiện: tự cập nhật khi BanHangPanel thanh toán thành công
        EventBus.subscribe(EventBus.SU_KIEN_THANH_TOAN, data -> {
            if (!dangLocKhoang) taiDuLieu();
        });
    }

    // =========================================================
    // XÂY DỰNG GIAO DIỆN
    // =========================================================

    private JPanel taoHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIConstants.BG_MAIN);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel lblTieuDe = new JLabel("THỐNG KÊ DOANH THU");
        lblTieuDe.setFont(UIConstants.FONT_TITLE);
        lblTieuDe.setForeground(UIConstants.PRIMARY);
        header.add(lblTieuDe, BorderLayout.WEST);

        JPanel panelNut = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelNut.setBackground(UIConstants.BG_MAIN);

        JButton btnXuatCSV = UIConstants.createStyledButton("Xuất CSV", new Color(39, 174, 96));
        btnXuatCSV.addActionListener(e -> xuatCSV());
        panelNut.add(btnXuatCSV);

        JButton btnRefresh = UIConstants.createStyledButton("Làm mới", UIConstants.INFO);
        btnRefresh.addActionListener(e -> {
            dangLocKhoang = false;
            taiDuLieu();
        });
        panelNut.add(btnRefresh);

        header.add(panelNut, BorderLayout.EAST);
        return header;
    }

    private JPanel taoHangLocNgay() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        panel.setBackground(UIConstants.BG_MAIN);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        panel.add(taoLabel("Từ ngày:"));
        spinTuNgay = taoSpinnerNgay(-30); // mặc định 30 ngày trước
        panel.add(spinTuNgay);

        panel.add(taoLabel("Đến ngày:"));
        spinDenNgay = taoSpinnerNgay(0); // hôm nay
        panel.add(spinDenNgay);

        JButton btnLoc = UIConstants.createStyledButton("Lọc theo khoảng", UIConstants.PRIMARY);
        btnLoc.addActionListener(e -> locTheoKhoang());
        panel.add(btnLoc);

        return panel;
    }

    private JSpinner taoSpinnerNgay(int offsetNgay) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, offsetNgay);
        SpinnerDateModel model = new SpinnerDateModel(cal.getTime(), null, null, Calendar.DAY_OF_MONTH);
        JSpinner spinner = new JSpinner(model);
        spinner.setEditor(new JSpinner.DateEditor(spinner, "dd/MM/yyyy"));
        spinner.setFont(UIConstants.FONT_TABLE);
        spinner.setPreferredSize(new Dimension(120, 28));
        return spinner;
    }

    private JPanel taoHangKPI() {
        JPanel row = new JPanel(new GridLayout(1, 4, 12, 0));
        row.setBackground(UIConstants.BG_MAIN);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        lblDoanhThu = new JLabel("0 đ", SwingConstants.CENTER);
        lblSoHD     = new JLabel("0",   SwingConstants.CENTER);
        lblSapHetHan = new JLabel("0",  SwingConstants.CENTER);
        lblHetHang   = new JLabel("0",  SwingConstants.CENTER);

        row.add(taoKPICard("Doanh thu", lblDoanhThu, UIConstants.SUCCESS));
        row.add(taoKPICard("Số hóa đơn", lblSoHD, UIConstants.PRIMARY));
        row.add(taoKPICard("SP sắp hết hạn (<=7 ngày)", lblSapHetHan, new Color(230, 126, 34)));
        row.add(taoKPICard("SP hết hàng", lblHetHang, UIConstants.DANGER));
        return row;
    }

    private JPanel taoKPICard(String title, JLabel lblValue, Color accent) {
        JPanel card = new JPanel(new BorderLayout(0, 6));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, accent),
            BorderFactory.createEmptyBorder(14, 16, 14, 16)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(UIConstants.FONT_LABEL);
        lblTitle.setForeground(new Color(127, 140, 141));
        card.add(lblTitle, BorderLayout.NORTH);

        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblValue.setForeground(accent);
        card.add(lblValue, BorderLayout.CENTER);

        return card;
    }

    private JPanel taoHangDuoi() {
        JPanel row = new JPanel(new GridLayout(1, 2, 12, 0));
        row.setBackground(UIConstants.BG_MAIN);

        // ── Biểu đồ cột ──
        JPanel cardChart = taoCard("Biểu đồ doanh thu");
        chartPanel = new BarChartPanel();
        cardChart.add(chartPanel, BorderLayout.CENTER);
        row.add(cardChart);

        // ── Top 5 sản phẩm ──
        JPanel cardTop = taoCard("Top 5 sản phẩm bán chạy");
        String[] cot = {"Sản phẩm", "SL bán", "Doanh thu (đ)"};
        modelTop = new DefaultTableModel(cot, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable bangTop = new JTable(modelTop);
        bangTop.setFont(UIConstants.FONT_TABLE);
        bangTop.setRowHeight(28);
        bangTop.getTableHeader().setFont(UIConstants.FONT_TABLE_HEADER);
        bangTop.setGridColor(UIConstants.BORDER_COLOR);
        bangTop.getColumnModel().getColumn(0).setPreferredWidth(200);
        bangTop.getColumnModel().getColumn(1).setPreferredWidth(60);
        bangTop.getColumnModel().getColumn(2).setPreferredWidth(120);

        // Zebra stripes + align
        bangTop.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                if (sel) { setBackground(UIConstants.PRIMARY_LIGHT); }
                else { setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252)); }
                setForeground(UIConstants.TEXT_DARK);
                if (col == 1) setHorizontalAlignment(SwingConstants.CENTER);
                else if (col == 2) setHorizontalAlignment(SwingConstants.RIGHT);
                else setHorizontalAlignment(SwingConstants.LEFT);
                return this;
            }
        });

        cardTop.add(new JScrollPane(bangTop), BorderLayout.CENTER);
        row.add(cardTop);
        return row;
    }

    private JPanel taoCard(String title) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_COLOR),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        JLabel lbl = new JLabel(title);
        lbl.setFont(UIConstants.FONT_SUBTITLE);
        lbl.setForeground(UIConstants.TEXT_DARK);
        card.add(lbl, BorderLayout.NORTH);
        return card;
    }

    // =========================================================
    // XỬ LÝ NGHIỆP VỤ
    // =========================================================

    /**
     * Tải dữ liệu mặc định (hôm nay / 7 ngày) bằng SwingWorker.
     * doInBackground(): chạy trên background thread — gọi DB không block UI.
     * done(): chạy trên EDT — cập nhật giao diện an toàn.
     */
    @SuppressWarnings("unchecked")
    private void taiDuLieu() {
        new SwingWorker<Map<String, Object>, Void>() {
            @Override
            protected Map<String, Object> doInBackground() {
                // Chạy trên background thread — không block giao diện
                Map<String, Object> result = new HashMap<>();
                result.put("doanhThu", hoaDonDAO.doanhThuHomNay());
                result.put("soHD", hoaDonDAO.soHoaDonHomNay());
                result.put("sapHetHan", sanPhamDAO.demSapHetHan(7));
                result.put("hetHang", sanPhamDAO.demHetHang());
                result.put("chart", hoaDonDAO.doanhThu7Ngay());
                result.put("top5", hoaDonDAO.topSanPham(5));
                return result;
            }

            @Override
            protected void done() {
                try {
                    // Cập nhật UI trên EDT (Event Dispatch Thread)
                    Map<String, Object> r = get();
                    capNhatKPI(
                            (double) r.get("doanhThu"),
                            (int) r.get("soHD"),
                            (int) r.get("sapHetHan"),
                            (int) r.get("hetHang")
                    );
                    chartPanel.setData((List<Object[]>) r.get("chart"));
                    chartPanel.repaint();
                    capNhatTop5((List<Object[]>) r.get("top5"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.execute();
    }

    /** Cập nhật 4 thẻ KPI trên giao diện. */
    private void capNhatKPI(double doanhThu, int soHD, int sapHetHan, int hetHang) {
        lblDoanhThu.setText(fmt.format((long) doanhThu) + " đ");
        lblSoHD.setText(String.valueOf(soHD));
        lblSapHetHan.setText(String.valueOf(sapHetHan));
        lblHetHang.setText(String.valueOf(hetHang));

        // Tô màu cảnh báo
        lblSapHetHan.setForeground(sapHetHan > 0 ? new Color(230, 126, 34) : UIConstants.SUCCESS);
        lblHetHang.setForeground(hetHang > 0 ? UIConstants.DANGER : UIConstants.SUCCESS);
    }

    /**
     * Lọc thống kê theo khoảng ngày đã chọn — dùng SwingWorker.
     */
    @SuppressWarnings("unchecked")
    private void locTheoKhoang() {
        java.util.Date tuUtil = (java.util.Date) spinTuNgay.getValue();
        java.util.Date denUtil = (java.util.Date) spinDenNgay.getValue();

        if (tuUtil.after(denUtil)) {
            JOptionPane.showMessageDialog(this,
                "Ngày bắt đầu phải trước ngày kết thúc!",
                "Lỗi khoảng ngày", JOptionPane.WARNING_MESSAGE);
            return;
        }

        java.sql.Date tuNgay = new java.sql.Date(tuUtil.getTime());
        java.sql.Date denNgay = new java.sql.Date(denUtil.getTime());
        dangLocKhoang = true;
        long diffDays = (denUtil.getTime() - tuUtil.getTime()) / (1000 * 60 * 60 * 24);

        new SwingWorker<Map<String, Object>, Void>() {
            @Override
            protected Map<String, Object> doInBackground() {
                Map<String, Object> result = new HashMap<>();
                result.put("doanhThu", hoaDonDAO.doanhThuTheoKhoang(tuNgay, denNgay));
                result.put("soHD", hoaDonDAO.soHoaDonTheoKhoang(tuNgay, denNgay));
                result.put("sapHetHan", sanPhamDAO.demSapHetHan(7));
                result.put("hetHang", sanPhamDAO.demHetHang());
                if (diffDays <= 31) {
                    result.put("chart", hoaDonDAO.doanhThuTheoKhoangNgay(tuNgay, denNgay));
                } else {
                    result.put("chart", hoaDonDAO.doanhThu7Ngay());
                }
                result.put("top5", hoaDonDAO.topSanPhamTheoKhoang(5, tuNgay, denNgay));
                return result;
            }

            @Override
            protected void done() {
                try {
                    Map<String, Object> r = get();
                    capNhatKPI(
                            (double) r.get("doanhThu"),
                            (int) r.get("soHD"),
                            (int) r.get("sapHetHan"),
                            (int) r.get("hetHang")
                    );
                    chartPanel.setData((List<Object[]>) r.get("chart"));
                    chartPanel.repaint();
                    capNhatTop5((List<Object[]>) r.get("top5"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.execute();
    }

    private void capNhatTop5(List<Object[]> data) {
        modelTop.setRowCount(0);
        int rank = 1;
        for (Object[] row : data) {
            modelTop.addRow(new Object[]{
                rank++ + ". " + row[0],
                row[1],
                fmt.format((long)(double) row[2])
            });
        }
    }

    /** Xuất dữ liệu hiện tại ra file CSV. */
    private void xuatCSV() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Chọn nơi lưu báo cáo CSV");
        fc.setSelectedFile(new java.io.File("bao_cao_doanh_thu.csv"));
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files", "csv"));

        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        java.io.File file = fc.getSelectedFile();
        if (!file.getName().endsWith(".csv")) {
            file = new java.io.File(file.getAbsolutePath() + ".csv");
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(file, java.nio.charset.StandardCharsets.UTF_8))) {
            // BOM cho Excel đọc UTF-8
            pw.write('\uFEFF');

            // Header thông tin
            pw.println("BÁO CÁO DOANH THU - QUẢN LÝ BÁN SỮA");
            pw.println("Ngày xuất:," + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date()));
            pw.println();

            // KPI
            pw.println("CHỈ SỐ TỔNG QUAN");
            pw.println("Doanh thu:," + lblDoanhThu.getText());
            pw.println("Số hóa đơn:," + lblSoHD.getText());
            pw.println("SP sắp hết hạn:," + lblSapHetHan.getText());
            pw.println("SP hết hàng:," + lblHetHang.getText());
            pw.println();

            // Top 5
            pw.println("TOP 5 SẢN PHẨM BÁN CHẠY");
            pw.println("STT,Sản phẩm,SL bán,Doanh thu (đ)");
            for (int i = 0; i < modelTop.getRowCount(); i++) {
                pw.println((i + 1) + ","
                    + modelTop.getValueAt(i, 0).toString().replaceFirst("^\\d+\\.\\s*", "") + ","
                    + modelTop.getValueAt(i, 1) + ","
                    + modelTop.getValueAt(i, 2));
            }

            JOptionPane.showMessageDialog(this,
                "Xuất báo cáo thành công!\n" + file.getAbsolutePath(),
                "Thành công", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Lỗi khi xuất file: " + ex.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =========================================================
    // HỖ TRỢ
    // =========================================================
    private JLabel taoLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(UIConstants.FONT_TABLE);
        return lbl;
    }

    // =========================================================
    // INNER: Bar chart thuần Graphics2D
    // =========================================================
    static class BarChartPanel extends JPanel {
        private List<Object[]> data; // {label, value}

        BarChartPanel() {
            setBackground(Color.WHITE);
        }

        void setData(List<Object[]> data) { this.data = data; }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (data == null || data.isEmpty()) {
                g.setColor(Color.GRAY);
                g.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                g.drawString("Chưa có dữ liệu", getWidth() / 2 - 50, getHeight() / 2);
                return;
            }

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int pad    = 40;
            int botPad = 36;
            int w      = getWidth()  - pad * 2;
            int h      = getHeight() - pad - botPad;

            // Max value
            double maxVal = data.stream()
                .mapToDouble(r -> (double) r[1]).max().orElse(1);
            if (maxVal == 0) maxVal = 1;

            int barW = Math.max(6, w / data.size() - 8);
            NumberFormat fmtM = NumberFormat.getNumberInstance(Locale.of("vi", "VN"));

            for (int i = 0; i < data.size(); i++) {
                String label = (String) data.get(i)[0];
                double val   = (double)  data.get(i)[1];
                int barH = (int) (val / maxVal * h);
                int x    = pad + i * (w / data.size()) + (w / data.size() - barW) / 2;
                int y    = pad + h - barH;

                // Gradient bar
                GradientPaint gp = new GradientPaint(x, y,
                    new Color(41, 128, 185), x, y + barH, new Color(108, 180, 238));
                g2.setPaint(gp);
                g2.fillRoundRect(x, y, barW, barH, 6, 6);

                // Giá trị trên bar
                g2.setColor(UIConstants.TEXT_DARK);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
                String valStr = val >= 1_000_000
                    ? fmtM.format((long)(val / 1_000_000)) + "M"
                    : fmtM.format((long) val);
                FontMetrics fm = g2.getFontMetrics();
                int vx = x + (barW - fm.stringWidth(valStr)) / 2;
                if (barH > 16) g2.drawString(valStr, vx, y - 3);

                // Label ngày dưới
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                fm = g2.getFontMetrics();
                int lx = x + (barW - fm.stringWidth(label)) / 2;
                g2.drawString(label, lx, pad + h + 16);
            }

            // Trục X
            g2.setColor(UIConstants.BORDER_COLOR);
            g2.drawLine(pad, pad + h, pad + w, pad + h);
        }
    }
}
