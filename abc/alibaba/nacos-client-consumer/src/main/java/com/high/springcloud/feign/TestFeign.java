package com.high.springcloud.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author high
 * @version 1.0
 * @since 1.0
 */
@FeignClient("provider-service")
public interface TestFeign {
    @GetMapping("/info")
    public String info();
}
