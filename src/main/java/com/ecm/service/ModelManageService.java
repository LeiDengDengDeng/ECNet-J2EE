package com.ecm.service;

import com.ecm.model.*;
//import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.List;

public interface ModelManageService {

    public JSONObject getEvidences(int cid);

    public void saveHeader(Evidence_Head header);

    public void deleteHeaderById(int id);

    public void deleteHeadersByCid(int cid);

    public void saveBody(Evidence_Body body);

    public int getLogicNodeIDofBody(int bid);

    public void deleteBodyById(int id);

    public void deleteBodiesByCid(int cid);

    public void saveJoint(MOD_Joint joint);

    public void deleteJointById(int id,int cid);

    public void saveFact(MOD_Fact fact);

    public int getLogicNodeIDofFact(int fid,int cid);

    public void deleteFactById(int id,int cid);

    public void deleteJointsByCid(int cid);

    public void saveArrow(MOD_Arrow arrow);

    public void deleteArrowsByCid(int cid);

    public void writeToExcel(int cid,String filePath);

    public void writeToXML(int cid,String filePath);

    public void writeToXMLSchema(int cid,String filePath);
}
