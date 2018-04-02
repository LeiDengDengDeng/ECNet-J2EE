package com.ecm.controller;

import com.ecm.model.LogicNode;
import com.ecm.service.LogicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.rmi.runtime.Log;

import java.util.List;

/**
 * Created by deng on 2018/3/31.
 */
@RestController
@RequestMapping(value = "/logic")
public class LogicController {
    @Autowired
    private LogicService logicService;

    @RequestMapping(value = "getAll")
    public List<LogicNode> getAll(@RequestParam("caseID") int caseID) {
        return logicService.getAllNodesByCaseID(caseID);
    }

    @PostMapping(value = "add")
    public int add(@RequestBody LogicNode logicNode){
        return logicService.saveNode(logicNode).getId();
    }
}
