package com.ums.sys.mapper;

import com.ums.sys.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liubo
 * @since 2024-06-22
 */
public interface UserMapper extends BaseMapper<User> {


    List<String> getRoleNameByUserId(Integer userId);
}
