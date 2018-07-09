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

}
