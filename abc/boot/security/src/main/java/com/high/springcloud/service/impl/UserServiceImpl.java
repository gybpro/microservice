package com.high.springcloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.high.springcloud.domain.User;
import com.high.springcloud.mapper.UserMapper;
import com.high.springcloud.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author high
 * @description 针对表【sys_user】的数据库操作Service实现
 * @createDate 2022-12-23 13:20:53
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService, UserDetailsService {

    private UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 认证方法，用户登录后会传递用户名进来
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询用户信息
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(StringUtils.hasText(username), User::getUsername, username)
        );

        // 判断用户是否存在
        if (!ObjectUtils.isEmpty(user)) {
            // 根据角色，查询权限信息
            if ("wangwu".equals(username)) {
                // 查询角色
                List<String> roleNameList = userMapper.selectUserRoleNameByUserId(user.getUserid());

                // 完成授权操作
                user.setAuths(roleNameList);
            } else {
                // 根据用户id，查询权限信息
                List<String> auths = userMapper.selectUserPerCodeByUserId(user.getUserid());

                // 完成授权操作
                user.setAuths(auths);
            }
            // 返回用户对象，权限检验操作交给SpringSecurity完成
            return user;
        }
        // 返回null，表示授权异常
        return null;
    }

}




