package dao;

import model.ChiTietHoaDon;
import utils.AppConstants;
import java.sql.*;
import java.util.List;

/**
 * DAO xử lý việc tạo hóa đơn và chi tiết hóa đơn trong 1 transaction.
 * Bao gồm: trừ tồn kho + cộng điểm khách hàng.
 *
 * @author Nguyễn Quang Hiếu - BIT240082
 */
public class HoaDonDAO {

    /**
     * Tạo hóa đơn hoàn chỉnh trong 1 transaction:
     * 1. INSERT hoa_don
     * 2. INSERT chi_tiet_hoa_don (nhiều dòng)
     * 3. Trừ tồn kho từng sản phẩm
     * 4. Cộng điểm khách hàng (nếu có)
     *
     * @return id hóa đơn vừa tạo, hoặc -1 nếu thất bại
     */
    public int taoHoaDon(int idNhanVien, int idKhachHang,
                         double tongTien, List<ChiTietHoaDon> danhSachCT) {
        String sqlHD  = "INSERT INTO hoa_don (id_nhan_vien, id_khach_hang, tong_tien) VALUES (?, ?, ?)";
        String sqlCT  = "INSERT INTO chi_tiet_hoa_don (id_hoa_don, id_san_pham, so_luong, don_gia, thanh_tien) "
                       + "VALUES (?, ?, ?, ?, ?)";
        String sqlTK  = "UPDATE san_pham SET so_luong = so_luong - ? WHERE id = ? AND so_luong >= ?";
        String sqlDiem = "UPDATE khach_hang SET diem_tich_luy = diem_tich_luy + ? WHERE id = ?";

        Connection conn = null;
        try {
            conn = DatabaseConnection.layKetNoi();
            conn.setAutoCommit(false); // Bắt đầu transaction

            // 1. Tạo hóa đơn
            int idHoaDon;
            try (PreparedStatement ps = conn.prepareStatement(sqlHD, Statement.RETURN_GENERATED_KEYS)) {
                if (idNhanVien > 0) ps.setInt(1, idNhanVien);
                else ps.setNull(1, Types.INTEGER);
                if (idKhachHang > 0) ps.setInt(2, idKhachHang);
                else ps.setNull(2, Types.INTEGER);
                ps.setDouble(3, tongTien);
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (!rs.next()) { conn.rollback(); return -1; }
                idHoaDon = rs.getInt(1);
            }

            // 2 & 3. Chi tiết + trừ tồn kho
            try (PreparedStatement psCT = conn.prepareStatement(sqlCT);
                 PreparedStatement psTK = conn.prepareStatement(sqlTK)) {
                for (ChiTietHoaDon ct : danhSachCT) {
                    // Trừ tồn kho (kiểm tra đủ hàng)
                    psTK.setInt(1, ct.getSoLuong());
                    psTK.setInt(2, ct.getIdSanPham());
                    psTK.setInt(3, ct.getSoLuong());
                    int affected = psTK.executeUpdate();
                    if (affected == 0) {
                        conn.rollback();
                        throw new SQLException("Không đủ tồn kho: " + ct.getTenSanPham());
                    }
                    psTK.clearParameters();

                    // Lưu chi tiết hóa đơn
                    psCT.setInt(1, idHoaDon);
                    psCT.setInt(2, ct.getIdSanPham());
                    psCT.setInt(3, ct.getSoLuong());
                    psCT.setDouble(4, ct.getDonGia());
                    psCT.setDouble(5, ct.getThanhTien());
                    psCT.addBatch();
                }
                psCT.executeBatch();
            }

            // 4. Cộng điểm khách hàng (1 điểm / 100.000đ)
            if (idKhachHang > 0) {
                int diemThem = (int) (tongTien / AppConstants.TIEN_MOI_DIEM);
                if (diemThem > 0) {
                    try (PreparedStatement psDiem = conn.prepareStatement(sqlDiem)) {
                        psDiem.setInt(1, diemThem);
                        psDiem.setInt(2, idKhachHang);
                        psDiem.executeUpdate();
                    }
                }
            }

            conn.commit(); // Hoàn thành transaction
            return idHoaDon;

        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ignored) {}
            e.printStackTrace();
            return -1;
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {}
        }
    }

    /** Lấy id nhân viên từ id tài khoản đăng nhập. */
    public int layIdNhanVienTheoTaiKhoan(int idTaiKhoan) {
        String sql = "SELECT id FROM nhan_vien WHERE id_tai_khoan = ?";
        try (Connection conn = DatabaseConnection.layKetNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTaiKhoan);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Lấy danh sách hóa đơn (JOIN nhan_vien, khach_hang).
     * Mỗi Object[]: {id, ngay_lap(String), ten_nv, ten_kh, tong_tien}
     */
    public List<Object[]> layTatCa() {
        List<Object[]> ds = new java.util.ArrayList<>();
        String sql = """
            SELECT hd.id,
                   DATE_FORMAT(hd.ngay_lap, '%d/%m/%Y %H:%i') AS ngay_lap,
                   COALESCE(nv.ho_ten, '—') AS ten_nv,
                   COALESCE(kh.ho_ten, 'Khách lẻ') AS ten_kh,
                   hd.tong_tien
            FROM hoa_don hd
            LEFT JOIN nhan_vien nv ON hd.id_nhan_vien = nv.id
            LEFT JOIN khach_hang kh ON hd.id_khach_hang = kh.id
            ORDER BY hd.ngay_lap DESC
            """;
        try (Connection conn = DatabaseConnection.layKetNoi();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next())
                ds.add(new Object[]{rs.getInt(1), rs.getString(2),
                    rs.getString(3), rs.getString(4), rs.getDouble(5)});
        } catch (SQLException e) { e.printStackTrace(); }
        return ds;
    }

    /**
     * Lấy chi tiết sản phẩm của một hóa đơn.
     * Mỗi Object[]: {ten_sp, so_luong, don_gia, thanh_tien}
     */
    public List<Object[]> layChiTiet(int idHoaDon) {
        List<Object[]> ds = new java.util.ArrayList<>();
        String sql = """
            SELECT COALESCE(sp.ten_sp, ct.id_san_pham) AS ten_sp,
                   ct.so_luong, ct.don_gia, ct.thanh_tien
            FROM chi_tiet_hoa_don ct
            LEFT JOIN san_pham sp ON ct.id_san_pham = sp.id
            WHERE ct.id_hoa_don = ?
            """;
        try (Connection conn = DatabaseConnection.layKetNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idHoaDon);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                ds.add(new Object[]{rs.getString(1), rs.getInt(2),
                    rs.getDouble(3), rs.getDouble(4)});
        } catch (SQLException e) { e.printStackTrace(); }
        return ds;
    }

    /** Doanh thu hôm nay. */
    public double doanhThuHomNay() {
        String sql = "SELECT COALESCE(SUM(tong_tien),0) FROM hoa_don WHERE DATE(ngay_lap)=CURDATE()";
        try (Connection conn = DatabaseConnection.layKetNoi();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    /** Doanh thu tháng này. */
    public double doanhThuThangNay() {
        String sql = "SELECT COALESCE(SUM(tong_tien),0) FROM hoa_don "
                   + "WHERE MONTH(ngay_lap)=MONTH(CURDATE()) AND YEAR(ngay_lap)=YEAR(CURDATE())";
        try (Connection conn = DatabaseConnection.layKetNoi();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    /** Số hóa đơn hôm nay. */
    public int soHoaDonHomNay() {
        String sql = "SELECT COUNT(*) FROM hoa_don WHERE DATE(ngay_lap)=CURDATE()";
        try (Connection conn = DatabaseConnection.layKetNoi();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    /**
     * Top N sản phẩm bán chạy nhất (tổng thời gian).
     * Mỗi Object[]: {ten_sp, tong_so_luong, tong_doanh_thu}
     */
    public List<Object[]> topSanPham(int n) {
        List<Object[]> ds = new java.util.ArrayList<>();
        String sql = """
            SELECT COALESCE(sp.ten_sp, CONCAT('SP#', ct.id_san_pham)) AS ten_sp,
                   SUM(ct.so_luong) AS tong_sl,
                   SUM(ct.thanh_tien) AS tong_tien
            FROM chi_tiet_hoa_don ct
            LEFT JOIN san_pham sp ON ct.id_san_pham = sp.id
            GROUP BY ct.id_san_pham, sp.ten_sp
            ORDER BY tong_sl DESC
            LIMIT ?
            """;
        try (Connection conn = DatabaseConnection.layKetNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, n);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                ds.add(new Object[]{rs.getString(1), rs.getInt(2), rs.getDouble(3)});
        } catch (SQLException e) { e.printStackTrace(); }
        return ds;
    }

    /**
     * Doanh thu 7 ngày gần nhất.
     * Luôn trả về đủ 7 ngày, ngày không có doanh thu sẽ = 0.
     * Mỗi Object[]: {ngay(String "dd/MM"), doanh_thu(double)}
     */
    public List<Object[]> doanhThu7Ngay() {
        List<Object[]> ds = new java.util.ArrayList<>();
        String sql = """
            WITH RECURSIVE dates AS (
                SELECT CURDATE() - INTERVAL 6 DAY AS d
                UNION ALL
                SELECT d + INTERVAL 1 DAY FROM dates WHERE d < CURDATE()
            )
            SELECT DATE_FORMAT(dates.d, '%d/%m') AS ngay,
                   COALESCE(SUM(hd.tong_tien), 0) AS dt
            FROM dates
            LEFT JOIN hoa_don hd ON DATE(hd.ngay_lap) = dates.d
            GROUP BY dates.d
            ORDER BY dates.d ASC
            """;
        try (Connection conn = DatabaseConnection.layKetNoi();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next())
                ds.add(new Object[]{rs.getString(1), rs.getDouble(2)});
        } catch (SQLException e) { e.printStackTrace(); }
        return ds;
    }

    /**
     * Lọc hóa đơn theo khoảng ngày và SĐT khách hàng.
     * Mỗi Object[]: {id, ngay_lap, ten_nv, ten_kh, tong_tien}
     */
    public List<Object[]> loc(String tuNgay, String denNgay, String sdtKH) {
        List<Object[]> ds = new java.util.ArrayList<>();
        StringBuilder sql = new StringBuilder("""
            SELECT hd.id,
                   DATE_FORMAT(hd.ngay_lap, '%d/%m/%Y %H:%i'),
                   COALESCE(nv.ho_ten, '—'),
                   COALESCE(kh.ho_ten, 'Khách lẻ'),
                   hd.tong_tien
            FROM hoa_don hd
            LEFT JOIN nhan_vien nv ON hd.id_nhan_vien = nv.id
            LEFT JOIN khach_hang kh ON hd.id_khach_hang = kh.id
            WHERE 1=1
            """);
        List<Object> params = new java.util.ArrayList<>();
        if (tuNgay != null && !tuNgay.isEmpty()) {
            sql.append(" AND DATE(hd.ngay_lap) >= STR_TO_DATE(?, '%d/%m/%Y')");
            params.add(tuNgay);
        }
        if (denNgay != null && !denNgay.isEmpty()) {
            sql.append(" AND DATE(hd.ngay_lap) <= STR_TO_DATE(?, '%d/%m/%Y')");
            params.add(denNgay);
        }
        if (sdtKH != null && !sdtKH.isEmpty()) {
            sql.append(" AND kh.sdt LIKE ?");
            params.add("%" + sdtKH + "%");
        }
        sql.append(" ORDER BY hd.ngay_lap DESC");
        try (Connection conn = DatabaseConnection.layKetNoi();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                ds.add(new Object[]{rs.getInt(1), rs.getString(2),
                    rs.getString(3), rs.getString(4), rs.getDouble(5)});
        } catch (SQLException e) { e.printStackTrace(); }
        return ds;
    }

    /**
     * Doanh thu theo khoảng ngày.
     * @param tuNgay java.sql.Date bắt đầu
     * @param denNgay java.sql.Date kết thúc
     */
    public double doanhThuTheoKhoang(java.sql.Date tuNgay, java.sql.Date denNgay) {
        String sql = "SELECT COALESCE(SUM(tong_tien),0) FROM hoa_don WHERE DATE(ngay_lap) BETWEEN ? AND ?";
        try (Connection conn = DatabaseConnection.layKetNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, tuNgay);
            ps.setDate(2, denNgay);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    /** Số hóa đơn theo khoảng ngày. */
    public int soHoaDonTheoKhoang(java.sql.Date tuNgay, java.sql.Date denNgay) {
        String sql = "SELECT COUNT(*) FROM hoa_don WHERE DATE(ngay_lap) BETWEEN ? AND ?";
        try (Connection conn = DatabaseConnection.layKetNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, tuNgay);
            ps.setDate(2, denNgay);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    /**
     * Doanh thu theo từng ngày trong khoảng (cho biểu đồ).
     * Mỗi Object[]: {ngay(String "dd/MM"), doanh_thu(double)}
     */
    public List<Object[]> doanhThuTheoKhoangNgay(java.sql.Date tuNgay, java.sql.Date denNgay) {
        List<Object[]> ds = new java.util.ArrayList<>();
        String sql = """
            WITH RECURSIVE dates AS (
                SELECT ? AS d
                UNION ALL
                SELECT d + INTERVAL 1 DAY FROM dates WHERE d < ?
            )
            SELECT DATE_FORMAT(dates.d, '%d/%m') AS ngay,
                   COALESCE(SUM(hd.tong_tien), 0) AS dt
            FROM dates
            LEFT JOIN hoa_don hd ON DATE(hd.ngay_lap) = dates.d
            GROUP BY dates.d
            ORDER BY dates.d ASC
            """;
        try (Connection conn = DatabaseConnection.layKetNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, tuNgay);
            ps.setDate(2, denNgay);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                ds.add(new Object[]{rs.getString(1), rs.getDouble(2)});
        } catch (SQLException e) { e.printStackTrace(); }
        return ds;
    }

    /** Top N sản phẩm bán chạy theo khoảng ngày. */
    public List<Object[]> topSanPhamTheoKhoang(int n, java.sql.Date tuNgay, java.sql.Date denNgay) {
        List<Object[]> ds = new java.util.ArrayList<>();
        String sql = """
            SELECT COALESCE(sp.ten_sp, CONCAT('SP#', ct.id_san_pham)) AS ten_sp,
                   SUM(ct.so_luong) AS tong_sl,
                   SUM(ct.thanh_tien) AS tong_tien
            FROM chi_tiet_hoa_don ct
            LEFT JOIN san_pham sp ON ct.id_san_pham = sp.id
            JOIN hoa_don hd ON ct.id_hoa_don = hd.id
            WHERE DATE(hd.ngay_lap) BETWEEN ? AND ?
            GROUP BY ct.id_san_pham, sp.ten_sp
            ORDER BY tong_sl DESC
            LIMIT ?
            """;
        try (Connection conn = DatabaseConnection.layKetNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, tuNgay);
            ps.setDate(2, denNgay);
            ps.setInt(3, n);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                ds.add(new Object[]{rs.getString(1), rs.getInt(2), rs.getDouble(3)});
        } catch (SQLException e) { e.printStackTrace(); }
        return ds;
    }
}
