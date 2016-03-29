package com.example.studyhelper.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import com.example.studyhelper.application.StudyHelperApplication;
import com.example.studyhelper.http.NetRequest;


public abstract class BaseActivity extends ActionBarActivity {
    /**
     * 配置文件操作
     */
    protected SharedPreferences spUtil;
    protected SharedPreferences.Editor editor;
    protected ProgressDialog progressDialog;
    protected StudyHelperApplication application=((StudyHelperApplication)getApplication());
    /**
     * 布局ID
     *
     * @return layoutID
     */
    protected abstract int getLayoutId();

    /**
     * 初始化布局
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spUtil = getSharedPreferences("config", MODE_PRIVATE);
        editor = spUtil.edit();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("获取数据中...");
        progressDialog.setCancelable(false);
        setContentView(getLayoutId());
        initView();
        initData();
    }

    /**
     * Toast提醒
     */
    protected void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        closeKeyboard();
        NetRequest.getInstance(this).getRequestQueue().cancelAll(NetRequest.TAG);
    }

    /**
     * 关闭软键盘
     */
    protected void closeKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
        }
    }
}
