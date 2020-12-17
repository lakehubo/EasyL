package com.lake.easyl.adapter;


import androidx.fragment.app.FragmentManager;

/**
 * fragment 适配器
 *
 * @author lake
 * create by 2020/10/28 10:41 AM
 */
public abstract class FragmentAdapter implements EasyAdapter{
    private final FragmentManager fragmentManager;

    public FragmentAdapter(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    public FragmentManager getFragmentManager(){
        return fragmentManager;
    }
}
