/**description: 用于笔记预览的实体类
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/2/4
 */

package notebook.entity;

import androidx.annotation.NonNull;

import com.example.biji.R;

public class NotePreview extends Note{
    private int icon = R.drawable.baseline_text_snippet_24;

    public NotePreview() {
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }


}
