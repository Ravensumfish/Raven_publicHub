/**description: 登录界面功能
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/2/3
 */

package notebook.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.biji.R;
import com.google.android.material.textfield.TextInputEditText;
import notebook.sql.UserDB;
import notebook.utils.AppUtils;

public class LoginActivity extends AppCompatActivity {

    private Button btn;
    private TextInputEditText etUsername, etPassword;

    private CheckBox cb_auto_login, cb_remember;

    private String mUsername, mPassword;

    private TextView tv_tip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initView();
        checkBoxFunction2();

        UserDB mySQLiteOpenHelper = new UserDB(this);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initVariable();
                if (mySQLiteOpenHelper.login(mUsername, mPassword)) {
                    //登录成功
                    Log.d("TAG","登录成功-->>");
                    //判断checkBox
                    checkBoxFunction1();
                    //跳转主界面
                    AppUtils.startActivity(LoginActivity.this, NoteActivity.class, mUsername);
                } else {
                    //失败则弹窗提示
                    Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tv_tip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.startActivity(LoginActivity.this, SignActivity.class);
            }
        });
    }

    public void initView() {

        btn = findViewById(R.id.button_login);
        etUsername = findViewById(R.id.uname_login);
        etPassword = findViewById(R.id.password_login);
        tv_tip = findViewById(R.id.tip_sign);
        cb_auto_login = findViewById(R.id.cb_auto_login);
        cb_remember = findViewById(R.id.cb_remember);

    }

    private void initVariable() {
        mUsername = etUsername.getText().toString();
        mPassword = etPassword.getText().toString();
        Log.d("TAG","(mUsername:)-->>"+mUsername);
        Log.d("TAG","(mPassword:)-->>"+mPassword);
    }

    private void checkBoxFunction1() {
        SharedPreferences sp = getSharedPreferences("spData", MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        if (cb_remember.isChecked()) {
            edit.putString("password", mPassword);
            edit.putBoolean("isRemember", true);
            Log.d("TAG","(已记住密码)-->>");
        } else {
            edit.putBoolean("isRemember", false);
        }

        if (cb_auto_login.isChecked()) {
            edit.putString("username", mUsername);
            edit.putString("password", mPassword);
            edit.putBoolean("isAutoLogin", true);
            Log.d("TAG","(已进入自动登录状态)-->>");
        } else {
            edit.putBoolean("isAutoLogin", false);
        }

        edit.apply();
    }

    private void checkBoxFunction2() {
        SharedPreferences sp = getSharedPreferences("spData", MODE_PRIVATE);

        if (sp.getBoolean("isAutoLogin", false)) {
            String username = sp.getString("username", null);
            cb_auto_login.setChecked(true);
            Log.d("TAG","(自动登录成功)-->>"+"username: "+username);
            //AppUtils.startActivity(LoginActivity.this, NoteActivity.class, username);
        }

        if (sp.getBoolean("isRemember", false)) {
            String password =sp.getString("password", null);
            String username =sp.getString("username", null);
            etUsername.setText(username);
            etPassword.setText(password);
            cb_remember.setChecked(true);
            Log.d("TAG","(记住密码成功)-->>"+"password: "+ password);

        }

    }

}