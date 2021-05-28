package com.offcn.project.controller;

import com.alibaba.fastjson.JSON;
import com.offcn.dycommons.AppResponse;
import com.offcn.dycommons.enums.ProjectStatusEnume;
import com.offcn.project.contants.ProjectConstant;
import com.offcn.project.pojo.TReturn;
import com.offcn.project.service.ProjectCreateService;
import com.offcn.project.vo.BaseVo;
import com.offcn.project.vo.ProjectBaseInfoVo;
import com.offcn.project.vo.ProjectRedisStorageVo;
import com.offcn.project.vo.ProjectReturnVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "项目基本功能模块（创建、保存、项目信息获取、文件上传等）")
@Slf4j
@RestController
@RequestMapping("/project")
public class ProjectCreateController {
    @Autowired
    private ProjectCreateService projectCreateService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @ApiOperation("项目发起第1步-阅读同意协议")
    @PostMapping("/init")
    public AppResponse<String> init(BaseVo vo) {
        //前台传入令牌值
        String accessToken = vo.getAccessToken();
        //redis查询id
        String memberId = redisTemplate.opsForValue().get(accessToken);

        if (memberId.isEmpty()) {
            return AppResponse.fail("无权限,请先登录");
        }
        //生成临时项目并保存
        String projectToken = projectCreateService.initCreateProject(Integer.valueOf(memberId));

        return AppResponse.ok(projectToken);

    }

    @ApiOperation("项目发起第2步-保存项目的基本信息")
    @PostMapping("/savebaseInfo")
    public AppResponse savebaseInfo(ProjectBaseInfoVo vo) {
        //1、取得redis中之前存储JSON结构的项目信息
        String token = redisTemplate.opsForValue().get(ProjectConstant.TEMP_PROJECT_PREFIX + vo.getProjectToken());
        //2、转换为redis存储对应的vo
        ProjectRedisStorageVo redisStorageVo = null;
        if (!token.isEmpty()) {
            redisStorageVo = JSON.parseObject(token, ProjectRedisStorageVo.class);
        } else {
            return AppResponse.fail("请先登录");
        }
        //3、将页面收集来的数据，复制到和redis映射的vo中
        BeanUtils.copyProperties(vo, redisStorageVo);
        //4、将这个Vo对象再转换为json字符串
        String jsonObj = JSON.toJSONString(vo);
        //5、重新更新到redis
        redisTemplate.opsForValue().set(ProjectConstant.TEMP_PROJECT_PREFIX + redisStorageVo.getProjectToken(), jsonObj);
        return AppResponse.ok(jsonObj);
    }


    @ApiOperation("项目发起第3步-项目保存项目回报信息")
    @PostMapping("/savereturn")
    public AppResponse<Object> saveReturnInfo(@RequestBody List<ProjectReturnVo> pro) {
        //1、取得redis中之前存储JSON结构的项目信息
        //1.1获取令牌
        ProjectReturnVo projectReturnVo = pro.get(0);
        String jsonObj = redisTemplate.opsForValue().get(ProjectConstant.TEMP_PROJECT_PREFIX + projectReturnVo.getProjectToken());
        //2、转换为redis存储对应的vo
        ProjectRedisStorageVo redisStorageVo = JSON.parseObject(jsonObj, ProjectRedisStorageVo.class);
        //3、将页面收集来的回报数据封装重新放入redis
        List returns = new ArrayList<TReturn>();
        for (ProjectReturnVo returnVo : pro) {
            TReturn tReturn = new TReturn();
            BeanUtils.copyProperties(returnVo, tReturn);
            returns.add(tReturn);
        }
        //4、更新return集合
        redisStorageVo.setProjectReturns(returns);
        String returnStr = JSON.toJSONString(redisStorageVo);

        //5、重新更新到redis
        redisTemplate.opsForValue().set(ProjectConstant.TEMP_PROJECT_PREFIX + projectReturnVo.getProjectToken(), returnStr);
        return AppResponse.ok("ok");
    }

    @ApiOperation("提交订单")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "accessToken",value = "用户的令牌",required = true),
            @ApiImplicitParam(name = "projectToken",value = "临时项目令牌",required = true),
            @ApiImplicitParam(name = "ops",value = "审核状态",required = true)
    })
    @GetMapping("/submit")
    public AppResponse submit(String accessToken, String projectToken, String ops) {
        //1.判断用户是否登录
        String mid = redisTemplate.opsForValue().get(accessToken);

        if (mid.isEmpty()) {
            AppResponse response = new AppResponse();
            response.setMsg("无权限请登录");
            return response;
        }

        //2.获取登录对象
        String redisVostr = redisTemplate.opsForValue().get(ProjectConstant.TEMP_PROJECT_PREFIX + projectToken);

        ProjectRedisStorageVo redisStorageVo = JSON.parseObject(redisVostr, ProjectRedisStorageVo.class);

        //3.判断状态
        if (!ops.isEmpty()) {

            if (ops.equalsIgnoreCase("1")) {

                projectCreateService.saveProjectInfo(ProjectStatusEnume.SUBMIT_AUTH, redisStorageVo);
                return AppResponse.ok(null);
            }
            if (ops.equalsIgnoreCase("0")) {

                projectCreateService.saveProjectInfo(ProjectStatusEnume.DRAFT, redisStorageVo);
                return AppResponse.ok(null);
            }
        }

        return AppResponse.fail(null);
    }

}
