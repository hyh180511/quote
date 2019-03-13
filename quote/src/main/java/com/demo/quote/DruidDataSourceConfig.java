package com.demo.quote;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by hyh on 2018/10/17.
 */
@Configuration
@EnableTransactionManagement
public class DruidDataSourceConfig  implements EnvironmentAware {

    private RelaxedPropertyResolver propertyResolver;
    private RelaxedPropertyResolver druidPropertyResolver;

    public void setEnvironment(Environment env) {
        this.propertyResolver = new RelaxedPropertyResolver(env, "spring.datasource.");
        this.druidPropertyResolver = new RelaxedPropertyResolver(env, "spring.datasource.druid.");
    }

    @Bean
    public DataSource dataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(propertyResolver.getProperty("url"));
        datasource.setDriverClassName(propertyResolver.getProperty("driver-class-name"));
        datasource.setUsername(propertyResolver.getProperty("username"));
        datasource.setPassword(propertyResolver.getProperty("password"));
        datasource.setInitialSize(Integer.valueOf(druidPropertyResolver.getProperty("initial-size")));
        datasource.setMinIdle(Integer.valueOf(druidPropertyResolver.getProperty("min-idle")));
        datasource.setMaxWait(Long.valueOf(druidPropertyResolver.getProperty("max-wait")));
        datasource.setMaxActive(Integer.valueOf(druidPropertyResolver.getProperty("max-active")));
        datasource.setMinEvictableIdleTimeMillis(Long.valueOf(druidPropertyResolver.getProperty("min-evictable-idle-time-millis")));
        try {
            datasource.setFilters("stat,wall");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datasource;
    }
}