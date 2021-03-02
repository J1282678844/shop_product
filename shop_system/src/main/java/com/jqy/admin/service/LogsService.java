package com.jqy.admin.service;

import com.jqy.admin.model.po.Logs;
import com.jqy.admin.model.vo.LogsVo;

import java.util.Map;

/**
 * @ClassName LogsService
 * @Author 阳
 * @Date 2021/2/28 16:48
 * @Version 1.0
 **/

public interface LogsService {

    void add(Logs logs);

    Map getData(LogsVo vo);
}
