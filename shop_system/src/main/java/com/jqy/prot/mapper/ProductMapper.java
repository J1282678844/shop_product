package com.jqy.prot.mapper;

import com.jqy.prot.model.po.Product;
import com.jqy.prot.model.vo.ProductVo;

import java.util.List;

/**
 * @ClassName ProductMapper
 * @Author 阳
 * @Date 2021/1/18 20:46
 * @Version 1.0
 **/

public interface ProductMapper {
    Integer getCount(ProductVo vo);

    List<Product> getData(ProductVo vo);
}
