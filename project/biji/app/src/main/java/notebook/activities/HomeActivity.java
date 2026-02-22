/**
 * description: 主界面功能
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/2/13
 */
package notebook.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.biji.R;
import com.example.biji.databinding.ActivityHomeBinding;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import notebook.AI.APIClient;
import notebook.entity.User;
import notebook.sql.UserDB;
import notebook.utils.AppUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity {

    String username;
    Button btn_start;
    AppCompatButton btn_settings;
    Handler mHandler;
    ActivityHomeBinding homeBinding;
    String mURL;
    APIClient apiClient;
    List<String> pages;
    final int singleMax = 20;
    int currentPage = 0;
    String originSetting;
    int userId;
    User user;
    UserDB userDB;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        homeBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(homeBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initView();
        initData();
        initClick();

    }

    private void initData() {
        mURL = "https://api.deepseek.com/v1/chat/completions";
        originSetting = getString(R.string.origin_setting);
        apiClient = new APIClient();
        pages = new ArrayList<>();

        userDB = new UserDB(HomeActivity.this);
        Intent intent = getIntent();
        userId = intent.getIntExtra("user_id",-1);
        user = userDB.getUserById(userId);
        username = user.getName();
        Log.d("TAG", "(user:)-->>" + user);
        Log.d("TAG", "(userid:)-->>" + userId);
    }


    private void initView() {
        btn_start = findViewById(R.id.btn_home_start);
        btn_settings = findViewById(R.id.btn_home_settings);
    }

    private void initClick() {
        startNote();
        settings();
        ask();
        btnLast();
        btnNext();
        overChat();

    }

    private void overChat() {
        homeBinding.btnHomeFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (homeBinding.btnHomeFinish.getVisibility() == View.VISIBLE) {
                    clear();
                    homeBinding.btnHomeFinish.setVisibility(View.INVISIBLE);
                    homeBinding.etHomeChatAsk.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void clear() {
        pages.clear();
        currentPage = 0;
        homeBinding.tvHomeChatResponse.setText(null);
    }

    private void btnNext() {
        homeBinding.btnHomeNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pages != null && !pages.isEmpty()) {
                    if (currentPage < pages.size() - 1) {
                        currentPage++;
                        homeBinding.tvHomeChatResponse.setText(pages.get(currentPage));
                    } else if (currentPage == pages.size() - 1) {
                        homeBinding.btnHomeFinish.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void btnLast() {
        homeBinding.btnHomeLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pages != null && !pages.isEmpty()) {
                    if (currentPage > 0) {
                        currentPage--;
                        homeBinding.tvHomeChatResponse.setText(pages.get(currentPage));
                    }
                }
            }
        });
    }

    private void ask() {
        homeBinding.btnHomeSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prompt = homeBinding.etHomeChatAsk.getText().toString();

                if (AppUtils.isEmpty(prompt))return;

                homeBinding.etHomeChatAsk.setText(null);
                homeBinding.etHomeChatAsk.setVisibility(View.INVISIBLE);
                Log.d("TAG", "(ask:)-->>" + prompt);

                getNetRequest(prompt);

            }
        });
    }

    private String decodeJson(String json) {
        JsonObject obj = new Gson().fromJson(json, JsonObject.class);
        String message = obj.getAsJsonArray("choices").get(0).getAsJsonObject()
                .getAsJsonObject("message").get("content").getAsString();

        return message;
    }

    private void settings() {
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.startActivityWithUserId(HomeActivity.this, UserActivity.class,userId);
            }
        });
    }

    private void startNote() {
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.startActivityWithUserId(HomeActivity.this, NoteVp2Activity.class, userId);
            }
        });
    }

    private void getNetRequest(String prompt) {

        apiClient.getResponse(prompt,originSetting + "。用户的名字是" + username, mURL, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("TAG", "(ask:response)-->>失败响应1");
                runOnUiThread(() -> homeBinding.tvHomeChatResponse.setText("--无响应--"));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d("TAG", "(ask:ResponseCode)-->>" + response.code());
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    String result = decodeJson(responseBody);
                    Log.d("TAG", "(ask:response)-->>成功响应" + "reply:" + result);
                    AppUtils.splitTextToPages(result, singleMax, pages);
                    runOnUiThread(() ->
                            homeBinding.tvHomeChatResponse.setText(pages.get(0)));
                    currentPage = 0;
                } else {
                    Log.d("TAG", "(ask:response)-->>失败响应2");
                    runOnUiThread(() -> homeBinding.tvHomeChatResponse.setText("--无响应--"));
                }

            }
        });

    }

}