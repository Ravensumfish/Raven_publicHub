/**
 * description: 个人主页（设置）
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/2/13
 */
package notebook.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import com.example.biji.databinding.DialogEditIntroduceBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import notebook.entity.User;
import notebook.sql.UserDB;
import notebook.utils.AppUtils;
import notebook.utils.SPUtils;

public class UserActivity extends AppCompatActivity {

    String username;
    int userId;
    User user;
    UserDB userDB;
    TextView tvIntroduce, tvId;
    TextView tvUsername;
    ImageView avatar;
    Button btn_back;
    ActivityResultLauncher resultLauncher = permit();
    ActivityResultLauncher<String> selectImage = imageSelect();
    SharedPreferences mSp;
    DialogEditIntroduceBinding dialogBinding;
    String mIntroduce;


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
        tvIntroduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog();
            }
        });
    }

    private void showEditDialog() {
        Dialog dialog = new Dialog(UserActivity.this);
        dialogBinding = DialogEditIntroduceBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            window.setGravity(Gravity.CENTER);
        }
        dialog.show();

        if (!AppUtils.isEmpty(mIntroduce)) {
            dialogBinding.etDialogIntroduce.getEditText().setText(mIntroduce);
        }
        dialogBinding.btnDialogCancel.setOnClickListener(view -> dialog.dismiss());
        dialogBinding.btnDialogConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AppUtils.isEmpty(dialogBinding.etDialogIntroduce.getEditText().toString())) {
                    mIntroduce = dialogBinding.etDialogIntroduce.getEditText().getText().toString();
                    SPUtils.editString(mSp,"introduce_" + userId ,mIntroduce);
                    tvIntroduce.setText(mIntroduce);
                }
                dialog.dismiss();
            }
        });

    }

    //从相册中选取头像
    private void choosePhoto() {
        String permission;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permission = Manifest.permission.READ_MEDIA_IMAGES;
        } else {
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
        tvUsername = findViewById(R.id.user_username);
        btn_back = findViewById(R.id.btn_user_back);
        avatar = findViewById(R.id.avatar);
        tvIntroduce = findViewById(R.id.tv_user_introduce);
        tvId = findViewById(R.id.tv_user_id);
    }

    private void initData() {
        userDB = new UserDB(this);
        Intent intent = getIntent();
        userId = intent.getIntExtra("user_id",-1);
        user = userDB.getUserById(userId);
        username = user.getName();
        tvUsername.setText(username);
        tvId.setText(String.valueOf(userId));
        mSp = SPUtils.getSpData(UserActivity.this);
        String uri = mSp.getString("avatar_uri_" + userId, null);
        if (uri != null) {
            avatar.setImageURI(Uri.parse(uri));
        }
        mIntroduce = mSp.getString("introduce_" + userId ,null);
        if (!AppUtils.isEmpty(mIntroduce)) {
            tvIntroduce.setText(mIntroduce);
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
                            SPUtils.editString(mSp, "avatar_uri_" + userId, avatarPath);
                        } else {
                            Log.d("TAG", "(图片:设置失败)-->>URI:null");
                        }
                    }
                });

    }

    //相册权限是暂时的，需要及时把用户选择的图片保存下来放入app内部存储，否则原路径失效，页面会崩
    private String copyToMyFile(Uri uri) throws IOException {
        File filesDir = getFilesDir();
        File imageFile = new File(filesDir, "my_avatar_"+ userId +".jpg");
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