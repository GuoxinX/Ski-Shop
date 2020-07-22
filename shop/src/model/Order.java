package model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

//订单
@Entity
@Table(name="t_Order")
public class Order {
	

	@Id
	@GeneratedValue
	private int id;//主键
	
	private String orderid;//订单号
	
	private String createtime;//生成时间
	
	@ManyToOne
	@JoinColumn(name="userid")
	private User user;//关联的用户，外键
	
	private String name;//收货人姓名
	
	private String address;//收货地址
	
	private String phone;//手机号码
	
	private double totalprice;//总价
	
	private String status;//处理中 已发货  确认收货  取消订单
	
	@Column(name="details", columnDefinition="TEXT")
	private String details;//订单明细

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public double getTotalprice() {
		return totalprice;
	}

	public void setTotalprice(double totalprice) {
		this.totalprice = totalprice;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	
	

	

	

	

	

	
	
	
	
	
}
