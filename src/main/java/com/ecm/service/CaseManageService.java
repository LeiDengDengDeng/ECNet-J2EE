package com.ecm.service;
import com.ecm.model.Case;
import com.ecm.model.Judgment;
import net.sf.json.JSONArray;

import java.util.List;

public interface CaseManageService {

    public JSONArray getAllCases(String username);

    public JSONArray getFinishedCases(String username);

    public JSONArray getProcessingCases(String username);

    public JSONArray getRawCases(String username);

    public JSONArray findCasesByName(String username,String casename);

    public Case saveCase(Case c);

    public Case updateCase(Case c);

    public Judgment saveJudgment(Judgment judgment);

    public void deleteCases(List<Integer> cases);

    public Case getCaseById(int id);

    public boolean isCaseNumExisted(int id,String caseNum);

    public boolean isCaseNumExisted(String caseNum);
}
