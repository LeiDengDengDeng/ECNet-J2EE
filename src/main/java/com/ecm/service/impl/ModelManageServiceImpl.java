package com.ecm.service.impl;

import com.ecm.dao.*;
import com.ecm.model.*;
import com.ecm.service.ModelManageService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map number = new HashMap();//链体id与证据清单序号

        // 标题字体对象
        HSSFFont titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 16);
//        font.setFontName("新宋体");
        titleFont.setColor(HSSFColor.BLUE.index);

        //列名字体
        HSSFFont cNameFont = workbook.createFont();
        cNameFont.setBold(true);
        cNameFont.setFontHeightInPoints((short) 10);
//        font.setFontName("新宋体");
        cNameFont.setColor(HSSFColor.BLUE.index);

        //单元格字体
        HSSFFont font = workbook.createFont();
        font.setBold(false);
        font.setFontHeightInPoints((short) 10);
//        font.setFontName("新宋体");
        cNameFont.setColor(HSSFColor.BLACK.index);

        //标题样式
        HSSFCellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        titleStyle.setFont(titleFont);
        titleStyle.setWrapText(true);//自动换行

        //列名样式
        HSSFCellStyle cStyle = workbook.createCellStyle();
        cStyle.setAlignment(HorizontalAlignment.CENTER);
        cStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cStyle.setFont(cNameFont);
        cStyle.setWrapText(true);
        //设置边框样式
//        cStyle.setBorderTop(BorderStyle.THIN);
//        cStyle.setBorderBottom(BorderStyle.THIN);
//        cStyle.setBorderLeft(BorderStyle.THIN);
//        cStyle.setBorderRight(BorderStyle.THIN);
        //设置边框颜色
//        cStyle.setTopBorderColor(HSSFColor.BLACK.index);
//        cStyle.setBottomBorderColor(HSSFColor.BLACK.index);
//        cStyle.setLeftBorderColor(HSSFColor.BLACK.index);
//        cStyle.setRightBorderColor(HSSFColor.BLACK.index);

        //列名样式
        HSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(font);
        style.setWrapText(true);
        //设置边框样式
//        style.setBorderTop(BorderStyle.THIN);
//        style.setBorderBottom(BorderStyle.THIN);
//        style.setBorderLeft(BorderStyle.THIN);
//        style.setBorderRight(BorderStyle.THIN);

        //创建证据清单sheet
        HSSFSheet sheet1 = workbook.createSheet("证据清单");
//        sheet1.autoSizeColumn(1, true);
        CellRangeAddress callRangeAddress = new CellRangeAddress(0,0,1,9);//起始行,结束行,起始列,结束列
        sheet1.addMergedRegion(callRangeAddress);
        HSSFRow row = sheet1.createRow(0);
        HSSFCell cell = row.createCell(1);
        cell.setCellValue("证据清单");
        cell.setCellStyle(titleStyle);

        HSSFRow row2 = sheet1.createRow(1);
        String[] titles = {"序号","证据名称","证据明细","证据种类（下拉）","提交人","质证理由","质证结论（下拉）","链头信息","该链头在证据中的关键文本（短句）"};
        for(int i=1;i<=titles.length;i++)
        {
            HSSFCell cell2 = row2.createCell(i);
            //加载单元格样式
            cell2.setCellStyle(cStyle);
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

            if(hNum>1)
            for(int j = 1;j<=7;j++){
                CellRangeAddress cra = new CellRangeAddress(rowNum,rowNum+hNum-1,j,j);
                sheet1.addMergedRegion(cra);
//                RegionUtil.setBorderBottom(1,cra, sheet1);
//                RegionUtil.setBorderLeft(1,cra, sheet1);
//                RegionUtil.setBorderRight(1,cra, sheet1);
//                RegionUtil.setBorderTop(1,cra, sheet1);
//                setRegionStyle(sheet1,cra,style);
            }
            HSSFCell ctemp1 = hrow.getCell(1);
            if(ctemp1==null){
                ctemp1 = hrow.createCell(1);
                ctemp1.setCellStyle(style);
            }
            ctemp1.setCellValue(i+1);
            number.put(bid,i+1);

            HSSFCell ctemp2 = hrow.getCell(2);
            if(ctemp2==null){
                ctemp2 = hrow.createCell(2);
                ctemp2.setCellStyle(style);
            }
            ctemp2.setCellValue(body.getName());

            HSSFCell ctemp3 = hrow.getCell(3);
            if(ctemp3==null){
                ctemp3 = hrow.createCell(3);
                ctemp3.setCellStyle(style);
            }
            ctemp3.setCellValue(body.getBody());

            HSSFCell ctemp4 = hrow.getCell(4);
            if(ctemp4==null){
                ctemp4 = hrow.createCell(4);
                ctemp4.setCellStyle(style);
            }
            ctemp4.setCellValue(body.getTypeToString());

            HSSFCell ctemp5 = hrow.getCell(5);
            if(ctemp5==null){
                ctemp5 = hrow.createCell(5);
                ctemp5.setCellStyle(style);
            }
            ctemp5.setCellValue(body.getCommitter());

            HSSFCell ctemp6 = hrow.getCell(6);
            if(ctemp6==null){
                ctemp6 = hrow.createCell(6);
                ctemp6.setCellStyle(style);
            }
            ctemp6.setCellValue(body.getReason());

            HSSFCell ctemp7 = hrow.getCell(7);
            if(ctemp7==null){
                ctemp7 = hrow.createCell(7);
                ctemp7.setCellStyle(style);
            }
            ctemp7.setCellValue(body.getTrustToString());

            for(int k = 0;k<hNum;k++){
                Evidence_Head h = headers.get(k);
                HSSFRow rowtemp;
                if(k==0){
                    rowtemp = hrow;
                }else{
                    rowtemp = sheet1.createRow(rowNum);
                }
                HSSFCell ctempk_8 = rowtemp.createCell(8);
                ctempk_8.setCellValue(h.getHead());
                ctempk_8.setCellStyle(style);
                HSSFCell ctempk_9 = rowtemp.createCell(9);
                ctempk_9.setCellValue(h.getKeyText());
                ctempk_9.setCellStyle(style);
                rowNum++;
            }
            if(hNum==0){
                rowNum++;
            }
        }


        //创建事实清单sheet
        HSSFSheet sheet2 = workbook.createSheet("事实清单");
//        sheet2.autoSizeColumn(1, true);
        CellRangeAddress callRangeAddress2 = new CellRangeAddress(0,0,1,7);//起始行,结束行,起始列,结束列
        sheet2.addMergedRegion(callRangeAddress2);
        HSSFRow r1 = sheet2.createRow(0);
        HSSFCell c1 = r1.createCell(1);
        c1.setCellValue("事实清单");
        c1.setCellStyle(titleStyle);

        HSSFRow r2 = sheet2.createRow(1);
        String[] titles2 = {"序号","事实名称","事实明细(较长文本)","来自事实的链头（联结点）","来自证据的链头","证据序号(引用证据清单的序号)","与链头相关的证据中的关键文本(短句)"};
        for(int i=1;i<=titles2.length;i++)
        {
            HSSFCell cell2 = r2.createCell(i);
            //加载单元格样式
            cell2.setCellStyle(cStyle);
            cell2.setCellValue(titles2[i-1]);
        }

        int startRow = 2;
        int endRow = 2;
        List<MOD_Fact> facts = factDao.findAllByCaseID(cid);
        for(int i = 0;i<facts.size();i++){
            startRow = endRow;
            HSSFRow hrow = sheet2.createRow(endRow);
            MOD_Fact fact = facts.get(i);
            int fid = fact.getId();
            List<MOD_Joint> joints = jointDao.findAllByFactID(fid);
            int jNum = joints.size();

            for(int j = 0;j<jNum;j++){
                MOD_Joint joint = joints.get(j);
                List<Integer> hids = arrowDao.getHeaderIdByJointId(joint.getId());
                int hNum = hids.size();
                HSSFRow rowtemp;
                if(j==0){
                    rowtemp = hrow;
                }else{
                    rowtemp = sheet2.createRow(endRow);
                }

                if(hNum>1){
                    CellRangeAddress cra = new CellRangeAddress(endRow,endRow+hNum-1,4,4);
                    sheet2.addMergedRegion(cra);
//                    RegionUtil.setBorderBottom(1,cra, sheet1);
//                    RegionUtil.setBorderLeft(1,cra, sheet1);
//                    RegionUtil.setBorderRight(1,cra, sheet1);
//                    RegionUtil.setBorderTop(1,cra, sheet1);
//                   setRegionStyle(sheet2,cra,style);
                }
                HSSFCell ctemp4 = rowtemp.getCell(4);
                if(ctemp4==null){
                    ctemp4 = rowtemp.createCell(4);
                    ctemp4.setCellStyle(style);
                }
                ctemp4.setCellValue(joint.getContent());

                for(int k = 0;k<hNum;k++){
                    int hid = hids.get(k);
                    Evidence_Head head = evidenceHeadDao.findById(hid);
                    HSSFRow rtmp;
                    if(k==0){
                        rtmp = rowtemp;
                    }else{
                        rtmp = sheet2.createRow(endRow);
                    }
                    HSSFCell ctemp5 = rtmp.createCell(5);
                    ctemp5.setCellValue(head.getHead());
                    ctemp5.setCellStyle(style);
                    HSSFCell ctemp6 = rtmp.createCell(6);
                    ctemp6.setCellValue(number.get(head.getBodyid()).toString());
                    ctemp6.setCellStyle(style);
                    HSSFCell ctemp7 = rtmp.createCell(7);
                    ctemp7.setCellValue(head.getKeyText());
                    ctemp7.setCellStyle(style);
                    endRow++;
                }
                if(hNum==0){
                    HSSFCell ctemp5 = rowtemp.createCell(5);
                    ctemp5.setCellStyle(style);
                    HSSFCell ctemp6 = rowtemp.createCell(6);
                    ctemp6.setCellStyle(style);
                    HSSFCell ctemp7 = rowtemp.createCell(7);
                    ctemp7.setCellStyle(style);
                    endRow++;
                }
            }
            if(jNum==0){
                HSSFCell ctemp4 = hrow.createCell(4);
                ctemp4.setCellStyle(style);
                endRow++;
            }

            if(endRow-1>startRow){
                for(int m = 1;m<=3;m++){
                    CellRangeAddress crat = new CellRangeAddress(startRow,endRow-1,m,m);
                    sheet2.addMergedRegion(crat);
//                    RegionUtil.setBorderBottom(1,crat, sheet1);
//                    RegionUtil.setBorderLeft(1,crat, sheet1);
//                    RegionUtil.setBorderRight(1,crat, sheet1);
//                    RegionUtil.setBorderTop(1,crat, sheet1);
//                    setRegionStyle(sheet2,crat,style);
                }
            }
            HSSFCell ctemp1 = hrow.getCell(1);
            if(ctemp1==null){
                ctemp1 = hrow.createCell(1);
                ctemp1.setCellStyle(style);
            }
            ctemp1.setCellValue(i+1);

            HSSFCell ctemp2 = hrow.getCell(2);
            if(ctemp2==null){
                ctemp2 = hrow.createCell(2);
                ctemp2.setCellStyle(style);
            }
            ctemp2.setCellValue(fact.getName());

            HSSFCell ctemp3 = hrow.getCell(3);
            if(ctemp3==null){
                ctemp3 = hrow.createCell(3);
                ctemp3.setCellStyle(style);
            }
            ctemp3.setCellValue(fact.getContent());
        }


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

    private void setRegionStyle(HSSFSheet sheet, CellRangeAddress region, HSSFCellStyle cs) {
        for (int i = region.getFirstRow(); i <= region.getLastRow(); i++) {
            HSSFRow row = sheet.getRow(i);
            if(row==null){
                row = sheet.createRow(i);
            }
            for (int j = region.getFirstColumn(); j <= region.getLastColumn(); j++) {
                HSSFCell cell = row.getCell(j);
                if(cell==null){
                    cell = row.createCell(j);
                    System.out.println(i+";"+j);
                }
                cell.setCellStyle(cs);
            }
        }
    }
}
