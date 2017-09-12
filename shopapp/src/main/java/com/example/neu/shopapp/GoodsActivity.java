package com.example.neu.shopapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.example.neu.baseinfo.Connectinfo;
import com.example.neu.util.MyApplication;
import com.example.neu.util.MyGridViewAdapter;
import com.example.neu.util.MyVolleyRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoodsActivity extends AppCompatActivity implements Listener<JSONObject>{

	private GridView goods;
	private List<Map<String, String>> data;
	private MyGridViewAdapter adapter;
	private ProgressBar pb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);

		//初始化商品GridView
		pb = (ProgressBar) this.findViewById(R.id.progressBar);
		data = new ArrayList<Map<String, String>>();

		adapter = new MyGridViewAdapter(data,R.layout.goods_grid_view_item);
		goods = (GridView) this.findViewById(R.id.goods);

		goods.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				goods.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				if (goods.getNumColumns() > 0) {
					int columnWidth = (goods.getWidth() / goods.getNumColumns()) - goods.getHorizontalSpacing();
					System.out.println("width---------------------------------"+columnWidth);
					adapter.setItemWidth(columnWidth);
				}
			}
		});


		goods.setAdapter(adapter);


		Intent intent=this.getIntent();
		String cateId=intent.getStringExtra("cateId");
		Map<String,String> params=new HashMap<String,String>();
		params.put("cateId", cateId);
		//创建post请求
		MyVolleyRequest mr=new MyVolleyRequest(Connectinfo.goodsurl, params,this);
		//将请求添加到Volley请求队列
		MyApplication.getInstance().addToRequestQueue(mr);

		goods.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Map<String,String> m=data.get(arg2);
				String goodsId=(String)m.get("id");
				Intent intent=new Intent(GoodsActivity.this,GoodsDetailActivity.class);
				intent.putExtra("goodsId", goodsId);
				GoodsActivity.this.startActivity(intent);
			}

		});
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
			adapter.notifyDataSetChanged();
		}
	}
	public void parse(JSONObject jo) {

		if (jo != null) {
			//Log.v("postjson", jo.toString());
			JSONArray jsonArray = jo.optJSONArray("goodsList");
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
								+"\r\n原价：￥"+jsonObj.optString("goodsPrice")
								+"\r\n现售：￥"+jsonObj.optString("goodsDiscount"));
						m.put("url", Connectinfo.contexturl+jsonObj.optString("goodsPic"));

						data.add(m);
					}
					//Log.v("data", data.toString());
				}
				else{
					Toast.makeText(this, "抱歉，暂无此类商品！", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MyApplication.getInstance().cancelPendingRequests();
	}





}
