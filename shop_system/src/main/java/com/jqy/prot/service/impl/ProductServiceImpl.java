package com.jqy.prot.service.impl;

import com.jqy.prot.mapper.ProductMapper;
import com.jqy.prot.model.po.Product;
import com.jqy.prot.model.vo.ProductVo;
import com.jqy.prot.service.ProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ProductServiceImpl
 * @Author 阳
 * @Date 2021/1/18 20:46
 * @Version 1.0
 **/
@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    ProductMapper productMapper;

    @Override
    public Map getData(ProductVo vo) {
        Map map = new HashMap();
        Integer count = productMapper.getCount(vo);
        map.put("count",count);
        List<Product> list = productMapper.getData(vo);
        map.put("list",list);
        return map;
    }

    @Override
    public void add(Product product) {
        product.setAuthor("jqyang");
        productMapper.add(product);
    }

    @Override
    public Product getDataById(Integer id) {
        return productMapper.getDataById(id);
    }

    @Override
    public void update(Product product) {
        product.setAuthor("jqyang");
        productMapper.update(product);
    }


}
