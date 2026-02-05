/**description: 笔记界面功能
 * author:漆子君
 * email:3100776336@qq.com
 * date:2026/2/5
 */

package notebook.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.biji.R;

import notebook.entity.Note;

public class NoteDetailActivity extends AppCompatActivity {

    EditText mTitle;
    EditText mContent;
    TextView mCreateTime;
    TextView mUpdateTime;
    TextView mWordCount;

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
        initEvent();

    }

    private void initEvent() {
        Intent intent = getIntent();
        if (intent != null){
        Note note = (Note)intent.getSerializableExtra("note");
            if (note != null) {
                mTitle.setText(note.getTitle());
                mContent.setText(note.getContent());
                mWordCount.setText(note.getWordCount());
            }
        }
    }

    private void initView() {
        mTitle = findViewById(R.id.note_detail_title);
        mContent = findViewById(R.id.note_detail_content);
        mWordCount =  findViewById(R.id.note_detail_word_count);
    }

}