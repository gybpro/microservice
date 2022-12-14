package com.high.springcloud.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.high.springcloud.domain.ProductInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author high
 * @version 1.0
 * @since 1.0
 */
// 为Feign提供微服务的名称(确定要调用的微服务)，Feign可以自动去获取微服务的ip+port
@FeignClient("eureka-client-provider")
@RequestMapping("/product")
public interface ProviderProductFeign {
    /*
    以方法签名确定要调用的方法，@XxxMapping(url)指定调用时的请求方式和请求地址
    方法签名包括：方法名、参数、返回值、微服务名称(对象)
    Feign远程调用其实是对RestTemplate远程方法调用的增强，使用时只需要注入Feign对象，调用方法即可
     */
    @GetMapping("/list")
    List<ProductInfo> list();

    @GetMapping("/listByName")
    List<ProductInfo> listByName(@RequestParam("name") String name);

    @GetMapping("/one/{id}")
    ProductInfo one(@PathVariable("id") Integer id);

    @GetMapping("/page")
    Page<ProductInfo> page(@RequestParam("page") Page<ProductInfo> page);

    @GetMapping("/pageByIds")
    Page<ProductInfo> pageByIds(@RequestParam("current") long current,
                                @RequestParam("size") long size,
                                @RequestParam(required = false, name = "ids") List<Integer> ids);

    @PostMapping("/save")
    Boolean save(@RequestBody ProductInfo productInfo);

    @PutMapping("/updateById")
    Boolean updateById(@RequestBody ProductInfo productInfo);

    @PostMapping("/saveOrUpdateBatch")
    Boolean saveOrUpdateBatch(@RequestBody List<ProductInfo> productInfoList);

    @DeleteMapping("/removeById/{id}")
    Boolean removeById(@PathVariable("id") Integer id);

    @DeleteMapping("/removeByIds")
    Boolean removeByIds(@RequestParam("ids") List<Integer> ids);

    // 偏差14个小时
    @GetMapping("/testTime1")
    String testTime(@RequestParam("date") Date date);
    // 只有年月日，没有偏差
    @GetMapping("/testTime2")
    String testTime(@RequestParam("date") LocalDate date);
    // 丢失s
    @GetMapping("/testTime3")
    String testTime(@RequestParam("date") LocalDateTime date);
    // 不建议单独传递时间，如果有，则要用字符串传递
    @GetMapping("/testTime4")
    String testTime(@RequestParam("date") String date);
    @PostMapping("/testTime5")
    String testTime(@RequestBody ProductInfo productInfo);
}
