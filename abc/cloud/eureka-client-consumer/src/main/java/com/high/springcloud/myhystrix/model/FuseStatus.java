package com.high.springcloud.myhystrix.model;

/**
 * 熔断器状态枚举
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
public enum FuseStatus {
    // 熔断器关闭(保险丝连接/空气开关闭合)
    CLOSE,
    // 熔断器打开(保险丝断开/空气开关跳闸)
    OPEN,
    // 半开状态(打回大部分请求，允许少量请求尝试访问)
    HALF_OPEN
}
