import dao.DatabaseConnection;
import gui.LoginFrame;

import javax.swing.*;
import java.util.concurrent.CompletableFuture;

/**
 * Điểm khởi chạy ứng dụng Quản lý Bán Sữa.
 *
 * @author Phạm Duy An - BIT240002
 */
public class Main {
    public static void main(String[] args) {
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

        // Khởi tạo CSDL bất đồng bộ → hiển thị màn hình đăng nhập khi hoàn tất
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
