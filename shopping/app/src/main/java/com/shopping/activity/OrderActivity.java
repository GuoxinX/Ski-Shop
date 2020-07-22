package com.shopping.activity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import com.shopping.http.HttpUtil;
import com.shopping.vo.Order;



public class OrderActivity extends Activity {

	private List<Order> list = new ArrayList<Order>() ;

	public ListView showList;
	
	private SharedPreferences mSharedPreferences;
	
	private SharedPreferences.Editor mEditor;
	
	private String username = "";  
	
	private EditText order_orderid;
	
	private  String[] TYPE_SEL = {"所有状态","处理中","已发货","确认收货","取消订单"};
	
	
	private ArrayAdapter disAdapter;
	
	public Spinner disSpi;
	
	private int selType;
	

	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.order);
			
	        order_orderid = (EditText)findViewById(R.id.order_orderid);
	        
			showList = (ListView) findViewById(R.id.order_list_content);
			
			showList.setOnItemClickListener(new MyListItemListener());
			
			mSharedPreferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
	        
	        username = mSharedPreferences.getString("username", null);
			
			initData(null,null);
			
			disSpi = (Spinner) findViewById(R.id.name_sel_spinner);
			disAdapter = new ArrayAdapter<String>(this,
					R.layout.spinner_text, TYPE_SEL);
	        
			disAdapter
			.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			disSpi.setAdapter(disAdapter);
			
			disSpi.setSelection(0);
			disSpi.setOnItemSelectedListener(new SpinnerSelectedListener());
			disSpi.setVisibility(View.VISIBLE);
	        
	    }
	 
	 
	 @Override
	protected void onRestart() {
		super.onRestart();
		initData(null,null);
		
	}
	 
	//退出按钮响应函数
	public void loginout_system(View v){
			Intent intent = new Intent(OrderActivity.this, LoginActivity.class);
			startActivity(intent);
			
			this.finish();
			 
			mSharedPreferences = getSharedPreferences("SharedPreferences",
						Context.MODE_PRIVATE);
				
			mEditor = mSharedPreferences.edit();
			mEditor.putString("username", null);
			mEditor.commit();	
				
	}

	 
	 public final class MyListItemListener implements OnItemClickListener {
			
			public void onItemClick(AdapterView<?> view, View arg1,
					final int position, long arg3) {
				
				Intent intent = new Intent(OrderActivity.this ,OrderViewActivity.class);
				
				intent.putExtra("id",  String.valueOf(list.get(position).getId()));
				
				
				
				OrderActivity.this.startActivity(intent);

			}
	}
	 
	 
	 void initData(String orderid,String status) {
		 	list.clear();
		 	
		 	
		 	Map<String, String> parameter = new HashMap<String, String>();
		 	parameter.put("orderid", orderid);
		 	parameter.put("status", status);
		 	parameter.put("username", username);
		 
		 	
			String result = HttpUtil.sendRequest(HttpUtil.orderlist, parameter);
			
			
			
			try {
				
				JSONArray tables = new JSONArray(result);

				for (int i = 0; i < tables.length(); i++) {

					JSONObject obj_tmp = tables.getJSONObject(i);
					Order bean = new Order();
					bean.setId(obj_tmp.getInt("id"));
					bean.setOrderid(obj_tmp.getString("orderid"));
					bean.setStatus(obj_tmp.getString("status"));
					

					list.add(bean);
					
					
				}

				showList.setAdapter(new Myadapter(OrderActivity.this, list));

			} catch (JSONException e) {
				
				e.printStackTrace();
				Log.e("mobile", "格式转换错误");
			}
		}
	
	 
	 private class Myadapter extends BaseAdapter {
			private Context c;
			private List<Order> list;

			public Myadapter(Context c, List<Order> list) {
				this.c = c;
				this.list = list;
			}

			public int getCount() {
				// TODO Auto-generated method stub
				return list.size();
			}

			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return list.get(position);
			}

			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				View v = LayoutInflater.from(c).inflate(R.layout.order_list_item,
						null);

				TextView tv1 = (TextView) v.findViewById(R.id.order_orderid);
				TextView tv3 = (TextView) v.findViewById(R.id.order_status);
				

				tv1.setText(list.get(position).getOrderid());

				tv3.setText(list.get(position).getStatus());
				
				return v;
			}

		}
	 
	 
	//搜索按钮响应函数
	public void order_search(View v){
					
		String orderid = order_orderid.getText().toString();
					
				
		initData(orderid,null);
					
				
					
	}
	
	
	class SpinnerSelectedListener implements OnItemSelectedListener {
		 
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			
			String status = null;
			
			selType = arg2;
			
			if (selType ==1 ){
				status ="处理中";
				
			}else if (selType ==2 ){
				status ="已发货";
				
			}else if (selType ==3 ){
				status ="确认收货";
				
			}else if (selType ==4 ){
				status ="取消订单";
				
			}
			
			initData(null,status);
				

		}

		public void onNothingSelected(AdapterView<?> arg0) {

		}

	}

}
