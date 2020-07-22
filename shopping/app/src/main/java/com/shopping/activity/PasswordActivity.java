package com.shopping.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.shopping.http.HttpUtil;

public class PasswordActivity extends Activity {

	private SharedPreferences mSharedPreferences;
	
	private String username;
	
	private EditText password_password1;
	private EditText password_password2;
	private EditText password_password3;

	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.password);
	        
	        mSharedPreferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
	        
			username = mSharedPreferences.getString("username", null);
			
			password_password1 = (EditText) findViewById(R.id.password_password1);
			password_password2 = (EditText) findViewById(R.id.password_password2);
			password_password3 = (EditText) findViewById(R.id.password_password3);
	        
	 }

	
	//修改密码按钮响应函数
	public void password_system(View v){
		
		String password1 = password_password1.getText().toString();
		String password2 = password_password2.getText().toString();
		String password3 = password_password3.getText().toString();
		
		
		if(password1==null||"".equals(password1)){
			new AlertDialog.Builder(PasswordActivity.this).setTitle("错误提示").setMessage("原密码不能为空\n请重新输入").create().show();
			return;
		}
		if(password2==null||"".equals(password2)){
			new AlertDialog.Builder(PasswordActivity.this).setTitle("错误提示").setMessage("新密码不能为空\n请重新输入").create().show();
			return;
		}
		
		if(!password2.equals(password3)){
			new AlertDialog.Builder(PasswordActivity.this).setTitle("错误提示").setMessage("确认新密码和新密码不一致\n请重新输入").create().show();
			return;
		}
		
		Map<String, String> parameter = new HashMap<String, String>();
		
		parameter.put("username",username );
		parameter.put("password1", password1);
		parameter.put("password2", password2);
		
				
		String response = HttpUtil.sendRequest(HttpUtil.passwordedit, parameter);
		
		if("fail".equals(response)){
			new AlertDialog.Builder(PasswordActivity.this).setTitle("错误提示").setMessage("原密码错误\n请重新输入").create().show();
			return;
			
		}
		
		if("success".equals(response)){
			Toast.makeText(PasswordActivity.this, "修改成功", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(PasswordActivity.this,LoginActivity.class);
			startActivity(intent);
			this.finish();
			
		}
			 
	}
	
	
	
	//返回按钮响应函数
	public void passwordback_system(View v){
		
		PasswordActivity.this.finish();
	}
	
	
	
	
}
