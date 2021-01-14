package com.jqy.prot.mapper;

import com.jqy.prot.model.po.Type;

import java.util.List;

/**
 * @ClassName TypeMapper
 * @Author 阳
 * @Date 2021/1/12 19:02
 * @Version 1.0
 **/

public interface TypeMapper {
    List<Type> getData();

    Type getDataByPid(Integer pId);

    Integer add(Type type);

    void update(Type type);
}
