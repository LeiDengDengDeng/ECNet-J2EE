package com.ecm.service;

import com.ecm.model.Evidence_Body;
import com.ecm.model.Evidence_Document;
import com.ecm.model.Evidence_Head;

import java.util.List;

public interface EvidenceService{

    public Evidence_Document saveOrUpdate(Evidence_Document evidence_document);

    public int findIdByAjxhAndType(int ajxh, int type);
    public Evidence_Document findDocuByAjxhAndType(int ajxh, int type);
    public List<Evidence_Body> findBodyByDocu(int documentid);
    public Evidence_Body save(Evidence_Body evidence_body);
    public Evidence_Head save(Evidence_Head evidence_head);
    public void deleteBodyById(int id);
    public void deleteBodyAll(int document_id);
    public void updateBodyById(String body, int id);
    public void updateTypeById(int type, int id);
    public void updateTrustById(int trust, int id);
    public List<Evidence_Body> createHead(int documentid) ;
    public void updateHeadById(String head, int id);
    public void deleteHeadById(int id);
    public void deleteHeadAllByBody(int body_id);
    public List<Evidence_Head> findHeadByBody(int bodyid);
    public int findLogicId(int bodyid);
    public void importFactByExcel(String filepath,int caseId,List<Evidence_Body> bodylist);
    public void importLogicByExcel(String filepath,int caseId,List<Evidence_Body> bodylist);


}
