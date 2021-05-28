package com.offcn.project.vo;

public class BaseVo {
    //判断当前用户是否是登录状态
    private String accessToken;

    public BaseVo() {
    }

    public BaseVo(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
