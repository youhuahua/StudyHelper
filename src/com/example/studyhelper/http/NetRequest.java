package com.example.studyhelper.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.studyhelper.utils.DiskLruCache;
import com.example.studyhelper.utils.FileUtils;
import com.example.studyhelper.utils.Md5Utils;

/**
 * volley网络框架的使用
 */
public class NetRequest {
	private static NetRequest mInstance;
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;
	private static Context mCtx;
	public static final String TAG = "REQUEST_DATA";
	private DiskLruCache mDiskLruCache;
	private static final long DISK_CACHE_SIZE = 1024 * 1024 * 10;

	// 获得应用version号码
	public int getAppVersion(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 1;
	}

	private NetRequest(Context context) {
		mCtx = context;
		mRequestQueue = getRequestQueue();
		mImageLoader = getImageLoader();

		if (FileUtils.getUsableSpace(FileUtils.getCacheDir(mCtx)) > DISK_CACHE_SIZE) {
			try {
				// 1.磁盘缓存在文件系统的存储路径(具体是指/sdcard/Android/data/package_name/cache,package_name表示当前应用的包名，
				// 这个路径被Android系统认定为应用程序的缓存路径，当程序被卸载的时候，这里的数据也会一起被清除掉，这样就不会出现删除程序之后手机上还有很多残留数据的问题。)
				// 2.版本号，一般设为1即可，版本号变化时会清空之前缓存的所有文件，实际不一定有效
				// 3.单个节点所对应数据的个数，一般设为1即可
				// 4.缓存总大小
				mDiskLruCache = DiskLruCache.open(FileUtils.getCacheDir(mCtx), getAppVersion(mCtx), 1, DISK_CACHE_SIZE);// 10MB
			} catch (IOException e) {
				System.out.println("磁盘缓存不可用");
				e.printStackTrace();
			}
		}

	}

	/**
	 * @param context
	 * @return
	 */
	public static synchronized NetRequest getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new NetRequest(context.getApplicationContext());
		}
		return mInstance;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			// getApplicationContext()是关键, 它会避免
			// Activity或者BroadcastReceiver带来的缺点.
			mRequestQueue = Volley.newRequestQueue(mCtx);
		}
		return mRequestQueue;
	}

	public <T> void addToRequestQueue(Request<T> req) {
		getRequestQueue().add(req);
	}

	public ImageLoader getImageLoader() {
		if (mImageLoader == null) {
			mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
				@Override
				public void putBitmap(String url, Bitmap bitmap) {
					imageCache.put(url, bitmap);
					String key = Md5Utils.md5(url);
					OutputStream outputStream = null;
					try {
						if (mDiskLruCache != null && null == mDiskLruCache.get(key)) {
							DiskLruCache.Editor editor = mDiskLruCache.edit(key);
							if (editor != null) {
								outputStream = editor.newOutputStream(0);
								if (bitmap.compress(CompressFormat.PNG, 100, outputStream)) {
									editor.commit();
								} else {
									editor.abort();
								}

							}

							mDiskLruCache.flush();
						}
					} catch (IOException e) {
						System.out.println("=============磁盘缓存失败===============");
						e.printStackTrace();
					} finally {
						if (outputStream != null) {
							try {
								outputStream.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}

				@Override
				public Bitmap getBitmap(String url) {
					Bitmap img = imageCache.get(url);
					InputStream is = null;
					if (img == null) {
						String key = Md5Utils.md5(url);
						try {
							if (mDiskLruCache != null) {
								if (mDiskLruCache.get(key) == null) {
									return null;
								} else {
									DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);
									Bitmap bitmap = null;
									if (snapShot != null) {
										is = snapShot.getInputStream(0);
										bitmap = BitmapFactory.decodeStream(is);
									}
									return bitmap;
								}
							}
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							if (is != null) {
								try {
									is.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					} else {
						return img;
					}
					return null;
				}
			});
		}
		return mImageLoader;
	}

	/**
	 * Volley用于全局的图片内存缓存
	 */
	protected final LruCache<String, Bitmap> imageCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / 1024 / 8)) {
		@Override
		protected int sizeOf(String key, Bitmap bitmap) {
			// 重写此方法来衡量每张图片的大小，默认返回图片数量。
			int size = bitmap.getRowBytes() * bitmap.getHeight() / 1024;// 单位kb
			return size;
		}

	};

	/**
	 * 取消所有请求
	 */
	public void cancleAll() {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(NetRequest.TAG);
		}
	}
}
