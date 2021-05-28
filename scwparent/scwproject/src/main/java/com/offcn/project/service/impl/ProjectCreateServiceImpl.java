package com.offcn.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.offcn.dycommons.enums.ProjectStatusEnume;
import com.offcn.project.contants.ProjectConstant;
import com.offcn.project.enums.ProjectImageTypeEnume;
import com.offcn.project.mapper.*;
import com.offcn.project.pojo.*;
import com.offcn.project.service.ProjectCreateService;
import com.offcn.project.vo.ProjectRedisStorageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectCreateServiceImpl implements ProjectCreateService {
    @Autowired
    private TProjectMapper projectMapper;

    @Autowired
    private TProjectImagesMapper projectImagesMapper;

    @Autowired
    private TProjectTagMapper projectTagMapper;

    @Autowired
    private TProjectTypeMapper projectTypeMapper;

    @Autowired
    private TReturnMapper tReturnMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 实现思路：
     * 生成一个令牌UUID用来作用于取这个用户的id，
     *
     * @param memberId
     * @return
     */
    @Override
    public String initCreateProject(Integer memberId) {

        //UUID作为key值存入redis
        String str = UUID.randomUUID().toString().replace("-", "");

        //生成临时储存对象存储id
        ProjectRedisStorageVo redisStorageVo = new ProjectRedisStorageVo();

        redisStorageVo.setMemberid(memberId);

        String jsonStr = JSON.toJSONString(redisStorageVo);

        redisTemplate.opsForValue().set(ProjectConstant.TEMP_PROJECT_PREFIX + str, jsonStr);

        return str;
    }

    @Override
    public void saveProjectInfo(ProjectStatusEnume status, ProjectRedisStorageVo projectvo) {

        //1.存基本信息
        TProject project = new TProject();
        BeanUtils.copyProperties(projectvo, project);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(new Date());
        project.setCreatedate(format);
        project.setStatus(status.getCode() + "");
        projectMapper.insertSelective(project);
        //2.存图片
        Integer id = project.getId();
        String headerImage = projectvo.getHeaderImage();
        TProjectImages headerImages = new TProjectImages(null, id, headerImage, ProjectImageTypeEnume.HEADER.getCode());
        projectImagesMapper.insertSelective(headerImages);

        List<String> detailsImage =
                projectvo.getDetailsImage();

        for (String detailsImag : detailsImage) {
            TProjectImages detailsIma = new TProjectImages(null, id, detailsImag, ProjectImageTypeEnume.DETAILS.getCode());
            projectImagesMapper.insertSelective(detailsIma);
        }
        //3存标签
        List<Integer> tagids = projectvo.getTagids();
        for (Integer tagid : tagids) {
            TProjectTag tag = new TProjectTag(null, id, tagid);
            projectTagMapper.insertSelective(tag);
        }
        //4存类型
        List<Integer> typeids = projectvo.getTypeids();
        for (Integer typeid : typeids) {
            TProjectType type = new TProjectType(null, id, typeid);
            projectTypeMapper.insertSelective(type);
        }

        //5存回报
        List<TReturn> projectReturns = projectvo.getProjectReturns();
        for (TReturn tReturn : projectReturns) {
            tReturn.setProjectid(id);
            tReturnMapper.insert(tReturn);
        }

        //6删除redis
        redisTemplate.delete(ProjectConstant.TEMP_PROJECT_PREFIX + projectvo.getProjectToken());
    }
}
