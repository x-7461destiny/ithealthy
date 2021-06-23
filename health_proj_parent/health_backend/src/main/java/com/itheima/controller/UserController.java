package com.itheima.controller;


import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户操作
 */
@RestController
@RequestMapping("/user")


public class UserController {
    //获取当前登录的用户的用户名
    @GetMapping("/getUsername")
    public Result getUsername() {
//        当spring security完成认证后，会将当前信息保存到框架提供的上下文对象
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user != null) {
            String username = user.getUsername();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS);
        }
        return new Result(false,MessageConstant.GET_USERNAME_FAIL);
    }
}
