package com.demo.quote.auth.pojo;

import lombok.Data;

import java.io.Serializable;
@Data
public class Role implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;// 角色编号
	private String name;// 角色名称
	private String remark;// 角色描述
	private String indexUrl;// 首页url
	private String indexRemark;// 首页描述
	private int level;
	private Integer[] ids;


}
