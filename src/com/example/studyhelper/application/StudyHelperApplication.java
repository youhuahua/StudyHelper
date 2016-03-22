package com.example.studyhelper.application;

import android.annotation.SuppressLint;
import android.app.Application;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.util.LruCache;
import android.util.Log;
import cn.bmob.v3.Bmob;
import com.example.studyhelper.utils.Config;


public class StudyHelperApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		// 初始化 Bmob SDK
		// 使用时请将第二个参数Application ID替换成你在Bmob服务器端创建的Application ID
		Bmob.initialize(this, Config.BMOB_APPID);
	}
}
