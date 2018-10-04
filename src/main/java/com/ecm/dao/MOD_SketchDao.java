package com.ecm.dao;

import com.ecm.model.MOD_Sketch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MOD_SketchDao extends JpaRepository<MOD_Sketch, Integer> {

    public MOD_Sketch save(MOD_Sketch sketch);

    public MOD_Sketch findById(int id);

    public List<MOD_Sketch> findAllByCaseID(int caseID);

    public List<MOD_Sketch> findAllByBodyID(int bodyID);

    public List<MOD_Sketch> findAllByFactID(int factID);

    public MOD_Sketch findByBodyIDAndFactID(int bodyID,int factID);

    public void deleteById(int id);

    public void deleteAllByBodyID(int bodyID);

    public void deleteAllByFactID(int factID);

    public void deleteAllByCaseID(int caseID);

    public void deleteByBodyIDAndFactID(int bodyID,int factID);
}
