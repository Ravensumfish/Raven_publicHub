/**
 * description: 用于笔记预览
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/2/2
 */

package notebook.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.biji.R;

import java.util.ArrayList;
import java.util.List;

import notebook.adapter.NoteAdapter;
import notebook.entity.Note;
import notebook.entity.NotePreview;
import notebook.helper.MyItemTouchHelperCallBack;
import notebook.sql.NoteDB;
import notebook.utils.AppUtils;

public class NoteActivity extends AppCompatActivity {

    Button mBtn;
    RecyclerView mRv;
    NoteAdapter noteAdapter;
    List<NotePreview> mNoteList;
    NoteDB noteDB;
    ItemTouchHelper mItemTouchHelper;
    MyItemTouchHelperCallBack mCallBack;
    String mUsername;

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
        initEvent();
    }

    private void initEvent() {
        initClick();
        refreshData();
        Log.d("TAG", "(onCreate刷新列表:)-->>");
    }

    //实时
    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
        Log.d("TAG", "(onResume刷新列表:)-->>");
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
        Intent intent = getIntent();
        mUsername = intent.getStringExtra("username");
        noteDB = new NoteDB(this);
        mNoteList = new ArrayList<>();
        noteAdapter = new NoteAdapter(NoteActivity.this, mNoteList);
        mCallBack = new MyItemTouchHelperCallBack(noteAdapter);
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
        mItemTouchHelper = new ItemTouchHelper(mCallBack);
        mItemTouchHelper.attachToRecyclerView(mRv);
        Log.d("TAG", "(试试:成功生成rv)-->>");

    }

    //rvItem的点击事件
    private void rvItemClick() {
        noteAdapter.setItemClickListener(new NoteAdapter.onItemClickListener() {
            @Override
            public void onItemClick(NotePreview notePreview) {
                Note note = (Note) notePreview;
                note.setAuthor(mUsername);
                Log.d("TAG", "(列表页id为"+note.getId()+"的笔记:)-->>" + note);
                AppUtils.startActivity(NoteActivity.this, NoteDetailActivity.class, note);
            }
        });

    }

    //跳转笔记编辑界面添加新的笔记
    private void addNewNoteClick() {
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note note = addNote();
                AppUtils.startActivity(NoteActivity.this, NoteDetailActivity.class, note);
            }
        });
    }

    //添加新笔记并写进NoteDB
    public Note addNote() {
        Note note = new Note();
        note.setAuthor(mUsername);
        note.setCreateTime(AppUtils.getCurrentTime());
        note.setUpdateTime(AppUtils.getCurrentTime());
        long row = noteDB.insert(note);
        if (row != -1) {
            Log.d("TAG", "(newNote:id)-->>" + note.getId());
            Toast.makeText(NoteActivity.this, "新增笔记成功", Toast.LENGTH_SHORT).show();
            return note;
        } else {
            Toast.makeText(NoteActivity.this, "新增笔记失败", Toast.LENGTH_SHORT).show();
            return null;
        }
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
        notePreview.setCreateTime(note.getCreateTime());
        notePreview.setUpdateTime(note.getUpdateTime());
        return notePreview;
    }

    //搜索功能
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_preview, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            //当搜索栏中文本变化时调用此方法
            @Override
            public boolean onQueryTextChange(String newText) {
                List<Note> notes = noteDB.query(newText);
                mNoteList.clear();
                for (Note note : notes) {
                    NotePreview notePreview = noteToNotePreview(note);
                    mNoteList.add(notePreview);
                }
                noteAdapter.refreshData(mNoteList);
                return true;
            }

            //提交时调用，目前用不着，因为搜索是根据文本实时变化的
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}