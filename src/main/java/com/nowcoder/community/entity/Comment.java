package com.nowcoder.community.entity;

import java.util.Date;

public class Comment {

    private int id;
    private int userId;
    // 用户给某个实体评论
    // 这个实体可以是： 帖子、评论
    // 即用户可以给帖子评论, 也可以给评论评论, 通过entityType进行区分
    // entityType = 1：帖子
    // entityType = 2: 评论
    private int entityType;
    private int entityId;

    // 进行评论的时候 可能是对帖子评论, 这个时候实体是帖子, 实体类型为1
    // 有的时候是对 回帖(帖子的一级评论进行评论) , 这个时候实体是回帖(评论), 实体类型为2
    // 上面两种情况都不需要targetId
    // 但是有的时候是对二级评论进行评论 这个时候需要显示 XXX回复 YYY：评论内容, 这个YYY其实就是二级评论的作者, 即targetId
    private int targetId;
    private String content;
    private int status;
    private Date createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", userId=" + userId +
                ", entityType=" + entityType +
                ", entityId=" + entityId +
                ", targetId=" + targetId +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                '}';
    }
}
