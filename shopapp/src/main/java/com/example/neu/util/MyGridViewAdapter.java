package com.example.neu.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.neu.shopapp.R;

import java.util.List;
import java.util.Map;


public class MyGridViewAdapter extends BaseAdapter implements OnScrollListener{

	private Context mContext;
	private static ImageLoader mImageLoader; // imageLoader对象，用来初始化NetworkImageView
	private int layout;//加载的行布局视图
	private int mItemHeight = 0;//子项高度
	private int mItemWidth = 0;//子项宽度
	
	private List<Map<String, String>> data;
	
	
	
	public MyGridViewAdapter(List<Map<String, String>> data) {
		mContext = MyApplication.getContext();
		// 初始化mImageLoader，并且传入了自定义的内存缓存
		mImageLoader = MyApplication.getInstance().getImageLoader(); 
		this.data=data;
		this.layout= R.layout.goods_grid_view_item;
	}
	
	public MyGridViewAdapter(List<Map<String, String>> data,int layout) {
		mContext = MyApplication.getContext();
		// 初始化mImageLoader，并且传入了自定义的内存缓存
		mImageLoader = MyApplication.getInstance().getImageLoader(); 
		this.data=data;
		this.layout=layout;
	}
	
	public MyGridViewAdapter(Context context,List<Map<String, String>> data,int layout) {
		mContext = context;
		// 初始化mImageLoader，并且传入了自定义的内存缓存
		mImageLoader = MyApplication.getInstance().getImageLoader(); 
		this.data=data;
		this.layout=layout;
	}

	@Override
	public int getCount() {
		return data.size(); // 返回item的个数
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/* 
	 * 重要的方法。通过viewHolder复用view，并且设置好item的宽度
	 * 
	 * @param position
	 * @param convertView
	 * @param parent
	 * @return
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			// init convertView by layout
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(layout, null);
		}
		// 得到item中显示图片的view
		NetworkImageView networkImageView = ViewHolder.get(convertView, R.id.imageView); 
		// 得到item中的textview
		TextView textView = ViewHolder.get(convertView, R.id.textView); 
		// 设置默认的图片
		networkImageView.setDefaultImageResId(R.drawable.default_photo); 
		// 设置图片加载失败后显示的图片
		networkImageView.setErrorImageResId(R.drawable.error_photo); 
		if(mItemWidth>0){
			networkImageView.getLayoutParams().width = mItemWidth;
			networkImageView.getLayoutParams().height = mItemWidth; 
		}
		
		/*if (networkImageView.getLayoutParams().height != mItemHeight) {
			// 通过activity中计算出的结果，在这里设置networkImageview的高度（得到的是正方形）
			networkImageView.getLayoutParams().height = mItemHeight; 
		}*/

		Log.d("gridview", (data.get(position).get("url")).toString());
		System.out.println("gridview"+networkImageView);
		System.out.println("mImageLoader"+mImageLoader);
		// 开始加载网络图片
		networkImageView.setImageUrl((String)(data.get(position).get("url")), mImageLoader);
		textView.setText("" + data.get(position).get("text")); // 简单的设置textview上的文字
		return convertView;
	}
	
	/**
	 * 设置item子项的高度。
	 */
	public void setItemHeight(int height) {
		if (height == mItemHeight) {
			return;
		}
		mItemHeight = height;
		notifyDataSetChanged();
	}
	
	/**
	 * 设置item子项的宽度。
	 */
	public void setItemWidth(int width) {
		mItemWidth = width;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount , int totalItemCount ) {
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// 仅当GridView静止时才去下载图片，GridView滑动时取消所有正在下载的任务  
        if (scrollState == SCROLL_STATE_IDLE) {  
             //loadBitmaps(mFirstVisibleItem, mVisibleItemCount);  
        } else {  
             //MyApplication.getInstance().cancelPendingRequests();
        }  
	}
	

}
