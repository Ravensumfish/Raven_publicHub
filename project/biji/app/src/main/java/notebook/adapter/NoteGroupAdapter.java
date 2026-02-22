/**
 * description: 用于笔记组rv的适配器类
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/2/21
 */

package notebook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.biji.R;

import java.util.Collections;
import java.util.List;

import notebook.entity.NoteGroup;
import notebook.sql.NoteDB;

public class NoteGroupAdapter extends RecyclerView.Adapter<NoteGroupAdapter.NoteGroupViewHolder>{

    private Context mContext;
    private List<NoteGroup> groups;
    private NoteDB noteDB;
    private int userId;

    public NoteGroupAdapter(Context mContext, List<NoteGroup> groups, int userId) {
        this.mContext = mContext;
        this.groups = groups;
        this.userId = userId;
        noteDB = new NoteDB(mContext);
    }

    public interface onItemClickListener {
        void onItemClick(NoteGroup noteGroup);
    }

    public interface onLongItemClickListener {
        void onLongItemClick(NoteGroup noteGroup);
    }

    onItemClickListener itemClickListener;
    onLongItemClickListener longItemClickListener;


    public void setItemClickListener(onItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setLongItemClickListener(onLongItemClickListener itemClickListener) {
        this.longItemClickListener = itemClickListener;
    }

    public void refreshData(List<NoteGroup> groups) {
        this.groups = groups;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return groups.size();
    }

    @NonNull
    @Override
    public NoteGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_group_note, parent, false);
        return new NoteGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteGroupViewHolder holder, int position) {
        holder.bind(groups.get(position));
    }

    public class NoteGroupViewHolder extends RecyclerView.ViewHolder {

        TextView mTitle;
        TextView mCreateTime;
        TextView mNoteCount;

        public NoteGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.rv_group_title);
            mCreateTime = itemView.findViewById(R.id.rv_group_create_time);
            mNoteCount = itemView.findViewById(R.id.rv_group_item_count);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    NoteGroup noteGroup = groups.get(pos);
                    itemClickListener.onItemClick(noteGroup);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = getAdapterPosition();
                    NoteGroup noteGroup = groups.get(pos);
                    longItemClickListener.onLongItemClick(noteGroup);
                    return true;
                }
            });
        }

        public void bind(NoteGroup ng) {
            mTitle.setText(ng.getTitle());
            mCreateTime.setText(ng.getCreateTime());
            mNoteCount.setText(String.valueOf(ng.getNoteCount()));
        }

    }
}
