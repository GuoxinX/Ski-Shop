package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

//用户
@Entity
@Table(name="t_User")
public class User  {
	

	@Id
	@GeneratedValue
	private int id;//主键

	private int userlock;//是否删除的标志，0表示正常，1表示删除
	
	private String username;//用户名
	
	private String password;//密码
	
	private String truename;//姓名
	
	private String phone;//手机号码
	
	private String createtime;//注册时间

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserlock() {
		return userlock;
	}

	public void setUserlock(int userlock) {
		this.userlock = userlock;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTruename() {
		return truename;
	}

	public void setTruename(String truename) {
		this.truename = truename;
	}


	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}


	
	
	

	

	

	

	
	
	
	
	
}
