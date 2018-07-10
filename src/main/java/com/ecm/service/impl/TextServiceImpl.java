package com.ecm.service.impl;

import com.ecm.dao.*;
import com.ecm.keyword.manager.HeadCreator;
import com.ecm.keyword.manager.KeyWordCalculator;
import com.ecm.keyword.reader.ExcelUtil;
import com.ecm.model.*;
import com.ecm.model.Text;
import com.ecm.service.EvidenceService;
import com.ecm.service.LogicService;
import com.ecm.service.ModelManageService;
import com.ecm.service.TextService;
import com.ecm.util.ImportXMLUtil;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.kernel.color.Color;

import com.itextpdf.layout.property.HorizontalAlignment;
import net.sf.json.JSONArray;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;

import java.io.IOException;


@Service
public class TextServiceImpl implements TextService {


    @Autowired
    public TextDao textDao;

    @Autowired
    public LogicService logicService;

    @Autowired
    public ModelManageService modelManageService;



    @Override
    public Text getText(int cid) {
        Text text = findTextByCid(cid);
        if (text==null) {
            text=new Text();
            text.setCaseID(cid);
            text.setEvidence("上述事实，有公诉机关及被告人提供，并经法庭质证、认证的下列证据予以证明："+modelManageService.getEvidencesList(cid));
            text.setFact("公诉机关指控："+modelManageService.getFactList(cid));
            text.setResult("本院认为："+logicService.getResultContents(cid));
           return textDao.save(text);
        }else{
            return text;
        }

    }

    @Override
    public Text findTextByCid(int cid) {
        return textDao.getFirstByCaseID(cid);
    }

    @Override
    public Text updateText(Text text) {
        int id = findTextByCid(text.getCaseID()).getId();
        if (id != -1) {
            text.setId(id);
        }
        return textDao.save(text);
    }

    @Override
    public void writeToPDF(Text text, String filePath) throws IOException {

        //1、创建流对象
        PdfWriter pdfWriter=new PdfWriter(new File(filePath));

        //2、创建文档对象
        PdfDocument pdfDocument=new PdfDocument(pdfWriter);

        //3、创建内容文档对象

        Document document=new Document(pdfDocument, PageSize.A4);
        PdfDocumentInfo documentInfo=pdfDocument.getDocumentInfo();
        documentInfo.setCreator("ECM");

        //事实
        Paragraph paragraph=new Paragraph("事实段");

        //设置字体，解决中文显示问题
        PdfFont font= PdfFontFactory.createFont("STSongStd-Light","UniGB-UCS2-H",true);
        paragraph.setFont(font);
        paragraph.setBackgroundColor(Color.LIGHT_GRAY);
        paragraph.setHorizontalAlignment(HorizontalAlignment.CENTER);

        //将内容写入文档

        document.add(paragraph);

        //、创建内容
        paragraph=new Paragraph(text.getFact());

        //设置字体，解决中文显示问题
        font= PdfFontFactory.createFont("STSongStd-Light","UniGB-UCS2-H",true);
        paragraph.setFont(font);
      //  paragraph.setBackgroundColor(Color.LIGHT_GRAY);
        paragraph.setHorizontalAlignment(HorizontalAlignment.CENTER);

        //将内容写入文档

        document.add(paragraph);


        //证据
         paragraph=new Paragraph("证据段");

        //设置字体，解决中文显示问题
        font= PdfFontFactory.createFont("STSongStd-Light","UniGB-UCS2-H",true);
        paragraph.setFont(font);
        paragraph.setBackgroundColor(Color.LIGHT_GRAY);
        paragraph.setHorizontalAlignment(HorizontalAlignment.CENTER);

        //将内容写入文档

        document.add(paragraph);

        //、创建内容
        paragraph=new Paragraph(text.getEvidence());

        //设置字体，解决中文显示问题
        font= PdfFontFactory.createFont("STSongStd-Light","UniGB-UCS2-H",true);
        paragraph.setFont(font);
       // paragraph.setBackgroundColor(Color.LIGHT_GRAY);


        //将内容写入文档

        document.add(paragraph);

        //结果
        paragraph=new Paragraph("裁判分析段");

        //设置字体，解决中文显示问题
        font= PdfFontFactory.createFont("STSongStd-Light","UniGB-UCS2-H",true);
        paragraph.setFont(font);
        paragraph.setBackgroundColor(Color.LIGHT_GRAY);
        paragraph.setHorizontalAlignment(HorizontalAlignment.CENTER);

        //将内容写入文档

        document.add(paragraph);

        //、创建内容
        paragraph=new Paragraph(text.getResult());
        //设置字体，解决中文显示问题
        font= PdfFontFactory.createFont("STSongStd-Light","UniGB-UCS2-H",true);
        paragraph.setFont(font);
      //  paragraph.setBackgroundColor(Color.LIGHT_GRAY);


        //将内容写入文档

        document.add(paragraph);

        document.close();

        System.out.println("ok!!!");

    }
}