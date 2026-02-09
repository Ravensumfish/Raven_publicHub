/**
 * description: 笔记界面功能，滑动与拖拽效果
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/2/8
 */

package notebook.helper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import notebook.adapter.NoteAdapter;

public class MyItemTouchHelperCallBack extends ItemTouchHelper.Callback {

    private ItemMoveListener itemMoveListener;
    private NoteAdapter noteAdapter;

    public MyItemTouchHelperCallBack(NoteAdapter noteAdapter) {
        this.itemMoveListener = noteAdapter;
        this.noteAdapter = noteAdapter;
    }

    //Flag是方向标志，表示拖动与侧滑效果支持的方向
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        //上下拖动
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        //左右滑动 这里我需要左滑删除数据
        int swipeFlags = ItemTouchHelper.LEFT;

        return makeMovementFlags(dragFlags, swipeFlags);
    }

    //当拖动时执行的方法，当监听到item移动，回调adapter中方法执行数据刷新
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        int fromPos = viewHolder.getAdapterPosition();
        int toPos = target.getAdapterPosition();
        return itemMoveListener.onItemMove(fromPos, toPos);
    }


    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int pos = viewHolder.getAdapterPosition();
        noteAdapter.notifyItemChanged(pos);
        itemMoveListener.onItemRemove(pos);

    }
}
