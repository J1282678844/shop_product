package com.jqy.prot.controller;

import com.jqy.prot.model.po.Type;
import com.jqy.prot.service.TypeService;
import com.jqy.result.CommonsReturn;
import com.jqy.result.ReturnCode;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @ClassName TypeController
 * @Author 阳
 * @Date 2021/1/12 18:58
 * @Version 1.0
 **/
@RestController
//@CrossOrigin
@RequestMapping("/api/type")
public class TypeController {

    @Resource
    TypeService typeService;

    @GetMapping("getData")
    public CommonsReturn getData(){
        List<Type> list = typeService.getData();
        return CommonsReturn.success(list);
    }

    @GetMapping("getDataByPid")
    public CommonsReturn getDataByPid(Integer pId){
        Type type = typeService.getDataByPid(pId);
        return CommonsReturn.success(type);
    }

    @PostMapping("add")
    public CommonsReturn add(Type type){
        Integer id = typeService.add(type);
        return CommonsReturn.success(id);
    }

    @PostMapping("update")
    public CommonsReturn update(Type type){
        if (type.getId() == null){
            return CommonsReturn.error(ReturnCode.PARAM_ERROR);
        }
        typeService.update(type);
        return CommonsReturn.success();
    }

}
