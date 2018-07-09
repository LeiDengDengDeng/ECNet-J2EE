package com.ecm.dao;


import com.ecm.model.Evidence_Document;
import com.ecm.model.Text;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface TextDao extends JpaRepository<Text, Integer> {

    public Text getFirstByCaseID(int caseId);

    public Text save(Text text);
}
