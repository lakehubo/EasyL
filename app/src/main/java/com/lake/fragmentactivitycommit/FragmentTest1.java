package com.lake.fragmentactivitycommit;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lake.easyl.EasyBaseFragment;

/**
 * @author lake
 * create by 2020/10/26 5:12 PM
 */
public class FragmentTest1 extends EasyBaseFragment {
    private TextView textView;

    @Override
    public int onInitViewResId() {
        return R.layout.fragment_test;
    }

    @Override
    protected void initView(View view) {
        textView = view.findViewById(R.id.fragment_text);
        textView.setText("test_1");
    }

    @Override
    public int fragmentKey() {
        return 0;
    }

    @Override
    public void bindBundle(Bundle bundle) {
        String test = bundle.getString("test");
        if (!TextUtils.isEmpty(test))
            textView.setText(test);
    }

    @Override
    public void onClear() {
        textView.setText("test_1");
    }
}
