/**
 * description: 个人主页（设置）
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/2/13
 */
package notebook.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.biji.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import notebook.utils.SPUtils;

public class UserActivity extends AppCompatActivity {

    String username;
    TextView introduce,id;
    Button btn_edit;
    TextView mUsername;
    ImageView avatar;
    Button btn_back;
    ActivityResultLauncher resultLauncher = permit();
    ActivityResultLauncher<String> selectImage = imageSelect();
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });
    }

    //从相册中选取头像
    private void choosePhoto() {
        String permission;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permission = Manifest.permission.READ_MEDIA_IMAGES;
        } else{
            permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        }
            //判断权限
            if (ContextCompat.checkSelfPermission(UserActivity.this, permission) == PackageManager.PERMISSION_GRANTED) {
                selectImage.launch("image/*");


            } else {
                resultLauncher.launch(permission);

            }
    }


    private void initView() {
        mUsername = findViewById(R.id.user_username);
        btn_back = findViewById(R.id.btn_user_back);
        avatar = findViewById(R.id.avatar);
        introduce = findViewById(R.id.introduce);
    }

    private void initData() {
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        mUsername.setText(username);
        sp = SPUtils.getSpData(UserActivity.this);
        String uri = sp.getString("avatar_uri", null);
        if (uri != null) {
            avatar.setImageURI(Uri.parse(uri));
        }
    }

    //选择图片回调
    private ActivityResultLauncher<String> imageSelect() {
        return registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri o) {
                        if (o != null) {
                            avatar.setImageURI(o);
                            String avatarPath;
                            try {
                                avatarPath = copyToMyFile(o);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            SPUtils.edit(sp, "avatar_uri", avatarPath);
                        } else {
                            Log.d("TAG", "(图片:设置失败)-->>URI:null");
                        }
                    }
                });

    }

    //相册权限是暂时的，需要及时把用户选择的图片保存下来放入app内部存储，否则原路径失效，页面会崩
    private String copyToMyFile(Uri uri) throws IOException {
        File filesDir = getFilesDir();
        File imageFile = new File(filesDir, "my_avatar.jpg");
        try (FileOutputStream outputStream = new FileOutputStream(imageFile);
             InputStream inputStream = getContentResolver().openInputStream(uri)) {

            byte[] buffer = new byte[1024];

            if (inputStream != null) {
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile.getAbsolutePath();
    }


    //权限请求回调
    private ActivityResultLauncher permit() {

        return registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                isGranted ->
                {
                    if (isGranted) {
                        Log.d("TAG", "(相机权限:)-->>" + "获取成功");
                    } else {
                        Toast.makeText(this, "无权限访问相册", Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "(相机权限:)-->>" + "获取失败");
                    }
                });
    }


}