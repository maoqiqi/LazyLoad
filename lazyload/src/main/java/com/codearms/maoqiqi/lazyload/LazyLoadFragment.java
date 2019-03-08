package com.codearms.maoqiqi.lazyload;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment基类,封装了懒加载的实现
 * Author: fengqi.mao.march@gmail.com
 * Date: 2019/3/4 17:42
 */
public abstract class LazyLoadFragment extends Fragment {

    /**
     * TAG
     */
    private final String TAG = getClass().getSimpleName();

    /**
     * 上下文
     */
    protected Context context;

    /**
     * 根布局
     */
    protected View rootView;

    /**
     * 是否启用View复用,默认开启。
     */
    private boolean isReuse = true;

    /**
     * Fragment是否可见
     */
    private boolean isVisible = false;

    /**
     * 视图是否创建完成
     */
    private boolean isViewCreated = false;

    /**
     * 数据是否已加载完毕
     */
    private boolean isLoadDataCompleted = false;

    /**
     * 是否每次切换Fragment强制刷新数据
     */
    private boolean isForcedToRefresh = false;

    // Fragment的可见状态发生变化时会被调用.
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG, "-->setUserVisibleHint(boolean isVisibleToUser) = " + isVisibleToUser);
        if (isViewCreated) {
            if (isVisible != isVisibleToUser) {
                isVisible = isVisibleToUser;
                onVisibleChange(isVisible);
            }
            if (isVisible && (!isLoadDataCompleted || isForcedToRefresh)) {
                isLoadDataCompleted = true;
                loadData();
            }
        }
    }

    // 该方法只在我们直接用标签在布局中定义的时候才会被调用
    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
        Log.d(TAG, "-->onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState)");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "-->onAttach(Context context)");
        this.context = context;
    }

    // 系统会在创建片段时调用此方法,只会调用一次。
    // 关于变量的初始化和赋值操作可以在onCreate()里进行,这样就可以避免重复的操作。
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "-->onCreate(@Nullable Bundle savedInstanceState)");
    }

    // 支持view的复用,防止与ViewPager使用时出现重复创建view的问题.可以设置不复用。
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.d(TAG, "-->onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)");
        if (!isReuse || rootView == null) {
            Log.d(TAG, "-->创建View");
            rootView = createView(inflater, container);
            initViews(savedInstanceState);
        } else {
            Log.d(TAG, "-->复用View");
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "-->onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)");
        isViewCreated = true;
    }

    // 当Fragment所在的Activity启动完成后回调.
    // 如果是创建时,setUserVisibleHint()在onCreateView()前调用,那么就等到onViewCreated()后才回调onVisibleChange(true)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "-->onActivityCreated(@Nullable Bundle savedInstanceState)");
        if (getUserVisibleHint()) {
            if (!isVisible) {
                isVisible = true;
                onVisibleChange(true);
            }
            if (!isLoadDataCompleted || isForcedToRefresh) {
                isLoadDataCompleted = true;
                loadData();
            }
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d(TAG, "-->onViewStateRestored(@Nullable Bundle savedInstanceState)");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "-->onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "-->onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "-->onPause()");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "-->onSaveInstanceState(@NonNull Bundle outState)");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "-->onStop()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "-->onDestroyView()");
        isViewCreated = false;
        if (!isReuse) rootView = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "-->onDestroy()");
        rootView = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "-->onDetach()");
        context = null;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "-->onConfigurationChanged(Configuration newConfig)");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d(TAG, "-->onHiddenChanged(boolean hidden) = " + hidden);
    }

    /**
     * 创建视图
     *
     * @param inflater  实例化MXL布局文件对象
     * @param container 父容器
     * @return 视图
     */
    protected abstract View createView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container);

    /**
     * 初始化控件
     *
     * @param savedInstanceState 保存实例状态
     */
    protected void initViews(@Nullable Bundle savedInstanceState) {

    }

    /**
     * 提供Fragment可见与不可见时回调,并可以在该函数内进行一些ui操作,如显示/隐藏加载框,不会报null异常。
     *
     * @param isVisible true:不可见 -> 可见 false:可见  -> 不可见
     */
    protected void onVisibleChange(boolean isVisible) {
        Log.d(TAG, "-->onVisibleChange(boolean isVisible) = " + isVisible);
    }

    /***
     * 支持数据懒加载并且只加载一次
     */
    protected void loadData() {
        Log.d(TAG, "-->loadData()");
    }

    /**
     * 设置是否需要复用View
     *
     * @param reuse true:复用 false:不复用
     */
    public void setReuse(boolean reuse) {
        isReuse = reuse;
    }

    /**
     * 设置是否每次切换Fragment强制刷新数据
     *
     * @param forcedToRefresh true:是 false:不是
     */
    public void setForcedToRefresh(boolean forcedToRefresh) {
        isForcedToRefresh = forcedToRefresh;
    }
}