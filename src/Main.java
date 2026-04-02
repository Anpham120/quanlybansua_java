import dao.DatabaseConnection;
import gui.LoginFrame;

import javax.swing.*;
import java.util.concurrent.CompletableFuture;

/**
 * Lớp khởi chạy ứng dụng Quản lý Bán Sữa.
 * Sử dụng CompletableFuture để khởi tạo CSDL bất đồng bộ,
 * tránh block luồng chính trong khi chờ kết nối MySQL.
 *
 * Nhóm thực hiện:
 * 1. Phạm Duy An - BIT240002 (Nền tảng, Đăng nhập, Tài khoản, EventBus)
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

        // Khởi tạo CSDL bất đồng bộ bằng CompletableFuture
        // runAsync(): chạy trên luồng nền (ForkJoinPool), không chặn luồng chính
        // thenRun(): sau khi hoàn tất → hiển thị màn hình đăng nhập trên luồng giao diện
        // exceptionally(): xử lý lỗi kết nối CSDL → hiện hộp thoại cảnh báo
        CompletableFuture.runAsync(() -> DatabaseConnection.khoiTaoDatabase())
                .thenRun(() -> SwingUtilities.invokeLater(() ->
                        new LoginFrame().setVisible(true)
                ))
                .exceptionally(ex -> {
                    System.err.println("Lỗi khởi tạo CSDL: " + ex.getMessage());
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(null,
                                    "Không thể kết nối cơ sở dữ liệu!\n" + ex.getMessage(),
                                    "Lỗi khởi động", JOptionPane.ERROR_MESSAGE)
                    );
                    return null;
                });
    }
}
