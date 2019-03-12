package com.demo.quote.auth.pojo;

import lombok.Data;

import java.io.Serializable;
@Data
public class Resource implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private String interceptUrl;
	private String remark;
	private Integer roleId;
	private String roleName;
	private Integer orderBy;
	

}
