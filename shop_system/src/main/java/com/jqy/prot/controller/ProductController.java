package com.jqy.prot.controller;

import com.jqy.prot.model.vo.ProductVo;
import com.jqy.prot.service.ProductService;
import com.jqy.result.CommonsReturn;
import com.jqy.result.ReturnCode;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @ClassName ProductController
 * @Author 阳
 * @Date 2021/1/18 20:45
 * @Version 1.0
 **/
@RestController
@CrossOrigin
@RequestMapping("/api/product")
public class ProductController {

    @Resource
    ProductService productService;

    @GetMapping("/getData")
    public CommonsReturn getData(ProductVo vo){
        if (vo.getCurrent()==null || vo.getSize()==null){
            return CommonsReturn.error(ReturnCode.PARAM_ERROR);
        }
        Map list = productService.getData(vo);
        return CommonsReturn.success(list);
    }

}
