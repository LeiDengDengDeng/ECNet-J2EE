package com.ecm.controller;

import com.ecm.model.*;
import com.ecm.service.LogicService;
import com.ecm.service.ModelManageService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value="/model")
public class ModelController {
    @Autowired
    private ModelManageService modelManageService;
    @Autowired
    private LogicService logicService;

    @RequestMapping(value="/getEvidences")
    public JSONObject getEvidences(@RequestParam("cid") int cid){

        return modelManageService.getEvidences(cid);
    }

    @RequestMapping(value="/saveHead")
    public int saveHead(@RequestBody Evidence_Head head){

        return modelManageService.saveHeader(head).getId();
    }

    @RequestMapping(value="/deleteHead")
    public void deleteHead(@RequestParam("id") int id){

        modelManageService.deleteHeaderById(id);
    }

    @RequestMapping(value="/saveBody")
    public int saveBody(@RequestBody Evidence_Body body){

        if(body.getLogicNodeID()>=0){
            logicService.modEvidenceOrFactNode(body.getLogicNodeID(),body.getBody());
        }else{
            int lid = logicService.addEvidenceOrFactNode(body.getCaseID(),body.getBody(),0);
            body.setLogicNodeID(lid);
        }

        return modelManageService.saveBody(body).getId();
    }

    @RequestMapping(value="/deleteBody")
    public void deleteBody(@RequestParam("id") int id){

        modelManageService.deleteBodyById(id);
        int lid = modelManageService.getLogicNodeIDofBody(id);
        if(lid>=0)
            logicService.deleteNode(lid);
    }

    @RequestMapping(value="/saveJoint")
    public int saveJoint(@RequestBody MOD_Joint joint){
        return modelManageService.saveJoint(joint).getId();
    }

    @RequestMapping(value="/deleteJoint")
    public void deleteJoint(@RequestParam("id") int id){

        modelManageService.deleteJointById(id);
    }

    @RequestMapping(value="/saveFact")
    public int saveFact(@RequestBody MOD_Fact fact){

        if(fact.getLogicNodeID()>=0){
            logicService.modEvidenceOrFactNode(fact.getLogicNodeID(),fact.getContent());
        }else{
            int lid = logicService.addEvidenceOrFactNode(fact.getCaseID(),fact.getContent(),1);
            fact.setLogicNodeID(lid);
        }
        return modelManageService.saveFact(fact).getId();
    }

    @RequestMapping(value="/deleteFact")
    public void deleteFact(@RequestParam("id") int id){

        modelManageService.deleteFactById(id);
        MOD_Fact fact = modelManageService.getFactByID(id);
        if(fact!=null){
            if(fact.getLogicNodeID()>=0){
                logicService.deleteNode(fact.getLogicNodeID());
            }
        }

    }

    @RequestMapping(value="/saveAll")
    public void saveAll(@RequestBody Evidence_Data all){

//        JSONArray headsArr =  JSONArray.fromObject(all.get("headers"));
//        List<Evidence_Head> heads = JSONArray.toList(headsArr,Evidence_Head.class,new JsonConfig());
//        JSONArray bodiesArr =  JSONArray.fromObject(all.get("bodies"));
//        List<Evidence_Body> bodies = JSONArray.toList(bodiesArr,Evidence_Body.class,new JsonConfig());
//        JSONArray jointsArr =  JSONArray.fromObject(all.get("joints"));
//        List<MOD_Joint> joints = JSONArray.toList(jointsArr,MOD_Joint.class,new JsonConfig());
//        JSONArray factsArr =  JSONArray.fromObject(all.get("facts"));
//        List<MOD_Fact> facts = JSONArray.toList(factsArr,MOD_Fact.class,new JsonConfig());
//        JSONArray arrowsArr =  JSONArray.fromObject(all.get("arrows"));
//        List<MOD_Arrow> arrows = JSONArray.toList(arrowsArr,MOD_Arrow.class,new JsonConfig());

//        System.out.println("*************");
        modelManageService.saveHeaders(all.getHeaders());
        modelManageService.saveBodies(all.getBodies());
        modelManageService.saveJoints(all.getJoints());
        modelManageService.saveFacts(all.getFacts());
        modelManageService.deleteArrowsByCid(all.getCaseID());
        modelManageService.saveArrows(all.getArrows());
        saveInLogic(all.getLinks(),all.getCaseID());
    }

//    @RequestMapping(value="/saveInLogic")
    public void saveInLogic(HashMap<Integer,List<Integer>> list, int cid){
//        System.out.println("&&&&&&&&&&&");
        logicService.deleteAllLinksBetweenEvidenceAndFactNode(cid);
        for(int bid : list.keySet()){
//            System.out.println("%%"+bid);
            int eid = modelManageService.getLogicNodeIDofBody(bid);
            List<Integer> arr = list.get(bid);
            for(int i = 0;i<arr.size();i++){
                int fid = arr.get(i);
                int factID = modelManageService.getFactByID(fid).getLogicNodeID();
                System.out.println("cid:"+cid+";eid:"+"fid:"+factID);
                logicService.addLinkForEvidenceAndFactNode(cid,eid,factID);
            }
        }
    }

    @RequestMapping(value="/saveArrows")
    public void saveArrows(@RequestBody List<MOD_Arrow> arrows){
        for(int i = 0;i<arrows.size();i++) {
            modelManageService.saveArrow(arrows.get(i));
        }
    }

    @RequestMapping(value="/deleteArrows")
    public void deleteArrows(@RequestParam("cid") int cid){

        modelManageService.deleteArrowsByCid(cid);
    }

    @RequestMapping(value="/exportExcel")
    public ResponseEntity<InputStreamResource> exportExcel(HttpServletRequest request)
            throws IOException {
        String filePath = System.getProperty("user.dir")+"\\src\\main\\resources\\download\\证据链.xls";
        int cid = Integer.parseInt(request.getParameter("cid"));
        modelManageService.writeToExcel(cid,filePath);

        return exportFile(filePath);
    }

    @RequestMapping(value="/exportXML")
    public ResponseEntity<InputStreamResource> exportXML(HttpServletRequest request)
            throws IOException {
        String filePath = System.getProperty("user.dir")+"\\src\\main\\resources\\download\\证据链.xml";
        int cid = Integer.parseInt(request.getParameter("cid"));
//        modelManageService.writeToXML(cid,filePath);
        modelManageService.writeToXMLBySchema(cid,filePath);
        return exportFile(filePath);
    }

    private ResponseEntity<InputStreamResource> exportFile(String filePath)
            throws IOException{
        FileSystemResource fileSystemResource = new FileSystemResource(filePath);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", new String( fileSystemResource.getFilename().getBytes("utf-8"), "ISO8859-1" )));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(fileSystemResource.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(fileSystemResource.getInputStream()));
    }
}
