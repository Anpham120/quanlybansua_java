package dao;

import model.TaiKhoan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp truy xuất dữ liệu bảng tài khoản.
 * 
 * @author Phạm Duy An - BIT240002
 */
public class TaiKhoanDAO {

    /**
     * Lấy danh sách tài khoản nhân viên chưa liên kết với nhân viên nào.
     * Dùng cho ComboBox chọn TK khi thêm/sửa nhân viên.
     * @param idTKHienTai ID tài khoản hiện tại của NV đang sửa (để include nó trong danh sách), 0 nếu thêm mới
     */
    public List<TaiKhoan> layTKChuaLienKet(int idTKHienTai) {
        List<TaiKhoan> ds = new ArrayList<>();
        String sql = """
            SELECT tk.* FROM tai_khoan tk
            WHERE tk.vai_tro = 'Nhân viên'
              AND (tk.id NOT IN (SELECT COALESCE(id_tai_khoan, 0) FROM nhan_vien)
                   OR tk.id = ?)
            ORDER BY tk.id
            """;
        try (Connection conn = DatabaseConnection.layKetNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTKHienTai);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ds.add(new TaiKhoan(
                    rs.getInt("id"),
                    rs.getString("ten_dang_nhap"),
                    rs.getString("mat_khau"),
                    rs.getString("vai_tro")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    /**
     * Đăng nhập: kiểm tra tên đăng nhập và mật khẩu.
     * @return TaiKhoan nếu đúng, null nếu sai
     */
    public TaiKhoan dangNhap(String tenDangNhap, String matKhau) {
        String sql = "SELECT * FROM tai_khoan WHERE ten_dang_nhap = ? AND mat_khau = ?";
        try (Connection conn = DatabaseConnection.layKetNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tenDangNhap);
            ps.setString(2, matKhau);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new TaiKhoan(
                        rs.getInt("id"),
                        rs.getString("ten_dang_nhap"),
                        rs.getString("mat_khau"),
                        rs.getString("vai_tro")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy danh sách tất cả tài khoản.
     */
    public List<TaiKhoan> layTatCa() {
        List<TaiKhoan> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM tai_khoan ORDER BY id";
        try (Connection conn = DatabaseConnection.layKetNoi();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                danhSach.add(new TaiKhoan(
                        rs.getInt("id"),
                        rs.getString("ten_dang_nhap"),
                        rs.getString("mat_khau"),
                        rs.getString("vai_tro")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    /**
     * Tìm kiếm tài khoản theo tên đăng nhập.
     */
    public List<TaiKhoan> timKiem(String tuKhoa) {
        List<TaiKhoan> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM tai_khoan WHERE ten_dang_nhap LIKE ? ORDER BY id";
        try (Connection conn = DatabaseConnection.layKetNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + tuKhoa + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                danhSach.add(new TaiKhoan(
                        rs.getInt("id"),
                        rs.getString("ten_dang_nhap"),
                        rs.getString("mat_khau"),
                        rs.getString("vai_tro")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    /**
     * Kiểm tra trùng tên đăng nhập (bỏ qua 1 ID khi sửa).
     */
    public boolean kiemTraTrungTenDN(String tenDN, int idBoQua) {
        String sql = "SELECT COUNT(*) FROM tai_khoan WHERE ten_dang_nhap = ? AND id != ?";
        try (Connection conn = DatabaseConnection.layKetNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tenDN);
            ps.setInt(2, idBoQua);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Thêm tài khoản mới.
     */
    public boolean them(TaiKhoan tk) {
        String sql = "INSERT INTO tai_khoan (ten_dang_nhap, mat_khau, vai_tro) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.layKetNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tk.getTenDangNhap());
            ps.setString(2, tk.getMatKhau());
            ps.setString(3, tk.getVaiTro());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật tài khoản.
     */
    public boolean capNhat(TaiKhoan tk) {
        String sql = "UPDATE tai_khoan SET ten_dang_nhap = ?, mat_khau = ?, vai_tro = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.layKetNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tk.getTenDangNhap());
            ps.setString(2, tk.getMatKhau());
            ps.setString(3, tk.getVaiTro());
            ps.setInt(4, tk.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** Kiểm tra tài khoản có liên kết với nhân viên không. */
    public boolean kiemTraLienKetNhanVien(int idTaiKhoan) {
        String sql = "SELECT COUNT(*) FROM nhan_vien WHERE id_tai_khoan = ?";
        try (Connection conn = DatabaseConnection.layKetNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTaiKhoan);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Xóa tài khoản kèm gỡ liên kết nhân viên (SET NULL). */
    public boolean xoaCoLienKet(int id) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.layKetNoi();
            conn.setAutoCommit(false);

            // Gỡ liên kết nhân viên
            String sqlNV = "UPDATE nhan_vien SET id_tai_khoan = NULL WHERE id_tai_khoan = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlNV)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }

            // Xóa tài khoản
            String sqlTK = "DELETE FROM tai_khoan WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlTK)) {
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

    /**
     * Xóa tài khoản theo ID.
     */
    public boolean xoa(int id) {
        String sql = "DELETE FROM tai_khoan WHERE id = ?";
        try (Connection conn = DatabaseConnection.layKetNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Đổi mật khẩu: kiểm tra mật khẩu cũ rồi cập nhật mật khẩu mới.
     * @return true nếu thành công
     */
    public boolean doiMatKhau(int idTaiKhoan, String matKhauCu, String matKhauMoi) {
        String sql = "UPDATE tai_khoan SET mat_khau = ? WHERE id = ? AND mat_khau = ?";
        try (Connection conn = DatabaseConnection.layKetNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, matKhauMoi);
            ps.setInt(2, idTaiKhoan);
            ps.setString(3, matKhauCu);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
