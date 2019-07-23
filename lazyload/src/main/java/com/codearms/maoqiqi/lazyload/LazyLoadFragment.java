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
     * TAG(支持自定义)
     */
    private String tag = getClass().getSimpleName();

    /**
     * 上下文
     */
    protected Context context;

    /**
     * 根布局
     */
    protected View rootView;

    /**
     * 视图是否创建完成
     */
    private boolean isViewCreated = false;

    /**
     * 数据是否已加载完毕
     */
    private boolean isLoadDataCompleted = false;

    /**
     * 是否启用View复用,默认开启。
     */
    private boolean isReuse = true;

    /**
     * 是否每次切换Fragment强制刷新数据
     */
    private boolean isForcedToRefresh = false;

    // Fragment的可见状态发生变化时被调用.
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(tag, "-->setUserVisibleHint(boolean isVisibleToUser) = " + isVisibleToUser);
        if (isViewCreated) {
            onVisibleChange(isVisibleToUser);
            if (isVisibleToUser && (!isLoadDataCompleted || isForcedToRefresh)) {
                loadData();
            }
        }
    }

    // 该方法只在我们直接用标签在布局中定义的时候才会被调用
    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
        Log.d(tag, "-->onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState)");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(tag, "-->onAttach(Context context)");
        this.context = context;
    }

    // 系统会在创建片段时调用此方法,只会调用一次。
    // 关于变量的初始化和赋值操作可以在onCreate()里进行,这样就可以避免重复的操作。
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(tag, "-->onCreate(@Nullable Bundle savedInstanceState)");
        if (savedInstanceState != null) {
            isLoadDataCompleted = savedInstanceState.getBoolean("isLoadDataCompleted", false);
            isReuse = savedInstanceState.getBoolean("isReuse", true);
            isForcedToRefresh = savedInstanceState.getBoolean("isForcedToRefresh", false);
            Log.d(tag, "isLoadDataCompleted = " + isLoadDataCompleted + ", isReuse = " + isReuse + ", isForcedToRefresh = " + isForcedToRefresh);
        }
    }

    // 支持view的复用,防止与ViewPager使用时出现重复创建view的问题.可以设置不复用。
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.d(tag, "-->onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)");
        if (!isReuse || rootView == null) {
            Log.d(tag, "-->创建View");
            rootView = createView(inflater, container);
            initViews(savedInstanceState);
        } else {
            Log.d(tag, "-->复用View");
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(tag, "-->onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)");
        isViewCreated = true;
    }

    // 当Fragment所在的Activity启动完成后回调.
    // 如果是创建时,setUserVisibleHint()在onCreateView()前调用,那么就等到onViewCreated()后才回调onVisibleChange(true)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(tag, "-->onActivityCreated(@Nullable Bundle savedInstanceState)");
        if (getUserVisibleHint()) {
            onVisibleChange(true);
            if (!isLoadDataCompleted || isForcedToRefresh) {
                loadData();
            }
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d(tag, "-->onViewStateRestored(@Nullable Bundle savedInstanceState)");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(tag, "-->onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(tag, "-->onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(tag, "-->onPause()");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(tag, "-->onSaveInstanceState(@NonNull Bundle outState)");
        Log.d(tag, "isLoadDataCompleted = " + isLoadDataCompleted + ", isReuse = " + isReuse + ", isForcedToRefresh = " + isForcedToRefresh);
        outState.putBoolean("isLoadDataCompleted", isLoadDataCompleted);
        outState.putBoolean("isReuse", isReuse);
        outState.putBoolean("isForcedToRefresh", isForcedToRefresh);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(tag, "-->onStop()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(tag, "-->onDestroyView()");
        isViewCreated = false;
        rootView = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(tag, "-->onDestroy()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(tag, "-->onDetach()");
        context = null;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(tag, "-->onConfigurationChanged(Configuration newConfig)");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d(tag, "-->onHiddenChanged(boolean hidden) = " + hidden);
    }

    /**
     * 设置日志TAG
     *
     * @param tag tag
     */
    public void setTag(String tag) {
        if (tag != null && !tag.equals("")) this.tag = tag;
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
     * 初始化控件。有些初始化必须在onCreateView中,例如setAdapter.否则,会弹出'No adapter attached; skipping layout'。
     *
     * @param savedInstanceState 保存实例状态
     */
    protected void initViews(@Nullable Bundle savedInstanceState) {

    }

    /**
     * 提供Fragment可见与不可见时回调,并可以在该函数内进行一些ui操作,如显示/隐藏加载框,不会报null异常。
     *
     * @param isVisible true:可见 false:不可见
     */
    protected void onVisibleChange(boolean isVisible) {
        Log.d(tag, "-->onVisibleChange(boolean isVisible) = " + isVisible);
    }

    /***
     * 支持数据懒加载并且只加载一次
     */
    protected void loadData() {
        Log.d(tag, "-->loadData()");
    }

    /**
     * 数据加载成功调用,不调用每次打开Fragment都会重新去加载数据
     */
    public void loadDataCompleted() {
        isLoadDataCompleted = true;
    }

    /**
     * 数据是否加载成功
     *
     * @return true:成功 false:失败
     */
    public boolean isLoadDataCompleted() {
        return isLoadDataCompleted;
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