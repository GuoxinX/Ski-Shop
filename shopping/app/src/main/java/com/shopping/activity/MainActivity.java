package com.shopping.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class MainActivity extends Activity {

	private SharedPreferences mSharedPreferences;
	private String username;
	
	

	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.activity_main);
	        
	        mSharedPreferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
	        
	        username = mSharedPreferences.getString("username", null);
			
			
			TextView tv = (TextView)findViewById(R.id.waitingText);
			tv.setText("你好,"+username+",欢迎登录购物商城！！");
			
	        
	        new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					
					Intent intent = new Intent(MainActivity.this , TabHostActivity.class);
					MainActivity.this.startActivity(intent);
					MainActivity.this.finish();
				}
			}, 1000);
	    }

	

}
