package com.high.springcloud.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @TableName sys_user
 */
@TableName(value ="sys_user")
@Data
public class User implements Serializable, UserDetails {
    private Integer userid;

    private String username;

    private String userpwd;

    private String sex;

    private String address;

    private Integer status;

    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    List<String> auths;

    /**
     * 授权操作
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return auths.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return String.valueOf(this.userid);
    }

    @Override
    public String getPassword() {
        return userpwd;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.status == 1;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.status == 1;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.status == 1;
    }

    @Override
    public boolean isEnabled() {
        return this.status == 1;
    }
}
