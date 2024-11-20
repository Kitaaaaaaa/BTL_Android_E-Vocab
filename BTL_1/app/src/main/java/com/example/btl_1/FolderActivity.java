package com.example.btl_1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class FolderActivity extends AppCompatActivity {
    ListView lvFolder;
    ArrayList<FolderItem> listFolder = new ArrayList<>();
    FDAdapter fdAdapter;
    DBManageger dbManageger;

    LinearLayout menuLayout;
    ImageButton menuButton;

    ImageButton menuMenu;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_folder);


        lvFolder = findViewById(R.id.lvFolder);
        dbManageger = new DBManageger(this);
        dbManageger.open();

        listFolder = dbManageger.getAllFolder();

        fdAdapter = new FDAdapter(FolderActivity.this, R.layout.activity_folder_item, listFolder);
        lvFolder.setAdapter(fdAdapter);

        lvFolder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FolderItem selectedFolder = listFolder.get(position);

                // Truyền đối tượng FolderItem thay vì chỉ id
                Intent intent = new Intent(FolderActivity.this, VocabActivity.class);
                intent.putExtra("folder", selectedFolder); // Truyền FolderItem vào Intent
                startActivity(intent);
            }
        });

        ImageButton btnAddFD = findViewById(R.id.bt_add_folder);
        btnAddFD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFolder();
            }
        });

        menuButton = findViewById(R.id.menu);
        menuLayout = findViewById(R.id.menu_layout);
        menuMenu = findViewById(R.id.bt_menu);


        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuLayout.getVisibility() == View.GONE) {
                    // Nếu menu đang ẩn, trượt menu vào
                    menuLayout.setVisibility(View.VISIBLE);  // Đảm bảo menu được hiển thị
                    menuLayout.animate().translationX(0).setDuration(300);  // Trượt vào từ ngoài màn hình
                } else {
                    // Nếu menu đang hiển thị, trượt menu ra ngoài
                    menuLayout.animate().translationX(-menuLayout.getWidth()).setDuration(300)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    menuLayout.setVisibility(View.GONE);  // Ẩn menu khi trượt xong
                                }
                            });
                }
            }
        });
        // Sự kiện click cho menu layout trượt vào
        menuMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Trượt menu layout vào màn hình khi nhấn vào menu trong layout
                if (menuLayout.getVisibility() == View.GONE) {
                    menuLayout.setVisibility(View.VISIBLE);  // Đảm bảo menu được hiển thị
                    menuLayout.animate().translationX(0).setDuration(300);  // Trượt vào
                } else {
                    // Trượt menu layout ra ngoài nếu đang hiển thị
                    menuLayout.animate().translationX(-menuLayout.getWidth()).setDuration(300)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    menuLayout.setVisibility(View.GONE);  // Ẩn menu sau khi trượt ra ngoài
                                }
                            });
                }
            }
        });

    }

    private void addFolder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.add_folder, null);
        builder.setView(dialogView);

        EditText editTextFolderName = dialogView.findViewById(R.id.tv_add_Terminology);
        Spinner spinnerType = dialogView.findViewById(R.id.sp_add_type);
        Button buttonSave = dialogView.findViewById(R.id.bt_add_add);
        Button buttonCancel = dialogView.findViewById(R.id.bt_add_Cancel);

        AlertDialog dialog = builder.create();

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String folderName = editTextFolderName.getText().toString().trim();

                if (!folderName.isEmpty()) {
                    FolderItem newFolder = new FolderItem(folderName);
                    // Thêm thư mục vào DB
                    long result = dbManageger.insFolder(newFolder);

                    if (result != - 1) {
                        listFolder.clear();
                        listFolder.addAll(dbManageger.getAllFolder());
                        fdAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                        Toast.makeText(FolderActivity.this, "Thêm thư mục thành cồng", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(FolderActivity.this, "Lỗi khi thêm thư mục", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(FolderActivity.this, "Hãy nhập tên thư mục", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

}
