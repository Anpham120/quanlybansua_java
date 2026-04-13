# 🥛 Hệ thống Quản lý Bán Sữa

Ứng dụng desktop quản lý cửa hàng kinh doanh sữa — **Java Swing + MySQL**, kiến trúc **DAO–MVC**.

> **Bài tập lớn môn Lập trình Java** · HK2 năm học 2025–2026  
> Trường Đại học CMC — Khoa Công nghệ Thông tin

---

## 📋 Tính năng

| Module | Mô tả |
|---|---|
| 🔐 Đăng nhập | Xác thực + phân quyền Quản lý / Nhân viên |
| 🛒 Bán hàng | Giỏ hàng, chiết khấu thành viên, thanh toán (JDBC Transaction) |
| 📦 Sản phẩm | CRUD sữa, lọc loại, tô màu tồn kho (hết hàng = đỏ, sắp hết = cam) |
| 👥 Khách hàng | CRUD thành viên, điểm tích lũy, hạng tự động |
| 👨‍💼 Nhân viên | CRUD nhân sự, liên kết tài khoản, kiểm tra tuổi ≥ 18 |
| 🧾 Hóa đơn | Lịch sử giao dịch, lọc theo ngày/SĐT, xem chi tiết |
| 📊 Thống kê | Dashboard KPI, biểu đồ doanh thu (Graphics2D), Top 5 SP, xuất CSV |
| 🔑 Tài khoản | CRUD tài khoản, phân vai trò |
| 🔒 Đổi mật khẩu | Nhân viên tự đổi MK cá nhân |

## 🏗️ Kiến trúc nâng cao

Ngoài mô hình DAO–MVC cơ bản, dự án tích hợp **3 kỹ thuật nâng cao**:

| Kỹ thuật | Lớp áp dụng | Mô tả |
|---|---|---|
| **Lập trình bất đồng bộ** | `Main`, `ThongKePanel` | `CompletableFuture` khởi tạo CSDL; `SwingWorker` tải dữ liệu không chặn giao diện |
| **Lập trình hàm (FP)** | `BanHangPanel` | `Stream API`, `Optional`, `IntStream` xử lý giỏ hàng |
| **Kiến trúc thông điệp** | `EventBus` | Pub/Sub giữa các module — tự cập nhật dữ liệu khi thanh toán |

```
src/
├── Main.java                    ← Điểm khởi chạy (CompletableFuture)
├── model/    (6 POJO)           ← Tầng Model
├── dao/      (6 DAO + DB)       ← Tầng Data Access
├── gui/      (10 Swing UI)      ← Tầng View
└── utils/    (5 tiện ích)       ← EventBus, UIConstants, AppConstants, InputFilter, TableHelper
```

## 🗃️ Cơ sở dữ liệu

MySQL 8.x — **Tự động tạo** DB + bảng + dữ liệu mẫu khi chạy lần đầu.

```
tai_khoan ←1:1→ nhan_vien ←1:N→ hoa_don ←1:N→ chi_tiet_hoa_don →N:1→ san_pham
                                    ↑
                              khach_hang (nullable = Khách lẻ)
```

## ⚡ Cài đặt & Chạy

**Yêu cầu:** JDK 11+ · MySQL 8.x · Eclipse/IntelliJ

```bash
# 1. Clone
git clone https://github.com/Anpham120/quanlybansua_java.git

# 2. Mở bằng Eclipse: File → Import → Existing Projects

# 3. Thêm JDBC Driver: Build Path → Add External JARs → lib/mysql-connector-j-9.2.0.jar

# 4. Cấu hình MySQL:
#    Sửa src/dao/DatabaseConnection.java dòng 17-18
#    Đặt TAI_KHOAN và MAT_KHAU theo MySQL của bạn

# 5. Chạy src/Main.java → DB tự tạo lần đầu
```

## 🔑 Tài khoản mặc định

| Tên đăng nhập | Mật khẩu | Vai trò |
|---|---|---|
| `duyan` | `admin123` | Quản lý |
| `ducanh` | `123456` | Nhân viên |
| `tuananh` | `123456` | Nhân viên |
| `quanghieu` | `123456` | Nhân viên |
| `vanhieu` | `123456` | Nhân viên |

## 🎯 Phân quyền

| Chức năng | Quản lý | Nhân viên |
|---|:---:|:---:|
| Menu hiển thị | 6 mục | 2 mục |
| Bán hàng & Thanh toán | ✗ | ✓ |
| CRUD Sản phẩm/KH/NV/TK | ✓ | ✗ |
| Xem sản phẩm (chỉ đọc) | ✓ | ✓ |
| Thống kê & Xuất CSV | ✓ | ✗ |
| Đổi mật khẩu cá nhân | ✗ (dùng tab TK) | ✓ |

## 🏆 Hạng thành viên

| Hạng | Điểm | Chiết khấu |
|---|---|---|
| Thường | 0 – 9 | 0% |
| 🥈 Bạc | 10 – 49 | 2% |
| 🥇 Vàng | 50 – 99 | 5% |
| 💎 Kim cương | ≥ 100 | 10% |

> Quy tắc: Mỗi 100.000 VNĐ thanh toán = 1 điểm tích lũy

## 👨‍💻 Nhóm thực hiện

| STT | Họ và tên | MSSV | Công việc chính |
|---|---|---|---|
| 1 | Phạm Duy An | BIT240002 | Kiến trúc tổng thể, khởi tạo CSDL bất đồng bộ, giao diện chính, phân quyền |
| 2 | Bùi Đào Đức Anh | BIT240025 | Quản lý sản phẩm, bộ lọc theo loại, cảnh báo tồn kho, tiện ích bảng |
| 3 | Đỗ Tuấn Anh | BIT240015 | Quản lý khách hàng & nhân viên, đổi mật khẩu, EventBus |
| 4 | Nguyễn Quang Hiếu | BIT240082 | Bán hàng (Stream API + Optional), chiết khấu, quản lý tài khoản |
| 5 | Phan Văn Hiếu | BIT240094 | Hóa đơn, thống kê bất đồng bộ (SwingWorker), biểu đồ, xuất CSV |

## 📄 Công nghệ

| Thành phần | Phiên bản |
|---|---|
| Java SE | JDK 24 |
| Java Swing | Tích hợp JDK |
| MySQL | 8.x |
| MySQL Connector/J | 9.2.0 |
| Nimbus Look & Feel | Tích hợp JDK |

---

*Đồ án môn Lập trình Java — Đại học CMC — 2026*
