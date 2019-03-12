package com.demo.quote.auth.mapper;

import com.demo.quote.auth.pojo.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleMapper {
    public abstract List<Role> loadRolesByUser(int accountId);
}
