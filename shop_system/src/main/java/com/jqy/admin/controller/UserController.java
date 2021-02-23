package com.jqy.admin.controller;

import com.jqy.admin.model.po.User;
import com.jqy.admin.model.vo.UserVo;
import com.jqy.admin.service.UserService;
import com.jqy.result.CommonsReturn;
import com.jqy.result.ReturnCode;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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

    @PostMapping("/update")
    public CommonsReturn update(User user) {
        if (user.getId() == null) {
            return CommonsReturn.error(ReturnCode.PARAM_ERROR);
        }
        userService.update(user);
        return CommonsReturn.success();
    }

    @DeleteMapping("/delete")
    public CommonsReturn delete(Integer id) {
        userService.delete(id);
        return CommonsReturn.success();
    }

    @PostMapping("/login")
    public Map login(String name,String password){
        Map map = userService.login(name,password);
        return map;
    }

}
