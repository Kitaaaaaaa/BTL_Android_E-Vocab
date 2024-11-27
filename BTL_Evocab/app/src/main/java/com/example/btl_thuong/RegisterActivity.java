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

public class RegisterActivity extends AppCompatActivity {
    EditText edt_register_username, edt_register_phoneNumber,
            edt_register_password, edt_register_rePass;
    Button btn_register_register;
    TextView tv_register_login;

    DBManager dbManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getViews();

        dbManager=new DBManager(this);
        dbManager.open();

        btn_register_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        tv_register_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LogInActivity.class);
                startActivity(i);
            }
        });
    }

    private void getViews() {
        edt_register_username = findViewById(R.id.edt_register_username);
        edt_register_phoneNumber = findViewById(R.id.edt_register_phoneNumber);
        edt_register_password = findViewById(R.id.edt_register_pass);
        edt_register_rePass = findViewById(R.id.edt_register_rePass);
        btn_register_register = findViewById(R.id.btn_register_register);
        tv_register_login = findViewById(R.id.tv_register_login);
    }

    private void register(){
        String username = edt_register_username.getText().toString().trim();
        String phone = edt_register_phoneNumber.getText().toString().trim();
        String pass = edt_register_password.getText().toString().trim();
        String rePass = edt_register_rePass.getText().toString().trim();
        String warning ="";
        if (username.isEmpty() || phone.isEmpty() || pass.isEmpty() || rePass.isEmpty()){
            warning+="Please enter all information!";
        }
        if(dbManager.isUserNameExists(username)){
            warning+="\nUsername already exists!";
        }
        if(phone.length() != 10 || phone.charAt(0) != '0'){
            warning+="\nPhone number must start with 0 and have 10 digits!";
        }
        if(!pass.equals(rePass)){
            warning+="\nPasswords do not match!";
        }

        if (!warning.isEmpty()) {
            Toast.makeText(RegisterActivity.this, warning, Toast.LENGTH_SHORT).show();
            return;
        }

        User user=new User(0, username, phone, pass);
        long result = dbManager.insertUser(user);
        if (result != -1) {
            Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
            // Lưu trạng thái đăng nhập vào SharedPreferences
            SharedPreferences prefs = getSharedPreferences("UserPref", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("userID", (int) result); // Giả sử result là ID người dùng mới
            editor.putBoolean("isLoggedIn", true);
            editor.apply();

            Intent i = new Intent(RegisterActivity.this, ListFolderActivity.class);
            startActivity(i);
//            Intent i = new Intent(RegisterActivity.this, LogInActivity.class);
//            startActivity(i);
        }else {
            Toast.makeText(RegisterActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
        }
    }

}