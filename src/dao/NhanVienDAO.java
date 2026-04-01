package dao;

import model.NhanVien;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp truy xuất dữ liệu nhân viên (CRUD + tìm kiếm).
 * 
 * @author Đỗ Tuấn Anh - BIT240131
 */
public class NhanVienDAO {

    /** Lấy tất cả nhân viên. */
    public List<NhanVien> layTatCa() {
        List<NhanVien> ds = new ArrayList<>();
        String sql = "SELECT * FROM nhan_vien ORDER BY id";
        try (Connection conn = DatabaseConnection.layKetNoi();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ds.add(docNhanVien(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    /** Tìm kiếm nhân viên theo tên hoặc SĐT. */
    public List<NhanVien> timKiem(String tuKhoa) {
        List<NhanVien> ds = new ArrayList<>();
        if (tuKhoa == null || tuKhoa.trim().isEmpty()) return layTatCa();

        String sql = "SELECT * FROM nhan_vien WHERE ho_ten LIKE ? OR sdt LIKE ? ORDER BY id";
        try (Connection conn = DatabaseConnection.layKetNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + tuKhoa + "%");
            ps.setString(2, "%" + tuKhoa + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ds.add(docNhanVien(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    /** Kiểm tra trùng SĐT nhân viên. */
    public boolean kiemTraTrungSDT(String sdt, int idBoQua) {
        String sql = "SELECT COUNT(*) FROM nhan_vien WHERE sdt = ? AND id != ?";
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

    /** Thêm nhân viên mới. */
    public boolean them(NhanVien nv) {
        String sql = "INSERT INTO nhan_vien (ho_ten, sdt, dia_chi, ngay_sinh, gioi_tinh, id_tai_khoan) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.layKetNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ganGiaTri(ps, nv);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Cập nhật nhân viên. */
    public boolean capNhat(NhanVien nv) {
        String sql = "UPDATE nhan_vien SET ho_ten=?, sdt=?, dia_chi=?, ngay_sinh=?, gioi_tinh=?, "
                   + "id_tai_khoan=? WHERE id=?";
        try (Connection conn = DatabaseConnection.layKetNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ganGiaTri(ps, nv);
            ps.setInt(7, nv.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Kiểm tra nhân viên có liên kết với hóa đơn không. */
    public boolean kiemTraLienKetHoaDon(int idNhanVien) {
        String sql = "SELECT COUNT(*) FROM hoa_don WHERE id_nhan_vien = ?";
        try (Connection conn = DatabaseConnection.layKetNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idNhanVien);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Xóa nhân viên kèm gỡ liên kết hóa đơn (SET NULL). */
    public boolean xoaCoLienKet(int id) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.layKetNoi();
            conn.setAutoCommit(false);

            // Gỡ liên kết hóa đơn
            String sqlHD = "UPDATE hoa_don SET id_nhan_vien = NULL WHERE id_nhan_vien = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlHD)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }

            // Xóa nhân viên
            String sqlNV = "DELETE FROM nhan_vien WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlNV)) {
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

    /** Xóa nhân viên. */
    public boolean xoa(int id) {
        String sql = "DELETE FROM nhan_vien WHERE id = ?";
        try (Connection conn = DatabaseConnection.layKetNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private NhanVien docNhanVien(ResultSet rs) throws SQLException {
        return new NhanVien(
            rs.getInt("id"),
            rs.getString("ho_ten"),
            rs.getString("sdt"),
            rs.getString("dia_chi"),
            rs.getDate("ngay_sinh"),
            rs.getString("gioi_tinh"),
            rs.getInt("id_tai_khoan")
        );
    }

    private void ganGiaTri(PreparedStatement ps, NhanVien nv) throws SQLException {
        ps.setString(1, nv.getHoTen());
        ps.setString(2, nv.getSdt());
        ps.setString(3, nv.getDiaChi());
        ps.setDate(4, nv.getNgaySinh());
        ps.setString(5, nv.getGioiTinh());
        // id_tai_khoan = 0 nghĩa là chưa liên kết → lưu NULL tránh vi phạm FK
        if (nv.getIdTaiKhoan() > 0) {
            ps.setInt(6, nv.getIdTaiKhoan());
        } else {
            ps.setNull(6, java.sql.Types.INTEGER);
        }
    }
}
