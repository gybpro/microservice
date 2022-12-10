package com.high.springcloud.controller;

import com.high.springcloud.shop.entity.ResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author high
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@RestController
@RequestMapping("/provider")
public class ProviderController {
    @Value("${server.port}")
    private Integer port;

    /*
    提供者的重点在于如何接收参数：
        1.键值对参数数据(地址栏请求体均可)：@RequestParam
        2.地址栏占位符传值参数：@PathVariables
        3.请求体中的json数据：@RequestBody
     */
    @GetMapping("/01/{id}")
    public ResultDTO<Map<String, Object>> provider01(@PathVariable Integer id,
                                                     @RequestParam String username,
                                                     @RequestParam String password) {
        return packingData(id, username, password);
    }

    @PostMapping("/02/{id}")
    public ResultDTO<Map<String, Object>> provider02(@PathVariable Integer id,
                                                     @RequestParam String username,
                                                     @RequestParam String password,
                                                     @RequestBody Map<String, Object> user) {
        return packingDataWithBody(id, username, password, user);
    }

    @PutMapping("/03/{id}")
    public ResultDTO<Map<String, Object>> provider03(@PathVariable Integer id,
                                                     @RequestParam String username,
                                                     @RequestParam String password,
                                                     @RequestBody Map<String, Object> user) {
        return packingDataWithBody(id, username, password, user);
    }

    @DeleteMapping("/04/{id}")
    public ResultDTO<Map<String, Object>> provider04(@PathVariable Integer id,
                                                     @RequestParam String username,
                                                     @RequestParam String password) {
        return packingData(id, username, password);
    }

    @GetMapping("/testRibbon")
    public String testRibbon() {
        return "hello ribbon";
    }

    private ResultDTO<Map<String, Object>> packingData(Integer id, String username, String password) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("id", id);
        resultMap.put("username", username);
        resultMap.put("password", password);
        resultMap.put("port", port);

        return new ResultDTO<Map<String, Object>>()
                .setCode(200)
                .setMessage("请求成功")
                .setSuccess(true)
                .setData(resultMap);
    }

    private ResultDTO<Map<String, Object>> packingDataWithBody(Integer id, String username, String password,
                                                               Map<String, Object> user) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("id", id);
        resultMap.put("username", username);
        resultMap.put("password", password);
        resultMap.put("user", user);
        resultMap.put("port", port);

        return new ResultDTO<Map<String, Object>>()
                .setCode(200)
                .setMessage("请求成功")
                .setSuccess(true)
                .setData(resultMap);
    }
}
