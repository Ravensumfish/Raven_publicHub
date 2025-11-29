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

public class SignActivity extends AppCompatActivity {

    private TextInputEditText etUsername;
    private TextInputEditText etPassword;
    private Button btnSign;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign);
        etPassword = findViewById(R.id.password);
        etUsername = findViewById(R.id.username);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnSign = findViewById(R.id.Button3);
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign();
            }
        });

        Button goback =findViewById(R.id.Button4);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(SignActivity.this, MainActivity.class);
                startActivity(intent1);
            }
        });

    }

    public void sign() {
        SharedPreferences.Editor editor = getSharedPreferences("data", Context.MODE_PRIVATE).edit();
        String password = etPassword.getText().toString();
        String username = etUsername.getText().toString();
        if (password.isEmpty()|| username.isEmpty() || password.length()>6) {
            Toast.makeText(SignActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
        }
        else {
            editor.putString("name", username);
            editor.putString("password", password);
            editor.apply();
            Toast.makeText(SignActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignActivity.this, MainActivity.class);
            startActivity(intent);
        }

    }
}