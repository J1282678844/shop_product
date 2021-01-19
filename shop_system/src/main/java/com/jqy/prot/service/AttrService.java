package com.jqy.prot.service;

import com.jqy.prot.model.po.Attr;
import com.jqy.prot.model.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * @ClassName AttrService
 * @Author 阳
 * @Date 2021/1/14 21:19
 * @Version 1.0
 **/

public interface AttrService {
    Map getData(AttrVo vo);

    void add(Attr attr);

    Attr getDataById(Integer id);

    void update(Attr attr);

    void delete(Attr attr);

    List<Attr> getDataByTypeId(Integer typeId);
}
