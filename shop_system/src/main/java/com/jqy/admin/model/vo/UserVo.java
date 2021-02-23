package com.jqy.admin.model.vo;

import com.jqy.admin.model.po.User;

/**
 * @ClassName UserVo
 * @Author 阳
 * @Date 2021/2/21 10:37
 * @Version 1.0
 **/

public class UserVo extends User {
    private Integer current;

    private Integer size;

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
