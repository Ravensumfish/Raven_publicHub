package notebook.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.biji.R;

import notebook.utils.AppUtils;

public class HomeActivity extends AppCompatActivity {

    String username;
    Button btn_start;
    AppCompatButton btn_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initView();
        initEvent();
        initClick();

    }

    private void initEvent() {
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        Log.d("TAG","(username:)-->>" + username);
    }

    private void initView() {
        btn_start = findViewById(R.id.btn_home_start);
        btn_settings = findViewById(R.id.btn_home_settings);
    }

    private void initClick() {
        startNote();
        settings();

    }

    private void settings() {
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.startActivity(HomeActivity.this,UserActivity.class,username);
            }
        });
    }

    private void startNote() {
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.startActivity(HomeActivity.this,NoteActivity.class,username);
            }
        });
    }


}