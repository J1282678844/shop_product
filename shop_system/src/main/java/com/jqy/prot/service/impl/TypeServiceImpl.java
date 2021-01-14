package com.jqy.prot.service.impl;

import com.jqy.prot.mapper.TypeMapper;
import com.jqy.prot.model.po.Type;
import com.jqy.prot.service.TypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName TypeServiceImpl
 * @Author 阳
 * @Date 2021/1/12 19:01
 * @Version 1.0
 **/
@Service
public class TypeServiceImpl implements TypeService {

    @Resource
    TypeMapper typeMapper;

    @Override
    public List<Type> getData() {
        List<Type> list = typeMapper.getData();
        return list;
    }

    @Override
    public Type getDataByPid(Integer pId) {
        return typeMapper.getDataByPid(pId);
    }

    @Override
    public Integer add(Type type) {
        Integer id = typeMapper.add(type);
        return id;
    }

    @Override
    public void update(Type type) {
        typeMapper.update(type);
    }
}
