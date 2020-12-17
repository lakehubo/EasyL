package com.lake.fragmentactivitycommit;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lake.easyl.EasyBaseFragment;

/**
 * @author lake
 * create by 2020/10/26 5:12 PM
 */
public class FragmentTest2 extends EasyBaseFragment {

    @Override
    public int onInitViewResId() {
        return R.layout.fragment_test;
    }

    @Override
    protected void initView(View view) {
        ((TextView) view.findViewById(R.id.fragment_text)).setText("test_2");
    }

    @Override
    public int fragmentKey() {
        return 1;
    }

    @Override
    public void bindBundle(Bundle bundle) {

    }

    @Override
    public void onClear() {

    }
}
