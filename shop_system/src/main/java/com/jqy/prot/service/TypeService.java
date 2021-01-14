package com.jqy.prot.service;

import com.jqy.prot.model.po.Type;

import java.util.List;
import java.util.Map;

/**
 * @ClassName TypeService
 * @Author 阳
 * @Date 2021/1/12 19:01
 * @Version 1.0
 **/

public interface TypeService {

    List<Type> getData();

    Type getDataByPid(Integer pId);

    Integer add(Type type);

    void update(Type type);


}
