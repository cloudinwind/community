package com.nowcoder.community;

import com.nowcoder.community.util.RedisKeyUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.*;
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

    // 统计20万个重复数据的独立总数 (即统计20万个数据中 不重复的数据的个数)
    @Test
    public void testHyperLogLog() {
        String redisKey = "test:hll:01";

        for (int i = 1; i <= 100000; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey, i);
        }

        for (int i = 1; i <= 100000; i++) {
            int r = (int) (Math.random() * 100000 + 1);
            redisTemplate.opsForHyperLogLog().add(redisKey, r);
        }

        long size = redisTemplate.opsForHyperLogLog().size(redisKey);
        System.out.println(size);
    }

    // 将3组数据合并, 再统计合并后的重复数据的独立总数(即统计不重复的数据的个数)
    @Test
    public void testHyperLogLogUnion() {
        String redisKey2 = "test:hll:02";
        for (int i = 1; i <= 10000; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey2, i);
        }

        String redisKey3 = "test:hll:03";
        for (int i = 5001; i <= 15000; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey3, i);
        }

        String redisKey4 = "test:hll:04";
        for (int i = 10001; i <= 20000; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey4, i);
        }

        String unionKey = "test:hll:union";
        redisTemplate.opsForHyperLogLog().union(unionKey, redisKey2, redisKey3, redisKey4);

        long size = redisTemplate.opsForHyperLogLog().size(unionKey);
        System.out.println(size);
    }

    // 统计一组数据的布尔值
    @Test
    public void testBitMap() {
        String redisKey = "test:bm:01";

        // 记录
        redisTemplate.opsForValue().setBit(redisKey, 1, true);
        redisTemplate.opsForValue().setBit(redisKey, 4, true);
        redisTemplate.opsForValue().setBit(redisKey, 7, true);

        // 查询
        System.out.println(redisTemplate.opsForValue().getBit(redisKey, 0));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey, 1));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey, 2));


        // 统计 存储的内容中为 true (或者1) 的个数
        Object obj = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.bitCount(redisKey.getBytes());
            }
        });

        System.out.println(obj);
    }

    // 统计3组数据的布尔值, 并对这3组数据做OR运算.
    @Test
    public void testBitMapOperation() {
        String redisKey2 = "test:bm:02";
        redisTemplate.opsForValue().setBit(redisKey2, 0, true);
        redisTemplate.opsForValue().setBit(redisKey2, 1, true);
        redisTemplate.opsForValue().setBit(redisKey2, 2, true);

        String redisKey3 = "test:bm:03";
        redisTemplate.opsForValue().setBit(redisKey3, 2, true);
        redisTemplate.opsForValue().setBit(redisKey3, 3, true);
        redisTemplate.opsForValue().setBit(redisKey3, 4, true);

        String redisKey4 = "test:bm:04";
        redisTemplate.opsForValue().setBit(redisKey4, 4, true);
        redisTemplate.opsForValue().setBit(redisKey4, 5, true);
        redisTemplate.opsForValue().setBit(redisKey4, 6, true);

        // 运算结果是存储的 key 为 "test:bm:or"
        String redisKey = "test:bm:or";

        Object obj = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.bitOp(RedisStringCommands.BitOperation.OR,
                        redisKey.getBytes(), redisKey2.getBytes(), redisKey3.getBytes(), redisKey4.getBytes());
                return connection.bitCount(redisKey.getBytes());
            }
        });

        System.out.println(obj);

        System.out.println(redisTemplate.opsForValue().getBit(redisKey, 0));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey, 1));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey, 2));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey, 3));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey, 4));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey, 5));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey, 6));
    }

    @Test
    public void testUV(){
        String redisKey = RedisKeyUtil.getDAUKey("20240911");
        // 统计 存储的内容中为 true (或者1) 的个数
        Object obj = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.bitCount(redisKey.getBytes());
            }
        });

        System.out.println(obj);

        String redisKey2 = RedisKeyUtil.getUVKey("20240911");
        long size = redisTemplate.opsForHyperLogLog().size(redisKey2);
        System.out.println(size);
    }

}

