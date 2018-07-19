package com.ecm.service.impl;

import com.ecm.dao.CaseDao;
import com.ecm.dao.JudgmentDao;
import com.ecm.model.Case;
import com.ecm.model.Judgment;
import com.ecm.service.CaseManageService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
public class CaseManageServiceImpl implements CaseManageService {

    @Autowired
    private CaseDao caseDao;
    @Autowired
    private JudgmentDao judgmentDao;

    @Override
    public JSONArray getAllCases(String username) {
        JSONArray res = new JSONArray();
        List<Judgment> judges = judgmentDao.getAllByName(username);

        for(int i = 0;i<judges.size();i++){
            JSONObject jsonObject = new JSONObject();

            Judgment j = judges.get(i);
            Case c = caseDao.findById(Integer.parseInt(j.getCid()));

            jsonObject.put("cid",j.getCid());
            jsonObject.put("caseNum",c.getCaseNum());
            jsonObject.put("cname",c.getName());
            jsonObject.put("type",c.getType());
            jsonObject.put("fillingDate",c.getFillingDate().toString());

//            if(j.getHasJudge().equals('0')){
//                jsonObject.put("courtClerk",j.getRealName());
//            }else
//            if (j.getIsJudge().equals('0')||j.getIsUndertaker().equals('Y')){
                jsonObject.put("manageJudge",j.getRealName());
//            }

            res.add(jsonObject);
        }
        return res;
    }

    @Override
    public JSONArray getFinishedCases(String username) {
        JSONArray res = new JSONArray();
        List<Judgment> judges = judgmentDao.getAllByName(username);

        for(int i = 0;i<judges.size();i++){
            JSONObject jsonObject = new JSONObject();

            Judgment j = judges.get(i);
            Case c = caseDao.findById(Integer.parseInt(j.getCid()));

//            Timestamp fillingDate = c.getFillingDate();
            Timestamp closingDate = c.getClosingDate();
            Timestamp now = new Timestamp(System.currentTimeMillis());
            if(closingDate!=null&&closingDate.before(now)){
                jsonObject.put("cid",j.getCid());
                jsonObject.put("caseNum",c.getCaseNum());
                jsonObject.put("cname",c.getName());
                jsonObject.put("type",c.getType());
                jsonObject.put("fillingDate",c.getFillingDate().toString());
                jsonObject.put("manageJudge",j.getRealName());
                res.add(jsonObject);
            }
        }
        return res;
    }

    @Override
    public JSONArray getProcessingCases(String username) {
        JSONArray res = new JSONArray();
        List<Judgment> judges = judgmentDao.getAllByName(username);

        for(int i = 0;i<judges.size();i++){
            JSONObject jsonObject = new JSONObject();

            Judgment j = judges.get(i);
            Case c = caseDao.findById(Integer.parseInt(j.getCid()));

            Timestamp closingDate = c.getClosingDate();
            Timestamp now = new Timestamp(System.currentTimeMillis());
            if(closingDate==null||closingDate.after(now)){
                jsonObject.put("cid",j.getCid());
                jsonObject.put("caseNum",c.getCaseNum());
                jsonObject.put("cname",c.getName());
                jsonObject.put("type",c.getType());
                jsonObject.put("fillingDate",c.getFillingDate().toString());
                jsonObject.put("manageJudge",j.getRealName());
                res.add(jsonObject);
            }
        }
        return res;
    }

    @Override
    public JSONArray getRawCases(String username) {
        return null;
    }

    @Override
    public JSONArray findCasesByName(String username, String casename) {

        JSONArray res = new JSONArray();
        List<Case> cases = caseDao.findByNameLike(casename);

        for(int i = 0;i<cases.size();i++){

            Case c = cases.get(i);
            List<String> jnames = judgmentDao.getNameByCid(c.getId()+"");

            if(jnames.contains(username)){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("cid",c.getId());
                jsonObject.put("caseNum",c.getCaseNum());
                jsonObject.put("cname",c.getName());
                jsonObject.put("type",c.getType());
                jsonObject.put("fillingDate",c.getFillingDate().toString());
                jsonObject.put("manageJudge",username);
                res.add(jsonObject);
            }
        }
        return res;
    }

    @Override
    @Transactional
    public Case saveCase(Case c) {
        int id = caseDao.getMaxID()+1;
        c.setId(id);
        Judgment j = new Judgment();
        j.setCid(id+"");
        j.setIsJudge("0");
        j.setIsUndertaker("Y");
        j.setJid("0");
        j.setRealName("林世开");
        judgmentDao.save(j);
        return caseDao.save(c);
    }

    @Override
    @Transactional
    public Case updateCase(Case c) {
        return caseDao.save(c);
    }

    @Override
    @Transactional
    public void deleteCases(List<Integer> cases) {
        for(int i = 0;i<cases.size();i++){
            int cid = cases.get(i);
            caseDao.deleteById(cid);
            judgmentDao.deleteByCid(cid+"");
        }
    }

    @Override
    public Case getCaseById(int id) {
        return caseDao.findById(id);
    }

    @Override
    public boolean isCaseNumExisted(int id, String caseNum) {
        return (caseDao.findOtherCaseByCaseNum(id, caseNum)!=null);
    }
}
