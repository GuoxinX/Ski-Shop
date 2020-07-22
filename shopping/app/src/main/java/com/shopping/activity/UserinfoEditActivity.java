package com.shopping.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.shopping.http.HttpUtil;

public class UserinfoEditActivity extends Activity {

	private SharedPreferences mSharedPreferences;
	
	private String username;
	
	private EditText userinfo_phone;
	private EditText userinfo_truename;


	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.userinfoedit);
	        
	        mSharedPreferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
	        
			username = mSharedPreferences.getString("username", null);
			
			userinfo_phone = (EditText) findViewById(R.id.userinfo_phone);
			userinfo_truename = (EditText) findViewById(R.id.userinfo_truename);
			
			
			Map<String, String> parameter = new HashMap<String, String>();
		 	parameter.put("username", username);
		 	
		 	
			String result = HttpUtil.sendRequest(HttpUtil.userdetails, parameter);
			
			
			try {
				
				JSONArray tables = new JSONArray(result);

				for (int i = 0; i < tables.length(); i++) {

					JSONObject obj_tmp = tables.getJSONObject(i);
	
					userinfo_truename.setText(obj_tmp.getString("truename"));
					userinfo_phone.setText(obj_tmp.getString("phone"));
					
					
					
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("mobile", "格式转换错误");
			}
			
	        
	 }

	
	//修改密码按钮响应函数
	public void userinfoedit_system(View v){
		
		String phone = userinfo_phone.getText().toString();
		String truename = userinfo_truename.getText().toString();
		
		
		
		if(phone==null||"".equals(phone)){
			new AlertDialog.Builder(UserinfoEditActivity.this).setTitle("错误提示").setMessage("手机号不能为空\n请重新输入").create().show();
			return;
		}
		if(truename==null||"".equals(truename)){
			new AlertDialog.Builder(UserinfoEditActivity.this).setTitle("错误提示").setMessage("姓名不能为空\n请重新输入").create().show();
			return;
		}
		
		
		
		Map<String, String> parameter = new HashMap<String, String>();
		
		parameter.put("username",username );
		parameter.put("truename", truename);
		parameter.put("phone", phone);
		
				
		String response = HttpUtil.sendRequest(HttpUtil.userdedit, parameter);
		
		
		
		if("success".equals(response)){
			Toast.makeText(UserinfoEditActivity.this, "修改成功", Toast.LENGTH_LONG).show();
			
			UserinfoEditActivity.this.finish();
		}
			 
	}
	
	
	
	//返回按钮响应函数
	public void userinfoback_system(View v){
		
		UserinfoEditActivity.this.finish();
	}
	
	
	
	
}
