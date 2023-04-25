package com.high.springcloud.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

/**
 * @author high
 * @version 1.0
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
/* ES：
    @Document是ES提供的注解
    indexName:索引名称
    createIndex：启动时是否创建
    shards：分片个数
    replicas：副本个数
    refreshInterval：数据导入到索引里面，最多几秒搜索到
 */
@Document(indexName = "goods", createIndex = true, shards = 3, replicas = 1, refreshInterval = "1s")
public class Goods {

    /**
     * 商品ID
     */
    @Id //默认使用keyword关键字模式，不进行分词
    @Field
    private Integer goodsId;
    /**
     * 商品名称
     * analyzer:导入时使用的分词
     * searchAnalyzer：搜索时使用的分词
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String goodsName;
    /**
     * 商品描述
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String goodsDesc;
    /**
     * 商品价格
     */
    @Field(type = FieldType.Double)
    private Double goodsPrice;
    /**
     * 商品的销量
     */
    @Field(type = FieldType.Long)
    private Long goodsSaleNum;
    /**
     * 商品的卖点
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String goodsBrief;
    /**
     * 商品的状态
     */
    @Field(type = FieldType.Integer)
    private Integer goodsStatus;
    /**
     * 商品的库存
     */
    @Field(type = FieldType.Integer)
    private Integer goodsStock;
    /**
     * 商品的标签
     */
    @Field(type = FieldType.Text)
    private List<String> goodsTags;
    /**
     * 上架的时间
     * 指定时间格式化
     */
    private Date goodsUpTime;

}
