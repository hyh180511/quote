package com.demo.quote.auth.sercurity;

import com.demo.quote.auth.pojo.Resource;
import com.demo.quote.auth.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class MyInvocationSecurityMetadataSourceService implements FilterInvocationSecurityMetadataSource {

    private final static String ROLE_PROFIX = "ROLE_";

    @Autowired
    private ResourceService resourceService;

    private Map<RequestMatcher, Collection<ConfigAttribute>> requestMap;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        //System.out.println("get All getAttributes...");
        final HttpServletRequest request = ((FilterInvocation) object)
                .getRequest();

            if (requestMap ==null)
            {
                loadRequestMap();
            }

            // LinkedHashMap 维持插入顺序，即先插入先访问
            for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap
                    .entrySet()) {
                // 因此，数据库配置中position靠前（1~N)的规则优先起作用
                if (entry.getKey().matches(request)) {
                    return entry.getValue();
                }
            }

        // 如果没有配置规则，即该资源未进行安全保护
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
      return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    private void loadRequestMap() {
        // 从数据库获取所有拦截配置
        // 顺序(1~N)，优先匹配低position的，如果成功，返回；意味着低order的规则优先执行
        List<Resource> accessAttributes = resourceService.loadAllRoleResources();
        // 构造FilterInvocationSecurityMetadataSource的规则集合
        // @RequestMatcher: 拦截url的ANT类型表达式，即资源
        // @Collection<ConfigAttribute>: 对应RequestMatcher的可访问角色集合（Role）
        // LinkedHashMap 维持插入顺序，即先插入先访问
        Map<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>();

        // MatcherType matcherType = MatcherType.ant; // 当前仅支持ant形式，不支持
        for (Resource accessAttribute : accessAttributes) {
            String path = accessAttribute.getInterceptUrl();
            // 去掉空的配置
            if (!StringUtils.hasText(path)) {
                continue;
            }
            String roleName = accessAttribute.getRoleName();
            if (!StringUtils.hasText(roleName)) {
                continue;
            }
            // 安全配置，角色名称的封装；实现ConfigAttribute接口
            SecurityConfig securityConfig = new SecurityConfig(ROLE_PROFIX+roleName);
            // 根据配置构造相应的RequestMatcher
            RequestMatcher requestMatcher;
            if ("/**".equals(path) || "**".equals(path)) {
                // 匹配任何规则，尽量不在配置中使用
                requestMatcher = AnyRequestMatcher.INSTANCE;
            } else {
                requestMatcher = new AntPathRequestMatcher(path); // 默认
            }
            // 构造相应的可访问角色集合
            Collection<ConfigAttribute> attributes;
            if (requestMap.containsKey(requestMatcher)) {
                // 该path重复，一个资源可多个角色访问
                attributes = requestMap.get(requestMatcher);
                attributes.add(securityConfig);
            } else {
                // 该path首次分析
                attributes = new LinkedList<ConfigAttribute>();
                attributes.add(securityConfig);
                requestMap.put(requestMatcher, attributes);
            }
        }
        // 更新requestMap
        this.setRequestMap(requestMap);
    }

    public void setRequestMap(Map<RequestMatcher, Collection<ConfigAttribute>> requestMap) {

        this.requestMap = requestMap;

    }
}
