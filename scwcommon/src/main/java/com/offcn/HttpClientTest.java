package com.offcn;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpClientTest {
    public static void main(String[] args) throws ClientProtocolException, IOException {
        //1、创建一个httpClient
        HttpClient httpClient = new DefaultHttpClient();
        //2、构造一个请求
        HttpGet httpGet = new HttpGet("http://www.ujiuye.com");
        //3、发送请求，并收到响应
        HttpResponse response = httpClient.execute(httpGet);
        //4、获取响应的数据
        HttpEntity entity = response.getEntity();
        //5、拿到响应的字符串数据
        String string = EntityUtils.toString(entity);
        System.out.println(string);
    }
}
