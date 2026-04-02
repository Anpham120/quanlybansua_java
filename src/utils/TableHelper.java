package utils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * Tiện ích tạo bảng (JTable) với giao diện thống nhất.
 * Bao gồm: hiệu ứng zebra stripe, header style, căn lề.
 *
 * @author Phạm Duy An - BIT240002
 */
public class TableHelper {

    /** Màu xen kẽ cho hàng chẵn/lẻ. */
    private static final Color ZEBRA_EVEN = Color.WHITE;
    private static final Color ZEBRA_ODD = new Color(248, 250, 252);

    /**
     * Áp dụng style chung cho JTable: font, chiều cao, zebra, header.
     */
    public static void applyStyle(JTable table) {
        table.setFont(UIConstants.FONT_TABLE);
        table.setRowHeight(28);
        table.setSelectionBackground(UIConstants.PRIMARY_LIGHT);
        table.setSelectionForeground(UIConstants.TEXT_DARK);
        table.setGridColor(UIConstants.BORDER_COLOR);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));

        // Kiểu dáng tiêu đề
        JTableHeader header = table.getTableHeader();
        header.setFont(UIConstants.FONT_TABLE_HEADER);
        header.setBackground(UIConstants.PRIMARY);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 36));
        header.setReorderingAllowed(false);
    }

    /**
     * Tạo DefaultTableCellRenderer với zebra stripe và căn lề tùy chỉnh.
     *
     * @param centerCols chỉ số các cột căn giữa
     * @param rightCols  chỉ số các cột căn phải
     */
    public static DefaultTableCellRenderer createZebraRenderer(int[] centerCols, int[] rightCols) {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

                // Màu nền
                if (isSelected) {
                    setBackground(UIConstants.PRIMARY_LIGHT);
                    setForeground(UIConstants.TEXT_DARK);
                } else {
                    setBackground(row % 2 == 0 ? ZEBRA_EVEN : ZEBRA_ODD);
                    setForeground(UIConstants.TEXT_DARK);
                }

                // Căn lề
                setHorizontalAlignment(SwingConstants.LEFT); // Mặc định
                for (int c : centerCols) {
                    if (col == c) { setHorizontalAlignment(SwingConstants.CENTER); break; }
                }
                for (int c : rightCols) {
                    if (col == c) { setHorizontalAlignment(SwingConstants.RIGHT); break; }
                }

                return this;
            }
        };
    }

    /**
     * Tạo DefaultTableModel không cho phép sửa trực tiếp trên bảng.
     */
    public static DefaultTableModel createReadOnlyModel(String[] columns) {
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    /**
     * Tạo JLabel dùng trong form (không bị co/stretch).
     */
    public static JLabel createFormLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(UIConstants.FONT_TABLE);
        Dimension d = lbl.getPreferredSize();
        lbl.setPreferredSize(d);
        lbl.setMinimumSize(d);
        return lbl;
    }

    /**
     * Tạo JButton nhỏ dùng trong panel (Thêm, Sửa, Xóa, Làm mới).
     */
    public static JButton createSmallButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(UIConstants.FONT_TABLE);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(90, 32));
        return btn;
    }
}
