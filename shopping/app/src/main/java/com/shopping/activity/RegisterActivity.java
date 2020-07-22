package com.shopping.activity;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.shopping.http.HttpUtil;

public class RegisterActivity extends Activity {

	
	private EditText editUsername;
	private EditText editPassword;
	private EditText editPassword2;
	private EditText editTruename;
	private EditText editPhone;
	
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		 //��android2.3֮��ͬ����ȡ�������ݷ�������Ĳ����� ��������2�д���
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());	
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().build());
		
		editUsername = (EditText)findViewById(R.id.editUsername);
		editPassword = (EditText)findViewById(R.id.editPassword);
		editPassword2 = (EditText)findViewById(R.id.editPassword2);
		editTruename = (EditText)findViewById(R.id.editTruename);
		editPhone = (EditText)findViewById(R.id.editPhone);
		
	}

	
	
	
	//ע�ᰴť��Ӧ����
		 public void register_system(View v){
			
			String username = editUsername.getText().toString();
			String password1 = editPassword.getText().toString();
			String password2 = editPassword2.getText().toString();
			String phone = editPhone.getText().toString();
			String truename = editTruename.getText().toString();
			
			if(username==null||"".equals(username)){
				new AlertDialog.Builder(RegisterActivity.this).setTitle("������ʾ").setMessage("�û�������Ϊ��\n����������").create().show();
				return;
			}
			if(password1==null||"".equals(password1)){
				new AlertDialog.Builder(RegisterActivity.this).setTitle("������ʾ").setMessage("���벻��Ϊ��\n����������").create().show();
				return;
			}
			
			if(!password1.equals(password2)){
				new AlertDialog.Builder(RegisterActivity.this).setTitle("������ʾ").setMessage("������������벻һ��\n����������").create().show();
				return;
			}
			 
			Map<String, String> parameter = new HashMap<String, String>();
				
			parameter.put("username",username );
			parameter.put("password", password1);
			parameter.put("phone", phone);
			parameter.put("truename", truename);
			
			
			String response = HttpUtil.sendRequest(HttpUtil.register, parameter);
			
			if("fail".equals(response)){
				new AlertDialog.Builder(RegisterActivity.this).setTitle("������ʾ").setMessage("���û����Ѿ�����\n����������").create().show();
				return;
				
			}
			
			if("success".equals(response)){
				Toast.makeText(RegisterActivity.this, "ע��ɹ�", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
				startActivity(intent);
				this.finish();
				
			}
			
			 
		}
		 
		 
		//���ذ�ť��Ӧ����
		 public void register_back(View v){
			Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
			startActivity(intent);
			
			this.finish();	
		 }

}
