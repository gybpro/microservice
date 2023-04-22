package com.high.springcloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Security配置类
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
// 是否开启方法级别的权限校验
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;

    public WebSecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 基于内存认证
        auth.inMemoryAuthentication();
        // 基于数据库认证
        auth.userDetailsService(userDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 表单登录
        http.formLogin()
                .and()
                // 鉴权操作，匹配通过，不匹配返回403(403和401都是没有权限访问)
                .authorizeRequests()
                // // 限权资源
                // .antMatchers("/save")
                // // 需要权限
                // .hasAuthority("sys:save")
                // .antMatchers("/delete")
                // .hasAuthority("sys:delete")
                // .antMatchers("/update")
                // .hasAuthority("sys:update")
                // .antMatchers("/query")
                // .hasAuthority("sys:query")
                // .antMatchers("/export")
                // .hasAuthority("sys:export")
                // .antMatchers("/info")
                // // hasRole()验证会自动添加ROLE_前缀，然后再进行验证，
                // // 所以一般数据库的角色信息要有ROLE_前缀
                // .hasRole("BA")
                // 其他资源
                .anyRequest()
                // 认证后即可访问
                .authenticated();
    }
}
