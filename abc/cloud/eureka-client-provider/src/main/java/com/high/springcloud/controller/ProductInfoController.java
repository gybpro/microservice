package com.high.springcloud.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.high.springcloud.domain.ProductInfo;
import com.high.springcloud.service.ProductInfoService;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author high
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/product")
public class ProductInfoController {
    /*
    注入通用service，一般在控制层使用通用service，在业务层使用通用mapper
    但也有例外，因为通用service可以进行批量增删改查操作，而通用mapper只能
    进行批量查询和删除，无法批量增加或修改，当在业务层需要批量增加或修改时，
    则在业务层中可注入通用service
     */
    private final ProductInfoService productInfoService;

    public ProductInfoController(ProductInfoService productInfoService) {
        this.productInfoService = productInfoService;
    }

    @GetMapping("/list")
    public List<ProductInfo> list() {
        return productInfoService.list();
    }

    @GetMapping("/listByName")
    public List<ProductInfo> listByName(String name) {
        /* return productInfoService.list(
                new QueryWrapper<ProductInfo>()
                        .like(
                                // 参数1：执行条件(boolean)
                                StringUtils.hasText(name),
                                // 参数2：表字段(column列)普通条件构造器中只能写原列名
                                "product_name",
                                // 参数3：值
                                name
                        )
        ); */

        return productInfoService.list(
                new LambdaQueryWrapper<ProductInfo>().like(
                                // 参数1：执行条件(boolean)
                                StringUtils.hasText(name),
                                // 参数2：表字段(column列)lambda条件构造器中可以用lambda表达式映射获取列名
                                ProductInfo::getProductName,
                                // 参数3：值
                                name
                )
        );
    }

    @GetMapping("/one/{id}")
    public ProductInfo one(@PathVariable Integer id) {
        return productInfoService.getById(id);
    }

    @GetMapping("/page")
    /* public Page<ProductInfo> page(@RequestParam long current, @RequestParam long size) {
        return productInfoService.page(new Page<>(current, size));
    } */
    public Page<ProductInfo> page(Page<ProductInfo> page) {
        return productInfoService.page(page);
    }

    @GetMapping("/pageByIds")
    public Page<ProductInfo> pageByIds(@RequestParam long current, @RequestParam long size,
                                       @RequestParam(required = false) List<Integer> ids) {
        return productInfoService.page(
                new Page<>(current, size),
                new LambdaQueryWrapper<ProductInfo>().in(
                        // 执行条件
                        !ObjectUtils.isEmpty(ids),
                        // 表字段/列名
                        ProductInfo::getId,
                        ids
                )
        );
    }

    @PostMapping("/save")
    public Boolean save(@RequestBody ProductInfo productInfo) {
        /* save、update和saveOrUpdate的区别是：
            save、update只能一种操作，
            saveOrUpdate是如果id存在则修改，不存在则新增
         */
        return productInfoService.save(productInfo);
    }

    @PutMapping("/updateById")
    public Boolean updateById(@RequestBody ProductInfo productInfo) {
        return productInfoService.updateById(productInfo);
    }

    @PostMapping("/saveOrUpdateBatch")
    public Boolean saveOrUpdateBatch(@RequestBody List<ProductInfo> productInfoList) {
        return productInfoService.saveOrUpdateBatch(productInfoList);
    }

    @DeleteMapping("/removeById/{id}")
    public Boolean removeById(@PathVariable Integer id) {
        return productInfoService.removeById(id);
    }

    @DeleteMapping("/removeByIds")
    public Boolean removeByIds(@RequestParam List<Integer> ids) {
        return productInfoService.removeByIds(ids);
    }
}
