package com.lake.easyl.dispath;

import android.os.Bundle;

/**
 * 缓存栈存储的fragment历史记录以及附带的数据信息
 *
 * @author lake
 * create by 2020/10/27 5:28 PM
 */
public class FragmentCache {
    private int key;
    private Bundle bundle;

    public FragmentCache(int key, Bundle bundle) {
        this.key = key;
        this.bundle = bundle;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }
}
