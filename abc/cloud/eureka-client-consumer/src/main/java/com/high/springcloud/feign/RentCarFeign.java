package com.high.springcloud.feign;

import com.high.springcloud.feign.hystrix.RentCarFeignHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author high
 * @version 1.0
 * @since 1.0
 */
// 这里需要指定熔断类
@FeignClient(value = "eureka-client-provider", contextId = "rentCar", fallback = RentCarFeignHystrix.class)
public interface RentCarFeign {
    @GetMapping("/rent")
    String rent();
}
