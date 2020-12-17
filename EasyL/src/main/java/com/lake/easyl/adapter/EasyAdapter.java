package com.lake.easyl.adapter;

import androidx.fragment.app.FragmentManager;

import com.lake.easyl.dispath.FragmentKey;

/**
 * fragment适配器接口
 * @author lake
 * create by 2020/10/28 10:46 AM
 */
public interface EasyAdapter {
    /**
     * fragment数量
     * @return
     */
    int getCount();

    /**
     * 获取fragment对象
     * @param position
     * @return
     */
    FragmentKey getItem(int position);

    /**
     * 设置fragment的显示容器id
     * @param position
     * @return
     */
    int getItemViewParentResId(int position);

    /**
     * 设置显示容器数量
     * @return
     */
    int getViewParentCount();

    /**
     * 设置FragmentManager
     * @return
     */
    FragmentManager getFragmentManager();
}
