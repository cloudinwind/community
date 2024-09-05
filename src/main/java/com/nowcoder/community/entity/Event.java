package com.nowcoder.community.entity;

import java.util.HashMap;
import java.util.Map;

public class Event {
    /**
     * 目前的三个事件：
     * 1. 评论后，发布通知
     * 2. 点赞后，发布通知
     * 3. 关注后，发布通知
     */
    // 事件存储的位置(topic)
    private String topic;
    // 触发该事件的用户 当前登陆用户触发, 产生一个消息
    private int userId;
    // 实体类型(帖子、用户) (事件发生在哪个实体上)
    private int entityType;
    // 实体类型的id
    private int entityId;
    // 该实体的作者
    private int entityUserId;
    // 一些其他数据, 便于之后扩展(因为目前只有这三种事件, 之后可能有其他事件, 需要其他的数据,无法预判需要哪些数据, 因此使用Map)
    private Map<String, Object> data = new HashMap<>();

    public String getTopic() {
        return topic;
    }

    public Event setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public Event setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public Event setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public Event setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityUserId() {
        return entityUserId;
    }

    public Event setEntityUserId(int entityUserId) {
        this.entityUserId = entityUserId;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Event setData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

}

