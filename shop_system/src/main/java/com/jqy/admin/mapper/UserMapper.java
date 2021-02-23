package com.jqy.admin.mapper;

import com.jqy.admin.model.po.User;
import com.jqy.admin.model.vo.UserVo;

import java.util.List;

/**
 * @ClassName UserMapper
 * @Author 阳
 * @Date 2021/2/21 10:33
 * @Version 1.0
 **/

public interface UserMapper {
    Integer getCount(UserVo vo);

    List<User> getData(UserVo vo);

    void add(User user);

    User getDataById(Integer id);

    void update(User user);

    void delete(Integer id);

    User login(String name);
}
