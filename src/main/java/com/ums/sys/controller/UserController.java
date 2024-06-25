package com.ums.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mysql.cj.util.StringUtils;
import com.ums.common.vo.Result;
import com.ums.sys.entity.User;
import com.ums.sys.service.IUserService;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mysql.cj.util.StringUtils.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liubo
 * @since 2024-06-22
 */
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping("/all")
    public Result<List<User>> getAllUsers() {
        List<User> list=userService.list();
        return Result.success(list,"查询成功");
    }

    @PostMapping("/login")
    public Result<Map<String,Object>> login(@RequestBody User user){
        // 因为 user传过来为json字符串，所以用@RequestBody 进行实体转换

        // 业务代码在userService里完成
        Map<String,Object> data = userService.login(user);

        if(data != null){
            return Result.success(data,"登录成功");
        }
        return Result.fail(20002,"用户名或密码错误");
    }

    @GetMapping("/info")
    public Result<Map<String,Object>> getUserInfo(@RequestParam("token") String token){
        // @RequestParam("token") 是从url中获取值
        // 根据token获取用户信息，信息存进了redis中
        Map<String,Object> data = userService.getUserInfo(token);
        if(data != null){
            return Result.success(data);
        }
        return Result.fail(2003,"登录信息无效，请重新登录");
    }

    @PostMapping("/logout")
    public Result<?> logout(@RequestHeader("X-Token") String token){
        userService.logout(token);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<Map<String,Object>> getUserList(@RequestParam(value = "username",required = false) String username,
                                                  @RequestParam(value = "phone",required = false) String phone,
                                                  @RequestParam(value = "pageNo") Long pageNo,
                                                  @RequestParam(value = "pageSize") Long pageSize
    ){

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();

        // 根据传入的 username 和 phone 是否为空来构建查询条件
        if (username != null && !username.isEmpty()) {
            wrapper.eq(User::getUsername, username);
        }
        if (phone != null && !phone.isEmpty()) {
            wrapper.eq(User::getPhone, phone);
        }

        // 苞米豆 里的Page包，不是Spring的，传入当前页数和每页大小
        Page<User> page = new Page<>(pageNo, pageSize);

        // 分页查找，用 .page()方法
        userService.page(page,wrapper);

        Map<String,Object> data = new HashMap<>();
        data.put("total",page.getTotal());
        data.put("rows",page.getRecords());

        return Result.success(data);
    }

}
