package com.ecm.model;

import javax.persistence.*;

/**
 * Created by deng on 2018/4/3.
 */
@Entity
@Table(name="evidence_fact_link")
public class EvidenceFactLink {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    private int initEviNodeID; // 原始证据节点id
    private int copyEviNodeID; // 复制的证据节点id
    private int factNodeID; // 证据节点对应的事实节点id

    public EvidenceFactLink(int initEviNodeID, int copyEviNodeID, int factNodeID) {
        this.initEviNodeID = initEviNodeID;
        this.copyEviNodeID = copyEviNodeID;
        this.factNodeID = factNodeID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInitEviNodeID() {
        return initEviNodeID;
    }

    public void setInitEviNodeID(int initEviNodeID) {
        this.initEviNodeID = initEviNodeID;
    }

    public int getCopyEviNodeID() {
        return copyEviNodeID;
    }

    public void setCopyEviNodeID(int copyEviNodeID) {
        this.copyEviNodeID = copyEviNodeID;
    }

    public int getFactNodeID() {
        return factNodeID;
    }

    public void setFactNodeID(int factNodeID) {
        this.factNodeID = factNodeID;
    }
}
