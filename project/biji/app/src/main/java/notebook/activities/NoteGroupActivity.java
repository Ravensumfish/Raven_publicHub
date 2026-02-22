/**
 * description: 用于笔记group预览与item分组
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/2/21
 */

package notebook.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.biji.R;
import com.example.biji.databinding.ActivityNoteGroupBinding;
import com.example.biji.databinding.DialogNoteGroupEditBinding;

import java.util.ArrayList;
import java.util.List;

import notebook.adapter.NoteGroupAdapter;
import notebook.entity.Note;
import notebook.entity.NoteGroup;
import notebook.entity.NotePreview;
import notebook.sql.NoteDB;
import notebook.utils.AppUtils;
import notebook.utils.SPUtils;

public class NoteGroupActivity extends Fragment {
    ActivityNoteGroupBinding binding;
    DialogNoteGroupEditBinding editBinding;
    RecyclerView mRv;
    ActionBar actionBar;
    NoteGroupAdapter noteAdapter;
    List<NoteGroup> mNoteGroup;
    NoteDB noteDB;
    SharedPreferences mSp;
    int userId;
    int groupId;
    Note mNote;

    //在这里接收数据
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle arguments = getArguments();
        if (arguments != null) {
            userId = arguments.getInt("user_id", -1);
            mNote = (Note) arguments.getSerializable("note");
            Log.d("TAG", "(Group:mNote)-->>" + mNote);
        }

    }

    //在这里布置布局
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityNoteGroupBinding.inflate(inflater, container, false);
        editBinding = DialogNoteGroupEditBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    //在这里进行需要的操作
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initRv();
        initEvent();

    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
        Log.d("TAG", "(onResume刷新group列表:)-->>");
    }

    private void initData() {
        mSp = SPUtils.getSpData(requireContext());
        mRv = binding.rvNoteGroup;
        noteDB = new NoteDB(requireContext());
        mNoteGroup = new ArrayList<>();
        noteAdapter = new NoteGroupAdapter(requireContext(), mNoteGroup, userId);
        actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (mNote != null) {
                actionBar.setTitle("选择分组");
                groupId = mNote.getGroupId();
                binding.btnNoteAdd.setVisibility(View.INVISIBLE);
                if (groupId != -1) {
                    binding.btnGroupRemove.setVisibility(View.VISIBLE);
                }
            }
        }

        Log.d("TAG", "(userid:)-->>" + userId);
        Log.d("TAG", "(数据NoteGroupList:成功初始化)-->>");
    }

    private void initRv() {
        mRv.setAdapter(noteAdapter);
        mRv.setLayoutManager(new LinearLayoutManager(requireContext()));
        Log.d("TAG", "(试试:成功生成rv)-->>");
    }

    private void initEvent() {
        initClick();
        refreshData();
        Log.d("TAG", "(onCreate刷新列表:)-->>");
    }

    private void initClick() {
        //根据传来的数据不同而变化模式
        addNewGroupClick();
        removeFromGroup();
        rvItemClick();
    }

    private void removeFromGroup() {
        binding.btnGroupRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNote.setGroupId(-1);
                noteDB.update(mNote, userId);
                Toast.makeText(requireContext(), "移出分组成功", Toast.LENGTH_SHORT).show();
                requireActivity().finish();
            }
        });
    }

    //设置添加新组按钮
    private void addNewGroupClick() {
        binding.btnNoteAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();

            }
        });
    }


    //弹窗创建新组流程
    private void showDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(editBinding.getRoot());
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            window.setGravity(Gravity.CENTER);
        }
        dialog.show();

        editBinding.btnDialogCancel.setOnClickListener(view -> dialog.dismiss());
        editBinding.btnDialogConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AppUtils.isEmpty(editBinding.etDialogGroupTitle.getText().toString())) {
                    NoteGroup group = addGroup();
                    String title = editBinding.etDialogGroupTitle.getText().toString();
                    if (group != null) {
                        group.setTitle(title);
                        noteDB.updateGroup(group, userId);
                        refreshData();
                    }
                    Log.d("TAG", "(设置组名成功:)-->>" + title);
                }
                dialog.dismiss();
            }
        });
    }

    //在数据库中加入新组
    private NoteGroup addGroup() {
        NoteGroup group = new NoteGroup();
        group.setCreateTime(AppUtils.getCurrentTime());
        long row = noteDB.insertGroup(group, userId);
        if (row != -1) {
            Log.d("TAG", "(newNoteGroup:id)-->>" + group.getId());
            Toast.makeText(requireContext(), "新增组成功", Toast.LENGTH_SHORT).show();
            return group;
        } else {
            Toast.makeText(requireContext(), "新增组失败", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    //为item设置点击事件
    private void rvItemClick() {
        noteAdapter.setItemClickListener(new NoteGroupAdapter.onItemClickListener() {
            @Override
            public void onItemClick(NoteGroup noteGroup) {
                Log.d("TAG", "(groupId为" + noteGroup.getId() + "的笔记组:)-->>" + noteGroup);
                //没有note说明是笔记组展示，有note就是选择分组模式
                if (mNote == null) {
                    AppUtils.startActivityWithGroupId(requireContext(), NoteActivity.class, noteGroup.getId());
                } else {

                    if (groupId == noteGroup.getId()) {
                        Toast.makeText(requireContext(), "已在该分组中！", Toast.LENGTH_SHORT).show();
                    } else {
                        mNote.setGroupId(noteGroup.getId());
                        long update = noteDB.update(mNote, userId);
                        if (update != -1) {
                            Toast.makeText(requireContext(), "加入分组成功", Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "(group:)-->>分组成功");
                        } else {
                            Log.d("TAG", "(group:)-->>分组失败");
                        }
                    }
                    requireActivity().finish();
                }
            }
        });

        //选择分组模式不需要长按
        if (mNote == null) {
            noteAdapter.setLongItemClickListener(new NoteGroupAdapter.onLongItemClickListener() {
                @Override
                public void onLongItemClick(NoteGroup noteGroup) {
                    dialogDelete(noteGroup);
                }
            });
        }
    }

    private void refreshData() {
        mNoteGroup = getNoteGroupFromDB();
        noteAdapter.refreshData(mNoteGroup);
    }

    private List<NoteGroup> getNoteGroupFromDB() {
        List<NoteGroup> noteGroup = noteDB.queryAllGroups(userId);
        Log.d("TAG", "(试试:笔记组预览生成成功)-->>" + noteGroup + "\n有" + noteGroup.size() + "条数据");
        return noteGroup;
    }

    //这里是actionBar的搜索设置
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_note_preview, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            //当搜索栏中文本变化时调用此方法
            @Override
            public boolean onQueryTextChange(String newText) {
                List<NoteGroup> noteGroups = noteDB.queryGroup(newText, userId);
                mNoteGroup.clear();
                mNoteGroup.addAll(noteGroups);
                noteAdapter.refreshData(mNoteGroup);
                return true;
            }

            //提交时调用，目前用不着，因为搜索是根据文本实时变化的
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            requireActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void dialogDelete(NoteGroup noteGroup) {
        new AlertDialog.Builder(requireContext())
                .setMessage("确认删除吗？")
                .setPositiveButton("确认", (dialog, which) ->
                {
                    long row = noteDB.deleteGroup(noteGroup, userId);

                    if (row > 0) {
                        Toast.makeText(requireContext(), "成功删除", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "删除失败", Toast.LENGTH_SHORT).show();
                    }
                    refreshData();
                })
                .setNegativeButton("取消", null).show();
    }
}
