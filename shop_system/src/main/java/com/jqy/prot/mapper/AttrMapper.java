package com.jqy.prot.mapper;

import com.jqy.prot.model.po.Attr;
import com.jqy.prot.model.vo.AttrVo;

import java.util.List;

/**
 * @ClassName AttrMapper
 * @Author 阳
 * @Date 2021/1/14 21:19
 * @Version 1.0
 **/

public interface AttrMapper {
    Integer getCount(AttrVo vo);

    List<Attr> getData(AttrVo vo);

    void add(Attr attr);

    Attr getDataById(Integer id);
}
