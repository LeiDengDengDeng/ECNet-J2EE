package com.ecm.service;

import com.ecm.model.LogicNode;

import java.util.List;

/**
 * Created by deng on 2018/3/28.
 */
public interface LogicService {
    List<LogicNode> getAllNodesByCaseID(int caseID);

    int addEvidenceOrFactNode(int caseID, String detail, int type);

    LogicNode saveNode(LogicNode node);

    void deleteNode(int id);
}
