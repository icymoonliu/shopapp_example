package com.example.neu.util;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * MyApplication
 * 
 * 继承Application类，主要重写里面的onCreate（）方法（android.app.Application包的onCreate（）才是真正的Android程序的入口点），
 * 在创建的时候，初始化变量的值。然后在整个应用中的各个文件中就可以对该变量进行操作了。
 * 在ApplicationManifest.xml文件中配置自定义的Application
 * <application     android:name=".MyApplication">
 * </application>

 */
public class MyApplication extends Application {

	public static final String TAG = "VolleyPatterns";
	public static int memoryCacheSize;
	private static Context context;
    private static MyApplication sInstance;
    
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;
	
	@Override
	public void onCreate() {
		super.onCreate();
		if(sInstance==null)
			sInstance = this;
		context=this.getApplicationContext();
		memoryCacheSize = getMemoryCacheSize();
		mRequestQueue = getRequestQueue();
		mImageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache());
		// 初始化ImageLoader
		/*@SuppressWarnings("deprecation")
		DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.icon_stub) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.icon_empty) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.icon_error) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
				.build(); // 创建配置过得DisplayImageOption对象

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(options)
				.threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
		ImageLoader.getInstance().init(config);*/
		
	}

	public static Context getContext() {
		return context;
	}
	public static synchronized MyApplication getInstance() {
        return sInstance;
    }
	
	// 得到需要分配的缓存大小，这里用设备的八分之一内存的大小做缓存
	public int getMemoryCacheSize() {
		// Get memory class of this device, exceeding this amount will throw an
		// OutOfMemory exception.
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		// Use 1/8th of the available memory for this memory cache.
		return maxMemory / 8;
	}
	
	public RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(context);
		}
		return mRequestQueue;
    }
    
    public ImageLoader getImageLoader() {
    	return mImageLoader;
	}
	
	/**
     * Adds the specified request to the global queue, if tag is specified
     * then it is used else Default TAG is used.
     * 
     * @param req
     * @param tag
     */
	public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        VolleyLog.d("Adding request to queue: %s", req.getUrl());
        getRequestQueue().add(req);
    }
	
	 /**
     * Adds the specified request to the global queue using the Default TAG.
     * 
     * @param req
     */
    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
    
    
    public void cancelPendingRequests() {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(TAG);
        }
    }
    
    /**
     * Cancels all pending requests by the specified TAG, it is important
     * to specify a TAG so that the pending/ongoing requests can be cancelled.
     * 
     * @param tag
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}
