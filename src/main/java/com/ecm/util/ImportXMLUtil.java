package com.ecm.util;

import java.io.IOException;
import java.util.ArrayList;
        import java.util.List;

        import javax.xml.parsers.DocumentBuilder;
        import javax.xml.parsers.DocumentBuilderFactory;
        import javax.xml.parsers.ParserConfigurationException;

import com.ecm.model.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;


/**
 * 用DOM方式读取xml文件
 * @author lune
 */
public class ImportXMLUtil {
    private  DocumentBuilderFactory dbFactory = null;
    private  DocumentBuilder db = null;
    private  Document document = null;
    private  int caseId=-1;

    private List<Evidence_Document> documentList = new ArrayList<>();

    private List<Evidence_Body> bodyList = new ArrayList<>();

    private List<MOD_Fact> factList = new ArrayList<>();

    private List<LogicNode> logicNodeList=new ArrayList<>();

    public ImportXMLUtil(String fileName,int caseId){
        try {
            dbFactory = DocumentBuilderFactory.newInstance();
            db = dbFactory.newDocumentBuilder();
            //将给定 URI 的内容解析为一个 XML 文档,并返回Document对象
            document = db.parse(fileName);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        this.caseId=caseId;

    }
    public  List<Evidence_Document> getDocuments(){
        //按文档顺序返回包含在文档中且具有给定标记名称的所有 Element 的 NodeList
        Node root = document.getElementsByTagName("documents").item(0);
        documentList=new ArrayList<>();
        //遍历
        NodeList list=root.getChildNodes();
        for(int i=0;i<list.getLength();i++) {

            //获取第i个book结点
            Node node = list.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Element evi = (Element) node;
               // System.out.println(evi.getTagName());
                Evidence_Document evidence_document = new Evidence_Document();

                //获取第i个book的所有属性
                NamedNodeMap namedNodeMap = evi.getAttributes();
                //获取已知名为id的属性值
                //System.out.println(namedNodeMap.getLength());
                int id = Integer.parseInt(namedNodeMap.getNamedItem("id").getTextContent());
                int type = Integer.parseInt(namedNodeMap.getNamedItem("type").getTextContent());
                evidence_document.setId(id);
                evidence_document.setCaseID(caseId);
                evidence_document.setType(type);

                //获取book结点的子节点,包含了Test类型的换行
                String text = evi.getElementsByTagName("text").item(0).getTextContent();
                evidence_document.setText(text);
                String committer = evi.getElementsByTagName("committer").item(0).getTextContent();
                evidence_document.setCommitter(committer);

                documentList.add(evidence_document);
            }
        }

        return documentList;
    }


    public  List<Evidence_Body> getEvidences(){
        //按文档顺序返回包含在文档中且具有给定标记名称的所有 Element 的 NodeList
        Node root = document.getElementsByTagName("evidences").item(0);
        bodyList=new ArrayList<>();
        //遍历
        NodeList list=root.getChildNodes();
        for(int i=0;i<list.getLength();i++) {
            //获取第i个book结点
            Node node = list.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element evi = (Element) node;
                int id=Integer.valueOf(evi.getAttribute("id"));
                int x=Integer.valueOf(evi.getAttribute("x"));
                int y=Integer.valueOf(evi.getAttribute("y"));
                int documentId=Integer.valueOf(evi.getAttribute("documentId"));
                int type=Integer.valueOf(evi.getAttribute("type"));
                int trust=Integer.valueOf(evi.getAttribute("trust"));
                int logicNodeId=Integer.valueOf(evi.getAttribute("logicNodeId"));

                Evidence_Body evidence_body=new Evidence_Body();



                String name=evi.getElementsByTagName("name").item(0).getTextContent();
                String content=evi.getElementsByTagName("content").item(0).getTextContent();
               // String typeString=evi.getElementsByTagName("type").item(0).getTextContent();
                String committer=evi.getElementsByTagName("committer").item(0).getTextContent();
              //  String trustString=evi.getElementsByTagName("trust").item(0).getTextContent();
                String reason=evi.getElementsByTagName("reason").item(0).getTextContent();

                evidence_body.setId(id);
                evidence_body.setDocumentid(documentId);
                evidence_body.setLogicNodeID(logicNodeId);
                evidence_body.setTrust(trust);
                evidence_body.setType(type);
                evidence_body.setX(x);
                evidence_body.setY(y);
                evidence_body.setName(name);
                evidence_body.setBody(content);
                evidence_body.setCommitter(committer);
                evidence_body.setReason(reason);





                NodeList heads=evi.getElementsByTagName("heads").item(0).getChildNodes();

                for(int j=0;i<heads.getLength();j++){
                    Element headNode= (Element) heads.item(j);
                    int headid=Integer.valueOf(headNode.getAttribute("id"));
                    int headx=Integer.valueOf(evi.getAttribute("x"));
                    int heady=Integer.valueOf(evi.getAttribute("y"));
                    String headname=evi.getElementsByTagName("name").item(0).getTextContent();
                    String headcontent=evi.getElementsByTagName("content").item(0).getTextContent();

                    Evidence_Head head=new Evidence_Head();
                    head.setId(headid);
                    head.setDocumentid(documentId);
                    head.setHead(headcontent);
                    head.setBodyid(id);
                    head.setCaseID(caseId);
                    head.setName(headname);
                    head.setX(headx);
                    head.setY(heady);
                    evidence_body.addHead(head);
                }

                bodyList.add(evidence_body);


            }
        }

        return bodyList;
    }




    public static void main(String args[]){
        String fileName ="upload.xml";
        ImportXMLUtil importXMLUtil=new ImportXMLUtil(fileName,41722);
        try {
            List<Evidence_Document> list =importXMLUtil.getDocuments();
            for(Evidence_Document document :list){
                System.out.println(document.toString());
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}