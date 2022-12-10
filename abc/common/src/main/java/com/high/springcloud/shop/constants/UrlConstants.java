package com.high.springcloud.shop.constants;

public interface UrlConstants {

    // 单机访问
    // String BASE_URL = "http://localhost:8081/";
    // 负载均衡请求访问，将ip和端口替换为微服务名称，Ribbon会自动轮询同一名称的微服务列表
    String BASE_LOAD_BALANCED_URL = "http://EUREKA-CLIENT-PROVIDER/";

    /* String GET_PROVIDER_01 = BASE_URL + "provider/01/{id}?username=zhangsan&password=123";
    String POST_PROVIDER_02 = BASE_URL + "provider/02/{id}?username=lisi&password=321";
    String PUT_PROVIDER_03 = BASE_URL + "provider/03/{id}?username=wangwu&password=111";
    String DELETE_PROVIDER_04 = BASE_URL + "provider/04/{id}?username=zhaoliu&password=222"; */

    String GET_PROVIDER_01 = BASE_LOAD_BALANCED_URL + "provider/01/{id}?username=zhangsan&password=123";
    String POST_PROVIDER_02 = BASE_LOAD_BALANCED_URL + "provider/02/{id}?username=lisi&password=321";
    String PUT_PROVIDER_03 = BASE_LOAD_BALANCED_URL + "provider/03/{id}?username=wangwu&password=111";
    String DELETE_PROVIDER_04 = BASE_LOAD_BALANCED_URL + "provider/04/{id}?username=zhaoliu&password=222";

}
