package com.ecm.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by deng on 2018/4/2.
 */
@Data
public class LogicNodeMaxValue implements Serializable{
    private int maxY;
    private int maxNodeID;

    public LogicNodeMaxValue(int maxY, int maxNodeID) {
        this.maxY = maxY;
        this.maxNodeID = maxNodeID;
    }
}
