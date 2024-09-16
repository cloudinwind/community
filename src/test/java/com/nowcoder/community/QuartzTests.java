package com.nowcoder.community;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class QuartzTests {

    @Autowired
    private Scheduler scheduler;

    // 删除数据库中初始化的数据, 从而使得之前配置的Quartz定时任务的程序运行的时候不再执行
    @Test
    public void testDeleteJob() throws SchedulerException {
        // name: job的名词
        // group: job的组
        boolean b = scheduler.deleteJob(new JobKey("alphaJob", "alphaJobGroup"));
        System.out.println(b);
    }
}
