package com.example.btl_thuong;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    ImageButton btnMenu, btnAdd, btnSearch;
    EditText edtSearch;

    ArrayList<FolderItem> myListFolder = new ArrayList<>();
    ArrayList<FolderItem> searchedFolders = new ArrayList<>();

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
                FolderItem select = searchedFolders.get(position);

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

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFolder();
            }
        });
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int after) {
                searchFolder();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        menuManager = new MenuManager();
        menuManager.setUpMenuFolder(this);
    }

    @Override
    protected void onRestart() {
        menuManager = new MenuManager();
        menuManager.setUpMenuFolder(this);
        super.onRestart();
    }

    private void getViews() {
        lvFolder = findViewById(R.id.lvFolder);
        btnMenu = findViewById(R.id.btn_lstFolder_menu);
        btnAdd = findViewById(R.id.btnAddFolder);
        edtSearch = findViewById(R.id.edt_folder_search);
        btnSearch = findViewById(R.id.btn_folder_search);

    }

    private void receiveIntent() {
        SharedPreferences prefs = getSharedPreferences("UserPref", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
        int userID = prefs.getInt("userID", -1);
        if (isLoggedIn && userID != -1) {
            currentUser = dbManager.getUserById(userID);
            if (currentUser != null) {
                myListFolder = dbManager.getAllFolder(currentUser.getUserID());
                searchedFolders.addAll(myListFolder);
                fdAdapter = new FolderAdapter(ListFolderActivity.this, R.layout.item_folder, myListFolder);
                lvFolder.setAdapter(fdAdapter);
            } else {
                Toast.makeText(this, "User information not found!", Toast.LENGTH_SHORT).show();
                logoutAndRedirectToLogin();
            }
        } else {
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

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setDimAmount(0f);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);

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


    private void searchFolder() {
        String searchText = edtSearch.getText().toString().trim();
        if (!searchText.isEmpty()) {
            searchedFolders.clear();

            for(FolderItem folderItem:myListFolder){
                if(folderItem.getFolderName().toLowerCase().contains(searchText)){
                    searchedFolders.add(folderItem);
                    fdAdapter = new FolderAdapter(ListFolderActivity.this, R.layout.item_folder, searchedFolders);
                    lvFolder.setAdapter(fdAdapter);
                }
            }
        }else {
            searchedFolders.clear();
            searchedFolders.addAll(myListFolder);
            fdAdapter = new FolderAdapter(ListFolderActivity.this, R.layout.item_folder, searchedFolders);
            lvFolder.setAdapter(fdAdapter);
        }
        fdAdapter.notifyDataSetChanged();
    }
}













