package com.example.neu.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.neu.shopapp.R;

import java.util.List;
import java.util.Map;

public class MyListViewAdapter extends BaseAdapter {
	private Context mContext;
	private static ImageLoader mImageLoader; // imageLoader对象，用来初始化NetworkImageView
	private int layout;//加载的行布局视图	
	private List<Map<String, String>> data;
		
	public MyListViewAdapter(List<Map<String, String>> data) {
		mContext = MyApplication.getContext();
		// 初始化mImageLoader，并且传入了自定义的内存缓存
		mImageLoader = MyApplication.getInstance().getImageLoader(); 
		this.data=data;
		this.layout= R.layout.shopcar_list_view_item;
	}
	
	public MyListViewAdapter(List<Map<String, String>> data,int layout) {
		mContext = MyApplication.getContext();
		// 初始化mImageLoader，并且传入了自定义的内存缓存
		mImageLoader = MyApplication.getInstance().getImageLoader(); 
		this.data=data;
		this.layout=layout;
	}
	
	public MyListViewAdapter(Context context,List<Map<String, String>> data,int layout) {
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
		final int index=position;
		if (convertView == null) {
			// init convertView by layout
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(layout, null);
		}
		// 得到item中显示图片的view
		NetworkImageView networkImageView = ViewHolder.get(convertView, R.id.imageView); 
		// 得到item中的textview
		TextView textView1 = ViewHolder.get(convertView, R.id.textView1); 
		TextView textView2 = ViewHolder.get(convertView, R.id.textView2);
		ImageView button = ViewHolder.get(convertView, R.id.button);
		// 设置默认的图片
		networkImageView.setDefaultImageResId(R.drawable.default_photo); 
		// 设置图片加载失败后显示的图片
		networkImageView.setErrorImageResId(R.drawable.error_photo); 
		
		
		/*if (networkImageView.getLayoutParams().height != mItemHeight) {
			// 通过activity中计算出的结果，在这里设置networkImageview的高度（得到的是正方形）
			networkImageView.getLayoutParams().height = mItemHeight; 
		}*/

		Log.d("listview", (data.get(position).get("url")).toString());
		System.out.println("listview"+networkImageView);
		System.out.println("mImageLoader"+mImageLoader);
		// 开始加载网络图片
		networkImageView.setImageUrl((String)(data.get(position).get("url")), mImageLoader);
		textView1.setText("" + data.get(position).get("text1")); // 简单的设置textview上的文字
		textView2.setText("" + data.get(position).get("text2")); // 简单的设置textview上的文字
		button.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PrefStore pref=PrefStore.getInstance();
				MySQLiteQueryHelper myHelper=new MySQLiteQueryHelper(mContext, "shop.db", null, 1);
				SQLiteDatabase db=myHelper.getWritableDatabase();
				db.execSQL("delete from shopcar where goods_id=? and goods_size=? and goods_color=? and user_name=?",
                        new String[]{ data.get(index).get("goods_id"), data.get(index).get("goods_size"), data.get(index).get("goods_color"),pref.getPref("curUserName", "")});
				data.remove(index);
				notifyDataSetChanged();
			}
		});
		button.setFocusable(false);
		return convertView;
	}
	
}
