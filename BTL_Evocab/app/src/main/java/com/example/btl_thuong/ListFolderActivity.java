package com.example.btl_thuong;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ListFolderActivity extends AppCompatActivity {
    ListView lvFolder;
    ImageButton btnMenu, btnAdd;

    ArrayList<FolderItem> myListFolder = new ArrayList<>();
    FolderAdapter fdAdapter;
    DBManager dbManager;
    MenuManager menuManager;

    User currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_folder);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getViews();


        dbManager = new DBManager(this);
        dbManager.open();

        receiveIntent();


        lvFolder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FolderItem select = myListFolder.get(position);

                Intent i = new Intent(ListFolderActivity.this, ListVocabActivity.class);
                i.putExtra("folder", select);
                startActivity(i);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("DEBUG", "Button Add clicked");
                addFolder();
            }
        });
        menuManager = new MenuManager();
        menuManager.setUpMenuFolder(this);
    }

    private void getViews() {
        lvFolder = findViewById(R.id.lvFolder);
        btnMenu = findViewById(R.id.btn_lstFolder_menu);
        btnAdd = findViewById(R.id.btnAddFolder);
    }

    private void receiveIntent(){
//        Intent i = getIntent();
//        if(i!=null && i.hasExtra("User")){
//            currentUser=(User) i.getSerializableExtra("User");
//            if (currentUser != null) {
//                myListFolder = dbManager.getAllFolder(currentUser.getUserID());
//                fdAdapter = new FolderAdapter(ListFolderActivity.this, R.layout.item_folder, myListFolder);
//                lvFolder.setAdapter(fdAdapter);
//            }else {
//                Toast.makeText(this, "No information found for this user!", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        }else {
//            Toast.makeText(this, "No data found for this user!", Toast.LENGTH_SHORT).show();
//            finish();
//        }
        SharedPreferences prefs = getSharedPreferences("UserPref", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
        int userID = prefs.getInt("userID", -1);
        if (isLoggedIn && userID != -1) {
            currentUser = dbManager.getUserById(userID);
            if (currentUser != null) {
                myListFolder=dbManager.getAllFolder(currentUser.getUserID());
                fdAdapter=new FolderAdapter(ListFolderActivity.this, R.layout.item_folder, myListFolder);
                lvFolder.setAdapter(fdAdapter);
            }else {
                Toast.makeText(this, "User information not found!", Toast.LENGTH_SHORT).show();
                logoutAndRedirectToLogin();
            }
        }else {
            logoutAndRedirectToLogin();
        }
    }
    private void logoutAndRedirectToLogin() {
        SharedPreferences prefs = getSharedPreferences("UserPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear(); // Xóa thông tin đăng nhập
        editor.apply();

        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
        finish();
    }


    public void addFolder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.add_folder, null);
        builder.setView(dialogView);

        EditText edtFolderName = dialogView.findViewById(R.id.edtFolderName);
        Button btnSave = dialogView.findViewById(R.id.btnSaveFD);
        Button btnCancel = dialogView.findViewById(R.id.btnCancelFD);

        AlertDialog dialog = builder.create();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fName = edtFolderName.getText().toString();

                if (!fName.isEmpty()) {
//                    if (currentUser != null) {
                        FolderItem newFolder = new FolderItem(0, fName, currentUser.getUserID());

                        //Them folder vao DB
                        long result = dbManager.insFolder(newFolder);
                        if (result != -1) {
                            myListFolder.clear();
                            myListFolder.addAll(dbManager.getAllFolder(currentUser.getUserID()));
                            fdAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                            Toast.makeText(ListFolderActivity.this, "Add new folder successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ListFolderActivity.this, "Add new folder failed!", Toast.LENGTH_SHORT).show();

                        }
//                    }
                }
            }
        });
        dialog.show();
    }
}













