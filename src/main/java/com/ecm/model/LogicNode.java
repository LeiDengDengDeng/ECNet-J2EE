package com.ecm.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by deng on 2018/3/28.
 */
@Data
@Entity
@Table(name = "logic_node")
public class LogicNode {
    @Id
    private int id;
    private int caseID;

    private int nodeID;
    private int parentNodeID; // 若不存在，则设置为-1
    private String topic;
    private String detail;
    private int type; // 0-证据,1-事实,2-法条,3-结论
    private int x; // 画布上的x坐标
    private int y;

    public LogicNode() {
    }

    public LogicNode(int caseID, int nodeID, int parentNodeID, String topic, String detail, int type, int x, int y) {
        this.caseID = caseID;
        this.nodeID = nodeID;
        this.parentNodeID = parentNodeID;
        this.topic = topic;
        this.detail = detail;
        this.type = type;
        this.x = x;
        this.y = y;
    }
}
