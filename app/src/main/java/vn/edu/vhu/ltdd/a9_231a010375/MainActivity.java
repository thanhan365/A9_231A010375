package vn.edu.vhu.ltdd.a9_231a010375;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "A9_231A010375";
    private final List<MonAn> danhSach = new ArrayList<>();
    private MonAnAdapter adapter;
    private ListView lvMonAn;
    private TextView tvTrong, tvThongKe;
    private EditText edtTen, edtGia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        lvMonAn = findViewById(R.id.lvMonAn);
        tvTrong = findViewById(R.id.tvTrong);
        tvThongKe = findViewById(R.id.tvThongKe);
        edtTen = findViewById(R.id.edtTen);
        edtGia = findViewById(R.id.edtGia);
        Button btnThem = findViewById(R.id.btnThem);

        taoDuLieuMau();
        capNhatThongKe();

        // ---- Bước then chốt: gắn adapter vào ListView ----
        adapter = new MonAnAdapter(this, danhSach);
        lvMonAn.setAdapter(adapter);

        // ListView có sẵn cơ chế hiện View thay thế khi danh sách rỗng
        lvMonAn.setEmptyView(tvTrong);

        // Bấm một dòng
        lvMonAn.setOnItemClickListener((parent, view, position, id) -> {
            MonAn mon = (MonAn) parent.getItemAtPosition(position);
            Toast.makeText(this,
                    getString(R.string.chon_mon, mon.getTen()), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Chọn dòng " + position + ": " + mon.getTen());
        });

        // Giữ lâu một dòng để xóa. Lab A11 sẽ thay bằng context menu + hộp thoại xác nhận.
        lvMonAn.setOnItemLongClickListener((parent, view, position, id) -> {
            MonAn mon = danhSach.remove(position);
            adapter.notifyDataSetChanged(); // BÁO cho adapter biết dữ liệu đã đổi
            capNhatThongKe(); // NC4: Cập nhật thống kê sau khi xóa
            Toast.makeText(this,
                    getString(R.string.da_xoa, mon.getTen()), Toast.LENGTH_SHORT).show();
            return true; // true = đã xử lý, đừng gọi tiếp onItemClick
        });

        btnThem.setOnClickListener(v -> themMon());
    }

    private void taoDuLieuMau() {
        danhSach.add(new MonAn("Phở bò tái", "Nước dùng ninh xương 8 giờ", 45000, R.drawable.mon1));
        danhSach.add(new MonAn("Bún chả Hà Nội", "Chả nướng than hoa", 50000, R.drawable.mon2));
        danhSach.add(new MonAn("Cơm tấm sườn", "Sườn nướng mật ong", 40000, R.drawable.mon3));
        danhSach.add(new MonAn("Bánh mì thịt nguội", "Pate nhà làm", 25000, R.drawable.mon4));
        danhSach.add(new MonAn("Trà đào cam sả", "Mát lạnh, ít đường", 30000, R.drawable.mon5));
    }

    /** Đọc dữ liệu từ hai ô nhập, kiểm tra hợp lệ rồi thêm vào danh sách. */
    private void themMon() {
        String ten = edtTen.getText().toString().trim();
        String giaStr = edtGia.getText().toString().trim();

        if (ten.isEmpty()) {
            edtTen.setError(getString(R.string.err_ten));
            edtTen.requestFocus();
            return;
        }

        int gia;
        try {
            gia = Integer.parseInt(giaStr);
        } catch (NumberFormatException e) {
            edtGia.setError(getString(R.string.err_gia));
            edtGia.requestFocus();
            return;
        }

        if (gia <= 0) {
            edtGia.setError(getString(R.string.err_gia_duong));
            edtGia.requestFocus();
            return;
        }

        danhSach.add(0, new MonAn(ten, getString(R.string.moi_them), gia, R.drawable.mon1));
        adapter.notifyDataSetChanged();
        capNhatThongKe(); // NC4: Cập nhật thống kê sau khi thêm
        lvMonAn.smoothScrollToPosition(0); // cuộn lên để người dùng thấy món vừa thêm
        edtTen.setText("");
        edtGia.setText("");
        edtTen.requestFocus();
        Log.d(TAG, "Đã thêm: " + ten + " — tổng " + danhSach.size() + " món");
    }

    /** NC4: Tính và cập nhật tổng số món cùng tổng giá trị thực đơn. */
    private void capNhatThongKe() {
        int tongMon = danhSach.size();
        int tongTien = 0;
        for (MonAn mon : danhSach) {
            tongTien += mon.getGia();
        }
        tvThongKe.setText(getString(R.string.thong_ke, tongMon, MonAn.dinhDangGia(tongTien)));
    }
}