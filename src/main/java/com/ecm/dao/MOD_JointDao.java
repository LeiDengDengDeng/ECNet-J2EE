package com.ecm.dao;

import com.ecm.model.MODPK;
import com.ecm.model.MOD_Joint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MOD_JointDao extends JpaRepository<MOD_Joint, Integer> {

    public MOD_Joint save(MOD_Joint joint);

    public void deleteById(int id);

    public void deleteByIdAndCaseID(int id,int cid);

    public void deleteAllByCaseID(int cid);

//    public void deleteById(int id);

    public void deleteAllByFactIDAndCaseID(int factID,int cid);

    public List<MOD_Joint> findAllByCaseID(int cid);

    public List<MOD_Joint> findAllByFactIDAndCaseID(int factID,int cid);

    public MOD_Joint findByIdAndCaseID(int id,int cid);

    public MOD_Joint findById(int id);
}
