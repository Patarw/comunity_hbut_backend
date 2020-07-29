package com.hbut.user.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//安全配置类
//导入了spring security的依赖要加上这个配置类，不然的话会拦截所有访问路径
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests() //所有security全注解配置实现的开端
                .antMatchers("/**").permitAll() //允许所有路径通过
                .anyRequest().authenticated() //任何请求，认证后可以登陆
                .and().csrf().disable(); //使crsf拦截失效
    }
}
