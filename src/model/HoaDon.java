package model;

import java.time.LocalDateTime;

/**
 * Model hóa đơn bán hàng.
 * @author Nguyễn Quang Hiếu - BIT240082
 */
public class HoaDon {
    private int id;
    private int idNhanVien;
    private int idKhachHang; // 0 = khách lẻ
    private LocalDateTime ngayLap;
    private double tongTien;

    public HoaDon() {}

    public HoaDon(int idNhanVien, int idKhachHang, double tongTien) {
        this.idNhanVien = idNhanVien;
        this.idKhachHang = idKhachHang;
        this.tongTien = tongTien;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdNhanVien() { return idNhanVien; }
    public void setIdNhanVien(int idNhanVien) { this.idNhanVien = idNhanVien; }

    public int getIdKhachHang() { return idKhachHang; }
    public void setIdKhachHang(int idKhachHang) { this.idKhachHang = idKhachHang; }

    public LocalDateTime getNgayLap() { return ngayLap; }
    public void setNgayLap(LocalDateTime ngayLap) { this.ngayLap = ngayLap; }

    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }
}
