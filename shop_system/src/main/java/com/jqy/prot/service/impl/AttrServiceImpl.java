package com.jqy.prot.service.impl;

import com.jqy.prot.mapper.AttrMapper;
import com.jqy.prot.mapper.AttrValueMapper;
import com.jqy.prot.model.po.Attr;
import com.jqy.prot.model.po.AttrValue;
import com.jqy.prot.model.vo.AttrVo;
import com.jqy.prot.service.AttrService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName AttrServiceImpl
 * @Author 阳
 * @Date 2021/1/14 21:19
 * @Version 1.0
 **/
@Service
public class AttrServiceImpl implements AttrService {

    @Resource
    AttrMapper attrMapper;
    @Resource
    AttrValueMapper attrValueMapper;


    @Override
    public Map getData(AttrVo vo) {
        Map map = new HashMap();
        //查询总条数
        Integer count = attrMapper.getCount(vo);
        map.put("count", count);
        List<Attr> list = attrMapper.getData(vo);
        map.put("list", list);
        return map;
    }

    @Override
    public void add(Attr attr) {
        attr.setAuthor("jqyang");
        attrMapper.add(attr);
    }

    @Override
    public Attr getDataById(Integer id) {
        return attrMapper.getDataById(id);
    }

    @Override
    public void update(Attr attr) {
        attrMapper.update(attr);
    }

    @Override
    public void delete(Attr attr) {
        attrMapper.delete(attr);
    }

    @Override
    public List<Attr> getDataByTypeId(Integer typeId) {
        List<Attr> list = attrMapper.getDataByTypeId(typeId);
        return list;
    }

    @Override
    public Map queryAttrByTypeId(Integer typeId) {
        //声明map集合
        Map map = new HashMap();
        //声明skuDatas 集合
        List<Attr> skuDatas = new ArrayList<>();
        //声明attrDatas 集合
        List<Attr> attrDatas = new ArrayList<>();
        //查询所有属性数据
        List<Attr> attrs = attrMapper.getDataByTypeId(typeId);
        //遍历所有属性数据
        for (int i = 0; i < attrs.size(); i++) {
            //得到每个数据
            Attr attr = attrs.get(i);
            //判断是 sku
            if (attr.getIsSKU() == 0) {
                //判断类型不是 input 则没有values
                if (attr.getType() != 3) {
                    //查询属性对应的属性值
                    List<AttrValue> attrValues = attrValueMapper.getDataByAttrId(attr.getId());
                    attr.setValues(attrValues);
                }
                skuDatas.add(attr);
            } else {
                //判断 不是 sku
                //判断类型不是 input 则没有values
                if (attr.getType() != 3) {
                    //查询属性对应的属性值
                    List<AttrValue> attrValues = attrValueMapper.getDataByAttrId(attr.getId());
                    attr.setValues(attrValues);
                }
                attrDatas.add(attr);
            }
        }
        map.put("skuDatas", skuDatas);
        map.put("attrDatas", attrDatas);
        return map;
    }
}
