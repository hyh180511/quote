package com.demo.quote.util;

import com.demo.quote.pojo.ProjectInfo;
import com.demo.quote.pojo.ProjectMgr;
import com.demo.quote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ProjectMgrUtil {

    @Autowired
    UserService userService;

    public static ProjectMgrUtil projectMgrUtil;

    public ProjectMgrUtil() {
    }

    @PostConstruct
    public void init()
    {
      projectMgrUtil=this;
      projectMgrUtil.userService=this.userService;
    }

    public ProjectMgr getProjectMgr(int proId)
    {
        ProjectMgr projectMgr=new ProjectMgr();
        ProjectInfo projectInfo= userService.loadProjectInfoById(proId);
        projectMgr.setProjectInfo(projectInfo);

        return projectMgr;
    }
}
