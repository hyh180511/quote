package com.demo.quote.auth.sercurity;
import com.demo.quote.QuoteManagerApplication;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsUtils;

@Configuration
//@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
//    @Autowired
//    private MyFilterSecurityInterceptor myFilterSecurityInterceptor;
//
//    @Autowired
//    private MyAuthenticationSuccessHandler  myAuthenticationSuccessHandler;
//
//    @Bean
//    UserDetailsService myUserDetailsService(){
//        return new MyUserDetailsService();
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .anyRequest().authenticated() //任何请求,登录后可以访问
//                .and()
//                .formLogin() // 定义当需要用户登录时候，转到的登录页面。
//                .loginPage("/login")// 设置登录页面
//                .loginProcessingUrl("/j_spring_security_check") // 自定义的登录接口
//                .successHandler(myAuthenticationSuccessHandler)
//                .failureUrl("/login?error")
//                .permitAll() //登录页面用户任意访问
//                .and()
//                .headers().frameOptions().disable()
//                .and()
//                .logout().permitAll()
//                .logoutUrl("/quote/logout")
//                .logoutSuccessUrl("/login")
//                .and()
//                .sessionManagement().invalidSessionUrl("/login"); //注销行为任意访问
//
//        http.addFilterBefore(myFilterSecurityInterceptor, FilterSecurityInterceptor.class).csrf().disable();// 关闭csrf防护
//
//    }
//
//    @Override
//    public void configure(WebSecurity web) throws Exception {
////        web.ignoring().antMatchers("/js/**", "/css/**", "/images/**", "/**/favicon.ico");
//        web.ignoring().antMatchers("/**/*.js", "/lang/*.json", "/**/*.css", "/**/*.js", "/**/*.map", "/**/*.html", "/**/*.png");
//        //防止拦截css,js，image文件
//    }
////
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(myUserDetailsService());
//        //登录验证，绑定自定义的UserDetailServiceHolder
//    }

//               单点登录数据配置
//       -----------------------------------------------------------------------------------------------

    @Autowired
    private CasAuthenticationEntryPoint casAuthenticationEntryPoint;

    @Autowired
    private CasAuthenticationProvider casAuthenticationProvider;

    @Autowired
    private CasAuthenticationFilter casAuthenticationFilter;

    @Autowired
    private LogoutFilter logoutFilter;

    @Autowired
    private QuoteManagerApplication.CasServerConfig casServerConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();

        http.csrf().disable();

        http.authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers("/**/*.js", "/lang/*.json", "/**/*.css", "/**/*.js", "/**/*.map", "/**/*.html", "/**/*.png").permitAll()//防止拦截css,js，image文件
                .antMatchers("/static/**").permitAll() // 不拦截静态资源
                .antMatchers("/api/**").permitAll()  // 不拦截对外API
                .anyRequest().authenticated();  // 所有资源都需要登陆后才可以访问。

        http.logout().permitAll();  // 不拦截注销

        http.exceptionHandling().authenticationEntryPoint(casAuthenticationEntryPoint);
        // 单点注销的过滤器，必须配置在SpringSecurity的过滤器链中，如果直接配置在Web容器中，貌似是不起作用的。我自己的是不起作用的。
        SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
        singleSignOutFilter.setCasServerUrlPrefix(this.casServerConfig.getHost());

        http.addFilter(casAuthenticationFilter)
                .addFilterBefore(logoutFilter, LogoutFilter.class)
                .addFilterBefore(singleSignOutFilter, CasAuthenticationFilter.class);

        http.antMatcher("/**");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(casAuthenticationProvider);
    }

    @Bean
    public ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> singleSignOutHttpSessionListener(){
        ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> servletListenerRegistrationBean =
                new ServletListenerRegistrationBean<>();
        servletListenerRegistrationBean.setListener(new SingleSignOutHttpSessionListener());
        return servletListenerRegistrationBean;
    }


}