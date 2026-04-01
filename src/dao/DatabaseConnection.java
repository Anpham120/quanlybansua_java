package dao;

import java.sql.*;

/**
 * Quản lý kết nối MySQL và khởi tạo cơ sở dữ liệu.
 * Tự động tạo database, bảng và dữ liệu mẫu khi chạy lần đầu.
 * 
 * @author Phạm Duy An - BIT240002
 */
public class DatabaseConnection {

    // ===== CẤU HÌNH KẾT NỐI - THAY ĐỔI NẾU CẦN =====
    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String TEN_CSDL = "quanlybansua";
    private static final String TAI_KHOAN = "root";
    private static final String MAT_KHAU = "root123"; // <-- Đổi mật khẩu MySQL tại đây

    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/";
    private static final String URL_CSDL = URL + TEN_CSDL
            + "?useUnicode=true&characterEncoding=UTF-8&useSSL=false"
            + "&allowPublicKeyRetrieval=true&serverTimezone=Asia/Ho_Chi_Minh";

    /**
     * Lấy kết nối đến cơ sở dữ liệu.
     */
    public static Connection layKetNoi() throws SQLException {
        return DriverManager.getConnection(URL_CSDL, TAI_KHOAN, MAT_KHAU);
    }

    /**
     * Đóng kết nối an toàn.
     */
    public static void dongKetNoi(Connection conn) {
        if (conn != null) {
            try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    /**
     * Khởi tạo toàn bộ cơ sở dữ liệu: tạo DB, tạo bảng, thêm dữ liệu mẫu.
     */
    public static void khoiTaoDatabase() {
        taoCSDL();
        taoBang();
        themDuLieuMau();
        System.out.println("=== Co so du lieu da duoc khoi tao thanh cong! ===");
    }

    private static void taoCSDL() {
        try (Connection conn = DriverManager.getConnection(URL, TAI_KHOAN, MAT_KHAU);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + TEN_CSDL
                    + " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
        } catch (SQLException e) {
            System.err.println("Loi tao CSDL: " + e.getMessage());
        }
    }

    private static void taoBang() {
        try (Connection conn = layKetNoi();
             Statement stmt = conn.createStatement()) {

            // Bảng 1: Tài khoản
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS tai_khoan ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "ten_dang_nhap VARCHAR(50) UNIQUE NOT NULL, "
                    + "mat_khau VARCHAR(100) NOT NULL, "
                    + "vai_tro VARCHAR(20) DEFAULT 'nhân viên'"
                    + ")");

            // Bảng 2: Nhân viên
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS nhan_vien ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "ho_ten VARCHAR(100) NOT NULL, "
                    + "sdt VARCHAR(15), "
                    + "dia_chi VARCHAR(200), "
                    + "ngay_sinh DATE, "
                    + "gioi_tinh VARCHAR(5), "
                    + "id_tai_khoan INT, "
                    + "FOREIGN KEY (id_tai_khoan) REFERENCES tai_khoan(id)"
                    + ")");

            // Bảng 3: Sản phẩm
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS san_pham ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "ten_sp VARCHAR(150) NOT NULL, "
                    + "loai VARCHAR(50), "
                    + "thuong_hieu VARCHAR(100), "
                    + "don_gia DOUBLE NOT NULL, "
                    + "so_luong INT DEFAULT 0, "
                    + "mo_ta TEXT, "
                    + "ngay_san_xuat DATE, "
                    + "han_su_dung DATE"
                    + ")");

            // Bảng 4: Khách hàng
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS khach_hang ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "ho_ten VARCHAR(100) NOT NULL, "
                    + "sdt VARCHAR(15), "
                    + "dia_chi VARCHAR(200), "
                    + "diem_tich_luy INT DEFAULT 0"
                    + ")");

            // Bảng 5: Hóa đơn
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS hoa_don ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "id_nhan_vien INT, "
                    + "id_khach_hang INT, "
                    + "ngay_lap DATETIME DEFAULT CURRENT_TIMESTAMP, "
                    + "tong_tien DOUBLE DEFAULT 0, "
                    + "FOREIGN KEY (id_nhan_vien) REFERENCES nhan_vien(id), "
                    + "FOREIGN KEY (id_khach_hang) REFERENCES khach_hang(id)"
                    + ")");

            // Bảng 6: Chi tiết hóa đơn
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS chi_tiet_hoa_don ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "id_hoa_don INT, "
                    + "id_san_pham INT, "
                    + "so_luong INT NOT NULL, "
                    + "don_gia DOUBLE NOT NULL, "
                    + "thanh_tien DOUBLE NOT NULL, "
                    + "FOREIGN KEY (id_hoa_don) REFERENCES hoa_don(id), "
                    + "FOREIGN KEY (id_san_pham) REFERENCES san_pham(id)"
                    + ")");

            System.out.println("Tat ca bang da duoc tao thanh cong.");
        } catch (SQLException e) {
            System.err.println("Loi tao bang: " + e.getMessage());
        }
    }

    private static void themDuLieuMau() {
        try (Connection conn = layKetNoi();
             Statement stmt = conn.createStatement()) {

            // Kiểm tra nếu đã có dữ liệu thì bỏ qua
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM tai_khoan");
            rs.next();
            if (rs.getInt(1) > 0) {
                System.out.println("Du lieu mau da ton tai, bo qua.");
                return;
            }

            // --- Tài khoản mẫu (5 thành viên nhóm) ---
            stmt.executeUpdate("INSERT INTO tai_khoan (ten_dang_nhap, mat_khau, vai_tro) VALUES "
                    + "('duyan', 'admin123', 'Quản lý'), "
                    + "('ducanh', '123456', 'Nhân viên'), "
                    + "('tuananh', '123456', 'Nhân viên'), "
                    + "('quanghieu', '123456', 'Nhân viên'), "
                    + "('vanhieu', '123456', 'Nhân viên')");

            // --- Nhân viên mẫu (4 nhân viên — Quản lý không nằm trong bảng NV) ---
            stmt.executeUpdate("INSERT INTO nhan_vien (ho_ten, sdt, dia_chi, ngay_sinh, gioi_tinh, id_tai_khoan) VALUES "
                    + "('Bùi Đào Đức Anh', '0902222222', '45 Trần Đại Nghĩa, Hai Bà Trưng, Hà Nội', '2006-02-01', 'Nam', 2), "
                    + "('Đỗ Tuấn Anh', '0903333333', '78 Giải Phóng, Đống Đa, Hà Nội', '2006-03-01', 'Nam', 3), "
                    + "('Nguyễn Quang Hiếu', '0904444444', '23 Nguyễn Trãi, Thanh Xuân, Hà Nội', '2006-04-01', 'Nam', 4), "
                    + "('Phan Văn Hiếu', '0905555555', '56 Lê Thanh Nghị, Hai Bà Trưng, Hà Nội', '2006-05-01', 'Nam', 5)");

            // --- Sản phẩm sữa mẫu (đa dạng 6 loại) ---
            stmt.executeUpdate("INSERT INTO san_pham (ten_sp, loai, thuong_hieu, don_gia, so_luong, mo_ta, ngay_san_xuat, han_su_dung) VALUES "
                    + "('Sữa tươi Vinamilk 1L', 'Sữa tươi', 'Vinamilk', 32000, 100, 'Sữa tươi tiệt trùng không đường', '2026-01-01', '2026-07-01'), "
                    + "('Sữa tươi TH True Milk 1L', 'Sữa tươi', 'TH', 35000, 80, 'Sữa tươi sạch nguyên chất', '2026-01-15', '2026-07-15'), "
                    + "('Sữa tươi Dutch Lady 1L', 'Sữa tươi', 'Dutch Lady', 33000, 70, 'Sữa tươi tiệt trùng', '2026-01-10', '2026-07-10'), "
                    + "('Sữa tươi Milo 180ml', 'Sữa tươi', 'Nestlé', 8000, 300, 'Thức uống lúa mạch Milo', '2026-02-01', '2026-08-01'), "
                    + "('Sữa bột Ensure Gold 850g', 'Sữa bột', 'Abbott', 750000, 30, 'Sữa bột dinh dưỡng người lớn', '2025-12-01', '2027-12-01'), "
                    + "('Sữa bột Optimum Gold 900g', 'Sữa bột', 'Vinamilk', 320000, 40, 'Sữa bột cho trẻ em', '2026-01-01', '2027-06-01'), "
                    + "('Sữa bột Anlene Gold 800g', 'Sữa bột', 'Fonterra', 420000, 25, 'Sữa bột xương khớp', '2025-11-01', '2027-11-01'), "
                    + "('Sữa chua Vinamilk có đường', 'Sữa chua', 'Vinamilk', 7000, 200, 'Sữa chua ăn có đường 100g', '2026-02-01', '2026-04-01'), "
                    + "('Sữa chua uống Yakult 65ml', 'Sữa chua', 'Yakult', 5500, 250, 'Sữa chua uống men sống', '2026-02-15', '2026-04-15'), "
                    + "('Sữa đặc Ông Thọ 380g', 'Sữa đặc', 'Vinamilk', 18000, 150, 'Sữa đặc có đường', '2026-01-01', '2026-12-01'), "
                    + "('Sữa đặc Ngôi Sao Phương Nam 380g', 'Sữa đặc', 'Vinamilk', 17000, 120, 'Sữa đặc có đường', '2026-01-15', '2026-12-15'), "
                    + "('Sữa hộp Vinamilk ADM Gold 110ml', 'Sữa hộp', 'Vinamilk', 9500, 180, 'Sữa dinh dưỡng hộp giấy', '2026-01-01', '2026-09-01'), "
                    + "('Sữa hộp Dutch Lady 110ml', 'Sữa hộp', 'Dutch Lady', 8500, 200, 'Sữa tiệt trùng hộp nhỏ', '2026-02-01', '2026-10-01'), "
                    + "('Sữa hộp NutiFood GrowPlus 110ml', 'Sữa hộp', 'NutiFood', 9000, 150, 'Sữa tăng trưởng hộp giấy', '2026-01-20', '2026-09-20'), "
                    + "('Sữa hạt TH True Nut Óc Chó 1L', 'Sữa hạt', 'TH', 45000, 60, 'Sữa hạt óc chó nguyên chất', '2026-01-01', '2026-06-01'), "
                    + "('Sữa hạt Vinamilk Super Nut 1L', 'Sữa hạt', 'Vinamilk', 42000, 50, 'Sữa 5 loại hạt dinh dưỡng', '2026-02-01', '2026-07-01'), "
                    + "('Sữa hạt Mộc Châu Hạnh Nhân 180ml', 'Sữa hạt', 'Mộc Châu', 12000, 100, 'Sữa hạt hạnh nhân tự nhiên', '2026-01-15', '2026-06-15')");

            // --- Khách hàng mẫu ---
            stmt.executeUpdate("INSERT INTO khach_hang (ho_ten, sdt, dia_chi, diem_tich_luy) VALUES "
                    + "('Hoàng Minh Đức', '0911234567', '15 Bà Triệu, Hoàn Kiếm, Hà Nội', 50), "
                    + "('Vũ Thị Hương', '0922345678', '102 Đội Cấn, Ba Đình, Hà Nội', 120), "
                    + "('Trịnh Văn Long', '0933456789', '37 Thái Hà, Đống Đa, Hà Nội', 30), "
                    + "('Đặng Thị Ngọc', '0944567890', '88 Kim Mã, Ba Đình, Hà Nội', 200), "
                    + "('Lý Thanh Tùng', '0955678901', '64 Trường Chinh, Thanh Xuân, Hà Nội', 75)");

            System.out.println("Du lieu mau da duoc them thanh cong.");
        } catch (SQLException e) {
            System.err.println("Loi them du lieu mau: " + e.getMessage());
        }
    }
}
