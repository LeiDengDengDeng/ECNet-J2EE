package com.ecm.service.impl;

import com.ecm.dao.*;
import com.ecm.model.*;
import com.ecm.service.ModelManageService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ModelManageServiceImpl implements ModelManageService {

    @Autowired
    private EvidenceBodyDao evidenceBodyDao;
    @Autowired
    private EvidenceHeadDao evidenceHeadDao;
    @Autowired
    private MOD_JointDao jointDao;
    @Autowired
    private MOD_ArrowDao arrowDao;
    @Autowired
    private MOD_FactDao factDao;

    @Override
    public JSONObject getEvidences(int cid) {
        JSONObject res = new JSONObject();
        JSONArray trusts = new JSONArray();
        JSONArray untrusts = new JSONArray();

        List<Evidence_Body> bodies = evidenceBodyDao.findAllByCaseID(cid);
        for(int i = 0;i<bodies.size();i++){
            Evidence_Body body = bodies.get(i);
            int bid = body.getId();

            if(body.getTrust()==1){
                JSONObject jo = new JSONObject();
                jo.put("body",body);
//                jo.put("bid",bid);
//                jo.put("documentID",body.getDocumentid());
//                jo.put("name",body.getName());
//                jo.put("content",body.getBody());
//                jo.put("isDefendant",body.getIsDefendant());
//                jo.put("type",body.getType());
//                jo.put("committer",body.getCommitter());
//                jo.put("reason",body.getReason());
//                jo.put("conclusion",body.getConclusion());
//                jo.put("x",body.getX());
//                jo.put("y",body.getY());

                List<Evidence_Head> headers = evidenceHeadDao.findAllByCaseIDAndBodyid(cid,bid);
                jo.put("headers",headers);
                trusts.add(jo);
            }else{
                JSONObject jo = new JSONObject();
                jo.put("content",body.getBody());
                jo.put("isDefendant",body.getIsDefendant());
                List<String> headers = evidenceHeadDao.findContentsByCaseIDAndBodyid(cid,bid);
                jo.put("headers",headers);
                untrusts.add(jo);
            }
        }

        List<Evidence_Head> freeHeaders = evidenceHeadDao.findAllByCaseIDAndBodyid(cid,-1);
        res.put("trusts",trusts);
        res.put("untrusts",untrusts);
        res.put("freeHeaders",freeHeaders);
        res.put("joints",jointDao.findAllByCaseID(cid));
        res.put("arrows",arrowDao.findAllByCaseID(cid));
        res.put("facts",factDao.findAllByCaseID(cid));

        return res;
    }

    @Override
    public void saveHeader(Evidence_Head header) {
        evidenceHeadDao.save(header);
    }

    @Override
    @Transactional
    public void deleteHeaderById(int id) {
        evidenceHeadDao.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteHeadersByCid(int cid) {
        evidenceHeadDao.deleteAllByCaseID(cid);
    }

    @Override
    public void saveBody(Evidence_Body body) {
        evidenceBodyDao.save(body);
    }

    @Override
    public int getLogicNodeIDofBody(int bid) {
        return evidenceBodyDao.findLogicId(bid);
    }

    @Override
    @Transactional
    public void deleteBodyById(int id) {
        evidenceBodyDao.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteBodiesByCid(int cid) {
        evidenceBodyDao.deleteAllByCaseID(cid);
    }

    @Override
    public void saveJoint(MOD_Joint joint) {
        jointDao.save(joint);
    }

    @Override
    @Transactional
    public void deleteJointById(int id) {
        jointDao.deleteById(id);
    }

    @Override
    public void saveFact(MOD_Fact fact) {
        factDao.save(fact);
    }

    @Override
    public int getLogicNodeIDofFact(int fid) {
        return factDao.getLogicNodeIDByID(fid);
    }

    @Override
    @Transactional
    public void deleteFactById(int id) {
        factDao.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteJointsByCid(int cid) {
        jointDao.deleteAllByCaseID(cid);
    }

    @Override
    public void saveArrow(MOD_Arrow arrow) {
        arrowDao.save(arrow);
    }

    @Override
    @Transactional
    public void deleteArrowsByCid(int cid) {
        arrowDao.deleteAllByCaseID(cid);
    }

    @Override
    public void writeToExcel(int cid,String filePath) {
        //创建workbook
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建证据清单sheet
        HSSFSheet sheet1 = workbook.createSheet("证据清单");
        CellRangeAddress callRangeAddress = new CellRangeAddress(0,0,1,9);//起始行,结束行,起始列,结束列
        sheet1.addMergedRegion(callRangeAddress);

        HSSFRow row = sheet1.createRow(0);
        HSSFCell cell = row.createCell(1);
        cell.setCellValue("证据清单");

        HSSFRow row2 = sheet1.createRow(1);
        String[] titles = {"序号","证据名称","证据明细","证据种类（下拉）","提交人","质证理由","质证结论（下拉）","链头信息","该链头在证据中的关键文本（短句）"};
        for(int i=1;i<=titles.length;i++)
        {
            HSSFCell cell2 = row2.createCell(i);
            //加载单元格样式
//            cell2.setCellStyle(colStyle);
            cell2.setCellValue(titles[i-1]);
        }

        List<Evidence_Body> bodies = evidenceBodyDao.findAllByCaseID(cid);
        int rowNum = 2;
        for(int i = 0;i<bodies.size();i++){
            HSSFRow hrow = sheet1.createRow(rowNum);
            Evidence_Body body = bodies.get(i);
            int bid = body.getId();
            List<Evidence_Head> headers = evidenceHeadDao.findAllByCaseIDAndBodyid(cid,bid);
            int hNum = headers.size();
            if(hNum<3)
                hNum = 3;

            for(int j = 1;j<=7;j++){
                CellRangeAddress cra = new CellRangeAddress(rowNum,rowNum+hNum-1,j,j);
                sheet1.addMergedRegion(cra);
            }
            HSSFCell ctemp1 = hrow.createCell(1);
            ctemp1.setCellValue(bid);
            HSSFCell ctemp2 = hrow.createCell(2);
            ctemp2.setCellValue(body.getName());
            HSSFCell ctemp3 = hrow.createCell(3);
            ctemp3.setCellValue(body.getBody());
            HSSFCell ctemp4 = hrow.createCell(4);
            ctemp4.setCellValue(body.getTypeToString());
            HSSFCell ctemp5 = hrow.createCell(5);
            ctemp5.setCellValue(body.getCommitter());
            HSSFCell ctemp6 = hrow.createCell(6);
            ctemp6.setCellValue(body.getReason());
            HSSFCell ctemp7 = hrow.createCell(7);
            ctemp7.setCellValue(body.getTrustToString());

            if(headers.size()>0){
                Evidence_Head h = headers.get(0);
                HSSFCell ctemp8 = hrow.createCell(8);
                ctemp8.setCellValue(h.getHead());
                HSSFCell ctemp9 = hrow.createCell(9);
                ctemp9.setCellValue(h.getKeyText());

                for(int k = 1;k<headers.size();k++){
                    rowNum++;
                    h = headers.get(k);
                    HSSFRow rowtemp = sheet1.createRow(rowNum);
                    HSSFCell ctempk_8 = rowtemp.createCell(8);
                    ctempk_8.setCellValue(h.getHead());
                    HSSFCell ctempk_9 = rowtemp.createCell(9);
                    ctempk_9.setCellValue(h.getKeyText());
                }
                rowNum++;
            }
            if(headers.size()<3){
                rowNum+=(3-headers.size());
            }
        }

        //创建事实清单sheet
        HSSFSheet sheet2 = workbook.createSheet("事实清单");

        File file = new File(filePath);
        try {
            if(!file.exists()){
                file.createNewFile();
            }

            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            workbook.close();
            fileOut.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
