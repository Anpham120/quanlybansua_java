package utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Hằng số nghiệp vụ dùng chung cho toàn bộ ứng dụng.
 *
 * @author Phạm Duy An - BIT240002
 */
public class AppConstants {

    // ===== QUY TẮC TÍCH ĐIỂM =====
    /** Mỗi 100.000đ mua hàng = 1 điểm tích lũy. */
    public static final int TIEN_MOI_DIEM = 100_000;

    // ===== HẠNG KHÁCH HÀNG =====
    public static final int DIEM_BAC = 50;
    public static final int DIEM_VANG = 150;
    public static final int DIEM_KIM_CUONG = 300;

    public static final double GIAM_BAC = 0.03;       // 3%
    public static final double GIAM_VANG = 0.05;       // 5%
    public static final double GIAM_KIM_CUONG = 0.10;  // 10%

    // ===== ĐỊNH DẠNG TIỀN =====
    /** Format tiền Việt Nam: 35.000 (dấu chấm phân cách hàng nghìn). */
    public static final DecimalFormat FORMAT_TIEN;

    static {
        FORMAT_TIEN = new DecimalFormat("#,###");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        FORMAT_TIEN.setDecimalFormatSymbols(symbols);
    }

    // ===== VALIDATE =====
    public static final int TEN_DN_MIN = 4;
    public static final int MAT_KHAU_MIN = 6;
    public static final int TUOI_LAM_VIEC_MIN = 18;

    // ===== VAI TRÒ =====
    public static final String VAI_TRO_QUAN_LY = "Quản lý";
    public static final String VAI_TRO_NHAN_VIEN = "Nhân viên";
}
