package com.hbut.community.client.impl;

import VO.Result;
import VO.UserVO;
import com.hbut.community.client.UserClient;
import org.springframework.stereotype.Component;

@Component
public class UserClientImpl implements UserClient {
    @Override
    public Result<UserVO> getUserById(Integer id) {
        return Result.newError("hbut-user服务出错,熔断器启动");
    }
}
