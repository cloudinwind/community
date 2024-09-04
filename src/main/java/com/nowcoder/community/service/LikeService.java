package com.nowcoder.community.service;

import com.nowcoder.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private RedisTemplate redisTemplate;

    // 点赞 当用户进行点赞, 需要将redis中对应的地方添加这个行为, 因此使用编程式事务
    // 因为可能是给帖子点赞, 可能是给帖子的评论点赞，可能是给评论的评论点赞
    // 因此 entityType：0 对应帖子, 1 对应评论
    // entityUserId: 发布实体的用户id
    public void like(int userId, int entityType, int entityId, int entityUserId) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                // 获取实体的点赞key
                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
                // 获取用户key(即发布实体的用户)
                String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);

                // 判断当前登陆的用户是否已经给 该实体点赞
                boolean isMember = operations.opsForSet().isMember(entityLikeKey, userId);

                // 下面进行事务处理
                operations.multi();
                // 如果已经点赞, 则再次点击相当于三取消点赞
                if (isMember) {
                    operations.opsForSet().remove(entityLikeKey, userId);
                    // 发布该帖子的用户收到的赞减去一
                    operations.opsForValue().decrement(userLikeKey);
                } else {
                    // 如果没有点赞, 则点赞
                    operations.opsForSet().add(entityLikeKey, userId);
                    // 发布该实体的用户得到的赞加1
                    operations.opsForValue().increment(userLikeKey);
                }

                return operations.exec();
            }
        });
    }

    // 查询某实体获得的 点赞数量
    public long findEntityLikeCount(int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    // 查询某人对某实体的点赞状态
    public int findEntityLikeStatus(int userId, int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey, userId) ? 1 : 0;
    }

    // 查询某个用户获得的赞的数量
    public int findUserLikeCount(int userId) {
        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
        Integer count = (Integer) redisTemplate.opsForValue().get(userLikeKey);
        return count == null ? 0 : count.intValue();
    }

}

