package com.high.springcloud.controller;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

/**
 * @author high
 * @version 1.0
 * @since 1.0
 */
public class RibbonController {
    private final RestTemplate restTemplate;

    // 负载均衡客户端
    private final LoadBalancerClient loadBalancerClient;

    public RibbonController(RestTemplate restTemplate, LoadBalancerClient loadBalancerClient) {
        this.restTemplate = restTemplate;
        this.loadBalancerClient = loadBalancerClient;
    }

    /**
     * Ribbon底层实现原理
     * 1.拦截请求
     * 2.截取主机名称
     * 3.借助Eureka的服务发现获取匹配名称的微服务列表
     * 4.通过负载均衡(Ribbon是用轮询算法)算法获取ip+port
     * 5.reConstructUrl 重新构建URL
     * 6.发起请求
     *
     * @param serviceName
     * @return
     */
    @GetMapping("testRibbon")
    public String testRibbon(String serviceName) {
        // 在@LoadBalance注解加持下，Spring容器中的RestTemplate对象是交给Ribbon管理的
        // 如果要发固定的绝对ip+port路径URL，则不能使用容器中的RestTemplate对象，因为其一定会走Ribbon规定的路线，不能正常发出
        // 此时要自己new一个新的RestTemplate对象
        RestTemplate template = new RestTemplate();
        System.out.println(template.getForObject("http://localhost:8081/provider/testRibbon", String.class));

        // 正常情况下需要ip+port以及路径才可以正常访问
        // Ribbon会自动查找匹配名称的微服务列表，然后自动轮询该列表的服务，获取其ip+port进行字符串拼接出服务url并调用该服务
        // http://serviceName/provider/testRibbon
        return restTemplate.getForObject("http://" + serviceName + "/provider/testRibbon", String.class);
    }


    /*
    轮询算法的实现：
        请求的次数 % 同名称微服务列表的大小(保证不会下标越界，且是一个周期函数)
        int index = i % size
        list.get(index)
        请求次数i必须是全局的，而i++，在高并发下并不安全
        加同步锁，效率太低
        一般在高性能要求下用CAS自旋锁，没有线程等待和唤醒的开销
        CAS自旋锁的优缺点：
            优点：在Java语言层面无锁，性能好(在底层汇编层面有cmpxchg指令锁)
            缺点：会导致短时间内CPU飙升，还有ABA问题
        ABA问题：CAS中有内存值V，预期值A，要修改的值B，当内存值与预期值相等时，
        将预期值A变成要修改的值B，但有时候并不会与预期一致，会出现某一线程将A改为B，
        但又将值改回A，导致预期值不变，其他线程看见预期值与内存值一致又再次修改，
        导致线程不安全问题
        ABA解决方案：使用AtomicStampedReference对象中的版本号，AtomicStampedReference对象
        中维护着对象值和版本号，每次对象值修改时版本号+1，只有当对象值和版本号都满足期望的时候才允许写入
     */
    /**
     * 核心是负载均衡算法(Ribbon默认是ZoneAvoidanceRule区间内亲和/可用性轮询算法)
     * Ribbon中有一个核心负载均衡算法接口IRule，其中有七中种负载均衡算法：
     * 1.RoundRobinRule轮询  2.RandomRule随机
     * 3.WeightedResponseTimeRule权重：先轮询访问，后统计信息，按响应快的权重选择访问服务
     * 4.BestAvailableRule最优可用算法：选择并发连接数最少的服务访问
     * 5.AvailabilityFilteringRule可用性过滤算法
     * 6.RetryRule重试：轮询，失败则重试
     * 7.ZoneAvoidanceRule区域内可用性轮询算法：会剔除不可用区域，然后再判断可用性
     *
     * @param serviceName
     * @return
     */
    @GetMapping("/testRibbonRule")
    public String testRibbonRule(String serviceName) {
        ServiceInstance choose = loadBalancerClient.choose(serviceName);
        return choose.toString();
    }
}
