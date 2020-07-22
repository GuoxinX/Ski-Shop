package com.shopping.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shopping.http.HttpUtil;


public class OrderViewActivity extends Activity {

	
	private TextView orderview_orderid;
	private TextView orderview_createtime;
	private TextView orderview_name;
	private TextView orderview_address;
	private TextView orderview_phone;
	private TextView orderview_totalprice;
	private TextView orderview_status;
	private TextView orderview_details;
	
	private String id;
	
	private LinearLayout linearLayoutqueren;
	private LinearLayout linearLayoutquxiao;
	

	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.orderview);
	        
	        Intent intent=getIntent();
			Bundle bundle=intent.getExtras();
			id = bundle.getString("id");
			
			linearLayoutqueren = (LinearLayout)findViewById(R.id.linearLayoutqueren);
			linearLayoutquxiao = (LinearLayout)findViewById(R.id.linearLayoutquxiao);
			
			orderview_orderid = (TextView)findViewById(R.id.orderview_orderid);
			orderview_createtime = (TextView)findViewById(R.id.orderview_createtime);
			orderview_name = (TextView)findViewById(R.id.orderview_name);
			orderview_address = (TextView)findViewById(R.id.orderview_address);
			orderview_phone = (TextView)findViewById(R.id.orderview_phone);
			orderview_totalprice = (TextView)findViewById(R.id.orderview_totalprice);
			orderview_status = (TextView)findViewById(R.id.orderview_status);
			orderview_details = (TextView)findViewById(R.id.orderview_details);
			
			Map<String, String> parameter = new HashMap<String, String>();
		 	parameter.put("id", id);
		 	
		 	
			String result = HttpUtil.sendRequest(HttpUtil.orderdetails, parameter);
			
			String status = "";
			
			try {
				
				JSONArray tables = new JSONArray(result);

				for (int i = 0; i < tables.length(); i++) {

					JSONObject obj_tmp = tables.getJSONObject(i);
					
					orderview_orderid.setText(obj_tmp.getString("orderid"));
					orderview_createtime.setText(obj_tmp.getString("createtime"));
					orderview_name.setText(obj_tmp.getString("name"));

					orderview_address.setText(obj_tmp.getString("address"));
					orderview_phone.setText(obj_tmp.getString("phone"));
					orderview_totalprice.setText("￥"+obj_tmp.getString("totalprice"));
					orderview_status.setText(obj_tmp.getString("status"));
					orderview_details.setText("订单明细："+obj_tmp.getString("details"));
					
					status = obj_tmp.getString("status");
					
				}

			} catch (JSONException e) {
			
				e.printStackTrace();
				Log.e("mobile", "格式转换错误");
			}
			
			
			if("处理中".equals(status)){
				linearLayoutquxiao.setVisibility(View.VISIBLE);
				linearLayoutqueren.setVisibility(View.GONE);
			}
			if("已发货".equals(status)){
				linearLayoutqueren.setVisibility(View.VISIBLE);
				linearLayoutquxiao.setVisibility(View.GONE);
			}
			
	        
	    }

	
	 	//返回按钮响应函数
		 public void product_back(View v){
			 OrderViewActivity.this.finish();
			 
		 }
		 
		//确认收货响应函数
		public void queren_system(View v){
			

			Map<String, String> parameter = new HashMap<String, String>();

		 	parameter.put("id", id);
		 	
		 	
			
		 	String result = HttpUtil.sendRequest(HttpUtil.orderstatusupdate, parameter);
		 	
		 	
		 	if("fail".equals(result)){
				new AlertDialog.Builder(OrderViewActivity.this).setTitle("错误提示").setMessage("只有已发货的订单才能确认收货").create().show();
				return;
				
			}
		 	
		 	if("success".equals(result)){
				Toast.makeText(OrderViewActivity.this, "操作成功", Toast.LENGTH_LONG).show();
				OrderViewActivity.this.finish();
				
			}
		 	
		 	
		 	
		 	
		}
		
		
		//取消订单响应函数
		public void quxiao_system(View v){
					

					Map<String, String> parameter = new HashMap<String, String>();

				 	parameter.put("id", id);
				 	
				 	
					
				 	String result = HttpUtil.sendRequest(HttpUtil.orderstatusupdate2, parameter);
				 	
				 	
				 	if("fail".equals(result)){
						new AlertDialog.Builder(OrderViewActivity.this).setTitle("错误提示").setMessage("只有处理中的订单才能取消订单").create().show();
						return;
						
					}
				 	
				 	if("success".equals(result)){
						Toast.makeText(OrderViewActivity.this, "操作成功", Toast.LENGTH_LONG).show();
						OrderViewActivity.this.finish();
						
					}
				 	
				 	
				 	
				 	
				}
		
}
