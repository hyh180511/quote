package com.demo.quote.auth.sercurity;

import com.demo.quote.auth.mapper.LoginUserMapper;
import com.demo.quote.auth.mapper.RoleMapper;
import com.demo.quote.auth.pojo.LoginUser;
import com.google.common.collect.Sets;
import com.demo.quote.auth.pojo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service

public class MyUserDetailsService implements UserDetailsService {
    private final static String ROLE_PROFIX = "ROLE_";

    /**
     * 登录用户访问接口
     */
    @Autowired
    private LoginUserMapper loginUserMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        LoginUser loginUser = null;
        try {
            // 从数据库获取用户
            loginUser = loginUserMapper.loadUserByUsername(username);
            // 用户不存在
            if (loginUser == null) {
                throw new UsernameNotFoundException("用户" + username + " 不存在!");
            }
            Integer userId = loginUser.getId();
            //设置登陆用户的角色
            List<Role> roles = roleMapper.loadRolesByUser(userId);
            if(roles==null||roles.size() == 0){
                throw new UsernameNotFoundException("用户" + username + " 不存在!");
            }
            loginUser.setRoles(roles);
            // 设置登录用户的权限
            Set<GrantedAuthority> authorities = Sets.newHashSet();
            for(Role role: roles){
                authorities.add(new SimpleGrantedAuthority(ROLE_PROFIX+role.getName()));
            }
            loginUser.setAuthorities(authorities);

            //设置用户登陆成功后打开页面
            loginUser.setDefaultUrl(roles.get(0).getIndexUrl());
        } catch (Exception e) {
            //
            e.printStackTrace();
        }
        // 返回登录用户
        return loginUser;
    }
}
