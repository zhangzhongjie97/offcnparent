package com.offcn.project.controller;


import com.offcn.dycommons.AppResponse;
import com.offcn.user.util.OssTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "项目基本功能模块（文件上传、项目信息获取等）")
@RequestMapping("/project")
@RestController
public class ProjectInfoController {

    @Autowired
    private OssTemplate ossTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;


    @ApiOperation("文件上传功能")
    @PostMapping("/upload")
    public AppResponse<Map<String, Object>> upload(@RequestParam("file") MultipartFile[] files) throws IOException {
        List<String> list = new ArrayList<String>();
        try {
            if (files != null && files.length > 0) {
                for (MultipartFile file : files) {
                    String url = ossTemplate.upload(file.getInputStream(), file.getOriginalFilename());
                    list.add(url);
                }
            }

            Map map = new HashMap();
            map.put("urls", list);
            return AppResponse.ok(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}