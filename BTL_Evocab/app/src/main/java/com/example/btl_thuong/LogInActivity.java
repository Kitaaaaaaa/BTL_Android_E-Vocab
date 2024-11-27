package com.example.btl_thuong;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LogInActivity extends AppCompatActivity {
    EditText edt_login_username, edt_login_pass;
    Button btn_login_login;
    TextView tv_login_register;

    DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getViews();

        dbManager = new DBManager(this);
        dbManager.open();

        btn_login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        tv_login_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LogInActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void getViews() {
        edt_login_username = findViewById(R.id.edt_login_username);
        edt_login_pass = findViewById(R.id.edt_login_pass);
        btn_login_login = findViewById(R.id.btn_login_login);
        tv_login_register = findViewById(R.id.tv_login_register);
    }

    private void login() {
        String username = edt_login_username.getText().toString().trim();
        String pass = edt_login_pass.getText().toString().trim();
        if (username.isEmpty() || pass.isEmpty()) {
            Toast.makeText(LogInActivity.this, "Please enter all information!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbManager.isValidUser(username, pass)) {
            User user = dbManager.getUser(username);

            //Luu trang thai dang nhap
            SharedPreferences prefs = getSharedPreferences("UserPref", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("userID", user.getUserID());
            editor.putBoolean("isLoggedIn", true);
            editor.apply();

            Intent i = new Intent(this, ListFolderActivity.class);
            i.putExtra("User", user);
            startActivity(i);
            finish();
        } else {
            Toast.makeText(this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
        }
    }
}