import dao.DatabaseConnection;
import gui.LoginFrame;
import javax.swing.*;

/**
 * Lớp khởi chạy ứng dụng Quản lý Bán Sữa.
 * 
 * Nhóm thực hiện:
 * 1. Phạm Duy An - BIT240002 (Nền tảng, Đăng nhập, Tài khoản)
 * 2. Bùi Đào Đức Anh - BIT240025 (Quản lý Sản phẩm)
 * 3. Đỗ Tuấn Anh - BIT240015 (Quản lý Khách hàng, Nhân viên)
 * 4. Nguyễn Quang Hiếu - BIT240091 (Bán hàng)
 * 5. Phan Văn Hiếu - BIT240094 (Hóa đơn, Thống kê)
 */
public class Main {
    public static void main(String[] args) {
        // Đặt giao diện Nimbus cho đẹp hơn giao diện mặc định
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Không thể sử dụng giao diện Nimbus.");
        }

        // Khởi tạo cơ sở dữ liệu (tạo bảng + dữ liệu mẫu nếu chưa có)
        DatabaseConnection.khoiTaoDatabase();

        // Hiển thị màn hình đăng nhập
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
