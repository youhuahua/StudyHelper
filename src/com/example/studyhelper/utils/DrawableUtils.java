package com.example.studyhelper.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

public class DrawableUtils {
	public static GradientDrawable createShape(Context context,int color){
		GradientDrawable drawable=new GradientDrawable();
		drawable.setCornerRadius(ScreenUtil.dip2px(context, 4));//设置4个角的弧度 
		drawable.setColor(color);// 设置颜色
		return drawable;
		
		
	}
	
	public static StateListDrawable createSelectorDrawable(Drawable pressedDrawable,Drawable normalDrawable){
//		<selector xmlns:android="http://schemas.android.com/apk/res/android"  android:enterFadeDuration="200">
//	    <item  android:state_pressed="true" android:drawable="@drawable/detail_btn_pressed"></item>
//	    <item  android:drawable="@drawable/detail_btn_normal"></item>
//	</selector>
		StateListDrawable stateListDrawable=new StateListDrawable();
		stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);// 按下显示的图片
		stateListDrawable.addState(new int[]{}, normalDrawable);// 抬起显示的图片
		return stateListDrawable;
		
	}
}
