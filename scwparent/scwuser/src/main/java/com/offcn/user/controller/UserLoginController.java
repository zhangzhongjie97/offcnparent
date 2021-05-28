package com.offcn.user.controller;

import com.alibaba.fastjson.JSON;
import com.offcn.dycommons.AppResponse;
import com.offcn.user.bean.TMember;
import com.offcn.user.service.UserService;
import com.offcn.user.util.SmsTemplate;
import com.offcn.user.vo.req.UserRegistVo;
import com.offcn.user.vo.req.UserRespVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Api(tags = "用户登录/注册模块（包括忘记密码等）")
@Slf4j
public class UserLoginController {


    @Autowired
    private SmsTemplate smsTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @ApiOperation("获取注册的验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phoneNo", value = "手机号", required = true)
    })//@ApiImplicitParams：描述所有参数；@ApiImplicitParam描述某个参数
    @PostMapping("/sendCode")
    public AppResponse<Object> sendCode(String phoneNo) {
        //1、生成验证码保存到服务器，准备用户提交上来进行对比
        String code = UUID.randomUUID().toString().substring(0, 4);
        //2、保存验证码和手机号的对应关系,设置验证码过期时间
        redisTemplate.opsForValue().set(phoneNo, code, 10000, TimeUnit.MINUTES);
        //3、短信发送构造参数
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", phoneNo);
        querys.put("param", "code:" + code);
        querys.put("tpl_id", "TP1711063");//短信模板
        //4、发送短信
        String sendCode = smsTemplate.sendCode(querys);
        if (sendCode.equals("") || sendCode.equals("fail")) {
            //短信失败
            return AppResponse.fail("短信发送失败");
        }
        return AppResponse.ok(sendCode);
    }

    @ApiOperation("用户注册")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "user", value = "用户名", required = true)
    })
    @PostMapping("/register")
    public AppResponse<Object> register(UserRegistVo user) {
        String Code = redisTemplate.opsForValue().get(user.getLoginacct());

        //验证验证码
        if (!Code.isEmpty() && Code != "") {
            if (Code.equalsIgnoreCase(user.getCode())) {
                try {
                    //转移bean
                    TMember tMember = new TMember();
                    BeanUtils.copyProperties(user, tMember);

                    userService.registerUser(tMember);
                    //生成日志
                    log.debug("用户信息注册成功：{}", tMember.getLoginacct());
                    //删除redis
                    redisTemplate.delete(user.getLoginacct());
                    //返回成功
                    return AppResponse.ok(null);
                } catch (BeansException e) {
                    e.printStackTrace();

                    return AppResponse.fail(e.getMessage());
                }
            } else {
                return AppResponse.fail("验证码错误");
            }
        } else {
            return AppResponse.fail("验证码过期了");
        }
    }

    @ApiOperation("用户登录")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "username", value = "用户名",required = true),
            @ApiImplicitParam(name = "password", value = "密码",required = true)
    })
    @PostMapping("/login")
    public AppResponse login(String username, String password) {
        TMember member = userService.login(username, password);
        if (member == null) {
            AppResponse response = AppResponse.fail(member);
            response.setMsg("登录失败");
            return response;
        } else {
            //2、登录成功;生成令牌
            String token = UUID.randomUUID().toString().replace("-", "");
            UserRespVo respVo = new UserRespVo();
            respVo.setAccessToken(token);
            BeanUtils.copyProperties(member, respVo);

            //3、经常根据令牌查询用户的id信息
            redisTemplate.opsForValue().set(respVo.getAccessToken(), member.getId().toString(), 2, TimeUnit.HOURS);

            //4.将个人信息存放redis中，用户需要信息取时直接从redis取值减少mysql的一次攻击
            redisTemplate.boundValueOps(member.getId().toString()).set(JSON.toJSONString(respVo));
            return AppResponse.ok(respVo);
        }
    }
    @ApiOperation("令牌获取用户信息")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token",value = "令牌",required = true)
    })
    @GetMapping("findUser/{token}")
    public AppResponse findUser(@PathVariable("token") String token){
       TMember tMember = userService.findUser(token);
       if(tMember == null){
           AppResponse response = AppResponse.fail(token);
           response.setMsg("令牌无效");
           return response;
       }
       return AppResponse.ok(tMember);
    }
}