package model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

//商品分类
@Entity
@Table(name="t_Fenlei")
public class Fenlei {
	
	@Id
	@GeneratedValue
	private int id;//主键
	
	private String name;//分类名
	
	private int fenleilock;//是否删除的标志，0表示正常，1表示删除


	
	
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

	public int getFenleilock() {
		return fenleilock;
	}

	public void setFenleilock(int fenleilock) {
		this.fenleilock = fenleilock;
	}

	

	

	

	

	
	
	
	
	
}
