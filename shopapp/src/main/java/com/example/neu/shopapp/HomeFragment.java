package com.example.neu.shopapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.example.neu.baseinfo.Connectinfo;
import com.example.neu.util.MyApplication;
import com.example.neu.util.MyGridViewAdapter;
import com.example.neu.util.MyVolleyRequest;
import com.example.neu.util.imagecycleview.ADInfo;
import com.example.neu.util.imagecycleview.ImageCycleView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class HomeFragment extends Fragment {

	private ImageCycleView mAdView;

	private ArrayList<ADInfo> infos = new ArrayList<ADInfo>();

	private String[] imageUrls = {Connectinfo.adImg1,Connectinfo.adImg2,Connectinfo.adImg3};

	private GridView newGoods,salesGoods;
	private List<Map<String, String>> data1,data2;
	private MyGridViewAdapter adapter1,adapter2;
	private ProgressBar pb1,pb2;
	private ImageLoader mImageLoader;


	public HomeFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view=inflater.inflate(R.layout.fragment_home, container, false);

		//初始化轮播图片信息
		if(infos!=null&&infos.size()==0){
			for(int i=0;i < imageUrls.length; i ++){
				ADInfo info = new ADInfo();
				info.setUrl(imageUrls[i]);
				//info.setContent("top-->" + i);
				infos.add(info);
			}
		}
		mAdView = (ImageCycleView) view.findViewById(R.id.ad_view);
		//给轮播组件填充图片
		mAdView.setImageResources(infos, new MyCycleViewListener());
		mImageLoader= MyApplication.getInstance().getImageLoader();

		//初始化新到商品GridView
		pb1 = (ProgressBar) view.findViewById(R.id.progressBar1);
		data1 = new ArrayList<Map<String, String>>();
		/*adapter1 = new SimpleAdapter(this.getActivity(), data1,
				R.layout.grid_view_item, 
				new String[] { "goodsImg", "goodsName"}, 
				new int[] { R.id.goodsImg, R.id.goodsName });*/
		adapter1 = new MyGridViewAdapter(data1,R.layout.goods_grid_view_item);
		newGoods = (GridView) view.findViewById(R.id.newGoods);
		newGoods.setAdapter(adapter1);
		newGoods.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Map<String,String> m=data1.get(arg2);
				String goodsId=(String)m.get("id");
				Intent intent=new Intent(HomeFragment.this.getActivity(),GoodsDetailActivity.class);
				intent.putExtra("goodsId", goodsId);
				HomeFragment.this.getActivity().startActivity(intent);
			}

		});
		/*adapter1.setViewBinder(new ViewBinder() {
			@Override
			public boolean setViewValue(View view, Object data,
					String textRepresentation) {
				if (view instanceof ImageView && data instanceof Bitmap) {
					ImageView iv = (ImageView) view;
					iv.setImageBitmap((Bitmap) data);
					return true;
				}
				return false;
			}
		});*/

		//加载新到商品
		/*AsynHttp asynObject1 = new AsynHttp(Connectinfo.newgoodsurl,this.getActivity(), new NewGoodsCallBack());
		asynObject1.GetData();*/
		//创建post请求
		MyVolleyRequest mr=new MyVolleyRequest(Connectinfo.newgoodsurl, new NewGoodsCallBack());
		//将请求添加到Volley请求队列
		MyApplication.getInstance().addToRequestQueue(mr);

		//初始化热销商品GridView
		pb2 = (ProgressBar) view.findViewById(R.id.progressBar2);
		data2 = new ArrayList<Map<String, String>>();
		/*adapter2 = new SimpleAdapter(this.getActivity(), data2,
				R.layout.salesgoods_grid_view_item, 
				new String[] { "goodsImg", "goodsName"}, 
				new int[] { R.id.goodsImg, R.id.goodsName });*/
		adapter2 = new MyGridViewAdapter(data2,R.layout.goods_grid_view_item);
		salesGoods = (GridView) view.findViewById(R.id.salesGoods);
		salesGoods.setAdapter(adapter2);
		salesGoods.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Map<String,String> m=data1.get(arg2);
				String goodsId=(String)m.get("id");
				Intent intent=new Intent(HomeFragment.this.getActivity(),GoodsDetailActivity.class);
				intent.putExtra("goodsId", goodsId);
				HomeFragment.this.getActivity().startActivity(intent);
			}

		});
		/*adapter2.setViewBinder(new ViewBinder() {
			@Override
			public boolean setViewValue(View view, Object data,
					String textRepresentation) {
				if (view instanceof ImageView && data instanceof Bitmap) {
					ImageView iv = (ImageView) view;
					iv.setImageBitmap((Bitmap) data);
					return true;
				}
				return false;
			}
		});*/

		//加载热销商品
		/*AsynHttp asynObject2 = new AsynHttp(Connectinfo.salesgoodsurl,this.getActivity(), new SalesGoodsCallBack());
		asynObject2.GetData();*/
		//创建post请求
		MyVolleyRequest mr2=new MyVolleyRequest(Connectinfo.salesgoodsurl, new SalesGoodsCallBack());
		//将请求添加到Volley请求队列
		MyApplication.getInstance().addToRequestQueue(mr2);

		return view;
	}
	class MyCycleViewListener implements ImageCycleView.ImageCycleViewListener {

		@Override
		public void onImageClick(ADInfo info, int position, View imageView) {
			//Toast.makeText(HomeFragment.this.getActivity(), "content->"+info.getContent(), Toast.LENGTH_SHORT).show();
		}

		@Override
		public void displayImage(String imageURL, ImageView imageView) {
			//imageURL是图片网络地址
			//imageView是ImageView实例
			//R.drawable.def_image默认图片id
			//R.drawable.err_image加载图片错误时的图片
			mImageLoader.get(imageURL, ImageLoader.getImageListener(imageView,
					R.drawable.default_photo, R.drawable.error_photo));
		}
	};
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mAdView.startImageCycle();
	}


	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mAdView.pushImageCycle();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mAdView.pushImageCycle();
	}

	class NewGoodsCallBack implements Listener<JSONObject>{

		/*@Override
		public void callBackFunction(JSONObject jsonobj) {
			// TODO Auto-generated method stub
			if(jsonobj!=null){
				parse(jsonobj);	
				getBitMap(data1);	
				//adapter.notifyDataSetChanged();
			}
		}*/
		@Override
		public void onResponse(JSONObject response) {
			// TODO Auto-generated method stub
			if(response!=null){
				parse(response);
				//getBitMap(data1);	
				pb1.setVisibility(View.INVISIBLE);
				adapter1.notifyDataSetChanged();
			}
		}

		public void parse(JSONObject jo) {

			if (jo != null) {
				//Log.v("postjson", jo.toString());
				JSONArray jsonArray = jo.optJSONArray("newGoods");
				// 数组名称
				if (jsonArray != null) {
					int length = jsonArray.length();
					if (0 < length) {
						for (int i = 0; i < length; i++) {
							JSONObject jsonObj = jsonArray.optJSONObject(i);
							if (jsonObj == null)
								continue;

							Map<String, String> m = new HashMap<String, String>();

							m.put("id", jsonObj.optString("goodsId"));
							m.put("text", jsonObj.optString("goodsName")
									+"\r\n现售：￥"+jsonObj.optString("goodsDiscount"));
							m.put("url", Connectinfo.contexturl+jsonObj.optString("goodsPic"));

							data1.add(m);

						}
						Log.v("data1", data1.toString());
					}
					else{
						Toast.makeText(HomeFragment.this.getActivity(), "抱歉，暂无新到商品！", Toast.LENGTH_SHORT).show();
					}
				}

			}
		}
		/*public void getBitMap(List<Map<String, Object>> data) {
			if (data != null) {
				for (Map<String, Object> m : data) {
					String url = (String) m.get("goodsPic");
					AsynGetImage agi = new AsynGetImage(Connectinfo.contexturl+url, HomeFragment.this.getActivity(),
							this, false);
					agi.set_data(m);
					agi.getImage();
					
				}
			}
		}*/
		/*@Override
		public void getHttpImgCallBackFunction(Map<String, Object> result) {
			// TODO Auto-generated method stub
			if (result != null) {
				Bitmap bm = (Bitmap) result.get("Bitmap");
				Map<String,Object> m=(Map<String,Object>)result.get("data");
				if(m!=null){
					m.put("goodsImg", bm);
					//adapter.notifyDataSetChanged();
				}
				count1++;
			}
			if(count1==data1.size()){
				pb1.setVisibility(View.INVISIBLE);
				adapter1.notifyDataSetChanged();
			}
		}*/

	}

	class SalesGoodsCallBack implements Listener<JSONObject>{

		/*@Override
		public void callBackFunction(JSONObject jsonobj) {
			// TODO Auto-generated method stub
			if(jsonobj!=null){
				parse(jsonobj);	
				getBitMap(data2);	
				//adapter.notifyDataSetChanged();
			}
		}*/

		@Override
		public void onResponse(JSONObject response) {
			// TODO Auto-generated method stub
			if(response!=null){
				parse(response);
				pb2.setVisibility(View.INVISIBLE);
				adapter2.notifyDataSetChanged();
			}
		}
		public void parse(JSONObject jo) {

			if (jo != null) {
				//Log.v("postjson", jo.toString());
				JSONArray jsonArray = jo.optJSONArray("salesGoods");
				// 数组名称
				if (jsonArray != null) {
					int length = jsonArray.length();
					if (0 < length) {
						for (int i = 0; i < length; i++) {
							JSONObject jsonObj = jsonArray.optJSONObject(i);
							if (jsonObj == null)
								continue;

							Map<String, String> m = new HashMap<String, String>();

							m.put("id", jsonObj.optString("goodsId"));
							m.put("text", jsonObj.optString("goodsName")
									+"\r\n现售：￥"+jsonObj.optString("goodsDiscount"));
							m.put("url", Connectinfo.contexturl+jsonObj.optString("goodsPic"));

							data2.add(m);

						}
						Log.v("data2", data2.toString());
					}
					else{
						Toast.makeText(HomeFragment.this.getActivity(), "抱歉，暂无热销商品！", Toast.LENGTH_SHORT).show();
					}
				}
			}
		}

	}


}
