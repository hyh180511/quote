package com.demo.quote.auth.sercurity;
import com.demo.quote.auth.pojo.LoginUser;
import org.jboss.logging.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


/**
 * spring security安全机制，当用户登录成功后，执行该handler的onAuthenticationSuccess（）函数
 * 
 * 在onAuthenticationSuccess()中通常进行如下的工作：
 * 1. 进行与登录用户相关的初始化操作
 * 2. 设置与登录用户相关的默认登录页面
 * 3. 其他
 * 
 * @author mulan
 * 
 */
@Component
public class MyAuthenticationSuccessHandler extends
        SimpleUrlAuthenticationSuccessHandler {


	/**
	 * 用户登录成功时被执行
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
			throws ServletException, IOException {

		// 获取登录用户
		LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		// 初始化 log4j 配置
		String userName = loginUser.getUsername();
		String userIP = request.getRemoteAddr();
		MDC.put("userName", userName);
		MDC.put("userIP", userIP);
		
		//将用户的信息存入session中
		HttpSession session = request.getSession();
		session.setAttribute("User", loginUser.getName());
		session.setAttribute("UserID", loginUser.getId());

		
		// 根据用户角色进入指定默认页面
		// 当前涉及中，一个用户具有多个角色，因此，必须制定默认角色
		// List<Role> roles = loginUser.getRoles();
		
		String user_default_url = loginUser.getDefaultUrl();
		if(user_default_url != null && user_default_url.trim().length() >0){
			this.setDefaultTargetUrl(user_default_url);
		}
		super.onAuthenticationSuccess(request, response, authentication);

	}
}
