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

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class CateFragment extends Fragment implements Listener<JSONObject>{

	private GridView cate;
	private List<Map<String, String>> data;
	private MyGridViewAdapter adapter;
	private ProgressBar pb;

	public CateFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view=inflater.inflate(R.layout.fragment_cate, container, false);
		//初始化商品分类GridView
		pb = (ProgressBar) view.findViewById(R.id.progressBar);
		data = new ArrayList<Map<String, String>>();

		adapter = new MyGridViewAdapter(data,R.layout.cate_grid_view_item);
		cate = (GridView) view.findViewById(R.id.cate);
		cate.setAdapter(adapter);

		//创建post请求
		MyVolleyRequest mr=new MyVolleyRequest(Connectinfo.cateurl, this);
		//将请求添加到Volley请求队列
		MyApplication.getInstance().addToRequestQueue(mr);

		cate.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Map<String,String> m=data.get(arg2);
				String cateId=(String)m.get("id");
				Intent intent=new Intent(CateFragment.this.getActivity(),GoodsActivity.class);
				intent.putExtra("cateId", cateId);
				startActivity(intent);
			}

		});
		return view;
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
			JSONArray jsonArray = jo.optJSONArray("catesList");
			// 数组名称
			if (jsonArray != null) {
				int length = jsonArray.length();
				if (0 < length) {
					for (int i = 0; i < length; i++) {
						JSONObject jsonObj = jsonArray.optJSONObject(i);
						if (jsonObj == null)
							continue;

						Map<String, String> m = new HashMap<String, String>();
						m.put("id", jsonObj.optString("cateId"));
						m.put("text", jsonObj.optString("cateName"));
						m.put("url", Connectinfo.contexturl+jsonObj.optString("catePic"));

						data.add(m);
					}
					Log.v("data", data.toString());
				}
				else{
					Toast.makeText(CateFragment.this.getActivity(), "抱歉，暂无商品分类信息！", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
}
