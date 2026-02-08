/**
 * description: 使callBack只负责监听，具体操作在adapter中实现
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/2/8
 */

package notebook.helper;
public interface ItemMoveListener {
    boolean onItemMove(int fromPos,int toPos);
    void onItemRemove(int pos);
}


