/**
 * description: 笔记界面功能
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/2/5
 */

package notebook.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.biji.R;
import com.example.biji.databinding.ActivityNoteDetailBinding;
import com.example.biji.databinding.BottomSheetToolFontBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import notebook.entity.Note;
import notebook.entity.NoteGroup;
import notebook.entity.User;
import notebook.helper.ImeToolBarHelper;
import notebook.sql.NoteDB;
import notebook.sql.UserDB;
import notebook.utils.AppUtils;
import notebook.utils.SPUtils;

public class NoteDetailActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etTitle, etContent;
    TextView tvWordCount;
    Button btnBackArrow, btnSave, btnMenu;
    CardView toolbar;
    Note mNote;
    NoteDB noteDB;
    UserDB userDB;
    boolean isSaved = false;
    PopupMenu mPopupMenu;
    BottomSheetToolFontBinding toolFontBinding;
    ActivityNoteDetailBinding binding;
    BottomSheetBehavior<View> sheetBehavior;
    ViewFlipper viewFlipper;
    int userId;
    int groupId;
    long noteId;
    NoteGroup mGroup;
    User user;
    SharedPreferences mSp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityNoteDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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
    protected void onRestart() {
        super.onRestart();
        refreshData();
    }

    private void refreshData() {
        mNote = noteDB.queryNoteById(noteId,userId);
        groupId = mNote.getGroupId();
        if (groupId != -1) {
            mGroup = noteDB.queryGroupById(groupId, userId);
        } else {
            mGroup = null;
        }
        menuClick();
    }

    //页面退出时自动保存数据
    //这里不能使用onStop,因为按照生命周期其在onResume之后，会造成列表显示与数据不同步的情况
    protected void onPause() {
        super.onPause();
        save();
    }


    private void initEvent() {
        initClick();
        wordCountChange();
//        showToolBar();
//        sheetListener();
    }

    private void initClick() {
        goBack();
        saveClick();
        menuClick();
//        toolBarClick();
    }

    private void toolBarClick() {
        binding.ivToolFontStyle.setOnClickListener(NoteDetailActivity.this);

    }

    private void initView() {
        etTitle = findViewById(R.id.note_detail_title);
        etContent = findViewById(R.id.note_detail_content);
        tvWordCount = findViewById(R.id.note_detail_word_count);
        btnBackArrow = findViewById(R.id.btn_detail_back);
        btnSave = findViewById(R.id.btn_detail_save);
        btnMenu = findViewById(R.id.btn_detail_menu);
        toolbar = findViewById(R.id.toolbar_edit);
    }

    //初始化activity中的相关数据
    private void initData() {
        noteDB = new NoteDB(this);
        userDB = new UserDB(this);
        mSp = SPUtils.getSpData(NoteDetailActivity.this);
        Intent intent = getIntent();
        //得到extra note noteDB中id对应的笔记
        noteId = intent.getLongExtra("note_id",-1);
        userId = mSp.getInt("user_id", -1);
        user = userDB.getUserById(userId);
        mNote = noteDB.queryNoteById(noteId,userId);

        if (mNote != null) {
            groupId = mNote.getGroupId();
            Log.d("TAG", "(mNote:id)-->>" + noteId);
            Log.d("TAG", "(mNote)-->>" + mNote);
            Log.d("TAG", "(mNote:groupId)-->>" + mNote.getGroupId());
            etTitle.setText(mNote.getTitle());
            etContent.setText(mNote.getContent());
            tvWordCount.setText(mNote.getWordCount());
        }
        if (groupId != -1) {
            mGroup = noteDB.queryGroupById(groupId, userId);
        }
        Log.d("TAG", "(数据NoteDetailData:成功初始化)-->>");

        sheetBehavior = BottomSheetBehavior.from(binding.toolSheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        viewFlipper = binding.viewFlipperTool;
        viewFlipper.setDisplayedChild(0);

        Log.d("TAG", "(userid:)-->>" + userId);
    }

    private void wordCountChange() {
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            //设置文本监听器，在文本变化时更新字数
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateWordCount(AppUtils.wordCount(s.toString()));

            }
        });
    }

    //字数变化时更新相应对象的字数属性，并更新控件显示
    private void updateWordCount(String content) {
        tvWordCount.setText(content);
        mNote.setWordCount(content);
    }

    //返回
    private void goBack() {
        btnBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        //判断是否编辑过
        if (AppUtils.isChanged(mNote.getTitle(), title) || AppUtils.isChanged(mNote.getContent(), content)) {
            mNote.setUpdateTime(AppUtils.getCurrentTime());
        }

        if (AppUtils.isEmpty(title)) {
            mNote.setTitle("无题");
        } else {
            mNote.setTitle(title);
        }
        mNote.setContent(content);
        mNote.setWordCount(AppUtils.wordCount(content));

        if (mNote.getId() != 0) {
            noteDB.update(mNote, userId);
            isSaved = true;
            Log.d("TAG", "(更新后note详情)-->>" + mNote);

        } else {
            Log.d("TAG", "(NoteDetail:update)-->>error");
        }

    }

    //保存按钮的功能
    private void saveClick() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
                if (isSaved) {
                    Toast.makeText(NoteDetailActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    //点击后弹出详情页
    private void menuClick() {
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupMenu = new PopupMenu(NoteDetailActivity.this, v);
                mPopupMenu.inflate(R.menu.menu_note_detial);
                mPopupMenu.getMenu().findItem(R.id.menu_author).setTitle("作者： " + user.getName());
                mPopupMenu.getMenu().findItem(R.id.menu_create_time).setTitle("创建时间： " + mNote.getCreateTime());
                mPopupMenu.getMenu().findItem(R.id.menu_update_time).setTitle("更新时间： " + mNote.getUpdateTime());
                if (mGroup != null) {
                    mPopupMenu.getMenu().findItem(R.id.menu_group).setTitle("组: " + mGroup.getTitle());
                } else {
                    mPopupMenu.getMenu().findItem(R.id.menu_group).setTitle("组: 无");
                }
                setDeleteItemColor();
                mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.menu_delete) {
                            dialogDelete();
                            return true;
                        } else if (item.getItemId() == R.id.menu_to_group) {
                            toGroup();
                        }
                        return false;
                    }
                });
                mPopupMenu.show();
            }

            private void setDeleteItemColor() {
                MenuItem deleteItem = mPopupMenu.getMenu().findItem(R.id.menu_delete);
                SpannableString spannableString = new SpannableString(deleteItem.getTitle());
                spannableString.setSpan(
                        new ForegroundColorSpan(Color.parseColor("#D02727")),
                        0,
                        spannableString.length(),
                        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                deleteItem.setTitle(spannableString);

            }

        });
    }

    private void toGroup() {
        Intent intent = new Intent(NoteDetailActivity.this, NoteVp2Activity.class);
        intent.putExtra("note", mNote);
        intent.putExtra("user_id", userId);
        Log.d("TAG", "(NoteIntent:note)-->>" + mNote);
        startActivity(intent);
        Log.d("TAG", "(toGroup:)-->>进入点击事件，执行跳转");
    }

    private void dialogDelete() {
        new AlertDialog.Builder(NoteDetailActivity.this)
                .setMessage("确认删除吗？")
                .setPositiveButton("确认", (dialog, which) ->
                {
                    long row = noteDB.delete(mNote, userId);

                    if (row > 0) {
                        Toast.makeText(NoteDetailActivity.this, "成功删除", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(NoteDetailActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                })
                .setNegativeButton("取消", null).show();
    }


    //废稿
    //软键盘监听到弹出或高度变化时，始终将工具栏固定在键盘上方
    private void showToolBar() {
        ImeToolBarHelper.bind(findViewById(android.R.id.content), toolbar);
    }

    //展示字体样式详情
    private void showFontSheet() {
        if (viewFlipper.getDisplayedChild() != 0 || sheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
            viewFlipper.setDisplayedChild(0);
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            setToolBarLocation();
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            toolbar.setTranslationY(0);
        }


    }

    private void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(toolbar.getWindowToken(), 0);
    }

    //单独对工具栏中的按钮集合相应的点击事件
    @Override
    public void onClick(View v) {

        if (v.equals(binding.ivToolFontStyle)) {
            Log.d("TAG", "(点击toolbar:fontstyle)-->>");
            showFontSheet();
        } else if (v.equals(binding.ivToolListCheckbox)) {
        }

    }

    public void setToolBarLocation() {
        int sheetHeight = binding.viewFlipperTool.getHeight();
        int toolbarHeight = toolbar.getHeight();
        Log.d("TAG", "(height:)-->>" + "sheet:" + sheetHeight + "toolbar:" + toolbarHeight);
        int result = -sheetHeight;
        toolbar.setTranslationY(result);
    }

    private void fontSheetClick() {
        View currentView = viewFlipper.getCurrentView();
        if (currentView != null && viewFlipper.getDisplayedChild() == 0) {
            toolFontBinding = BottomSheetToolFontBinding.bind(currentView);
            toolFontBinding.toolFontH1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int start = etContent.getSelectionStart();
                    int end = etContent.getSelectionEnd();
                    if (start == end) return;
                    etContent.getText().setSpan(new AbsoluteSizeSpan(24, true), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    etContent.getText().setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


                }
            });
        }
    }

    private void sheetListener() {
        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (i == BottomSheetBehavior.STATE_EXPANDED) {
                    hideSoftInput();
                    setToolBarLocation();
                    fontSheetClick();
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
    }

}