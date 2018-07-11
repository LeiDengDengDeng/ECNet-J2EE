package com.ecm.service;

import com.ecm.model.Evidence_Body;
import com.ecm.model.Evidence_Document;
import com.ecm.model.Evidence_Head;
import com.ecm.model.Text;
import com.ecm.util.ImportXMLUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface TextService {

    Text getText(int cid);
    Text findTextByCid(int cid);
    Text updateText(Text text);
    void writeToWord(Text text, String filePath) throws IOException;

    void writeToPDF(Text text, String filePath) throws IOException;
}
