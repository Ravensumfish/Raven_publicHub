/**description: 用于笔记rv的适配器类
 * author:漆子君
 * email:3100776336@qq.com
 * date:2026/2/4
 */

package notebook.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.biji.R;

import java.util.ArrayList;

import notebook.entity.NotePreview;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private final ArrayList<NotePreview> notes;

    public interface onItemClickListener{
        void onItemClick(NotePreview notePreview);
    }

    onItemClickListener itemClickListener;

    public void setItemClickListener(onItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public NoteAdapter(ArrayList<NotePreview> notes) {
        this.notes = notes;
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
         holder.bind(notes.get(position));
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView mTitle;
        TextView mFirstLine;
        TextView mUpdateTime;
        TextView mWordCount;
        ImageView mIcon;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.rv_item_title);
            mFirstLine = itemView.findViewById(R.id.rv_item_firstLine);
            mUpdateTime = itemView.findViewById(R.id.rv_item_time_update);
            mWordCount = itemView.findViewById(R.id.rv_item_word_count);
            mIcon = itemView.findViewById(R.id.rv_item_icon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    NotePreview notePreview = notes.get(pos);
                    itemClickListener.onItemClick(notePreview);
                }
            });
        }

        public void bind(NotePreview np) {
            mTitle.setText(np.getTitle());
            mFirstLine.setText(getFirstLine(np.getContent()));
            mUpdateTime.setText(np.getUpdateTime());
            mWordCount.setText(np.getWordCount());
            mIcon.setImageResource(np.getIcon());
        }

        public String getFirstLine(String s) {
            String firstLine = s;
            if (s == null || s.isEmpty()) {
                return "";
            }
            int index = s.indexOf("\n");
            if (index != -1) {
                firstLine = s.substring(0, index);
            }
            return firstLine;
        }
    }
}
