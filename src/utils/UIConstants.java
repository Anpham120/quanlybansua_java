package utils;

import java.awt.*;
import javax.swing.*;

/**
 * Hằng số giao diện dùng chung cho toàn bộ ứng dụng.
 * 
 * @author Phạm Duy An - BIT240002
 */
public class UIConstants {

    // ===== MÀU SẮC =====
    public static final Color PRIMARY = new Color(41, 128, 185);
    public static final Color PRIMARY_DARK = new Color(31, 97, 141);
    public static final Color PRIMARY_LIGHT = new Color(174, 214, 241);
    public static final Color SUCCESS = new Color(39, 174, 96);
    public static final Color DANGER = new Color(231, 76, 60);
    public static final Color WARNING = new Color(243, 156, 18);
    public static final Color BG_MAIN = new Color(236, 240, 241);
    public static final Color BG_SIDEBAR = new Color(44, 62, 80);
    public static final Color BG_SIDEBAR_HOVER = new Color(52, 73, 94);
    public static final Color BG_SIDEBAR_ACTIVE = new Color(41, 128, 185);
    public static final Color BG_CARD = Color.WHITE;
    public static final Color INFO = new Color(52, 152, 219);
    public static final Color TEXT_DARK = new Color(44, 62, 80);
    public static final Color TEXT_LIGHT = Color.WHITE;
    public static final Color BORDER_COLOR = new Color(189, 195, 199);

    // ===== FONT CHỮ =====
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_LABEL = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_INPUT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_TABLE_HEADER = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_TABLE = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_MENU = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);

    // ===== KÍCH THƯỚC =====
    public static final int SIDEBAR_WIDTH = 220;
    public static final int HEADER_HEIGHT = 55;
    public static final int MENU_ITEM_HEIGHT = 45;
    public static final Dimension BUTTON_SIZE = new Dimension(110, 35);

    /**
     * Tạo JButton với style thống nhất.
     */
    public static JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BUTTON);
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(BUTTON_SIZE);
        return btn;
    }
}
