package com.high.springcloud.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.high.springcloud.domain.ProductInfo;
import com.high.springcloud.mapper.ProductInfoMapper;
import com.high.springcloud.service.ProductInfoService;
import org.springframework.stereotype.Service;

/**
* @author high
* @description 针对表【b_product_info(产品信息表)】的数据库操作Service实现
* @createDate 2022-12-10 17:35:02
*/
@Service
public class ProductInfoServiceImpl extends ServiceImpl<ProductInfoMapper, ProductInfo>
    implements ProductInfoService{

}




