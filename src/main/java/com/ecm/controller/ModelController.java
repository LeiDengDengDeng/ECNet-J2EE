package com.ecm.controller;

import com.ecm.model.*;
import com.ecm.service.ModelManageService;
import net.sf.json.JSONObject;
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
import java.util.List;

@RestController
@RequestMapping(value="/model")
public class ModelController {
    @Autowired
    private ModelManageService modelManageService;

    @RequestMapping(value="/getEvidences")
    public JSONObject getEvidences(@RequestParam("cid") int cid){

        return modelManageService.getEvidences(cid);
    }

//    @RequestMapping(value="/getJoints")
//    public List<MOD_Joint> getJoints(@RequestParam("cid") int cid){
//
//        return modelManageService.getJoints(cid);
//    }
//
//    @RequestMapping(value="/getArrows")
//    public List<MOD_Arrow> getArrows(@RequestParam("cid") int cid){
//
//        return modelManageService.getArrows(cid);
//    }

    @RequestMapping(value="/saveHeaders")
    public void saveHeaders(@RequestBody List<Evidence_Head> headers){

        modelManageService.saveHeaders(headers);
    }

    @RequestMapping(value="/deleteHeaders")
    public void deleteHeaders(@RequestBody List<Integer> hids){

        for(int i = 0;i<hids.size();i++){
            modelManageService.deleteHeaderById(hids.get(i));
        }
    }

    @RequestMapping(value="/saveBodies")
    public void saveBodies(@RequestBody List<Evidence_Body> bodies){

        modelManageService.saveBodies(bodies);
    }

    @RequestMapping(value="/deleteBodies")
    public void deleteBodies(@RequestBody List<Integer> bids){

        for(int i = 0;i<bids.size();i++){
            modelManageService.deleteBodyById(bids.get(i));
        }
    }

    @RequestMapping(value="/saveJoints")
    public void saveJoints(@RequestBody List<MOD_Joint> joints){

        modelManageService.saveJoints(joints);
    }

    @RequestMapping(value="/deleteJoints")
    public void deleteJoints(@RequestBody List<Integer> jids){

        for(int i = 0;i<jids.size();i++){
            modelManageService.deleteJointById(jids.get(i));
        }
    }

    @RequestMapping(value="/saveFacts")
    public void saveFacts(@RequestBody List<MOD_Fact> facts){

        modelManageService.saveFacts(facts);
    }

    @RequestMapping(value="/deleteFacts")
    public void deleteFacts(@RequestBody List<Integer> fids){

        for(int i = 0;i<fids.size();i++){
            modelManageService.deleteFactById(fids.get(i));
        }
    }

    @RequestMapping(value="/saveArrows")
    public void saveArrows(@RequestBody List<MOD_Arrow> arrows){

        modelManageService.saveArrows(arrows);
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
