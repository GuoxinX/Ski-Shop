package com.shopping.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import android.widget.Gallery;
import android.widget.TextView;
import android.widget.Toast;

import com.shopping.adapter.PicDetailsGalleryAdapter;
import com.shopping.http.HttpUtil;
import com.shopping.vo.Pic;

public class ProductActivity extends Activity {

	
	private int id ;
	
	private TextView textname;
	private TextView textfenlei;
	private TextView textprice;

	private TextView textcreatetime;
	private TextView textinfo;
	
	
	
	private Gallery picDetailsGallery;
	
	private PicDetailsGalleryAdapter adapter;
	
	
	private SharedPreferences mSharedPreferences;
	

	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.product_details);
	        
	        Intent intent=getIntent();
			Bundle bundle=intent.getExtras();
			id = Integer.parseInt(bundle.getString("id"));
	        
			
			textname = (TextView)findViewById(R.id.textname);
			textfenlei = (TextView)findViewById(R.id.textfenlei);
			textprice = (TextView)findViewById(R.id.textprice);

			textcreatetime = (TextView)findViewById(R.id.textcreatetime);
			textinfo = (TextView)findViewById(R.id.textinfo);
			
			
			Map<String, String> parameter = new HashMap<String, String>();
		 	parameter.put("id", id+"");
		 	
		 	
			String result = HttpUtil.sendRequest(HttpUtil.productdetails, parameter);
			
			
			
			try {
				
				JSONArray tables = new JSONArray(result);

				for (int i = 0; i < tables.length(); i++) {

					JSONObject obj_tmp = tables.getJSONObject(i);
					
					textname.setText(obj_tmp.getString("name"));
					textfenlei.setText(obj_tmp.getString("fenlei"));
					textprice.setText("￥"+obj_tmp.getString("price"));

					textcreatetime.setText(obj_tmp.getString("createtime"));
					textinfo.setText("商品详情："+obj_tmp.getString("info"));
					
				}

			} catch (JSONException e) {
			
				e.printStackTrace();
				Log.e("mobile", "格式转换错误");
			}
			
			
			Map<String, String> parameter2 = new HashMap<String, String>();
		 	parameter2.put("pid", id+"");
			
			result = HttpUtil.sendRequest(HttpUtil.piclist, parameter2);
			
			List<Pic> piclist = new ArrayList<Pic>();
			
			try {
				
				JSONArray tables = new JSONArray(result);

				for (int i = 0; i < tables.length(); i++) {
					Pic pic = new Pic();
					
					JSONObject obj_tmp = tables.getJSONObject(i);
					
					pic.setImgpath(obj_tmp.getString("imgpath"));
					
					piclist.add(pic);
				}

			} catch (JSONException e) {

				e.printStackTrace();
				Log.e("mobile", "格式转换错误");
			}
	        
			picDetailsGallery = (Gallery) findViewById(R.id.picDetailsGallery);
			adapter = new PicDetailsGalleryAdapter(ProductActivity.this);
			picDetailsGallery.setAdapter(adapter);
			
			if(piclist.size()>0){
				StringBuffer sb = new StringBuffer();
				for(int i=0;i<piclist.size();i++){
					sb.append(HttpUtil.URL+"/uploadfile/"+piclist.get(i).getImgpath());
					
					if(i<piclist.size()-1){
						sb.append(",");
					}
					
				}
				
				String good_image= sb.toString();
				adapter.setPics_image(good_image.split(","));
				adapter.notifyDataSetChanged();
			}
			
			
			
	        
	    }

	
	 
	 //返回按钮响应函数
	 public void product_back(View v){
		 ProductActivity.this.finish();
		 
	 }
	 
	//加入到购物车响应函数
	public void addcart(View v){
		mSharedPreferences = getSharedPreferences("SharedPreferences",
				Context.MODE_PRIVATE);
		
		String username = mSharedPreferences.getString("username", null);
		
		Map<String, String> parameter = new HashMap<String, String>();

	 	parameter.put("username", username);
	 	parameter.put("pid", id+"");
	 	
	 	
		
	 	String result = HttpUtil.sendRequest(HttpUtil.cartadd, parameter);
	 	
	 	
	 	if("fail".equals(result)){
			new AlertDialog.Builder(ProductActivity.this).setTitle("错误提示").setMessage("该商品已经加入到购物车，请勿重复添加").create().show();
			return;
			
		}
	 	
	 	if("success".equals(result)){
			
			
			new AlertDialog.Builder(ProductActivity.this).setTitle("提示").setMessage("添加成功").create().show();
			return;
			
		}
	 	
	 	
	 	
	 	
	}

}
