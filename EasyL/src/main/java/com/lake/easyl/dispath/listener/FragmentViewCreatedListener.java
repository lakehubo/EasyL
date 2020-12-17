package com.lake.easyl.dispath.listener;

import android.view.View;

/**
 * fragment view创建完成回调
 *
 * @author lake
 * create by 2020/10/27 2:49 PM
 */
public interface FragmentViewCreatedListener {
    void onViewCreated(View view, int key);
}
