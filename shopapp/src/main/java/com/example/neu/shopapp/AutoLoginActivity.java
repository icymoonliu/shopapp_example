package com.example.neu.shopapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.example.neu.baseinfo.Connectinfo;
import com.example.neu.util.MyApplication;
import com.example.neu.util.MyVolleyRequest;
import com.example.neu.util.PrefStore;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AutoLoginActivity extends AppCompatActivity implements Listener<JSONObject>{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auto_login);
		ImageView iv=(ImageView)this.findViewById(R.id.imageView1);
		iv.setImageBitmap(readBitMap(this,R.drawable.login_logo));

		PrefStore pref = PrefStore.getInstance();
		if(pref.getPref("authority",null)!=null){//保存过自动登录的信息
			/*AsynHttp asynObject = new AsynHttp(Connectinfo.autologinurl,this, this,true);

			List<NameValuePair> data = new ArrayList<NameValuePair>();
			data.add(new BasicNameValuePair("userName", pref.getPref("userName",null)));
			data.add(new BasicNameValuePair("authority", pref.getPref("authority",null)));

			asynObject.set_data(data);
			asynObject.GetData();*/

			//创建post请求
			Map<String,String> params = new HashMap<String,String>();
			params.put("userName", pref.getPref("userName",null)==null?"":pref.getPref("userName",null));
			params.put("authority", pref.getPref("authority",null)==null?"":pref.getPref("authority",null));
			MyVolleyRequest mr=new MyVolleyRequest(Connectinfo.autologinurl, params, this);
			//将请求添加到Volley请求队列
			MyApplication.getInstance().addToRequestQueue(mr,"autologin");
		}
		else{
			this.startActivity(new Intent(this,MainActivity.class));
			this.finish();//关闭当前Activity
		}

	}

	/**  * 以最省内存的方式读取本地资源的图片  * @param context  * @param resId  * @return  */
	public static Bitmap readBitMap(Context context, int resId){
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		//获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is,null,opt);
	}

	/*@Override
	public void callBackFunction(JSONObject jsonobj) {
		// TODO Auto-generated method stub
		if(jsonobj!=null){
			Log.v("autologinreturnjson",jsonobj.toString());
			try {
				if(jsonobj.getString("autoLoginFlag").equals("1")){//自动登录成功
					Intent intent=new Intent(this,HomeActivity.class);
					startActivity(intent);
				}
				else{//自动登录失败
					Toast.makeText(this, jsonobj.getString("msg"),
							Toast.LENGTH_SHORT).show();
					PrefStore pref = PrefStore.getInstance(this);
					pref.removePref("userName");
					pref.removePref("authority");
					Intent intent=new Intent(this,MainActivity.class);
					startActivity(intent);
				}
				this.finish();

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
			Log.v("autologinreturnjson",response.toString());

			if(response.optString("autoLoginFlag").equals("1")){//自动登录成功
				Intent intent=new Intent(this,HomeActivity.class);
				startActivity(intent);
			}
			else{//自动登录失败
				Toast.makeText(this, response.optString("msg"),
						Toast.LENGTH_SHORT).show();
				PrefStore pref = PrefStore.getInstance(this);
				pref.removePref("userName");
				pref.removePref("authority");
				Intent intent=new Intent(this,MainActivity.class);
				startActivity(intent);
			}
			this.finish();
		}
	}
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		MyApplication.getInstance().cancelPendingRequests("autologin");
	}
}
