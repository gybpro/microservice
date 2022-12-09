package com.high.controller;

import com.high.shop.constants.UrlConstants;
import com.high.shop.entity.ResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author high
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@RestController
@RequestMapping("/consumer")
public class ConsumerController {
    /*
    RestTemplate远程调用：同步的http RestFul Api调用
    RestFul是一种以动词的形式对URL资源进行操作的规范原则
        Get请求：获取URL资源
            ResponseEntity<T> getForEntity(String url, Class<T> responseType, Map<String, ?> uriVariables)
            参数1：请求资源URL地址
            参数2：返回值结果集的字节码类型
            参数3：请求地址中的占位符
        Post请求：添加URL资源
            ResponseEntity<T> postForEntity(String url, Object request, Class<T> responseType,
                                            Map<String, ?> uriVariables)
            参数1：请求资源URL地址
            参数2：请求体中封装的数据
            参数3：返回值结果集的字节码类型
            参数4：请求地址中的占位符
        Put请求：修改URL资源
            void put(String url, Object request, Map<String, ?> uriVariables)
        Delete请求：删除URL资源
            void delete(String url, Map<String, ?> uriVariables)

     */
    private final RestTemplate restTemplate;

    public ConsumerController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/01")
    public ResultDTO consumer01() {
        // 封装地址栏占位符传递值的Map集合
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("id", 111);

        ResponseEntity<ResultDTO> entity = restTemplate.getForEntity(
                UrlConstants.GET_PROVIDER_01,
                ResultDTO.class,
                uriVariables
        );

        // 输出ResponseEntity中的属性信息
        log.info("StatusCode: {}, StatusCodeValue: {}", entity.getStatusCode(), entity.getStatusCodeValue());
        log.info("Headers: {}", entity.getHeaders());
        log.info("Body: {}", entity.getBody());

        return entity.getBody();
    }

    @GetMapping("/02")
    public ResultDTO consumer02() {
        // 封装地址栏占位符传递值的Map集合
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("id", 222);

        return restTemplate.getForObject(
                UrlConstants.GET_PROVIDER_01,
                ResultDTO.class,
                uriVariables
        );
    }

    @GetMapping("/03")
    public ResultDTO consumer03() {
        // 封装地址栏占位符传递值的Map集合
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("id", 333);

        // 封装请求体中的数据
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("address","深圳市宝安区航城街道索佳科技园");
        requestBody.put("age",22);

        ResponseEntity<ResultDTO> entity = restTemplate.postForEntity(
                UrlConstants.POST_PROVIDER_02,
                requestBody,
                ResultDTO.class,
                uriVariables
        );

        // 输出ResponseEntity中的属性信息
        log.info("StatusCode: {}, StatusCodeValue: {}", entity.getStatusCode(), entity.getStatusCodeValue());
        log.info("Headers: {}", entity.getHeaders());
        log.info("Body: {}", entity.getBody());

        return entity.getBody();
    }

    @GetMapping("/04")
    public ResultDTO consumer04() {
        // 封装地址栏占位符传递值的Map集合
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("id", 444);

        // 封装请求体中的数据
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("address","深圳市宝安区航城街道索佳科技园");
        requestBody.put("age",33);

        return restTemplate.postForObject(
                UrlConstants.POST_PROVIDER_02,
                requestBody,
                ResultDTO.class,
                uriVariables
        );
    }

    @GetMapping("/05")
    public ResultDTO consumer05() {
        // 封装地址栏占位符传递值的Map集合
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("id", 555);

        // 封装请求体中的数据
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("address","深圳市宝安区航城街道索佳科技园");
        requestBody.put("age",44);

        restTemplate.put(
                UrlConstants.PUT_PROVIDER_03,
                requestBody,
                uriVariables
        );

        return ResultDTO
                .builder()
                .code(200)
                .message("请求成功")
                .success(true)
                .build();
    }

    @GetMapping("/06")
    public ResultDTO consumer06() {
        // 封装地址栏占位符传递值的Map集合
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("id", 666);

        restTemplate.delete(
                UrlConstants.DELETE_PROVIDER_04,
                uriVariables
        );

        return ResultDTO
                .builder()
                .code(200)
                .message("请求成功")
                .success(true)
                .build();
    }
}
