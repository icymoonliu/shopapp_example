package com.example.neu.shopapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.neu.util.AmountView;

import java.util.List;

public class SizeColorActivity extends AppCompatActivity {

	private AmountView num;
	private Spinner size,color;
	private List<String> data1,data2;
	private String selNum="1",selSize,selColor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_size_color);
		num = (AmountView) findViewById(R.id.num);
		num.setGoods_storage(50);
		num.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
			@Override
			public void onAmountChange(View view, int amount) {
				//Toast.makeText(getApplicationContext(), "Amount=>  " + amount, Toast.LENGTH_SHORT).show();
				selNum=amount+"";
			}
		});
		Intent intent=this.getIntent();
		data1=intent.getStringArrayListExtra("sizes");
		data2=intent.getStringArrayListExtra("colors");
		String goods_size=intent.getStringExtra("goods_size");
		String goods_color=intent.getStringExtra("goods_color");
		String goods_num=intent.getStringExtra("goods_num");
		System.out.println("n:"+goods_num+";s:"+goods_size+";c:"+goods_color);
		num.setAmount(Integer.parseInt(goods_num));
		size=(Spinner)findViewById(R.id.size);
		ArrayAdapter<String> adapter1=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,data1);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		size.setAdapter(adapter1);
		size.setSelection(data1.indexOf(goods_size));
		size.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				selSize=data1.get(arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		color=(Spinner)findViewById(R.id.color);
		ArrayAdapter<String> adapter2=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,data2);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		color.setAdapter(adapter2);
		color.setSelection(data2.indexOf(goods_color));
		color.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				selColor=data2.get(arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		Button button=(Button)findViewById(R.id.ok);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.v("click", "result:"+selNum+","+selSize+","+selColor);
				Intent intent=new Intent();
				intent.putExtra("selNum", selNum);
				intent.putExtra("selSize", selSize);
				intent.putExtra("selColor", selColor);
				System.out.println("result:"+selNum+","+selSize+","+selColor);
				SizeColorActivity.this.setResult(Activity.RESULT_OK, intent);
				SizeColorActivity.this.finish();
			}
		});
	}




}
