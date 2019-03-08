package com.codearms.maoqiqi.lazyload.simple;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * 主页
 * Author: fengqi.mao.march@gmail.com
 * Date: 2019/3/5 11:00
 */
public class MainActivity extends BaseActivity {

    private int[] rbIds = {R.id.rb_1, R.id.rb_2, R.id.rb_3, R.id.rb_4};

    private ViewPager viewPager;
    private RadioGroup radioGroup;

    // 选中Fragment对应的位置
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 去掉toolbar阴影效果
        if (getSupportActionBar() != null) getSupportActionBar().setElevation(0);

        viewPager = findViewById(R.id.view_pager);
        radioGroup = findViewById(R.id.radio_group);

        if (savedInstanceState != null) {
            position = savedInstanceState.getInt("position", 0);
        }

        // 设置ViewPager
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(rbIds.length - 1);
        viewPager.setCurrentItem(position);
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());

        // 设置默认选中常用框架
        radioGroup.check(rbIds[position]);
        // 设置RadioGroup的监听
        radioGroup.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // 保存当前选中索引
        outState.putInt("position", position);
    }

    private final class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        // FragmentPagerAdapter内部已经做了缓存
        @Override
        public Fragment getItem(int position) {
            Log.d("MyFragment" + (position + 1), "getItem(int position), position = " + position);
            return MyFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return rbIds.length;
        }

        // 不用重写,这里是需要打印日志。
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            Object o = super.instantiateItem(container, position);
            Log.d("MyFragment" + (position + 1), "instantiateItem()->Fragment->hashCode():" + o.hashCode());
            return o;
        }
    }

    private final class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Log.d("info", "onPageSelected(),position=" + position);
            // RadioGroup的OnCheckedChangeListener里的onCheckedChanged(RadioGroup group, int checkedId)会被调用三次
            // radioGroup.check(rbIds[position]);
            // 只调用一次onCheckedChanged(RadioGroup group, int checkedId)
            ((RadioButton) radioGroup.getChildAt(position)).setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private final class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_1:
                    position = 0;
                    break;
                case R.id.rb_2:
                    position = 1;
                    break;
                case R.id.rb_3:
                    position = 2;
                    break;
                case R.id.rb_4:
                    position = 3;
                    break;
            }
            Log.d("info", "onCheckedChanged(),checkedId=" + checkedId + ",position=" + position);
            viewPager.setCurrentItem(position);
        }
    }
}