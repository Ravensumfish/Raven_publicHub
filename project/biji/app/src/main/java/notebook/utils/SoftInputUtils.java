/**
 * description: 监听软键盘弹出变化，精确目标控件位置
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/2/17
 */

package notebook.utils;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import androidx.core.view.ViewCompat;

public class SoftInputUtils {
    private View rootView, objView;
    private int softInputHeight = 0;
    int navigationBarHeight = 0;
    private boolean isSoftInputShow_pre = false;
    private boolean isNavigationBarShow = false;
    private boolean isSoftInputHeightChanged = false;
    private isSoftInputChanged listener;

    //监听软键盘变化
    public interface isSoftInputChanged {
        void onChanged(boolean isSoftInputShow, int softInputHeight, int viewOffset);
    }

    //为目标View绑定软键盘监听
    public void attachSI(View objView, isSoftInputChanged listener) {
        //先执行判空操作，若空则直接返回
        if (objView == null || listener == null) return;
        rootView = objView.getRootView();
        if (rootView == null) return;

        this.objView = objView;
        this.listener = listener;

        rootView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                int rootHeight = rootView.getHeight();
                navigationBarHeight = getNavigationBarHeight(objView.getContext());
                //获取可视范围
                Rect rect = new Rect();
                rootView.getWindowVisibleDisplayFrame(rect);

                if (rootHeight - rect.bottom == navigationBarHeight) {
                    isNavigationBarShow = true;
                }

                //计算高度，判断是否弹出，以及是否变化
                int multiHeight = isNavigationBarShow ? navigationBarHeight : 0;
                int softInputHeight = rootHeight - rect.bottom - multiHeight;
                boolean isSoftInputShow_aft = false;
                if (softInputHeight > 0) {
                    isSoftInputShow_aft = true;
                    if (SoftInputUtils.this.softInputHeight != softInputHeight) {
                        isSoftInputHeightChanged = true;
                        SoftInputUtils.this.softInputHeight = softInputHeight;
                    }
                }
                Log.d("TAG","(SIutils:SIHeight)-->>" + softInputHeight);

                int[] location = new int[2];
                objView.getLocationOnScreen(location);

                //减少不必要回调,若前后键盘状态都为弹出，则在键盘高度因输入法切换改变时进行回调
                if (isSoftInputShow_aft != isSoftInputShow_pre || (isSoftInputShow_aft && isSoftInputHeightChanged)) {

                    listener.onChanged(isSoftInputShow_aft, softInputHeight, location[1] + objView.getHeight());

                }

                //最后更新状态
                isSoftInputShow_pre = isSoftInputShow_aft;

            }
        });

    }

    public static int getNavigationBarHeight(Context context) {
        if (context != null) {

            int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");

            if (resourceId > 0) {
                return context.getResources().getDimensionPixelSize(resourceId);
            }
        }

        return 0;

    }

}
