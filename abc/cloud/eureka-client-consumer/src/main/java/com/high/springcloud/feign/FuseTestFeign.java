package com.high.springcloud.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Fuse熔断器测试Feign
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
@FeignClient(value = "eureka-client-provider", contextId = "testFuse")
public interface FuseTestFeign {
    @GetMapping("/rent")
    String testFuse();
}
