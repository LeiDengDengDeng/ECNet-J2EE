package com.ecm.service;

import com.ecm.model.Evidence_Body;
import com.ecm.model.Evidence_Document;
import com.ecm.model.Evidence_Head;
import com.ecm.util.ImportXMLUtil;

import java.util.List;

public interface EvidenceService{

    Evidence_Document saveOrUpdate(Evidence_Document evidence_document);

    int findIdByAjxhAndType(int ajxh, int type);
    Evidence_Document findDocuByAjxhAndType(int ajxh, int type);
    List<Evidence_Body> findBodyByDocu(int documentid);
    Evidence_Body save(Evidence_Body evidence_body);
    Evidence_Head save(Evidence_Head evidence_head);
    void deleteBodyById(int id);
    void deleteBodyAll(int document_id);
    void deleteBodyAllByCaseId(int caseId);
    void updateBodyById(String body, int id);
    void updateTypeById(int type, int id);
    void updateTrustById(int trust, int id);
    List<Evidence_Body> createHead(int documentid) ;
    void updateHeadById(String head, int id);
    void deleteHeadById(int id);
    void deleteHeadAllByCaseId(int caseId);
    void deleteHeadAllByBody(int body_id);

    List<Evidence_Head> findHeadByBody(int bodyid);
    int findLogicId(int bodyid);
    List<Evidence_Document> importDocumentByExcel(String filepath,int caseId);
    List<Evidence_Body> importEviByExcel(String filepath,int caseId,List<Evidence_Document> documentList);
    void importFactByExcel(String filepath,int caseId,List<Evidence_Body> bodylist);
    void importLogicByExcel(String filepath,int caseId,List<Evidence_Body> bodylist);
    List<Evidence_Document> importDocumentByXML(ImportXMLUtil xmlUtil);
    List<Evidence_Body> importEviByXML(ImportXMLUtil xmlUtil,List<Evidence_Document> documentList);
    void importFactByXML(ImportXMLUtil xmlUtil,List<Evidence_Body> bodylist);
    void importLogicByXML(ImportXMLUtil xmlUtil,List<Evidence_Body> bodylist);
    void deleteAllTable(int caseId);

}
