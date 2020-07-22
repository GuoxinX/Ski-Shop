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
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shopping.http.HttpUtil;
import com.shopping.util.MyBackAsynaTask;
import com.shopping.vo.Cart;


public class CartActivity extends Activity {

	private SharedPreferences mSharedPreferences;
	private String username;
	
	private RelativeLayout relativeLayoutNoGoods;
	private LinearLayout linearLayoutCartList;
	
	private ListView listViewShopCart;
	
	private CartListViewAdapter adapter;
	
	private List<Cart> cartList = new ArrayList<Cart>();
	
	private double sumPrice;
	
	public TextView textCartGoodsAllPrice;


	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.cart_view);
	        
	        mSharedPreferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
	        
	        username = mSharedPreferences.getString("username", null);
	        
	        textCartGoodsAllPrice = (TextView) findViewById(R.id.textCartGoodsAllPrice);
			
	        relativeLayoutNoGoods = (RelativeLayout) findViewById(R.id.relativeLayoutNoGoods);
			linearLayoutCartList = (LinearLayout) findViewById(R.id.linearLayoutCartList);
	        
	        listViewShopCart = (ListView) findViewById(R.id.listViewShopCart);
	        
	        adapter = new CartListViewAdapter(CartActivity.this,CartActivity.this);
			listViewShopCart.setCacheColorHint(Color.TRANSPARENT);
			listViewShopCart.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			loadingCartListData();
	        
	    }
	 
	 @Override
		protected void onRestart() {
			
			super.onRestart();
			loadingCartListData();
		}


	//加载购物车数据
	 public void loadingCartListData(){
		 	Map<String, String> parameter = new HashMap<String, String>();
			
			parameter.put("username", username);
			

			String result = HttpUtil.sendRequest(HttpUtil.cartlist, parameter);
			
			try {
				
				JSONArray tables = new JSONArray(result);
				
				if(tables.length()>0){
					linearLayoutCartList.setVisibility(View.VISIBLE);
					relativeLayoutNoGoods.setVisibility(View.GONE);
				}else{
					linearLayoutCartList.setVisibility(View.GONE);
					relativeLayoutNoGoods.setVisibility(View.VISIBLE);
				}
				
				cartList.clear();
				for (int i = 0; i < tables.length(); i++) {

					JSONObject obj_tmp = tables.getJSONObject(i);
					Cart bean = new Cart();
					
					
					bean.setId(obj_tmp.getString("id"));
					bean.setName(obj_tmp.getString("name"));
					bean.setPrice(obj_tmp.getString("price"));
					bean.setSl(obj_tmp.getString("sl"));
					bean.setTotal(obj_tmp.getString("total"));
					bean.setImgpath(obj_tmp.getString("imgpath"));
					
					cartList.add(bean);
					
				}
				
				sumPrice = 0;
		
				for(int i = 0 ; i< cartList.size() ;i ++){
					sumPrice += Double.parseDouble(cartList.get(i).getTotal());
				}
				
				textCartGoodsAllPrice.setText("总计:￥"+sumPrice);
			

				adapter.setCartLists(cartList);
				
				adapter.notifyDataSetChanged();
				
				

			} catch (JSONException e) {
			
				e.printStackTrace();
				Log.e("mobile", "格式转换错误");
			}
		
			
		}
	 
	 //删除购物车中的商品
	 public void cartDetele(String cart_id ){
		 
		Map<String, String> parameter = new HashMap<String, String>();
			
		parameter.put("cart_id", cart_id);
			

		String result = HttpUtil.sendRequest(HttpUtil.cartdelete, parameter);
		
		if("success".equals(result)){
			Toast.makeText(CartActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
			loadingCartListData();
		}else{
			Toast.makeText(CartActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
		}
		
		
			
	}
	 
	 
	//增加购物车中的商品
		 public void cartADD(String cart_id ){
			 
			Map<String, String> parameter = new HashMap<String, String>();
				
			parameter.put("cart_id", cart_id);
				

			String result = HttpUtil.sendRequest(HttpUtil.cartupdate, parameter);
			
			if("success".equals(result)){
				Toast.makeText(CartActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
				loadingCartListData();
			}else{
				Toast.makeText(CartActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
			}
			
			
				
		} 
		 
		 
		//减少购物车中的商品
		 public void cartMinus(String cart_id ){
			 
			Map<String, String> parameter = new HashMap<String, String>();
				
			parameter.put("cart_id", cart_id);
				

			String result = HttpUtil.sendRequest(HttpUtil.cartupdate2, parameter);
			
			if("success".equals(result)){
				Toast.makeText(CartActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
				loadingCartListData();
			}else{
				Toast.makeText(CartActivity.this, "操作失败，数量为1，不能在减少", Toast.LENGTH_SHORT).show();
			}
			
			
				
		}  
		 
		 
		 
		//提交订单按钮响应函数
		public void orderadd(View v){
					
			Intent intent = new Intent(CartActivity.this, OrderAddActivity.class);
			
			startActivity(intent);
		} 
		 
	 
	 
	 class CartListViewAdapter extends BaseAdapter{
			private Context context;
			private LayoutInflater inflater;
			public List<Cart> cartLists;
			public ViewHolder holder;
			private CartActivity cartActivity;
			
			public CartListViewAdapter(Context context,CartActivity cartActivity) {
				this.context = context;
				this.cartActivity = cartActivity;
				this.inflater = LayoutInflater.from(context);
			}
			
			@Override
			public int getCount() {
				return cartLists == null ? 0 : cartLists.size();
			}

			@Override
			public Object getItem(int position) {
				return cartLists.get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}
			
			public List<Cart> getCartLists() {
				return cartLists;
			}
			
			
			public void setCartLists(List<Cart> cartLists) {
				this.cartLists = cartLists;
			}
			
			
			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				if (null == convertView) {
					convertView = inflater.inflate(R.layout.cart_listivew_item, null);
					holder = new ViewHolder();
					holder.cart_productname = (TextView) convertView.findViewById(R.id.cart_productname);
					holder.cart_price = (TextView) convertView.findViewById(R.id.cart_price);
					holder.cart_sl = (TextView) convertView.findViewById(R.id.cart_sl);
					holder.cart_total = (TextView) convertView.findViewById(R.id.cart_total);
					holder.imageCartPic = (ImageView) convertView.findViewById(R.id.imageCartPic);
					holder.imageCartDetele = (ImageView) convertView.findViewById(R.id.imageCartDetele);
					
					holder.buttonGoodsNumberMinus = (Button) convertView.findViewById(R.id.buttonGoodsNumberMinus);
					holder.buttonGoodsNumberADD = (Button) convertView.findViewById(R.id.buttonGoodsNumberADD);

					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}

				final Cart bean =cartLists.get(position);
				
				holder.cart_productname.setText(bean.getName());
				
				holder.cart_price.setText("￥"+bean.getPrice());
				

				
				holder.cart_sl.setText(bean.getSl());
				
				holder.cart_total.setText("￥"+bean.getTotal());


				
				MyBackAsynaTask asynaTask = new MyBackAsynaTask(HttpUtil.URL+"/uploadfile/"+bean.getImgpath(), holder.imageCartPic);
				
				asynaTask.execute();
				
				//删除调用函数
				holder.imageCartDetele.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						cartActivity.cartDetele(bean.getId());
					}
				});
				
				
				//减少按钮调用函数
				holder.buttonGoodsNumberMinus.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						cartActivity.cartMinus(bean.getId());
					}
				});
				
				//增加按钮调用函数
				holder.buttonGoodsNumberADD.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						cartActivity.cartADD(bean.getId());
					}
				});
				
				
				
				return convertView;
			}
			
			public class ViewHolder {
				TextView cart_productname;
				TextView cart_price;
				TextView cart_sl;
				TextView cart_total;
				ImageView imageCartPic;
				ImageView imageCartDetele;
				Button buttonGoodsNumberMinus;
				Button buttonGoodsNumberADD;
				
			}
	 }
	 
}
