package com.example.six;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class vp2Activity extends AppCompatActivity {

    private ViewPager2 vp2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vp2);
        initVp2();
    }

    private void initVp2() {
        vp2 = findViewById(R.id.vp2);
        FragmentVpAdapter fragmentVpAdapter = new FragmentVpAdapter(this, getData2());
        vp2.setAdapter(fragmentVpAdapter);
        TabLayout tabLayout = findViewById(R.id.tab);
        new TabLayoutMediator(tabLayout, vp2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("页面1");
                        break;
                    case 1:
                        tab.setText("页面2");
                        break;
                    default:
                        tab.setText("页面3");
                }

            }
        }).attach();
    }

    private ArrayList<String> getData1() {
        ArrayList<String> data = new ArrayList<>();
        data.add("text1");
        data.add("text2");
        data.add("text3");
        data.add("text4");
        data.add("text5");
        return data;
    }

    private ArrayList<FragmentInterface> getData2() {
        ArrayList<FragmentInterface> fragList = new ArrayList<>();
        fragList.add(new FragmentInterface() {
            @Override
            public Fragment back() {
                return new FragmentFirst();
            }
        });

        fragList.add(new FragmentInterface() {
            @Override
            public Fragment back() {
                return new FragmentSecond();
            }
        });

        fragList.add(new FragmentInterface() {
            @Override
            public Fragment back() {
                return new FragmentThird();
            }
        });

        return fragList;
    }

}
