package com.itheima.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService{
    @Autowired
    private CheckGroupDao checkGroupDao;


    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
//        新增检查组，使检查组关联检查项
//        操作t_checkgroup表
        checkGroupDao.add(checkGroup);
        //设置检查组和检查项的多对多的关联关系，操作t_checkgroup_checkitem表
        Integer CheckGroupId = checkGroup.getId();
//        if (checkitemIds != null && checkitemIds.length > 0) {
//            for (Integer checkitemId : checkitemIds) {
//                Map<String,Integer> map = new HashMap<>();
//                map.put("checkgroupId",CheckGroupId);
//                map.put("checkitemId",checkitemId);
//                checkGroupDao.setCheckGroupAndCheckItem(map);
//            }
//        }
        this.setCheckGroupAndCheckItem(CheckGroupId,checkitemIds);

    }

    //分页查询, 需要用到mybatis分页助手
    public PageResult PageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckGroup> page = checkGroupDao.findByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }


    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }


    public void edit(CheckGroup checkGroup,Integer[] checkitemIds) {
        checkGroupDao.edit(checkGroup);
        //清理关联关系 根据检查项id 操作表t_checkgroup_checkitem
        checkGroupDao.deleteAssociation(checkGroup.getId());
        Integer checkGroupId = checkGroup.getId();
        //重新建立关联关系
        this.setCheckGroupAndCheckItem(checkGroupId,checkitemIds);

    }


    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }


//    public void delete(Integer id) {
//        checkGroupDao.deleteCheckGroupById(id);
//    }

    //多对多关联 检查项与检查组
    public void setCheckGroupAndCheckItem(Integer checkGroupId, Integer[] checkitemIds) {
        if (checkitemIds != null && checkitemIds.length > 0) {
            for (Integer checkitemId : checkitemIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("checkgroupId", checkGroupId);
                map.put("checkitemId", checkitemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }

    }

}
