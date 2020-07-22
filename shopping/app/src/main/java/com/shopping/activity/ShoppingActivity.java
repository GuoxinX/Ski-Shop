package com.shopping.activity;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.shopping.http.HttpUtil;
import com.shopping.util.MyBackAsynaTask;
import com.shopping.vo.Fenlei;
import com.shopping.vo.Product;


public class ShoppingActivity extends Activity   {
	
	private List<Product> list = new ArrayList<Product>() ;

	public ListView showList;
	
	private SharedPreferences mSharedPreferences;
	
	private SharedPreferences.Editor mEditor;
	
	
	private List<Fenlei> fenleilist = new ArrayList<Fenlei>() ;
	
	private  String[] TYPE_SEL = null;
	
	
	private ArrayAdapter disAdapter;
	
	public Spinner disSpi;
	
	private int selType;
	
	private String fenlei = null;
	
	private EditText shopping_name;
	
	private GoodsListViewAdapter adapter;
	

	@SuppressLint("NewApi")
	@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	       
	        setContentView(R.layout.shopping);
	        //在android2.3之后同步获取网络数据访问网络的操作， 加入如下2行代码
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());	
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().build());
			
			shopping_name = (EditText)findViewById(R.id.shopping_name);
			
			showList = (ListView) findViewById(R.id.goodsListView);
			
			showList.setOnItemClickListener(new MyListItemListener());
			
			getFenlei();
			
			TYPE_SEL = new String[fenleilist.size()+1];
			
			TYPE_SEL[0] = "所有分类";
			for(int i=0;i<fenleilist.size();i++){
				TYPE_SEL[i+1] = fenleilist.get(i).getName();	
			}
			
		
			
			disSpi = (Spinner) findViewById(R.id.name_sel_spinner);
			disAdapter = new ArrayAdapter<String>(this,
					R.layout.spinner_text, TYPE_SEL);
	        
			disAdapter
			.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			disSpi.setAdapter(disAdapter);
			
			disSpi.setSelection(0);
			disSpi.setOnItemSelectedListener(new SpinnerSelectedListener());
			disSpi.setVisibility(View.VISIBLE);

			adapter = new GoodsListViewAdapter(ShoppingActivity.this);
			showList.setCacheColorHint(Color.TRANSPARENT);
			showList.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			
			initData(null,null);
			
	    }

	 	
	 //退出按钮响应函数
	 public void loginout_system(View v){
		Intent intent = new Intent(ShoppingActivity.this, LoginActivity.class);
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
				
				Intent intent = new Intent(ShoppingActivity.this ,ProductActivity.class);
				
				intent.putExtra("id",  String.valueOf(list.get(position).getId()));
				
				
				
				ShoppingActivity.this.startActivity(intent);

			}
		}

	 //获取商品分类信息
	 void getFenlei() {
		 	
		 fenleilist.clear();
		 
		 	
		 String result = HttpUtil.sendRequest(HttpUtil.fenleilist, null);
			
			
			
		try {
				
			JSONArray tables = new JSONArray(result);

			for (int i = 0; i < tables.length(); i++) {

				JSONObject obj_tmp = tables.getJSONObject(i);
				Fenlei bean = new Fenlei();
				bean.setId(obj_tmp.getInt("id"));
				
				bean.setName(obj_tmp.getString("name"));
				

				fenleilist.add(bean);
					
					
			}

				

		} catch (JSONException e) {
				
			e.printStackTrace();
			Log.e("mobile", "格式转换错误");
		}
	}
	 
	 
	 
	 void initData(String name,String fenlei) {
		 	list.clear();
		 	
		 	
		 	Map<String, String> parameter = new HashMap<String, String>();
		 	parameter.put("name", name);
		 	parameter.put("fenlei", fenlei);
		 
		 	
			String result = HttpUtil.sendRequest(HttpUtil.productlist, parameter);
			
			
			
			try {
				
				JSONArray tables = new JSONArray(result);

				for (int i = 0; i < tables.length(); i++) {

					JSONObject obj_tmp = tables.getJSONObject(i);
					Product bean = new Product();
					bean.setId(obj_tmp.getInt("id"));
					bean.setFenlei(obj_tmp.getString("fenlei"));
					bean.setName(obj_tmp.getString("name"));
					bean.setPrice(obj_tmp.getString("price"));
					bean.setImgpath(obj_tmp.getString("imgpath"));

					list.add(bean);
					
					
				}

				adapter.setGoodsLists(list);
				
				adapter.notifyDataSetChanged();

			} catch (JSONException e) {
				
				e.printStackTrace();
				Log.e("mobile", "格式转换错误");
			}
		}
	 
	 
	 
	 
	 
	 class SpinnerSelectedListener implements OnItemSelectedListener {
		 
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				
				
				selType = arg2;
				
				if (selType >0 ){
					fenlei = fenleilist.get(selType-1).getName();
					
				}else{
					fenlei = null;
				}
				
				initData(null,fenlei);
				
				
		
				

			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}

		}
	 
	 
	 
	//搜索按钮响应函数
	public void shopping_search(View v){
				
		String name = shopping_name.getText().toString();
				
			
		initData(name,fenlei);		
	}
	
	
	class GoodsListViewAdapter extends BaseAdapter{
		private Context context;
		private LayoutInflater inflater;
		private List<Product> goodsLists;
		public GoodsListViewAdapter(Context context) {
			this.context = context;
			this.inflater = LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
			return goodsLists == null ? 0 : goodsLists.size();
		}

		@Override
		public Object getItem(int position) {
			return goodsLists.get(position);
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}

		
		public List<Product> getGoodsLists() {
			return goodsLists;
		}
		
		public void setGoodsLists(List<Product> goodsLists) {
			this.goodsLists = goodsLists;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			Product bean= goodsLists.get(position);
			convertView = inflater.inflate(R.layout.listivew_goods_item, null);
			holder = new ViewHolder();
			holder.imageGoodsPic = (ImageView) convertView.findViewById(R.id.imageGoodsPic);
			holder.textGoodsName = (TextView) convertView.findViewById(R.id.textGoodsName);
			holder.textGoodsPrice = (TextView) convertView.findViewById(R.id.textGoodsPrice);
			holder.textGoodsfenlei = (TextView) convertView.findViewById(R.id.textGoodsfenlei);
			

			MyBackAsynaTask asynaTask = new MyBackAsynaTask(HttpUtil.URL+"/uploadfile/"+bean.getImgpath(), holder.imageGoodsPic);
			
			asynaTask.execute();
			
			holder.textGoodsName.setText(bean.getName());
			holder.textGoodsPrice.setText("￥"+bean.getPrice());
			holder.textGoodsfenlei.setText(bean.getFenlei());
			
			return convertView;
		}
		class ViewHolder {
			ImageView imageGoodsPic;
			TextView textGoodsName;
			TextView textGoodsPrice;
			TextView textGoodsfenlei;
			
		}
	}	
	

}
