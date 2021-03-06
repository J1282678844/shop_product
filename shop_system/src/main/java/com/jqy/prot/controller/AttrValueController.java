package com.jqy.prot.controller;

import com.jqy.aop.LogsAnnotation;
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
//@CrossOrigin
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

    @GetMapping("/getDataByAid")
    public CommonsReturn getDataByAttrId(Integer attrId){
        return CommonsReturn.success(valueService.getDataByAttrId(attrId));
    }

    @LogsAnnotation("商品属性值新增")
    @PostMapping("/add")
    public CommonsReturn add(AttrValue value){
        valueService.add(value);
        return CommonsReturn.success();
    }

    @GetMapping("getDataById")
    public CommonsReturn getDataById(Integer id){
        AttrValue value = valueService.getDataById(id);
        return CommonsReturn.success(value);
    }

    @LogsAnnotation("商品属性值修改")
    @PostMapping("update")
    public CommonsReturn update(AttrValue value){
        if (value.getId() == null){
            return CommonsReturn.error(ReturnCode.PARAM_ERROR);
        }
        valueService.update(value);
        return CommonsReturn.success();
    }

    @LogsAnnotation("商品属性值删除")
    @PostMapping("delete")
    public CommonsReturn delete(AttrValue value){
        valueService.delete(value);
        return CommonsReturn.success();
    }

}
