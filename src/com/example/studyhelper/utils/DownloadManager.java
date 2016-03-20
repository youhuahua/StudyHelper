package com.example.studyhelper.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.os.Handler;
import android.os.Message;

public class DownloadManager implements Serializable{
	// =======================test=======================
	private int progress = 0;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			notifyObservers(progress);
			progress++;
			Message message = handler.obtainMessage();
			message.what = progress;
			if (progress <= 360) {
				handler.sendMessageDelayed(message, 20);
			}
		};
	};

	// =======================test over=======================
	public interface Observer {
		public void update(int progress);

//		public void updateState();
	}

	List<Observer> observers = new ArrayList<Observer>();

	// 有内容发生了改变1
	// 添加观察者
	public void addObserver(Observer observer) {
		
		if (observer == null) {
			throw new RuntimeException();
		}
		if (!observers.contains(observer)) {
			observers.add(observer);
		}
	}

	// 通知数据发生变化
	public void notifyObservers(int progress) {
		for (Observer observer : observers) {
			observer.update(progress);
		}
	}

	// 通知状态改变
	public void notifyState() {
		for (Observer observer : observers) {
//			observer.updateState();
		}
	}

	// =======================test=======================
	public void test() {
		Message message = handler.obtainMessage();
		message.what = progress;
		handler.sendMessage(message);
	}
	// =======================test over=======================
}
