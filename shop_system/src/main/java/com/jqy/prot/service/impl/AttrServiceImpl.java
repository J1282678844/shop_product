package com.jqy.prot.service.impl;

import com.jqy.prot.mapper.AttrMapper;
import com.jqy.prot.model.po.Attr;
import com.jqy.prot.model.vo.AttrVo;
import com.jqy.prot.service.AttrService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName AttrServiceImpl
 * @Author 阳
 * @Date 2021/1/14 21:19
 * @Version 1.0
 **/
@Service
public class AttrServiceImpl implements AttrService {

    @Resource
    AttrMapper attrMapper;


    @Override
    public Map getData(AttrVo vo) {
        Map map = new HashMap();
        //查询总条数
        Integer count = attrMapper.getCount(vo);
        map.put("count",count);
        List<Attr> list = attrMapper.getData(vo);
        map.put("list",list);
        return map;
    }

    @Override
    public void add(Attr attr) {
        attr.setAuthor("jqyang");
        attrMapper.add(attr);
    }

    @Override
    public Attr getDataById(Integer id) {
        return attrMapper.getDataById(id);
    }

    @Override
    public void update(Attr attr) {
        attrMapper.update(attr);
    }

    @Override
    public void delete(Attr attr) {
        attrMapper.delete(attr);
    }
}
