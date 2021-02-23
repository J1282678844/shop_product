package com.jqy.prot.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jqy.prot.mapper.ProductAttrDataMapper;
import com.jqy.prot.mapper.ProductMapper;
import com.jqy.prot.model.po.Product;
import com.jqy.prot.model.po.ProductAttrData;
import com.jqy.prot.model.vo.ProductVo;
import com.jqy.prot.service.ProductService;
import com.jqy.result.ReturnCode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

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

    @Resource
    ProductAttrDataMapper productAttrDataMapper;

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
    public void add(Product product,String attr,String sku) {// attr：非SKU   sku：SKU
        //新增商品的基本信息
        product.setAuthor("jqyang");
        productMapper.add(product);
        //新增商品的属性信息
        List<ProductAttrData> paList = new ArrayList<>();
        //将attr的 JSON字符串  转为  JSON数组
        JSONArray arrAttr = JSONObject.parseArray(attr);
        //循环 JSON数组
        for (int i = 0; i < arrAttr.size(); i++) {
            //通过Java反射  获取 属性数据对象
            ProductAttrData proAttr = new ProductAttrData();
            //设置属性对应的 商品ID
            proAttr.setProId(product.getId());
            //添加非SKU数据
            proAttr.setAttrData(arrAttr.get(i).toString());//转为字符串形式
            //放入  list集合
            paList.add(proAttr);
        }

        //将sku的 JSON字符串  转为  JSON数组
        JSONArray arrSKU = JSONObject.parseArray(sku);
        //循环 JSON数组
        for (int i = 0; i < arrSKU.size(); i++) {
            //得到具体的 SKU JSON对象
            JSONObject jsonSKU = (JSONObject) arrSKU.get(i);
            //通过Java反射 获取 属性数据对象
            ProductAttrData proAttr = new ProductAttrData();
            //设置属性对应的 商品ID
            proAttr.setProId(product.getId());
            //设置属性对应的 价格
            proAttr.setPrice(jsonSKU.getDouble("prices"));
            //设置商品对应的 库存
            proAttr.setStocks(jsonSKU.getInteger("stockss"));
            //去除SKU中的重复 价格和库存
            jsonSKU.remove("prices");
            jsonSKU.remove("stockss");
            //添加SKU数据
            proAttr.setAttrData(arrSKU.get(i).toString());//转为字符串形式
            //放入  list集合
            paList.add(proAttr);
        }
        //进行批量 新增
        productAttrDataMapper.adds(paList);
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

    @Override
    public void delete(Product product) {
        productMapper.delete(product);
    }

    @Override
    public List<ProductAttrData> queryProdectAttrByPid(Integer pid) {
        return productAttrDataMapper.queryProdectAttrByPid(pid);
    }

    @Override
    public Map querySKUckvalues(Integer pid) {
        Map data=new HashMap();
        //查询属性数据
        List<ProductAttrData> productAttrDatas = productAttrDataMapper.queryDataByProId(pid);
        //sku的数据  {"memsize":["32g"],"color":['红色','绿色'],"netType":'联通'}
        Map skuData=new HashMap();
        Map attrData=new HashMap();
        Map tableData=new HashMap();
        List <JSONObject> tableList=new ArrayList<>();
//遍历所有数据
        for (int i = 0; i <productAttrDatas.size() ; i++) {
            //得到具体的一个属性数据
            ProductAttrData attrDatas = productAttrDatas.get(i);
            //判断此属性为那种属性
            if(attrDatas.getPrice()!=null){ //是sku属性   这种判断不严谨
                //得到对应数据的json字符串 {"memsize":"32G","pricess":111,"color":"红色","netType":"联通","storcks":111}
                //得到对应数据的json字符串 {"memsize":"32G","pricess":111,"color":"红色","netType":"联通","storcks":111}
                //将字符串转为json对象  引入fastjson工具类
                JSONObject jsonObject = JSONObject.parseObject(attrDatas.getAttrData());
                //遍历jsonobject
                Set<Map.Entry<String, Object>> entries = jsonObject.entrySet();
                for (Map.Entry<String, Object> entry : entries) {
                    String key=entry.getKey();//color
                    //判断skuData中是否有此属性
                    Set values = (Set) skuData.get(key);
                    if(values!=null){
                        values.add(entry.getValue());
                    }else{
                        //创建一个set 集合
                        Set valuesSet =new HashSet();
                        valuesSet.add(entry.getValue());
                        skuData.put(key,valuesSet);
                    }
                }
                //表格属性放入list
                tableList.add(jsonObject);
            }else {
                //得到对应数据的json字符串  {"factory":"苹果尝试"}
                JSONObject obj=JSONObject.parseObject(attrDatas.getAttrData());
                Set<Map.Entry<String, Object>> entries = obj.entrySet();
                for (Map.Entry<String, Object> entry : entries) {
                    attrData.put(entry.getKey(),entry.getValue());
                }
            }
        }
        //将sku 和attr 放入返回值中
        data.put("skudata",skuData);
        data.put("attrdata",attrData);
        data.put("tableData",tableList);
        return data;
    }
}
