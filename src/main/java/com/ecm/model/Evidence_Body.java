package com.ecm.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="EVIDENCE_BODY")
public class Evidence_Body {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ajxh")
    private int caseID;
    @Column(name = "document_id")
    private int documentid;
    @Column(name = "body")
    private String body;//内容
    @Column(name = "type")
    private int type;//0-证人证言，1-被告人供述和辩解，2-书证，3-鉴定结论，4-勘验、-检查笔录，5-其他
    private String name;//链体名称
    private String committer;
    private String reason;
    private int x = -1;//链体x坐标
    private int y = -1;//链体y坐标
    private int isDefendant;//0-原告证据   1-被告证据
    @Column(name = "trust")
    private int trust=1;//0-不采信 1-采信

    @Transient
    private List<Evidence_Head> headList=new ArrayList<>();//持有的head

    public List<Evidence_Head> getHeadList() {
        return headList;
    }

    public void setHeadList(List<Evidence_Head> headList) {
        this.headList = headList;
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

    public String getTrustToString(){
        if(trust==1){
            return "采信";
        }else
            return "不采信";

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
                return "勘验、检查笔录";

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

    public String getCommitter() {
        return committer;
    }

    public void setCommitter(String committer) {
        this.committer = committer;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public int getIsDefendant() {
        return isDefendant;
    }

    public void setIsDefendant(int isDefendant) {
        this.isDefendant = isDefendant;
    }

    public void addHead(Evidence_Head head) {
        headList.add(head);
    }
}
