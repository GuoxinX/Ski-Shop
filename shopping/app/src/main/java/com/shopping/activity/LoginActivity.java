package com.shopping.activity;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.shopping.http.HttpUtil;

public class LoginActivity extends Activity {
	
	
	private EditText login_user_edit;
	
	private EditText login_passwd_edit;
	
	private SharedPreferences mSharedPreferences;
	
	private SharedPreferences.Editor mEditor;
	
	

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().build());
		
		 login_user_edit = (EditText) findViewById(R.id.login_user_edit);
	        
	     login_passwd_edit = (EditText) findViewById(R.id.login_passwd_edit);
	}
	
	
	//注册按钮响应函数
	public void login_register(View v){
			
		Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
		startActivity(intent);
	}
	
	
	
	//登录按钮响应函数
		public void login_mobile_system(View v) { 
			String username = login_user_edit.getText().toString();
			String password = login_passwd_edit.getText().toString();
			
			Map<String, String> parameter = new HashMap<String, String>();
			
			parameter.put("username", username);
			parameter.put("password", password);

			String response = HttpUtil.sendRequest(HttpUtil.login, parameter);
			
			if(response==null){
				Toast.makeText(LoginActivity.this,"登录失败，用户名或者密码错误", Toast.LENGTH_LONG).show();
			}else if("fail".equals(response)){
				Toast.makeText(LoginActivity.this,"登录失败，用户名或者密码错误", Toast.LENGTH_LONG).show();
			}else {
				mSharedPreferences = getSharedPreferences("SharedPreferences",
						Context.MODE_PRIVATE);
				
				mEditor = mSharedPreferences.edit();
				mEditor.putString("username", username);
				mEditor.commit();
				
				Intent intent=new Intent();
				intent.setClass(LoginActivity.this, MainActivity.class);
				startActivityForResult(intent, 1);
				Toast.makeText(LoginActivity.this,"登录成功", Toast.LENGTH_LONG).show();
			}
			
			
		}
	
		
		//浏览商品响应函数
		public void vist(View v){
				
			Intent intent = new Intent(LoginActivity.this, VistActivity.class);
			startActivity(intent);
		}
	
}
