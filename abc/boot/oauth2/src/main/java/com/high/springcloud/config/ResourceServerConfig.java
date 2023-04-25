package com.high.springcloud.config;

import com.alibaba.cloud.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 资源管理器
 * 注意：资源服务器要和授权服务器分开，包括服务器中的引用对象
 *      需要创建一个新的tokenStore对象和jwtAccessTokenConverter对象
 *      一旦开启资源服务器，那么访问控制器中的资源必须包含token
 *      请求头：Authorization=Bearer jwt令牌
 *      注意！Bearer和jwt令牌中间有一个空格
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    // 资源服务器jwt转换器
    private final JwtAccessTokenConverter resourceJwtAccessTokenConverter;

    // 资源服务器token商店
    private final TokenStore resourceTokenStore;

    @Lazy
    public ResourceServerConfig(
            @Qualifier("resourceJwtAccessTokenConverter")
            JwtAccessTokenConverter resourceJwtAccessTokenConverter,
            @Qualifier("resourceTokenStore")
            TokenStore resourceTokenStore) {
        this.resourceJwtAccessTokenConverter = resourceJwtAccessTokenConverter;
        this.resourceTokenStore = resourceTokenStore;
    }

    @Bean
    @Primary
    @Qualifier("resourceJwtAccessTokenConverter")
    public JwtAccessTokenConverter resourceJwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();

        // 通过转换器设置token令牌
        // 设置签名验证密钥(对称加密)
        // jwtAccessTokenConverter.setSigningKey("high-oauth2");

        // 非对称加密
        // 读取公钥到内存中
        ClassPathResource resource = new ClassPathResource("rsa/sz2207.txt");

        try {
            // 读取公钥内容为字符串
            String publicKey = FileUtils.readFileToString(resource.getFile(), Charset.defaultCharset());

            // 设置签名验证密钥(公钥)
            jwtAccessTokenConverter.setVerifierKey(publicKey);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jwtAccessTokenConverter;
    }

    @Bean
    @Primary
    @Qualifier("resourceTokenStore")
    public TokenStore resourceTokenStore() {
        return new JwtTokenStore(resourceJwtAccessTokenConverter);
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(resourceTokenStore);
    }
}
