package com.example.myapplication1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.util.prefs.Preferences;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText etUsername;
    private TextInputEditText etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        etPassword = findViewById(R.id.password);
        etUsername = findViewById(R.id.username);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnLogin = findViewById(R.id.Button1);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
            Button btnSign = findViewById(R.id.Button2);
        btnSign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this,SignActivity.class);
                    startActivity(intent);
                }

    });
  }

  public void login(){
      SharedPreferences prefs = getSharedPreferences("data", Context.MODE_PRIVATE);
      String password =etPassword.getText().toString();
      String username =etUsername.getText().toString();
      String get_name = prefs.getString("name","");
      String get_password = prefs.getString("password","");
      if(username.equals(get_name)&&password.equals(get_password)){
          Toast.makeText(MainActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
          Intent intent = new Intent(MainActivity.this,HomeActivity.class);
          startActivity(intent);
      }
      else{
          Toast.makeText(MainActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
      }
  }

}