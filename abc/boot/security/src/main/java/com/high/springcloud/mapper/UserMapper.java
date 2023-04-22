package com.high.springcloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.high.springcloud.domain.User;

import java.util.List;

/**
* @author high
* @description 针对表【sys_user】的数据库操作Mapper
* @createDate 2022-12-23 13:20:53
* @Entity com.high.springboot.domain.User
*/
public interface UserMapper extends BaseMapper<User> {

    List<String> selectUserPerCodeByUserId(Integer userid);

    List<String> selectUserRoleNameByUserId(Integer userid);
}




