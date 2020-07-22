package model;



import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

//商品图片
@Entity
@Table(name="t_Pic")
public class Pic  {
	
	
	@Id
	@GeneratedValue
	private int id;//主键
	
	private int piclock;//是否删除的标志，0表示正常，1表示删除
	
	@ManyToOne
	@JoinColumn(name="productid")
	private Product product;//关联的商品，外键
	
	private String imgpath;//商品图片

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPiclock() {
		return piclock;
	}

	public void setPiclock(int piclock) {
		this.piclock = piclock;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getImgpath() {
		return imgpath;
	}

	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}
	
	
	
	
	
	
	
	
	
}
