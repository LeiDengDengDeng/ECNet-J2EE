package com.ecm.service.impl;

import com.ecm.dao.EvidenceFactLinkDao;
import com.ecm.dao.LogicNodeDao;
import com.ecm.model.EvidenceFactLink;
import com.ecm.model.LogicNode;
import com.ecm.model.LogicNodeMaxValue;
import com.ecm.service.LogicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by deng on 2018/3/28.
 */
@Service
public class LogicServiceImpl implements LogicService {
    @Autowired
    private LogicNodeDao logicNodeDao;
    @Autowired
    private EvidenceFactLinkDao evidenceFactLinkDao;

    @Override
    public List<LogicNode> getAllNodesByCaseID(int caseID) {
        List<LogicNode> logicNodes = logicNodeDao.findByCaseID(caseID);
        return logicNodes;
    }

    public int addEvidenceOrFactNode(int caseID, String detail, int type) {
        if (type != 0 && type != 1) {
            return -1;
        }

        LogicNodeMaxValue maxValue = getLogicNodeMaxValue(caseID);

        int nodeID = maxValue.getMaxNodeID() + 1;
        int parentNodeID = -1;
        String topic = type == 0 ? "证据" + nodeID : "事实" + nodeID;
        int x = 80;
        int y = maxValue.getMaxY() + 50;

        LogicNode node = new LogicNode(caseID, nodeID, parentNodeID, topic, detail, type, x, y);
        return logicNodeDao.save(node).getId();
    }

    @Override
    public void modEvidenceOrFactNode(int id, String detail) {
        LogicNode node = getNode(id);
        node.setDetail(detail);
        logicNodeDao.save(node);
    }

    @Override
    public void addLinkForEvidenceAndFactNode(int evidenceNodeID, int factNodeID) {
        LogicNode eviNode = getNode(evidenceNodeID);
        if (eviNode.getParentNodeID() == -1) {
            // 证据节点不存在父事实节点，修改该节点的parentNodeID信息
            eviNode.setParentNodeID(factNodeID);
            logicNodeDao.save(eviNode);
        } else {
            // 证据节点存在父事实节点，需要copy新的证据节点，并将新的证据节点的parentNodeID设置为factNodeID
            LogicNodeMaxValue maxValue = getLogicNodeMaxValue(eviNode.getCaseID());
            LogicNode copyEviNode = new LogicNode(eviNode.getCaseID(), maxValue.getMaxNodeID() + 1, factNodeID, eviNode.getTopic(), eviNode.getDetail(), eviNode.getType(), 80, maxValue.getMaxY() + 50);
            copyEviNode = logicNodeDao.save(copyEviNode);

            EvidenceFactLink eviFactLink = new EvidenceFactLink(evidenceNodeID, copyEviNode.getId(), factNodeID);
            evidenceFactLinkDao.save(eviFactLink);
        }
    }

    @Override
    public void deleteLinkForEvidenceAndFactNode(int evidenceNodeID, int factNodeID) {
        LogicNode eviNode = getNode(evidenceNodeID);
        if (eviNode.getParentNodeID() == factNodeID) {
            eviNode.setParentNodeID(-1);
            logicNodeDao.save(eviNode);
        } else {
            // 删除copy节点以及copy节点和事实节点的联系
            EvidenceFactLink eviFactLink = evidenceFactLinkDao.findByInitEviNodeIDAndFactNodeID(evidenceNodeID, factNodeID);
            logicNodeDao.delete(eviFactLink.getCopyEviNodeID());
            evidenceFactLinkDao.deleteByInitEviNodeIDAndFactNodeID(evidenceNodeID, factNodeID);
        }
    }

    @Override
    public LogicNode getNode(int id) {
        return logicNodeDao.findById(id);
    }

    @Override
    @Transactional
    public void saveAllNodesInSameCase(int caseID, List<LogicNode> logicNodes) {
        logicNodeDao.deleteByCaseID(caseID);
        for (LogicNode node : logicNodes) {
            logicNodeDao.save(node);
        }
    }

    @Override
    public LogicNode saveOrUpdateNode(LogicNode node) {
        return logicNodeDao.save(node);
    }

    @Override
    @Transactional
    public void deleteNode(int id) {
        logicNodeDao.deleteById(id);
    }

    private LogicNodeMaxValue getLogicNodeMaxValue(int caseID) {
        LogicNodeMaxValue maxValue;
        try {
            maxValue = logicNodeDao.getLogicNodeMaxValueByCaseID(caseID);
        } catch (org.springframework.dao.InvalidDataAccessApiUsageException e) {
            // 对应案件不存在节点时
            maxValue = new LogicNodeMaxValue(0, 0);
        }
        return maxValue;
    }
}
