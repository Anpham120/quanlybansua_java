package dao;

import model.KhachHang;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp truy xuất dữ liệu khách hàng (CRUD + tìm kiếm).
 * 
 * @author Đỗ Tuấn Anh - BIT240131
 */
public class KhachHangDAO {

    /** Lấy tất cả khách hàng. */
    public List<KhachHang> layTatCa() {
        List<KhachHang> ds = new ArrayList<>();
        String sql = "SELECT * FROM khach_hang ORDER BY id";
        try (Connection conn = DatabaseConnection.layKetNoi();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ds.add(docKhachHang(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    /** Tìm kiếm khách hàng theo tên hoặc SĐT. */
    public List<KhachHang> timKiem(String tuKhoa) {
        List<KhachHang> ds = new ArrayList<>();
        if (tuKhoa == null || tuKhoa.trim().isEmpty()) return layTatCa();

        String sql = "SELECT * FROM khach_hang WHERE ho_ten LIKE ? OR sdt LIKE ? ORDER BY id";
        try (Connection conn = DatabaseConnection.layKetNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + tuKhoa + "%");
            ps.setString(2, "%" + tuKhoa + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ds.add(docKhachHang(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    /** Kiểm tra trùng SĐT. */
    public boolean kiemTraTrungSDT(String sdt, int idBoQua) {
        String sql = "SELECT COUNT(*) FROM khach_hang WHERE sdt = ? AND id != ?";
        try (Connection conn = DatabaseConnection.layKetNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sdt);
            ps.setInt(2, idBoQua);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Thêm khách hàng mới. */
    public boolean them(KhachHang kh) {
        String sql = "INSERT INTO khach_hang (ho_ten, sdt, dia_chi, diem_tich_luy) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.layKetNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kh.getHoTen());
            ps.setString(2, kh.getSdt());
            ps.setString(3, kh.getDiaChi());
            ps.setInt(4, kh.getDiemTichLuy());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Cập nhật khách hàng. */
    public boolean capNhat(KhachHang kh) {
        String sql = "UPDATE khach_hang SET ho_ten=?, sdt=?, dia_chi=?, diem_tich_luy=? WHERE id=?";
        try (Connection conn = DatabaseConnection.layKetNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kh.getHoTen());
            ps.setString(2, kh.getSdt());
            ps.setString(3, kh.getDiaChi());
            ps.setInt(4, kh.getDiemTichLuy());
            ps.setInt(5, kh.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Kiểm tra khách hàng có liên kết với hóa đơn không. */
    public boolean kiemTraLienKetHoaDon(int idKhachHang) {
        String sql = "SELECT COUNT(*) FROM hoa_don WHERE id_khach_hang = ?";
        try (Connection conn = DatabaseConnection.layKetNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idKhachHang);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Xóa khách hàng kèm cập nhật hóa đơn liên quan (SET NULL). */
    public boolean xoaCoLienKet(int id) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.layKetNoi();
            conn.setAutoCommit(false);

            // Gỡ liên kết hóa đơn (chuyển thành khách lẻ)
            String sqlHD = "UPDATE hoa_don SET id_khach_hang = NULL WHERE id_khach_hang = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlHD)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }

            // Xóa khách hàng
            String sqlKH = "DELETE FROM khach_hang WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlKH)) {
                ps.setInt(1, id);
                int result = ps.executeUpdate();
                if (result > 0) {
                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    /** Xóa khách hàng. */
    public boolean xoa(int id) {
        String sql = "DELETE FROM khach_hang WHERE id = ?";
        try (Connection conn = DatabaseConnection.layKetNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private KhachHang docKhachHang(ResultSet rs) throws SQLException {
        return new KhachHang(
            rs.getInt("id"),
            rs.getString("ho_ten"),
            rs.getString("sdt"),
            rs.getString("dia_chi"),
            rs.getInt("diem_tich_luy")
        );
    }

    /** Cộng điểm tích lũy sau khi mua hàng (1 điểm / 100.000đ). */
    public void congDiem(int idKhachHang, int sooDiem) {
        if (idKhachHang <= 0 || sooDiem <= 0) return;
        String sql = "UPDATE khach_hang SET diem_tich_luy = diem_tich_luy + ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.layKetNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sooDiem);
            ps.setInt(2, idKhachHang);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
