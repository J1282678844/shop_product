package com.jqy.prot.controller;

import com.jqy.prot.model.po.Brand;
import com.jqy.prot.model.po.Type;
import com.jqy.prot.model.vo.BrandVo;
import com.jqy.prot.service.BrandService;
import com.jqy.result.CommonsReturn;
import com.jqy.result.ReturnCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName BrandController
 * @Author 阳
 * @Date 2021/1/13 21:38
 * @Version 1.0
 **/
@RestController
@CrossOrigin
@RequestMapping("/api/brand")
public class BrandController {

    @Resource
    BrandService brandService;

    @GetMapping("/getData")
    public CommonsReturn getData(BrandVo vo){
        if (vo.getCurrent() == null || vo.getSize() == null){
            return CommonsReturn.error(ReturnCode.PARAM_ERROR);
        }
        Map list = brandService.getData(vo);
        return CommonsReturn.success(list);
    }

    @GetMapping("getDataById")
    public CommonsReturn getDataById(Integer id){
        Brand brand = brandService.getDataById(id);
        return CommonsReturn.success(brand);
    }

    @PostMapping("add")
    public CommonsReturn add(Brand brand){
        brandService.add(brand);
        return CommonsReturn.success();
    }
/*
*
*
*
*
*
* */
    @PostMapping("update")
    public CommonsReturn update(Brand brand){
        if (brand.getId() == null){
            return CommonsReturn.error(ReturnCode.PARAM_ERROR);
        }
        brandService.update(brand);
        return CommonsReturn.success();
    }

    @RequestMapping("imgAdd")
    public String imgAdd(MultipartFile file, HttpServletRequest request) throws IOException {
        String realPath = request.getServletContext().getRealPath("/images");
        File f=new File(realPath);
        if(f.exists()==false){
            f.mkdir();
        }
        UUID uuid = UUID.randomUUID();
        String originalFilename = file.getOriginalFilename();
        int i = originalFilename.lastIndexOf(".");
        String substring = originalFilename.substring(i);
        String imgUrl=uuid+substring;
        File imgFile=new File(realPath+"/"+imgUrl);
        file.transferTo(imgFile);
        String imgPath="http://192.168.1.119:8080/images"+"/"+imgUrl;
        return imgPath;
    }

}
