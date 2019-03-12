package com.demo.quote.auth.sercurity;

import com.demo.quote.QuoteManagerApplication;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

@Configuration
@EnableConfigurationProperties({QuoteManagerApplication.CasServerConfig.class, QuoteManagerApplication.CasServiceConfig.class})
public class SecurityConfiguration {

    @Autowired
    private QuoteManagerApplication.CasServerConfig casServerConfig;

    @Autowired
    private QuoteManagerApplication.CasServiceConfig casServiceConfig;

    @Bean
    public ServiceProperties serviceProperties() {
        ServiceProperties serviceProperties = new ServiceProperties();
        serviceProperties.setService(this.casServiceConfig.getHost() +this.casServiceConfig.getServiceName()+ this.casServiceConfig.getLogin());
        serviceProperties.setSendRenew(this.casServiceConfig.getSendRenew());
        return serviceProperties;
    }



    @Bean
    public CasAuthenticationFilter casAuthenticationFilter(AuthenticationManager authenticationManager, ServiceProperties serviceProperties) {
        CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
        casAuthenticationFilter.setAuthenticationManager(authenticationManager);
        casAuthenticationFilter.setServiceProperties(serviceProperties);
        casAuthenticationFilter.setFilterProcessesUrl(this.casServiceConfig.getLogin());
//        casAuthenticationFilter.setContinueChainBeforeSuccessfulAuthentication(false);
        casAuthenticationFilter.setAuthenticationSuccessHandler(
                MyAuthenticationSuccessHandler()
        );
        return casAuthenticationFilter;
    }

    @Bean
    public CasAuthenticationEntryPoint casAuthenticationEntryPoint(ServiceProperties serviceProperties) {
        CasAuthenticationEntryPoint entryPoint = new CasAuthenticationEntryPoint();
        entryPoint.setLoginUrl(this.casServerConfig.getLogin());
        entryPoint.setServiceProperties(serviceProperties);
        return entryPoint;
    }

    @Bean
    public Cas20ServiceTicketValidator cas20ServiceTicketValidator() {
        return new Cas20ServiceTicketValidator(this.casServerConfig.getHost());
    }

    @Bean
    public CasAuthenticationProvider casAuthenticationProvider(
            AuthenticationUserDetailsService<CasAssertionAuthenticationToken> userDetailsService,
            ServiceProperties serviceProperties, Cas20ServiceTicketValidator ticketValidator) {
        CasAuthenticationProvider provider = new CasAuthenticationProvider();
        provider.setKey("an_id_for_this_auth_provider_only");
        provider.setServiceProperties(serviceProperties);
        provider.setTicketValidator(ticketValidator);
        provider.setAuthenticationUserDetailsService(userDetailsService);

        return provider;
    }

    @Bean
    public LogoutFilter logoutFilter() {
//        String logoutRedirectPath = this.casServerConfig.getLogout() + "?service=" +
//                this.casServiceConfig.getHost()+this.casServiceConfig.getServerName()+this.casServiceConfig.getLogin();
        String logoutRedirectPath = this.casServerConfig.getLogout()+ "?service=" +
                this.casServerConfig.getLogin();

        LogoutFilter logoutFilter = new LogoutFilter(logoutRedirectPath, new SecurityContextLogoutHandler());
        logoutFilter.setFilterProcessesUrl(this.casServiceConfig.getLogout());
        return logoutFilter;
    }


    @Bean
    public AuthenticationUserDetailsService<CasAssertionAuthenticationToken> customUserDetailsService()
    {
        UserDetailsByNameServiceWrapper userDetailsByNameServiceWrapper=new UserDetailsByNameServiceWrapper();
        userDetailsByNameServiceWrapper.setUserDetailsService(myUserDetailsService());
        return userDetailsByNameServiceWrapper;
    }

    @Bean
    UserDetailsService myUserDetailsService(){
        return new MyUserDetailsService();
    }


    @Bean
    public AuthenticationSuccessHandler MyAuthenticationSuccessHandler()
    {
        return new MyAuthenticationSuccessHandler();
    }
}
