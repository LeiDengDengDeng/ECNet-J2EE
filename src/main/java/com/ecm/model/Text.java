package com.ecm.model;

import javax.persistence.*;

@Entity
@Table(name="TEXT")
public class Text {

        @Id
        @GeneratedValue(strategy=GenerationType.IDENTITY)
        @Column(name = "id")
        private int id;
        @Column(name = "cid")
        private int caseID;
        @Column(name = "evidence")
        private String evidence;
        @Column(name = "fact")
        private String fact;
        @Column(name = "result")
        private String result;

    public Text() {
    }

    public Text(int caseID, String evidence, String fact, String result) {
        this.caseID = caseID;
        this.evidence = evidence;
        this.fact = fact;
        this.result = result;
    }

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

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public String getFact() {
        return fact;
    }

    public void setFact(String fact) {
        this.fact = fact;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
