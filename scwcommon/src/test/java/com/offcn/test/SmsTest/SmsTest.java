package com.offcn.test.SmsTest;

import com.offcn.user.util.HttpUtils;
import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

public class SmsTest {
    public static void main(String[] args) {
        String host = "http://dingxintz.market.alicloudapi.com";
        String path = "/dx/notifySms";
        String method = "POST";
        String appcode = "227e5ddce7074c1fb1b784b739ea25f8";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", "13056760963");
        querys.put("param", "name:zhangZhongJie");
        querys.put("tpl_id", "TP1711063");
        Map<String, String> bodys = new HashMap<String, String>();


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
