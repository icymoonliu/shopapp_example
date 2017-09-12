package com.example.neu.shopapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.example.neu.baseinfo.Connectinfo;
import com.example.neu.util.MyApplication;
import com.example.neu.util.MyVolleyRequest;
import com.example.neu.util.RefreshableView;

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
public class MeFragment extends Fragment implements Listener<JSONObject>{
	ListView order;
	List<Map<String, Object>> data;
	SimpleAdapter adapter;
	ProgressBar pb;
	RefreshableView refreshableView;

	public MeFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view=inflater.inflate(R.layout.fragment_me, container, false);
		pb = (ProgressBar) view.findViewById(R.id.progressBar);
		data = new ArrayList<Map<String, Object>>();
		order = (ListView) view.findViewById(R.id.order);
		adapter = new SimpleAdapter(this.getActivity(), data,
				R.layout.order_list_view_item,
				new String[] { "code", "date","status" },
				new int[] { R.id.code, R.id.date,R.id.status });
		order.setAdapter(adapter);

		//创建post请求
		MyVolleyRequest mr=new MyVolleyRequest(Connectinfo.orderurl, this);
		//将请求添加到Volley请求队列
		MyApplication.getInstance().addToRequestQueue(mr);

		//下拉刷新
		//handler = new Handler();
		refreshableView = (RefreshableView) view.findViewById(R.id.refreshable_view);
		refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener()
		{
			@Override
			public void onRefresh()
			{
				/*
				 * try { Thread.sleep(3000); } catch (InterruptedException e) {
				 * e.printStackTrace(); }
				 */
				data.clear();
				if(adapter!=null){
					adapter.notifyDataSetChanged();
				}

				pb.setVisibility(View.VISIBLE);System.out.println("refresh");
				//创建post请求
				MyVolleyRequest mr=new MyVolleyRequest(Connectinfo.orderurl, MeFragment.this);
				//将请求添加到Volley请求队列
				MyApplication.getInstance().addToRequestQueue(mr);
				refreshableView.finishRefreshing();
			}
		}, 0);
		return view;
	}

	@Override
	public void onResponse(JSONObject response) {System.out.println("orderresponse");
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
			JSONArray jsonArray = jo.optJSONArray("ordersList");
			// 数组名称
			if (jsonArray != null) {
				int length = jsonArray.length();
				if (0 < length) {
					for (int i = 0; i < length; i++) {
						JSONObject jsonObj = jsonArray.optJSONObject(i);
						if (jsonObj == null)
							continue;

						Map<String, Object> m = new HashMap<String, Object>();
						m.put("id", jsonObj.optString("orderId"));
						m.put("code", "订单号："+jsonObj.optString("orderCode"));
						m.put("date", "下单日期："+jsonObj.optString("orderDate"));
						int status=jsonObj.optInt("orderStatus");
						String str="";
						switch(status){
							case 0:
								str="等待付款";break;
							case 1:
								str="未发货";break;
							case 2:
								str="申请退款中";break;
							case 3:
								str="退款成功";break;
							case 4:
								str="已发货";break;
							case 5:
								str="交易成功";break;
							case 6:
								str="申请退货中";break;
							case 7:
								str="退货中";break;
							case 8:
								str="退货成功";break;
							case 9:
								str="交易关闭";break;
						}
						m.put("status", str);
						data.add(m);
					}
					//Log.v("data", data.toString());
				}
				else{
					Toast.makeText(MeFragment.this.getActivity(), "抱歉，您还没有任何订单！", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
}
