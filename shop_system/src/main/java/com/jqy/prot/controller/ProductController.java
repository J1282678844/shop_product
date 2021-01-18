package com.jqy.prot.controller;

import com.jqy.prot.model.po.Product;
import com.jqy.prot.model.vo.ProductVo;
import com.jqy.prot.service.ProductService;
import com.jqy.result.CommonsReturn;
import com.jqy.result.ReturnCode;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/add")
    public CommonsReturn add(Product product){
        productService.add(product);
        return CommonsReturn.success();
    }

    @GetMapping("/getDataById")
    public CommonsReturn getDataById(Integer id){
        Product product = productService.getDataById(id);
        return CommonsReturn.success(product);
    }

    @PostMapping("/update")
    public CommonsReturn update(Product product){
        if (product.getId()==null){
            return CommonsReturn.error(ReturnCode.PARAM_ERROR);
        }
        productService.update(product);
        return CommonsReturn.success();
    }



}
