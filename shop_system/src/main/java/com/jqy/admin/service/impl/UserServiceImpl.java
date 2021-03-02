package com.jqy.admin.service.impl;

import com.jqy.admin.mapper.UserMapper;
import com.jqy.admin.model.po.User;
import com.jqy.admin.model.vo.UserVo;
import com.jqy.admin.service.UserService;
import com.jqy.utils.JWT;
import com.jqy.utils.MD5Util;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName UserServiceImpl
 * @Author 阳
 * @Date 2021/2/21 10:32
 * @Version 1.0
 **/
@Service
public class UserServiceImpl implements UserService {

    @Resource
    UserMapper userMapper;

    @Override
    public Map getData(UserVo vo) {
        Map map = new HashMap();
        //查询总条数
        Integer count = userMapper.getCount(vo);
        map.put("count",count);
        List<User> list = userMapper.getData(vo);
        map.put("list",list);
        return map;
    }

    @Override
    public void add(User user) {
        user.setPassword(MD5Util.getMD5("123456"));
        userMapper.add(user);
    }

    @Override
    public User getDataById(Integer id) {
        return userMapper.getDataById(id);
    }

    @Override
    public void update(User user) {
        userMapper.update(user);
    }

    @Override
    public void delete(Integer id) {
        userMapper.delete(id);
    }

    @Override
    public User login(String name) {
        return userMapper.login(name);
    }
}
