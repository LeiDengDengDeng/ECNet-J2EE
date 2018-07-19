package com.ecm.controller;

import com.ecm.model.User;
import com.ecm.service.UserManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/user")
public class UserController {

    @Autowired
    private UserManageService userService;

    @RequestMapping(value="/getUserInfo")
    public User getUserInfo(@RequestParam("realName") String realName){
        User user = userService.getUserByRealName(realName);
        return user;
    }

    @RequestMapping(value="/updateUserInfo")
    public String updateUserInfo(@RequestParam("id") String id,@RequestParam("name") String name,
                                 @RequestParam("realName") String realName,@RequestParam("role") String role){
        if(userService.isNameExisted(id,name)){
            return "name exists";
        }else{
            userService.updateUserInfoById(id, name, realName, role);
            return "success";
        }
    }
}
