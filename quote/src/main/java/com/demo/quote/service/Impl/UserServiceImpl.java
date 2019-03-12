package com.demo.quote.service.Impl;

import com.demo.quote.mapper.UserMapper;
import com.demo.quote.pojo.ProjectInfo;
import com.demo.quote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("UserService")
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;


    @Override
    public Object loadUser() {
        return userMapper.loadUser();
    }

    @Override
    public ProjectInfo loadProjectInfoById(int proId) {
        return userMapper.loadProjectInfoById(proId);
    }
}
