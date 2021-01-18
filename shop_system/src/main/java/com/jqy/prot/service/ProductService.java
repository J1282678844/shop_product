package com.jqy.prot.service;

import com.jqy.prot.model.vo.ProductVo;

import java.util.Map;

/**
 * @ClassName ProductService
 * @Author 阳
 * @Date 2021/1/18 20:45
 * @Version 1.0
 **/

public interface ProductService {
    Map getData(ProductVo vo);
}
