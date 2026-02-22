/**
 * description: vp2
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/2/21
 */

package notebook.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.biji.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

import notebook.adapter.FragmentInterface;
import notebook.adapter.FragmentVp2Adapter;
import notebook.entity.Note;

public class NoteVp2Activity extends AppCompatActivity {

    ArrayList<FragmentInterface> fragmentList;
    FragmentVp2Adapter adapter;
    ViewPager2 vp2;
    TabLayout tabLayout;
    Note mNote;
    Bundle bundle;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vp2_note);

        initData();

        if (mNote == null) {
            setTab();
        }
    }

    private void setTab() {
        new TabLayoutMediator(tabLayout, vp2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int i) {
                if (i == 0) {
                    tab.setText("笔记");
                } else {
                    tab.setText("分组");
                }
            }
        }).attach();
    }

    private void initData() {
        showActionBar();
        vp2 = findViewById(R.id.note_vp2_fragment);
        tabLayout = findViewById(R.id.note_tab);

        Intent intent = getIntent();
        userId = intent.getIntExtra("user_id", -1);
        mNote = (Note) intent.getSerializableExtra("note");
        Log.d("TAG","(vp2:mNote)-->>" + mNote);
        bundle = new Bundle();
        bundle.putInt("user_id", userId);

        fragmentList = new ArrayList<>();
        if (mNote != null) {
            bundle.putSerializable("note", mNote);
            onlyGroupFragment();
        } else {
            towFragments();
        }

        adapter = new FragmentVp2Adapter(this, fragmentList);

        vp2.setAdapter(adapter);
    }

    private void towFragments() {
        fragmentList.add(new FragmentInterface() {
            @Override
            public Fragment back() {
                NoteItemActivity fragment = new NoteItemActivity();
                fragment.setArguments(bundle);
                return fragment;
            }
        });

        fragmentList.add(new FragmentInterface() {
            @Override
            public Fragment back() {
                NoteGroupActivity fragment = new NoteGroupActivity();
                fragment.setArguments(bundle);
                return fragment;
            }
        });
    }

    private void onlyGroupFragment() {
        fragmentList.add(new FragmentInterface() {
            @Override
            public Fragment back() {
                NoteGroupActivity fragment = new NoteGroupActivity();
                fragment.setArguments(bundle);
                return fragment;
            }
        });
    }

    private void showActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}