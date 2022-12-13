package com.high.springcloud.myhystrix.aspect;

import com.high.springcloud.myhystrix.model.Fuse;
import com.high.springcloud.myhystrix.model.FuseStatus;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 熔断切面
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
@Component
@Aspect
public class FuseAspect {
    // 因为一个消费者可以调用多个服务提供者，而每个提供者状态不同，
    // 所以每个提供者都要有对应的断路器，在消费者中要有一个存放断路器的容器
    private static final Map<String, Fuse> fuseMap = new HashMap<>();

    // 可以在运行时扫描读取服务名称，这里简单实现，就不作扫描，直接存入
    static {
        fuseMap.put("eureka-client-provider", new Fuse());
    }

    // 半开状态下用随机数，在低概率下随机次数尝试访问
    private final Random random = new Random();

    // 判断请求是否成功，计数并适时改变熔断器状态
    @Around("@annotation(com.high.springcloud.myhystrix.annotation.FusePointcut)")
    public Object HystrixAround(ProceedingJoinPoint joinPoint) {
        // 获取当前提供者的断路器，可以动态读取，这里简单实现
        Fuse fuse = fuseMap.get("eureka-client-provider");
        FuseStatus status = fuse.getStatus();
        switch (status) {
            case CLOSE:
                // 正常调用，执行默认方法
                try {
                    return joinPoint.proceed();
                } catch (Throwable e) {
                    // e.printStackTrace();
                    fuse.addFailCount();
                    return "我是备胎";
                }
            case OPEN:
                return "我是备胎";
            case HALF_OPEN:
                // 生成0-4的随机整数
                int i = random.nextInt(5);
                System.out.println(i);
                // 使用大约百分之20的流量去尝试访问
                if (i == 1) {
                    try {
                        Object result = joinPoint.proceed();
                        fuse.setStatus(FuseStatus.CLOSE);
                        return result;
                    } catch (Throwable e) {
                        // e.printStackTrace();
                        return "我是备胎";
                    }
                }
            default:
                return "我是备胎";
        }
    }
}
