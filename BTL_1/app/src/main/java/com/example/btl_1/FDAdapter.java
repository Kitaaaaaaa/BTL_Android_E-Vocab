package com.example.btl_1;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class FDAdapter  extends ArrayAdapter<FolderItem> {
    Context context;
    int layout;
    ArrayList<FolderItem> myFDItem;
    DBManager dbManager;
    public FDAdapter(@NonNull Context context, int resource, ArrayList<FolderItem> myFDItem) {
        super(context, resource, myFDItem);
        this.context = context;
        this.layout = resource;
        this.myFDItem = myFDItem;
        this.dbManager = new DBManager(context); // Khởi tạo dbManager
    }


    @Override
    public int getCount() {
        return myFDItem.size();
    }

    @Nullable
    @Override
    public FolderItem getItem(int position) {
        return myFDItem.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View currentView = convertView;

        // Kiểm tra nếu convertView là null, thì tạo view mới từ layout
        if (currentView == null) {
            currentView = LayoutInflater.from(context).inflate(layout, parent, false);
        }

        FolderItem folder = getItem(position);
        TextView tvName = currentView.findViewById(R.id.namefd);
        ImageView menuButton = currentView.findViewById(R.id.menufd);

        // Set tên thư mục
        if (tvName != null && folder != null) {
            tvName.setText(folder.getName());
        }

        // Kiểm tra nếu menuButton không null
        if (menuButton != null) {
            // Xử lý sự kiện nhấn nút menu
            menuButton.setOnClickListener(view -> {
                // Tạo PopupMenu
                PopupMenu popupMenu = new PopupMenu(context, menuButton);

                // Inflate menu từ folder_menu.xml
                popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());

                // Đặt Listener cho các mục menu
                popupMenu.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.edit_folder) {
                        editFolder(position);
                        return true;
                    } else if (item.getItemId() == R.id.delete_folder) {
                        deleteFolder(position);
                        return true;
                    } else {
                        return false;
                    }
                });


                // Hiển thị menu
                popupMenu.show();
            });

        }

        return currentView;
    }



    private void editFolder(int position) {
        FolderItem folder = myFDItem.get(position);

        // Hiển thị Dialog hoặc Activity để nhập tên mới
        EditText editText = new EditText(context);
        editText.setText(folder.getName()); // Tên cũ
        new AlertDialog.Builder(context)
                .setTitle("Sửa thư mục")
                .setView(editText)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String newName = editText.getText().toString().trim();
                    if (!newName.isEmpty()) {
                        folder.setName(newName);
                        int result = dbManager.updFolder(folder, folder.getId());
                        if (result > 0) {
                            Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged(); // Cập nhật giao diện
                        } else {
                            Toast.makeText(context, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteFolder(int position) {
        FolderItem folder = myFDItem.get(position);

        new AlertDialog.Builder(context)
                .setTitle("Xóa thư mục")
                .setMessage("Bạn có chắc chắn muốn xóa thư mục này không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    dbManager.open();  // Mở cơ sở dữ liệu trước khi xóa

                    int result = dbManager.delFolder(folder.getId());
                    if (result > 0) {
                        myFDItem.remove(position); // Xóa khỏi danh sách
                        notifyDataSetChanged(); // Cập nhật giao diện
                        Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                    }

                    dbManager.close();  // Đóng cơ sở dữ liệu sau khi xóa
                })
                .setNegativeButton("Hủy", null)
                .show();
    }


}
