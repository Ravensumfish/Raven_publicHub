/**
 * description: 用于笔记预览
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/2/2
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.biji.R;

import java.util.ArrayList;
import java.util.List;

import notebook.adapter.NoteAdapter;
import notebook.entity.Note;
import notebook.entity.NotePreview;
import notebook.sql.NoteDB;
import notebook.utils.AppUtils;

public class NoteActivity extends AppCompatActivity {

    Button mBtn;
    RecyclerView mRv;
    NoteAdapter noteAdapter;
    List<NotePreview> mNoteList;
    NoteDB noteDB ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_note_preview);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.note_preview), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initView();
        initData();
        initRv();
        initClick();

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    //刷新rv
    private void refreshData() {
        mNoteList = getNotePreviewsFromDB();
        noteAdapter.refreshData(mNoteList);
    }

    //绑定控件
    private void initView() {
        mBtn = findViewById(R.id.button_note);
        mRv = findViewById(R.id.rv_note);
    }

    //初始化activity中的相关数据
    private void initData() {
        noteDB = new NoteDB(this);
        mNoteList = new ArrayList<>();
        noteAdapter = new NoteAdapter(mNoteList);
        Log.d("TAG", "(数据NotePreviewList:成功初始化)-->>");
    }

    //封装点击事件
    private void initClick() {
        addNewNoteClick();
        rvItemClick();
    }

    //rv生成
    private void initRv() {
        mRv.setAdapter(noteAdapter);
        mRv.setLayoutManager(new LinearLayoutManager(this));
        Log.d("TAG", "(试试:成功生成rv)-->>");

    }

    //rvItem的点击事件
    private void rvItemClick() {
        noteAdapter.setItemClickListener(new NoteAdapter.onItemClickListener() {
            @Override
            public void onItemClick(NotePreview notePreview) {
                Note note = (Note)notePreview;
                Log.d("TAG","(列表页笔记id:)-->>" + note.getId());
                AppUtils.startActivity(NoteActivity.this, NoteDetailActivity.class,note);
            }
        });

    }

    //跳转笔记编辑界面添加新的笔记
    private void addNewNoteClick() {
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note note = addNote();
                AppUtils.startActivity(NoteActivity.this, NoteDetailActivity.class,note);
            }
        });
    }

    //添加新笔记并写进NoteDB
    public Note addNote() {
        Note note = new Note();
        note.setCreateTime(AppUtils.getCurrentTime());
        note.setUpdateTime(AppUtils.getCurrentTime());
        long row = noteDB.insert(note);
        if (row != -1) {
            Log.d("TAG","(newNote:id)-->>" + note.getId());
            Toast.makeText(NoteActivity.this, "新增笔记成功", Toast.LENGTH_SHORT).show();
            return note;
        } else {
            Toast.makeText(NoteActivity.this, "新增笔记失败", Toast.LENGTH_SHORT).show();
           return null;
        }
    }

    //获取笔记集合
    private List<Note> getNotes() {
        return noteDB.queryAll();
    }

    //从NoteDB中获取笔记预览
    private List<NotePreview> getNotePreviewsFromDB() {
        List<NotePreview> notePreviews = new ArrayList<>();
        List<Note> notes = noteDB.queryAll();
        for (Note note : notes) {
            NotePreview notePreview = noteToNotePreview(note);
            notePreviews.add(notePreview);
        }
        Log.d("TAG", "(试试:笔记预览生成成功)-->>" + notePreviews + "\n有" + notePreviews.size() + "条数据");
        return notePreviews;
    }

    //将父类转化为子类
    public NotePreview noteToNotePreview(Note note) {
        NotePreview notePreview = new NotePreview();
        notePreview.setId(note.getId());
        notePreview.setTitle(note.getTitle());
        notePreview.setContent(note.getContent());
        notePreview.setWordCount(note.getWordCount());
        notePreview.setUpdateTime(note.getUpdateTime());
        return notePreview;
    }

}