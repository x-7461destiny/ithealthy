package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import com.itheima.utils.DateUtils;
import org.aspectj.bridge.Message;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 体检预约服务
 */
@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;

    /**
     * 用分布式锁解决并发预约问题
     * @param map
     * @return
     * @throws Exception
     */
    public Result order(Map map)  throws Exception{
//  检查用户所选择的日期是否进行了预约设置，没有则无法预约
        String orderDate = (String) map.get("orderDate");
        OrderSetting orderSetting =  orderSettingDao.findByOrderDate(DateUtils.parseString2Date(orderDate));
        if (orderSetting == null) {
            //指定日期没有进行预约设置，无法完成预约
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //检查预约日期人数是否已满
        int number = orderSetting.getNumber();//可预约人数
        int reservations = orderSetting.getReservations();//已预约人数
        if (reservations >= number){
            //预约满
            return new Result(false, MessageConstant.ORDER_FULL);
        } else {

        }
        //检查用户是否重复预约
        String telephone = (String) map.get("telephone");
        Member member = memberDao.findByTelephone(telephone);
        if (member != null) {
            //判断是否重复预约
            Integer memberId = member.getId();//会员id
            Date order_Date = DateUtils.parseString2Date(orderDate);//预约日期
            String setmealId = (String) map.get("setmealId");//套餐id
            Order order = new Order(memberId,order_Date,Integer.parseInt(setmealId));
            //根据条件查询
            List<Order> list = orderDao.findByCondition(order);
            if (list != null && list.size() > 0) {
                //用户重复预约
                return  new Result(false,MessageConstant.HAS_ORDERED);
            }
        } else {
            //检查用户是否为会员，没有则自动注册
            member = new Member();
            member.setName((String)map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String)map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);//完成会员注册
        }
        //预约成功更新人数
        Order order = new Order();
        order.setMemberId(member.getId());//设置会员id
        order.setOrderDate(DateUtils.parseString2Date(orderDate));
        order.setOrderType((String) map.get("orderType"));
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        order.setSetmealId(Integer.parseInt((String) map.get("setmealId")));
        orderDao.add(order);

        orderSetting.setReservations(orderSetting.getReservations()+1);//设置已预约人数加一
        orderSettingDao.editReservationsByOrderDate(orderSetting);
        return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());
    }

    //根据预约id查询预约相关信息（体检人姓名，预约日期，套餐名称，预约类型）
    public Map findById(Integer id) throws Exception{
        Map map = orderDao.findById4Detail(id);
        if (map != null) {
            //处理日期格式
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate",DateUtils.parseDate2String(orderDate));
        }
        return map;
    }
}
