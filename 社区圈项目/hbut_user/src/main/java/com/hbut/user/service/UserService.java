package com.hbut.user.service;


import VO.UserVO;
import com.hbut.user.dao.UserRepository;
import com.hbut.user.entity.User;
import com.hbut.user.utils.SendSms;
import VO.Result;
import VO.Void;
import enums.ResultEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utils.JwtUtil;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private HttpServletRequest request;
    //@Autowired
    //private RabbitTemplate rabbitTemplate;

    /**获取用户信息**/
    public Result<UserVO> getUserMsg(Integer id){
        //从request里面拿到token，并验证token
        String token = (String) request.getAttribute("claims_user");
        if (token == null || "".equals(token)){
            return Result.newResult(ResultEnum.AUTHENTICATION_ERROR);
        }

        if (id == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        User user = userRepository.findUserById(id);
        if (user == null){
            return Result.newResult(ResultEnum.USER_NOT_EXIST);
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user,userVO);
        return Result.newSuccess(userVO);
    }

    /**通过id获取用户信息，这个是提供给community服务调用的，所以不需要token认证**/
    public Result<UserVO> getUserById(Integer id){
        User user = userRepository.findUserById(id);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user,userVO);
        return Result.newSuccess(userVO);
    }

    /**发送短信验证码**/
    public Result<Void> sendSms(String mobile){
        if (mobile == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        User user = userRepository.findUserByMobile(mobile);
        if (user != null){
            return Result.newResult(ResultEnum.REGISTER_MOBILE_ERROR); //如果手机号被注册了就直接return
        }

        //生成6位验证码
        StringBuilder sBuilder = new StringBuilder();
        Random rd = new Random((new Date()).getTime());
        for(int i = 0; i < 6; ++i) {
            sBuilder.append(String.valueOf(rd.nextInt(9)));
        }

        //将生成的code存到缓存里面，时限是6分钟
        String checkCode = sBuilder.toString();
        redisTemplate.opsForValue().set("checkCode_"+ mobile,checkCode,6, TimeUnit.MINUTES);
        //Map<String,String> map = new HashMap<>();
        //map.put("mobile",mobile);
        //map.put("checkCode",checkCode);
        //rabbitTemplate.convertAndSend("sms",map); 目前还没完全搞懂rabbitMQ，先不用这个先
        SendSms.sendSms(mobile,checkCode);
        return Result.newSuccess();
    }

    /**注册，接受验证码，如果验证码一致就返回true;**/
    public Boolean registerCheck(String code,String mobile){
        //先从缓存中取出验证码
        String checkCode = (String) redisTemplate.opsForValue().get("checkCode_"+ mobile);

        //然后核实缓存里面的验证码与输入验证码是否一致
        if (code.equals(checkCode)){
            return true;
        }else {
            return false;
        }
    }

    /**核实验证码无误后，注册**/
    public Result<Void> register(User user){
        if (user.getNickname() == null || user.getPassword() == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        user.setRegdate(new Date());
        user.setFanscount(0);
        user.setFollowcount(0);
        user.setUpdatedate(new Date());
        user.setLastdate(new Date());
        //对密码进行加密
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return Result.newSuccess();
    }

    /**使用手机号码和密码登陆**/
    public Result<Map<String,String>> loginByPhoneNumber(String mobile,String password){
        //一个jwt实际上就是一个字符串，它由三部分组成，头部、载荷与签名

        if (mobile == null || password == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        User user = userRepository.findUserByMobile(mobile);
        if (user == null){
            return Result.newResult(ResultEnum.LOGIN_ERROR);
        }
        if (encoder.matches(password,user.getPassword())){
            user.setLastdate(new Date());
            userRepository.save(user);
            //使用jwt来实现前后端的通信
            //生成jwt令牌
            String token = jwtUtil.createJWT(user.getId(),user.getMobile(),"user");
            Map<String,String> map = new HashMap<>();
            map.put("token",token);
            map.put("userId",user.getId().toString());
            map.put("role","user");
            return Result.newSuccess(map);
        }else{
            return Result.newResult(ResultEnum.LOGIN_ERROR);
        }
    }

    /**使用邮箱和密码登陆**/
    public Result<Map<String,String>> loginByEmail(String email,String password){
        if (email == null || password == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        User user = userRepository.findUserByEmail(email);
        if (user == null){
            return Result.newResult(ResultEnum.LOGIN_ERROR);
        }
        if (encoder.matches(password,user.getPassword())){
            user.setLastdate(new Date());
            userRepository.save(user);
            //使用jwt来实现前后端的通信
            //生成jwt令牌
            String token = jwtUtil.createJWT(user.getId(),user.getMobile(),"user");
            Map<String,String> map = new HashMap<>();
            map.put("token",token);
            map.put("id",user.getId().toString());
            map.put("role","user");
            return Result.newSuccess(map);
        }else{
            return Result.newResult(ResultEnum.LOGIN_ERROR);
        }
    }

    /**获取用户好友信息**/
    public Result<List<UserVO>> findFriends(Integer id){
        String token = (String) request.getAttribute("claims_user");
        if (token == null || "".equals(token)){
            return Result.newResult(ResultEnum.AUTHENTICATION_ERROR);
        }

        if (id == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        List<User> userList = userRepository.findFriends(id,1);
        List<UserVO> userVOList = new ArrayList<>();
        if (userList.equals(null)){
            return Result.newError("好友不存在");
        }
        for (User user : userList){
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user,userVO);
            userVOList.add(userVO);
        }
        return Result.newSuccess(userVOList);
    }

    /**申请好友**/
    public Result<Void> applyForFriends(Integer userId,Integer friendId){
        String token = (String) request.getAttribute("claims_user");
        if (token == null || "".equals(token)){
            return Result.newResult(ResultEnum.AUTHENTICATION_ERROR);
        }

        if (userId == null || friendId == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        if (userRepository.findFriend(userId,friendId) != null){
            return Result.newResult(ResultEnum.FRIEND_ALREADY_EXIST);
        }
        userRepository.insertFriend(userId,friendId,0,new Date());
        userRepository.insertFriend(friendId,userId,0,new Date());
        return Result.newSuccess();
    }

    /**同意好友请求**/
    public Result<Void> agreeBeFriend(Integer userId,Integer friendId){
        String token = (String) request.getAttribute("claims_user");
        if (token == null || "".equals(token)){
            return Result.newResult(ResultEnum.AUTHENTICATION_ERROR);
        }

        if (userId == null || friendId == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        if (userRepository.findFriend(userId,friendId) == null){
            return Result.newResult(ResultEnum.USER_NOT_EXIST);
        }
        userRepository.updateFriend(userId,friendId,1);
        userRepository.updateFriend(friendId,userId,1);
        return Result.newSuccess();
    }

    /**拒绝好友请求**/
    public Result<Void> disagreeBeFriend(Integer userId,Integer friendId){
        String token = (String) request.getAttribute("claims_user");
        if (token == null || "".equals(token)){
            return Result.newResult(ResultEnum.AUTHENTICATION_ERROR);
        }

        if (userId == null || friendId == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        if (userRepository.findFriend(userId,friendId) == null){
            return Result.newResult(ResultEnum.USER_NOT_EXIST);
        }
        userRepository.deleteFriend(userId,friendId);
        userRepository.deleteFriend(friendId,userId);
        return Result.newSuccess();
    }

    /**展示好友申请列表**/
    public Result<List<UserVO>> wantToBeFriendList(Integer id){
        String token = (String) request.getAttribute("claims_user");
        if (token == null || "".equals(token)){
            return Result.newResult(ResultEnum.AUTHENTICATION_ERROR);
        }

        if (id == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        List<User> userList = userRepository.findFriends(id,0);
        List<UserVO> userVOList = new ArrayList<>();
        if (userList.equals(null)){
            return Result.newError("好友不存在");
        }
        for (User user : userList){
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user,userVO);
            userVOList.add(userVO);
        }
        return Result.newSuccess(userVOList);
    }
}
