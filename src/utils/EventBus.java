package utils;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * EventBus đơn giản cho kiến trúc xử lý thông điệp (Publish/Subscribe).
 * Cho phép các module giao tiếp lỏng lẻo thông qua sự kiện mà không cần
 * tham chiếu trực tiếp lẫn nhau.
 *
 * <p>Sử dụng Generic ({@code Map<String, List<Consumer<Object>>>}),
 * Collections ({@code HashMap}, {@code CopyOnWriteArrayList}),
 * và Functional Interface ({@code Consumer<T>}).</p>
 *
 * <pre>
 * // Subscriber (đăng ký lắng nghe):
 * EventBus.subscribe("THANH_TOAN", data -> capNhatThongKe());
 *
 * // Publisher (phát sự kiện):
 * EventBus.publish("THANH_TOAN", idHoaDon);
 * </pre>
 *
 * @author Phạm Duy An - BIT240002
 */
public class EventBus {

    /**
     * Bản đồ lưu trữ bên lắng nghe: tên sự kiện → danh sách hàm xử lý.
     * Sử dụng CopyOnWriteArrayList để an toàn đa luồng khi phát/đăng ký
     * từ nhiều luồng khác nhau (EDT + luồng nền).
     */
    private static final Map<String, List<Consumer<Object>>> subscribers
            = new HashMap<>();

    // ===== HẰNG SỐ TÊN SỰ KIỆN =====

    /** Sự kiện phát sau khi thanh toán thành công. */
    public static final String SU_KIEN_THANH_TOAN = "THANH_TOAN";

    /** Sự kiện phát khi dữ liệu sản phẩm thay đổi (thêm/sửa/xóa/trừ kho). */
    public static final String SU_KIEN_CAP_NHAT_SAN_PHAM = "CAP_NHAT_SAN_PHAM";

    /** Sự kiện phát khi dữ liệu khách hàng thay đổi. */
    public static final String SU_KIEN_CAP_NHAT_KHACH_HANG = "CAP_NHAT_KHACH_HANG";

    /**
     * Đăng ký lắng nghe một sự kiện (Subscribe).
     *
     * @param event   Tên sự kiện (sử dụng hằng số {@code SU_KIEN_*})
     * @param handler Giao diện hàm {@code Consumer<Object>} xử lý sự kiện
     */
    public static void subscribe(String event, Consumer<Object> handler) {
        subscribers
                .computeIfAbsent(event, k -> new CopyOnWriteArrayList<>())
                .add(handler);
    }

    /**
     * Phát sự kiện cho tất cả bên lắng nghe đã đăng ký.
     * Sử dụng {@code Optional} + {@code Stream.forEach} (lập trình hàm).
     *
     * @param event Tên sự kiện
     * @param data  Dữ liệu gửi kèm (có thể null)
     */
    public static void publish(String event, Object data) {
        Optional.ofNullable(subscribers.get(event))
                .ifPresent(handlers ->
                        handlers.forEach(handler -> handler.accept(data))
                );
    }

    /**
     * Hủy tất cả bên lắng nghe của một sự kiện.
     *
     * @param event Tên sự kiện cần hủy
     */
    public static void unsubscribe(String event) {
        subscribers.remove(event);
    }

    /**
     * Hủy toàn bộ bên lắng nghe (dùng khi khởi tạo lại ứng dụng).
     */
    public static void clearAll() {
        subscribers.clear();
    }
}
