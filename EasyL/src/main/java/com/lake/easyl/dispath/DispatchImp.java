package com.lake.easyl.dispath;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * activity内部fragment页面管理类,封装FragmentManager
 *
 * @author lake
 * create by 2020/10/26 3:00 PM
 */
final class DispatchImp implements DispatchControl {

    /**
     * fragment 默认cache的数量大小
     */
    private static final int DEFAULT_PAGE_CACHE_SIZE = 16;

    private final FragmentManager fragmentManager;
    /**
     * fragment缓存（已创建并显示过的fragment）
     */
    private final Map<Integer, FragmentKey> fragmentMap;
    /**
     * fragment页面
     */
    private FragmentKey curFragmentKey;
    /**
     * 当前附带数据
     */
    private Bundle curBundle;
    /**
     * 历史页面显示的顺序，该缓存携带每次show时候传入的bundle信息
     */
    private final Stack<FragmentCache> backList = new Stack<>();
    /**
     * 装载fragment页面的容器id
     */
    private final int resId;
    /**
     * 页面来源
     */
    private List<Class<? extends FragmentKey>> sources;
    /**
     * fragment 各种状态的回调
     */
    private FragmentHiddenChangedListener fragmentHiddenChangedListener;
    private FragmentViewCreatedListener fragmentViewCreatedListener;
    private FragmentAttachedListener fragmentAttachedListener;
    private FragmentCreatedListener fragmentCreatedListener;
    private FragmentStartedListener fragmentStartedListener;
    private FragmentResumedListener fragmentResumedListener;
    private FragmentPausedListener fragmentPausedListener;
    private FragmentStoppedListener fragmentStoppedListener;
    private FragmentDetachedListener fragmentDetachedListener;
    private FragmentViewDestroyedListener fragmentViewDestroyedListener;
    private FragmentDestroyedListener fragmentDestroyedListener;

    DispatchImp(FragmentManager fragmentManager, int resId) {
        this(fragmentManager, resId, DEFAULT_PAGE_CACHE_SIZE);
    }

    DispatchImp(FragmentManager fragmentManager, int resId, int pageCacheSize) {
        this.fragmentManager = fragmentManager;
        this.resId = resId;
        int initialCapacity = (int) (pageCacheSize / 0.75f + 1);
        if (initialCapacity <= 1)
            throw new RuntimeException("the pageCacheSize must be bigger than 0!");
        if (initialCapacity > 1 << 30)
            throw new RuntimeException("the pageCacheSize must be smaller than " + pageCacheSize + "!");
        fragmentMap = new LinkedHashMap<Integer, FragmentKey>(initialCapacity) {
            private static final long serialVersionUID = 1L;

            @Override
            protected boolean removeEldestEntry(Entry<Integer, FragmentKey> eldest) {
                return size() > pageCacheSize;
            }
        };
        fragmentManager.registerFragmentLifecycleCallbacks(callbacks, false);//非递归调用
        fragmentManager.beginTransaction().disallowAddToBackStack().commit();
    }

    private FragmentManager.FragmentLifecycleCallbacks callbacks = new FragmentManager.FragmentLifecycleCallbacks() {
        @Override
        public void onFragmentAttached(@NonNull FragmentManager fm,
                                       @NonNull Fragment f,
                                       @NonNull Context context) {
            if (f instanceof FragmentKey) {
                FragmentKey fragmentKey = (FragmentKey) f;
                if (fragmentAttachedListener != null)
                    fragmentAttachedListener.onAttach(fragmentKey.fragmentKey());
            }
        }

        @Override
        public void onFragmentCreated(@NonNull FragmentManager fm,
                                      @NonNull Fragment f,
                                      @Nullable Bundle savedInstanceState) {
            if (f instanceof FragmentKey) {
                FragmentKey fragmentKey = (FragmentKey) f;
                if (fragmentCreatedListener != null)
                    fragmentCreatedListener.onCreate(fragmentKey.fragmentKey());
            }
        }

        @Override
        public void onFragmentViewCreated(@NonNull FragmentManager fm,
                                          @NonNull Fragment f,
                                          @NonNull View v,
                                          @Nullable Bundle savedInstanceState) {
            if (f instanceof FragmentKey) {
                FragmentKey fragmentKey = (FragmentKey) f;
                if (fragmentViewCreatedListener != null)
                    fragmentViewCreatedListener.onViewCreated(v, fragmentKey.fragmentKey());
            }
        }

        @Override
        public void onFragmentStarted(@NonNull FragmentManager fm,
                                      @NonNull Fragment f) {
            if (f instanceof FragmentKey) {
                FragmentKey fragmentKey = (FragmentKey) f;
                if (fragmentStartedListener != null)
                    fragmentStartedListener.onStart(fragmentKey.fragmentKey());
            }
        }

        @Override
        public void onFragmentResumed(@NonNull FragmentManager fm,
                                      @NonNull Fragment f) {
            if (f instanceof FragmentKey) {
                FragmentKey fragmentKey = (FragmentKey) f;
                if (fragmentResumedListener != null)
                    fragmentResumedListener.onResume(fragmentKey.fragmentKey());
            }
        }

        @Override
        public void onFragmentPaused(@NonNull FragmentManager fm,
                                     @NonNull Fragment f) {
            if (f instanceof FragmentKey) {
                FragmentKey fragmentKey = (FragmentKey) f;
                if (fragmentPausedListener != null)
                    fragmentPausedListener.onPause(fragmentKey.fragmentKey());
            }
        }

        @Override
        public void onFragmentStopped(@NonNull FragmentManager fm,
                                      @NonNull Fragment f) {
            if (f instanceof FragmentKey) {
                FragmentKey fragmentKey = (FragmentKey) f;
                if (fragmentStoppedListener != null)
                    fragmentStoppedListener.onStop(fragmentKey.fragmentKey());
            }
        }

        @Override
        public void onFragmentViewDestroyed(@NonNull FragmentManager fm,
                                            @NonNull Fragment f) {
            if (f instanceof FragmentKey) {
                FragmentKey fragmentKey = (FragmentKey) f;
                if (fragmentViewDestroyedListener != null)
                    fragmentViewDestroyedListener.onViewDestroyed(fragmentKey.fragmentKey());
            }
        }

        @Override
        public void onFragmentDestroyed(@NonNull FragmentManager fm,
                                        @NonNull Fragment f) {
            if (f instanceof FragmentKey) {
                FragmentKey fragmentKey = (FragmentKey) f;
                if (fragmentDestroyedListener != null)
                    fragmentDestroyedListener.onDestroy(fragmentKey.fragmentKey());
            }
        }

        @Override
        public void onFragmentDetached(@NonNull FragmentManager fm,
                                       @NonNull Fragment f) {
            if (f instanceof FragmentKey) {
                FragmentKey fragmentKey = (FragmentKey) f;
                if (fragmentDetachedListener != null)
                    fragmentDetachedListener.onDetach(fragmentKey.fragmentKey());
            }
        }
    };

    @Override
    public void setFragmentSource(List<Class<? extends FragmentKey>> pages) {
        sources = pages;
    }

    @Override
    public void showFragmentKey(int key, @Nullable Bundle bundle, CreateMode mode) {
        checkSource();
        showFragmentKeyIml(key, bundle, mode, false);
    }

    @Override
    public void showFragmentKey(int key, @Nullable Bundle bundle) {
        checkSource();
        showFragmentKeyIml(key, bundle, CreateMode.CLEAR, false);
    }

    @Override
    public void showFragmentKey(int key) {
        checkSource();
        showFragmentKeyIml(key, null, CreateMode.CLEAR, false);
    }

    @Override
    public int currentFragmentKey() {
        if (curFragmentKey == null)
            return -1;
        return curFragmentKey.fragmentKey();
    }

    /**
     * 显示对应key值的fragment
     *
     * @param key
     * @param bundle
     * @param mode
     */
    private void showFragmentKeyIml(int key, @Nullable Bundle bundle, CreateMode mode, boolean backTag) {
        if (curFragmentKey != null && curFragmentKey.fragmentKey() == key) {
            return;
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        FragmentKey oldTag = fragmentMap.get(key);
        if (curFragmentKey != null) {
            if (!backTag) {//非回退操作则进行入栈记录
                backList.push(new FragmentCache(curFragmentKey.fragmentKey(), curBundle));
            }
            transaction.hide(curFragmentKey.fragment());
            if (fragmentHiddenChangedListener != null)
                fragmentHiddenChangedListener.onHiddenChanged(true,
                        curFragmentKey.fragmentKey());
        }
        FragmentKey fragmentKey;
        if (oldTag != null) {
            fragmentKey = oldTag.fragment();
        } else {
            fragmentKey = createFragmentByKey(key, bundle);
            fragmentMap.put(key, fragmentKey.fragment());
            transaction.add(resId, fragmentKey.fragment());
        }
        transaction.show(fragmentKey.fragment());
        transaction.commit();
        if (bundle != null)
            fragmentKey.onBindBundle(bundle);
        if (fragmentHiddenChangedListener != null)
            fragmentHiddenChangedListener.onHiddenChanged(false,
                    fragmentKey.fragmentKey());
        curFragmentKey = fragmentKey.fragment();
        curBundle = bundle;
    }

    /**
     * 检测fragment 数据源不为空列表
     */
    private void checkSource() {
        if (sources == null || sources.size() == 0)
            throw new RuntimeException("please setFragmentSource first!" +
                    "it is necessary to show the fragment content");
    }

    /**
     * 根据key值创建fragment
     *
     * @param key
     */
    private FragmentKey createFragmentByKey(int key, @Nullable Bundle bundle) {
        for (Class<? extends FragmentKey> clazz : sources) {
            try {
                FragmentKey fragmentKey = clazz.newInstance();
                Method method = clazz.getMethod("fragmentKey");
                Object object = method.invoke(fragmentKey);
                if (object instanceof Integer) {
                    int classKey = (Integer) object;
                    if (classKey == -1)
                        throw new RuntimeException("the fragment key can not be -1!");
                    if (key == classKey) {
                        if (bundle != null)
                            fragmentKey.onBindBundle(bundle);
                        return fragmentKey;
                    }
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(clazz.getName() +
                        " class has not implements fragmentKey method!");
            } catch (InstantiationException e) {
                throw new RuntimeException(clazz.getName() +
                        " class can not create!please sure that constructor is public and no param");
            }
        }
        throw new RuntimeException("no fragment by this key!");
    }

    @Override
    public boolean backFragment() {
        if (backList.empty())
            return false;
        FragmentCache cache = backList.pop();
        Log.e("lake", "backFragment: " + cache.getKey());
        showFragmentKeyIml(cache.getKey(), cache.getBundle(), CreateMode.NO_CLEAR, true);
        return true;
    }

    @Override
    public boolean isFragmentCreated(int key) {
        return fragmentMap.containsKey(key);
    }

    @Override
    public void setFragmentHiddenChangedListener(FragmentHiddenChangedListener l) {
        fragmentHiddenChangedListener = l;
    }

    @Override
    public void setFragmentViewCreatedListener(FragmentViewCreatedListener l) {
        fragmentViewCreatedListener = l;
    }

    @Override
    public void setFragmentAttachedListener(FragmentAttachedListener l) {
        this.fragmentAttachedListener = l;
    }

    @Override
    public void setFragmentCreatedListener(FragmentCreatedListener l) {
        this.fragmentCreatedListener = l;
    }

    @Override
    public void setFragmentStartedListener(FragmentStartedListener l) {
        this.fragmentStartedListener = l;
    }

    @Override
    public void setFragmentResumedListener(FragmentResumedListener l) {
        this.fragmentResumedListener = l;
    }

    @Override
    public void setFragmentPausedListener(FragmentPausedListener l) {
        this.fragmentPausedListener = l;
    }

    @Override
    public void setFragmentStoppedListener(FragmentStoppedListener l) {
        this.fragmentStoppedListener = l;
    }

    @Override
    public void setFragmentDetachedListener(FragmentDetachedListener l) {
        this.fragmentDetachedListener = l;
    }

    @Override
    public void setFragmentViewDestroyedListener(FragmentViewDestroyedListener l) {
        this.fragmentViewDestroyedListener = l;
    }

    @Override
    public void setFragmentDestroyedListener(FragmentDestroyedListener l) {
        this.fragmentDestroyedListener = l;
    }

    @Override
    public void releaseControl() {
        fragmentManager.unregisterFragmentLifecycleCallbacks(callbacks);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for (FragmentKey fragmentKey : fragmentMap.values())
            transaction.remove(fragmentKey.fragment());
        transaction.commit();
        fragmentMap.clear();
        sources.clear();
        sources = null;
        curFragmentKey = null;
    }
}
