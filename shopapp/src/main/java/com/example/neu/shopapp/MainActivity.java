package com.example.neu.shopapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.example.neu.baseinfo.Connectinfo;
import com.example.neu.util.Md5Util;
import com.example.neu.util.MyApplication;
import com.example.neu.util.MyVolleyRequest;
import com.example.neu.util.PrefStore;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements Listener<JSONObject>{

	private Button btnLogin;
	private EditText usernameEd,passwordEd;
	private CheckBox isAutoLoginCB;
	private boolean isAutoLogin;
	private String userName,userPassword;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		//ApplicationManager.getInstance().addActivity(this);

		btnLogin=(Button)findViewById(R.id.btnLogin);
		usernameEd=(EditText)this.findViewById(R.id.editText1);
		passwordEd=(EditText)this.findViewById(R.id.editText2);

		isAutoLoginCB=(CheckBox)findViewById(R.id.checkBox1);

		usernameEd.setText("user");
		passwordEd.setText("111");

		usernameEd.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				String username=usernameEd.getText().toString();
				if(TextUtils.isEmpty(username)){
					usernameEd.setError("请输入用户名");
				}
			}
		});

		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// 获得用户名和密码
				userName = usernameEd.getText().toString();
				userPassword = passwordEd.getText().toString();
				isAutoLogin=isAutoLoginCB.isChecked();
					
				/*AsynHttp asynObject = new AsynHttp(Connectinfo.loginurl,MainActivity.this, MainActivity.this);	
				List<NameValuePair> data = new ArrayList<NameValuePair>();
				data.add(new BasicNameValuePair("userName", userName));
				data.add(new BasicNameValuePair("userPass", userPassword));
					
				asynObject.set_data(data);
				asynObject.GetData();	*/

				//创建post请求
				Map<String,String> params = new HashMap<String,String>();
				params.put("userName", userName);
				params.put("userPass", userPassword);
				MyVolleyRequest mr=new MyVolleyRequest(Connectinfo.loginurl, params, MainActivity.this);
				//将请求添加到Volley请求队列
				MyApplication.getInstance().addToRequestQueue(mr,"login");
			}
		});
		//给注册用户组件添加单击事件处理
		TextView reg=(TextView)findViewById(R.id.register);
		reg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(MainActivity.this,
						RegActivity.class);
				startActivityForResult(intent,200);

			}
		});
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		MyApplication.getInstance().cancelPendingRequests("login");
	}

/*	@Override
	public void callBackFunction(JSONObject jsonobj) {
		// TODO Auto-generated method stub
		if(jsonobj!=null){	
			try {
				if(jsonobj.getString("loginFlag").equals("1")){
					Intent intent=new Intent(MainActivity.this,HomeActivity.class);
					startActivity(intent);
					this.finish();//关闭当前登录窗口

					if(isAutoLogin){//选中自动登录
						//authority格式：MD5加密过的(username+’,’+pwd)（MD5为32位小写）
						PrefStore pref = PrefStore.getInstance(MainActivity.this);
						pref.savePref("userName", userName);
						pref.savePref("authority", Md5Util.getMD5Str(userName+","+Md5Util.getMD5Str(userPassword)));
						Log.v("authority",Md5Util.getMD5Str(userName+","+Md5Util.getMD5Str(userPassword)));
					}				
				}
				else{
					Toast.makeText(MainActivity.this, jsonobj.getString("msg"),
							Toast.LENGTH_SHORT).show();
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		
	}*/


	//Volley请求成功的回调函数
	@Override
	public void onResponse(JSONObject response) {
		// TODO Auto-generated method stub
		if(response!=null){
			if(response.optString("loginFlag").equals("1")){
				Intent intent=new Intent(MainActivity.this,HomeActivity.class);
				startActivity(intent);
				this.finish();//关闭当前登录窗口
				PrefStore pref = PrefStore.getInstance(MainActivity.this);
				if(isAutoLogin){//选中自动登录
					//authority格式：MD5加密过的(username+’,’+pwd)（MD5为32位小写）				
					pref.savePref("userName", userName);
					pref.savePref("authority", Md5Util.getMD5Str(userName+","+Md5Util.getMD5Str(userPassword)));
					Log.v("authority",Md5Util.getMD5Str(userName+","+Md5Util.getMD5Str(userPassword)));
				}
				pref.savePref("curUserName", userName);
			}
			else{
				Toast.makeText(MainActivity.this, response.optString("msg"),
						Toast.LENGTH_SHORT).show();
			}
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==200){
			String username=data.getStringExtra("username");
			String password=data.getStringExtra("password");
			usernameEd.setText(username);
			passwordEd.setText(password);
		}
	}
}
