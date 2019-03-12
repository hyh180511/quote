package com.demo.quote;

import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.WebApplicationInitializer;
@EnableTransactionManagement // 启注解事务管理，等同于xml配置方式的 <tx:annotation-driven />
@SpringBootApplication
@ServletComponentScan
public class QuoteManagerApplication extends SpringBootServletInitializer implements WebApplicationInitializer {

//	public static void main(String[] args) {
//		SpringApplication.run(QuoteManagerApplication.class, args);
//	}

	@Data
	@ConfigurationProperties(prefix = "security.cas.server")
	public class CasServerConfig {
		private String host;
		private String login;
		private String logout;

	}

	@Data
	@ConfigurationProperties(prefix = "security.cas.service")
	public class CasServiceConfig {
		private String host;
		private String login;
		private String logout;
		private String serviceName;
		private Boolean sendRenew = false;
	}


	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application){

		return application.sources(QuoteManagerApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(QuoteManagerApplication.class, args);
	}

}
