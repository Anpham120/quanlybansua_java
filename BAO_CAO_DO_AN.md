# TRƯỜNG ĐẠI HỌC CÔNG NGHIỆP HÀ NỘI
## KHOA CÔNG NGHỆ THÔNG TIN

---

&nbsp;

&nbsp;

# BÁO CÁO BÀI TẬP LỚN
## MÔN: LẬP TRÌNH JAVA

&nbsp;

## ĐỀ TÀI: HỆ THỐNG QUẢN LÝ BÁN SỮA

&nbsp;

| Thông tin | Chi tiết |
|---|---|
| Giảng viên hướng dẫn | *(Tên giảng viên)* |
| Lớp | *(Tên lớp)* |
| Học kỳ | HK2 – Năm học 2025–2026 |

&nbsp;

**Nhóm thực hiện:**

| STT | Họ và tên | Mã sinh viên |
|---|---|---|
| 1 | Phạm Duy An | BIT240002 |
| 2 | Bùi Đào Đức Anh | BIT240025 |
| 3 | Đỗ Tuấn Anh | BIT240015 |
| 4 | Nguyễn Quang Hiếu | BIT240082 |
| 5 | Phan Văn Hiếu | BIT240094 |

&nbsp;

**Hà Nội, tháng 3 năm 2026**

---

# LỜI NÓI ĐẦU

Trong bối cảnh chuyển đổi số đang diễn ra mạnh mẽ tại Việt Nam, các cơ sở kinh doanh nhỏ lẻ — đặc biệt là các cửa hàng bán sữa — vẫn còn quản lý theo phương thức thủ công, dễ dẫn đến sai sót trong theo dõi hàng tồn kho, thống kê doanh thu và chăm sóc khách hàng thân thiết. Những vấn đề này không chỉ làm giảm hiệu quả kinh doanh mà còn gây khó khăn trong việc ra quyết định kịp thời của người quản lý.

Xuất phát từ thực tế đó, nhóm chúng em lựa chọn đề tài **"Hệ thống Quản lý Bán Sữa"** nhằm xây dựng một ứng dụng desktop bằng ngôn ngữ Java, có thể giải quyết các bài toán quản lý cơ bản: quản lý sản phẩm, quản lý nhân sự, quản lý khách hàng thành viên, xử lý bán hàng, lưu trữ hóa đơn và thống kê kinh doanh theo thời gian thực.

Đây là cơ hội quý báu để chúng em vận dụng tổng hợp kiến thức đã học trong môn Lập trình Java: lập trình hướng đối tượng, Java Swing, JDBC, MySQL và các design pattern như DAO–MVC, đồng thời rèn luyện kỹ năng làm việc nhóm, phân công công việc và quản lý mã nguồn trong một dự án thực tế.

Mặc dù đã cố gắng hoàn thiện, song do kinh nghiệm còn hạn chế, đồ án không tránh khỏi những thiếu sót. Chúng em rất mong nhận được sự góp ý của giảng viên để hoàn thiện hơn.

Chúng em xin chân thành cảm ơn giảng viên đã tận tình hướng dẫn và hỗ trợ trong suốt quá trình thực hiện đồ án này.

*Hà Nội, tháng 3 năm 2026*

*Nhóm sinh viên thực hiện*

---

# MỤC LỤC

- Lời nói đầu
- Mục lục
- Danh mục bảng biểu
- Danh mục hình vẽ
- Danh mục từ viết tắt
- **Tóm tắt đề tài và Phân công công việc**
- Chương 1: Công nghệ, Thư viện và Design Pattern
- Chương 2: Thiết kế Chức năng
- Chương 3: Thiết kế Giao diện
- Chương 4: Thiết kế Cơ sở Dữ liệu
- Chương 5: Mô tả Kết quả Phần mềm
- Kết luận
- Danh mục Tài liệu Tham khảo
- Phụ lục: Cấu trúc Mã nguồn và Hướng dẫn Cài đặt

---

# DANH MỤC BẢNG BIỂU

| Số bảng | Tên bảng |
|---|---|
| Bảng 1 | Phân công công việc nhóm |
| Bảng 2 | Công nghệ và thư viện sử dụng |
| Bảng 3 | Các design pattern áp dụng |
| Bảng 4 | Danh sách Use Case |
| Bảng 5 | Quy tắc hạng thành viên và chiết khấu |
| Bảng 6 | Phân quyền chức năng theo vai trò |
| Bảng 7 | Mô tả bảng tai_khoan |
| Bảng 8 | Mô tả bảng nhan_vien |
| Bảng 9 | Mô tả bảng san_pham |
| Bảng 10 | Mô tả bảng khach_hang |
| Bảng 11 | Mô tả bảng hoa_don |
| Bảng 12 | Mô tả bảng chi_tiet_hoa_don |

---

# DANH MỤC HÌNH VẼ

| Số hình | Tên hình |
|---|---|
| Hình 1 | Sơ đồ Use Case tổng quan |
| Hình 2 | Sơ đồ lớp (Class Diagram) |
| Hình 3 | Sơ đồ ERD (quan hệ thực thể) |
| Hình 4 | Màn hình Đăng nhập |
| Hình 5 | Giao diện chính – Cửa sổ MainFrame (Quản lý) |
| Hình 6 | Giao diện chính – Cửa sổ MainFrame (Nhân viên) |
| Hình 7 | Module Bán hàng |
| Hình 8 | Module Quản lý Sản phẩm (tô màu tồn kho) |
| Hình 9 | Module Thống kê (biểu đồ doanh thu + lọc ngày) |

---

# DANH MỤC TỪ VIẾT TẮT

| Từ viết tắt | Ý nghĩa |
|---|---|
| CSDL | Cơ sở dữ liệu |
| DAO | Data Access Object |
| MVC | Model–View–Controller |
| GUI | Graphical User Interface |
| JDBC | Java Database Connectivity |
| FK | Foreign Key – Khóa ngoại |
| PK | Primary Key – Khóa chính |
| SĐT | Số điện thoại |
| HSD | Hạn sử dụng |
| NSX | Ngày sản xuất |
| KPI | Key Performance Indicator |
| CRUD | Create – Read – Update – Delete |
| JDK | Java Development Kit |
| POJO | Plain Old Java Object |
| L&F | Look and Feel (giao diện Swing theme) |
| CSV | Comma-Separated Values |

---

# BẢNG PHÂN CÔNG CÔNG VIỆC

*(Bảng 1 – Phân công công việc)*

| STT | Họ và tên | MSSV | Nội dung công việc được giao | Đóng góp |
|---|---|---|---|---|
| 1 | Phạm Duy An | BIT240002 | Kiến trúc tổng thể; `DatabaseConnection` (auto-init CSDL); `LoginFrame`; `MainFrame` (phân quyền CardLayout); `TaiKhoanPanel`; `TaiKhoanDAO`; `UIConstants` (design system); `DoiMatKhauDialog` | **25%** |
| 2 | Bùi Đào Đức Anh | BIT240025 | `SanPhamPanel`; `SanPhamDAO`; Model `SanPham`; Validate ngày NSX/HSD; Tô màu hàng sắp hết / hết hàng; Lọc sản phẩm theo loại | **20%** |
| 3 | Đỗ Tuấn Anh | BIT240015 | `KhachHangPanel`; `KhachHangDAO`; `NhanVienPanel`; `NhanVienDAO`; Model `KhachHang` (getHang, getTyLeGiam); Model `NhanVien`; Validate tuổi nhân viên (≥ 18) | **20%** |
| 4 | Nguyễn Quang Hiếu | BIT240082 | `BanHangPanel`; `HoaDonDAO.taoHoaDon()` (JDBC Transaction); Logic chiết khấu; Cộng điểm tích lũy; Đăng ký khách hàng nhanh tại quầy | **20%** |
| 5 | Phan Văn Hiếu | BIT240094 | `HoaDonPanel`; `ThongKePanel` (lọc khoảng ngày, xuất CSV); `BarChartPanel` (Graphics2D); Truy vấn thống kê trong `HoaDonDAO`; Dashboard KPI cảnh báo | **15%** |

---

# TÓM TẮT ĐỀ TÀI

## I. Tên đề tài

**Hệ thống Quản lý Bán Sữa** — Ứng dụng quản lý cửa hàng kinh doanh sữa sử dụng Java Swing và MySQL, triển khai theo mô hình DAO–MVC.

## II. Mục tiêu

- Xây dựng ứng dụng desktop hỗ trợ quản lý toàn diện hoạt động kinh doanh của một cửa hàng bán sữa.
- Tự động hóa quy trình bán hàng: tra cứu sản phẩm, lập hóa đơn, trừ kho và cộng điểm tích lũy cho khách hàng thành viên trong một giao dịch duy nhất.
- Cung cấp dashboard thống kê doanh thu với bộ lọc khoảng thời gian, cảnh báo tồn kho và xuất báo cáo CSV.
- Áp dụng phân quyền người dùng (Quản lý / Nhân viên) đảm bảo an toàn và tính toàn vẹn dữ liệu.

## III. Nội dung chính

Hệ thống bao gồm 8 module chức năng:

| Module | Mô tả tóm tắt |
|---|---|
| Đăng nhập | Xác thực tài khoản, phân quyền Quản lý / Nhân viên |
| Bán hàng | Tìm sản phẩm, giỏ hàng, chiết khấu thành viên, thanh toán |
| Sản phẩm | CRUD sản phẩm sữa (loại, hãng, giá, tồn kho, HSD) |
| Khách hàng | CRUD thành viên, điểm tích lũy, hạng thành viên |
| Nhân sự | CRUD nhân viên với kiểm tra tuổi, liên kết tài khoản |
| Hóa đơn | Xem lịch sử hóa đơn, lọc theo ngày/SĐT, xem chi tiết |
| Thống kê | Dashboard KPI, biểu đồ doanh thu, lọc khoảng ngày, xuất CSV, cảnh báo tồn kho/hết hạn |
| Tài khoản | CRUD tài khoản đăng nhập, phân vai trò |

## IV. Kết quả thực hiện

- Ứng dụng chạy ổn định, đầy đủ chức năng, không có lỗi trong các luồng xử lý chính.
- Giao diện thân thiện, nhất quán về màu sắc và bố cục nhờ lớp `UIConstants` dùng chung.
- Phân quyền rõ ràng: Nhân viên chỉ truy cập Bán hàng và Xem sản phẩm; Quản lý có toàn quyền.
- Nhân viên có thể tự đổi mật khẩu qua `DoiMatKhauDialog` mà không cần quyền Quản lý.
- Module Thống kê hỗ trợ lọc theo khoảng ngày và xuất báo cáo CSV tương thích Excel.

---

# CHƯƠNG 1: CÔNG NGHỆ, THƯ VIỆN VÀ DESIGN PATTERN

## 1.1. Ngôn ngữ và Nền tảng

*(Bảng 2 – Công nghệ và thư viện sử dụng)*

| Công nghệ / Thư viện | Phiên bản | Mục đích sử dụng |
|---|---|---|
| Java SE | JDK 24 | Ngôn ngữ lập trình chính |
| Java Swing | Tích hợp trong JDK | Xây dựng giao diện người dùng desktop |
| MySQL | 8.x | Hệ quản trị cơ sở dữ liệu quan hệ |
| MySQL Connector/J | 9.2.0 | JDBC Driver kết nối Java với MySQL |
| Nimbus Look & Feel | Tích hợp trong JDK | Theme giao diện hiện đại cho Swing |

**Lý do lựa chọn:** Java Swing là framework GUI thuần túy, không phụ thuộc thêm bất kỳ thư viện ngoài nào (ngoài JDBC Driver), phù hợp với phạm vi đồ án môn học. MySQL là RDBMS phổ biến, được hỗ trợ tốt qua JDBC và có hiệu suất đáp ứng yêu cầu của ứng dụng quy mô nhỏ–vừa.

## 1.2. Design Pattern áp dụng

*(Bảng 3 – Các design pattern áp dụng)*

| Pattern | Nơi áp dụng | Lợi ích |
|---|---|---|
| Data Access Object (DAO) | Tầng dao/ | Tách biệt SQL khỏi GUI |
| Model–View–Controller (MVC) | Toàn bộ kiến trúc | Dễ bảo trì, mở rộng |
| Static Factory (Singleton biến thể) | DatabaseConnection | Quản lý kết nối tập trung |
| Template Method (biến thể) | UIConstants | Giao diện nhất quán |
| Observer | Swing EventListener | Phản ứng sự kiện người dùng |

### 1.2.1. Data Access Object (DAO)

Mỗi bảng trong CSDL được ánh xạ với một lớp DAO riêng biệt, chịu trách nhiệm duy nhất cho các thao tác SQL tương ứng: `SanPhamDAO` xử lý CRUD sản phẩm, trừ kho và đếm cảnh báo; `KhachHangDAO` xử lý CRUD khách hàng và cộng điểm; `NhanVienDAO` quản lý nhân sự; `TaiKhoanDAO` xử lý đăng nhập và đổi mật khẩu; `HoaDonDAO` tạo hóa đơn theo Transaction và cung cấp các truy vấn thống kê theo khoảng thời gian.

Nhờ DAO, khi cần thay đổi câu SQL (ví dụ thêm điều kiện lọc) chỉ cần sửa trong lớp DAO tương ứng mà không cần động đến lớp GUI.

> **[Chèn Hình 2: Sơ đồ Class Diagram – render từ 3 file `diagrams/ClassDiagram_Model.puml`, `ClassDiagram_DAO.puml`, `ClassDiagram_GUI.puml`]**

### 1.2.2. Model–View–Controller (MVC)

- **Model (`package model`):** Các lớp POJO biểu diễn đối tượng nghiệp vụ: `TaiKhoan`, `NhanVien`, `SanPham`, `KhachHang`, `HoaDon`, `ChiTietHoaDon`. Model không chứa logic hiển thị hay SQL.
- **View (`package gui`):** Các lớp Panel/Frame xây dựng giao diện bằng Java Swing, nhận dữ liệu từ DAO và hiển thị lên JTable, JLabel, JTextField,...
- **Controller (ActionListener bên trong View):** Logic xử lý sự kiện (click nút, nhập liệu) nằm trong các lớp Panel, điều phối gọi DAO rồi cập nhật View.

### 1.2.3. Static Factory Method

`DatabaseConnection.layKetNoi()` là phương thức tĩnh trả về một `Connection` mới mỗi lần gọi với cấu hình (host, port, user, password) được định nghĩa tập trung trong class. Cách này đảm bảo tất cả DAO dùng chung một bộ tham số kết nối và dễ thay đổi hàng loạt.

### 1.2.4. UIConstants – Template Method (biến thể)

```java
public static final Color PRIMARY = new Color(41, 128, 185);
public static final Color SUCCESS = new Color(39, 174, 96);
public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);

public static JButton createStyledButton(String text, Color color) { ... }
```

Mọi Panel đều gọi `UIConstants.createStyledButton()` thay vì tự tạo nút, đảm bảo giao diện đồng nhất về màu sắc và kích thước trên toàn ứng dụng.

### 1.2.5. Observer Pattern (Swing Event System)

Java Swing tích hợp sẵn Observer Pattern thông qua hệ thống sự kiện: các lớp Panel đóng vai trò Observer bằng cách đăng ký `ActionListener`, `DocumentListener`, `ListSelectionListener`... lên các component. Khi người dùng tương tác, Swing tự động kích hoạt callback tương ứng.

---

# CHƯƠNG 2: THIẾT KẾ CHỨC NĂNG

## 2.1. Danh sách Use Case

*(Bảng 4 – Danh sách Use Case)*

| Mã UC | Tên Use Case | Tác nhân | Mô tả |
|---|---|---|---|
| UC01 | Đăng nhập hệ thống | Quản lý, Nhân viên | Xác thực tên đăng nhập và mật khẩu |
| UC02 | Quản lý Sản phẩm (CRUD) | Quản lý | Thêm, sửa, xóa, tìm kiếm sản phẩm |
| UC03 | Quản lý Khách hàng (CRUD) | Quản lý | Thêm, sửa, xóa, tra cứu khách hàng thành viên |
| UC04 | Quản lý Nhân viên (CRUD) | Quản lý | Thêm, sửa, xóa thông tin nhân sự, liên kết tài khoản |
| UC05 | Quản lý Tài khoản (CRUD) | Quản lý | Thêm, sửa, xóa tài khoản đăng nhập |
| UC06 | Xem lịch sử Hóa đơn | Quản lý | Xem, lọc và tra cứu hóa đơn đã lập |
| UC07 | Xem Thống kê doanh thu | Quản lý | Xem KPI, biểu đồ, lọc khoảng ngày, xuất CSV |
| UC08 | Xem danh sách Sản phẩm | Quản lý, Nhân viên | Xem danh sách sản phẩm (chỉ đọc với NV) |
| UC09 | Bán hàng & Thanh toán | Nhân viên | Lập giỏ hàng, áp chiết khấu, thanh toán |
| UC10 | Trừ tồn kho sản phẩm | UC09 (include) | Tự động trừ kho sau khi thanh toán |
| UC11 | Cộng điểm tích lũy | UC09 (include) | Tự động cộng điểm cho KH thành viên |
| UC12 | Áp dụng chiết khấu thành viên | UC09 (extend) | Giảm giá theo hạng khi KH có tài khoản |
| UC13 | Đăng ký khách hàng mới | UC09 (extend) | Đăng ký thành viên mới tại quầy |
| UC14 | Đổi mật khẩu cá nhân | Nhân viên | Tự đổi mật khẩu không cần quyền Quản lý |

> **Lưu ý:** UC01 (Đăng nhập) là điều kiện tiên quyết bắt buộc cho tất cả Use Case khác.

> **[Chèn Hình 1: Sơ đồ Use Case – render từ file `diagrams/UseCase.puml`]**

## 2.2. Phân quyền theo vai trò

*(Bảng 6 – Phân quyền chức năng theo vai trò)*

| Chức năng | Quản lý | Nhân viên |
|---|:---:|:---:|
| Đăng nhập | ✓ | ✓ |
| Xem danh sách Sản phẩm | ✓ | ✓ |
| **Bán hàng & Thanh toán** | ✗ | ✓ |
| **Đổi mật khẩu cá nhân** | ✗ (dùng tab TK) | ✓ |
| Thêm / Sửa / Xóa Sản phẩm | ✓ | ✗ |
| Quản lý Khách hàng | ✓ | ✗ |
| Quản lý Nhân viên | ✓ | ✗ |
| Quản lý Tài khoản | ✓ | ✗ |
| Xem lịch sử Hóa đơn | ✓ | ✗ |
| Xem Thống kê & Xuất CSV | ✓ | ✗ |

**Lưu ý phân quyền đổi mật khẩu:** Quản lý đã có quyền quản lý toàn bộ tài khoản qua tab "Tài khoản" nên không cần nút riêng. Nhân viên có nút "Đổi mật khẩu" trên header để tự quản lý mật khẩu cá nhân mà không gây xung đột quyền.

## 2.3. Luồng xử lý Bán hàng (Use Case chính)

UC09 – Bán hàng & Thanh toán là Use Case trung tâm của hệ thống. Nhân viên tìm kiếm sản phẩm theo tên (lọc theo thời gian thực), chọn số lượng và thêm vào giỏ hàng. Hệ thống cho phép chọn khách hàng thành viên từ danh sách hoặc nhập SĐT để tra cứu — nếu tìm thấy sẽ tự động áp tỷ lệ chiết khấu tương ứng; nếu không tìm thấy sẽ đề xuất đăng ký mới. Nếu không chọn khách hàng, đơn hàng được ghi là "Khách lẻ" (không chiết khấu, không tích điểm). Khi nhân viên xác nhận thanh toán, `HoaDonDAO.taoHoaDon()` thực hiện toàn bộ thao tác trong một JDBC Transaction: tạo hóa đơn, ghi chi tiết từng sản phẩm, trừ tồn kho và cộng điểm tích lũy. Nếu bất kỳ bước nào thất bại, toàn bộ giao dịch được ROLLBACK.

## 2.4. Quy tắc nghiệp vụ Hạng thành viên và Chiết khấu

*(Bảng 5 – Quy tắc hạng thành viên và chiết khấu)*

| Hạng thành viên | Điểm tích lũy | Chiết khấu | Màu hiển thị |
|---|---|---|---|
| Thường | 0 – 9 điểm | 0% | Trắng |
| Bạc | 10 – 49 điểm | 2% | Xám bạc |
| Vàng | 50 – 99 điểm | 5% | Vàng |
| Kim cương | ≥ 100 điểm | 10% | Xanh ngọc |

**Quy tắc tích điểm:** Mỗi 100.000 VNĐ (tính trên tổng tiền sau chiết khấu) = 1 điểm.

**Công thức tính tiền:**
```
Tổng tiền gốc   = Σ (số lượng × đơn giá)
Tiền chiết khấu  = Tổng tiền gốc × tỷ lệ chiết khấu
Thanh toán       = Tổng tiền gốc − Tiền chiết khấu
Điểm cộng thêm   = floor(Thanh toán / 100.000)
```

## 2.5. Luồng Nhân viên – Tài khoản

Hệ thống thiết kế quan hệ **1–1 (nullable)** giữa `nhan_vien` và `tai_khoan`:

1. Quản lý tạo **Tài khoản** trước (tab Tài khoản) → thiết lập `ten_dang_nhap`, `mat_khau`, `vai_tro`.
2. Quản lý tạo **Nhân viên** (tab Nhân viên) → ComboBox "Tài khoản" chỉ hiện các TK vai trò "Nhân viên" chưa liên kết → chọn để liên kết.
3. Nhân viên đã liên kết TK sẽ đăng nhập được vào hệ thống với giao diện bán hàng.
4. Tài khoản vai trò "Quản lý" **không** nằm trong bảng nhân viên — đăng nhập trực tiếp với giao diện quản trị.

## 2.6. Luồng xử lý Đăng nhập

Người dùng nhập tên đăng nhập và mật khẩu tại `LoginFrame`. Hệ thống gọi `TaiKhoanDAO.dangNhap()` để truy vấn cơ sở dữ liệu. Nếu thông tin hợp lệ, `MainFrame` được khởi tạo với bộ menu tương ứng vai trò: Quản lý thấy 6 mục menu (Sản phẩm, Khách hàng, Nhân viên, Hóa đơn, Thống kê, Tài khoản), Nhân viên thấy 2 mục (Bán hàng, Sản phẩm) cùng nút "Đổi mật khẩu" trên header.

---

# CHƯƠNG 3: THIẾT KẾ GIAO DIỆN

## 3.1. Tổng quan thiết kế

Ứng dụng sử dụng **Nimbus Look & Feel** — theme hiện đại tích hợp sẵn trong JDK — kết hợp với bảng màu tùy chỉnh trong `UIConstants`:

- **Màu chủ đạo (Primary):** `#2980B9` (xanh dương đậm) — cho header, nút chính
- **Màu thành công (Success):** `#27AE60` (xanh lá) — cho nút Thêm, Thanh toán
- **Màu cảnh báo (Danger):** `#E74C3C` (đỏ) — cho nút Xóa, Đăng xuất
- **Màu thông tin (Info):** `#3498DB` (xanh nhạt) — cho thẻ KPI
- **Font chữ:** Segoe UI (Windows) / Dialog (dự phòng)

Tất cả nút bấm sử dụng `UIConstants.createStyledButton()` — factory method thiết lập `setBackground()`, `setOpaque(true)`, `setBorderPainted(false)` để đảm bảo màu hiển thị đúng dưới Nimbus L&F.

## 3.2. Màn hình Đăng nhập (LoginFrame)

Card trắng đặt giữa nền xám gradient. Header nền xanh `PRIMARY_COLOR` hiển thị tên hệ thống. Form nhập tên đăng nhập và mật khẩu (JPasswordField ẩn ký tự), hỗ trợ nhấn **Enter** để đăng nhập. Thông báo lỗi hiển thị ngay bên dưới form.

> **[Chèn Hình 4: Màn hình Đăng nhập]**

## 3.3. Cửa sổ chính (MainFrame)

Bố cục chia 2 vùng: **thanh menu trái** (200px) chứa các nút chức năng dạng dọc; **vùng nội dung phải** sử dụng `CardLayout`. Header hiển thị tên người dùng, vai trò, ngày hiện tại và nút "Đổi mật khẩu" (chỉ với Nhân viên).

> **[Chèn Hình 5: Giao diện Quản lý (6 menu)]**
> **[Chèn Hình 6: Giao diện Nhân viên (2 menu + nút đổi MK)]**

## 3.4. Module Bán hàng (BanHangPanel)

Bố cục 3 vùng: **(1)** Danh sách sản phẩm với tìm kiếm realtime và Spinner số lượng; **(2)** Giỏ hàng (chỉnh sửa, xóa từng dòng); **(3)** Thanh toán: chọn khách hàng từ combo (mặc định "Khách lẻ"), hiển thị hạng và chiết khấu, xác nhận thanh toán. Hỗ trợ đăng ký khách hàng mới nhanh tại quầy.

> **[Chèn Hình 7: Module Bán hàng]**

## 3.5. Giao diện CRUD

Các module Sản phẩm, Khách hàng, Nhân viên, Tài khoản tuân theo bố cục chuẩn: **Form nhập liệu phía trên** + nhóm nút Thêm/Sửa/Xóa/Làm mới; **JTable phía dưới** hiển thị dữ liệu. Click hàng → điền form → chỉnh sửa → lưu.

> **[Chèn Hình 8: Module Sản phẩm – tô màu tồn kho]**

## 3.6. Module Thống kê (ThongKePanel)

Gồm:
- **5 thẻ KPI:** Doanh thu hôm nay / Doanh thu tháng / Số hóa đơn hôm nay / Sản phẩm sắp hết hạn (7 ngày) / Sản phẩm hết hàng
- **Bộ lọc khoảng ngày:** Từ ngày – Đến ngày (JSpinner Date) + nút "Lọc" + nút "Xuất CSV"
- **Biểu đồ cột** doanh thu tự vẽ bằng `Graphics2D`
- **Bảng Top 5 sản phẩm** bán chạy, tô màu vàng/bạc/đồng

> **[Chèn Hình 9: Module Thống kê]**

---

# CHƯƠNG 4: THIẾT KẾ CƠ SỞ DỮ LIỆU

## 4.1. Tổng quan

CSDL `quanlybansua`, MySQL 8.x, bộ mã `utf8mb4_unicode_ci`. Toàn bộ cấu trúc và dữ liệu mẫu tự động khởi tạo bởi `DatabaseConnection.khoiTaoDatabase()` khi chạy lần đầu.

Hệ thống gồm **6 bảng**:

| Bảng | Vai trò |
|---|---|
| `tai_khoan` | Tài khoản đăng nhập và phân quyền |
| `nhan_vien` | Thông tin nhân viên (chỉ vai trò NV, không bao gồm QL) |
| `san_pham` | Danh mục sản phẩm sữa và tồn kho |
| `khach_hang` | Thành viên khách hàng và điểm tích lũy |
| `hoa_don` | Hóa đơn bán hàng |
| `chi_tiet_hoa_don` | Chi tiết từng dòng sản phẩm trong hóa đơn |

## 4.2. Mô tả chi tiết các bảng

### 4.2.1. Bảng `tai_khoan`

| Cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
|---|---|---|---|
| id | INT | PK, AUTO_INCREMENT | Mã tài khoản |
| ten_dang_nhap | VARCHAR(50) | UNIQUE, NOT NULL | Tên đăng nhập |
| mat_khau | VARCHAR(100) | NOT NULL | Mật khẩu |
| vai_tro | VARCHAR(20) | DEFAULT 'nhân viên' | 'Quản lý' hoặc 'Nhân viên' |

### 4.2.2. Bảng `nhan_vien`

| Cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
|---|---|---|---|
| id | INT | PK, AUTO_INCREMENT | Mã nhân sự |
| ho_ten | VARCHAR(100) | NOT NULL | Họ và tên |
| sdt | VARCHAR(15) | — | Số điện thoại |
| dia_chi | VARCHAR(200) | — | Địa chỉ |
| ngay_sinh | DATE | — | Ngày sinh (validate ≥ 18 tuổi) |
| gioi_tinh | VARCHAR(5) | — | Nam / Nữ |
| id_tai_khoan | INT | FK → tai_khoan.id | Liên kết TK (chỉ TK vai trò NV) |

### 4.2.3. Bảng `san_pham`

| Cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
|---|---|---|---|
| id | INT | PK, AUTO_INCREMENT | Mã sản phẩm |
| ten_sp | VARCHAR(150) | NOT NULL | Tên sản phẩm |
| loai | VARCHAR(50) | — | Phân loại (Sữa tươi, Sữa bột,...) |
| thuong_hieu | VARCHAR(100) | — | Thương hiệu |
| don_gia | DOUBLE | NOT NULL | Đơn giá (VNĐ) |
| so_luong | INT | DEFAULT 0 | Tồn kho |
| mo_ta | TEXT | — | Mô tả chi tiết |
| ngay_san_xuat | DATE | — | Ngày sản xuất |
| han_su_dung | DATE | — | Hạn sử dụng |

### 4.2.4. Bảng `khach_hang`

| Cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
|---|---|---|---|
| id | INT | PK, AUTO_INCREMENT | Mã khách hàng |
| ho_ten | VARCHAR(100) | NOT NULL | Họ và tên |
| sdt | VARCHAR(15) | — | Số điện thoại |
| dia_chi | VARCHAR(200) | — | Địa chỉ |
| diem_tich_luy | INT | DEFAULT 0 | Điểm tích lũy |

**Ghi chú:** Khách lẻ (không đăng ký thành viên) được xử lý bằng `id_khach_hang = NULL` trong hóa đơn.

### 4.2.5. Bảng `hoa_don`

| Cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
|---|---|---|---|
| id | INT | PK, AUTO_INCREMENT | Mã hóa đơn |
| id_nhan_vien | INT | FK → nhan_vien.id | NV lập hóa đơn |
| id_khach_hang | INT | FK, NULL | KH (NULL = khách lẻ) |
| ngay_lap | DATETIME | DEFAULT CURRENT_TIMESTAMP | Thời điểm thanh toán |
| tong_tien | DOUBLE | DEFAULT 0 | Tổng tiền sau chiết khấu |

### 4.2.6. Bảng `chi_tiet_hoa_don`

| Cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
|---|---|---|---|
| id | INT | PK, AUTO_INCREMENT | Mã chi tiết |
| id_hoa_don | INT | FK → hoa_don.id | Hóa đơn chứa dòng này |
| id_san_pham | INT | FK → san_pham.id | Sản phẩm được mua |
| so_luong | INT | NOT NULL | Số lượng mua |
| don_gia | DOUBLE | NOT NULL | Đơn giá tại thời điểm mua (snapshot) |
| thanh_tien | DOUBLE | NOT NULL | = so_luong × don_gia |

**Ghi chú:** `don_gia` lưu **tại thời điểm giao dịch**, đảm bảo lịch sử không bị ảnh hưởng khi giá sản phẩm thay đổi.

## 4.3. Sơ đồ quan hệ (ERD)

- `tai_khoan` ← 1:1 → `nhan_vien` (qua `id_tai_khoan`, nullable)
- `nhan_vien` ← 1:N → `hoa_don`
- `khach_hang` ← 1:N → `hoa_don` (nullable)
- `hoa_don` ← 1:N → `chi_tiet_hoa_don`
- `san_pham` ← 1:N → `chi_tiet_hoa_don`

> **[Chèn Hình 3: Sơ đồ ERD]**

## 4.4. JDBC Transaction

```java
conn.setAutoCommit(false);
try {
    // 1. INSERT hoa_don → lấy generatedKey
    // 2. INSERT chi_tiet_hoa_don (từng dòng)
    // 3. UPDATE san_pham SET so_luong = so_luong - ?
    // 4. UPDATE khach_hang SET diem_tich_luy += ? (nếu có KH)
    conn.commit();
} catch (SQLException e) {
    conn.rollback(); // Hoàn tác TẤT CẢ
}
```

---

# CHƯƠNG 5: MÔ TẢ KẾT QUẢ PHẦN MỀM

## 5.1. Màn hình Đăng nhập

Hệ thống tự tạo DB + bảng + dữ liệu mẫu khi khởi động lần đầu.

**Tài khoản mặc định:**

| Tên đăng nhập | Mật khẩu | Vai trò |
|---|---|---|
| duyan | admin123 | Quản lý |
| ducanh | 123456 | Nhân viên |
| tuananh | 123456 | Nhân viên |
| quanghieu | 123456 | Nhân viên |
| vanhieu | 123456 | Nhân viên |

## 5.2. Module Bán hàng

1. Gõ tên sản phẩm → lọc tức thì (DocumentListener).
2. Chọn SP → Spinner giới hạn theo tồn kho.
3. "Thêm vào giỏ" → gộp nếu trùng SP.
4. Chọn khách hàng (mặc định "Khách lẻ") hoặc nhập SĐT tra cứu.
5. "Thanh toán" → hiện tổng gốc, chiết khấu, thực trả → xác nhận.
6. Transaction: tạo HD + ghi chi tiết + trừ kho + cộng điểm.

## 5.3. Module Quản lý Sản phẩm

- Validate: đơn giá > 0, HSD > NSX, tên không trống.
- Tô màu tồn kho: `= 0` đỏ (hết hàng), `< 5` cam (sắp hết).
- Lọc theo loại (ComboBox) + tìm theo tên.
- Nhân viên: chỉ xem, ẩn nút CRUD.

## 5.4. Module Quản lý Khách hàng

- Kiểm tra trùng SĐT, tô màu theo hạng thành viên.
- Xóa KH đã có hóa đơn → chuyển hóa đơn thành "Khách lẻ".

## 5.5. Module Quản lý Nhân sự

- Validate: tuổi ≥ 18, SĐT 10 số bắt đầu bằng 0, địa chỉ ≥ 10 ký tự.
- ComboBox "Tài khoản" chỉ hiện TK vai trò NV chưa liên kết.

## 5.6. Module Hóa đơn

- Lọc theo khoảng ngày, SĐT khách hàng.
- Click hóa đơn → bảng chi tiết phía dưới hiển thị danh sách SP.

## 5.7. Module Thống kê

- **5 KPI Cards:** Doanh thu hôm nay, doanh thu tháng, số HD hôm nay, SP sắp hết hạn (7 ngày), SP hết hàng.
- **Bộ lọc khoảng ngày:** Chọn Từ ngày – Đến ngày → "Lọc" → cập nhật biểu đồ + Top 5.
- **Biểu đồ cột:** Tự vẽ `Graphics2D`, nhãn ngày và giá trị trên đầu cột.
- **Top 5 SP bán chạy:** Tô vàng/bạc/đồng cho 3 vị trí đầu.
- **Xuất CSV:** Xuất báo cáo doanh thu + Top 5 ra file `.csv` với UTF-8 BOM, tương thích Excel.

## 5.8. Module Tài khoản

- CRUD tài khoản, phân vai trò Quản lý / Nhân viên.
- `UNIQUE` constraint ngăn trùng tên đăng nhập.

## 5.9. Đổi mật khẩu cá nhân (DoiMatKhauDialog)

- Chỉ hiển thị với vai trò Nhân viên (nút trên header).
- Yêu cầu nhập mật khẩu cũ → mật khẩu mới → xác nhận.
- Validate: mật khẩu mới ≥ 4 ký tự, khớp xác nhận, khác MK cũ, đúng MK cũ.
- Gọi `TaiKhoanDAO.doiMatKhau()` sử dụng `PreparedStatement`.

---

# KẾT LUẬN

## Kết quả đạt được

- **8 module** hoạt động ổn định, liên kết chặt chẽ, phân quyền rõ ràng.
- **Kiến trúc sạch** DAO–MVC: tách biệt hoàn toàn 3 tầng.
- **Toàn vẹn dữ liệu** qua JDBC Transaction trong thanh toán.
- **Tự động hóa triển khai:** Auto-init CSDL + bảng + 17 sản phẩm mẫu.
- **Thống kê nâng cao:** Lọc khoảng ngày, cảnh báo tồn kho/hết hạn, xuất CSV.
- **Tự quản lý mật khẩu:** Nhân viên đổi MK không cần quyền quản trị.

## Hạn chế

- Mật khẩu lưu **plain-text** — chưa hash bằng BCrypt/SHA-256.
- Chưa có chức năng **in hóa đơn** ra PDF.
- Giao diện **chưa responsive** — kích thước cố định.
- Chưa có **backup CSDL** định kỳ.

## Hướng phát triển

1. **Bảo mật:** Hash mật khẩu với BCrypt; khóa TK sau nhiều lần đăng nhập sai.
2. **In ấn & Xuất file:** iText cho hóa đơn PDF; Apache POI cho báo cáo Excel.
3. **Thống kê nâng cao:** Biểu đồ đường, biểu đồ tròn phân tích loại SP.
4. **Web Application:** Spring Boot + React cho đa thiết bị, đa người dùng.
5. **Nhập hàng:** Module quản lý nhà cung cấp và đặt hàng.

---

# DANH MỤC TÀI LIỆU THAM KHẢO

[1] Oracle Corporation, "Java SE 24 Documentation," *Oracle Help Center*, 2024. [Online]. Available: https://docs.oracle.com/en/java/javase/24/. [Accessed: Mar. 2026].

[2] Oracle Corporation, "Creating a GUI with Swing," *The Java™ Tutorials*, 2023. [Online]. Available: https://docs.oracle.com/javase/tutorial/uiswing/. [Accessed: Mar. 2026].

[3] MySQL AB, "MySQL 8.0 Reference Manual," *MySQL Developer Zone*, 2023. [Online]. Available: https://dev.mysql.com/doc/refman/8.0/en/. [Accessed: Mar. 2026].

[4] MySQL Development Team, "MySQL Connector/J 9.x Developer Guide," *MySQL Developer Zone*, 2024. [Online]. Available: https://dev.mysql.com/doc/connector-j/en/. [Accessed: Mar. 2026].

[5] E. Gamma, R. Helm, R. Johnson, and J. Vlissides, *Design Patterns: Elements of Reusable Object-Oriented Software*. Reading, MA: Addison-Wesley, 1994.

[6] P. Deitel and H. Deitel, *Java How to Program, Early Objects*, 11th ed. Upper Saddle River, NJ: Pearson Education, 2017.

[7] Baeldung, "Introduction to Java Database Connectivity (JDBC)," *Baeldung*, 2024. [Online]. Available: https://www.baeldung.com/java-jdbc. [Accessed: Mar. 2026].

[8] TutorialsPoint, "Swing Tutorial," *TutorialsPoint*, 2023. [Online]. Available: https://www.tutorialspoint.com/swing/. [Accessed: Mar. 2026].

[9] C. S. Horstmann, *Core Java Volume I – Fundamentals*, 11th ed. Upper Saddle River, NJ: Prentice Hall, 2018.

---

# PHỤ LỤC: CẤU TRÚC MÃ NGUỒN VÀ HƯỚNG DẪN CÀI ĐẶT

## A. Cấu trúc thư mục dự án

```
QuanLyBanSua/
├── src/
│   ├── Main.java                    ← Điểm khởi chạy ứng dụng
│   │
│   ├── model/                       ← Tầng Model (POJO)
│   │   ├── TaiKhoan.java
│   │   ├── NhanVien.java
│   │   ├── SanPham.java
│   │   ├── KhachHang.java           (+ getHang(), getTyLeGiam())
│   │   ├── HoaDon.java
│   │   └── ChiTietHoaDon.java
│   │
│   ├── dao/                         ← Tầng DAO (truy cập CSDL)
│   │   ├── DatabaseConnection.java  ← Auto-init CSDL + dữ liệu mẫu
│   │   ├── TaiKhoanDAO.java         ← CRUD + đăng nhập + đổi mật khẩu
│   │   ├── NhanVienDAO.java
│   │   ├── SanPhamDAO.java          ← + đếm sắp hết hạn, hết hàng
│   │   ├── KhachHangDAO.java
│   │   └── HoaDonDAO.java           ← Transaction + thống kê khoảng ngày
│   │
│   ├── gui/                         ← Tầng View (Java Swing)
│   │   ├── LoginFrame.java
│   │   ├── MainFrame.java           ← CardLayout + phân quyền menu
│   │   ├── BanHangPanel.java        ← Giỏ hàng, chiết khấu, thanh toán
│   │   ├── SanPhamPanel.java
│   │   ├── KhachHangPanel.java
│   │   ├── NhanVienPanel.java
│   │   ├── HoaDonPanel.java
│   │   ├── ThongKePanel.java        ← KPI + lọc ngày + xuất CSV
│   │   ├── TaiKhoanPanel.java
│   │   └── DoiMatKhauDialog.java    ← Đổi MK cá nhân (NV)
│   │
│   └── utils/                       ← Tiện ích dùng chung
│       ├── UIConstants.java         ← Màu sắc, font, factory nút
│       ├── AppConstants.java        ← Hằng số nghiệp vụ
│       ├── InputFilter.java         ← DocumentFilter (chỉ số)
│       └── TableHelper.java         ← Zebra renderer, label factory
│
├── lib/
│   └── mysql-connector-j-9.2.0.jar
│
└── diagrams/
    ├── UseCase.puml               ← 14 Use Case, 2 Actor
    ├── ClassDiagram_Model.puml    ← 6 lớp Model (POJO)
    ├── ClassDiagram_DAO.puml      ← 7 lớp DAO + DatabaseConnection
    ├── ClassDiagram_GUI.puml      ← 10 lớp GUI + 4 lớp Utils + Entry
    └── ERD.puml                   ← 6 bảng CSDL + quan hệ
```

## B. Hướng dẫn Cài đặt và Chạy

**Yêu cầu:**

| Thành phần | Phiên bản |
|---|---|
| Java JDK | 11+ (khuyến nghị 17 hoặc 24) |
| MySQL Server | 8.x |
| IDE | Eclipse / IntelliJ IDEA |

**Các bước:**

1. Clone/copy thư mục `QuanLyBanSua` vào máy.

2. **Cấu hình MySQL:** Sửa `src/dao/DatabaseConnection.java` (dòng 17–18):
```java
private static final String TAI_KHOAN = "root";
private static final String MAT_KHAU  = "root123";
```

3. **Import vào Eclipse:** File → Import → Existing Projects into Workspace.

4. **Thêm JDBC Driver:** Build Path → Add External JARs → chọn `lib/mysql-connector-j-9.2.0.jar`.

5. **Chạy:** Run `src/Main.java` → DB tự tạo lần đầu.

6. **Đăng nhập:** `duyan` / `admin123` (Quản lý).

---

*Hết báo cáo*
