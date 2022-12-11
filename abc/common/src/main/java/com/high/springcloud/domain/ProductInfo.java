package com.high.springcloud.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @TableName b_product_info
 */
@TableName(value ="b_product_info")
@Data
public class ProductInfo implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String productName;

    private BigDecimal rate;

    private Integer cycle;

    private Date releaseTime;

    private Integer productType;

    private String productNo;

    private BigDecimal productMoney;

    private BigDecimal leftProductMoney;

    private BigDecimal bidMinLimit;

    private BigDecimal bidMaxLimit;

    private Integer productStatus;

    private Date productFullTime;

    private String productDesc;

    private static final long serialVersionUID = 1L;
}
