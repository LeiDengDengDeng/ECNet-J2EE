package com.ecm.service.impl;

import com.ecm.dao.UserDao;
import com.ecm.model.User;
import com.ecm.service.UserManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserManageServiceImpl implements UserManageService {

    @Autowired
    private UserDao userDao;

    @Override
    public String getPassword(String name) {
        return userDao.getPassword(name);
    }

    @Override
    public User getUserByName(String name) {
        return userDao.findUserByName(name);
    }

    @Override
    public User getUserByRealName(String realName) {
        return userDao.findUserByRealName(realName);
    }

    @Override
    @Transactional
    public User saveUserInfo(User user) {
        return userDao.save(user);
    }

    @Override
    public String getUserIdByName(String name) {
        return userDao.findUserByName(name).getId();
    }

    @Override
    public boolean isNameExisted(String id,String name) {
        return (userDao.findOtherUserByName(id,name)!=null);
    }

    @Override
    @Transactional
    public void updateUserInfoById(String id, String name, String realName, String role) {
        userDao.updateUserInfoById(id, name, realName, role);
    }
}
