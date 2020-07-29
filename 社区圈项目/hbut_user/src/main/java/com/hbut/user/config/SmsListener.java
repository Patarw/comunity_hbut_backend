package com.hbut.user.config;


import org.springframework.stereotype.Component;

import java.util.Map;

//rabbitMQ监听器,接收短信验证码
/*@Component
@RabbitListener(queues = "sms")*/
public class SmsListener {

 /*   //接收短信验证码
    @RabbitHandler
    public void executeSms(Map<String,String> map){
        System.out.println(map.get("mobile"));
        System.out.println(map.get("checkCode"));
    }*/
}
