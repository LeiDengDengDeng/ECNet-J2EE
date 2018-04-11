package com.ecm.service;

import com.ecm.model.*;
//import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.List;

public interface ModelManageService {

    public JSONObject getEvidences(int cid);

    public Evidence_Head saveHeader(Evidence_Head header);

    public void saveHeaders(List<Evidence_Head> headers);

    public void deleteHeaderById(int id);

    public void deleteHeadersByCid(int cid);

    public Evidence_Body saveBody(Evidence_Body body);

    public void saveBodies(List<Evidence_Body> bodies);

    public int getLogicNodeIDofBody(int bid);

    public void deleteBodyById(int id);

    public void deleteBodiesByCid(int cid);

    public MOD_Joint saveJoint(MOD_Joint joint);

    public void saveJoints(List<MOD_Joint> joints);

    public void deleteJointById(int id);

    public MOD_Fact saveFact(MOD_Fact fact);

    public void saveFacts(List<MOD_Fact> facts);

//    public int getLogicNodeIDofFact(int fid,int cid);

    public int getLogicNodeIDofFact(int fid);

    public MOD_Fact getFactByID(int id);

    public void deleteFactById(int id);

    public void deleteJointsByCid(int cid);

    public MOD_Arrow saveArrow(MOD_Arrow arrow);

    public void saveArrows(List<MOD_Arrow> arrows);

    public void deleteArrowsByCid(int cid);

    public void writeToExcel(int cid,String filePath);

    public void writeToXML(int cid,String filePath);

    public void writeToXMLSchema(int cid,String filePath);
}
