package model;

import java.sql.Date;

/**
 * Model nhân viên.
 * 
 * @author Đỗ Tuấn Anh - BIT240131
 */
public class NhanVien {
    private int id;
    private String hoTen;
    private String sdt;
    private String diaChi;
    private Date ngaySinh;
    private String gioiTinh;
    private int idTaiKhoan;

    public NhanVien() {}

    public NhanVien(int id, String hoTen, String sdt, String diaChi,
                    Date ngaySinh, String gioiTinh, int idTaiKhoan) {
        this.id = id;
        this.hoTen = hoTen;
        this.sdt = sdt;
        this.diaChi = diaChi;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.idTaiKhoan = idTaiKhoan;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public Date getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(Date ngaySinh) { this.ngaySinh = ngaySinh; }

    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }

    public int getIdTaiKhoan() { return idTaiKhoan; }
    public void setIdTaiKhoan(int idTaiKhoan) { this.idTaiKhoan = idTaiKhoan; }
}
