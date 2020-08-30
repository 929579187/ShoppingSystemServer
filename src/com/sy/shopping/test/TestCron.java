package com.sy.shopping.test;

import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;

import java.io.IOException;

public class TestCron {
    public static void main(String[] args) throws IOException {
        //通过Cron表达式来指定操作执行的时间节点
        CronUtil.schedule("0/3 * * * * ? *", (Task) () -> {
                    System.out.println("Task excuted.");
                }
        );

        // 支持秒级别定时任务
        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }
}
