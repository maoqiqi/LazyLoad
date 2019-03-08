package com.codearms.maoqiqi.lazyload.simple;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;

/**
 * 基类Activity
 * Author: fengqi.mao.march@gmail.com
 * Date: 2019/3/5 10:00
 */
public class BaseActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    protected int getNumber() {
        return 1;
    }

    // The activity is being created.
    // 您应该在此方法中执行所有正常的静态设置—创建视图、将数据绑定到列表等等.始终后接onStart().
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG + getNumber(), "-->onCreate(@Nullable Bundle savedInstanceState)");
        Log.e("configuration", getResources().getConfiguration().toString());
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        Log.d(TAG + getNumber(), "-->onAttachFragment(Fragment fragment) = " + fragment.getClass().getName());
    }

    // 在Activity已停止并即将再次启动前调用.始终后接onStart().
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG + getNumber(), "-->onRestart()");
    }

    // The activity is about to become visible.
    // 如果Activity转入前台,则后接onResume(),如果Activity转入隐藏状态,则后接onStop().
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG + getNumber(), "-->onStart()");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG + getNumber(), "-->onRestoreInstanceState(Bundle savedInstanceState)");
    }

    // The activity has become visible (it is now "resumed").
    // 此时,Activity处于Activity堆栈的顶层,并具有用户输入焦点.始终后接onPause().
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG + getNumber(), "-->onResume()");
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.d(TAG + getNumber(), "-->onAttachedToWindow()");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG + getNumber(), "-->onCreateOptionsMenu(Menu menu)");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d(TAG + getNumber(), "-->onPrepareOptionsMenu(Menu menu)");
        return super.onPrepareOptionsMenu(menu);
    }

    // Another activity is taking focus (this activity is about to be "paused").
    // 此方法通常用于确认对持久性数据的未保存更改、停止动画以及其他可能消耗CPU的内容.
    // 它应该非常迅速地执行所需操作,因为它返回后,下一个Activity才能继续执行.
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG + getNumber(), "-->onPause()");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG + getNumber(), "-->onSaveInstanceState(Bundle outState)");
    }

    // The activity is no longer visible (it is now "stopped")
    // 如果Activity被销毁,或另一个Activity(一个现有Activity或新Activity)继续执行并将其覆盖,就可能发生这种情况.
    // 如果Activity恢复与用户的交互,则后接onRestart(),如果Activity被销毁,则后接onDestroy().
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG + getNumber(), "-->onStop()");
    }

    // The activity is about to be destroyed.
    // 当Activity结束(有人对Activity调用了finish()),或系统为节省空间而暂时销毁该Activity 实例时,可能会调用它.
    // 您可以通过isFinishing()方法区分这两种情形.
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG + getNumber(), "-->onDestroy()");
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d(TAG + getNumber(), "-->onDetachedFromWindow()");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG + getNumber(), "-->onConfigurationChanged(Configuration newConfig)");
        Log.e("configuration", newConfig.toString());
    }
}