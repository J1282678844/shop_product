package com.jqy.prot.controller;

import com.jqy.prot.model.po.Attr;
import com.jqy.prot.model.po.AttrValue;
import com.jqy.prot.model.vo.AttrValueVo;
import com.jqy.prot.model.vo.AttrVo;
import com.jqy.prot.service.AttrValueService;
import com.jqy.result.CommonsReturn;
import com.jqy.result.ReturnCode;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @ClassName AttrValueController
 * @Author 阳
 * @Date 2021/1/17 21:27
 * @Version 1.0
 **/
@RestController
@CrossOrigin
@RequestMapping("/api/attrValue")
public class AttrValueController {

    @Resource
    AttrValueService valueService;

    @GetMapping("/getData")
    public CommonsReturn getData(AttrValueVo vo){
        if (vo.getCurrent() == null || vo.getSize() == null){
            return CommonsReturn.error(ReturnCode.PARAM_ERROR);
        }
        Map list = valueService.getData(vo);
        return CommonsReturn.success(list);
    }

    @PostMapping("/add")
    public CommonsReturn add(AttrValue value){
        valueService.add(value);
        return CommonsReturn.success();
    }

}
