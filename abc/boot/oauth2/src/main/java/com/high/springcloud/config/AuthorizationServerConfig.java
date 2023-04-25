package com.high.springcloud.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;

/**
 * 授权服务器，用于颁发token，颁发token的四种方式：
 * 1.验证码授权：authorization_code
 * 2.静默授权
 * 3.密码授权[重点]
 * 4.客户端授权[重点]
 * <p>
 * JWT：Json Web Token，通过web的方式传递token令牌，生成的方式具有一定规则性(密钥加密)
 * 生成的令牌：aaa.bbb.ccc
 *      1.头部：指定编码方式
 *      2.载荷：具体承载的数据
 *      3.签名：根据指定盐值和密匙对数据进行签名，在一定程度上防止被篡改
 *          对称加密(单一密钥)，非对称加密(密钥对，公私钥，私钥颁发token，公钥验证签名)
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    // Redis连接工厂
    private final RedisConnectionFactory redisConnectionFactory;

    // 授权管理器，密码授权需要
    private final AuthenticationManager authenticationManager;

    // 认证服务器jwt转换器
    private final JwtAccessTokenConverter authorizationJwtAccessTokenConverter;

    // 认证服务器token商店
    private final TokenStore authorizationTokenStore;

    @Lazy
    public AuthorizationServerConfig(PasswordEncoder passwordEncoder,
                                     RedisConnectionFactory redisConnectionFactory,
                                     AuthenticationManager authenticationManager,
                                     @Qualifier("authorizationJwtAccessTokenConverter")
                                     JwtAccessTokenConverter authorizationJwtAccessTokenConverter,
                                     @Qualifier("authorizationTokenStore")
                                     TokenStore authorizationTokenStore) {
        this.passwordEncoder = passwordEncoder;
        this.redisConnectionFactory = redisConnectionFactory;
        this.authenticationManager = authenticationManager;
        this.authorizationJwtAccessTokenConverter = authorizationJwtAccessTokenConverter;
        this.authorizationTokenStore = authorizationTokenStore;
    }

    /*
    JWT方式授权：
        {
            "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2ODI0MTQ3ODksInVzZXJfbmFtZSI6IjEiLCJhdXRob3JpdGllcyI6WyJzeXM6dXBkYXRlIiwic3lzOnF1ZXJ5Il0sImp0aSI6IjIzYzY5NTBjLWE1YzItNDI1MC04MjhlLTc1OTY2NDI2ODIxZSIsImNsaWVudF9pZCI6IndlYiIsInNjb3BlIjpbIndlYiJdfQ.twjL1Aeh02-HlN5G_RV0qZYmPL7gd67uq7DmZNHJjGY",
            "token_type": "bearer",
            "expires_in": 7199,
            "scope": "web",
            "jti": "23c6950c-a5c2-4250-828e-75966426821e"
        }
     */
    @Bean
    @Qualifier("authorizationJwtAccessTokenConverter")
    public JwtAccessTokenConverter authorizationJwtAccessTokenConverter() {
        // 生成Jwt的token转换器
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();

        // 通过转换器设置token令牌
        // 设置签名密钥(对称加密)
        // jwtAccessTokenConverter.setSigningKey("high-oauth2");

        // 非对称加密
        // 读取私钥到内存中
        ClassPathResource resource = new ClassPathResource("rsa/sz2207.jks");

        // 创建密钥工厂对象
        KeyStoreKeyFactory factory = new KeyStoreKeyFactory(resource, "sz2207".toCharArray());

        // 获取密钥对
        KeyPair keyPair = factory.getKeyPair("sz2207");

        // 设置签名密钥(私钥)
        jwtAccessTokenConverter.setKeyPair(keyPair);

        return jwtAccessTokenConverter;
    }

    @Bean
    @Qualifier("authorizationTokenStore")
    public TokenStore authorizationTokenStore() {
        // token生成时，存储到Redis中
        // return new RedisTokenStore(redisConnectionFactory);

        // JWT方式生成令牌
        return new JwtTokenStore(authorizationJwtAccessTokenConverter);
    }

    /*
    第三方账号信息配置
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 1.配置验证码授权方式获取token的第三方账号信息
        /*
        a.先通过登录的方式获取验证码，根据验证码获取token令牌
            (Get)http://localhost:9001/oauth/authorize?response_type=code&
            client_id=web&state=high&redirect_uri=https://www.baidu.com
        b.重定向到指定地址中：code码就在重定向访问的地址中
            (Get)https://www.baidu.com/?code=8p1JNQ&state=high
        c.再根据code码获取token令牌
            (Post)http://localhost:9001/oauth/token?grant_type=authorization_code&
            code=8p1JNQ&redirect_uri=https://www.baidu.com
            通过API接口调试工具，选择Authorization，通过Basic Auth方式来进行认证：
            第三方的账号和密码：web web-secret(可自定义设置的账号密码)
                它本质上就是一个请求头：
                Authorization: Basic d2ViOndlYi1zZWNyZXQ= -> web:web-secret
        d.通过Post请求，返回的json数据就包含token令牌
            {
                "access_token": "10eb942f-8440-4c7e-9c42-b8f97b8b398a",
                "token_type": "bearer",
                "expires_in": 7199,
                "scope": "web"
            }
         */
        /* 1.验证码授权
        clients.inMemory()
                // 账号名称
                .withClient("web")
                // 账号密码
                .secret(passwordEncoder.encode("web-secret"))
                // 授权方式
                // 验证码授权
                .authorizedGrantTypes("authorization_code")
                // token的生命周期
                .accessTokenValiditySeconds(7200)
                // 作用域
                .scopes("web")
                // 重定向地址，验证码授权方式，通过重定向地址获取token令牌
                .redirectUris("https://www.baidu.com"); */
        // 2.配置静默授权方式获取token的第三方账号信息
        /*
        a.先登录，然后通过浏览器进行授权操作
            (Get)http://localhost:9001/oauth/authorize?response_type=token&
            client_id=web&state=high&redirect_uri=https://www.baidu.com
        b.授权完成后，重定向的地址中，就会包含token令牌
            (Get)https://www.baidu.com/#access_token=a046b176-afa6-4363-ab0c-
            9e36e38593e1&token_type=bearer&state=high&expires_in=7199&scope=web
         */
        /* 2.静默授权
        clients.inMemory()
                // 账号名称
                .withClient("web")
                // 账号密码
                .secret(passwordEncoder.encode("web-secret"))
                // 授权方式
                // 静默授权
                .authorizedGrantTypes("implicit")
                // token的生命周期
                .accessTokenValiditySeconds(7200)
                // 作用域
                .scopes("web")
                // 重定向地址，通过重定向地址获取token令牌
                .redirectUris("https://www.baidu.com"); */
        // 3.配置密码授权方式获取token的第三方账号信息[重点]
        /*
        a.直接通过post请求，security登录，用户密码正确直接授权，直接获取token令牌
            (POST)http://localhost:9001/oauth/token
            传递参数grant_type, username, password
            通过API接口调试工具，选择Authorization，通过Basic Auth方式来进行认证：
            第三方的账号和密码：web web-secret(可自定义设置的账号密码)
                它本质上就是一个请求头：
                Authorization: Basic d2ViOndlYi1zZWNyZXQ= -> web:web-secret
        b.授权成功，返回的json数据就包含token令牌
            {
                "access_token": "21caad60-fd82-403d-8dbb-be57506bc74a",
                "token_type": "bearer",
                "expires_in": 7199,
                "scope": "web"
            }
         */
        clients.inMemory()
                // 账号名称
                .withClient("web")
                // 账号密码
                .secret(passwordEncoder.encode("web-secret"))
                // 授权方式
                // 密码授权
                .authorizedGrantTypes("password")
                // token的生命周期
                .accessTokenValiditySeconds(7200)
                // 作用域
                .scopes("web")
        // 4.配置客户端授权方式获取token的第三方账号信息[重点]
        /*
        客户端授权已经不是给一个用户授权了，而是给整个应用授权
        a.直接post请求，通过第三方用户密码获得token令牌
            (POST)http://localhost:9001/oauth/token?grant_type=client_credentials
        b.授权成功，返回的json数据就包含token令牌
            {
                "access_token": "fce16023-5105-4c16-833d-e46bb3cdb6dd",
                "token_type": "bearer",
                "expires_in": 7199,
                "scope": "client"
            }
         */
                .and()
                // 账号名称
                .withClient("client")
                // 账号密码
                .secret(passwordEncoder.encode("client-secret"))
                // 授权方式
                // 密码授权
                .authorizedGrantTypes("client_credentials")
                // token的生命周期
                .accessTokenValiditySeconds(7200)
                // 作用域
                .scopes("client");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // tokenStore()：指定token的存放位置和生成方式
        endpoints.tokenStore(authorizationTokenStore)
                // 密码授权方式，所需的认证管理器对象
                // 没有添加则报错：UnsupportedGrantTypeException,
                // Unsupported grant type: password 不支持密码授权方式
                .authenticationManager(authenticationManager)
                .accessTokenConverter(authorizationJwtAccessTokenConverter);
    }
}
