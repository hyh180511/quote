package com.demo.quote.auth.service;
import com.demo.quote.auth.mapper.ResourceMapper;
import com.demo.quote.auth.pojo.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("resourceService")
public class ResourceServiceImpl implements ResourceService {

	@Autowired
	private ResourceMapper resourceMapper;
	
	@Override
	public List<Resource> loadAllResources() {
		return resourceMapper.loadAllResources();
	}

	@Override
	public List<Resource> loadAllRoleResources() {
		return resourceMapper.loadAllRoleResources();
	}
	
	/**
	 * 加载菜单对应的所有监测资源
	 * @param privilegeId 菜单ID
	 * @return
	 */
	@Override
	public List<Resource> loadResourceByPrivilege(int privilegeId){
		return resourceMapper.loadResourceByPrivilege(privilegeId);
	}

}
