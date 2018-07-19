package com.ecm.controller;

import com.ecm.model.Case;
import com.ecm.service.CaseManageService;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/case")
public class CaseController {

    @Autowired
    private CaseManageService caseManageService;

    @RequestMapping(value="/getAll")
    public JSONArray getAllCases(@RequestParam("username") String username){
        return caseManageService.getAllCases(username);
    }

    @RequestMapping(value="/getFinishedCases")
    public JSONArray getFinishedCases(@RequestParam("username") String username){
        return caseManageService.getFinishedCases(username);
    }

    @RequestMapping(value="/getProcessingCases")
    public JSONArray getProcessingCases(@RequestParam("username") String username){
        return caseManageService.getProcessingCases(username);
    }

    @RequestMapping(value="/search")
    public JSONArray searchCasesByName(@RequestParam("username") String username,@RequestParam("casename") String casename){
        return caseManageService.findCasesByName(username, casename);
    }

    @RequestMapping(value="/save")
    public int saveCase(@RequestBody Case c){
        if(caseManageService.isCaseNumExisted(c.getId(),c.getCaseNum())){
            return -1;
        }else{
            return caseManageService.saveCase(c).getId();
        }
    }

    @RequestMapping(value="/update")
    public String updateCase(@RequestBody Case c){
        if(caseManageService.isCaseNumExisted(c.getId(),c.getCaseNum())){
          return "caseNum exists";
        }else{
            Case c_origin = caseManageService.getCaseById(c.getId());
            c_origin.setCaseNum(c.getCaseNum());
            c_origin.setName(c.getName());
            c_origin.setType(c.getType());
            c_origin.setFillingDate(c.getFillingDate());
            c_origin.setClosingDate(c.getClosingDate());
            caseManageService.updateCase(c_origin);
            return "success";
        }
    }

    @RequestMapping(value="/delete")
    public void deleteCases(@RequestBody List<Integer> cases){
        caseManageService.deleteCases(cases);
    }

    @RequestMapping(value="/getCase")
    public Case getCaseById(@RequestParam("caseID") int caseID){
        return caseManageService.getCaseById(caseID);
    }

}
