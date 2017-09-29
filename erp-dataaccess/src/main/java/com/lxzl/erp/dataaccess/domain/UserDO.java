package com.lxzl.erp.dataaccess.domain;

import java.util.Date;

import com.lxzl.se.common.util.poi.ExcelField;
import com.lxzl.se.dataaccess.mysql.domain.BaseDO;

public class UserDO extends BaseDO {

	/**
	 * 系统
	 */
	public static final String SYS_USER = "SYS";
	public static final String DEFAULT_PASSWORD = "123456";
	public static final Integer STATUS_NORMAL = 1;

	/**
	 * 
	 */
	private static final long serialVersionUID = -9099613469600917361L;

	private Integer id;
	
	/**
	 * 用户名
	 */
	@ExcelField(order = 0, header = "用户名")
	private String username;
	/**
	 * 密码
	 */
	@ExcelField(order = 1, header = "密码")
	private String password;
	/**
	 * 昵称
	 */
	@ExcelField(order = 2, header = "昵称")
	private String nick;
	/**
	 * 性别
	 */
	@ExcelField(order = 3, header = "性别")
	private Integer sex;
	/**
	 * 年龄
	 */
	private Integer age;
	/**
	 * 生日
	 */
	private Date birthday;
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 电话
	 */
	private String tel;
	/**
	 * 邮箱
	 */
	private String email;
	/**
	 * 状态
	 */
	private Integer status;
	/**
	 * 最后登陆时间
	 */
	private Date lastLoginTime;
	/**
	 * 最后登陆ip
	 */
	private String lastLoginIp;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	@Override
	public String toString() {
		return "UserDO [id=" + id + ", username=" + username + ", password=" + password + ", nick=" + nick + ", sex=" + sex + ", age=" + age + ", birthday="
				+ birthday + ", address=" + address + ", tel=" + tel + ", email=" + email + ", status=" + status + ", lastLoginTime=" + lastLoginTime
				+ ", lastLoginIp=" + lastLoginIp + "]";
	}

}
