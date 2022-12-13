package com.high.springcloud.controller;

import com.high.springcloud.feign.RentCarFeign;
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

    public HystrixRentCarController(RentCarFeign rentCarFeign) {
        this.rentCarFeign = rentCarFeign;
    }

    @GetMapping("/rent")
    public String rent() {
        return rentCarFeign.rent();
    }
}
