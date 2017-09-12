package com.example.neu.util;

import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.example.neu.baseinfo.Const;


public class MyErrorListener implements ErrorListener{

	@Override
	public void onErrorResponse(VolleyError error) {
		// TODO Auto-generated method stub
		if(error!=null){
			if(error.networkResponse!=null){
				//Log.e("Volley error:", error.getMessage());
				byte[] htmlBodyBytes = error.networkResponse.data;
				//Log.e("Volley error:", new String(htmlBodyBytes), error);
			}
			String errorMsg;
			if(error instanceof TimeoutError){
				errorMsg= Const.TIMEOUT_ERROR;
			}
			else if(error instanceof NetworkError){
				errorMsg=Const.NETWORK_ERROR;
			}
			else if(error instanceof ParseError){
				errorMsg=Const.PARSE_ERROR;
			}
			else if(error instanceof NoConnectionError){
				errorMsg=Const.NO_CONNECTION_ERROR;
			}
			else if((error instanceof ServerError) || (error instanceof AuthFailureError)){
				NetworkResponse response = error.networkResponse;
				errorMsg=Const.Server_ERROR+response.statusCode;
			}
			else{
				errorMsg="Volley请求失败"+error;
			}
			Toast.makeText(MyApplication.getContext(), errorMsg,Toast.LENGTH_SHORT).show();
		}
	}

}
