package dao;

import model.SanPham;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp truy xuất dữ liệu sản phẩm sữa (CRUD + tìm kiếm + lọc).
 * 
 * @author Bùi Đào Đức Anh - BIT240025
 */
public class SanPhamDAO {

    /** Lấy tất cả sản phẩm. */
    public List<SanPham> layTatCa() {
        List<SanPham> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM san_pham ORDER BY id";
        try (Connection conn = DatabaseConnection.layKetNoi();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                danhSach.add(docSanPham(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    /** Tìm kiếm sản phẩm theo tên. */
    public List<SanPham> timTheoTen(String tuKhoa) {
        List<SanPham> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM san_pham WHERE ten_sp LIKE ? ORDER BY id";
        try (Connection conn = DatabaseConnection.layKetNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + tuKhoa + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                danhSach.add(docSanPham(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    /** Lọc sản phẩm theo loại. */
    public List<SanPham> locTheoLoai(String loai) {
        List<SanPham> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM san_pham WHERE loai = ? ORDER BY id";
        try (Connection conn = DatabaseConnection.layKetNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, loai);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                danhSach.add(docSanPham(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    /** Tìm kiếm kết hợp theo tên + loại. */
    public List<SanPham> timKiem(String tuKhoa, String loai) {
        if (loai == null || loai.equals("Tất cả")) {
            if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
                return layTatCa();
            }
            return timTheoTen(tuKhoa);
        }
        if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
            return locTheoLoai(loai);
        }

        List<SanPham> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM san_pham WHERE ten_sp LIKE ? AND loai = ? ORDER BY id";
        try (Connection conn = DatabaseConnection.layKetNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + tuKhoa + "%");
            ps.setString(2, loai);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                danhSach.add(docSanPham(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    /** Lấy danh sách loại sản phẩm (dùng cho ComboBox lọc). */
    public List<String> layDanhSachLoai() {
        List<String> dsLoai = new ArrayList<>();
        String sql = "SELECT DISTINCT loai FROM san_pham ORDER BY loai";
        try (Connection conn = DatabaseConnection.layKetNoi();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                dsLoai.add(rs.getString("loai"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsLoai;
    }

    /** Kiểm tra sản phẩm trùng tên (dùng trước khi thêm/sửa). */
    public boolean kiemTraTrung(String tenSP, int idBoQua) {
        String sql = "SELECT COUNT(*) FROM san_pham WHERE ten_sp = ? AND id != ?";
        try (Connection conn = DatabaseConnection.layKetNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tenSP);
            ps.setInt(2, idBoQua);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Thêm sản phẩm mới. */
    public boolean them(SanPham sp) {
        String sql = "INSERT INTO san_pham (ten_sp, loai, thuong_hieu, don_gia, so_luong, mo_ta, ngay_san_xuat, han_su_dung) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.layKetNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ganGiaTri(ps, sp);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Cập nhật sản phẩm. */
    public boolean capNhat(SanPham sp) {
        String sql = "UPDATE san_pham SET ten_sp=?, loai=?, thuong_hieu=?, don_gia=?, so_luong=?, "
                   + "mo_ta=?, ngay_san_xuat=?, han_su_dung=? WHERE id=?";
        try (Connection conn = DatabaseConnection.layKetNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ganGiaTri(ps, sp);
            ps.setInt(9, sp.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Trừ tồn kho khi bán hàng. Trả về false nếu không đủ hàng. */
    public boolean truTonKho(int idSanPham, int soLuong) {
        String sql = "UPDATE san_pham SET so_luong = so_luong - ? "
                   + "WHERE id = ? AND so_luong >= ?";
        try (Connection conn = DatabaseConnection.layKetNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, soLuong);
            ps.setInt(2, idSanPham);
            ps.setInt(3, soLuong);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Kiểm tra sản phẩm có đang liên kết với hóa đơn không. */
    public boolean kiemTraLienKetHoaDon(int idSanPham) {
        String sql = "SELECT COUNT(*) FROM chi_tiet_hoa_don WHERE id_san_pham = ?";
        try (Connection conn = DatabaseConnection.layKetNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSanPham);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Xóa sản phẩm kèm xóa chi tiết hóa đơn liên quan (dùng transaction). */
    public boolean xoaCoLienKet(int id) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.layKetNoi();
            conn.setAutoCommit(false);

            // Xóa chi tiết hóa đơn liên quan trước
            String sqlChiTiet = "DELETE FROM chi_tiet_hoa_don WHERE id_san_pham = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlChiTiet)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }

            // Cập nhật lại tổng tiền các hóa đơn bị ảnh hưởng
            String sqlCapNhatTong = "UPDATE hoa_don SET tong_tien = "
                + "(SELECT COALESCE(SUM(thanh_tien), 0) FROM chi_tiet_hoa_don WHERE id_hoa_don = hoa_don.id)";
            try (PreparedStatement ps = conn.prepareStatement(sqlCapNhatTong)) {
                ps.executeUpdate();
            }

            // Sau đó xóa sản phẩm
            String sqlSanPham = "DELETE FROM san_pham WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlSanPham)) {
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

    /** Xóa sản phẩm theo ID. */
    public boolean xoa(int id) {
        String sql = "DELETE FROM san_pham WHERE id = ?";
        try (Connection conn = DatabaseConnection.layKetNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Đọc 1 dòng ResultSet thành SanPham. */
    private SanPham docSanPham(ResultSet rs) throws SQLException {
        return new SanPham(
            rs.getInt("id"),
            rs.getString("ten_sp"),
            rs.getString("loai"),
            rs.getString("thuong_hieu"),
            rs.getDouble("don_gia"),
            rs.getInt("so_luong"),
            rs.getString("mo_ta"),
            rs.getDate("ngay_san_xuat"),
            rs.getDate("han_su_dung")
        );
    }

    /** Đếm sản phẩm sắp hết hạn (trong vòng N ngày tới). */
    public int demSapHetHan(int soNgay) {
        String sql = "SELECT COUNT(*) FROM san_pham WHERE han_su_dung IS NOT NULL "
                   + "AND han_su_dung BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL ? DAY) "
                   + "AND so_luong > 0";
        try (Connection conn = DatabaseConnection.layKetNoi();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, soNgay);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    /** Đếm sản phẩm hết hàng (tồn kho = 0). */
    public int demHetHang() {
        String sql = "SELECT COUNT(*) FROM san_pham WHERE so_luong = 0";
        try (Connection conn = DatabaseConnection.layKetNoi();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    /** Gán giá trị cho PreparedStatement. */
    private void ganGiaTri(PreparedStatement ps, SanPham sp) throws SQLException {
        ps.setString(1, sp.getTenSP());
        ps.setString(2, sp.getLoai());
        ps.setString(3, sp.getThuongHieu());
        ps.setDouble(4, sp.getDonGia());
        ps.setInt(5, sp.getSoLuong());
        ps.setString(6, sp.getMoTa());
        ps.setDate(7, sp.getNgaySanXuat());
        ps.setDate(8, sp.getHanSuDung());
    }
}
