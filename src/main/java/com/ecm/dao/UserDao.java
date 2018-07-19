package com.ecm.dao;

import com.ecm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserDao extends JpaRepository<User, String> {

    @Query(value = "select u.password from User u where u.name = ?1")
    public String getPassword(String name);

    public User findUserByName(String name);

    public User findUserByRealName(String realName);

    @Query(value = "select u from User u where u.id<>?1 and u.name = ?2")
    public User findOtherUserByName(String id,String name);

    public User save(User user);

    @Modifying
    @Query(value = "update User u set u.name=?2,u.realName=?3,u.role=?4 where u.id=?1")
    public void updateUserInfoById(String id,String name,String realName,String role);
}
