package com.ecm.service;

import com.ecm.model.LogicNode;

import java.util.List;

/**
 * Created by deng on 2018/3/28.
 */
public interface LogicService {
    List<LogicNode> getAllNodesByCaseID(int caseID);

    int addEvidenceOrFactNode(int caseID, String detail, int type);

    int addNode(int caseID, int parentNodeID, String detail, int type);

    /**
     * 修改节点的detail信息
     *
     * @param id
     * @param detail
     */
    void modEvidenceOrFactNode(int id, String detail);

    void addLinkForEvidenceAndFactNode(int evidenceNodeID, int factNodeID);

    void deleteLinkForEvidenceAndFactNode(int evidenceNodeID, int factNodeID);

    LogicNode getNode(int id);

    /**
     * 保存案件的所有节点，保存前先清空所有节点
     *
     * @param caseID
     * @param logicNodes
     */
    void saveAllNodesInSameCase(int caseID, List<LogicNode> logicNodes);

    void deleteNode(int id);

    String generateXMLFile(int caseID);

    String generateExcelFile(int caseID);
}
