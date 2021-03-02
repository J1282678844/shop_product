package com.jqy.admin.controller;

import com.jqy.admin.model.po.User;
import com.jqy.admin.model.vo.UserVo;
import com.jqy.admin.service.UserService;
import com.jqy.aop.LogsAnnotation;
import com.jqy.result.CommonsReturn;
import com.jqy.result.ReturnCode;
import com.jqy.utils.JWT;
import com.jqy.utils.MD5Util;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Base64;
import java.util.Map;

/**
 * @ClassName UserController
 * @Author 阳
 * @Date 2021/2/21 10:27
 * @Version 1.0
 **/
@RestController
//@CrossOrigin
@RequestMapping("/admin/user")
public class UserController {

    @Resource
    UserService userService;

    @GetMapping("/getData")
    public CommonsReturn getData(UserVo vo) {
        if (vo.getCurrent() == null || vo.getSize() == null) {
            return CommonsReturn.error(ReturnCode.PARAM_ERROR);
        }
        Map list = userService.getData(vo);
        return CommonsReturn.success(list);
    }

    @LogsAnnotation("新增用户信息")
    @PostMapping("/add")
    public CommonsReturn add(User user) {
        userService.add(user);
        return CommonsReturn.success();
    }

    @GetMapping("/getDataById")
    public CommonsReturn getDataById(Integer id) {
        User user = userService.getDataById(id);
        return CommonsReturn.success(user);
    }

    @LogsAnnotation("修改用户信息")
    @PostMapping("/update")
    public CommonsReturn update(User user) {
        if (user.getId() == null) {
            return CommonsReturn.error(ReturnCode.PARAM_ERROR);
        }
        userService.update(user);
        return CommonsReturn.success();
    }

    @LogsAnnotation("删除用户信息")
    @DeleteMapping("/delete")
    public CommonsReturn delete(Integer id) {
        userService.delete(id);
        return CommonsReturn.success();
    }


    @RequestMapping("/login")
    public CommonsReturn login(String name, String password){
        User user = userService.login(name);
        if (user == null){
            return CommonsReturn.error(ReturnCode.USERNAME_NOEXIST);
        }
        if (!user.getPassword().equals(MD5Util.getMD5(password))){
            return CommonsReturn.error(ReturnCode.PASSWORD_ERROR);
        }
        //jwt 生成token令牌
        String token = JWT.sign(user, 60 * 30);
        //将token进行base64加密
        Base64.Encoder encoder = Base64.getEncoder();
        //设置加密信息 字节数组
        byte[] userInfo = (name + "jqy" + token).getBytes();
        //加密
        byte[] encode = encoder.encode(userInfo);
        //将字节数组 转为 字符串 返回
        String tokenInfo = new String(encode);
        return CommonsReturn.success(tokenInfo);
    }

}
