package com.ecm.model;

import javax.persistence.*;

@Entity
@Table(name="MOD_Sketch")
public class MOD_Sketch {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "caseID")
    private int caseID;
    @Column(name = "bodyID")
    private int bodyID = -1;
    @Column(name = "factID")
    private int factID = -1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCaseID() {
        return caseID;
    }

    public void setCaseID(int caseID) {
        this.caseID = caseID;
    }

    public int getBodyID() {
        return bodyID;
    }

    public void setBodyID(int bodyID) {
        this.bodyID = bodyID;
    }

    public int getFactID() {
        return factID;
    }

    public void setFactID(int factID) {
        this.factID = factID;
    }
}
