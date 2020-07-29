package com.hbut.community.client;

import VO.UserVO;
import VO.Result;
import com.hbut.community.client.impl.UserClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**利用feign实现服务之间的调用，这里需要调用的是用户user服务**/
@FeignClient(name = "hbut-user",fallback = UserClientImpl.class)
public interface UserClient {
    @RequestMapping(value = "/user/getUserById",method = RequestMethod.GET)
    public Result<UserVO> getUserById(@RequestParam("id") Integer id);
}
