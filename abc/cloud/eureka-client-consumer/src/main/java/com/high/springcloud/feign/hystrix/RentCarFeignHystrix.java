package com.high.springcloud.feign.hystrix;

import com.high.springcloud.feign.RentCarFeign;
import org.springframework.stereotype.Component;

/**
 * @author high
 * @version 1.0
 * @since 1.0
 */
@Component // 交给IoC容器管理
public class RentCarFeignHystrix implements RentCarFeign {
    // 备选方案
    @Override
    public String rent() {
        return "我是备胎";
    }
}
