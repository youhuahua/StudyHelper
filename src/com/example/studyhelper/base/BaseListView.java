package com.example.studyhelper.base;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
import com.example.studyhelper.R;


public class BaseListView extends ListView {
	
	public BaseListView(Context context) {
		super(context);
		init();
	}

	public BaseListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public BaseListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
//		setSelector  点击显示的颜色
//		setCacheColorHint  拖拽的颜色
//		setDivider  每个条目的间隔	的分割线	
		this.setSelector(R.drawable.nothing); 
		this.setCacheColorHint(R.drawable.nothing);
		this.setDivider(getContext().getResources().getDrawable(R.drawable.nothing));
	}

}
