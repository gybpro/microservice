package com.high.springcloud.controller;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author high
 * @version 1.0
 * @since 1.0
 */
@RestController
public class DiscoveryController {
    private final DiscoveryClient discoveryClient;

    public DiscoveryController(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @GetMapping("/doDiscovery")
    public String doDiscovery(String serviceName) {
        // 服务发现，通过服务的应用名称找到服务的具体信息
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);

        instances.forEach(System.out::println);

        // 获取服务ip和port
        ServiceInstance serviceInstance = instances.get(0);
        String ip = serviceInstance.getHost();
        int port = serviceInstance.getPort();
        System.out.println(ip + ":" + port);

        return instances.get(0).toString();
    }
}
