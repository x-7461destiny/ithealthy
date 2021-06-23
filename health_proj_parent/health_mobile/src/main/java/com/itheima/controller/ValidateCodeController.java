package com.itheima.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * 验证码操作
 */

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;
    //发送验证码,体检预约
    @PostMapping("/send4Order")
    public Result send4Order(String telephone) {
        //随机生成4位数字验证码
//        Integer validateCode = ValidateCodeUtils.generateValidateCode(4);
        Integer validateCode = 1234;
        //给用户发送验证码, 由于短信未完成  暂时注释
//        try {
//            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//            return  new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
//        }
        //将验证码保存到redis（5分钟有效）
        jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_ORDER,300,validateCode.toString());

        return  new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    @RequestMapping("/send4Login")
    public Result sen4login(String telephone) {
        //随机生成4位数字验证码
//        Integer validateCode = ValidateCodeUtils.generateValidateCode(6);
        Integer validateCode = 123456;
        //给用户发送验证码
//        try {
//            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//            return  new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
//        }
        //将验证码保存到redis（5分钟有效）
        jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_LOGIN,300,validateCode.toString());

        return  new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
