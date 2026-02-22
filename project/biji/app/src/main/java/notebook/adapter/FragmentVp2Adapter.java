/**
 * description: 用于vp2的适配器类
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/2/21
 */

package notebook.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class FragmentVp2Adapter extends FragmentStateAdapter {


    private final ArrayList<FragmentInterface> fragments;

    public FragmentVp2Adapter(@NonNull FragmentActivity fragmentActivity, ArrayList<FragmentInterface> fragments) {
        super(fragmentActivity);
        this.fragments = fragments;
    }


    @NonNull
    @Override
    //从数据源拿数据
    public Fragment createFragment(int position) {
        return fragments.get(position).back();
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}

