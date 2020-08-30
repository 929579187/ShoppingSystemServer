package com.sy.shopping.listener;

import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import com.sy.shopping.constant.BusinessConstant;
import com.sy.shopping.service.ProductService;
import com.sy.shopping.service.impl.ProductServiceImpl;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebListener;

/**
 * 用于在整个系统启动和关闭的过程中做一些初始化和销毁操作
 */
@WebListener
public class MyServletContextListener implements ServletContextListener {
    private ProductService productService = new ProductServiceImpl();

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            //1.同步商品信息到Redis中，如果后期有新的商品上架，也需要写入Redis中
            System.out.println("同步商品的点击量信息到Redis中 start....");
            boolean syncSuccess = productService.syncProductsClickCountToRedis();
            if (!syncSuccess) {
                throw new RuntimeException("同步商品信息到Redis失败...");
            }
            System.out.println("同步商品的点击量信息到Redis中 finish....");

            //2.定期同步Redis中的点击量到MySQL中
            System.out.println("将Redis中的点击量同步到MySQL start...");
            //通过Cron表达式来指定操作执行的时间节点
            CronUtil.schedule(BusinessConstant.SYNC_REDIS_CLICK_COUNT_CRON_PATTERN, (Task) () -> {
                productService.syncProductsClickCountToMySQL();
                    }
            );
            // 支持秒级别定时任务
            CronUtil.setMatchSecond(true);
            CronUtil.start();
            System.out.println("将Redis中的点击量同步到MySQL end...");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("系统初始化失败...");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("应用程序销毁...");
        System.out.println("将Redis中的点击量同步到MySQL start...");
        productService.syncProductsClickCountToMySQL();
        System.out.println("将Redis中的点击量同步到MySQL end...");
    }
}
