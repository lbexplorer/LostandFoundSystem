package com.ums.sys.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ums.sys.entity.User;
import com.ums.sys.mapper.UserMapper;
import com.ums.sys.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;



/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liubo
 * @since 2024-06-22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private RedisTemplate redisTemplate;



    @Override
    public Map<String,Object> login(User user) {
        //根据用户名和密码查询
        // 查询数据库
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,user.getUsername());
        wrapper.eq(User::getPassword,user.getPassword());
        User loginUser =this.baseMapper.selectOne(wrapper);
        //结果不为空，则生成token，并将用户信息存入redis
        if(loginUser!=null){
            //暂时用UUID，终极方案使用jwt
            String key="user:"+ UUID.randomUUID();
            //存入redis
            loginUser.setPassword(null);    // 设置密码为空，密码没必要放入
            redisTemplate.opsForValue().set(key, loginUser,30, TimeUnit.MINUTES);   // timeout为登录时间
            // 返回数据
            Map<String,Object> data = new HashMap<>();
            data.put("token",key);
            return data;
        }
        // 结果不为空，生成token，前后端分离，前端无法使用session，可以使用token
        // 并将用户信息存入redis
        return null;
    }

    @Override
    public Map<String, Object> getUserInfo(String token) {
        // 根据token获取用户信息，信息存进了redis中,获取信息
        Object obj=redisTemplate.opsForValue().get(token);
        if(obj!=null){
            User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
            Map<String,Object> data = new HashMap<>();
            data.put("name",loginUser.getUsername());
            data.put("avatar",loginUser.getAvatar());
            //角色
            // 先在xml里写SQL语句id=getRoleNameByUserId，然后去UserMapper里实现接口
            List<String> roleList = this.baseMapper.getRoleNameByUserId(loginUser.getId());
            data.put("roles",roleList);

            return data;

        }
        return null;
    }

    @Override
    public void logout(String token) {
        redisTemplate.delete(token);    // 从redis中删除token
    }


}
