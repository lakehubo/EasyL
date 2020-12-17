package com.lake.easyl.dispath;

import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import com.lake.easyl.adapter.EasyAdapter;
import com.lake.easyl.dispath.listener.FragmentAttachedListener;
import com.lake.easyl.dispath.listener.FragmentCreatedListener;
import com.lake.easyl.dispath.listener.FragmentDestroyedListener;
import com.lake.easyl.dispath.listener.FragmentDetachedListener;
import com.lake.easyl.dispath.listener.FragmentHiddenChangedListener;
import com.lake.easyl.dispath.listener.FragmentPausedListener;
import com.lake.easyl.dispath.listener.FragmentResumedListener;
import com.lake.easyl.dispath.listener.FragmentStartedListener;
import com.lake.easyl.dispath.listener.FragmentStoppedListener;
import com.lake.easyl.dispath.listener.FragmentViewCreatedListener;
import com.lake.easyl.dispath.listener.FragmentViewDestroyedListener;

import java.util.List;

/**
 * activity 分发数据或者请求 fragment接口
 *
 * @author lake
 * create by 2020/10/26 2:50 PM
 */
public interface DispatchControl {
    /**
     * 显示指定key的fragment页面，携带初始化数据，指定clear模式
     */
    void showFragmentKey(int key, Bundle bundle, CreateMode mode);

    /**
     * 显示指定key的fragment页面，携带初始化数据，clear模式
     */
    void showFragmentKey(int key, Bundle bundle);

    /**
     * 显示指定key的fragment页面，无初始化数据，clear模式
     */
    void showFragmentKey(int key);

    /**
     * 回退fragment页面
     *
     * @return false表示无缓存的页面栈
     */
    boolean backFragment();

    /**
     * 指定key的fragment是否已经创建
     *
     * @param key
     * @return
     */
    boolean isFragmentCreated(int key);

    /**
     * 设置fragment页面集合
     *
     * @param pages
     * @return
     */
    void setFragmentSource(List<Class<? extends FragmentKey>> pages);

    /**
     * 当前显示的fragment的key值
     *
     * @return
     */
    int currentFragmentKey();

    /**
     * fragment 隐藏显示状态回调
     */
    void setFragmentHiddenChangedListener(FragmentHiddenChangedListener l);

    /**
     * fragment view 创建完成后的回调函数
     */
    void setFragmentViewCreatedListener(FragmentViewCreatedListener l);

    /**
     * fragment attach回调
     *
     * @param l
     */
    void setFragmentAttachedListener(FragmentAttachedListener l);

    /**
     * fragment Created回调
     *
     * @param l
     */
    void setFragmentCreatedListener(FragmentCreatedListener l);

    /**
     * fragment Start回调
     *
     * @param l
     */
    void setFragmentStartedListener(FragmentStartedListener l);

    /**
     * fragment Resume回调
     *
     * @param l
     */
    void setFragmentResumedListener(FragmentResumedListener l);

    /**
     * fragment Pause回调
     *
     * @param l
     */
    void setFragmentPausedListener(FragmentPausedListener l);

    /**
     * fragment Stop回调
     *
     * @param l
     */
    void setFragmentStoppedListener(FragmentStoppedListener l);

    /**
     * fragment Detached回调
     *
     * @param l
     */
    void setFragmentDetachedListener(FragmentDetachedListener l);

    /**
     * fragment ViewDestroyed回调
     *
     * @param l
     */
    void setFragmentViewDestroyedListener(FragmentViewDestroyedListener l);

    /**
     * fragment Destroyed 回调
     *
     * @param l
     */
    void setFragmentDestroyedListener(FragmentDestroyedListener l);

    /**
     * 创建接口
     *
     * @param fragmentManager
     * @param resId           fragment容器id
     * @return
     */
    static DispatchControl newControl(FragmentManager fragmentManager, int resId) {
        return new DispatchImp(fragmentManager, resId);
    }

    /**
     * 创建接口
     *
     * @param fragmentManager
     * @param resId           fragment容器id
     * @param cacheSize       fragment缓存页数
     * @return
     */
    static DispatchControl newControl(FragmentManager fragmentManager, int resId, int cacheSize) {
        return new DispatchImp(fragmentManager, resId, cacheSize);
    }

    /**
     * 释放资源
     */
    void releaseControl();
}
