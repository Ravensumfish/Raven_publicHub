/**description: 注册界面功能
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/2/3
 */
package notebook.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.biji.R;
import com.google.android.material.textfield.TextInputEditText;

import notebook.entity.User;
import notebook.sql.UserDB;

public class SignActivity extends AppCompatActivity {
    private Button btn;
    private TextInputEditText etUsername;
    private TextInputEditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.sign), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        UserDB mySQLiteOpenHelper = new UserDB(this);
        initView();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String mPassword = etPassword.getText().toString();
               String mUsername = etUsername.getText().toString();
               User user = new User(mUsername, mPassword);
               if (checkChar(mPassword)) {
                   Log.d("TAG","(字符认证通过:)-->>");
                   long result = mySQLiteOpenHelper.sign(user);
                   if (result != -1) {
                       Log.d("TAG", "(注册:成功)-->>");
                       Toast.makeText(SignActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                       finish();
                   } else {
                       Log.d("TAG","(result:" + result + ")-->>");
                       Toast.makeText(SignActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                   }
               } else {
                   Toast.makeText(SignActivity.this, "注册失败，请检查密码是否符合规范", Toast.LENGTH_SHORT).show();
               }
            }
        });
    }
    public void initView() {

        btn = findViewById(R.id.button_sign);
        etUsername = findViewById(R.id.uname_sign);
        etPassword = findViewById(R.id.password_sign);

    }

    //字符检测
    public boolean checkChar(String s){
        if (s.length() >= 4 && s.length() <= 8) {
           return s.matches("[a-zA-Z0-9]+");
        }
        return false;
    }

}