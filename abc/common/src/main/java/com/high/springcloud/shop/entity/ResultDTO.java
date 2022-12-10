package com.high.springcloud.shop.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author high
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder // 建造者模式构建对象
@Accessors(chain = true) // @Data的额外配置，chain属性是链式调用，默认false
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultDTO<T> implements Serializable {
    private Integer code; // 自定义状态码
    private String message; // 状态信息
    private Boolean success; // 操作成功与否标记
    private T data; // 返回的结果数据
}
