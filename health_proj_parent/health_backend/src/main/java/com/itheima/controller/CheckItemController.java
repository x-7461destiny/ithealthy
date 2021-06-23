package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 检查项管理
 */

@RestController
@RequestMapping("/checkitem")
public class CheckItemController {

    @Reference
    private CheckItemService checkItemService;

    @RequestMapping("/add")
    public Result add(@RequestBody CheckItem checkItem){
        try{
            checkItemService.add(checkItem);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return  new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }
    //检查项分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = checkItemService.pageQuery(queryPageBean);
        return pageResult;
    }

    @PreAuthorize("hasAnyAuthority('CHECKITEM_DELETE')") // 权限校验，值应对应权限表的关键字
    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try{
            checkItemService.deleteById(id);
        } catch (Exception e) {
            return new Result(false,MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    @PostMapping("/edit")
    public Result edit(@RequestBody CheckItem checkItem) {
        try {
            checkItemService.edit(checkItem);
        } catch (Exception e) {
            return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

    @GetMapping("/findById")
    public Result findById(Integer id) {
        try {
            CheckItem checkItem = checkItemService.findById(id);
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkItem);
        } catch (Exception e) {
            return  new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }

    }

    @GetMapping("/findAll")
    public Result findAll() {
        try {
            List<CheckItem> list = checkItemService.findAll();
            return  new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,list);
        } catch (Exception e) {
            return  new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }



//
//    //检查项分页查询
//    @RequestMapping("/findPage")
//    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
//        PageResult pageResult = checkItemService.pageQuery(queryPageBean);
//        return pageResult;
//    }
//
//    //删除检查项
//    @RequestMapping("/delete")
//    public Result delete(Integer id){
//        try{
//            checkItemService.deleteById(id);
//        }catch (Exception e){
//            e.printStackTrace();
//            //服务调用失败
//            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
//        }
//        return  new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
//    }
//
//    //编辑检查项
//    @RequestMapping("/edit")
//    public Result edit(@RequestBody CheckItem checkItem){
//        try{
//            checkItemService.edit(checkItem);
//        }catch (Exception e){
//            e.printStackTrace();
//            //服务调用失败
//            return new Result(false, MessageConstant.EDIT_CHECKITEM_FAIL);
//        }
//        return  new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
//    }
//
//    @RequestMapping("/findById")
//    public Result findById(Integer id){
//        try{
//            CheckItem checkItem = checkItemService.findById(id);
//            return  new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
//        }catch (Exception e){
//            e.printStackTrace();
//            //服务调用失败
//            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
//        }
//    }
}
