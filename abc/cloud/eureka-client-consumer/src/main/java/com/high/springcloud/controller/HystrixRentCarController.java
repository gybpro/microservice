package com.high.springcloud.controller;

import com.high.springcloud.feign.FuseTestFeign;
import com.high.springcloud.feign.RentCarFeign;
import com.high.springcloud.myhystrix.annotation.FusePointcut;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author high
 * @version 1.0
 * @since 1.0
 */
@RestController
public class HystrixRentCarController {
    private final RentCarFeign rentCarFeign;

    private final FuseTestFeign fuseTestFeign;

    public HystrixRentCarController(RentCarFeign rentCarFeign, FuseTestFeign fuseTestFeign) {
        this.rentCarFeign = rentCarFeign;
        this.fuseTestFeign = fuseTestFeign;
    }

    @GetMapping("/rent")
    public String rent() {
        return rentCarFeign.rent();
    }

    @GetMapping("/testFuse")
    @FusePointcut
    public String testFuse() {
        return fuseTestFeign.testFuse();
    }
}
