package com.high.springcloud.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.high.springcloud.domain.ProductInfo;
import com.high.springcloud.feign.ProviderProductFeign;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * @author high
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/p")
public class FeignProductController {
    /*
    Feign的使用是编写接口，注入接口，但是实际运行中一定是需要有对象的
    那么feign肯定是使用了动态代理实现代理对象的，实际上也是如此，
    feign底层是采用了JDK动态代理技术实现的
     */
    private final ProviderProductFeign providerProductFeign;

    private final RestTemplate restTemplate;

    public FeignProductController(ProviderProductFeign providerProductFeign, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.providerProductFeign = providerProductFeign;
        // 手写简易Feign，实现无参数get请求
        /* this.providerProductFeign = (ProviderProductFeign) Proxy.newProxyInstance(
                // 类加载器：JDK动态代理要求实现类与接口为同一类加载器加载
                ProviderProductFeign.class.getClassLoader(),
                // 接口集(实现的所有接口)
                new Class[]{ProviderProductFeign.class},
                // 回调方法(调用事件句柄)
                (proxy, method, args) -> {
                    GetMapping getMapping = method.getAnnotation(GetMapping.class);
                    String[] paths = getMapping.value();
                    String path = paths[0];

                    Class<?> clazz = method.getDeclaringClass();
                    FeignClient feignClient = clazz.getAnnotation(FeignClient.class);
                    String serviceName = feignClient.value();
                    RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
                    String[] prePaths = requestMapping.value();
                    String prePath = prePaths[0];

                    Class<?> returnType = method.getReturnType();

                    // 无参数get方法实现
                    String url = "http://" + serviceName + prePath + path ;
                    return restTemplate.getForObject(url, returnType);
                }
        ); */
    }

    @GetMapping("/list")
    public List<ProductInfo> list() {
        return providerProductFeign.list();
    }

    @GetMapping("/listByName")
    public List<ProductInfo> listByName(String name) {
        return providerProductFeign.listByName(name);
    }

    @GetMapping("/one/{id}")
    public ProductInfo one(@PathVariable Integer id) {
        return providerProductFeign.one(id);
    }

    @GetMapping("/page")
    public Page<ProductInfo> page(Page<ProductInfo> page) {
        return providerProductFeign.page(page);
    }

    @GetMapping("/pageByIds")
    public Page<ProductInfo> pageByIds(@RequestParam long current, @RequestParam long size,
                                @RequestParam(required = false) List<Integer> ids) {
        return providerProductFeign.pageByIds(current, size, ids);
    }

    @PostMapping("/save")
    public Boolean save(@RequestBody ProductInfo productInfo) {
        return providerProductFeign.save(productInfo);
    }

    @PutMapping("/updateById")
    public Boolean updateById(@RequestBody ProductInfo productInfo) {
        return providerProductFeign.updateById(productInfo);
    }

    @PostMapping("/saveOrUpdateBatch")
    public Boolean saveOrUpdateBatch(@RequestBody List<ProductInfo> productInfoList) {
        return providerProductFeign.saveOrUpdateBatch(productInfoList);
    }

    @DeleteMapping("/removeById/{id}")
    public Boolean removeById(@PathVariable Integer id) {
        return providerProductFeign.removeById(id);
    }

    @DeleteMapping("/removeByIds")
    public Boolean removeByIds(@RequestParam List<Integer> ids) {
        return providerProductFeign.removeByIds(ids);
    }

    /*
    Mon Dec 12 20:32:36 CST 2022
    Tue Dec 13 10:32:36 CST 2022  +- 14个小时
    1.不建议单独传递时间参数
    2.转成字符串传递(没有偏差)
    3.jdk8新日期类型 LocalDate(只有年月日，没有偏差) LocalDateTime(会丢失s)
    4.包含在对象中传递()
     */
    @GetMapping("/testTime")
    public String testTime() {
        // 获取日期
        Date date = new Date();
        // Date -> LocalDateTime
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        // LocalDateTime -> LocalDate
        LocalDate localDate = localDateTime.toLocalDate();
        // 直接传递Date日期，会偏差14个小时
        String useDate = providerProductFeign.testTime(date);
        // 传递LocalDate，没有偏差，但只有年月日
        String useLocalDate = providerProductFeign.testTime(localDate);
        // 传递LocalDateTime，会丢失s
        String useLocalDateTime = providerProductFeign.testTime(localDateTime);
        // 使用字符串没有偏差
        String useStr = providerProductFeign.testTime(date.toString());
        // 使用对象
        ProductInfo productInfo = new ProductInfo();
        productInfo.setReleaseTime(date);
        String useObj = providerProductFeign.testTime(productInfo);
        return "原始日期：date=" + date +
                " 使用Date传递：useDate=" + useDate +
                " 使用LocalDate传递：useLocalDate=" + useLocalDate +
                " 使用LocalDateTime传递：useLocalDateTime=" + useLocalDateTime +
                " 使用字符串传递：useStr=" + useStr +
                " 使用对象传递：useObj=" + useObj;
    }
}
