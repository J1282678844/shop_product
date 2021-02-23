package com.jqy.admin.service;

import com.jqy.admin.model.po.User;
import com.jqy.admin.model.vo.UserVo;

import java.util.Map;

/**
 * @ClassName UserService
 * @Author 阳
 * @Date 2021/2/21 10:30
 * @Version 1.0
 **/

public interface UserService {
    Map getData(UserVo vo);

    void add(User user);

    User getDataById(Integer id);

    void update(User user);

    void delete(Integer id);

    Map login(String name, String password);
}
