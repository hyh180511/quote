package com.demo.quote.auth.pojo;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
public class User implements Serializable {
	/**
	 * User类
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;// 用户编号
	private String name;// 用户名
	private String login;// 登录名
	private String pass;// 登录密码
	private String remark;// 角色描述
	private List<Integer> allRoleIds = new ArrayList<Integer>();
	private Integer rid;
	private String rname;
	private String defaultUrl;
	private String realName;//真实姓名
	private String telephone;//联系电话
	private String jobTitle;//职位名称

	public User() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;

	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}


	public List<Integer> getAllRoleIds() {
		return allRoleIds;
	}

	public void setAllRoleIds(List<Integer> allRoleIds) {
		this.allRoleIds = allRoleIds;
	}

	public String getDefaultUrl() {
		return defaultUrl;
	}

	public void setDefaultUrl(String defaultUrl) {
		this.defaultUrl = defaultUrl;
	}

	public Integer getRid() {
		return rid;
	}

	public void setRid(Integer rid) {
		this.rid = rid;
		allRoleIds.add(rid);
	}

	public String getRname() {
		return rname;
	}

	public void setRname(String rname) {
		this.rname = rname;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
}
