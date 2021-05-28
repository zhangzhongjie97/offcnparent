package com.offcn.user.enums;

public enum UserExceptionEnum {
    //枚举类相当于这三个定义的属性必须是第一个参数为Integer第二个为Stirng 并为枚举类设定有参构造，
    // 再去调用的时候根据你想调用的属性来调用get方法，如需要获取1, "登录账号已经存在"，
    //则需要LOGINACCT_EXIST.getcode.getmsg
    LOGINACCT_EXIST(1, "登录账号已经存在"),
    EMAIL_EXIST(2, "邮箱已经存在"),
    LOGINACCT_LOCKED(3, "账号已经被锁定");
    //验证码
    private Integer code;
    //发送状态
    private String msg;

    UserExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
