package com.shopping.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.TabHost;

public class TabHostActivity extends TabActivity {
	
	
	
	private TabHost mHost;
	
	private RadioGroup tabItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabhost);
		initResourceRefs();
	    initSettings();
		
	}

	
	 private void initResourceRefs(){
		  mHost = getTabHost();
		  
		  mHost.addTab(mHost.newTabSpec("shopping").setIndicator("shopping")
		    		.setContent(new Intent(this , ShoppingActivity.class)));

		    
		  mHost.addTab(mHost.newTabSpec("cart").setIndicator("cart")
		    		.setContent(new Intent(this , CartActivity.class)));
		  
		  mHost.addTab(mHost.newTabSpec("order").setIndicator("order")
		    		.setContent(new Intent(this , OrderActivity.class)));
		    
		  mHost.addTab(mHost.newTabSpec("userinfo").setIndicator("userinfo")
		    		.setContent(new Intent(this , UserinfoActivity.class)));  
			    
		  
		  
		  tabItems = (RadioGroup)findViewById(R.id.home_radio_button_group);
	 }
	 
	 
	 
	 private void initSettings(){
		 
		 
		 tabItems.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					// TODO Auto-generated method stub
					
					
					switch(checkedId){
					

					 case R.id.tab_item_shopping :
						 mHost.setCurrentTabByTag("shopping");
						 break;
					 case R.id.tab_item_cart :
						 mHost.setCurrentTabByTag("cart");
						 break;
					 case R.id.tab_item_order :
						 mHost.setCurrentTabByTag("order");
						 break;
					 case R.id.tab_item_userinfo :
						 mHost.setCurrentTabByTag("userinfo");
						 break;			
					
					
					}
					
					
				}
			});
		 
		
	 }
	
	

}
