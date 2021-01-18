package com.jqy.prot.service.impl;

import com.jqy.prot.mapper.BrandMapper;
import com.jqy.prot.model.po.Brand;
import com.jqy.prot.model.vo.BrandVo;
import com.jqy.prot.service.BrandService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName BrandServiceImpl
 * @Author 阳
 * @Date 2021/1/13 21:40
 * @Version 1.0
 **/
@Service
public class BrandServiceImpl implements BrandService {

    @Resource
    BrandMapper brandMapper;

    @Override
    public Map getData(BrandVo vo) {
        Map map = new HashMap();
        //查询总条数
        Integer count = brandMapper.getCount(vo);
        map.put("count",count);
        List<Brand> list = brandMapper.getData(vo);
        map.put("list",list);
        return map;
    }

    @Override
    public Brand getDataById(Integer id) {
        return brandMapper.getDataById(id);
    }

    @Override
    public void add(Brand brand) {
        brand.setAuthor("jqyang");
        brandMapper.add(brand);
    }

    @Override
    public void update(Brand brand) {
        brandMapper.update(brand);
    }

    @Override
    public void delete(Brand brand) {
        brandMapper.delete(brand);
    }
}
