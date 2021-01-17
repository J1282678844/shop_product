package com.jqy.prot.mapper;

import com.jqy.prot.model.po.AttrValue;
import com.jqy.prot.model.vo.AttrValueVo;

import java.util.List;

/**
 * @ClassName AttrValueMapper
 * @Author 阳
 * @Date 2021/1/17 21:29
 * @Version 1.0
 **/

public interface AttrValueMapper {
    Integer getCount(AttrValueVo vo);

    List<AttrValue> getData(AttrValueVo vo);

    void add(AttrValue value);

    AttrValue getDataById(Integer id);
}
