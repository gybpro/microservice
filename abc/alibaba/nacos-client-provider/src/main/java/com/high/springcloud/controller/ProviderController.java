package com.high.springcloud.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author high
 * @version 1.0
 * @since 1.0
 */
@RestController
public class ProviderController {
    @GetMapping("/info")
    public String info() {
        return "20k";
    }
}
