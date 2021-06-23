package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import com.itheima.utils.SMSUtils;

import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")


public class OrderController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private OrderService orderService;
    //在线预约体检
    @PostMapping("/submit")
    public Result submit(@RequestBody Map map) {
        String telephone = (String) map.get("telephone");
        //将用户输入的验证码和redis保存的进行对比
        String validateCodeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        String validateCode = (String) map.get("validateCode");
        if (validateCode != null && validateCodeInRedis != null && validateCode.equals(validateCodeInRedis)) {
            //比对成功，调用服务完成预约
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            Result  result= new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
            try {
                result = orderService.order(map);
            }catch (Exception e) {
                e.printStackTrace();
                return result;
            }
            if (result.isFlag()) {
                //预约成功可以为用户发短信
//                try {
//                    //个人用户无法发通知，需要用validate
//                    SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone, (String) map.get("orderDate"));
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
            }
            return  result;
        } else {
            //比对失败，返回结果
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }

    //根据预约id查询预约相关信息
    @PostMapping("/findById")
    public Result findById(Integer id) {
        try {
            Map map =  orderService.findById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e){
            e.printStackTrace();
            return  new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }

}
