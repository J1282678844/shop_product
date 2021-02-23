package com.jqy.prot.service.impl;

import com.jqy.prot.mapper.AttrValueMapper;
import com.jqy.prot.model.po.AttrValue;
import com.jqy.prot.model.vo.AttrValueVo;
import com.jqy.prot.service.AttrValueService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName AttrValueServiceImpl
 * @Author 阳
 * @Date 2021/1/17 21:29
 * @Version 1.0
 **/
@Service
public class AttrValueServiceImpl implements AttrValueService {

    @Resource
    AttrValueMapper valueMapper;


    @Override
    public Map getData(AttrValueVo vo) {
        Map map = new HashMap();
        Integer count = valueMapper.getCount(vo);
        map.put("count",count);
        List<AttrValue> list = valueMapper.getData(vo);
        map.put("list",list);
        return map;
    }

    @Override
    public List<AttrValue> getDataByAttrId(Integer attrId) {
        return valueMapper.getDataByAttrId(attrId);
    }

    @Override
    public void add(AttrValue value) {
        value.setAuthor("jqyang");
        valueMapper.add(value);
    }

    @Override
    public AttrValue getDataById(Integer id) {
        return valueMapper.getDataById(id);
    }

    @Override
    public void update(AttrValue value) {
        value.setAuthor("jqyang");
        valueMapper.update(value);
    }

    @Override
    public void delete(AttrValue value) {
        valueMapper.delete(value);
    }
}
