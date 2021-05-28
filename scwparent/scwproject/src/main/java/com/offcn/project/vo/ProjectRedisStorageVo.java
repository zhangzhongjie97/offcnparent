package com.offcn.project.vo;

import com.offcn.project.pojo.TReturn;

import java.util.List;

public class ProjectRedisStorageVo {
    //众筹商品结构
    private String projectToken;//项目的临时token
    private Integer memberid;//会员id
    private List<Integer> typeids; //项目的分类id
    private List<Integer> tagids; //项目的标签id
    private String name;//项目名称
    private String remark;//项目简介
    private Integer money;//筹资金额
    private Integer day;//筹资天数
    private String headerImage;//项目头部图片
    private List<String> detailsImage;//项目详情图片
    private List<TReturn> projectReturns;//项目回报
    private Integer count;//回报数量

    public ProjectRedisStorageVo() {
    }

    public ProjectRedisStorageVo(String projectToken, Integer memberid, List<Integer> typeids, List<Integer> tagids, String name, String remark, Integer money, Integer day, String headerImage, List<String> detailsImage, List<TReturn> projectReturns, Integer count) {
        this.projectToken = projectToken;
        this.memberid = memberid;
        this.typeids = typeids;
        this.tagids = tagids;
        this.name = name;
        this.remark = remark;
        this.money = money;
        this.day = day;
        this.headerImage = headerImage;
        this.detailsImage = detailsImage;
        this.projectReturns = projectReturns;
        this.count = count;
    }

    public String getProjectToken() {
        return projectToken;
    }

    public void setProjectToken(String projectToken) {
        this.projectToken = projectToken;
    }

    public Integer getMemberid() {
        return memberid;
    }

    public void setMemberid(Integer memberid) {
        this.memberid = memberid;
    }

    public List<Integer> getTypeids() {
        return typeids;
    }

    public void setTypeids(List<Integer> typeids) {
        this.typeids = typeids;
    }

    public List<Integer> getTagids() {
        return tagids;
    }

    public void setTagids(List<Integer> tagids) {
        this.tagids = tagids;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public String getHeaderImage() {
        return headerImage;
    }

    public void setHeaderImage(String headerImage) {
        this.headerImage = headerImage;
    }

    public List<String> getDetailsImage() {
        return detailsImage;
    }

    public void setDetailsImage(List<String> detailsImage) {
        this.detailsImage = detailsImage;
    }

    public List<TReturn> getProjectReturns() {
        return projectReturns;
    }

    public void setProjectReturns(List<TReturn> projectReturns) {
        this.projectReturns = projectReturns;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
