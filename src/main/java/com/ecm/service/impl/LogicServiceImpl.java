package com.ecm.service.impl;

import com.ecm.dao.LogicNodeDao;
import com.ecm.model.LogicNode;
import com.ecm.model.LogicNodeMaxValue;
import com.ecm.service.LogicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by deng on 2018/3/28.
 */
@Service
public class LogicServiceImpl implements LogicService {
    @Autowired
    private LogicNodeDao logicNodeDao;

    @Override
    public List<LogicNode> getAllNodesByCaseID(int caseID) {
        List<LogicNode> logicNodes = logicNodeDao.findByCaseID(caseID);
        return logicNodes;
    }

    public int addEvidenceOrFactNode(int caseID, String detail, int type) {
        if (type != 0 && type != 1) {
            return -1;
        }

        LogicNodeMaxValue maxValue;
        try {
            maxValue = logicNodeDao.getLogicNodeMaxValueByCaseID(caseID);
        } catch (org.springframework.dao.InvalidDataAccessApiUsageException e) {
            // 对应案件不存在节点时
            maxValue = new LogicNodeMaxValue(0, 0);
        }

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
        LogicNode node = logicNodeDao.findById(id);
        node.setDetail(detail);
        logicNodeDao.save(node);
    }

    @Override
    public LogicNode getNode(int id) {
        return logicNodeDao.findById(id);
    }

    @Override
    public LogicNode saveNode(LogicNode node) {
        return logicNodeDao.save(node);
    }

    @Override
    public void deleteNode(int id) {
        logicNodeDao.deleteById(id);
    }
}
