package com.ecm.service;

import com.ecm.model.User;

public interface UserManageService {

    public String getPassword(String name);

    public User getUserByName(String name);

    public User getUserByRealName(String realName);

    public User saveUserInfo(User user);

    public String getUserIdByName(String name);

    public boolean isNameExisted(String id,String name);

    public void updateUserInfoById(String id,String name,String realName,String role);
}
