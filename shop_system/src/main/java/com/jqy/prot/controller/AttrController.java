package com.jqy.prot.controller;

import com.jqy.prot.model.vo.AttrVo;
import com.jqy.prot.service.AttrService;
import com.jqy.result.CommonsReturn;
import com.jqy.result.ReturnCode;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @ClassName AttrController
 * @Author 阳
 * @Date 2021/1/14 21:18
 * @Version 1.0
 **/
@RestController
@CrossOrigin
@RequestMapping("/api/attr")
public class AttrController {

    @Resource
    AttrService attrService;

    @GetMapping("/getData")
    public CommonsReturn getData(AttrVo vo){
        if (vo.getCurrent() == null || vo.getSize() == null){
            return CommonsReturn.error(ReturnCode.PARAM_ERROR);
        }
        Map list = attrService.getData(vo);
        return CommonsReturn.success(list);
    }

}
