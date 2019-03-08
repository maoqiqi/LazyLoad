package com.codearms.maoqiqi.lazyload.simple;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codearms.maoqiqi.lazyload.LazyLoadFragment;

import java.lang.ref.WeakReference;
import java.util.Random;

/**
 * MyFragment
 * Author: fengqi.mao.march@gmail.com
 * Date: 2019/3/5 11:22
 */
public class MyFragment extends LazyLoadFragment {

    private int position;
    private int color;
    private String[] names;

    private TextView tvName;
    private ProgressBar progressBar;

    private MyHandler handler;
    // 0:还没有加载数据,1:正在加载数据...,2:加载数据了但是没有成功,3:加载数据成功
    private int status;

    /**
     * Use this factory method to create a new instance of this fragment using the provided parameters.
     *
     * @param position position
     * @return A new instance of fragment MyFragment.
     */
    public static MyFragment newInstance(int position) {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            position = getArguments().getInt("position", 0);
        }
        setTag(getClass().getSimpleName() + (position + 1));
        names = new String[]{getString(R.string.index_1), getString(R.string.index_2), getString(R.string.index_3), getString(R.string.index_4)};
        // 随机颜色
        Random random = new Random();
        color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        handler = new MyHandler(this);
        if (savedInstanceState != null) {
            status = savedInstanceState.getInt("status", 0);
        }
    }

    @Override
    protected View createView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return inflater.inflate(R.layout.fragment_my, container, false);
    }

    @Override
    protected void initViews(@Nullable Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        rootView.findViewById(R.id.layout).setBackgroundColor(color);
        tvName = rootView.findViewById(R.id.tv_name);
        progressBar = rootView.findViewById(R.id.progress_bar);
    }

    @Override
    protected void onVisibleChange(boolean isVisible) {
        super.onVisibleChange(isVisible);
        Log.d("Fragment" + +(position + 1), "status = " + status);
        if (isVisible) {
            loadData();
        } else {
            if (status == 1) {
                Log.d("Fragment" + +(position + 1), "取消加载");
                status = 2;
                handler.removeCallbacksAndMessages(null);
            }
        }
    }

    @Override
    protected void loadData() {
        super.loadData();
        if (status == 0) {
            status = 1;
            tvName.setText(names[position]);
            handler.sendEmptyMessageDelayed(1, 5000);
        } else if (status == 2) {
            status = 1;
            tvName.setText("加载失败,重新加载...");
            handler.sendEmptyMessageDelayed(1, 5000);
        } else if (status == 3) {
            tvName.setText(String.format("%s,加载完成!", names[position]));
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (status == 1) {
            Log.d("Fragment" + +(position + 1), "取消加载");
            status = 2;
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("status", status);
    }

    private static final class MyHandler extends Handler {

        private WeakReference<MyFragment> weakReference;

        MyHandler(MyFragment fragment) {
            super(fragment.getActivity().getMainLooper());
            this.weakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (weakReference.get() != null) {
                MyFragment fragment = weakReference.get();
                Log.d("Fragment" + (fragment.position + 1), "加载完成!");
                fragment.status = 3;
                fragment.tvName.setText(String.format("%s,加载完成!", fragment.names[fragment.position]));
                fragment.progressBar.setVisibility(View.GONE);
            }
        }
    }
}