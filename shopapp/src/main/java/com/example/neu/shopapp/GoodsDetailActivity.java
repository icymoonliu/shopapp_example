package com.example.neu.shopapp;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.example.neu.baseinfo.Connectinfo;
import com.example.neu.util.MyApplication;
import com.example.neu.util.MySQLiteQueryHelper;
import com.example.neu.util.MyVolleyRequest;
import com.example.neu.util.PrefStore;
import com.example.neu.util.imagecycleview.ADInfo;
import com.example.neu.util.imagecycleview.ImageCycleView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GoodsDetailActivity extends AppCompatActivity implements Listener<JSONObject>{

	private ImageCycleView mAdView;
	private ArrayList<ADInfo> infos = new ArrayList<ADInfo>();
	private String[] imageUrls;
	private ImageLoader mImageLoader;
	private ProgressBar pb;
	private Button addcart;
	private Map<String, Object> goodsDetail;
	private ArrayList<String> goodsSizes,goodsColors;
	private TextView goodsName,goodsMaterial,goodsPrice,goodsPostalfee,goodsSales,goodsCS,goodsDisc;
	private int REQEUST_CODE=200;
	private MySQLiteQueryHelper myHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_detail);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);

		goodsDetail = new HashMap<String, Object>();
		goodsSizes=new ArrayList<String>();
		goodsColors=new ArrayList<String>();

		mAdView = (ImageCycleView) this.findViewById(R.id.ad_view);
		mImageLoader= MyApplication.getInstance().getImageLoader();

		pb = (ProgressBar) this.findViewById(R.id.progressBar);
		goodsName=(TextView)this.findViewById(R.id.goodsName);
		goodsMaterial=(TextView)this.findViewById(R.id.goodsMaterial);
		goodsPrice=(TextView)this.findViewById(R.id.goodsPrice);
		goodsPostalfee=(TextView)this.findViewById(R.id.goodsPostalfee);
		goodsSales=(TextView)this.findViewById(R.id.goodsSales);
		goodsCS=(TextView)this.findViewById(R.id.goodsCS);
		goodsDisc=(TextView)this.findViewById(R.id.goodsDisc);

		Intent intent=this.getIntent();
		String goodsId=intent.getStringExtra("goodsId");
		Map<String,String> params=new HashMap<String,String>();
		params.put("goodsId", goodsId);
		//创建post请求
		MyVolleyRequest mr=new MyVolleyRequest(Connectinfo.goodsdetailurl, params,this);
		//将请求添加到Volley请求队列
		MyApplication.getInstance().addToRequestQueue(mr);

		goodsCS.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(GoodsDetailActivity.this,SizeColorActivity.class);
				intent.putStringArrayListExtra("sizes", goodsSizes);
				intent.putStringArrayListExtra("colors", goodsColors);
				String cs = goodsCS.getText().toString();
				String[] s=cs.split("   ");
				String goods_size = s[0].trim().replace("已选：", "");
				String goods_color = s[1].trim();
				String goods_num = s[2].trim().replace("件", "");
				intent.putExtra("goods_size", goods_size);
				intent.putExtra("goods_color", goods_color);
				intent.putExtra("goods_num", goods_num);
				GoodsDetailActivity.this.startActivityForResult(intent,REQEUST_CODE);
			}
		});

		myHelper=new MySQLiteQueryHelper(this, "shop.db", null, 1);
		addcart=(Button)this.findViewById(R.id.addcart);
		addcart.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				System.out.println("---"+goodsDetail);
				PrefStore pref = PrefStore.getInstance(GoodsDetailActivity.this);
				String user_name=pref.getPref("curUserName", "");
				String goods_id=goodsDetail.get("goodsId").toString();
				String goods_name=goodsDetail.get("goodsName").toString();
				String goods_pic=goodsDetail.get("goodsPic").toString();
				String cs = goodsCS.getText().toString();
				String[] s=cs.split("   ");
				String goods_size = s[0].trim().replace("已选：", "");
				String goods_color = s[1].trim();
				String goods_num = s[2].trim().replace("件", "");
				SQLiteDatabase db=myHelper.getReadableDatabase();

				Cursor cursor=db.rawQuery("select * from shopcar where goods_id=? and goods_size=? and goods_color=?",
						new String[]{goods_id,goods_size,goods_color});

				if(cursor.moveToNext()){
					db.execSQL("update shopcar set goods_num=goods_num+? where goods_id=? and goods_size=? and goods_color=?",
							new String[]{ goods_num, goods_id, goods_size, goods_color});
				}
				else{
					db.execSQL("insert into shopcar values(null,?,?,?,?,?,?,?)",
							new String[]{goods_id, goods_name, goods_num,
									goods_size, goods_color, goods_pic, user_name});
				}
				cursor.close();


				Toast.makeText(GoodsDetailActivity.this, "已成功加入购物车", Toast.LENGTH_SHORT).show();
			}

		});


	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==REQEUST_CODE){
			//System.out.println("selNum:"+data.getStringExtra("selNum"));
			//System.out.println("selSize:"+data.getStringExtra("selSize"));
			//System.out.println("selColor:"+data.getStringExtra("selColor"));
			if(data!=null){
				goodsCS.setText("已选："+data.getStringExtra("selSize")
						+"     "+data.getStringExtra("selColor")
						+"     "+data.getStringExtra("selNum")+"件");
			}
		}
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResponse(JSONObject response) {
		// TODO Auto-generated method stub
		if(response!=null){
			parse(response);
			pb.setVisibility(View.INVISIBLE);
			//更新Textview
			goodsName.setText(goodsDetail.get("goodsName").toString());
			goodsMaterial.setText(goodsDetail.get("goodsMaterial").toString());
			goodsPrice.setText("原价：￥"+goodsDetail.get("goodsPrice").toString()
					+"       现售：￥"+goodsDetail.get("goodsDiscount").toString());
			goodsPostalfee.setText("运费：￥"+goodsDetail.get("goodsPostalfee").toString());
			goodsSales.setText("共售出"+goodsDetail.get("goodsSales").toString()+"件");
			goodsCS.setText("已选："+goodsSizes.get(0).toString()
					+"     "+goodsColors.get(0).toString()
					+"     1件");
			goodsDisc.setText(goodsDetail.get("goodsDisc").toString());

		}

		//初始化轮播图片信息
		if(infos!=null&&infos.size()==0){
			if(imageUrls!=null){
				for(int i=0;i < imageUrls.length; i ++){
					ADInfo info = new ADInfo();
					info.setUrl(imageUrls[i]);
					//info.setContent("top-->" + i);
					infos.add(info);
				}
			}
		}

		//给轮播组件填充图片
		if(infos!=null&&infos.size()>0)
			mAdView.setImageResources(infos, new MyCycleViewListener());
	}

	public void parse(JSONObject jo) {

		if (jo != null) {
			Log.v("json", jo.toString());
			JSONObject jsonObj = jo.optJSONObject("goodsDetail");
			// 数组名称
			if (jsonObj != null) {
				goodsDetail.put("goodsId", jsonObj.optString("goodsId"));
				goodsDetail.put("goodsName", jsonObj.optString("goodsName"));
				goodsDetail.put("goodsPrice",jsonObj.optString("goodsPrice"));
				goodsDetail.put("goodsDiscount",jsonObj.optString("goodsDiscount"));
				goodsDetail.put("goodsDisc",jsonObj.optString("goodsDisc"));
				goodsDetail.put("goodsOrigin",jsonObj.optString("goodsOrigin"));
				goodsDetail.put("goodsMaterial",jsonObj.optString("goodsMaterial"));
				goodsDetail.put("goodsPostalfee",jsonObj.optString("goodsPostalfee"));
				goodsDetail.put("goodsSales",jsonObj.optString("goodsSales"));
				goodsDetail.put("goodsPic",jsonObj.optString("goodsPic"));
				JSONArray jsonArray=jsonObj.optJSONArray("pics");
				if(0<jsonArray.length()){
					imageUrls=new String[jsonArray.length()];
					for(int i=0;i<jsonArray.length();i++){
						JSONObject jobj = jsonArray.optJSONObject(i);
						imageUrls[i]=Connectinfo.contexturl+jobj.optString("picUrl");
					}
				}
				//Log.v("imageUrls", imageUrls.length+"");
				JSONObject jos = jo.optJSONObject("goodsSizes");
				JSONArray sizes=jos.optJSONArray("sizes");
				if(sizes!=null&&0<sizes.length()){
					for(int i=0;i<sizes.length();i++){
						JSONObject josize = sizes.optJSONObject(i);
						//Map<String,Object> m=new HashMap<String,Object>();
						//m.put("sizeId",josize.optString("sizeId"));
						//m.put("sizeName",josize.optString("sizeName"));						
						//goodsSizes.add(m);
						goodsSizes.add(josize.optString("sizeName"));
					}
				}
				JSONObject joc = jo.optJSONObject("goodsColors");
				JSONArray colors=joc.optJSONArray("colors");
				if(colors!=null&&0<colors.length()){
					for(int i=0;i<colors.length();i++){
						JSONObject jocolor = colors.optJSONObject(i);
						//Map<String,Object> m=new HashMap<String,Object>();
						//m.put("colorId",jocolor.optString("colorId"));
						//m.put("colorName",jocolor.optString("colorName"));						
						//goodsColors.add(m);
						goodsColors.add(jocolor.optString("colorName"));
					}
				}
			}
			else{
				Toast.makeText(GoodsDetailActivity.this, "抱歉，暂无该商品信息！", Toast.LENGTH_SHORT).show();
			}
		}
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
}
