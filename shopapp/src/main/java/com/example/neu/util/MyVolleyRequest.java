package com.example.neu.util;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MyVolleyRequest extends Request<JSONObject>{
	private Map<String, String> params;
	private Response.Listener<JSONObject> listener;    
	private PrefStore pref;
	private int method;
	
	


	public MyVolleyRequest(String url, Listener<JSONObject> listener) {
		super(Method.POST, url, new MyErrorListener());  
		this.params = null;
		this.listener = listener;
	}
	
	public MyVolleyRequest(int method, String url, Listener<JSONObject> listener) {
		super(method, url, new MyErrorListener());  
		this.params = null;
		this.listener = listener;
		this.method = method;
	}
	
	public MyVolleyRequest(String url, Map<String, String> params, Listener<JSONObject> listener) {
		super(Method.POST, url, new MyErrorListener());  
		this.params = params;
		this.listener = listener;		
	}	
	
	public MyVolleyRequest(int method, String url, Map<String, String> params, Listener<JSONObject> listener) {
		super(method, url, new MyErrorListener());  
		this.params = params;
		this.listener = listener;
		this.method = method;
	}
	
	public MyVolleyRequest(String url, Map<String, String> params, Listener<JSONObject> listener, ErrorListener errorListener) {
		super(Method.POST, url, errorListener);  
		this.params = params;
		this.listener = listener;		
	}	
	
	public MyVolleyRequest(int method, String url, Map<String, String> params, Listener<JSONObject> listener, ErrorListener errorListener) {
		super(method, url, errorListener);  
		this.params = params;
		this.listener = listener;
		this.method = method;
	}
	
	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	//当http请求是post时，会调用该函数读取请求参数     
	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		// TODO Auto-generated method stub
		return params;
	}
	
	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		// TODO Auto-generated method stub
		JSONObject jsonObject=null;
		try {            
			String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));    
			jsonObject = new JSONObject(jsonString);     
			
	        Map<String, String> responseHeaders = response.headers;
	        String rawCookies = responseHeaders.get("Set-Cookie");
	        Log.d("sessionid", "rawCookies----------------" + rawCookies);
	        if(rawCookies!=null){
		        //将sessionid存入本地sharedpreference
		        pref = PrefStore.getInstance();
		        String sessionId = rawCookies.substring(0, rawCookies.indexOf(";"));
		        pref.savePref("sessionId", sessionId);
		        Log.d("sessionid", "sessionid----------------" + sessionId);
		        //将cookie字符串添加到jsonObject中，该jsonObject会被deliverResponse递交，调用请求时则能在onResponse中得到            		           
		        jsonObject.put("Cookie",rawCookies);     
		        Log.d("sessionid","jsonObject----------------"+ response.toString()); 
	        }
			
		    return Response.success(jsonObject,  HttpHeaderParser.parseCacheHeaders(response));        
			
		}catch (UnsupportedEncodingException e) { 
			return Response.error(new ParseError(e));        
		} catch (JSONException je) {            
			return Response.error(new ParseError(je));        
		}    

	}

	@Override
	protected void deliverResponse(JSONObject response) {
		// TODO Auto-generated method stub
		listener.onResponse(response);    
	}
	
	//重写getHeaders 默认的key为cookie，value则为sharedpreference中保存的值
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
    	pref = PrefStore.getInstance();
    	String sessionId=pref.getPref("sessionId", "");
        if (sessionId.length() > 0) {
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("cookie", sessionId);
            Log.d("sessionid", "headers----------------" + headers);
            return headers;
        } else {
            return super.getHeaders();
        }
    }
}
