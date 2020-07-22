package com.shopping.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;



//与服务端进行数据交互

public class HttpUtil
{
	public static  String URL = "http://192.168.1.109:8080/shop/";//请求的服务器地址
	
	
	public static  String register = "appmethod!register";//注册的请求方法
	
	public static  String login = "appmethod!login";//登录的请求方法
	
	public static  String productlist = "appmethod!productlist";//商品信息列表
	
	public static  String fenleilist = "appmethod!fenleilist";//商品分类信息列表
	
	public static  String productdetails = "appmethod!productdetails";//商品详细信息
	
	public static  String piclist = "appmethod!piclist";//商品图片信息列表
	
	public static  String cartadd = "appmethod!cartadd";//加入到购物车
	
	public static  String cartlist = "appmethod!cartlist";//购物车列表
	
	public static  String cartdelete = "appmethod!cartdelete";//删除购物车中的商品
	
	public static  String cartupdate = "appmethod!cartupdate";//增加购物车中的商品
	
	public static  String cartupdate2 = "appmethod!cartupdate2";//减少购物车中的商品
	
	public static  String orderformadd = "appmethod!orderformadd";//添加订单
	
	public static  String userdetails = "appmethod!userdetails";//用户信息
	
	public static  String orderlist = "appmethod!orderlist";//我的订单
	
	public static  String orderdetails = "appmethod!orderdetails";//订单详情
	
	public static  String orderstatusupdate = "appmethod!orderstatusupdate";//确认收货
	
	public static  String orderstatusupdate2 = "appmethod!orderstatusupdate2";//取消订单
	
	public static  String passwordedit = "appmethod!passwordedit";//修改密码
	
	public static  String userdedit = "appmethod!userdedit";//编辑用户信息
	
	
	//发送请求，从服务端获取数据
	public static  String sendRequest(String method,Map<String, String> parameter) {
			

		String urlStr = URL+method;
		String response = null;

		try {
				
			List<Parameter>  params = HttpUtil.getParameter(parameter);
				
			response = HttpUtil.httpPost(urlStr, params);
				
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return response;
			
			
	}
	
	
	
	/**
	 * 通过GET方式发送请求
	 * @param url URL地址
	 * @param params 参数
	 * @return 
	 * @throws Exception
	 */
	public  static String httpGet(String url, String params) throws Exception
	{
		String response = null; //返回信息
		//拼接请求URL
		if (null!=params&&!params.equals(""))
		{
			url += "?" + params;
		}
		
		int timeoutConnection = 3000;  
		int timeoutSocket = 5000;  
		HttpParams httpParameters = new BasicHttpParams();// Set the timeout in milliseconds until a connection is established.  
	    HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);// Set the default socket timeout (SO_TIMEOUT) // in milliseconds which is the timeout for waiting for data.  
	    HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);  
	    
		// 构造HttpClient的实例
		HttpClient httpClient = new DefaultHttpClient(httpParameters);  
		// 创建GET方法的实例
		HttpGet httpGet = new HttpGet(url);
		try
		{
			HttpResponse httpResponse = httpClient.execute(httpGet);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) //SC_OK = 200
			{
				// 获得返回结果
				response = EntityUtils.toString(httpResponse.getEntity());
			}
			else
			{
				response = "返回码："+statusCode;
			}
		} catch (Exception e)
		{
			throw new Exception(e);
		} 
		return response;
	}

	/**
	 * 通过POST方式发送请求
	 * @param url URL地址
	 * @param params 参数
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws Exception
	 */
	public  static String httpPost(String url, List<Parameter> params) throws ClientProtocolException, IOException 
	{
		String response = null;
		int timeoutConnection = 3000;  
		int timeoutSocket = 5000;  
		HttpParams httpParameters = new BasicHttpParams();// Set the timeout in milliseconds until a connection is established.  
	    HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);// Set the default socket timeout (SO_TIMEOUT) // in milliseconds which is the timeout for waiting for data.  
	    HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);  
		// 构造HttpClient的实例
		HttpClient httpClient = new DefaultHttpClient(httpParameters);  
		HttpPost httpPost = new HttpPost(url);
		if (params.size()>=0)
		{
			//设置httpPost请求参数
			httpPost.setEntity(new UrlEncodedFormEntity(buildNameValuePair(params),HTTP.UTF_8));
		}
		//使用execute方法发送HTTP Post请求，并返回HttpResponse对象
		
		HttpResponse httpResponse = httpClient.execute(httpPost);
		
		
		
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		if(statusCode==HttpStatus.SC_OK)
		{
			//获得返回结果
			response = EntityUtils.toString(httpResponse.getEntity());
			
		}
		else
		{
			response = "返回码："+statusCode;
		}
		return response;
	}
	
	/**
	 * 把Parameter类型集合转换成NameValuePair类型集合
	 * @param params 参数集合
	 * @return
	 */
	private static List<BasicNameValuePair> buildNameValuePair(List<Parameter> params)
	{
		List<BasicNameValuePair> result = new ArrayList<BasicNameValuePair>();
		for (Parameter param : params)
		{
			BasicNameValuePair pair = new BasicNameValuePair(param.getName(), param.getValue());
			result.add(pair);
		}
		return result;
	}
	
	
	public static List<Parameter> getParameter(Map<String,String> map){
		List<Parameter> params = new ArrayList<Parameter>();
		
		if(map!=null){
			for (String key : map.keySet()) {
		       	 
				String value = map.get(key);
				String name = key;
				
				Parameter p = new Parameter();
				p.setName(name);
				p.setValue(value);
				params.add(p);
	        }
		}
		
		
		return params;
	}
	
	
	
	public static void main(String[] args) throws Exception {
		String urlStr = "http://127.0.0.1:8080/as_hotel/method!register";
		
		Map<String ,String> map = new HashMap<String,String>();
		map.put("sex","女");
		map.put("dianhua", "13000000000");
		map.put("xingming", "aaa");
		map.put("password", "111111");
		map.put("username","ttyy");
 		
		for (String key : map.keySet()) {
       	 
            System.out.println("key= "+ key + " and value= " + map.get(key));
  
        }
		
		List<Parameter>  params = HttpUtil.getParameter(map);
		
		
		String response = HttpUtil.httpPost(urlStr,params);
		System.out.println(response);
	}
}
