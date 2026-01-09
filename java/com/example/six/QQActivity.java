package com.example.six;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class QQActivity extends AppCompatActivity {

    private RecyclerView mRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view);

        initRv();
    }

    private void initRv(){
        mRv = findViewById(R.id.rv);

        mRv.setLayoutManager(new LinearLayoutManager(this));

        QQAdapter adapter = new QQAdapter(getQQData());
        mRv.setAdapter(adapter);

        adapter.setOnItemClickListener(new QQAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(QQData data) {
                Toast.makeText(QQActivity.this,"ovo",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(QQActivity.this,vp2Activity.class);
                startActivity(intent);
            }
        });

    }

   int icon1 = R.mipmap.cqupt_round;
    int icon2 = R.mipmap.xiao_round;

    private ArrayList<QQData> getQQData(){
        ArrayList<QQData> dataList = new ArrayList<>();
        dataList.add(new QQData("小氏家族","小明","完了我把墨水搞卷子上了",icon2));
        dataList.add(new QQData("智慧体育","系统","通知：同学们请在月底前完成校园跑",icon1));
        dataList.add(new QQData("小氏家族（无小明）","小红","[动画表情]",icon2));
        dataList.add(new QQData("小氏家族（无小红）","小李","[动画表情]",icon2));
        dataList.add(new QQData("小氏家族（无小李）","小王","[动画表情]",icon2));
        dataList.add(new QQData("小氏家族（无小王）","小刚","[动画表情]",icon2));
        dataList.add(new QQData("小氏家族（无小刚）","小明","你们都跑完了吗",icon2));

        return dataList;

    }


}