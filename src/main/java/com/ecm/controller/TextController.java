package com.ecm.controller;

import com.ecm.keyword.model.SplitType_Fact;
import com.ecm.model.*;
import com.ecm.service.LogicService;
import com.ecm.service.ModelManageService;
import com.ecm.service.TextService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value="/text")
public class TextController {

    @Autowired
    private TextService textService;

    @RequestMapping(value="/getTextContent")
    public Text getTextContent(@RequestParam("caseID") int caseID){
        return textService.getText(caseID);
    }
    @RequestMapping(value="/updateTextContent")
    public Text updateTextContent(@RequestParam("caseID") int caseID,@RequestParam("evidence") String evidence,@RequestParam("fact") String fact,@RequestParam("result") String result){
       Text text=new Text(caseID,evidence,fact,result);

        return textService.updateText(text);
    }
    @RequestMapping(value="/exportPDF")
    public ResponseEntity<InputStreamResource> exportExcel(HttpServletRequest request)
            throws IOException {
        String filePath = System.getProperty("user.dir")+"\\src\\main\\resources\\download\\说理语段.pdf";
        int cid = Integer.parseInt(request.getParameter("cid"));
        String evidence= request.getParameter("evidence");
        String fact=request.getParameter("fact");
        String result=request.getParameter("result");
        Text text=new Text(cid,evidence,fact,result);
        textService.writeToPDF(text,filePath);

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
