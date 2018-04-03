package com.ecm.dao;

import com.ecm.model.MODPK;
import com.ecm.model.MOD_Fact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MOD_FactDao extends JpaRepository<MOD_Fact, MODPK> {

    public MOD_Fact save(MOD_Fact fact);

    @Query(value = "select f.logicNodeID from MOD_Fact f where f.id= ?1")
    public int getLogicNodeIDByID(int id);

    public void deleteById(int id);

    public void deleteByIdAndCaseID(int id,int cid);

    public void deleteAllByCaseID(int cid);

    public List<MOD_Fact> findAllByCaseID(int caseID);
}
