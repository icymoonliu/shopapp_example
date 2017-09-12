package com.example.neu.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PrefStore {

	private static final String STORE_NAME = "Settings";
	private static Context mContext = null;
	private static PrefStore instance = null;

	public static PrefStore getInstance(Context context) {
		if (instance == null) {
			instance = new PrefStore(context);
		}
		return instance;
	}
	public static PrefStore getInstance() {
		if (instance == null) {
			instance = new PrefStore(MyApplication.getContext());
		}
		return instance;
	}
	public PrefStore(Context context) {
		mContext = context.getApplicationContext();
	}

	public boolean savePref(String key, String value) {

		if (mContext != null) {
			SharedPreferences pref = mContext.getSharedPreferences(STORE_NAME,
					Context.MODE_PRIVATE);
			Editor editor = pref.edit();
			editor.putString(key, value);
			editor.commit();

			// pref.edit().putString(key, value).commit();
			return true;
		} else {
			return false;
		}

	}

	public String getPref(String key, String defValue) {

		if (mContext != null) {
			SharedPreferences pref = mContext.getSharedPreferences(STORE_NAME,
					Context.MODE_MULTI_PROCESS);
			return pref.getString(key, defValue);
		} else {
			return null;
		}

	}
	
	public boolean removePref(String key){
		if (mContext != null) {
			SharedPreferences pref = mContext.getSharedPreferences(STORE_NAME,
					Context.MODE_PRIVATE);
			Editor editor = pref.edit();
			editor.remove(key);
			editor.commit();

			// pref.edit().putString(key, value).commit();
			return true;
		} else {
			return false;
		}
	}
	
	public boolean clearPref(){
		if (mContext != null) {
			SharedPreferences pref = mContext.getSharedPreferences(STORE_NAME,
					Context.MODE_MULTI_PROCESS);
			Editor editor=pref.edit();
			editor.clear();
			editor.commit();
			return true;
		}
		else {
			return false;
		}
	}

}
