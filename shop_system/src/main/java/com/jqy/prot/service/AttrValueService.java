package com.jqy.prot.service;

import com.jqy.prot.model.po.AttrValue;
import com.jqy.prot.model.vo.AttrValueVo;

import java.util.List;
import java.util.Map;

/**
 * @ClassName AttrValueService
 * @Author 阳
 * @Date 2021/1/17 21:28
 * @Version 1.0
 **/

public interface AttrValueService {
    Map getData(AttrValueVo vo);

    void add(AttrValue value);

    AttrValue getDataById(Integer id);

    void update(AttrValue value);

    void delete(AttrValue value);

    List<AttrValue> getDataByAttrId(Integer attrId);
}
