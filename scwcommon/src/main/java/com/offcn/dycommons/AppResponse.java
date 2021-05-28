package com.offcn.dycommons;

//T表示Type
public class AppResponse<T> {
    private Integer code;
    private String msg;
    private T data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> AppResponse<T> ok(T data) {
        AppResponse appResponse = new AppResponse();
        appResponse.setCode(ResponseCodeEnume.SUCCESS.getCode());
        appResponse.setMsg(ResponseCodeEnume.SUCCESS.getMsg());
        //这里的Data是任何要返回到前台的对象，可以当参数传进来返回到前台
        appResponse.setData(data);
        return appResponse;
    }

    public static <T> AppResponse<T> fail(T data) {
        AppResponse appResponse = new AppResponse();
        appResponse.setCode(ResponseCodeEnume.FAIL.getCode());
        appResponse.setMsg(ResponseCodeEnume.FAIL.getMsg());
        //这里的Data是任何要返回到前台的对象，可以当参数传进来返回到前台
        appResponse.setData(data);
        return appResponse;
    }
}
