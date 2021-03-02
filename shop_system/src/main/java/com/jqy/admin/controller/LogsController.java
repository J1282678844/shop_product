package com.jqy.admin.controller;

import com.jqy.admin.model.po.Logs;
import com.jqy.admin.model.vo.LogsVo;
import com.jqy.admin.service.LogsService;
import com.jqy.result.CommonsReturn;
import com.jqy.result.ReturnCode;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @ClassName LogsController
 * @Author 阳
 * @Date 2021/2/28 20:15
 * @Version 1.0
 **/
@RestController
@RequestMapping("/admin/logs")
public class LogsController {

    @Resource
    LogsService logsService;

    @RequestMapping("/getData")
    public CommonsReturn getData(LogsVo vo){
        if (vo.getCurrent() == null || vo.getSize() == null){
            return CommonsReturn.error(ReturnCode.PARAM_ERROR);
        }
        Map map = logsService.getData(vo);
        return CommonsReturn.success(map);
    }

}
