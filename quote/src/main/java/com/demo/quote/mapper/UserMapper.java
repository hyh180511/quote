package com.demo.quote.mapper;

import com.demo.quote.pojo.ProjectInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    public List<String> loadUser();

    public ProjectInfo loadProjectInfoById(int proId);
}
