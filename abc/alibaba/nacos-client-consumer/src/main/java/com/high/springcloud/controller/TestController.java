package com.high.springcloud.controller;

import com.high.springcloud.feign.TestFeign;
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
public class TestController {
    private final DiscoveryClient discoveryClient;

    private final TestFeign testFeign;

    public TestController(DiscoveryClient discoveryClient, TestFeign testFeign) {
        this.discoveryClient = discoveryClient;
        this.testFeign = testFeign;
    }

    @GetMapping("/test")
    public String test() {
        List<ServiceInstance> instances = discoveryClient.getInstances("provider-service");
        System.out.println(instances);
        return testFeign.info();
    }
}
