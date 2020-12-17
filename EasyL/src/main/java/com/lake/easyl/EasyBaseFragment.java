package com.lake.easyl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lake.easyl.dispath.FragmentKey;

/**
 * fragment 基类
 *
 * @author lake
 * create by 2020/10/26 2:46 PM
 */
public abstract class EasyBaseFragment extends Fragment implements FragmentKey {
    public abstract int onInitViewResId();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(onInitViewResId(), container, false);
        initView(view);
        Bundle bundle;
        if ((bundle = getArguments()) != null)
            bindBundle(bundle);
        return view;
    }

    @Override
    public EasyBaseFragment fragment() {
        return this;
    }

    @Override
    public void onBindBundle(Bundle bundle) {
        if (!isResumed()) {
            setArguments(bundle);
        } else {
            bindBundle(bundle);
        }
    }

    protected void bindBundle(Bundle bundle) {
    }

    protected void initView(View view) {
    }
}
