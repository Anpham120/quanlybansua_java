package utils;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Bộ lọc nhập liệu dùng chung cho toàn bộ ứng dụng.
 * Tránh lặp DocumentFilter ở từng Panel.
 *
 * @author Phạm Duy An - BIT240002
 */
public class InputFilter {

    /**
     * Chỉ cho phép nhập chữ số (0-9) vào JTextField.
     * Dùng cho: SĐT, Số lượng, Điểm tích lũy.
     */
    public static void applyDigitsOnly(JTextField field) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int off, String str, AttributeSet a)
                    throws BadLocationException {
                if (str != null && str.matches("\\d*")) super.insertString(fb, off, str, a);
            }

            @Override
            public void replace(FilterBypass fb, int off, int len, String str, AttributeSet a)
                    throws BadLocationException {
                if (str != null && str.matches("\\d*")) super.replace(fb, off, len, str, a);
            }
        });
    }

    /**
     * Chỉ cho phép nhập chữ số và dấu chấm (phân cách hàng nghìn).
     * Dùng cho: Đơn giá sản phẩm.
     */
    public static void applyDigitsAndDot(JTextField field) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int off, String str, AttributeSet a)
                    throws BadLocationException {
                if (str != null && str.matches("[\\d.]*")) super.insertString(fb, off, str, a);
            }

            @Override
            public void replace(FilterBypass fb, int off, int len, String str, AttributeSet a)
                    throws BadLocationException {
                if (str != null && str.matches("[\\d.]*")) super.replace(fb, off, len, str, a);
            }
        });
    }
}
