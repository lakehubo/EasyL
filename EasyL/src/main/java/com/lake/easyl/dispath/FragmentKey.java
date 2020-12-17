package com.lake.easyl.dispath;

import android.os.Bundle;

import com.lake.easyl.EasyBaseFragment;

/**
 * fragment key
 *
 * @author lake
 * create by 2020/10/26 2:57 PM
 */
public interface FragmentKey {
    /**
     * fragment 页面标记
     *
     * @return
     */
    int fragmentKey();

    /**
     * fragment 对象
     *
     * @return
     */
    EasyBaseFragment fragment();

    /**
     * 带有数据的初始化
     *
     * @param bundle
     */
    void onBindBundle(Bundle bundle);

    /**
     * 清除数据时候的操作
     */
    void onClear();
}
