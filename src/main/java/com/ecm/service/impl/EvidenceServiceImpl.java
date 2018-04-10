package com.ecm.service.impl;

import com.ecm.dao.EvidenceBodyDao;
import com.ecm.dao.EvidenceDocuDao;
import com.ecm.dao.EvidenceHeadDao;
import com.ecm.dao.MOD_ArrowDao;
import com.ecm.keyword.manager.HeadCreator;
import com.ecm.keyword.manager.KeyWordCalculator;
import com.ecm.keyword.reader.ExcelUtil;
import com.ecm.model.Evidence_Body;
import com.ecm.model.Evidence_Document;
import com.ecm.model.Evidence_Head;
import com.ecm.service.EvidenceService;
import com.ecm.service.LogicService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class EvidenceServiceImpl implements EvidenceService {


    @Autowired
    public EvidenceDocuDao evidenceDocuDao;
    @Autowired
    public EvidenceBodyDao evidenceBodyDao;
    @Autowired
    public EvidenceHeadDao evidenceHeadDao;
    @Autowired
    public LogicService logicService;
    @Autowired
    public MOD_ArrowDao arrowDao;

    @Override
    public Evidence_Document saveOrUpdate(Evidence_Document evidence_document) {
        return evidenceDocuDao.save(evidence_document);
    }

    @Override
    public int findIdByAjxhAndType(int ajxh, int type) {
        Evidence_Document evidence_document=evidenceDocuDao.getEvidenceDocument(ajxh, type);
        if(evidence_document==null){
            return -1;
        }else{
            return evidence_document.getId();
        }
    }

    @Override
    public Evidence_Document findDocuByAjxhAndType(int ajxh, int type) {
        return evidenceDocuDao.getEvidenceDocument(ajxh, type);
    }

    @Override
    public List<Evidence_Body> findBodyByDocu(int documentid) {
        return evidenceBodyDao.getAllByDocumentid(documentid);
    }

    @Override
    public Evidence_Body save(Evidence_Body evidence_body) {
        return evidenceBodyDao.save(evidence_body);
    }
    @Transactional
    @Override
    public Evidence_Head save(Evidence_Head evidence_head) {
        return evidenceHeadDao.save(evidence_head);
    }

    @Transactional
    @Override
    public void deleteBodyById(int id) {
         evidenceBodyDao.deleteById(id);
         return;
    }
    @Transactional
    @Override
    public void deleteBodyAll(int document_id) {
        List<Integer> bodyIdList=evidenceBodyDao.findAllByDocumentid(document_id);


        for (Integer i:bodyIdList
             ) {

            logicService.deleteNode(evidenceBodyDao.findLogicId(i));
        }
        evidenceBodyDao.deleteAllByDocumentid(document_id);
    }
    @Transactional
    @Override
    public void updateBodyById(String body, int id) {
        evidenceBodyDao.updateBodyById(body,id);
    }
    @Transactional
    @Override
    public void updateTypeById(int type, int id) {
        evidenceBodyDao.updateTypeById(type,id);
    }
    @Transactional
    @Override
    public void updateTrustById(int trust, int id) {
        evidenceBodyDao.updateTrustById(trust,id);
    }
    @Transactional
    @Override
    public List<Evidence_Body> createHead(int documentid) {
        List<Evidence_Body> bodies=evidenceBodyDao.getAllByDocumentid(documentid);
        JSONArray jsonArray=new JSONArray();
        for(Evidence_Body body:bodies){
            KeyWordCalculator keyWordCalculator=new KeyWordCalculator();
            HashMap<String, List<String>> res=keyWordCalculator.calcKeyWord(body.getBody());
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("id",body.getId());
            jsonObject.put("content",body.getBody());
            jsonObject.put("type",body.getTypeToString());
            jsonObject.put("keyWordMap",res);
            jsonObject.put("headList",new JSONArray());
            jsonArray.add(jsonObject);
        }
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("evidenceList",jsonArray);

        try {
            jsonObject= HeadCreator.getHead(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("网络传输错误");
            return null;
        }

        if(jsonObject==null){
            System.out.println("提取链头失败");
            return null;
        }else{
            JSONArray evidenceList=(JSONArray)jsonObject.get("evidenceList");
            List<Evidence_Body> evidenceBodyList=new ArrayList<>();
            for(Object object:evidenceList){
                JSONObject evidence=(JSONObject)object;
                Evidence_Body evidence_body=new Evidence_Body();
//                evidence_body.setCaseID(bodies.get(0).getCaseID());
//                evidence_body.setBody(evidence.getString("content"));
//                evidence_body.setDocumentid(documentid);
//                evidence_body.setType(EvidenceType.getTypeByName(evidence.getString("type")).getIndex());
                evidence_body.setId(evidence.getInt("id"));

                JSONArray headArray=evidence.getJSONArray("headList");
                evidenceHeadDao.deleteAllByBodyid(evidence.getInt("id"));
                //List<String> headNameList=(ArrayList<String>)evidence.get("headList");
                for(Object headname:headArray){
                    Evidence_Head head = new Evidence_Head();
                    head.setBodyid(evidence.getInt("id"));
                    head.setCaseID(bodies.get(0).getCaseID());
                    head.setDocumentid(documentid);
                    head.setHead(headname.toString());
                    head=evidenceHeadDao.save(head);
                    evidence_body.addHead(head);
                }
                evidenceBodyList.add(evidence_body);
            }

            return evidenceBodyList;
        }


    }
    @Transactional
    @Override
    public void updateHeadById(String head, int id) {
        evidenceHeadDao.updateHeadById(head, id);
    }
    @Transactional
    @Override
    public void deleteHeadById(int id) {
        evidenceHeadDao.deleteById(id);
        arrowDao.deleteAllByNodeFrom_hid(id);
    }
    @Transactional
    @Override
    public void deleteHeadAllByBody(int body_id) {
        List<Evidence_Head> heads = evidenceHeadDao.findAllByBodyid(body_id);
        for(int i = 0;i<heads.size();i++){
            Evidence_Head h = heads.get(i);
            evidenceHeadDao.deleteById(h.getId());
            arrowDao.deleteAllByNodeFrom_hid(h.getId());
        }
//        evidenceHeadDao.deleteAllByBodyid(body_id);
    }

    @Override
    public List<Evidence_Head> findHeadByBody(int bodyid) {
        return evidenceHeadDao.findAllByBodyid(bodyid);
    }

    @Override
    public int findLogicId(int bodyid) {
        return evidenceBodyDao.findLogicId(bodyid);
    }

    @Override
    @Async
    public void importFactByExcel(String filepath, int caseId, List<Evidence_Body> bodylist) {
        Workbook book = null;
        try {
            book = ExcelUtil.getExcelWorkbook(filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Sheet sheet = ExcelUtil.getSheetByNum(book, 1);
        int lastRowNum = sheet.getLastRowNum();
        String text="";
        List<HashMap<String,Object>> list=new ArrayList<>();
        HashMap<String,Object> hashMap=new HashMap<>();
        List<HashMap<String,Object>> headlist=new ArrayList<>();
        for (int i = 2; i <= lastRowNum; i++) {
            Row row = null;
            row = sheet.getRow(i);
            if (row != null) {
                if (row.getCell(3).getStringCellValue() != null && row.getCell(3).getStringCellValue() != "") {
                    System.out.println("reading line is " + i);
                    //  text = row.getCell(3).getStringCellValue();
                    System.out.println(hashMap.toString());
                    hashMap=new HashMap<>();
                    hashMap.put("id", row.getCell(1).getNumericCellValue());
                    hashMap.put("name",row.getCell(2).getStringCellValue());
                    hashMap.put("text",row.getCell(3).getStringCellValue());
                    headlist=new ArrayList<>();
                    hashMap.put("headList",headlist);
                    list.add(hashMap);
                }
                HashMap<String,Object> headMap=new HashMap<>();

                row.getCell(4).setCellType(HSSFCell.CELL_TYPE_STRING);
                row.getCell(6).setCellType(HSSFCell.CELL_TYPE_STRING);
                headMap.put("link",row.getCell(4).getStringCellValue());
                headMap.put("nodeId",row.getCell(5).getNumericCellValue());
                headMap.put("nodeFromEvi",row.getCell(6).getStringCellValue());
                headMap.put("keyText",row.getCell(7).getStringCellValue());
                headlist.add(headMap);
            }


        }


    }

    @Override
    @Async
    public void importLogicByExcel(String filepath, int caseId, List<Evidence_Body> bodyList) {

    }
}
