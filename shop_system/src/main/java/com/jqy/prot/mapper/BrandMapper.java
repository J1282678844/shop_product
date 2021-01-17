package com.jqy.prot.mapper;

import com.jqy.prot.model.po.Brand;
import com.jqy.prot.model.vo.BrandVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @ClassName BrandMapper
 * @Author 阳
 * @Date 2021/1/13 21:41
 * @Version 1.0
 **/

public interface BrandMapper {

    Integer getCount(BrandVo vo);

    List<Brand> getData(BrandVo vo);

    Brand getDataById(Integer id);

    void add(Brand brand);

    void update(Brand brand);

    void delete(Brand brand);
}
