package com.ecm.model;

import javax.persistence.*;

@Entity
@Table(name="MOD_JOINT")
//@IdClass(MODPK.class)
public class MOD_Joint {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
//    @Id
    private int caseID;
    private String name;
    private String content;
    private int x;
    private int y;
    private int factID = -1;
    private String type;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getFactID() {
        return factID;
    }

    public void setFactID(int factID) {
        this.factID = factID;
    }
}
