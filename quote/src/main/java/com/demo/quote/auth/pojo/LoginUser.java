package com.demo.quote.auth.pojo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;


/**
 * 系统登录用户，继承user实体类
 * 
 * @author mulan
 * 
 */
public class LoginUser extends User implements UserDetails, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 登录录用的权限集合，一个登录用户可以拥有多种角色
	 */
	private Set<GrantedAuthority> authorities;

	private List<Role> roles;

	/**
	 * 获取 登录录用的权限集合
	 */
	public Collection<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	@Override
	public boolean equals(Object rhs) {
		if (rhs instanceof LoginUser)
			return this.getUsername().equals(((LoginUser) rhs).getUsername());
		return false;
	}

	@Override
	public int hashCode() {
		return this.getUsername().hashCode();
	}

	@Override
	public String toString() {
		String userString = super.toString();
		String roleString = "Role [-";
		if (roles != null) {
			for (Role role : roles) {
				roleString = roleString + role.getName() + "#";
			}
		}
		roleString = roleString + "]";
		return userString + " " + roleString;
	}

	@Override
	public String getPassword() {
		return this.getPass();
	}

	@Override
	public String getUsername() {
		return this.getLogin();
	}

	/**
	 * 获取 是否过期
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * 是否被锁定
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * 密码是否失效
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * 是否启用
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}

}
