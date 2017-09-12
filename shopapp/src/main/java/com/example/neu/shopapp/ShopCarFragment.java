package com.example.neu.shopapp;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.example.neu.baseinfo.Connectinfo;
import com.example.neu.util.MyApplication;
import com.example.neu.util.MyListViewAdapter;
import com.example.neu.util.MySQLiteQueryHelper;
import com.example.neu.util.MyVolleyJSONRequest;
import com.example.neu.util.PrefStore;

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
public class ShopCarFragment extends Fragment implements Listener<JSONObject>{

	private MySQLiteQueryHelper myHelper;
	private ListView shopcar;
	private ArrayList<Map<String,String>> data;
	MyListViewAdapter adapter;
	Button addorder;

	public ShopCarFragment() {
		// Required empty public constructor
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view=inflater.inflate(R.layout.fragment_shop_car, container, false);
		data=new ArrayList<Map<String, String>>();
		refreshShopData();
		shopcar=(ListView)view.findViewById(R.id.shopcar);
		adapter=new MyListViewAdapter(data,
				R.layout.shopcar_list_view_item);
		shopcar.setAdapter(adapter);
		addorder=(Button)view.findViewById(R.id.addorder);
		if(data==null||data.size()==0){
			addorder.setText("当前购物车为空，快去购物吧！");
			addorder.setEnabled(false);
		}
		addorder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				List<String> goodsId=new ArrayList<String>();
				List<String> size=new ArrayList<String>();
				List<String> color=new ArrayList<String>();
				List<String> num=new ArrayList<String>();
				for(int i=0;i<data.size();i++){
					goodsId.add(data.get(i).get("goods_id"));
					size.add(data.get(i).get("goods_size"));
					color.add(data.get(i).get("goods_color"));
					num.add(data.get(i).get("goods_num"));
				}
				Map<String,Object> params=new HashMap<String,Object>();
				params.put("goodsId", new JSONArray(goodsId));
				params.put("size", new JSONArray(size));
				params.put("color", new JSONArray(color));
				params.put("num", new JSONArray(num));
				JSONObject jo=new JSONObject(params);
				System.out.println("json"+jo);
				MyVolleyJSONRequest mr=new MyVolleyJSONRequest(Connectinfo.addorderurl,
						jo,ShopCarFragment.this);
				MyApplication.getInstance().addToRequestQueue(mr);
			}
		});
		shopcar.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Map<String,String> m=data.get(arg2);
				String goodsId=(String)m.get("goods_id");
				Intent intent=new Intent(ShopCarFragment.this.getActivity(),GoodsDetailActivity.class);
				intent.putExtra("goodsId", goodsId);
				ShopCarFragment.this.startActivity(intent);
			}
		});
		return view;
	}

	private void refreshShopData() {
		// TODO Auto-generated method stub
		PrefStore pref = PrefStore.getInstance();
		String user_name=pref.getPref("curUserName", "");

		myHelper=new MySQLiteQueryHelper(this.getActivity(), "shop.db", null, 1);
		SQLiteDatabase db=myHelper.getReadableDatabase();
		Cursor cursor=db.rawQuery("select * from shopcar where user_name = ?",
				new String[]{user_name});
		data.clear();
		while(cursor.moveToNext()){
			String cs=cursor.getString(4)+"  "
					+cursor.getString(5)+"  "
					+cursor.getString(3)+"件";

			Map<String,String> m=new HashMap<String, String>();
			m.put("text1",cursor.getString(2));
			m.put("url", Connectinfo.contexturl+cursor.getString(6));
			m.put("text2",cs);
			m.put("goods_id", cursor.getString(1));
			m.put("goods_num", cursor.getString(3));
			m.put("goods_size", cursor.getString(4));
			m.put("goods_color", cursor.getString(5));
			data.add(m);
		}
		cursor.close();
	}

	@Override
	public void onResponse(JSONObject response) {
		// TODO Auto-generated method stub
		if(response!=null){
			Log.v("addorder", response.toString());
			if(response.optString("addOrderFlag").equals("1")){
				Toast.makeText(this.getActivity(), "生成订单成功",
						Toast.LENGTH_SHORT).show();
				PrefStore pref=PrefStore.getInstance();
				MySQLiteQueryHelper myHelper=new MySQLiteQueryHelper(this.getActivity(), "shop.db", null, 1);
				SQLiteDatabase db=myHelper.getWritableDatabase();
				db.execSQL("delete from shopcar where user_name=?",
						new String[]{pref.getPref("curUserName", "") });
				data.clear();
				adapter.notifyDataSetChanged();
				addorder.setText("当前购物车为空，快去购物吧！");
				addorder.setEnabled(false);
			}
			else{
				Toast.makeText(this.getActivity(), response.optString("msg"),
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		System.out.println("visible"+getUserVisibleHint()+","+isVisibleToUser);
		if(getUserVisibleHint()){
			if(adapter!=null){
				refreshShopData();
				adapter.notifyDataSetChanged();
			}
			if(addorder!=null){
				if(data==null||data.size()==0){
					addorder.setText("当前购物车为空，快去购物吧！");
					addorder.setEnabled(false);
				}
				else{
					addorder.setText("确认购买");
					addorder.setEnabled(true);
				}
			}
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(adapter!=null){
			refreshShopData();
			adapter.notifyDataSetChanged();
		}
	}
}
