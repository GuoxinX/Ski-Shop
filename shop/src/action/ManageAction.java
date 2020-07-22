package action;





import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Fenlei;
import model.Manage;
import model.Order;
import model.Pic;
import model.Product;
import model.User;

import org.apache.struts2.ServletActionContext;

import util.Pager;
import util.Util;

import com.opensymphony.xwork2.ActionSupport;

import dao.FenleiDao;
import dao.ManageDao;
import dao.OrderDao;
import dao.PicDao;
import dao.ProductDao;
import dao.UserDao;



public class ManageAction extends ActionSupport{

	
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
	
	
	private String url="./";
	
	
	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}
	
	private ManageDao manageDao;


	public ManageDao getManageDao() {
		return manageDao;
	}


	public void setManageDao(ManageDao manageDao) {
		this.manageDao = manageDao;
	}
	
	
	public void login() throws IOException{
		HttpServletRequest request = this.getRequest();
		PrintWriter writer = this.getPrintWriter();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		Manage bean = manageDao.selectBean(" where username='"+username+"' and password ='"+password+"' ");
		if(bean!=null){
			HttpSession session = request.getSession();
			session.setAttribute("manage", bean);

			writer.print("<script  language='javascript'>alert('登录成功');window.location.href='index.jsp'; </script>");
		}else{
			writer.print("<script  language='javascript'>alert('用户名或者密码错误');window.location.href='login.jsp'; </script>");
		}
		
		
	}
	
	//用户退出操作
	public void loginout() throws IOException{
		HttpServletRequest request = this.getRequest();
		HttpSession session = request.getSession();
		session.removeAttribute("manage");
		
		
		PrintWriter writer = this.getPrintWriter();
		writer.print("<script  language='javascript'>alert('退出成功');window.location.href='login.jsp'; </script>");
	}
	
	
	public String passwordupdate(){
		this.setUrl("password.jsp");
		return SUCCESS;
		
	}
	
	
	
	public void passwordupdate2() throws IOException{
		HttpServletRequest request = this.getRequest();
		PrintWriter writer = this.getPrintWriter();
		String password1 = request.getParameter("password1");
		String password2 = request.getParameter("password2");
		HttpSession session = request.getSession();
		Manage user = (Manage)session.getAttribute("manage");
		
		Manage bean = manageDao.selectBean(" where username='"+user.getUsername()+"' and password ='"+password1+"' ");
		if(bean!=null){
			bean.setPassword(password2);
			manageDao.updateBean(bean);

			writer.print("<script  language='javascript'>alert('操作成功');</script>");
		}else{
		
			writer.print("<script  language='javascript'>alert('原密码错误');window.location.href='method!passwordupdate'; </script>");
		}
		
		
	}
	
	private FenleiDao fenleiDao;


	public FenleiDao getFenleiDao() {
		return fenleiDao;
	}


	public void setFenleiDao(FenleiDao fenleiDao) {
		this.fenleiDao = fenleiDao;
	}
	
	
	//商品分类信息列表
	public String fenleilist(){
		HttpServletRequest request = this.getRequest();
		String name = request.getParameter("name");
		StringBuffer sb = new StringBuffer();
		sb.append(" where ");

		if(name !=null &&!"".equals(name)){
			sb.append(" name like '%"+name+"%' ");
			sb.append(" and ");
		
			request.setAttribute("name", name);
		}
		
		sb.append(" fenleilock=0 order by id desc ");
		
		String where = sb.toString();


		int currentpage = 1;
		int pagesize = 10;
		if(request.getParameter("pagenum") != null){
			currentpage = Integer.parseInt(request.getParameter("pagenum"));
		}
		
		long total = fenleiDao.selectBeanCount(where.replaceAll("order by id desc", ""));
		List<Fenlei> list = fenleiDao.selectBeanList((currentpage-1)*pagesize, pagesize, where);
		request.setAttribute("list", list);
		String pagerinfo = Pager.getPagerNormal((int)total, pagesize, currentpage, "method!fenleilist", "共有"+total+"条记录");
		request.setAttribute("pagerinfo", pagerinfo);
		request.setAttribute("url", "method!fenleilist");
		request.setAttribute("url2", "method!fenlei");
		request.setAttribute("title", "商品分类信息管理");
		this.setUrl("fenlei/fenleilist.jsp");
		return SUCCESS;
	}
	
	
	//跳转到添加商品分类页面
	public String fenleiadd(){
		HttpServletRequest request = this.getRequest();
		request.setAttribute("url", "method!fenleiadd2");
		request.setAttribute("title", "添加商品分类信息");
		this.setUrl("fenlei/fenleiadd.jsp");
		return SUCCESS;
	}
	
	
	//添加商品分类操作
	public void fenleiadd2() throws IOException{
		HttpServletRequest request = this.getRequest();
		
		String name = request.getParameter("name");
		Fenlei bean = new Fenlei();
		bean.setName(name);
		fenleiDao.insertBean(bean);
		
		PrintWriter writer = this.getPrintWriter();
		writer.print("<script  language='javascript'>alert('提交成功');window.location.href='method!fenleilist'; </script>");
		
	}
	
	
	
	//删除商品分类操作
	public void fenleidelete() throws IOException{
		HttpServletRequest request = this.getRequest();
		
		String id = request.getParameter("id");
		Fenlei bean =fenleiDao.selectBean(" where id= "+id);
		bean.setFenleilock(1);
		fenleiDao.updateBean(bean);
		
		PrintWriter writer = this.getPrintWriter();
		writer.print("<script  language='javascript'>alert('提交成功');window.location.href='method!fenleilist'; </script>");
		
	}
	
	//跳转到更新商品分类页面
	public String fenleiupdate(){
		HttpServletRequest request = this.getRequest();
		request.setAttribute("title", "修改商品分类信息");
		String id = request.getParameter("id");
		Fenlei bean =fenleiDao.selectBean(" where id= "+id);
		request.setAttribute("url", "method!fenleiupdate2?id="+id);
		request.setAttribute("bean", bean);
		this.setUrl("fenlei/fenleiupdate.jsp");
		return SUCCESS;
	}
	
	
	//更新商品分类操作
	public void fenleiupdate2() throws IOException{
		HttpServletRequest request = this.getRequest();
		
		String name = request.getParameter("name");
		String id = request.getParameter("id");
		Fenlei bean =fenleiDao.selectBean(" where id= "+id);
		bean.setName(name);
		fenleiDao.updateBean(bean);
		
		PrintWriter writer = this.getPrintWriter();
		writer.print("<script  language='javascript'>alert('提交成功');window.location.href='method!fenleilist'; </script>");
		
	}
	
	//跳转到查看商品分类页面
	public String fenleiupdate3(){
		HttpServletRequest request = this.getRequest();

		request.setAttribute("title", "查看商品分类信息");
		String id = request.getParameter("id");
		Fenlei bean =fenleiDao.selectBean(" where id= "+id);
		request.setAttribute("bean", bean);
		this.setUrl("fenlei/fenleiupdate3.jsp");
		return SUCCESS;
	}
	
	
	private ProductDao productDao;


	public ProductDao getProductDao() {
		return productDao;
	}


	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}
	
	
	//商品信息列表
	public String productlist(){
		HttpServletRequest request = this.getRequest();
		
		String name = request.getParameter("name");
		String fenlei = request.getParameter("fenlei");
		request.setAttribute("fenleilist", fenleiDao.selectBeanList(0, 9999, " where  fenleilock=0 "));
		
		
		
		StringBuffer sb = new StringBuffer();
		sb.append(" where ");
		
		if(name !=null &&!"".equals(name)){
			sb.append(" name like '%"+name+"%' ");
			sb.append(" and ");
			
			request.setAttribute("name", name);
		}
		
		if(fenlei !=null &&!"".equals(fenlei)){
			sb.append(" fenlei.name like '%"+fenlei+"%' ");
			sb.append(" and ");
			
			request.setAttribute("fenlei", fenlei);
		}
		
		
		
		sb.append(" productlock=0 order by id desc ");
	
		String where = sb.toString();
		

		int currentpage = 1;
		int pagesize = 10;
		if(request.getParameter("pagenum") != null){
			currentpage = Integer.parseInt(request.getParameter("pagenum"));
		}
		
		long total = productDao.selectBeanCount(where.replaceAll("order by id desc", ""));
		List<Product> list = productDao.selectBeanList((currentpage-1)*pagesize, pagesize, where);
		request.setAttribute("list", list);
		String pagerinfo = Pager.getPagerNormal((int)total, pagesize, currentpage, "method!productlist", "共有"+total+"条记录");
		request.setAttribute("pagerinfo", pagerinfo);
		request.setAttribute("url", "method!productlist");
		request.setAttribute("url2", "method!product");
		request.setAttribute("title", "商品信息管理");
		this.setUrl("product/productlist.jsp");
		return SUCCESS;
	}
	
	
	//跳转到添加商品页面
	public String productadd(){
		HttpServletRequest request = this.getRequest();
		request.setAttribute("fenleilist", fenleiDao.selectBeanList(0, 9999, " where  fenleilock=0 "));
		request.setAttribute("url", "method!productadd2");
		request.setAttribute("title", "添加商品信息");
		this.setUrl("product/productadd.jsp");
		return SUCCESS;
	}
	
	
	


	//添加商品操作
	public void productadd2() throws IOException{
		HttpServletRequest request = this.getRequest();
		
		String name = request.getParameter("name");
		String fenlei = request.getParameter("fenlei");
		String info = request.getParameter("info");

		String price = request.getParameter("price");
	
		
		
		Product bean = new Product();
		bean.setCreatetime(Util.getTime());
		bean.setFenlei(fenleiDao.selectBean(" where id= "+fenlei));

		bean.setInfo(info);

		bean.setName(name);
		bean.setPrice(Double.parseDouble(price));
		
		
		bean.setName(name);
		productDao.insertBean(bean);
		
		PrintWriter writer = this.getPrintWriter();
		writer.print("<script  language='javascript'>alert('提交成功');window.location.href='method!productlist'; </script>");
		
	}
	
	
	
	//删除商品操作
	public void productdelete() throws IOException{
		HttpServletRequest request = this.getRequest();
		
		String id = request.getParameter("id");
		Product bean =productDao.selectBean(" where id= "+id);
		bean.setProductlock(1);
		productDao.updateBean(bean);
		
		PrintWriter writer = this.getPrintWriter();
		writer.print("<script  language='javascript'>alert('提交成功');window.location.href='method!productlist'; </script>");
		
	}
	
	//跳转到更新商品页面
	public String productupdate(){
		HttpServletRequest request = this.getRequest();
		request.setAttribute("fenleilist", fenleiDao.selectBeanList(0, 9999, " where  fenleilock=0 "));
		request.setAttribute("title", "修改商品信息");
		String id = request.getParameter("id");
		Product bean =productDao.selectBean(" where id= "+id);
		request.setAttribute("url", "method!productupdate2?id="+id);
		request.setAttribute("bean", bean);
		this.setUrl("product/productupdate.jsp");
		return SUCCESS;
	}
	
	
	//更新商品操作
	public void productupdate2() throws IOException{
		HttpServletRequest request = this.getRequest();
		
		String name = request.getParameter("name");
		String fenlei = request.getParameter("fenlei");
		String info = request.getParameter("info");
		String price = request.getParameter("price");

		String id = request.getParameter("id");
		Product bean =productDao.selectBean(" where id= "+id);

		bean.setFenlei(fenleiDao.selectBean(" where id= "+fenlei));
		bean.setInfo(info);
		bean.setName(name);
		bean.setPrice(Double.parseDouble(price));
		productDao.updateBean(bean);
		
		PrintWriter writer = this.getPrintWriter();
		writer.print("<script  language='javascript'>alert('提交成功');window.location.href='method!productlist'; </script>");
		
	}
	
	
	
	//跳转到查看商品页面
	public String productupdate3(){
		HttpServletRequest request = this.getRequest();
		request.setAttribute("title", "查看商品信息");
		String id = request.getParameter("id");
		Product bean =productDao.selectBean(" where id= "+id);
		request.setAttribute("bean", bean);
		this.setUrl("product/productupdate3.jsp");
		return SUCCESS;
	}
	

	
	
	
	

	private OrderDao orderDao;
	
	
	public OrderDao getOrderDao() {
		return orderDao;
	}


	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}
	
	

	//订单信息列表
	public String orderlist(){
		HttpServletRequest request = this.getRequest();
		String orderid = request.getParameter("orderid");
		String username = request.getParameter("username");
		String status = request.getParameter("status");
		StringBuffer sb = new StringBuffer();
		sb.append(" where ");
		
		if(orderid !=null &&!"".equals(orderid)){
			sb.append(" orderid like '%"+orderid+"%' ");
			sb.append(" and ");
			
			request.setAttribute("orderid", orderid);
		}
		
		if(username !=null &&!"".equals(username)){
			sb.append(" user.username like '%"+username+"%' ");
			sb.append(" and ");
			
			request.setAttribute("username", username);
		}
		
		if(status !=null &&!"".equals(status)){
			sb.append(" status like '%"+status+"%' ");
			sb.append(" and ");
			
			request.setAttribute("status", status);
		}
		
		sb.append(" 1=1 order by id desc ");
	
		String where = sb.toString();
		

		int currentpage = 1;
		int pagesize = 10;
		if(request.getParameter("pagenum") != null){
			currentpage = Integer.parseInt(request.getParameter("pagenum"));
		}
		
		long total = orderDao.selectBeanCount(where.replaceAll("order by id desc", ""));
		List<Order> list = orderDao.selectBeanList((currentpage-1)*pagesize, pagesize, where);
		request.setAttribute("list", list);
		String pagerinfo = Pager.getPagerNormal((int)total, pagesize, currentpage, "method!orderlist", "共有"+total+"条记录");
		request.setAttribute("pagerinfo", pagerinfo);
		request.setAttribute("url", "method!orderlist");
		request.setAttribute("url2", "method!order");
		request.setAttribute("title", "订单信息管理");
		this.setUrl("order/orderlist.jsp");
		return SUCCESS;
	}
	
	
	
	//处理订单操作
	public void orderdelete() throws IOException{
		HttpServletRequest request = this.getRequest();
		
		Order bean = orderDao.selectBean(" where id= "+request.getParameter("id"));
		bean.setStatus("已发货");
		orderDao.updateBean(bean);

		
		PrintWriter writer = this.getPrintWriter();
		writer.print("<script  language='javascript'>alert('提交成功');window.location.href='method!orderlist'; </script>");
		
	}
	
	
	//跳转到查看订单详情页面
	public String orderupdate3(){
		HttpServletRequest request = this.getRequest();
		request.setAttribute("title", "查看订单详情");
		String id = request.getParameter("id");
		Order bean =orderDao.selectBean(" where id= "+id);
		request.setAttribute("bean", bean);
		this.setUrl("order/orderupdate3.jsp");
		return SUCCESS;
	}
	
	
	
	
	
	private UserDao userDao;
	
	
	public UserDao getUserDao() {
		return userDao;
	}


	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}


	//注册用户信息列表
	public String userlist(){
		HttpServletRequest request = this.getRequest();
		String username = request.getParameter("username");
		String truename = request.getParameter("truename");
	
		StringBuffer sb = new StringBuffer();
		sb.append(" where ");
		
		if(username !=null &&!"".equals(username)){
			sb.append(" username like '%"+username+"%' ");
			sb.append(" and ");
		
			request.setAttribute("username", username);
		}
		
		if(truename !=null &&!"".equals(truename)){
			sb.append(" truename like '%"+truename+"%' ");
			sb.append(" and ");
			
			request.setAttribute("truename", truename);
		}
		
		
		
		sb.append(" userlock=0 order by id desc ");
	
		String where = sb.toString();
		

		int currentpage = 1;
		int pagesize = 10;
		if(request.getParameter("pagenum") != null){
			currentpage = Integer.parseInt(request.getParameter("pagenum"));
		}
		
		long total = userDao.selectBeanCount(where.replaceAll("order by id desc", ""));
		List<User> list = userDao.selectBeanList((currentpage-1)*pagesize, pagesize, where);
		request.setAttribute("list", list);
		String pagerinfo = Pager.getPagerNormal((int)total, pagesize, currentpage, "method!userlist", "共有"+total+"条记录");
		request.setAttribute("pagerinfo", pagerinfo);
		request.setAttribute("url", "method!userlist");
		request.setAttribute("url2", "method!user");
		request.setAttribute("title", "注册用户信息管理");
		this.setUrl("user/userlist.jsp");
		return SUCCESS;
	}
	
	
	//删除注册用户操作
	public void userdelete() throws IOException{
		HttpServletRequest request = this.getRequest();
		
		User bean = userDao.selectBean(" where id= "+request.getParameter("id"));
		bean.setUserlock(1);
		userDao.updateBean(bean);
		
		PrintWriter writer = this.getPrintWriter();
		writer.print("<script  language='javascript'>alert('提交成功');window.location.href='method!userlist'; </script>");
		
	}
	
	
	private PicDao picDao;


	public PicDao getPicDao() {
		return picDao;
	}


	public void setPicDao(PicDao picDao) {
		this.picDao = picDao;
	}

	
	//商品图片列表
	public String piclist(){
		HttpServletRequest request = this.getRequest();
		
		String pid = request.getParameter("pid");
	
		request.setAttribute("pid", pid);
		
		
		StringBuffer sb = new StringBuffer();
		sb.append(" where ");

		sb.append(" piclock=0  and product.id="+pid+"  order by id desc ");
	
		String where = sb.toString();
		

		int currentpage = 1;
		int pagesize = 3;
		if(request.getParameter("pagenum") != null){
			currentpage = Integer.parseInt(request.getParameter("pagenum"));
		}
		
		long total = picDao.selectBeanCount(where.replaceAll("order by id desc", ""));
		List<Pic> list = picDao.selectBeanList((currentpage-1)*pagesize, pagesize, where);
		request.setAttribute("list", list);
		String pagerinfo = Pager.getPagerNormal((int)total, pagesize, currentpage, "method!piclist?pid="+pid, "共有"+total+"条记录");
		request.setAttribute("pagerinfo", pagerinfo);
		request.setAttribute("url", "method!piclist?pid="+pid);
		request.setAttribute("url2", "method!pic");
		request.setAttribute("title", "商品图片管理");
		this.setUrl("pic/piclist.jsp");
		return SUCCESS;
	}
	
	
	//跳转到添加商品页面
	public String picadd(){
		HttpServletRequest request = this.getRequest();
		String pid = request.getParameter("pid");
		request.setAttribute("url", "method!picadd2?pid="+pid);
		request.setAttribute("title", "添加商品图片");
		this.setUrl("pic/picadd.jsp");
		return SUCCESS;
	}
	
	private File uploadfile;
	
	
	public File getUploadfile() {
		return uploadfile;
	}


	public void setUploadfile(File uploadfile) {
		this.uploadfile = uploadfile;
	}
	
	

	//添加商品操作
	public void picadd2() throws IOException{
		HttpServletRequest request = this.getRequest();
		
		String pid = request.getParameter("pid");
		
		
		
		Pic bean = new Pic();


		bean.setProduct(productDao.selectBean(" where id= "+pid));

		if(uploadfile!=null){
			String savapath = ServletActionContext.getServletContext().getRealPath("/")+"/uploadfile/";
			String time = Util.getTime2();
			String imgpath = time+".jpg";
			File file = new File(savapath+imgpath);
			Util.copyFile(uploadfile, file);
			bean.setImgpath(imgpath);
		}
		
		
	
		picDao.insertBean(bean);
		
		PrintWriter writer = this.getPrintWriter();
		writer.print("<script  language='javascript'>alert('提交成功');window.location.href='method!piclist?pid="+pid+"'; </script>");
		
	}
	
	
	
	//删除商品图片操作
	public void picdelete() throws IOException{
		HttpServletRequest request = this.getRequest();
		
		String id = request.getParameter("id");
		Pic bean =picDao.selectBean(" where id= "+id);
		bean.setPiclock(1);
		picDao.updateBean(bean);
		
		PrintWriter writer = this.getPrintWriter();
		writer.print("<script  language='javascript'>alert('提交成功');window.location.href='method!piclist?pid="+bean.getProduct().getId()+"'; </script>");
		
	
	}
	

}
