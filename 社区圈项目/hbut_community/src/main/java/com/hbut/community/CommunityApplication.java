package com.hbut.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import utils.JwtUtil;

@SpringBootApplication
@EnableEurekaClient
//下面注解是为了能调用user服务里面的接口
@EnableDiscoveryClient
@EnableFeignClients
public class CommunityApplication {
    public static void main(String[] args){
        SpringApplication.run(CommunityApplication.class,args);
    }

    //添加jwt
    @Bean
    public JwtUtil jwtUtil(){
        return new JwtUtil();
    }
}
