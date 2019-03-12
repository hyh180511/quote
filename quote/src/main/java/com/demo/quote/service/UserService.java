package com.demo.quote.service;

import com.demo.quote.pojo.ProjectInfo;

public interface UserService {

    public Object loadUser();

    public ProjectInfo loadProjectInfoById(int proId);
}
