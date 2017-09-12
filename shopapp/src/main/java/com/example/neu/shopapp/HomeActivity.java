package com.example.neu.shopapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.example.neu.baseinfo.Connectinfo;
import com.example.neu.util.MyApplication;
import com.example.neu.util.MyVolleyRequest;
import com.example.neu.util.PrefStore;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements  OnCheckedChangeListener,Listener<JSONObject>{

	//ViewPager控件
	private ViewPager viewPager ;
	//RadioGroup控件
	private RadioGroup radioGroup ;
	//类型为Fragment的动态数组
	private ArrayList<Fragment> fList= new ArrayList<Fragment>() ;

	private long startTime=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		getOverflowMenu();

		//注册radiobutton选择监听器		
		radioGroup = (RadioGroup) findViewById(R.id.rg) ;
		radioGroup.setOnCheckedChangeListener(this);
		//ViewPager初始化函数
		initViewPager();

	}

	private void initViewPager()	{
		viewPager = (ViewPager) findViewById(R.id.viewPager);

		Fragment hf = new HomeFragment() ;
		Fragment cf = new CateFragment();
		Fragment sf = new ShopCarFragment();
		Fragment mf = new MeFragment();

		//将各Fragment加入数组中
		fList.add(hf); fList.add(cf);
		fList.add(sf); fList.add(mf);
		//设置ViewPager的适配器
		viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
		//当前为第一个页面
		viewPager.setCurrentItem(0);
		//ViewPager的页面改变监听器，可先不加这个监听器，此监听器用于滑动切换Fragment时改变RadioButton的选中状态
		viewPager.addOnPageChangeListener(new MyListener());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		//获取当前被选中的RadioButton的ID，用于改变ViewPager的当前页
		int current=0;
		switch(checkedId)
		{
			case R.id.radioButton1:
				current = 0 ;
				break ;
			case R.id.radioButton2:
				current = 1 ;
				break;
			case R.id.radioButton3:
				current = 2 ;
				break;
			case R.id.radioButton4:
				current = 3 ;
				break;
		}
		if(viewPager.getCurrentItem() != current){
			viewPager.setCurrentItem(current);
		}

	}

	public class MyAdapter extends FragmentPagerAdapter{

		public MyAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}
		@Override
		public Fragment getItem(int position) {
			return fList.get(position);
		}
		@Override
		public int getCount() {
			return fList.size();
		}
	}

	public class MyListener implements OnPageChangeListener{
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		@Override
		public void onPageSelected(int arg0) {
			//获取当前页面用于改变对应RadioButton的状态
			int current = viewPager.getCurrentItem() ;
			switch(current)
			{
				case 0:
					radioGroup.check(R.id.radioButton1);//将radiobutton设为选中
					break;
				case 1:
					radioGroup.check(R.id.radioButton2);
					break;
				case 2:
					radioGroup.check(R.id.radioButton3);
					break;
				case 3:
					radioGroup.check(R.id.radioButton4);
					break;
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
			case R.id.action_plus2:
				Intent intent= new Intent(HomeActivity.this,SearchActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				this.startActivity(intent);
				break;
			case R.id.action_plus3:
				logout();
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	private void logout(){
		Log.v("logout","logout");
		/*AsynHttp asynObject=new AsynHttp(Connectinfo.logouturl,this.getApplicationContext(),this);
		asynObject.GetData();*/

		//创建post请求
		MyVolleyRequest mr=new MyVolleyRequest(Connectinfo.logouturl, this);
		//将请求添加到Volley请求队列
		MyApplication.getInstance().addToRequestQueue(mr);
	}
	
/*	@Override
	public void callBackFunction(JSONObject jsonobj) {
		// TODO Auto-generated method stub
		if(jsonobj!=null){
			Log.v("logout",jsonobj.toString());
			try {
				if(jsonobj.getString("logoutFlag").equals("1")){//退出成功
					PrefStore pref = PrefStore.getInstance(this);
					pref.clearPref();
					//ApplicationManager.getInstance().exit();
					this.finish();
				}
				else{
					Log.v("logout", "退出登录失败");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}*/

	@Override
	public void onResponse(JSONObject response) {
		// TODO Auto-generated method stub
		if(response!=null){
			Log.v("logout",response.toString());

			if(response.optString("logoutFlag").equals("1")){//退出成功
				PrefStore pref = PrefStore.getInstance(this);
				pref.clearPref();
				//ApplicationManager.getInstance().exit();
				this.finish();
			}
			else{
				Log.v("logout", "退出登录失败");
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(System.currentTimeMillis()-startTime > 2000){
				Toast.makeText(this, "再按一次退出本程序", Toast.LENGTH_SHORT).show();
				startTime=System.currentTimeMillis();
			}
			else{
				//ApplicationManager.getInstance().exit();
				this.finish();
			}
		}
		return true;
	}

	// 强制actionbar显示overflow菜单
	// force to show overflow menu in actionbar for android 4.4 below
	private void getOverflowMenu() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		MyApplication.getInstance().cancelPendingRequests();
	}

}
