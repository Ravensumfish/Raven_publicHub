/**description: 用于笔记预览
 * author:漆子君
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
import java.util.Calendar;

import notebook.adapter.NoteAdapter;
import notebook.entity.NotePreview;
import notebook.utils.AppUtils;

public class NoteActivity extends AppCompatActivity {

    Button mBtn;
    RecyclerView mRv;
    ArrayList<NotePreview> notes;
    NoteAdapter noteAdapter;

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
        initRv();
        initClick();

    }

    private void initClick() {
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.startActivity(NoteActivity.this,NoteDetailActivity.class);
            }
        });
    }

    private void initRv() {
        Log.d("TAG","(试试:在生成rv之前)-->>");
        noteAdapter = new NoteAdapter(getNotes());
        mRv.setAdapter(noteAdapter);
        mRv.setLayoutManager(new LinearLayoutManager(this));
        Log.d("TAG","(试试:成功生成rv)-->>");
        noteAdapter.setItemClickListener(new NoteAdapter.onItemClickListener() {
            @Override
            public void onItemClick(NotePreview notePreview) {
                AppUtils.startActivity(NoteActivity.this,NoteDetailActivity.class,notePreview);
            }
        });
    }

    private void initView() {
        mBtn = findViewById(R.id.button_note);
        mRv = findViewById(R.id.rv_note);
    }

    private ArrayList<NotePreview> getNotes() {
        ArrayList<NotePreview> notePreviews = new ArrayList<>();
        Log.d("TAG","(试试:在创笔记预览组之前)-->>");
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        notePreviews.add(new NotePreview("我真受不了了", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈", hour + ":" + minute));
        notePreviews.add(new NotePreview("何意味", "呜呜呜", hour + ":" + minute));
        for (int i = 0; i < 8; i++) {
            notePreviews.add(new NotePreview("标题" + i, "这里是测试" + i, hour + ":" + minute));
        }
        Log.d("TAG","(试试:笔记预览生成成功)-->>"+ notePreviews +"\n有"+ notePreviews.size()+"条数据");
        return notePreviews;
    }

}