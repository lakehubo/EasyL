package com.lake.fragmentactivitycommit;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.lake.easyl.EasyBaseActivity;
import com.lake.easyl.dispath.FragmentKey;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends EasyBaseActivity {

    @Override
    protected int onInitFragmentContentResId() {
        return R.id.fragment_content;
    }

    @Override
    protected int onInitCreateViewLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<Class<? extends FragmentKey>> source = new ArrayList<>();
        source.add(FragmentTest1.class);
        source.add(FragmentTest2.class);
        source.add(FragmentTest3.class);

        pageControl.setFragmentSource(source);
        Bundle bundle = new Bundle();
        bundle.putString("test", "测试1");
        pageControl.showFragmentKey(0, bundle);

        findViewById(R.id.back).setOnClickListener(v -> {
            pageControl.backFragment();
        });

        findViewById(R.id.next).setOnClickListener(v -> {
            int curKey = pageControl.currentFragmentKey();
            pageControl.showFragmentKey((curKey + 1) > 2 ? 0 : (curKey + 1));
        });

        EditText editText = findViewById(R.id.key);

        findViewById(R.id.show_key).setOnClickListener(v -> {
            String keyStr = editText.getText().toString();
            if (!TextUtils.isEmpty(keyStr)) {
                int key = Integer.parseInt(keyStr);
                pageControl.showFragmentKey(key);
            }
        });

        pageControl.setFragmentViewCreatedListener((view, key) -> {
            Log.e("lake", "ViewCreated: " + key);
        });

        pageControl.setFragmentHiddenChangedListener((hidden, key) -> {
            Log.e("lake", key + "-hidden: " + hidden);
        });
    }
}