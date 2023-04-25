package com.high.springcloud.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * @author high
 * @version 1.0
 * @since 1.0
 */
@RestController
public class UserController {

    @GetMapping
    // 方法级别的权限校验
    // isAuthenticated()登录后即可访问
    @PreAuthorize("isAuthenticated()")
    public String sayHi() {
        return "Hello World!";
    }

    @GetMapping("/save")
    @PreAuthorize("hasAuthority('sys:save')")
    public String save() {
        return "save";
    }

    @GetMapping("/delete")
    @PreAuthorize("hasAuthority('sys:delete')")
    public String delete() {
        return "delete";
    }

    @GetMapping("/update")
    @PreAuthorize("hasAuthority('sys:update')")
    public String update() {
        return "update";
    }

    @GetMapping("/query")
    @PreAuthorize("hasAuthority('sys:query')")
    public String query() {
        return "query";
    }

    @GetMapping("/export")
    @PreAuthorize("hasAuthority('sys:export')")
    public String export() {
        return "export";
    }

    @GetMapping("/info")
    @PreAuthorize("hasRole('BA')")
    public Collection<? extends GrantedAuthority> info() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }

    @GetMapping("/name")
    @PreAuthorize("hasRole('BA')")
    public String getName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
