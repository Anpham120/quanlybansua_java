package model;

import java.sql.Date;

/**
 * Lớp đại diện cho sản phẩm sữa trong cửa hàng.
 * 
 * @author Bùi Đào Đức Anh - BIT240025
 */
public class SanPham {

    private int id;
    private String tenSP;
    private String loai;
    private String thuongHieu;
    private double donGia;
    private int soLuong;
    private String moTa;
    private Date ngaySanXuat;
    private Date hanSuDung;

    public SanPham() {}

    public SanPham(String tenSP, String loai, String thuongHieu, double donGia,
                   int soLuong, String moTa, Date ngaySanXuat, Date hanSuDung) {
        this.tenSP = tenSP;
        this.loai = loai;
        this.thuongHieu = thuongHieu;
        this.donGia = donGia;
        this.soLuong = soLuong;
        this.moTa = moTa;
        this.ngaySanXuat = ngaySanXuat;
        this.hanSuDung = hanSuDung;
    }

    public SanPham(int id, String tenSP, String loai, String thuongHieu, double donGia,
                   int soLuong, String moTa, Date ngaySanXuat, Date hanSuDung) {
        this(tenSP, loai, thuongHieu, donGia, soLuong, moTa, ngaySanXuat, hanSuDung);
        this.id = id;
    }

    // === Getter & Setter ===
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTenSP() { return tenSP; }
    public void setTenSP(String tenSP) { this.tenSP = tenSP; }

    public String getLoai() { return loai; }
    public void setLoai(String loai) { this.loai = loai; }

    public String getThuongHieu() { return thuongHieu; }
    public void setThuongHieu(String thuongHieu) { this.thuongHieu = thuongHieu; }

    public double getDonGia() { return donGia; }
    public void setDonGia(double donGia) { this.donGia = donGia; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public Date getNgaySanXuat() { return ngaySanXuat; }
    public void setNgaySanXuat(Date ngaySanXuat) { this.ngaySanXuat = ngaySanXuat; }

    public Date getHanSuDung() { return hanSuDung; }
    public void setHanSuDung(Date hanSuDung) { this.hanSuDung = hanSuDung; }
}
