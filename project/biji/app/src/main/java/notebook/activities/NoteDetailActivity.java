/**
 * description: 笔记界面功能
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/2/5
 */

package notebook.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.example.biji.R;

import notebook.entity.Note;
import notebook.sql.NoteDB;
import notebook.utils.AppUtils;

public class NoteDetailActivity extends AppCompatActivity {

    EditText etTitle, etContent;
    TextView tvCreateTime, tvUpdateTime, tvWordCount;
    Button btnBackArrow, btnSave;
    Note mNote;
    NoteDB noteDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_note_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.note_detail), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initView();
        initData();
        initEvent();

    }

    @Override
    protected void onPause() {
        super.onPause();
        save();
    }

    private void initEvent() {
        goBack();
        saveClick();

    }


    private void initView() {
        etTitle = findViewById(R.id.note_detail_title);
        etContent = findViewById(R.id.note_detail_content);
        tvWordCount = findViewById(R.id.note_detail_word_count);
        btnBackArrow = findViewById(R.id.back);
        btnSave = findViewById(R.id.save);
    }

    //初始化activity中的相关数据
    private void initData() {
        noteDB = new NoteDB(this);
        Intent intent = getIntent();
        //得到extra note
        mNote = (Note) intent.getSerializableExtra("note");
        if (mNote != null) {
            Log.d("TAG", "(mNote:id)-->>" + mNote.getId());
            etTitle.setText(mNote.getTitle());
            etContent.setText(mNote.getContent());
            tvWordCount.setText(mNote.getWordCount());
        }
        Log.d("TAG", "(数据NoteDetailData:成功初始化)-->>");
    }

    //返回
    private void goBack() {
        btnBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
                finish();
            }
        });
    }

    //保存笔记
    public void save() {
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();
        Log.d("TAG", "(title:)-->>" + title);
        Log.d("TAG", "(content:)-->>" + content);
        if (AppUtils.isEmpty(title)) {
            mNote.setTitle("无题");
        } else {
            mNote.setTitle(title);
        }
        mNote.setContent(content);
        mNote.setWordCount(String.valueOf(content.length()));
        mNote.setUpdateTime(AppUtils.getCurrentTime());
        if (mNote.getId() != 0) {
            noteDB.update(mNote);
            Toast.makeText(NoteDetailActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "(更新后note详情)-->>" + mNote);

        } else {
            mNote.setCreateTime(AppUtils.getCurrentTime());
            mNote.setUpdateTime(AppUtils.getCurrentTime());
            noteDB.insert(mNote);
            Log.d("TAG", "(编辑页新建笔记:新笔记id)-->>" + mNote.getId());
        }


        }

    private void saveClick() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }
}