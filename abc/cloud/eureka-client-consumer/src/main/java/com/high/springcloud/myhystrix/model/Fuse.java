package com.high.springcloud.myhystrix.model;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 熔断器模型
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
public class Fuse {
    // 熔断窗口时间(秒为单位) (所有熔断器的全局配置)
    private static final Integer WINDOW_TIME = 20;

    // 最大失败次数 (所有熔断器的全局配置)
    private static final Integer MAX_FAIL_COUNT = 3;

    // 熔断器状态(默认为闭合) (每个熔断器自身的状态)
    private FuseStatus status = FuseStatus.CLOSE;

    // 访问失败次数 (每个熔断器自身的失败次数)
    // AtomicInteger保证原子性，保证线程安全
    private AtomicInteger currentFailCount = new AtomicInteger(0);

    // 创建一个线程池，使用线程计时，用于重置窗口时间内的失败次数和改变熔断器状态
    private static ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(
            // 核心线程数(最少存在的线程数量)
            4,
            // 最大线程数(最大允许的线程数)
            8,
            // 保活时间(线程无使用状态下保持存活不回收的时间)
            30,
            // 保活时间单位
            TimeUnit.SECONDS,
            // 线程阻塞队列(可排队等待使用线程的访问数量)
            new LinkedBlockingQueue<>(2000),
            // 使用默认线程工厂执行线程
            Executors.defaultThreadFactory(),
            // 使用默认终止策略
            new ThreadPoolExecutor.AbortPolicy()
    );

    // 定时重置窗口时间内的失败次数 (每个熔断器自身的定时重置器)
    {
        poolExecutor.execute(() -> {
            while (true) {
                try {
                    // 定时
                    TimeUnit.SECONDS.sleep(WINDOW_TIME);
                    if (this.status.equals(FuseStatus.CLOSE)) {
                        // 断路器闭合时重置，非闭合状态下无需重置
                        this.currentFailCount.set(0);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // 记录失败次数
    public void addFailCount() {
        if (this.status.equals(FuseStatus.CLOSE)) {
            int i = currentFailCount.incrementAndGet();
            if (i >= MAX_FAIL_COUNT) {
                // 到达阈值，开启熔断器
                this.status = FuseStatus.OPEN;
                // 等待一个窗口时间，让断路器变为半开状态
                poolExecutor.execute(() -> {
                    try {
                        TimeUnit.SECONDS.sleep(WINDOW_TIME);
                        // 重置失败次数，在恢复闭合状态后才能正常进入方法
                        this.currentFailCount.set(0);
                        // 进入半开状态
                        this.status = FuseStatus.HALF_OPEN;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    public FuseStatus getStatus() {
        return status;
    }

    public void setStatus(FuseStatus status) {
        this.status = status;
    }

    public AtomicInteger getCurrentFailCount() {
        return currentFailCount;
    }

    public void setCurrentFailCount(AtomicInteger currentFailCount) {
        this.currentFailCount = currentFailCount;
    }
}
