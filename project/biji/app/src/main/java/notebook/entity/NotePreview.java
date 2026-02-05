/**description: 用于笔记预览的实体类
 * author:漆子君
 * email:3100776336@qq.com
 * date:2026/2/4
 */

package notebook.entity;

import android.graphics.drawable.Icon;

import androidx.annotation.NonNull;

import com.example.biji.R;

public class NotePreview extends Note{
    private int icon = R.drawable.baseline_text_snippet_24;


    public NotePreview(String title, String content, String updateTime) {
        super(title, content, updateTime);

    }


    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    @NonNull
    @Override
    public String toString() {
        return "NotePreview{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", wordCount='" + wordCount + '\'' +
                '}';
    }
}
