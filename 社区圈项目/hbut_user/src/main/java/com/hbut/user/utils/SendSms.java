package com.hbut.user.utils;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;


//阿里云短信服务
public class SendSms {

    public static void sendSms(String phone,String code) {

        String accessKeyId =  "LTAI4G459CTsSao7XEQvxe5t";
        String accessSecret = "pzpKI0z331pPsbtggPfyDgiDdZ0Lzq";

        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("SignName", "黎展鹏短信验证接口");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("TemplateCode", "SMS_197611107");

        JSONObject object=new JSONObject();
        object.put("checkCode",code);
        request.putQueryParameter("TemplateParam",object.toJSONString());

        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
