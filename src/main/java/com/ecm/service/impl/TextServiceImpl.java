package com.ecm.service.impl;

import com.ecm.dao.*;
import com.ecm.keyword.manager.HeadCreator;
import com.ecm.keyword.manager.KeyWordCalculator;
import com.ecm.keyword.reader.ExcelUtil;
import com.ecm.model.*;
import com.ecm.service.EvidenceService;
import com.ecm.service.LogicService;
import com.ecm.service.ModelManageService;
import com.ecm.service.TextService;
import com.ecm.util.ImportXMLUtil;
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
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.ecm.keyword.reader.ExcelUtil.getExcelWorkbook;
import static com.ecm.keyword.reader.ExcelUtil.getSheetByNum;

@Service
public class TextServiceImpl implements TextService {


    @Autowired
    public TextDao textDao;

    @Autowired
    public LogicService logicService;

    @Autowired
    public ModelManageService modelManageService;



    @Override
    public Text getText(int cid) {
        Text text = findTextByCid(cid);
        if (text==null) {
            text=new Text();
            text.setCaseID(cid);
            text.setEvidence("上述事实，有公诉机关及被告人提供，并经法庭质证、认证的下列证据予以证明："+modelManageService.getEvidencesList(cid));
            text.setFact("公诉机关指控："+modelManageService.getFactList(cid));
            text.setResult("本院认为："+logicService.getResultContents(cid));
           return textDao.save(text);
        }else{
            return text;
        }

    }

    @Override
    public Text findTextByCid(int cid) {
        return textDao.getFirstByCaseID(cid);
    }

    @Override
    public Text updateText(Text text) {
        int id = findTextByCid(text.getCaseID()).getId();
        if (id != -1) {
            text.setId(id);
        }
        return textDao.save(text);
    }
}