package action;





import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Cart;
import model.Fenlei;
import model.Order;
import model.Pic;
import model.Product;
import model.User;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import util.Arith;
import util.Util;

import com.opensymphony.xwork2.ActionSupport;

import dao.CartDao;
import dao.FenleiDao;
import dao.OrderDao;
import dao.PicDao;
import dao.ProductDao;
import dao.UserDao;



public class AppAction extends ActionSupport{

	
	
	private static final long serialVersionUID = 1L;



	// 获取请求对象
	public HttpServletRequest getRequest() {
		HttpServletRequest request = ServletActionContext.getRequest();
		return request;
	}

	// 获取响应对象
	public HttpServletResponse getResponse() {
		HttpServletResponse response = ServletActionContext.getResponse();
		return response;
	}

	// 获取session对象
	public HttpSession getSession() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		return session;
	}

	// 获取输出对象
	public PrintWriter getPrintWriter() {

		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return writer;
	}
	
	
	private UserDao userDao;
	

	
	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	//新用户注册操作
	public void register() throws IOException {
		HttpServletRequest request = this.getRequest();


		PrintWriter writer = this.getPrintWriter();
		String phone = request.getParameter("phone");
		String username = request.getParameter("username");
		String truename = request.getParameter("truename");
		String password = request.getParameter("password");

	
		User bean = userDao.selectBean(" where username='"+username+"' and  userlock=0");
		if(bean!=null){
			
			writer.print("fail");
			return;
		}
		bean = new User();
		bean.setCreatetime(Util.getTime());
		bean.setPhone(phone);
		bean.setPassword(password);
		bean.setUsername(username);
		bean.setTruename(truename);
	
		userDao.insertBean(bean);
		
		
		writer.print("success");
		
		
	}
	
	
	
	//登入请求
	public void login() throws IOException {
		
		HttpServletRequest request = this.getRequest();
		
		PrintWriter writer = this.getPrintWriter();
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		User user = userDao.selectBean(" where username = '" + username+ "' and password= '" + password + "' and userlock=0 ");
		if (user != null) {
			writer.print(user.getUsername());
		} else {
			writer.print("fail");
		}
		
	}
	

	private ProductDao productDao;

	public ProductDao getProductDao() {
		return productDao;
	}

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}
	
	

	//商品信息列表
	public void productlist() throws IOException {
		
		PrintWriter writer = this.getPrintWriter();
		HttpServletRequest request = this.getRequest();
		
		String name = request.getParameter("name");
		String fenlei = request.getParameter("fenlei");
	
		
		
		
		StringBuffer sb = new StringBuffer();
		sb.append(" where ");
		
		if(name !=null &&!"".equals(name)){
			sb.append(" name like '%"+name+"%' ");
			sb.append(" and ");
	
		}
		
		if(fenlei !=null &&!"".equals(fenlei)){
			sb.append(" fenlei.name like '%"+fenlei+"%' ");
			sb.append(" and ");
			
		
		}
		
		
		
		sb.append(" productlock=0 order by id desc ");
	
		String where = sb.toString();
		


		List<Product> list = productDao.selectBeanList(0, 9999, where);
		
		

		//组装成json对象传递给app端
		JSONArray array = new JSONArray();
		for(Product bean:list){
			JSONObject obj = new JSONObject();
			try {
				
				List<Pic> piclist = picDao.selectBeanList(0, 9999, " where  piclock=0 and product.id="+bean.getId() +"  order by id ");
				
				obj.put("id", bean.getId());
				obj.put("name", Util.isNull(bean.getName()));
				obj.put("fenlei", Util.isNull(bean.getFenlei().getName()));
				obj.put("price", Util.isNull(bean.getPrice()+""));
				if(piclist.size()>0){
					obj.put("imgpath", Util.isNull(piclist.get(0).getImgpath()));
				}else{
					obj.put("imgpath", "");
				}
				
			} catch (Exception e) {

			}
			array.put(obj);
		}
		
		
		writer.println(array.toString());
		
		writer.flush();
		writer.close();

	}
	
	
	private FenleiDao fenleiDao;

	public FenleiDao getFenleiDao() {
		return fenleiDao;
	}

	public void setFenleiDao(FenleiDao fenleiDao) {
		this.fenleiDao = fenleiDao;
	}
	
	

	//商品分类信息列表
	public void fenleilist() throws IOException {
		
		PrintWriter writer = this.getPrintWriter();
		
		StringBuffer sb = new StringBuffer();
		sb.append(" where ");

		sb.append(" fenleilock=0 order by id desc ");
	
		String where = sb.toString();
		


		List<Fenlei> list = fenleiDao.selectBeanList(0, 9999, where);
		
		

		//组装成json对象传递给app端
		JSONArray array = new JSONArray();
		for(Fenlei bean:list){
			JSONObject obj = new JSONObject();
			try {
				obj.put("id", bean.getId());
				obj.put("name", Util.isNull(bean.getName()));
			
			} catch (Exception e) {

			}
			array.put(obj);
		}
		
		
		writer.println(array.toString());
		
		writer.flush();
		writer.close();

	}
	
	
	
	//商品信息详情
	public void productdetails() throws IOException {
		HttpServletRequest request = this.getRequest();

		PrintWriter writer = this.getPrintWriter();
		Product bean = productDao.selectBean(" where id= "
				+ request.getParameter("id"));
		
		//组装成json对象传递给app端
		JSONArray array = new JSONArray();
		
		JSONObject obj = new JSONObject();
		try {
				obj.put("id", bean.getId());
				obj.put("name", Util.isNull(bean.getName()));
				obj.put("price", Util.isNull(bean.getPrice()+""));

				obj.put("createtime", Util.isNull(bean.getCreatetime()));
				obj.put("fenlei", Util.isNull(bean.getFenlei().getName()));
				obj.put("info", Util.isNull(bean.getInfo()));
				
		} catch (Exception e) {

		}
		array.put(obj);

		writer.println(array.toString());
		
		writer.flush();
		writer.close();
	}
	
	
	private PicDao picDao;

	
	public PicDao getPicDao() {
		return picDao;
	}

	public void setPicDao(PicDao picDao) {
		this.picDao = picDao;
	}

	//商品图片信息列表
	public void piclist() throws IOException {
		
		PrintWriter writer = this.getPrintWriter();
		HttpServletRequest request = this.getRequest();
		
		String pid = request.getParameter("pid");

		StringBuffer sb = new StringBuffer();
		sb.append(" where ");
		
		
		
		sb.append(" piclock=0  and product.id="+pid+" order by id desc ");
	
		String where = sb.toString();
		


		List<Pic> list = picDao.selectBeanList(0, 9999, where);
		
		

		//组装成json对象传递给app端
		JSONArray array = new JSONArray();
		for(Pic bean:list){
			JSONObject obj = new JSONObject();
			try {
				obj.put("id", bean.getId());
				obj.put("imgpath", Util.isNull(bean.getImgpath()));
				
				
			} catch (Exception e) {

			}
			array.put(obj);
		}
		
		
		writer.println(array.toString());
		
		writer.flush();
		writer.close();

	}
	
	
	
	private CartDao cartDao;



	public CartDao getCartDao() {
		return cartDao;
	}

	public void setCartDao(CartDao cartDao) {
		this.cartDao = cartDao;
	}
	
	
	//添加到购物车操作
	public void cartadd() throws IOException {
		HttpServletRequest request = this.getRequest();
		PrintWriter writer = this.getPrintWriter();	
		
		
		String username = request.getParameter("username");
		
		String pid = request.getParameter("pid");
		
		Product pro = productDao.selectBean(" where id= "+pid);
		

		Cart bean = cartDao.selectBean(" where product.id= "+pid +" and user.username= '"+username+"'");
		if(bean==null){
			bean = new Cart();
			bean.setCreatetime(Util.getTime());
			bean.setSl(1);
			bean.setUser(userDao.selectBean(" where username='"+username+"' "));
			bean.setProduct(pro);
			bean.setTotal(Arith.mul(pro.getPrice(), bean.getSl()));
			cartDao.insertBean(bean);
			writer.print("success");
		}else{
			writer.print("fail");
			
		}

	}
	
	
	//我的购物车列表
	public void cartlist() throws IOException {
		
		PrintWriter writer = this.getPrintWriter();
		HttpServletRequest request = this.getRequest();
		
		String username = request.getParameter("username");

		StringBuffer sb = new StringBuffer();
		sb.append(" where ");
		
		
		
		sb.append("  user.username='"+username+"' order by id desc ");
	
		String where = sb.toString();
		


		List<Cart> list = cartDao.selectBeanList(0, 9999, where);
		
		

		//组装成json对象传递给app端
		JSONArray array = new JSONArray();
		for(Cart bean:list){
			
			List<Pic> piclist = picDao.selectBeanList(0, 9999, " where  piclock=0 and product.id="+bean.getProduct().getId() +"  order by id ");
			
			JSONObject obj = new JSONObject();
			try {
				obj.put("id", bean.getId());
				obj.put("name", Util.isNull(bean.getProduct().getName()));
				obj.put("price", Util.isNull(bean.getProduct().getPrice()+""));

				obj.put("sl", Util.isNull(bean.getSl()+""));
				obj.put("total", Util.isNull(bean.getTotal()+""));
				if(piclist.size()>0){
					obj.put("imgpath", Util.isNull(piclist.get(0).getImgpath()));
				}else{
					obj.put("imgpath", "");
				}
				
				
				
			} catch (Exception e) {

			}
			array.put(obj);
		}
		
		
		writer.println(array.toString());
		
		writer.flush();
		writer.close();

	}
	
	
	//删除购物车中的商品操作
	public void cartdelete() throws IOException {
		HttpServletRequest request = this.getRequest();
		PrintWriter writer = this.getPrintWriter();	
		Cart bean = cartDao.selectBean(" where id= "+ request.getParameter("cart_id"));
		
		cartDao.deleteBean(bean);
		
		writer.print("success");
	}
	
	
	//增加购物车中商品数量的操作
	public void cartupdate() throws IOException{
		HttpServletRequest request = this.getRequest();
		PrintWriter writer = this.getPrintWriter();	
		
		
		Cart cart = cartDao.selectBean(" where id= "+request.getParameter("cart_id"));
		
		
		

		cart.setSl(cart.getSl()+1);
		
		cart.setTotal(Arith.mul(cart.getProduct().getPrice(), cart.getSl()));

		cartDao.updateBean(cart);

		writer.print("success");
			
	}
	
	//减少购物车中商品数量的操作
	public void cartupdate2() throws IOException{
		HttpServletRequest request = this.getRequest();
		PrintWriter writer = this.getPrintWriter();	
		
		
		Cart cart = cartDao.selectBean(" where id= "+request.getParameter("cart_id"));
		
		if(cart.getSl()<=1){
			writer.print("fail");
			return ;
		}
		

		cart.setSl(cart.getSl()-1);
		
		cart.setTotal(Arith.mul(cart.getProduct().getPrice(), cart.getSl()));

		cartDao.updateBean(cart);

		writer.print("success");
			
	}
	
	
	private OrderDao orderDao;

	
	public OrderDao getOrderDao() {
		return orderDao;
	}

	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}
	
	

	//添加订单操作
	public void orderformadd() throws IOException{
		HttpServletRequest request = this.getRequest();
		PrintWriter writer = this.getPrintWriter();	
		
		String address = request.getParameter("address");//收货地址
		String phone = request.getParameter("phone");//手机号码
		String name = request.getParameter("name");//收货人姓名
		String username = request.getParameter("username");//用户名
		
		User  user = userDao.selectBean(" where username='"+username+"' ");

			
		Order bean = new Order();
		
		bean.setAddress(address);
		bean.setCreatetime(Util.getTime());
		bean.setOrderid(Util.getTime2());
		bean.setPhone(phone);
		bean.setUser(user);
		bean.setName(name);
		bean.setStatus("处理中");

		List<Cart> list = cartDao.selectBeanList(0, 9999, " where user.id="+user.getId());
		StringBuffer sb = new StringBuffer();
		double totalprice = 0;
		for(Cart cart:list){
			double price = 0;
			
			price = cart.getProduct().getPrice();
			
			
			sb.append("商品名： "+cart.getProduct().getName() +",购买数量:"+cart.getSl()  +",单价￥"+price 
					+",小计￥"+ Arith.mul(cart.getSl(), price)) ;

			cartDao.deleteBean(cart);

			totalprice = totalprice+(Arith.mul(cart.getSl(), price));

		}
		
		bean.setDetails(sb.toString());
		bean.setTotalprice(totalprice);

		orderDao.insertBean(bean);

		writer.print("success");
		
	}
	
	//用户信息详情
	public void userdetails() throws IOException {
		HttpServletRequest request = this.getRequest();
		PrintWriter writer = this.getPrintWriter();
		
		String username = request.getParameter("username");
		
		User bean = userDao.selectBean(" where username= '"+username+"'");
		
		//组装成json对象传递给app端
		JSONArray array = new JSONArray();
		
		JSONObject obj = new JSONObject();
		try {
				obj.put("id", bean.getId());
				obj.put("username", Util.isNull(bean.getUsername()));
				obj.put("password", Util.isNull(bean.getPassword()));
				obj.put("createtime", Util.isNull(bean.getCreatetime()));
				obj.put("truename", Util.isNull(bean.getTruename()));
				obj.put("phone", Util.isNull(bean.getPhone()));
				
				
		} catch (Exception e) {

		}
		array.put(obj);

		writer.println(array.toString());
		
		writer.flush();
		writer.close();
	}

	
	
	
	//我的订单列表
	public void orderlist() throws IOException {
		
		PrintWriter writer = this.getPrintWriter();
		HttpServletRequest request = this.getRequest();
		
		String orderid = request.getParameter("orderid");
		String username = request.getParameter("username");
		String status = request.getParameter("status");
		
		
		
		StringBuffer sb = new StringBuffer();
		sb.append(" where ");
		
		if(orderid !=null &&!"".equals(orderid)){
			sb.append(" orderid like '%"+orderid+"%' ");
			sb.append(" and ");
	
		}
		if(status !=null &&!"".equals(status)){
			sb.append(" status like '%"+status+"%' ");
			sb.append(" and ");
	
		}
		
		
		
		sb.append(" user.username='"+username+"'  order by id desc ");
	
		String where = sb.toString();
		


		List<Order> list = orderDao.selectBeanList(0, 9999, where);
		
		

		//组装成json对象传递给app端
		JSONArray array = new JSONArray();
		for(Order bean:list){
			JSONObject obj = new JSONObject();
			try {
				obj.put("id", bean.getId());
				obj.put("orderid", Util.isNull(bean.getOrderid()));
				obj.put("createtime", Util.isNull(bean.getCreatetime()));
				obj.put("status", Util.isNull(bean.getStatus()));
				
			} catch (Exception e) {

			}
			array.put(obj);
		}
		
		
		writer.println(array.toString());
		
		writer.flush();
		writer.close();

	}
	
	
	//订单信息详情
	public void orderdetails() throws IOException {
		HttpServletRequest request = this.getRequest();

		PrintWriter writer = this.getPrintWriter();
		
		Order bean = orderDao.selectBean(" where id= "
				+ request.getParameter("id"));
		
		//组装成json对象传递给app端
		JSONArray array = new JSONArray();
		
		JSONObject obj = new JSONObject();
		try {
				obj.put("id", bean.getId());
				obj.put("orderid", Util.isNull(bean.getOrderid()));
				obj.put("createtime", Util.isNull(bean.getCreatetime()));
				
				obj.put("name", Util.isNull(bean.getName()));
				obj.put("address", Util.isNull(bean.getAddress()));
				obj.put("phone", Util.isNull(bean.getPhone()));
				
				obj.put("totalprice", Util.isNull(bean.getTotalprice()+""));
				obj.put("status", Util.isNull(bean.getStatus()));
				obj.put("details", Util.isNull(bean.getDetails()));
				
				
		} catch (Exception e) {

		}
		array.put(obj);

		writer.println(array.toString());
		
		writer.flush();
		writer.close();
	}
	
	
	//确认收货操作
	public void orderstatusupdate() throws IOException{
		HttpServletRequest request = this.getRequest();
		PrintWriter writer = this.getPrintWriter();	
		
		
		String id = request.getParameter("id");//用户名
		
	
			
		Order bean = orderDao.selectBean(" where id= "+id);
		
		if("已发货".equals(bean.getStatus())){
			bean.setStatus("确认收货");
			
			orderDao.updateBean(bean);
			writer.print("success");
		}else{
			writer.print("fail");
		}

		
	}
	
	
	//取消订单操作
	public void orderstatusupdate2() throws IOException{
		HttpServletRequest request = this.getRequest();
		PrintWriter writer = this.getPrintWriter();	
		
		
		String id = request.getParameter("id");//用户名
		
	
			
		Order bean = orderDao.selectBean(" where id= "+id);
		
		if("处理中".equals(bean.getStatus())){
			bean.setStatus("取消订单");
			
			orderDao.updateBean(bean);
			writer.print("success");
		}else{
			writer.print("fail");
		}	
	}
	
	
	
	
	//修改密码
	public void passwordedit() throws IOException {
		HttpServletRequest request = this.getRequest();

		PrintWriter writer = this.getPrintWriter();
		String username = request.getParameter("username");

		String password1 = request.getParameter("password1");//原密码
		
		String password2 = request.getParameter("password2");//新密码

	
		User bean = userDao.selectBean(" where username='"+username+"' and password='"+password1+"' and userlock=0");
		//原密码错误
		if(bean==null){
			
			writer.print("fail");
			return;
		}
		
		bean.setPassword(password2);

		userDao.updateBean(bean);
		
		//修改成功
		writer.print("success");
		
		
	}
	
	
	//编辑用户信息
	public void userdedit() throws IOException {
		HttpServletRequest request = this.getRequest();
		PrintWriter writer = this.getPrintWriter();
		
		String username = request.getParameter("username");

		String truename = request.getParameter("truename");
		
		String phone = request.getParameter("phone");

	
		User bean = userDao.selectBean(" where username='"+username+"'  and userlock=0");
		
		bean.setTruename(truename);
		bean.setPhone(phone);
		
		
		userDao.updateBean(bean);
		
		//修改成功
		writer.print("success");
		
		
	}
	
	
	
	
}
