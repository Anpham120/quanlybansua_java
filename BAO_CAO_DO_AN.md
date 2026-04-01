# TRƯỜNG ĐẠI HỌC CMC
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
| Hình 2a | Sơ đồ lớp – Tầng Model (6 POJO) |
| Hình 2b | Sơ đồ lớp – Tầng DAO (7 lớp) |
| Hình 2c | Sơ đồ lớp – Tầng GUI + Utils (15 lớp) |
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
| DAO | Data Access Object – Đối tượng truy cập dữ liệu, tầng chứa logic truy vấn SQL |
| MVC | Model–View–Controller – Mô hình phân tách ứng dụng thành 3 tầng: Dữ liệu, Giao diện, Điều khiển |
| GUI | Graphical User Interface – Giao diện đồ họa người dùng |
| JDBC | Java Database Connectivity – API kết nối Java với cơ sở dữ liệu |
| FK | Foreign Key – Khóa ngoại, ràng buộc tham chiếu giữa hai bảng |
| PK | Primary Key – Khóa chính, định danh duy nhất của một bản ghi |
| SĐT | Số điện thoại |
| HSD | Hạn sử dụng (của sản phẩm) |
| NSX | Ngày sản xuất (của sản phẩm) |
| KPI | Key Performance Indicator – Chỉ số đo lường hiệu quả kinh doanh |
| CRUD | Create – Read – Update – Delete – Bốn thao tác cơ bản trên dữ liệu |
| JDK | Java Development Kit – Bộ công cụ phát triển Java |
| POJO | Plain Old Java Object – Lớp Java thuần chỉ chứa thuộc tính và getter/setter |
| L&F | Look and Feel – Giao diện chủ đề của Swing (Nimbus, Metal, System...) |
| CSV | Comma-Separated Values – Định dạng file dữ liệu phân tách bằng dấu phẩy |

---

# BẢNG PHÂN CÔNG CÔNG VIỆC

*(Bảng 1 – Phân công công việc)*

| STT | Họ và tên | MSSV | Nội dung công việc được giao | Đóng góp |
|---|---|---|---|---|
| 1 | Phạm Duy An | BIT240002 | Kiến trúc tổng thể; `Main`; `DatabaseConnection` (auto-init CSDL + dữ liệu mẫu); `LoginFrame`; `MainFrame` (phân quyền CardLayout); `DoiMatKhauDialog`; `UIConstants` (design system); `AppConstants` | **20%** |
| 2 | Bùi Đào Đức Anh | BIT240025 | `SanPhamPanel`; `SanPhamDAO`; Model `SanPham`; Validate ngày NSX/HSD; Tô màu hàng sắp hết / hết hàng; Lọc sản phẩm theo loại; `InputFilter`; `TableHelper` | **20%** |
| 3 | Đỗ Tuấn Anh | BIT240015 | `KhachHangPanel`; `KhachHangDAO`; Model `KhachHang` (getHang, getTyLeGiam); `NhanVienPanel`; `NhanVienDAO`; Model `NhanVien`; Validate tuổi nhân viên (≥ 18) | **20%** |
| 4 | Nguyễn Quang Hiếu | BIT240082 | `BanHangPanel`; `TaiKhoanPanel`; `TaiKhoanDAO`; Model `TaiKhoan`; Logic chiết khấu; Cộng điểm tích lũy; Đăng ký khách hàng nhanh tại quầy | **20%** |
| 5 | Phan Văn Hiếu | BIT240094 | `HoaDonPanel`; `HoaDonDAO` (JDBC Transaction + thống kê khoảng ngày); `ThongKePanel`; Biểu đồ cột (`Graphics2D`); Xuất CSV; Model `HoaDon`; Model `ChiTietHoaDon` | **20%** |

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

> **[Chèn Hình 2a: Sơ đồ lớp – Tầng Model – render từ `diagrams/ClassDiagram_Model.puml`]**

> **[Chèn Hình 2b: Sơ đồ lớp – Tầng DAO – render từ `diagrams/ClassDiagram_DAO.puml`]**

> **[Chèn Hình 2c: Sơ đồ lớp – Tầng GUI + Utils – render từ `diagrams/ClassDiagram_GUI.puml`]**

### 1.2.2. Mô hình Model–View–Controller (MVC)

- **Model (`package model`):** Các lớp POJO biểu diễn đối tượng nghiệp vụ: `TaiKhoan`, `NhanVien`, `SanPham`, `KhachHang`, `HoaDon`, `ChiTietHoaDon`. Model không chứa logic hiển thị hay SQL.
- **View (`package gui`):** Các lớp Panel/Frame xây dựng giao diện bằng Java Swing, nhận dữ liệu từ DAO và hiển thị lên JTable, JLabel, JTextField,...
- **Controller (ActionListener bên trong View):** Logic xử lý sự kiện (click nút, nhập liệu) nằm trong các lớp Panel, điều phối gọi DAO rồi cập nhật View.

### 1.2.3. Phương thức khởi tạo tĩnh (Static Factory Method)

`DatabaseConnection.layKetNoi()` là phương thức tĩnh trả về một `Connection` mới mỗi lần gọi với cấu hình (host, port, user, password) được định nghĩa tập trung trong class. Cách này đảm bảo tất cả DAO dùng chung một bộ tham số kết nối và dễ thay đổi hàng loạt.

### 1.2.4. UIConstants – Mẫu khuôn (Template Method biến thể)

```java
public static final Color PRIMARY = new Color(41, 128, 185);
public static final Color SUCCESS = new Color(39, 174, 96);
public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);

public static JButton createStyledButton(String text, Color color) { ... }
```

Mọi Panel đều gọi `UIConstants.createStyledButton()` thay vì tự tạo nút, đảm bảo giao diện đồng nhất về màu sắc và kích thước trên toàn ứng dụng.

### 1.2.5. Mẫu quan sát (Observer Pattern – Hệ thống sự kiện Swing)

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

## 2.7. Luồng CRUD Sản phẩm

**Tác nhân:** Quản lý (CRUD đầy đủ) · Nhân viên (chỉ xem)

1. Quản lý mở tab **Sản phẩm** → bảng hiển thị toàn bộ sản phẩm, tô màu: đỏ (hết hàng, `so_luong = 0`), cam (sắp hết, `so_luong < 5`).
2. **Thêm:** Nhập tên, loại, thương hiệu, đơn giá, số lượng, NSX, HSD → hệ thống validate:
   - Tên không trống, không trùng tên SP đã có (gọi `SanPhamDAO.kiemTraTrung()`)
   - Thương hiệu không trống
   - Đơn giá > 0, số lượng ≥ 0
   - HSD phải sau NSX
   → Gọi `SanPhamDAO.them()` → `INSERT` vào bảng `san_pham` → refresh bảng.
3. **Sửa:** Click hàng → form điền dữ liệu → chỉnh sửa → nhấn **Sửa** → validate tương tự (bỏ qua trùng tên chính nó) → gọi `SanPhamDAO.capNhat()` → `UPDATE` → refresh.
4. **Xóa:** Chọn hàng → kiểm tra `SanPhamDAO.kiemTraLienKetHoaDon()` → nếu có: cảnh báo "chi tiết hóa đơn liên quan cũng sẽ bị xóa" → xác nhận → gọi `SanPhamDAO.xoaCoLienKet()` → `DELETE`.
5. **Tìm kiếm:** Gõ tên vào ô tìm kiếm → nhấn **Tìm** (hoặc Enter) → gọi `SanPhamDAO.timKiem()`.
6. **Lọc theo loại:** Chọn loại từ `ComboBox` → kết hợp với từ khóa tìm kiếm → gọi `SanPhamDAO.timKiem(tuKhoa, loai)`.
7. **Nhân viên:** Truy cập tab Sản phẩm ở chế độ chỉ đọc — các nút Thêm/Sửa/Xóa bị ẩn hoàn toàn.

## 2.8. Luồng CRUD Khách hàng

**Tác nhân:** Quản lý

1. Mở tab **Khách hàng** → bảng hiển thị danh sách thành viên, cột "Hạng" tự động tính từ `KhachHang.getHang()`, tô màu theo hạng (Bạc = xám, Vàng = vàng, Kim cương = xanh ngọc).
2. **Thêm:** Nhập họ tên, SĐT, địa chỉ → validate:
   - Họ tên không trống, ≥ 2 ký tự, chỉ chứa chữ cái và dấu cách
   - SĐT 10 chữ số, bắt đầu bằng "0"
   - Không trùng SĐT đã có (gọi `KhachHangDAO.kiemTraTrungSDT()`)
   → Gọi `KhachHangDAO.them()` → `INSERT` với `diem_tich_luy = 0` → refresh.
3. **Sửa:** Click hàng → chỉnh sửa thông tin (Quản lý có thể sửa điểm tích lũy, Nhân viên không) → validate + kiểm tra trùng SĐT (bỏ qua chính KH đang sửa) → gọi `KhachHangDAO.capNhat()`.
4. **Xóa:** Chọn hàng → kiểm tra `KhachHangDAO.kiemTraLienKetHoaDon()` → nếu có: cảnh báo "hóa đơn sẽ chuyển thành Khách lẻ" → xác nhận → gọi `KhachHangDAO.xoaCoLienKet()` (SET `id_khach_hang = NULL` + xóa KH).
5. **Tìm kiếm:** Gõ tên hoặc SĐT → nhấn **Tìm** (hoặc Enter) → gọi `KhachHangDAO.timKiem()` → lọc danh sách.

## 2.9. Luồng CRUD Nhân viên

**Tác nhân:** Quản lý

1. Mở tab **Nhân viên** → bảng hiển thị danh sách nhân sự kèm tên tài khoản liên kết.
2. **Thêm:** Nhập họ tên, SĐT, địa chỉ, ngày sinh, giới tính, chọn tài khoản → validate:
   - Họ tên không trống, tối thiểu 2 ký tự, chỉ chứa chữ cái
   - Tuổi ≥ 18 (tính từ `ngay_sinh` đến ngày hiện tại)
   - SĐT 10 chữ số, bắt đầu bằng "0", không trùng SĐT đã có
   - Địa chỉ ≥ 10 ký tự
   → Gọi `NhanVienDAO.them()` → `INSERT` → refresh.
3. **Sửa:** Click hàng → chỉnh sửa → validate tương tự → `NhanVienDAO.capNhat()`.
4. **Xóa:** Chọn hàng → xác nhận → nếu NV đã lập hóa đơn → cảnh báo "các hóa đơn liên quan sẽ hiển thị '—' thay vì tên NV" → xác nhận lần 2 → gọi `NhanVienDAO.xoaCoLienKet()` (SET `id_nhan_vien = NULL` trong `hoa_don` + xóa NV, trong JDBC Transaction).
5. **Tìm kiếm:** Gõ tên hoặc SĐT vào ô tìm kiếm → nhấn **Tìm** (hoặc Enter) → gọi `NhanVienDAO.timKiem()` → lọc danh sách.
6. **ComboBox Tài khoản:** Chỉ hiển thị các tài khoản vai trò "Nhân viên" chưa được liên kết bởi nhân viên khác + tài khoản hiện tại đang chọn.

## 2.10. Luồng CRUD Tài khoản

**Tác nhân:** Quản lý

1. Mở tab **Tài khoản** → bảng hiển thị STT, mã TK, tên đăng nhập, mật khẩu, vai trò.
2. **Thêm:** Nhập tên đăng nhập, mật khẩu, chọn vai trò → validate:
   - Tên đăng nhập không trống, ≥ 4 ký tự, không chứa khoảng trắng
   - Mật khẩu ≥ 6 ký tự
   - Tên đăng nhập không trùng (gọi `TaiKhoanDAO.kiemTraTrungTenDN()`)
   → Gọi `TaiKhoanDAO.them()` → `INSERT`.
3. **Sửa:** Click hàng → form điền dữ liệu → chỉnh sửa → validate tương tự (bỏ qua trùng TK đang sửa) → `TaiKhoanDAO.capNhat()`.
4. **Xóa:** Chọn hàng → kiểm tra `TaiKhoanDAO.kiemTraLienKetNhanVien()` → nếu có liên kết: cảnh báo "nhân viên liên quan sẽ mất liên kết đăng nhập" → xác nhận → gọi `TaiKhoanDAO.xoaCoLienKet()`:
   - SET `id_tai_khoan = NULL` trong `nhan_vien`
   - SET `id_nhan_vien = NULL` trong `hoa_don` (nếu NV liên kết có hóa đơn)
   - Xóa tài khoản
   - Tất cả trong **JDBC Transaction** đảm bảo toàn vẹn.
5. **Tìm kiếm:** Gõ tên đăng nhập vào ô tìm kiếm → nhấn **Tìm** (hoặc Enter) → gọi `TaiKhoanDAO.timKiem()` → lọc danh sách.

## 2.11. Luồng Xem Hóa đơn

**Tác nhân:** Quản lý

1. Mở tab **Hóa đơn** → bảng trên hiển thị danh sách hóa đơn (STT, mã HD, ngày lập, nhân viên, khách hàng, tổng tiền). Footer hiển thị **tổng doanh thu** và **số hóa đơn** của kết quả hiện tại.
2. **Lọc:** Nhập SĐT khách hàng và/hoặc khoảng ngày (Từ ngày – Đến ngày, định dạng `dd/MM/yyyy`) → nhấn **Lọc** → hệ thống validate:
   - Định dạng ngày hợp lệ (không chấp nhận ngày sai như 32/13/2026)
   - "Từ ngày" phải trước hoặc bằng "Đến ngày"
   → Gọi `HoaDonDAO.loc()` với bộ lọc tương ứng.
3. **Tất cả:** Nhấn nút **Tất cả** → xóa bộ lọc → hiển thị toàn bộ hóa đơn (gọi `HoaDonDAO.layTatCa()`).
4. **Xem chi tiết:** Click vào một hóa đơn → bảng dưới hiển thị danh sách sản phẩm trong hóa đơn đó (tên SP, số lượng, đơn giá, thành tiền) từ bảng `chi_tiet_hoa_don`.

## 2.12. Luồng Thống kê

**Tác nhân:** Quản lý

1. Mở tab **Thống kê** → hệ thống tự động tải dữ liệu (auto-refresh mỗi lần chuyển sang tab):
   - **4 thẻ KPI:** Doanh thu hôm nay, số hóa đơn hôm nay, SP sắp hết hạn (≤ 7 ngày), SP hết hàng (tồn kho = 0). Các thẻ cảnh báo tự đổi màu: cam nếu có SP sắp hết hạn, đỏ nếu có SP hết hàng.
   - **Biểu đồ cột:** Doanh thu 7 ngày gần nhất, vẽ bằng `Graphics2D` với gradient xanh dương, nhãn ngày và giá trị trên đầu cột (rút gọn dạng "M" cho triệu VNĐ).
   - **Top 5 SP bán chạy:** Sắp xếp theo tổng số lượng bán.
2. **Lọc khoảng ngày:** Chọn Từ ngày – Đến ngày (JSpinner Date) → nhấn **Lọc theo khoảng** → cập nhật KPI, biểu đồ (nếu khoảng ≤ 31 ngày) và bảng Top 5 theo khoảng ngày tùy chọn. Validate: "Từ ngày" phải trước "Đến ngày".
3. **Làm mới:** Nhấn **Làm mới** → reset về dữ liệu mặc định (hôm nay / 7 ngày).
4. **Xuất CSV:** Nhấn **Xuất CSV** → chọn nơi lưu file → hệ thống ghi KPI + Top 5 ra file `.csv` với UTF-8 BOM, tương thích mở trực tiếp bằng Excel.

## 2.13. Luồng Đổi mật khẩu cá nhân

**Tác nhân:** Nhân viên

1. Nhân viên nhấn nút **"Đổi mật khẩu"** trên header → mở `DoiMatKhauDialog`.
2. Nhập mật khẩu cũ → mật khẩu mới → xác nhận mật khẩu mới.
3. Hệ thống validate:
   - Mật khẩu cũ đúng (so sánh với DB)
   - Mật khẩu mới ≥ 4 ký tự
   - Mật khẩu mới khác mật khẩu cũ
   - Mật khẩu mới khớp với ô xác nhận
4. Gọi `TaiKhoanDAO.doiMatKhau()` → `UPDATE tai_khoan SET mat_khau = ? WHERE id = ?` (PreparedStatement).
5. Thành công → hiện thông báo → đóng dialog.

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
- **4 thẻ KPI:** Doanh thu hôm nay / Số hóa đơn hôm nay / SP sắp hết hạn (≤ 7 ngày) / SP hết hàng. Thẻ cảnh báo tự đổi màu cam/đỏ.
- **Bộ lọc khoảng ngày:** Từ ngày – Đến ngày (JSpinner Date) + nút "Lọc theo khoảng" + nút "Làm mới" + nút "Xuất CSV"
- **Biểu đồ cột** doanh thu tự vẽ bằng `Graphics2D` với gradient và giá trị rút gọn ("M" cho triệu)
- **Bảng Top 5 sản phẩm** bán chạy

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

- Validate: tên không trống + không trùng, thương hiệu bắt buộc, đơn giá > 0, số lượng ≥ 0, HSD > NSX.
- Tô màu tồn kho: `= 0` đỏ (hết hàng), `< 5` cam (sắp hết).
- Lọc theo loại (ComboBox) + tìm theo tên (nút Tìm / Enter).
- Xóa SP có trong hóa đơn: cảnh báo rồi xóa cả chi tiết hóa đơn liên quan.
- Nhân viên: chỉ xem + tìm, ẩn nút CRUD và form nhập liệu.

## 5.4. Module Quản lý Khách hàng

- Validate: họ tên ≥ 2 ký tự chỉ chấp nhận chữ cái, SĐT 10 số bắt đầu bằng 0 + không trùng.
- Tô màu theo hạng thành viên (Bạc/Vàng/Kim cương).
- Điểm tích lũy: Quản lý sửa được, Nhân viên chỉ đọc.
- Xóa KH đã có hóa đơn → chuyển hóa đơn thành "Khách lẻ".
- Tìm kiếm theo tên hoặc SĐT (nút Tìm / Enter).

## 5.5. Module Quản lý Nhân sự

- Validate: họ tên ≥ 2 ký tự chỉ chứa chữ cái, tuổi ≥ 18, SĐT 10 số bắt đầu bằng 0 + không trùng, địa chỉ ≥ 10 ký tự.
- ComboBox "Tài khoản" chỉ hiện TK vai trò NV chưa liên kết.
- Tìm kiếm theo tên hoặc SĐT.

## 5.6. Module Hóa đơn

- Lọc theo khoảng ngày (validate định dạng `dd/MM/yyyy`, "Từ ngày" ≤ "Đến ngày"), SĐT khách hàng.
- Nút **Tất cả** reset bộ lọc.
- Footer tổng doanh thu + số hóa đơn.
- Click hóa đơn → bảng chi tiết phía dưới hiển thị danh sách SP.

## 5.7. Module Thống kê

- **4 KPI Cards:** Doanh thu hôm nay, số HD hôm nay, SP sắp hết hạn (≤ 7 ngày), SP hết hàng. Thẻ cảnh báo tự đổi màu (cam/đỏ).
- **Bộ lọc khoảng ngày:** JSpinner Date → "Lọc theo khoảng" → cập nhật KPI + biểu đồ (nếu ≤ 31 ngày) + Top 5. Validate "Từ ngày" trước "Đến ngày".
- **Biểu đồ cột:** Tự vẽ `Graphics2D` với gradient, nhãn ngày và giá trị rút gọn ("M" cho triệu VNĐ) trên đầu cột.
- **Top 5 SP bán chạy** + nút **Làm mới** (reset về mặc định).
- **Xuất CSV:** Xuất báo cáo doanh thu + Top 5 ra file `.csv` với UTF-8 BOM, tương thích Excel.

## 5.8. Module Tài khoản

- CRUD tài khoản, phân vai trò Quản lý / Nhân viên.
- Validate: tên đăng nhập ≥ 4 ký tự, không khoảng trắng, không trùng (`kiemTraTrungTenDN`); mật khẩu ≥ 6 ký tự.
- Xóa có kiểm tra liên kết NV/HĐ trong JDBC Transaction.
- Tìm kiếm theo tên đăng nhập.

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

