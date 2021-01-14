package com.jqy.prot.model.vo;

import com.jqy.prot.model.po.Attr;

/**
 * @ClassName AttrVo
 * @Author 阳
 * @Date 2021/1/13 21:55
 * @Version 1.0
 **/

public class AttrVo extends Attr {

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
