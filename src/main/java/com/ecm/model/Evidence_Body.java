package com.ecm.model;

import javax.persistence.*;

@Entity
@Table(name="EVIDENCE_BODY")
public class Evidence_Body {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ajxh")
    private int case_id;
    @Column(name = "document_id")
    private int documentid;
    @Column(name = "body")
    private String body;//内容
    @Column(name = "type")
    private int type;//0-证人证言，1-被告人供述和辩解，2-书证，3-鉴定结论，4-勘验、-检查笔录，5-其他
    private String name;//链体名称
//    private String committer;
    private String reason;
    private String conclusion;
    private int x = -1;//链体x坐标
    private int y = -1;//链体y坐标

    @Column(name = "trust")
    private int trust=1;//0-不采信 1-采信

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCase_id() {
        return case_id;
    }

    public void setCase_id(int case_id) {
        this.case_id = case_id;
    }

    public int getDocumentid() {
        return documentid;
    }

    public void setDocumentid(int document_id) {
        this.documentid = document_id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTrust() {
        return trust;
    }

    public void setTrust(int trust) {
        this.trust = trust;
    }

    public String getTypeToString(){
        switch(type)
        {
            case 0:
                return "证人证言";

            case 1:
                return "被告人供述和辩解";

            case 2:
                return "书证";

            case 3:
                return "鉴定结论";

            case 4:
                return "勘验、-检查笔录";

            default:
                return "其他";

        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public String getCommitter() {
//        return committer;
//    }
//
//    public void setCommitter(String committer) {
//        this.committer = committer;
//    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
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
}
