package model;

/**
 * Lớp đại diện cho tài khoản đăng nhập.
 * 
 * @author Phạm Duy An - BIT240002
 */
public class TaiKhoan {
    private int id;
    private String tenDangNhap;
    private String matKhau;
    private String vaiTro; // "admin" hoặc "nhanvien"

    public TaiKhoan() {}

    public TaiKhoan(int id, String tenDangNhap, String matKhau, String vaiTro) {
        this.id = id;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.vaiTro = vaiTro;
    }

    public TaiKhoan(String tenDangNhap, String matKhau, String vaiTro) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.vaiTro = vaiTro;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    public String getVaiTro() { return vaiTro; }
    public void setVaiTro(String vaiTro) { this.vaiTro = vaiTro; }

    @Override
    public String toString() {
        return tenDangNhap + " (" + vaiTro + ")";
    }
}
