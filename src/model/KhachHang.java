package model;

/**
 * Model khách hàng.
 * 
 * @author Đỗ Tuấn Anh - BIT240015
 */
public class KhachHang {
    private int id;
    private String hoTen;
    private String sdt;
    private String diaChi;
    private int diemTichLuy;

    public KhachHang() {}

    public KhachHang(int id, String hoTen, String sdt, String diaChi, int diemTichLuy) {
        this.id = id;
        this.hoTen = hoTen;
        this.sdt = sdt;
        this.diaChi = diaChi;
        this.diemTichLuy = diemTichLuy;
    }

    // Phương thức truy cập và thiết lập
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public int getDiemTichLuy() { return diemTichLuy; }
    public void setDiemTichLuy(int diemTichLuy) { this.diemTichLuy = diemTichLuy; }

    /**
     * Trả về hạng khách hàng dựa theo điểm tích lũy.
     * Thường(0-9) | Bạc(10-49) | Vàng(50-99) | Kim cương(≥100)
     */
    public String getHang() {
        if (diemTichLuy >= 100) return "Kim cương";
        if (diemTichLuy >= 50)  return "Vàng";
        if (diemTichLuy >= 10)  return "Bạc";
        return "Thường";
    }

    /**
     * Tỷ lệ giảm giá theo hạng.
     * Thường=0%, Bạc=2%, Vàng=5%, Kim cương=10%
     */
    public double getTyLeGiam() {
        if (diemTichLuy >= 100) return 0.10;
        if (diemTichLuy >= 50)  return 0.05;
        if (diemTichLuy >= 10)  return 0.02;
        return 0.0;
    }

    @Override
    public String toString() {
        return getHang() + "  " + hoTen + " (" + sdt + ")";
    }
}
