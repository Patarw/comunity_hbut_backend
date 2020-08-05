package com.hbut.user.controller;

import VO.UserVO;
import com.hbut.user.entity.User;
import com.hbut.user.form.UserForm;
import com.hbut.user.service.UserService;
import VO.Result;
import VO.Void;
import enums.ResultEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Api(tags = "用户管理接口")
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    /**获取用户信息（需要token）**/
    @ApiImplicitParam(name = "Authorization", value = "需要token认证", required = false, dataType = "String",paramType = "header")
    @ApiOperation("获取用户信息（需要token）")
    @RequestMapping(value = "/getUserMsg",method = RequestMethod.POST)
    public Result<UserVO> getUserMsg(Integer id){
        return userService.getUserMsg(id);
    }

    /**通过id获取用户信息，这个是提供给community服务调用的，非前端接口！！**/
    @ApiOperation("通过id获取用户信息，这个是提供给community服务调用的，非前端接口！！")
    @RequestMapping(value = "/getUserById",method = RequestMethod.GET)
    public Result<UserVO> getUserById(Integer id){
       return userService.getUserById(id);
    }

    /**发送短信验证码**/
    @ApiOperation("发送短信验证码")
    @RequestMapping(value = "/sendSms",method = RequestMethod.POST)
    public Result<Void> createUser(String mobile){
        return userService.sendSms(mobile);
    }

    /**核实验证码无误后，注册**/
    @ApiOperation("核实输入的短信验证码,注册用户")
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public Result<Void> register(String code,@RequestBody User user){
        if (code == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        if (userService.registerCheck(code,user.getMobile())){
            return userService.register(user);
        }else{
            return Result.newResult(ResultEnum.REGISTER_ERROR);
        }
    }

    /**使用手机号码和密码登陆**/
    @ApiOperation("使用手机号码和密码登陆")
    @RequestMapping(value = "/loginByPhoneNumber",method = RequestMethod.POST)
    public Result<Map<String,String>> loginByPhoneNumber(String mobile, String password){
        return userService.loginByPhoneNumber(mobile,password);
    }

    /**使用邮箱和密码登陆**/
    @ApiOperation("使用邮箱和密码登陆")
    @RequestMapping(value = "/loginByEmail",method = RequestMethod.POST)
    public Result<Map<String,String>> loginByEmail(String email,String password){
       return userService.loginByEmail(email,password);
    }

    /**获取用户好友信息（需要token）**/
    @ApiImplicitParam(name = "Authorization", value = "需要token认证", required = false, dataType = "String",paramType = "header")
    @ApiOperation("通过用户id获取该用户的好友列表（需要token）")
    @RequestMapping(value = "/findFriends",method = RequestMethod.POST)
    public Result<List<UserVO>> findFriends(Integer id){
       return userService.findFriends(id);
    }

    /**申请好友（需要token）**/
    @ApiImplicitParam(name = "Authorization", value = "需要token认证", required = false, dataType = "String",paramType = "header")
    @ApiOperation("申请好友（需要token）")
    @RequestMapping(value = "/applyForFriends",method = RequestMethod.POST)
    public Result<Void> applyForFriends(@RequestParam("userId") Integer userId,@RequestParam("friendId") Integer friendId){
      return userService.applyForFriends(userId,friendId);
    }

    /**同意好友请求（需要token）**/
    @ApiImplicitParam(name = "Authorization", value = "需要token认证", required = false, dataType = "String",paramType = "header")
    @ApiOperation("同意好友请求（需要token）")
    @RequestMapping(value = "/agreeBeFriend",method = RequestMethod.POST)
    public Result<Void> agreeBeFriend(@RequestParam("userId") Integer userId,@RequestParam("friendId") Integer friendId){
       return userService.agreeBeFriend(userId,friendId);
    }

    /**拒绝好友请求（需要token）**/
    @ApiImplicitParam(name = "Authorization", value = "需要token认证", required = false, dataType = "String",paramType = "header")
    @ApiOperation("拒绝好友请求（需要token）")
    @RequestMapping(value = "/disagreeBeFriend",method = RequestMethod.POST)
    public Result<Void> disagreeBeFriend(@RequestParam("userId") Integer userId,@RequestParam("friendId") Integer friendId){
       return userService.disagreeBeFriend(userId,friendId);
    }

    /**展示好友申请列表（需要token）**/
    @ApiImplicitParam(name = "Authorization", value = "需要token认证", required = false, dataType = "String",paramType = "header")
    @ApiOperation("展示好友申请列表（需要token）")
    @RequestMapping(value = "/wantToBeFriendList",method = RequestMethod.POST)
    public Result<List<UserVO>> wantToBeFriendList(Integer id){
      return userService.wantToBeFriendList(id);
    }


    /**通过mobile获取用户id**/
    @ApiImplicitParam(name = "Authorization", value = "需要token认证", required = false, dataType = "String",paramType = "header")
    @ApiOperation("通过mobile获取用户id")
    @RequestMapping(value = "/getUserIdByMobile",method = RequestMethod.POST)
    public Result<Map<String,Integer>> getUserIdByMobile(String mobile){
        return userService.getUserIdByMobile(mobile);
    }

    /**更新用户信息**/
    @ApiImplicitParam(name = "Authorization", value = "需要token认证", required = false, dataType = "String",paramType = "header")
    @ApiOperation("更新用户信息,UserForm里面的参数传几个更新几个，但不能为空")
    @RequestMapping(value = "/updateUserMsg",method = RequestMethod.POST)
    public Result<Void> updateUserMsg(Integer userId, @RequestBody UserForm userForm){
        return userService.updateUserMsg(userId,userForm);
    }

    /**修改手机号**/
    @ApiImplicitParam(name = "Authorization", value = "需要token认证", required = false, dataType = "String",paramType = "header")
    @ApiOperation("修改手机号")
    @RequestMapping(value = "/updateMobile",method = RequestMethod.POST)
    public Result<Void> updateMobile(Integer userId,String mobile,String code){
        if (code == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        if (userService.registerCheck(code,mobile)){
            return userService.updateMobile(userId,mobile);
        }else{
            return Result.newResult(ResultEnum.REGISTER_ERROR);
        }
    }

    /**通过手机验证修改密码**/
    @ApiImplicitParam(name = "Authorization", value = "需要token认证", required = false, dataType = "String",paramType = "header")
    @ApiOperation("通过手机验证修改密码")
    @RequestMapping(value = "/updatePassword",method = RequestMethod.POST)
    public Result<Void> updatePassword(Integer userId,String password,String code,String mobile){
        if (code == null || mobile == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        if (userService.registerCheck(code,mobile)){
            return userService.updatePassword(userId,password);
        }else{
            return Result.newResult(ResultEnum.REGISTER_ERROR);
        }
    }

    /**上传图片文件到阿里云服务器，返回图片url**/
    @ApiOperation("上传图片文件到阿里云服务器，返回图片url,最多2MB,图片格式最好为png，jpg")
    @RequestMapping(value = "/uploadImage",method = RequestMethod.POST)
    public Result<Map<String,String>> uploadImage(@RequestParam("file") MultipartFile file){
        return userService.uploadImage(file);
    }
}
