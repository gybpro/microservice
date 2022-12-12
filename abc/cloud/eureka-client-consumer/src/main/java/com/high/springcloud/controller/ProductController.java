package com.high.springcloud.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.high.springcloud.domain.ProductInfo;
import com.high.springcloud.feign.ProviderProductFeign;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author high
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/p")
public class ProductController {
    /*
    Feign的使用是编写接口，注入接口，但是实际运行中一定是需要有对象的
    那么feign肯定是使用了动态代理实现代理对象的，实际上也是如此，
    feign底层是采用了JDK动态代理技术实现的
     */
    private final ProviderProductFeign providerProductFeign;

    public ProductController(ProviderProductFeign providerProductFeign) {
        this.providerProductFeign = providerProductFeign;
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
}
