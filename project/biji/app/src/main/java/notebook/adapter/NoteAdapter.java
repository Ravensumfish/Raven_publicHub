/**
 * description: 用于笔记rv的适配器类
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/2/4
 */

package notebook.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.biji.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import notebook.entity.NotePreview;
import notebook.helper.ItemMoveListener;
import notebook.sql.NoteDB;
import notebook.utils.SPUtils;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> implements ItemMoveListener {

    private Context mContext;
    private List<NotePreview> previews;
    private NoteDB noteDB;
    private int userId;
    List<Long> noteOrder;
    SharedPreferences mSp;
    StringBuilder builder = new StringBuilder();
    int groupId = -1;

    public NoteAdapter(Context mContext, List<NotePreview> previews, int userId) {
        this.mContext = mContext;
        this.previews = previews;
        this.userId = userId;
        noteDB = new NoteDB(mContext);
        mSp = SPUtils.getSpData(mContext);
    }

    public NoteAdapter(Context mContext, List<NotePreview> previews, int userId, int groupId) {
        this.mContext = mContext;
        this.previews = previews;
        this.userId = userId;
        this.groupId = groupId;
        noteDB = new NoteDB(mContext);
        mSp = SPUtils.getSpData(mContext);
    }

    //在adapter中实现拖动位移数据更新
    @Override
    public boolean onItemMove(int fromPos, int toPos) {
        Collections.swap(previews, fromPos, toPos);
        notifyItemMoved(fromPos, toPos);
        return true;
    }

    @Override
    public void itemMoveFinished() {
        saveOrder();
    }

    //把顺序存成字符串，存入sp
    private void saveOrder() {
        builder.setLength(0);
        for (int i = 0; i < previews.size(); i++) {
            builder.append(previews.get(i).getId());
            if (i < previews.size() - 1) {
                builder.append(",");
            }
        }

        if (groupId == -1) {
            SPUtils.editString(mSp, "note_order", builder.toString());
            Log.d("TAG", "(noteAdapter:order)-->>保存成功" + builder.toString());
        } else {
            SPUtils.editString(mSp, "note_order_" + groupId, builder.toString());
            Log.d("TAG", "(noteAdapter:orderInGroup)-->>保存成功" + builder.toString());
        }

    }

    //读取完成后更改noteList的顺序
    private void updateOrder() {

        previews.sort(new Comparator<NotePreview>() {
            @Override
            public int compare(NotePreview o1, NotePreview o2) {
                int index1 = noteOrder.indexOf(o1.getId());
                int index2 = noteOrder.indexOf(o2.getId());
                //如果sp中查找不到（新笔记），把其设为第一
                if (index1 == -1) index1 = Integer.MAX_VALUE;
                if (index2 == -1) index2 = Integer.MAX_VALUE;
                return Integer.compare(index1, index2);
            }
        });
        Log.d("TAG", "(updateOrder:)-->>排序完成");

    }

    private void decodeOrder() {
        String order;
        if (groupId == -1) {
            order = mSp.getString("note_order", null);
        } else {
            order = mSp.getString("note_order_" + groupId, null);
        }
        noteOrder = new ArrayList<>();
        if (order != null) {
            String[] id = order.split(",");
            for (int i = 0; i < id.length; i++) {
                //把笔记id按照sp中保存的顺序读取出来
                noteOrder.add(Long.parseLong(id[i]));
            }
        }
    }

    //在adapter中实现左滑弹窗删除选项，确认后删除并更新数据
    @Override
    public void onItemRemove(int pos) {
        Log.d("TAG", "(进入:onItemRemove)-->>position: " + pos);
        Log.d("TAG", "(进入:onItemRemove)-->>context: " + mContext);
        new AlertDialog.Builder(mContext)
                .setMessage("确认删除吗？")
                .setPositiveButton("确认", (dialog, which) ->
                {
                    NotePreview notePreview = previews.get(pos);
                    long row = noteDB.delete(notePreview, userId);
                    previews.remove(pos);
                    notifyItemRemoved(pos);
                    if (row > 0) {
                        Toast.makeText(mContext, "成功删除", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "删除失败", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", null).show();
    }


    public interface onItemClickListener {
        void onItemClick(NotePreview notePreview);
    }

    onItemClickListener itemClickListener;

    public void setItemClickListener(onItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    //刷新数据
    public void refreshData(List<NotePreview> previews) {
        this.previews = previews;
        decodeOrder();
        if (noteOrder != null) {
            updateOrder();
        }
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return previews.size();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.bind(previews.get(position));
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
                    NotePreview notePreview = previews.get(pos);
                    itemClickListener.onItemClick(notePreview);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return false;
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
