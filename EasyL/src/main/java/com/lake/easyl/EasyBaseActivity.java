package com.lake.easyl;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.lake.easyl.adapter.EasyAdapter;
import com.lake.easyl.adapter.FragmentAdapter;
import com.lake.easyl.dispath.DispatchControl;
import com.lake.easyl.dispath.FragmentKey;

import java.util.List;

/**
 * activity 基类
 *
 * @author lake
 * create by 2020/10/26 2:45 PM
 */
public abstract class EasyBaseActivity extends AppCompatActivity {
    /**
     * 初始化装载fragment内容的容器id
     *
     * @return
     */
    protected abstract int onInitFragmentContentResId();

    /**
     * 初始化装载activity view的id
     *
     * @return
     */
    protected abstract int onInitCreateViewLayoutId();

    /**
     * activity 与 fragment交互的接口实现类
     */
    protected DispatchControl pageControl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(onInitCreateViewLayoutId());
        pageControl = DispatchControl.newControl(getSupportFragmentManager()
                , onInitFragmentContentResId());
    }

    @Override
    public void onBackPressed() {
        if (!pageControl.backFragment())
            super.onBackPressed();
    }

    @Override
    protected void onStop() {
        pageControl.releaseControl();
        pageControl = null;
        super.onStop();
    }
}
