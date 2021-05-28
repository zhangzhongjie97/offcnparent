package com.offcn.user.service;

import com.offcn.user.bean.TMember;

public interface UserService {
    /**
     * 注册
     * @param member
     */
    public void registerUser(TMember member);

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    public TMember login(String username,String password);

    TMember findUser(String token);
}
