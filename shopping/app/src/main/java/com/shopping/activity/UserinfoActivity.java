package com.shopping.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.shopping.http.HttpUtil;


public class UserinfoActivity extends Activity {

	
	private SharedPreferences mSharedPreferences;
	
	private SharedPreferences.Editor mEditor;
	
	private TextView userinfo_username;
	private TextView userinfo_createtime;
	private TextView userinfo_truename;
	private TextView userinfo_phone;
	
	private String username;

	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.userinfo_details_view);
	        
	        mSharedPreferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
	        
			username = mSharedPreferences.getString("username", null);
			
			userinfo_username = (TextView)findViewById(R.id.userinfo_username);
			userinfo_createtime = (TextView)findViewById(R.id.userinfo_createtime);
			userinfo_truename = (TextView)findViewById(R.id.userinfo_truename);
			userinfo_phone = (TextView)findViewById(R.id.userinfo_phone);
			
			initData();
	        
	 }

	 void initData() {
		 Map<String, String> parameter = new HashMap<String, String>();
		 	parameter.put("username", username);
		 	
		 	
			String result = HttpUtil.sendRequest(HttpUtil.userdetails, parameter);
			
			
			try {
				
				JSONArray tables = new JSONArray(result);

				for (int i = 0; i < tables.length(); i++) {

					JSONObject obj_tmp = tables.getJSONObject(i);
					
					userinfo_username.setText(obj_tmp.getString("username"));
					userinfo_createtime.setText(obj_tmp.getString("createtime"));
					userinfo_truename.setText(obj_tmp.getString("truename"));
					userinfo_phone.setText(obj_tmp.getString("phone"));
					
					
					
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("mobile", "格式转换错误");
			}
		 
	 }
	 
	 
	 @Override
		protected void onRestart() {
			super.onRestart();
			initData();
			
		}
	 
	 //修改密码按钮响应函数
	 public void userinfo_password(View v){
		 Intent intent = new Intent(UserinfoActivity.this, PasswordActivity.class);
		 startActivity(intent);
		
		 
	 }
	 
	//编辑用户信息按钮响应函数
	public void userinfo_edit(View v){
			 Intent intent = new Intent(UserinfoActivity.this, UserinfoEditActivity.class);
			 startActivity(intent);
			
			 
	}

	//退出按钮响应函数
	public void loginout_system(View v){
		Intent intent = new Intent(UserinfoActivity.this, LoginActivity.class);
		startActivity(intent);
		
		this.finish();
		 
		mSharedPreferences = getSharedPreferences("SharedPreferences",
					Context.MODE_PRIVATE);
			
		mEditor = mSharedPreferences.edit();
		mEditor.putString("username", null);
		mEditor.commit();	
			
	}

}
