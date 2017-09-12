package com.example.neu.shopapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.example.neu.baseinfo.Connectinfo;
import com.example.neu.util.MyApplication;
import com.example.neu.util.MyVolleyRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegActivity extends AppCompatActivity implements Listener<JSONObject>{

	private EditText usernameEt,passwordEt,ageEt,mailEt;
	private RadioGroup sexRg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reg);

		usernameEt=(EditText)findViewById(R.id.RegUserName);
		passwordEt=(EditText)findViewById(R.id.RegPassWord);
		ageEt=(EditText)findViewById(R.id.RegAge);
		mailEt=(EditText)findViewById(R.id.RegMail);
		sexRg=(RadioGroup)findViewById(R.id.RegSex);

		Button regBtn=(Button)findViewById(R.id.RegBtn);
		regBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String username=usernameEt.getText().toString();
				String password=passwordEt.getText().toString();
				String age=ageEt.getText().toString();
				String mail=mailEt.getText().toString();

				RadioButton rb=(RadioButton)findViewById(
						sexRg.getCheckedRadioButtonId());
				String s=rb.getText().toString();
				String sex="0";
				if(s.equals("女")){
					sex="1";
				}
				Log.v("reg", username+" "+password+" "+sex);
				Map<String,String> params=new
						HashMap<String,String>();
				params.put("userName", username);
				params.put("userPass", password);
				params.put("userAge", age);
				params.put("userSex", sex);
				params.put("userEmail", mail);

				MyVolleyRequest mr=new MyVolleyRequest(
						Connectinfo.regurl,params,RegActivity.this);
				MyApplication.getInstance().addToRequestQueue(mr);
			}

		});

	}


	@Override
	public void onResponse(JSONObject response) {
		// TODO Auto-generated method stub
		if(response!=null){
			if(response.optString("regFlag").equals("1")){//注册成功
				Toast.makeText(this, "注册成功",
						Toast.LENGTH_SHORT).show();
				String username=usernameEt.getText().toString();
				String password=passwordEt.getText().toString();
				Intent intent=new Intent();
				intent.putExtra("username", username);
				intent.putExtra("password", password);
				this.setResult(Activity.RESULT_OK, intent);
				this.finish();
			}
			else{//注册失败
				Toast.makeText(this, response.optString("msg"),
						Toast.LENGTH_SHORT).show();
			}
		}
	}

}
