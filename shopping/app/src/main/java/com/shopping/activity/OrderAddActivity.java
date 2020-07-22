package com.shopping.activity;


import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.shopping.http.HttpUtil;


public class OrderAddActivity extends Activity {

	
	private EditText orderadd_name;
	private EditText orderadd_phone;
	private EditText orderadd_address;
	
	private SharedPreferences mSharedPreferences;
	
	private String username;

	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.orderadd);
	        
	        orderadd_name  = (EditText)findViewById(R.id.orderadd_name);
	        orderadd_phone = (EditText)findViewById(R.id.orderadd_phone);
	        orderadd_address = (EditText)findViewById(R.id.orderadd_address);
	        
	        mSharedPreferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
	        
			username = mSharedPreferences.getString("username", null);
			
			Map<String, String> parameter = new HashMap<String, String>();
		 	parameter.put("username", username);
		 	
		 	
			String result = HttpUtil.sendRequest(HttpUtil.userdetails, parameter);
			
			
			try {
				
				JSONArray tables = new JSONArray(result);

				for (int i = 0; i < tables.length(); i++) {

					JSONObject obj_tmp = tables.getJSONObject(i);
	
					orderadd_name.setText(obj_tmp.getString("truename"));
					orderadd_phone.setText(obj_tmp.getString("phone"));
					
					
					
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("mobile", "格式转换错误");
			}
	        
	    }

	//修改密码按钮响应函数
		public void orderadd_system(View v){
			
			String phone = orderadd_phone.getText().toString();
			String name = orderadd_name.getText().toString();
			String address = orderadd_address.getText().toString();
			
			
			
			if(phone==null||"".equals(phone)){
				new AlertDialog.Builder(OrderAddActivity.this).setTitle("错误提示").setMessage("收货人手机号不能为空\n请重新输入").create().show();
				return;
			}
			if(name==null||"".equals(name)){
				new AlertDialog.Builder(OrderAddActivity.this).setTitle("错误提示").setMessage("收货人姓名不能为空\n请重新输入").create().show();
				return;
			}
			if(address==null||"".equals(address)){
				new AlertDialog.Builder(OrderAddActivity.this).setTitle("错误提示").setMessage("收货地址\n请重新输入").create().show();
				return;
			}
			
			
			
			Map<String, String> parameter = new HashMap<String, String>();
			
			parameter.put("username",username );
			parameter.put("name", name);
			parameter.put("phone", phone);
			parameter.put("address", address);
			
					
			String response = HttpUtil.sendRequest(HttpUtil.orderformadd, parameter);
			
			
			
			if("success".equals(response)){
				Toast.makeText(OrderAddActivity.this, "添加成功，请点击我的订单查看", Toast.LENGTH_LONG).show();
				
				Intent intent = new Intent(OrderAddActivity.this,TabHostActivity.class);
				startActivity(intent);
				this.finish();
			}
				 
		}
		
		
		
		//返回按钮响应函数
		public void orderaddback_system(View v){
			
			OrderAddActivity.this.finish();
		}

}
