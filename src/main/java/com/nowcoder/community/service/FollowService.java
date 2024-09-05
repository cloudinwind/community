package com.nowcoder.community.service;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

@Service
public class FollowService implements CommunityConstant {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    // 关注动作 进行处理
    public void follow(int userId, int entityType, int entityId){
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {

                String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
                String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);

                redisOperations.multi();
                // 当前用户关注的这个实体类型的 集合中 新增关于当前实体类型的id
                redisOperations.opsForZSet().add(followeeKey, entityId, System.currentTimeMillis());
                // 当前这个实体新增一个关注者
                redisOperations.opsForZSet().add(followerKey, userId, System.currentTimeMillis());

                return redisOperations.exec();
            }
        });
    }

    // 取消关注动作 进行处理
    public void unfollow(int userId, int entityType, int entityId){
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {

                String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
                String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);

                redisOperations.multi();
                // 当前用户取消对这个实体的关注，则当前用户关注的这个实体类型的 集合中 删除关于当前实体类型的id
                redisOperations.opsForZSet().remove(followeeKey, entityId, System.currentTimeMillis());
                // 当前这个实体删除一个关注者
                redisOperations.opsForZSet().remove(followerKey, userId, System.currentTimeMillis());

                return redisOperations.exec();
            }
        });
    }

    // 查询 某个用户关注的实体的数量
    public long findFolloweeCount(int userId, int entityType){
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);

        return redisTemplate.opsForZSet().zCard(followeeKey);
    }

    // 返回 某个实体 的粉丝数(被多少人关注)
    public long findFollowerCount(int entityType, int entityId){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);

        return redisTemplate.opsForZSet().zCard(followerKey);
    }

    // 查询当前用户是否已经关注该 实体
    public boolean hasFollowed(int userId, int entityType, int entityId){
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return redisTemplate.opsForZSet().score(followeeKey, entityId) != null;
    }


}
