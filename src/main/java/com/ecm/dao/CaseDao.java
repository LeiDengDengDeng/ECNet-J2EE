package com.ecm.dao;

import com.ecm.model.Case;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CaseDao extends JpaRepository<Case, Integer> {

    public Case findById(int id);

    @Query(value = "select c from Case c where c.name like %?1%")
    public List<Case> findByNameLike(String name);

    public void deleteById(int id);

    public Case save(Case c);

    @Query(value = "select c from Case c where c.id<>?1 and c.caseNum = ?2")
    public Case findOtherCaseByCaseNum(int id,String caseNum);

    @Query(value = "select max(c.id) from Case c")
    public int getMaxID();

    public Case findByCaseNum(String caseNum);
}
