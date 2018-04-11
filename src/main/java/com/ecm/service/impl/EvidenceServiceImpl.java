package com.ecm.service.impl;

import com.ecm.dao.EvidenceBodyDao;
import com.ecm.dao.EvidenceDocuDao;
import com.ecm.dao.EvidenceHeadDao;
import com.ecm.dao.MOD_ArrowDao;
import com.ecm.keyword.manager.HeadCreator;
import com.ecm.keyword.manager.KeyWordCalculator;
import com.ecm.keyword.reader.ExcelUtil;
import com.ecm.model.*;
import com.ecm.service.EvidenceService;
import com.ecm.service.LogicService;
import com.ecm.service.ModelManageService;
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

import static com.ecm.keyword.reader.ExcelUtil.getExcelWorkbook;
import static com.ecm.keyword.reader.ExcelUtil.getSheetByNum;

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

    @Autowired
    public ModelManageService modelManageService;

    @Override
    public Evidence_Document saveOrUpdate(Evidence_Document evidence_document) {
        int id=findIdByAjxhAndType(evidence_document.getCaseID(),evidence_document.getType());
        if(id!=-1){
            evidence_document.setId(id);
        }
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
    public void deleteBodyAllByCaseId(int caseId) {


        evidenceBodyDao.deleteAllByCaseID(caseId);

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
    public List<Evidence_Document> importDocumentByExcel(String filepath, int caseId) {
        List<Evidence_Document> list=new ArrayList<>();
        Evidence_Document evidence_document1=new Evidence_Document();
        Evidence_Document evidence_document2=new Evidence_Document();
        Workbook book = null;
        try {
            book = ExcelUtil.getExcelWorkbook(filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Sheet sheet = ExcelUtil.getSheetByNum(book, 0);
        int lastRowNum = sheet.getLastRowNum();
        System.out.println("last number is " + lastRowNum);
        String text1="";
        String text2="";
        int xh=1;
        for (int i = 2; i <= lastRowNum; i++) {
            Row row = null;
            row = sheet.getRow(i);
            if (row.getCell(3).getStringCellValue()!=null&&row.getCell(3).getStringCellValue()!="") {
               // System.out.println("reading line is " + i);
                if(row.getCell(5).getStringCellValue().contains("被告")){
                    text2+=xh+"、"+row.getCell(3).getStringCellValue();
                }else{
                    text1+=xh+"、"+row.getCell(3).getStringCellValue();
                }
                xh++;
            }
        }
        evidence_document1.setType(0);//??????????????无法区分原告被告
        evidence_document1.setText(text1);
        evidence_document1.setCaseID(caseId);
        saveOrUpdate(evidence_document1);
        evidence_document2.setType(1);//??????????????无法区分原告被告
        evidence_document2.setText(text2);
        evidence_document2.setCaseID(caseId);
        saveOrUpdate(evidence_document2);

        list.add(evidence_document1);
        list.add(evidence_document2);
        return list;
    }



    @Override
    public List<Evidence_Body> importEviByExcel(String filepath, int caseId,List<Evidence_Document> doculist) {
        deleteBodyAllByCaseId(caseId);

        List<Evidence_Body> bodylist=new ArrayList<>();

        Workbook book = null;
        try {
            book = getExcelWorkbook(filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Sheet sheet = getSheetByNum(book, 0);
        int lastRowNum = sheet.getLastRowNum();
        System.out.println("last number is " + lastRowNum);
        String text = "";
        int xh = 1;
        Evidence_Body evidenceBody = new Evidence_Body();
        for (int i = 2; i <= lastRowNum; i++) {
            Row row = null;
            row = sheet.getRow(i);
            if (row != null) {
                if (row.getCell(3).getStringCellValue() != null && row.getCell(3).getStringCellValue() != "") {
                    System.out.println("reading line is " + i);
                    text = row.getCell(3).getStringCellValue();
                    //System.out.println(evidenceBody.toString());
                    evidenceBody = new Evidence_Body();

                    int logicNodeId=logicService.addEvidenceOrFactNode(caseId,text,0);
                    evidenceBody.setCaseID(caseId);
                    evidenceBody.setBody(text);
                    evidenceBody.setTypeByString(row.getCell(4).getStringCellValue());
                    evidenceBody.setTrustByString(row.getCell(7).getStringCellValue());
                    evidenceBody.setDocumentid(getDocuIdByDocuList(row.getCell(5).getStringCellValue(),doculist));
                    evidenceBody.setLogicNodeID(logicNodeId);
                    evidenceBody=save(evidenceBody);
                    bodylist.add(evidenceBody);
                    deleteHeadAllByBody(evidenceBody.getId());
                }
                Evidence_Head evidence_head = new Evidence_Head();
                evidence_head.setCaseID(caseId);
                // 将区域编号的cell中的内容当做字符串处理
                row.getCell(8).setCellType(HSSFCell.CELL_TYPE_STRING);
                evidence_head.setHead(row.getCell(8).getStringCellValue());
                evidence_head.setBodyid(evidenceBody.getId());
                evidence_head.setDocumentid(evidenceBody.getDocumentid());
                System.out.println(evidence_head.toString());
                evidence_head=save(evidence_head);
                evidenceBody.addHead(evidence_head);
            }


        }
        return  bodylist;





    }

    private int getDocuIdByDocuList(String stringCellValue,List<Evidence_Document> list) {
        if(stringCellValue.contains("原告")){
            return list.get(0).getId();
        }else{
            return list.get(1).getId();
        }
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

        MOD_Fact mod_fact=new MOD_Fact();
        for (int i = 2; i <= lastRowNum; i++) {
            Row row = null;
            row = sheet.getRow(i);
            if (row != null) {
                if (row.getCell(3).getStringCellValue() != null && row.getCell(3).getStringCellValue() != "") {
                    System.out.println("reading line is " + i);
                    System.out.println(hashMap.toString());
                    hashMap=new HashMap<>();
                    hashMap.put("id", row.getCell(1).getNumericCellValue());
                    hashMap.put("name",row.getCell(2).getStringCellValue());
                    hashMap.put("text",row.getCell(3).getStringCellValue());
                    headlist=new ArrayList<>();
                    hashMap.put("headList",headlist);
                    list.add(hashMap);


                    mod_fact=new MOD_Fact();
                    mod_fact.setCaseID(caseId);
                    mod_fact.setId((int)row.getCell(1).getNumericCellValue());
                    mod_fact.setContent(row.getCell(3).getStringCellValue());
                    mod_fact.setName(row.getCell(2).getStringCellValue());
                    mod_fact=modelManageService.saveFact(mod_fact);

                }

                HashMap<String,Object> headMap=new HashMap<>();



                row.getCell(4).setCellType(HSSFCell.CELL_TYPE_STRING);
                row.getCell(6).setCellType(HSSFCell.CELL_TYPE_STRING);
                headMap.put("link",row.getCell(4).getStringCellValue());
                headMap.put("nodeId",row.getCell(5).getNumericCellValue());
                headMap.put("nodeFromEvi",row.getCell(6).getStringCellValue());
                headMap.put("keyText",row.getCell(7).getStringCellValue());


                MOD_Joint mod_joint=new MOD_Joint();
                mod_joint.setCaseID(caseId);
                mod_joint.setContent(row.getCell(4).getStringCellValue());
                mod_joint.setFactID(mod_fact.getId());
                mod_joint=modelManageService.saveJoint(mod_joint);
                MOD_Arrow mod_arrow=new MOD_Arrow();
                mod_arrow.setCaseID(caseId);
                mod_arrow.setNodeTo_jid(mod_joint.getId());
                mod_arrow.setNodeFrom_hid(getHeadIdByEvi(bodylist.get((int)row.getCell(5).getNumericCellValue()-1),row.getCell(6).getStringCellValue()));
                mod_arrow=modelManageService.saveArrow(mod_arrow);
                headlist.add(headMap);
            }


        }

    }

    @Override
    @Async
    public void importLogicByExcel(String filepath, int caseId,List<Evidence_Body> bodyList) {


        Workbook book = null;
        try {
            book = ExcelUtil.getExcelWorkbook(filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Sheet sheet =ExcelUtil.getSheetByNum(book, 2);
        int lastRowNum = sheet.getLastRowNum();
        String text="";

        List<HashMap<String,Object>> list=new ArrayList<>();
        HashMap<String,Object> hashMap=new HashMap<>();
        List<HashMap<String,Object>> headlist=new ArrayList<>();
        List<HashMap<String,Object>> factList=new ArrayList<>();
        int resultId=-1;
        for (int i = 2; i <= lastRowNum; i++) {
            Row row = null;
            row = sheet.getRow(i);
            if (row != null) {
                if (row.getCell(4).getStringCellValue() != null && row.getCell(4).getStringCellValue() != "") {
                    System.out.println("reading line is " + i);
                    text = row.getCell(4).getStringCellValue();
                    text=text.substring(4);

                    System.out.println(hashMap.toString());
                    hashMap=new HashMap<>();
                    hashMap.put("result",text);//idList是string数组

                    resultId=logicService.addNode(caseId,-1,text,3);//结论
                    List<String> lawList= saveLawList(row.getCell(3).getStringCellValue(),resultId,caseId);
                    hashMap.put("law",lawList);
                    factList=new ArrayList<>();
                    hashMap.put("factList",factList);
                    list.add(hashMap);
                    }
                HashMap<String,Object> factMap=new HashMap<>();
                int id=(int)row.getCell(2).getStringCellValue().charAt(2)-'0';
                factMap.put("id",id);
                factMap.put("fact",row.getCell(2).getStringCellValue().substring(4));

                String detail=row.getCell(2).getStringCellValue().substring(4);
                int factId=logicService.addNode(caseId,resultId,detail,1);

                List<Integer> eviList= saveEviList(row.getCell(1).getStringCellValue(),factId,caseId,bodyList);
                factMap.put("evi",eviList);
                factList.add(factMap);

            }


        }
    //    System.out.println(list.toString());



    }



    private int getHeadIdByEvi(Evidence_Body body,String head){
        List<Evidence_Head> headList=body.getHeadList();
        for(Evidence_Head headtemp:headList){
            if(headtemp.getHead().equals(head)){
                return headtemp.getId();
            }
        }

        return -1;
    }

    private List<Integer> saveEviList(String stringCellValue, int factId, int caseId,List<Evidence_Body> bodyList) {


        List<Integer> list=new ArrayList<>();

        stringCellValue=stringCellValue.substring(2);
        // System.out.println(stringCellValue);
        String[] strs=stringCellValue.split("、");
        for(String str:strs){
            list.add(Integer.valueOf(str));
            // System.out.println(str);
            int i=Integer.valueOf(str);
            logicService.addNode(caseId,factId,bodyList.get(i-1).getBody(),0);
        }
        return list;
    }

    /**
     * 为了解决书名号中的顿号问题我也是煞费苦心
     * @param stringCellValue
     * @return
     */
    private List<String> saveLawList(String stringCellValue,int resultId,int caseId) {

//        String[] strs=stringCellValue.split("\\(\\?<=《\\)\\[^》]\\+\\(\\?=》\\)");
        List<String> result=new ArrayList<>();
        String lawName="";
//        lawName=lawName.substring(0,lawName.indexOf('》')+1);
//
//        for(String str:strs){
//            if(!str.contains("《")){
//                result.add(lawName+str);
//                System.out.println(lawName+str);
//            }else{
//                result.add(str);
//                System.out.println(str);
//                lawName=str;
//                lawName=lawName.substring(0,lawName.indexOf('》')+1);
//
//            }
//        }

        int begin=0;
        int end=0;
        String law="";
        for(int i=0;i<stringCellValue.length();i++){
            if(stringCellValue.charAt(i)=='《'){
                begin=i;
                int j=i;
                while(stringCellValue.charAt(j)!='》'){
                    j++;
                }
                end=j;
                lawName=stringCellValue.substring(begin,end+1);
                stringCellValue=stringCellValue.substring(end+1);
                // System.out.println("String"+stringCellValue);
                i=0;
            }
            if(stringCellValue.charAt(i)=='、'){
                law=lawName+stringCellValue.substring(0,i);
                stringCellValue=stringCellValue.substring(i+1);

                i=-1;
                //System.out.println(law);

                result.add(law);
                logicService.addNode(caseId,resultId,law,2);
            }


        }

        result.add(lawName+stringCellValue);
        logicService.addNode(caseId,resultId,lawName+stringCellValue,2);

        return  result;

    }
}
