package model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

//商品
@Entity
@Table(name="t_Product")
public class Product  {
	
	
	@Id
	@GeneratedValue
	private int id;//主键
	
	private String name;//商品名
	
	private int productlock;//是否删除的标志，0表示正常，1表示删除
	
	private double price;//单价


	private String createtime;//添加时间
	
	@ManyToOne
	@JoinColumn(name="fenleiid")
	private Fenlei fenlei;//商品分类
	
	@Column(name="content", columnDefinition="TEXT")
	private String info;//商品简介

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getProductlock() {
		return productlock;
	}

	public void setProductlock(int productlock) {
		this.productlock = productlock;
	}


	

	

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public Fenlei getFenlei() {
		return fenlei;
	}

	public void setFenlei(Fenlei fenlei) {
		this.fenlei = fenlei;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	

	

	

	
	
	
	
	
}
