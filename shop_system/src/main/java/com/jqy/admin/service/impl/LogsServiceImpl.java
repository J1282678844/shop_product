package com.jqy.admin.service.impl;

import com.jqy.admin.model.po.Logs;
import com.jqy.admin.model.vo.LogsVo;
import com.jqy.admin.service.LogsService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName LogsServiceImpl
 * @Author 阳
 * @Date 2021/2/28 16:48
 * @Version 1.0
 **/
@Service
public class LogsServiceImpl implements LogsService {

    @Resource
    MongoTemplate mongoTemplate;

    @Override
    public void add(Logs logs) {
        logs.setCreateTime(new Date());
        mongoTemplate.insert(logs);
    }

    @Override
    public Map getData(LogsVo vo) {
        Map map = new HashMap();
        //构建查询对象
        Query query = new Query();
        //设置查询条件
        if (vo.getRealName() != null && vo.getRealName() != ""){
            query.addCriteria(Criteria.where("realName").is(vo.getRealName()));
        }
        //查询总条数
        long count = mongoTemplate.count(query, Logs.class);
        //构建分页对象
        PageRequest page = PageRequest.of(vo.getCurrent()-1, vo.getSize());
        //设置分页信息
        query.with(page);
        /*-------查询数据--------*/
        List<Logs> logs = mongoTemplate.find(query, Logs.class);
        map.put("count",count);
        map.put("data",logs);
        return map;
    }
}
