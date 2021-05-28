package com.offcn.user.exception;

import com.offcn.user.enums.UserExceptionEnum;

public class UserException extends RuntimeException {

    /**
     * 表单所有用户异常
     */

    public UserException(UserExceptionEnum exceptionEnum) {
        //每次发生异常就抛出这个枚举类中定义的状态msg
        super(exceptionEnum.getMsg());
    }
}
