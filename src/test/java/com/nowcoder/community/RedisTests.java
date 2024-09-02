package com.nowcoder.community;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class RedisTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testStrings() {
        String redisKey = "test:count";

        redisTemplate.opsForValue().set(redisKey, 1);

        System.out.println(redisTemplate.opsForValue().get(redisKey));
        System.out.println(redisTemplate.opsForValue().increment(redisKey));
        System.out.println(redisTemplate.opsForValue().decrement(redisKey));
    }

    @Test
    public void testHashes() {
        String redisKey = "test:user";

        redisTemplate.opsForHash().put(redisKey, "id", 1);
        redisTemplate.opsForHash().put(redisKey, "username", "zhangsan");

        System.out.println(redisTemplate.opsForHash().get(redisKey, "id"));
        System.out.println(redisTemplate.opsForHash().get(redisKey, "username"));
    }

    @Test
    public void testLists() {
        // 列表相关操作
        /**
         *
         * LPUSH key value [value ...]：将一个或多个元素插入到列表的左端（头部）。
         * RPUSH key value [value ...]：将一个或多个元素插入到列表的右端（尾部）。
         * LPOP key：移除并返回列表左端（头部）的第一个元素。
         * RPOP key：移除并返回列表右端（尾部）的第一个元素。
         * LINDEX key index：获取列表中指定索引位置的元素。
         * LRANGE key start stop：获取列表在指定范围内的元素。索引是从 0 开始的，负数索引表示从列表的末尾开始计数。
         *
         */

        String redisKey = "test:ids";

        redisTemplate.opsForList().leftPush(redisKey, 101);
        redisTemplate.opsForList().leftPush(redisKey, 102);
        redisTemplate.opsForList().leftPush(redisKey, 103);

        System.out.println(redisTemplate.opsForList().size(redisKey));
        System.out.println(redisTemplate.opsForList().index(redisKey, 0));
        System.out.println(redisTemplate.opsForList().range(redisKey, 0, 2));


        System.out.println(redisTemplate.opsForList().leftPop(redisKey));
        System.out.println(redisTemplate.opsForList().leftPop(redisKey));
        System.out.println(redisTemplate.opsForList().leftPop(redisKey));
    }

    @Test
    public void testSets() {
        // sets 是无序的, 并且值是唯一的
        /**
         * SADD key member [member ...]：向集合中添加一个或多个成员。
         * SREM key member [member ...]：移除集合中的一个或多个成员。
         * SMEMBERS key：返回集合中的所有成员。
         * SISMEMBER key member：判断指定的成员是否在集合中，返回 1 表示存在，0 表示不存在。
         * SPOP key [count]：随机移除集合中的一个或多个成员，并返回被移除的成员。
         * SRANDMEMBER key [count]：随机返回集合中的一个或多个成员，但不移除这些成员。
         * SCARD key：返回集合中的成员数量。
         */


        String redisKey = "test:teachers";

        redisTemplate.opsForSet().add(redisKey, "刘备", "关羽", "张飞", "赵云", "诸葛亮");

        System.out.println(redisTemplate.opsForSet().size(redisKey));
        System.out.println(redisTemplate.opsForSet().pop(redisKey));
        System.out.println(redisTemplate.opsForSet().members(redisKey));
    }

    @Test
    public void testSortedSets() {

        // zsets: 有序集合, ZSet 中的每个元素不仅是唯一的，还关联了一个称为“分数”（score）的值，Redis 根据这个分数对集合中的元素进行排序。
        /**
         * ZADD key score member [score member ...]：将一个或多个成员及其分数添加到有序集合中。如果成员已存在，则更新其分数。
         * ZRANGE key start stop [WITHSCORES]：返回指定范围内的成员（按分数从小到大排列），WITHSCORES 选项可以同时返回成员和分数。
         * ZREVRANGE key start stop [WITHSCORES]：返回指定范围内的成员（按分数从大到小排列）。
         * ZRANK key member：返回指定成员在有序集合中的排名（从小到大），排名从 0 开始。
         * ZREVRANK key member：返回指定成员在有序集合中的倒序排名（从大到小）。
         * ZREM key member [member ...]：移除一个或多个成员。
         * ZSCORE key member：返回指定成员的分数。
         * ZCARD key：返回有序集合中的成员数量。
         * ZCOUNT key min max：返回指定分数范围内的成员数量。
         * ZRANGEBYSCORE key min max [WITHSCORES] [LIMIT offset count]：按分数范围返回成员，支持返回分数和分页。
         * ZREMRANGEBYSCORE key min max：移除指定分数范围内的所有成员。
         */
        String redisKey = "test:students";

        redisTemplate.opsForZSet().add(redisKey, "唐僧", 80);
        redisTemplate.opsForZSet().add(redisKey, "悟空", 90);
        redisTemplate.opsForZSet().add(redisKey, "八戒", 50);
        redisTemplate.opsForZSet().add(redisKey, "沙僧", 70);
        redisTemplate.opsForZSet().add(redisKey, "白龙马", 60);

        // 返回有序集合中的成员数量
        System.out.println(redisTemplate.opsForZSet().zCard(redisKey));
        // 返回指定成员的分数
        System.out.println(redisTemplate.opsForZSet().score(redisKey, "八戒"));
        // reverseRank: 返回指定成员在有序集合中的倒序排名(从0开始)
        System.out.println(redisTemplate.opsForZSet().reverseRank(redisKey, "八戒"));
        // reverseRange： 返回指定范围内的成员（按分数从大到小排列）。
        System.out.println(redisTemplate.opsForZSet().reverseRange(redisKey, 0, 2));
    }

    @Test
    public void testKeys() {
        redisTemplate.delete("test:user");
        // 判断是否有这个key
        System.out.println(redisTemplate.hasKey("test:user"));

        redisTemplate.expire("test:students", 10, TimeUnit.SECONDS);
    }

    // 多次访问同一个key
    @Test
    public void testBoundOperations() {
        String redisKey = "test:count";
        // 先将这个key进行操作绑定
        BoundValueOperations operations = redisTemplate.boundValueOps(redisKey);
        operations.increment();
        operations.increment();
        operations.increment();
        operations.increment();
        operations.increment();
        System.out.println(operations.get());
    }

    // 编程式事务
    @Test
    public void testTransactional() {
        Object obj = redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String redisKey = "test:tx";
                // operations.multi()：启动一个 Redis 事务。在调用 multi() 之后，所有的 Redis 操作都会被放入一个事务队列中，直到 exec() 被调用时，这些操作才会被执行。
                operations.multi();

                operations.opsForSet().add(redisKey, "zhangsan");
                operations.opsForSet().add(redisKey, "lisi");
                operations.opsForSet().add(redisKey, "wangwu");

                System.out.println(operations.opsForSet().members(redisKey));

                // operations.exec()：执行事务队列中的所有操作，并提交事务。事务中的所有命令要么全部执行，要么全部失败。
                return operations.exec();
            }
        });
        System.out.println(obj);
    }

}

