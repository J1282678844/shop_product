package com.jqy.prot.mapper;

import com.jqy.prot.model.po.ProductAttrData;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @ClassName ProductAttrDataMapper
 * @Author 阳
 * @Date 2021/1/21 12:15
 * @Version 1.0
 **/

public interface ProductAttrDataMapper {

    void adds(List<ProductAttrData> paList);

    List<ProductAttrData> queryProdectAttrByPid(Integer pid);

    @Select("select * from t_product_attrdata where proId=#{pid}")
    List<ProductAttrData> queryDataByProId(Integer pid);
}
