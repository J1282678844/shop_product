package com.jqy.prot.model.vo;

import com.jqy.prot.model.po.AttrValue;

/**
 * @ClassName AttrValueVo
 * @Author 阳
 * @Date 2021/1/17 21:33
 * @Version 1.0
 **/

public class AttrValueVo extends AttrValue {

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
