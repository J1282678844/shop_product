package com.jqy.prot.service;

import com.jqy.prot.model.po.Product;
import com.jqy.prot.model.po.ProductAttrData;
import com.jqy.prot.model.vo.ProductVo;
import com.jqy.result.ReturnCode;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ProductService
 * @Author 阳
 * @Date 2021/1/18 20:45
 * @Version 1.0
 **/

public interface ProductService {
    Map getData(ProductVo vo);

    void add(Product product, String attr, String sku);

    Product getDataById(Integer id);

    void update(Product product);

    void delete(Product product);

    List<ProductAttrData> queryProdectAttrByPid(Integer pid);

    Map querySKUckvalues(Integer pid);
}
