package com.offcn.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.offcn.user.bean.TMember;
import com.offcn.user.bean.TMemberExample;
import com.offcn.user.enums.UserExceptionEnum;
import com.offcn.user.exception.UserException;
import com.offcn.user.mapper.TMemberMapper;
import com.offcn.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private TMemberMapper tMemberMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Override
    public void registerUser(TMember member) {

        // 1、检查系统中此手机号是否存在
        TMemberExample example = new TMemberExample();
        TMemberExample.Criteria criteria = example.createCriteria();
        criteria.andLoginacctEqualTo(member.getLoginacct());
        List<TMember> tMembers =
                tMemberMapper.selectByExample(example);
        if (tMembers.size() < 0) {
            throw new UserException(UserExceptionEnum.LOGINACCT_EXIST);
        } else {// 2、手机号未被注册，设置相关参数，保存注册信息

            //密码加密类
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String encode = encoder.encode(member.getUserpswd());
            //设置密码
            member.setUserpswd(encode);
            member.setUsername(member.getLoginacct());
            //member.setEmail(member.getEmail());
            //实名认证状态 0 - 未实名认证， 1 - 实名认证申请中， 2 - 已实名认证
            member.setAuthstatus("0");
            //用户类型: 0 - 个人， 1 - 企业
            member.setUsertype("0");
            //账户类型: 0 - 企业， 1 - 个体， 2 - 个人， 3 - 政府
            member.setAccttype("2");
            System.out.println("插入数据:" + member.getLoginacct());
            tMemberMapper.insert(member);

        }
        //设置密码
        //实名认证状态 0 - 未实名认证， 1 - 实名认证申请中， 2 - 已实名认证
        //用户类型: 0 - 个人， 1 - 企业
        //账户类型: 0 - 企业， 1 - 个体， 2 - 个人， 3 - 政府

    }

    @Override
    public TMember login(String username, String password) {
        TMemberExample example = new TMemberExample();
        TMemberExample.Criteria criteria = example.createCriteria();
        criteria.andLoginacctEqualTo(username);
        //取到用户信息
        List<TMember> tMembers = tMemberMapper.selectByExample(example);
        //接收
        TMember tMember = null;
        if (tMembers.size() > 0) {
            tMember = tMembers.get(0);
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(password, tMember.getUserpswd())) {
            return tMember;
        }

        return null;
    }

    @Override
    public TMember findUser(String token) {
        //根据令牌获取id
        String id = redisTemplate.opsForValue().get(token);

        //根据id获取信息
        String jsonObj = redisTemplate.boundValueOps(id.toString()).get();
        //转对象
        TMember tMember = JSON.parseObject(jsonObj, TMember.class);

        return tMember;
    }
}
