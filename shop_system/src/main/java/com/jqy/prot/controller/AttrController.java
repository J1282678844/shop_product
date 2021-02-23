package com.jqy.prot.controller;

import com.jqy.prot.model.po.Attr;
import com.jqy.prot.model.po.Brand;
import com.jqy.prot.model.vo.AttrVo;
import com.jqy.prot.service.AttrService;
import com.jqy.result.CommonsReturn;
import com.jqy.result.ReturnCode;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @ClassName AttrController
 * @Author 阳
 * @Date 2021/1/14 21:18
 * @Version 1.0
 **/
@RestController
//@CrossOrigin
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

    @PostMapping("/add")
    public CommonsReturn add(Attr attr){
        attrService.add(attr);
        return CommonsReturn.success();
    }

    @GetMapping("getDataById")
    public CommonsReturn getDataById(Integer id){
        Attr attr = attrService.getDataById(id);
        return CommonsReturn.success(attr);
    }

    @GetMapping("/getDataByTypeId")
    public CommonsReturn getDataByTypeId(Integer typeId){
        List<Attr> list = attrService.getDataByTypeId(typeId);
        return CommonsReturn.success(list);
    }

    @PostMapping("update")
    public CommonsReturn update(Attr attr){
        if (attr.getId() == null){
            return CommonsReturn.error(ReturnCode.PARAM_ERROR);
        }
        attrService.update(attr);
        return CommonsReturn.success();
    }

    @PostMapping("delete")
    public CommonsReturn delete(Attr attr){
        attrService.delete(attr);
        return CommonsReturn.success();
    }

    //根据TypeId查询  属性
    @GetMapping("/queryAttrByTypeId")
    public CommonsReturn queryAttrByTypeId(Integer typeId){
        return CommonsReturn.success(attrService.queryAttrByTypeId(typeId));
    }

}
