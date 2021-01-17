package com.jqy.prot.service;

import com.jqy.prot.model.po.Brand;
import com.jqy.prot.model.vo.BrandVo;

import java.util.Map;

/**
 * @ClassName BrandService
 * @Author 阳
 * @Date 2021/1/13 21:39
 * @Version 1.0
 **/

public interface BrandService {
    Map getData(BrandVo vo);

    Brand getDataById(Integer id);

    void add(Brand brand);

    void update(Brand brand);

    void delete(Brand brand);
}
