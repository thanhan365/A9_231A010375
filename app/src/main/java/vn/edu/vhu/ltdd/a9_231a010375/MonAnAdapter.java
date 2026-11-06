package vn.edu.vhu.ltdd.a9_231a010375;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Adapter tự viết: biến mỗi đối tượng MonAn thành một dòng trong ListView.
 *
 * BaseAdapter bắt buộc cài đặt 4 phương thức: getCount, getItem, getItemId, getView.
 * Trong đó getView là nơi mọi việc thực sự diễn ra.
 */
public class MonAnAdapter extends BaseAdapter {
    /** Giữ sẵn tham chiếu tới các View của một dòng để khỏi findViewById lại. */
    private static class ViewHolder {
        ImageView imgAnh;
        TextView tvTen, tvMoTa, tvGia;
    }

    private final Context context;
    private final List<MonAn> danhSach;
    private final LayoutInflater inflater;

    public MonAnAdapter(Context context, List<MonAn> danhSach) {
        this.context = context;
        this.danhSach = danhSach;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return danhSach.size(); // ListView hỏi: có bao nhiêu dòng?
    }

    @Override
    public Object getItem(int position) {
        return danhSach.get(position); // dữ liệu của dòng thứ position
    }

    @Override
    public long getItemId(int position) {
        return position; // chưa có id thật thì dùng vị trí
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            // Không có dòng cũ để tái sử dụng → phải "thổi" layout mới (tốn kém)
            convertView = inflater.inflate(R.layout.item_mon_an, parent, false);
            holder = new ViewHolder();
            holder.imgAnh = convertView.findViewById(R.id.imgAnh);
            holder.tvTen = convertView.findViewById(R.id.tvTen);
            holder.tvMoTa = convertView.findViewById(R.id.tvMoTa);
            holder.tvGia = convertView.findViewById(R.id.tvGia);
            convertView.setTag(holder); // gắn kèm vào dòng để lần sau lấy ra
        } else {
            // Dòng này vừa cuộn khỏi màn hình → dùng lại, chỉ cần đổi nội dung
            holder = (ViewHolder) convertView.getTag();
        }

        MonAn mon = danhSach.get(position);
        holder.imgAnh.setImageResource(mon.getAnhResId());
        holder.tvTen.setText(mon.getTen());
        holder.tvMoTa.setText(mon.getMoTa());
        holder.tvGia.setText(MonAn.dinhDangGia(mon.getGia()));

        // NC2: Dòng có giá trên 45.000 đ thì nền đổi màu nhạt
        if (mon.getGia() > 45000) {
            convertView.setBackgroundColor(Color.parseColor("#FFF3E0"));
        } else {
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }

        return convertView;
    }
}