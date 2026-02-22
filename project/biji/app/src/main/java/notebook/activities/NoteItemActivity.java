/**
 * description: 用于笔记item预览
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/2/21
 */

package notebook.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.biji.R;
import com.example.biji.databinding.ActivityNotePreviewBinding;

import java.util.ArrayList;
import java.util.List;

import notebook.adapter.NoteAdapter;
import notebook.entity.Note;
import notebook.entity.NotePreview;
import notebook.entity.User;
import notebook.helper.MyItemTouchHelperCallBack;
import notebook.sql.NoteDB;
import notebook.sql.UserDB;
import notebook.utils.AppUtils;

public class NoteItemActivity extends Fragment {

    ActivityNotePreviewBinding binding;
    RecyclerView mRv;
    ActionBar actionBar;
    NoteAdapter noteAdapter;
    List<NotePreview> mNoteList;
    NoteDB noteDB;
    ItemTouchHelper mItemTouchHelper;
    MyItemTouchHelperCallBack mCallBack;
    String mUsername;
    int userId;
    User user;
    UserDB userDB;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle arguments = getArguments();
        if (arguments != null) {
            userId = arguments.getInt("user_id", -1);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityNotePreviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
        initRv();
        initEvent();

    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
        Log.d("TAG", "(onResume刷新列表:)-->>");
    }

    private void initEvent() {
        initClick();
        refreshData();
        Log.d("TAG", "(onCreate刷新列表:)-->>");
    }

    private void initClick() {
        addNewNoteClick();
        rvItemClick();
    }

    private void initRv() {
        mRv.setAdapter(noteAdapter);
        mRv.setLayoutManager(new LinearLayoutManager(requireContext()));
        mItemTouchHelper = new ItemTouchHelper(mCallBack);
        mItemTouchHelper.attachToRecyclerView(mRv);
        Log.d("TAG", "(试试:成功生成rv)-->>");
    }

    private void initView() {
        mRv = binding.rvNote;
        setMargin();
    }

    private void initData() {
        userDB = new UserDB(requireContext());
        user = userDB.getUserById(userId);
        mUsername = user.getName();
        noteDB = new NoteDB(requireContext());
        mNoteList = new ArrayList<>();
        noteAdapter = new NoteAdapter(requireContext(), mNoteList, userId);
        mCallBack = new MyItemTouchHelperCallBack(noteAdapter);
        actionBar = ((AppCompatActivity)requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Log.d("TAG", "(mUsername)-->>" + mUsername);
        Log.d("TAG", "(userid:)-->>" + userId);
        Log.d("TAG", "(数据NoteItemList:成功初始化)-->>");
    }

    private void setMargin() {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mRv.getLayoutParams();
        if (actionBar != null) {
            params.topMargin = actionBar.getHeight();
            Log.d("TAG", "(actionBarHeight:)-->>" + params.topMargin + "px");
            if (params.topMargin != 0) {
                mRv.setLayoutParams(params);
            }
        }
    }

    private void addNewNoteClick() {
        binding.buttonNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note note = addNote();
                AppUtils.startActivityWithNote(requireActivity(), NoteDetailActivity.class, note);
            }
        });
    }

    public Note addNote() {
        Note note = new Note();
        note.setCreateTime(AppUtils.getCurrentTime());
        note.setUpdateTime(AppUtils.getCurrentTime());
        long row = noteDB.insert(note,userId);
        if (row != -1) {
            Log.d("TAG", "(newNote:id)-->>" + note.getId());
            Toast.makeText(requireContext(), "新增笔记成功", Toast.LENGTH_SHORT).show();
            return note;
        } else {
            Toast.makeText(requireContext(), "新增笔记失败", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private void refreshData() {
        mNoteList = getNotePreviewsFromDB();
        noteAdapter.refreshData(mNoteList);
    }

    private List<NotePreview> getNotePreviewsFromDB() {
        List<NotePreview> notePreviews = new ArrayList<>();
        List<Note> notes = noteDB.queryAll(userId);
        for (Note note : notes) {
            NotePreview notePreview = noteToNotePreview(note);
            notePreviews.add(notePreview);
        }
        Log.d("TAG", "(试试:笔记预览生成成功)-->>" + notePreviews + "\n有" + notePreviews.size() + "条数据");
        return notePreviews;
    }

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

    private void rvItemClick() {
        noteAdapter.setItemClickListener(new NoteAdapter.onItemClickListener() {
            @Override
            public void onItemClick(NotePreview notePreview) {
                Note note = notePreview;
                Log.d("TAG", "(列表页id为" + note.getId() + "的笔记:)-->>" + note);
                AppUtils.startActivityWithNoteId(requireContext(), NoteDetailActivity.class, note.getId());
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_note_preview, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            //当搜索栏中文本变化时调用此方法
            @Override
            public boolean onQueryTextChange(String newText) {
                List<Note> notes = noteDB.query(newText,userId);
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

        super.onCreateOptionsMenu(menu,menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            requireActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}